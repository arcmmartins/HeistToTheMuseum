
package assignment2.Monitors;
import assignment2.DataStructures.Variables;
import assignment2.Interfaces.ItfLogger;
import assignment2.Interfaces.ItfCcs;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_Ccs implements ItfCcs{

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
     */
    @Override
    public synchronized void handACanvas(boolean hasPainting, int thiefID) {
        arrived++;
        if(hasPainting){
            int belong = whereIBelong(thiefID);
            nPaintings[belong]++;
        }
        if(arrived==v.M)
           notifyAll();
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
        int[] thieves = new int[v.M];
        int num=0;
        int tgt = -1;
        boolean onAssault=false;
    }
    /**
     * Assault parties
     */
    private AP[] aps ;
    /**
     * total stolen paintings
     */
    int nPaintings[] ;
    int arrived = 0;
    
    /**
     * struct that contains all important room information
     */
    private class MTRoom{
        boolean targeted = false;
        boolean empty = false;
        int ap = -1;
    }
    private MTRoom[] rooms ;
    
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
     */
    @Override
    public synchronized void startOperations() {
        for(int i=0;i<v.N;i++)
            rooms[i] = new MTRoom();
        
        for(int i=0;i<v.NAP;i++){
            aps[i] = new AP();
        }
    }

    /**
     * Auxiliary method that informs if the assault parties are full
     * @return the first group that is not full or -1 if all full
     */

    public int groupsFull(){
        for(int i = 0; i< aps.length ; i++){
            if(aps[i].num != v.NTAP)
                return i;
        }
        return -1;
    }

    /**
     * Auxiliary method that checks if a group is full
     * @param groupID the id of the group to check
     * @return wether the group is full or not
     */
    public synchronized boolean groupFull(int groupID){
        return aps[groupID].num == v.M;
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
    private Variables v;

    /**
     * Monitor ccs constructor
     * @param logger logger proxy instance
     * @param v variables instance
     */
    public M_Ccs(ItfLogger logger, Variables v){
        this.v = v;
        this.logger = logger;
        this.nPaintings= new int[v.NAP];
        this.rooms = new MTRoom[v.N];
        this.aps = new AP[v.NAP];
    }
    
    /**
     * aux method that adds a thief to a Ccs group( != assaultparty)
     * @param thiefID id of the thief
     * @return the target group
     */
    public synchronized int joinGroup(int thiefID){
        int tgt = groupsFull();
        if(tgt == -1)
            return -1;
        aps[tgt].thieves[aps[tgt].num] = thiefID;
        aps[tgt].num++;
        logger.OTLog(v.logger.S, thiefID, 1);
        return tgt;
    }

    
    


    /**
     * @param over if all rooms are empty
     * @return the next action to take
     */
    @Override
    public synchronized int appraiseSit(boolean over) {
        if(over)
            return 3;
        if(!onAssault())
            return 1;
        else if(onAssault()){
            return 2;
        }
        else return 0;
    }
    
    /**
     * waits for the groups to be full and then sends the thieves
     * to the assault
     */
    @Override
    public synchronized void sendAssaultParty() {
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
        nPaintings = new int[v.NAP];
        notifyAll();
    }

    /**
     * blocks the masterthief waiting for all the groups to arrive
     */
    @Override
    public synchronized void takeARest() {
        while(arrived != v.M){
            try{
                wait();
            }catch(Exception e){
                
            }
        }
    }

    /**
     * method called after all the thieves returned
     * collects all the canvas and resets the groups for the next
     * assault to start
     * @return wether each assault party emptied the assigned room
     */
    @Override
    public synchronized boolean[] collectCanvas() {
        boolean flag[];
        flag = new boolean[v.NAP];
        for(int i=0;i<v.NAP;i++){
            sum += nPaintings[i];
            if(nPaintings[i] != v.NTAP){
                flag[i] = true;
            }
        }
        for(int i =0 ;i<aps.length;i++){
            aps[i] = new AP();
        }
        return flag;
    }

    /**
     * @return the total paintings stolen
     */
    @Override
    public synchronized int sumUpResults() {
        return sum;
    }

    /**
     * method that indicates the assault party for the thief and blocks it
     * while he waits for the theft to start
     * 
     * @param thiefID the id of the thief
     * @return the assault party that will incorporate the thief for the
     *  next excursion
     */
    @Override
    public synchronized int prepareExcursion(int thiefID) {
        int ret = joinGroup(thiefID);
        // return must be != -1 and will always be
        notifyAll();
        while (!this.aps[ret].onAssault) {
            try {
                wait(); // espera que os ladroes entrem nos grupos
            } catch (Exception e) {
            }
        }
        return ret;
    }
}
