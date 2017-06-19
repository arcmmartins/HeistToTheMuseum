
package serverSide;
import Datastructures.*;
import interfaces.*;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import registry.RegistryConfiguration;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_AssaultParty implements ItfTAssaultParty{
    private VectorClock clock;
    private String reghostname;
    
    /**
     * Returns the ID of the Assault_Party
     * @param clock the vector clock
     * @return the return struct containg the updates clock and the ID of the Assault_Party
     */
    public ReturnStruct getID(VectorClock clock) {
        this.clock.update(clock);
        ReturnStruct rt = new ReturnStruct(this.clock, ID);
        return rt;
    }

    private class Tf implements Comparable<Tf>, Cloneable{
        private int displacement;
        private int ID;
        private int distToRoom;
        private int pos;
        private boolean ready;

        private Tf(int thiefID, int distToRoom, int disp, int pos) {
            this.displacement = disp;
            this.ID = thiefID;
            this.distToRoom = distToRoom;
            this.pos = pos;
            this.ready = false;
        }
        @Override
        public int compareTo(Tf next){//<0 before >1 after
            return this.pos-next.pos;
        }
        
        public Tf Clone(){
            Tf ret = new Tf(ID,distToRoom,displacement,pos);
            return ret;
        }

        @Override
        public String toString() {
            return "Tf{" + "displacement=" + displacement + ", ID=" + ID + ", distToRoom=" + distToRoom + ", pos=" + pos + '}';
        }
        
       
        
    }

    /**
     * this method clones a Tf thief
     * @param tv thieves
     * @return new thief struct
     */
    public Tf[] cloneThieves(Tf[] tv){
        Tf[] ret = new Tf[tv.length];
        for(int i=0; i<tv.length;i++){
            ret[i] = tv[i].Clone();
        }
        return ret;
    }
    
    private int howMany;
    private int whoWalks;    
    private int target;
    private int distance;
    private Tf[] thieves ;
    private ItfLogger log;
    private int ID;

    /**
     * AP constructor initializes variables,
     * it requires the logger interface, the Assault_Party Id and the registry hostname
     * @param log logging monitor
     * @param id Assault party id
     * @param reghostname the registry hostname 
     */
    public M_AssaultParty(ItfLogger log, int id, String reghostname){
        this.reghostname = reghostname;
        this.ID = id;
        this.target = -1;
        this.log = log;
        this.thieves = new Tf[Variables.NTAP];
        this.whoWalks = 0;
        this.target = -1;
        this.distance = -1;
        this.howMany = 0;
        this.clock  = new VectorClock(0);
    }
    
    /**
     * Returns the current target of this Assault_Party
     * @return the return struct containing the update clock and the target
     */
    public synchronized ReturnStruct getTarget(VectorClock clk) {
        this.clock.update(clk);
        ReturnStruct rt = new ReturnStruct(clock,target);
        return rt;
    }

    /**
     * method that reset the AP
     * when the thieves arrive to the base
     * @param target new target
     * @param distance distance to target
     * @param clk the clock
     * @return the return struct containing the updated clock
     * @throws RemoteException because of rmi
     */
    public synchronized ReturnStruct reset(int target, int distance, VectorClock clk) throws RemoteException{
        this.clock.update(clk);
        howMany = 0;
        this.target = target;
        this.distance = distance;
        log.APLog(Variables.logger.RId, ID, -1 , target, clk);
        ReturnStruct rt = new ReturnStruct(clock);
        return rt;
    }
    
    /**
     * Returns the distance to the site
     * @return the return struct containing the update clock and the distance
     */
    public synchronized ReturnStruct getDistance(VectorClock clk){
        this.clock.update(clk);
        ReturnStruct rt = new ReturnStruct(clock,distance);
        return rt;
    }
    
    /**
     * method that inserts the thief into the AP data structure
     * @param thiefID id of the thief
     * @param disp agility of the thief
     * @param clk the clock
     * @return the return struct containing the updated clock
     * @throws RemoteException because of rmi
     */
    public synchronized ReturnStruct joinGroup(int thiefID, int disp, VectorClock clk) throws RemoteException{
        this.clock.update(clk);
        thieves[howMany] = new Tf(thiefID, distance, disp, 0);
        howMany++;
        if(howMany==Variables.NTAP)
            notifyAll();
        log.APLog(Variables.logger.Id, ID, thiefID , thiefID, clk);
        log.APLog(Variables.logger.Pos, ID, thiefID , 0, clk);
        log.APLog(Variables.logger.Cv, ID, thiefID , 0, clk);
        ReturnStruct rt = new ReturnStruct(clock);
        return rt;
    }
    
    /**
     * The crawl operation, it must ensure all thieves form a line and do not
     * go further than the specified distance between them
     * @param thiefID the id of the thief crawling
     * @param in whether its crawling in or crawling out
     * @param clk the clock
     * @return the return struct containing the remaining distance to the site and the update clock
     * @throws RemoteException because of rmi
     */
    @Override
    public synchronized ReturnStruct crawlIn(int thiefID, boolean in, VectorClock clk) throws RemoteException {
           this.clock.update(clk);
        while (  howMany != Variables.NTAP || target==-1 || thiefID!=thieves[whoWalks].ID  ) {
            try {
                wait(); 
            } catch (Exception e) {
            }
        }
        
        Tf aux[]= null;
        boolean ok = false;
        int Iwalk = thieves[whoWalks].displacement;
        while(!ok && Iwalk > 0){
            aux = cloneThieves(thieves);
            ok = true;
            aux[whoWalks].pos += Iwalk; //walk
            aux[whoWalks].distToRoom -= Iwalk; //update dist to room
            
            if(aux[whoWalks].distToRoom<0){
                Iwalk = Iwalk + aux[whoWalks].distToRoom; // update iwalk to get to 0
                aux[whoWalks].pos = distance; //if negative we walked to much 
                aux[whoWalks].distToRoom=0;  //dont walk to much
            }
            Tf[] tmp = cloneThieves(aux);
            Arrays.sort(tmp);//sort array
            for(int i = tmp.length-1;i>=1;i--){ //check result
                if(tmp[i].pos-tmp[i-1].pos>Variables.S || (tmp[i].pos==tmp[i-1].pos && tmp[i].pos !=0 && tmp[i].pos != distance )){
                    ok = false;
                }
            }
            if(!ok){
                Iwalk-=1;
            }
        }
        if(ok) thieves=cloneThieves(aux);

        //String logg = "whoami:" + thiefID + "| amAt:" + thieves[whoWalks].pos + "| walked:" + Iwalk + "| whowalks:" +whoWalks;
        //log.OTLog(logg);
        int ret = whoWalks;
        if(whoWalks>=thieves.length-1)
            whoWalks=0;
        else
            whoWalks+=1;
        notifyAll();
        if(in){
            log.APLog(Variables.logger.Pos, ID, thiefID , thieves[ret].pos, clk);
        }else{
            log.APLog(Variables.logger.Pos, ID, thiefID , distance-thieves[ret].pos, clk);
        }
        ReturnStruct rt = new ReturnStruct(clock, thieves[ret].distToRoom);
        return rt;
    }
    
    /**
     * checks if all thieves have arrived
     * @return true if all thieves arrived else false
     */
    public synchronized boolean allArrived(){
        for(Tf tf : thieves){
            if(tf.distToRoom != 0){
                return false;
            }
        } 
        return true;
    }

    /**
     * blocks the thieves while other thieves are not at the room
     * @param clk the clock
     * @return the return struct containing the updated clock
     */
    public synchronized ReturnStruct waitAllArrived(VectorClock clk){
        this.clock.update(clk);
        while (!allArrived()) {
            try {
                if(!allArrived() && thieves[whoWalks].distToRoom == 0){
                    if(whoWalks>=thieves.length-1)
                        whoWalks=0;
                    else
                        whoWalks+=1;
                    notifyAll();
                }
                wait();
            } catch (Exception e) {
            }
        }
        notifyAll();
        ReturnStruct rt = new ReturnStruct(clock);
        return rt;
    }

    /**
     * checks if all thieves are ready
     * @return true if all ready else false
     */
    public boolean allReady(){
        for(Tf t : thieves){
            if(!t.ready) return false;
        }
        return true;
    }
    
    /**
     * sets the thief ready
     * @param thiefID the id of the thief 
     */
    private void setReady(int thiefID) {
        for(Tf t : thieves){
            if(t.ID==thiefID){
                t.ready=true;
            }
        }
    }
    
    /**
     * reverts the direction of the thief so he can return to the base
     * @param thiefID the of the thief reversing direction
     * @param clk the clock
     * @return the return struct
     */
    @Override
    public synchronized ReturnStruct reverseDirection(int thiefID, VectorClock clk) {
        this.clock.update(clk);
        //set pos to zero
        //set distance to room to initial
        //System.out.println("estou pronto para voltar: "+thiefID);
        setReady(thiefID);
       
        while (!allReady()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        for(Tf t : thieves){
            if(t.ID==thiefID){
                t.distToRoom = distance;
                t.pos = 0;
            }
        }
        whoWalks = 0;
        notifyAll();
        ReturnStruct rt = new ReturnStruct(clock);
        return rt;
    }

    /**
     * inverse of crawlin
     * @param thiefID the id of the thief crawling out
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because of rmi
     */
    @Override
    public synchronized ReturnStruct crawlOut(int thiefID, VectorClock clk) throws RemoteException{
        return crawlIn(thiefID, false, clk);
    }

     /**
     * Shutsdown the monitor and deregisters on the rmi registry
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
            java.util.logging.Logger.getLogger(M_AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject;
        if(this.ID == 0)
             nameEntryObject = RegistryConfiguration.REGISTRY_ASSAULT_PARTY0_NAME ;
         else
             nameEntryObject = RegistryConfiguration.REGISTRY_ASSAULT_PARTY1_NAME ;
        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_AssaultParty.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_AssaultParty.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("M_AssaultParty registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_AssaultParty.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("M_AssaultParty not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_AssaultParty.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(M_AssaultParty.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("M_AssaultParty shutdown.");
    }


}
