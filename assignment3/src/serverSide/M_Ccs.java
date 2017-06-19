package serverSide;
import Datastructures.*;
import interfaces.*;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import registry.RegistryConfiguration;
/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_Ccs implements ItfCcs{
    private VectorClock clock;
    private String reghostname;
    /**
     * aux method that checks the group of a thief
     * @param thiefID if of thief
     * @return the group where that thief belongs
     */
    int whereIBelong(int thiefID){
        for(int i = 0 ; i<aps.length;i++){
            for(int j=0;j<aps[i].num;j++){
                if(aps[i].thieves[j] == thiefID)
                    return i;
            }
        }
        //nunca chega aqui
        return -1;
    }

    /**
     * delivery of the canvas, if last thief also wakes the masterthief
     * @param hasPainting flag that indicates wether thief has painting
     * @param thiefID the id of the thief handing the canvas
     * @return the return struct containing the updated clock
     */
    @Override
    public synchronized ReturnStruct handACanvas(boolean hasPainting, int thiefID, VectorClock clk) {
        this.clock.update(clk);
        arrived++;
        if(hasPainting){
            int belong = whereIBelong(thiefID);
            nPaintings[belong]++;
        }
        if(arrived==Variables.M)
           notifyAll();
        ReturnStruct rt = new ReturnStruct(clock);
        return rt;
    }

    /**
     * total paintings stolen so far
     */
    int sum;
    /**
     * private class that implements a struct containing
     * important assault party information
     */
    private class AP{
        int[] thieves = new int[Variables.M];
        int num=0;
        int tgt = -1;
        boolean onAssault=false;
    }
    /**
     * Assault parties
     */
    private AP[] aps = new AP[Variables.NAP];
    /**
     * total stolen paintings
     */
    int nPaintings[] = new int[Variables.NAP];
    int arrived = 0;
    
    /**
     * struct that contains all important room information
     */
    private class MTRoom{
        boolean targeted = false;
        boolean empty = false;
        int ap = -1;
    }
    private MTRoom[] rooms = new MTRoom[Variables.N];
    
    /**
     * group target setter
     * @param roomID the id of the room
     * @param APidx the index of the group
     */
    public void setTarget(int roomID, int APidx){
        this.aps[APidx].tgt = roomID;
    }

    /**
     * The start of operations initializes some variables
     * @return the return struct with the updated clock
     */
    @Override
    public synchronized ReturnStruct startOperations(VectorClock clk) {
        this.clock.update(clk);
        for(int i=0;i<Variables.N;i++)
            rooms[i] = new MTRoom();
        
        for(int i=0;i<Variables.NAP;i++){
            aps[i] = new AP();
        }
        ReturnStruct rt = new ReturnStruct(clock);
        return rt;
    }

    /**
     * Auxiliary method that informs if the assault parties are full
     * @return the first group that is not full or -1 if all full
     */

    public int groupsFull(){
        for(int i = 0; i< aps.length ; i++){
            if(aps[i].num != Variables.NTAP)
                return i;
        }
        return -1;
    }

    /**
     * Auxiliary method that checks if a group is full
     * @param groupID the id of the group to check
     * @return whether the group is full or not
     */
    public synchronized boolean groupFull(int groupID){
        return aps[groupID].num == Variables.M;
    }
    
    

    /**
     * method that checks if there is any party with an ongoing assault
     * @return true if there is, else false
     */
    public synchronized boolean onAssault(){
        for(AP ap : aps){
           if(ap.onAssault)
               return true;
        }
        return false;
    }

    private ItfLogger logger;

    /**
     * control and collection site constructor
     * @param logger logger intance
     * @param reghostname the registry hostname
     */
    public M_Ccs(ItfLogger logger, String reghostname){
        this.reghostname = reghostname;
        this.logger = logger;
        this.clock = new VectorClock(0);
    }
    
    /**
     * aux method that adds a thief to a Ccs group( != assaultparty)
     * @param thiefID id of the thief
     * @param clk the clock
     * @return target group
     * @throws RemoteException because rmi
     */
    public synchronized int joinGroup(int thiefID, VectorClock clk) throws RemoteException{
        int tgt = groupsFull();
        if(tgt == -1)
            return -1;
        aps[tgt].thieves[aps[tgt].num] = thiefID;
        aps[tgt].num++;
        logger.OTLog(Variables.logger.S, thiefID, 1, clk);
        return tgt;
    }

    
    


    /**
     * @param over if all rooms are empty
     * @param clk the clock
     * @return the return struct containing the updated clock and the decision
     */
    @Override
    public synchronized ReturnStruct appraiseSit(boolean over, VectorClock clk) {
        this.clock.update(clk);
        if(over)
            return  new ReturnStruct(clock, 3);
        if(!onAssault())
            return  new ReturnStruct(clock, 1);
        else if(onAssault()){
            return  new ReturnStruct(clock,2);
        }
        else return  new ReturnStruct(clock, 0);
    }
    
    /**
     * waits for the groups to be full and then sends the thieves
     * to the assault
     * @param clk the clock
     * @return the return struct containing the updated clock
     */
    @Override
    public synchronized ReturnStruct sendAssaultParty(VectorClock clk) {
        this.clock.update(clk);
        while (groupsFull()!=-1) {
            try {
                wait(); // espera que os ladroes entrem nos grupos
            } catch (Exception e) {
            }
        }
        for(AP ap : aps){
            ap.onAssault = true;
            
        }
        arrived = 0;
        nPaintings = new int[Variables.NAP];
        notifyAll();
        return new ReturnStruct(clock);
    }

    /**
     * blocks the masterthief waiting for all the groups to arrive
     * @param clk the clock
     * @return the return struct with the updated clock
     */
    @Override
    public synchronized ReturnStruct takeARest(VectorClock clk) {
        this.clock.update(clk);
        while(arrived != Variables.M){
            try{
                wait();
            }catch(Exception e){
                
            }
        }
        return new ReturnStruct(clock);
    }

    /**
     * method called after all the thieves returned
     * collects all the canvas and resets the groups for the next
     * assault to start
     * @param clk the clock
     * @return the return struct
     */
    @Override
    public synchronized ReturnStruct collectCanvas(VectorClock clk) {
        this.clock.update(clk);
        boolean flag[];
        flag = new boolean[Variables.NAP];
        for(int i=0;i<Variables.NAP;i++){
            sum += nPaintings[i];
            if(nPaintings[i] != Variables.NTAP){
                flag[i] = true;
            }
        }
        for(int i =0 ;i<aps.length;i++){
            aps[i] = new AP();
        }
        return new ReturnStruct(clock, flag);
    }

    /**
     * Returns the total paintings stolen from the museum
     * @param clk the clock
     * @return the return struct containing the results and the updated clock
     */
    @Override
    public synchronized ReturnStruct sumUpResults(VectorClock clk) {
        this.clock.update(clk);
        return new ReturnStruct(clock, sum);
    }

    /**
     * method that indicates the assault party for the thief and blocks it
     * while he waits for the theft to start
     * 
     * @param thiefID the id of the thief
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return the return struct containing the updated clock and
     * the assault party that will incorporate the thief for the
     * next excursion
     */
    @Override
    public synchronized ReturnStruct prepareExcursion(int thiefID, VectorClock clk) throws RemoteException {
        this.clock.update(clk);
        int ret = joinGroup(thiefID, clk);
        // return must be != -1 and will always be
        notifyAll();
        while (!this.aps[ret].onAssault) {
            try {
                wait(); // espera que os ladroes entrem nos grupos
            } catch (Exception e) {
            }
        }
        return new ReturnStruct(clock, ret);
    }

         /**
     * Shutsdown the monitor and deregisters on rmi registry
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
            java.util.logging.Logger.getLogger(M_Ccs.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfiguration.REGISTRY_CONTROL_COLLECTION_SITE_NAME ;

        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Ccs.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Ccs.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("M_Ccs registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Ccs.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("M_Ccs not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Ccs.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(M_Ccs.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("M_Ccs shutdown.");
    }
    
    
}
