package assignment2.Threads;

import assignment2.DataStructures.Variables;
import assignment2.Interfaces.ItfTAssaultParty;
import assignment2.Interfaces.ItfCs;
import assignment2.Interfaces.ItfLogger;
import assignment2.Interfaces.ItfCcs;
import assignment2.Interfaces.ItfMuseum;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class MasterThief extends Thread{
    private final ItfCcs ccs;
    private final ItfCs cs;
    private final ItfLogger logger;
    private final ItfMuseum museum;
    private Variables v;
    private final ItfTAssaultParty[] APs;
    private class MTRoom{
        boolean roomEmpty = false;
        boolean beingHeisted = false;
    }
    private MTRoom rooms[] ;
    private int state;
    private int n_paintings;
    private int roomToAP[] ;
    private boolean over = false;

    /**
     *
     * @return the number of paintings stolen
     */
    public int getN_paintings() {
        return n_paintings;
    }

    /**
     *
     * @param n_paintings number of paintings
     */
    public void setN_paintings(int n_paintings) {
        this.n_paintings = n_paintings;
    }

    /**
     *
     * @param state state to set
     */
    public void setState(int state) {
        this.state = state;
    }
    
    /**
     *
     * @param ccs control and collection site monitor
     * @param logger logger monitor
     * @param cs concentration site monitor
     * @param APs Assault parties monitors
     * @param museum museum monitor
     * @param v config variable
     */
    public MasterThief(ItfCcs ccs, ItfLogger logger, ItfCs cs , ItfTAssaultParty[] APs, ItfMuseum museum, Variables v){
        this.ccs = ccs;
        this.cs = cs;
        this.APs = APs;
        this.museum = museum;
        this.logger = logger;
        this.n_paintings = 0;
        this.v = v;
        this.rooms = new MTRoom[v.N];
        this.roomToAP = new int[v.NAP];
        this.state = v.masterThief.planning_the_heist;
        for(int i =0;i<rooms.length;i++)
            rooms[i] = new MTRoom();
    }
    
    /**
     * method that calculates the next room to steal
     * sends each party to a different room except if they are at the last room
     * and sends the parties to the same
     * @return the next room to steal
     */
    public int nextRoom(){
        for(int i=0; i<rooms.length;i++){
            if(!rooms[i].beingHeisted && !rooms[i].roomEmpty)
            {
                return i;
            }
        }
        return rooms.length-1;
    }
    
    /**
     * method that checks if all the rooms are empty
     * @return if all the rooms are empty
     */
    public boolean allEmpty(){
        for(MTRoom room : rooms){
            if(!room.roomEmpty)
                return false;
        }
        return true;
    }
    
    @Override
    public void run() {
        while (!over) {
            try {
                if(state == v.masterThief.planning_the_heist){
                    logger.MTLog(state);
                    ccs.startOperations(); //muda para deciding_what_to_do
                    this.state = v.masterThief.deciding_what_to_do; 
                }else if(state ==v.masterThief.deciding_what_to_do){
                    logger.MTLog(state);
                        int next = ccs.appraiseSit(allEmpty());
                        switch(next){
                            case 1: //prepareAssaultParty();
                                cs.prepareAssaultParty(); //muda para assembling_a_group;
                                this.state = v.masterThief.assembling_a_group;
                                break;
                            case 2: //takeARest();
                                this.state = v.masterThief.waiting_for_arrival;
                                logger.MTLog(state);
                                ccs.takeARest(); //muda para waiting_for_arrival;
                                break;
                            case 3: //sumUpResults();
                                this.state = v.masterThief.presenting_the_report;
                                break;
                            default:
                                break;
                        }
                }else if(state == v.masterThief.assembling_a_group){
                    logger.MTLog(state);
                    for(int j =0; j<v.NAP;j++){
                        int nextr = nextRoom();
                        int distance = museum.getDistanceToRoom(nextr);
                        APs[j].reset(nextr, distance); // target , distancia
                        roomToAP[j] = nextr;
                        rooms[nextr].beingHeisted = true;
                    }
                    ccs.sendAssaultParty(); //muda para deciding_what_to_do;
                    this.state = v.masterThief.deciding_what_to_do;
                }else if(state == v.masterThief.presenting_the_report){
                    n_paintings=ccs.sumUpResults();
                    cs.warnHeistOver();
                    logger.MTLog(state);
                    logger.finalReport(n_paintings);
                    over = true;
                }else if(state == v.masterThief.waiting_for_arrival){
                    boolean[] empty = ccs.collectCanvas(); //muda para deciding_what_to_do;
                    for(int i=0;i<v.N;i++){
                        rooms[i].beingHeisted = false;
                    }
                    for(int i=0; i<v.NAP;i++){
                        if(empty[i]){
                            //System.out.println("quarto n:" +roomToAP[i] + " está vazia");
                            rooms[roomToAP[i]].roomEmpty = true;
                        }
                    }
                    this.state = v.masterThief.deciding_what_to_do;
                }
            } catch (Exception e) { System.out.println(e.getStackTrace()[0].toString()+ ": masterthief exception"); }
        }
    }
}
