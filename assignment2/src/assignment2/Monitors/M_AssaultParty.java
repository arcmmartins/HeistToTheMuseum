
package assignment2.Monitors;

import assignment2.Interfaces.ItfTAssaultParty;
import assignment2.Interfaces.ItfLogger;
import assignment2.DataStructures.Variables;
import java.util.Arrays;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_AssaultParty implements ItfTAssaultParty{

    /**
     * @return the assault party id
     */
    public int getID() {
        return ID;
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
     * this method clones a Tf tief
     * @param tv thieves
     * @return new thief struc
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
    private Variables v;
    /**
     * AP constructor initializes variables
     * @param log logging monitor
     * @param id Assault party id
     * @param v config variables
     */
    public M_AssaultParty(ItfLogger log, int id , Variables v){
        this.v = v;
        this.ID = id;
        this.target = -1;
        this.log = log;
        this.thieves = new Tf[v.NTAP];
        this.whoWalks = 0;
        this.target = -1;
        this.distance = -1;
        this.howMany = 0;
    }
    
    /**
     * @return the assault party target room
     */
    public synchronized int getTarget() {
        return target;
    }

    /**
     * method that reset the AP
     * when the thieves arrive to the base
     * @param target new target
     * @param distance distance to target
     */
    public synchronized void reset(int target, int distance) {
        howMany = 0;
        this.target = target;
        this.distance = distance;
        log.APLog(v.logger.RId, ID, -1 , target);
        
    }
    
    /**
     * method that inserts the thief into the AP data structure
     * @param thiefID id of the thief
     * @param disp agility of the thief
     */
    public synchronized void joinGroup(int thiefID, int disp){
        thieves[howMany] = new Tf(thiefID, distance, disp, 0);
        howMany++;
        if(howMany==v.NTAP)
            notifyAll();
        log.APLog(v.logger.Id, ID, thiefID , thiefID);
        log.APLog(v.logger.Pos, ID, thiefID , 0);
        log.APLog(v.logger.Cv, ID, thiefID , 0);
        
    }
    
    /**
     * The crawl operation, it must ensure all thieves form a line and do not
     * go further than the specified distance between them
     * @param thiefID the id of the thief crawling
     * @param in whether its crawling in or crawling out
     * @return the distance left to the room
     */
    @Override
    public synchronized int crawlIn(int thiefID, boolean in) {
           
        while (  howMany != v.NTAP || target==-1 || thiefID!=thieves[whoWalks].ID  ) {
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
                if(tmp[i].pos-tmp[i-1].pos>v.S || (tmp[i].pos==tmp[i-1].pos && tmp[i].pos !=0 && tmp[i].pos != distance )){
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
            log.APLog(v.logger.Pos, ID, thiefID , thieves[ret].pos);
        }else{
            log.APLog(v.logger.Pos, ID, thiefID , distance-thieves[ret].pos);
        }
        
        return thieves[ret].distToRoom;
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
     */
    public synchronized void waitAllArrived(){
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
    }

    /**
     * checks if all thieves are ready
     * @return trye if all ready else false
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
     */
    @Override
    public synchronized void reverseDirection(int thiefID) {
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
    }

    /**
     * inverse of crawlin
     * @param thiefID the id of the thief crawling out
     * @return the distance left to the base
     */
    @Override
    public synchronized int crawlOut(int thiefID){
        int ret = crawlIn(thiefID, false);
        return ret;
    }
    


}
