package serverSide;

import Datastructures.*;
import interfaces.*;
import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import registry.RegistryConfiguration;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Logger implements ItfLogger {

    private boolean toTerm;
    private String states = "";
    private Room rooms[] = new Room[Variables.N];
    private OT ots[] = new OT[Variables.M];
    private MT mt = new MT();
    private AP aps[] = new AP[Variables.NAP];
    private static final String FILENAME = "log.txt";
    private String lastStateLog = "";
    private String lastAPLog = "";
    private VectorClock clock;
    private String reghostname;
    /**
     * constructor of the Logger monitor,
     * it requires the registry hostname
     * @param reghostname the registry hostname
     */
    public Logger(String reghostname) {
        for (int i = 0; i < rooms.length; i++) {//init rooms
            rooms[i] = new Room();
        }
        for (int i = 0; i < ots.length; i++) {//init ots
            ots[i] = new OT();
        }
        for (int i = 0; i < aps.length; i++) {//init aps
            aps[i] = new AP();
        }
        toTerm = false;
        clock = new VectorClock(0);
        this.reghostname = reghostname;
    }

    /**
     * method to append the caption to the log file
     */
    public void LogEnd() {
        String output;
        output = "Legend: \n";
        output += "MstT Stat    – state of the master thief \n";
        output += String.format("Thief # Stat - state of the ordinary thief # (# - 1 .. %d) \n", Variables.M);
        output += String.format("Thief # S    – situation of the ordinary thief # (# - 1 .. %d) either 'W' (waiting to join a party) or 'P' (in party) \n", Variables.M);
        output += String.format("Thief # MD   – maximum displacement of the ordinary thief # (# - 1 .. %d) a random number between %d and %d \n", Variables.M, Variables.MD[0], Variables.MD[1]);
        output += String.format("Assault party # RId        – assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5) \n");
        output += String.format("Assault party # Elem # Id  – assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6) \n");
        output += String.format("Assault party # Elem # Pos – assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)\n");
        output += String.format("Assault party # Elem # Cv  – assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1) \n");
        output += String.format("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls \n");
        output += String.format("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30\n");
        output+=String.format("VCk  0       - local clock of the master thief\n");
        output+=String.format("VCk  1       - local clock of the ordinary thief 1\n");
        output+=String.format("VCk  2       - local clock of the ordinary thief 2\n");
        output+=String.format("VCk  3       - local clock of the ordinary thief 3\n");
        output+=String.format("VCk  4       - local clock of the ordinary thief 4\n");
        output+=String.format("VCk  5       - local clock of the ordinary thief 5\n");
        output+=String.format("VCk  6       - local clock of the ordinary thief 6\n");
        if (toTerm) {
            out.print(output);
        } else {
            try {
                Files.write(Paths.get(FILENAME), output.getBytes(), APPEND);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * method to append the header to the log file
     */
    private void LogHeader() {
        String output;
        output="                             Heist to the Museum - Description of the internal state\n";
        output+="MstT   Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6                VCk\n";
        output+="Stat  Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    0   1   2   3   4   5   6 \n";
        output+="                   Assault party 1                       Assault party 2                       Museum\n";
        output+="           Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5\n";
        output+="    RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT\n";
        if (toTerm) {
            out.print(output);
        } else {
            try {
                Files.write(Paths.get(FILENAME), output.getBytes());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * method to write the log to the file
     *
     * @param total the total number of paintings stolen
     */
    private void LogTotal(int total) {
        String output = String.format("My friends, tonight's effort produced %2d priceless paintings!\n\n", total);
        if (toTerm) {
            out.print(output);
        } else {
            try {
                Files.write(Paths.get(FILENAME), output.getBytes(), APPEND);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * method to return the index of a thief in a party
     *
     * @param AP assault party index
     * @param id thief id
     * @return index
     */
    private int getElemIdx(int AP, int id) {
        for (int i = 0; i < aps[AP].elems.size(); i++) {
            if (aps[AP].elems.get(i).Id == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * method used by the assault party to log
     *
     * @param kind kind of parameter being logged
     * @param APid id of the assault party
     * @param Eid id of the thief
     * @param val value of the parameter
     */
    @Override
    public synchronized ReturnStruct APLog(int kind, int APid, int Eid, int val, VectorClock clk) {
        this.clock.update(clk);
        switch (kind) {
            case Variables.logger.RId://mudou de sala
                aps[APid] = new AP();
                aps[APid].Rid = val;
                //set all to empty
                aps[APid].elems.clear();
                break;
            case Variables.logger.Id:
                Elem e = new Elem();
                e.Id = val;
                //e.Cv =-1;
                //e.Pos=-1;
                aps[APid].elems.add(e);
                break;
            case Variables.logger.Pos:
                aps[APid].elems.get(getElemIdx(APid, Eid)).Pos = val;
                break;
            case Variables.logger.Cv:
                aps[APid].elems.get(getElemIdx(APid, Eid)).Cv = val;
                break;
        }
        //if called and complete update log
        APLogged();
        return new ReturnStruct(clock);
    }

    /**
     * method used by the ordinary thief to log
     *
     * @param kind kind of parameter being logged
     * @param Tid id of the thief
     * @param val value of the parameter
     */
    @Override
    public synchronized ReturnStruct OTLog(int kind, int Tid, int val, VectorClock clk) {
        this.clock.update(clk);
        switch (kind) {
            case Variables.logger.Stat:
                ots[Tid].Stat = val;
                break;
            case Variables.logger.S:
                ots[Tid].S = val;
                break;
            case Variables.logger.MD:
                ots[Tid].MD = val;
                break;
        }
        //if called and complete update log
        TLogged();
        return new ReturnStruct(clock);
    }

    /**
     * method used by the master thief to log
     *
     * @param val value of the status
     */
    @Override
    public synchronized ReturnStruct MTLog(int val, VectorClock clk) {
        this.clock.update(clk);
        //System.out.println("mt: "+val);
        mt.Stat = val;
        //if called and complete update log
        TLogged();
        return new ReturnStruct(clock);
    }

    /**
     * method used by the museum to log
     *
     * @param kind kind of parameter being logged
     * @param Rid id of the room
     * @param val value of the parameter
     */
    @Override
    public synchronized ReturnStruct MuseumLog(int kind, int Rid, int val, VectorClock clk) {
        this.clock.update(clk);
        switch (kind) {
            case Variables.logger.NP:
                rooms[Rid].NP = val;
                break;
            case Variables.logger.DT:
                rooms[Rid].DT = val;
                break;
        }
        return new ReturnStruct(clock);
    }

    /**
     * method used to log the report
     *
     * @param total_paint the total number of paintings stolen
     */
    @Override
    public synchronized ReturnStruct finalReport(int total_paint, VectorClock clk) {
        this.clock.update(clk);
        LogHeader();
        LogStates();
        LogTotal(total_paint);
        LogEnd();
        return new ReturnStruct(clock);
    }

    /**
     * method used to log the states string to the output
     *
     */
    private synchronized void LogStates() {
        if (toTerm) {
            out.println(states);
        } else {
            try {
                Files.write(Paths.get(FILENAME), states.getBytes(), APPEND);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * method used to return the rooms in string format
     *
     * @return String rooms
     */
    private String getRooms() {
        String ret = "";
        int i = 0;
        for (Room r : rooms) {
            if (i < 5) {
                ret += r.toString() + "   ";
            }
            i++;
        }
        return ret;
    }

    /**
     * method used to return the thieves in string format
     *
     * @return String thieves
     */
    private String getOTs(){
        String ret="";
        int i=0;
        for(OT ot : ots){
            if(i<5)
                ret+=ot.toString()+"    ";
            else if(i==5)
                ret+=ot.toString();
            i++;
        }
        return ret;
    }

    /**
     * method called when an assault party logs a parameter
     *
     */
    private synchronized void APLogged() {

        //check all AP have room
        //check all OT are completed
        if (allAPlogged()) {
            String str = getAPs() + "" + getRooms();
            if (!lastAPLog.equals(str)) {
                states += str + "\n";
                lastAPLog = str;
            }
        }

    }

    /**
     * method used to verify if all assault parties logged
     *
     * @return boolean true|false
     */
    private boolean allAPlogged() {
        for (int i = 0; i < aps.length; i++) {
            if (aps[i].Rid == -1 || aps[i].elems.size() != 3) {
                return false;
            }
            for (Elem e : aps[i].elems) {
                if (e.Cv == -1 || e.Id == -1 || e.Pos == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * method called when an thief logs a parameter
     *
     */
    private synchronized void TLogged() {

        if (mt.Stat != -1 && allThievesLogged()) {
            String str = mt + getOTs() + getVCK();
            if (!lastStateLog.equals(str)) {
                states += str + "\n";
                lastStateLog = str;
            }
        }
    }
    /**
     * method used to return the thieves VCk
     * @return String vck
     */
    private String getVCK(){
        String ret="";
        ret+=String.format("%6d",clock.getRelogio()[Variables.M]);
        for(int i=0 ; i<clock.getRelogio().length-1;i++){
            ret+=String.format("%4d",clock.getRelogio()[i]);       
        }
        return ret;
    }
    
    /**
     * method used to verify if all thieves logged
     *
     * @return boolean true|false
     */
    private boolean allThievesLogged() {
        for (OT ot : ots) {
            if (ot.MD == -1 || ot.S == -1 || ot.Stat == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * method used to return the assault parties in string format
     *
     * @return String assault parties
     */
    private String getAPs() {
        String ret = "";
        for (int i = 0; i < aps.length && i < 2; i++) {
            ret += aps[i].toString(i == 1);
        }
        return ret;
    }

    private class Room {

        int DT, NP;

        @Override
        public String toString() {
            return String.format("%2d %2d", NP, DT);
        }

    }

    private class OT {

        private int Stat, S, MD;

        public OT() {
            Stat = -1;
            S = -1;
            MD = -1;
        }

        @Override
        public String toString() {
            String str = "";
            if (S == 0) {
                str = "W";
            } else {
                str = "P";
            }
            return String.format("%4d %1s %2d", Stat, str, MD);
        }
    }

    private class MT {

        private int Stat;

        public MT() {
            Stat = -1;
        }

        @Override
        public String toString() {
            return String.format("%4d  ", Stat);
        }
    }

    private class Elem {

        private int Id, Pos, Cv;

        public Elem() {
            Id = -1;
            Pos = -1;
            Cv = -1;
        }

        @Override
        public String toString() {
            return String.format("%1d  %2d  %1d ", Id + 1, Pos, Cv);
        }
    }

        /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    @Override
    public void shutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;
        
        rmiRegHostName = reghostname;
        rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject;
        nameEntryObject = RegistryConfiguration.REGISTRY_LOGGER_NAME ;
        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Logger registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Logger not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Logger shutdown.");
    }
    
    
    private class AP {

        private ArrayList<Elem> elems = new ArrayList<Elem>();
        private int Rid;

        public AP() {
            Rid = -1;
        }

        public String toString(boolean sec) {
            String s = "";
            for (Elem e : this.elems) {
                s += e + "  ";
            }
            if (!sec) {
                return String.format("     %1d    ", Rid + 1) + s;
            } else {
                return String.format("%1d    ", Rid + 1) + s;
            }
        }
    }
}
