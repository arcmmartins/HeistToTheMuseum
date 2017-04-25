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
    private final ItfTAssaultParty[] APs;
    private class MTRoom{
        boolean roomEmpty = false;
        boolean beingHeisted = false;
    }
    private MTRoom rooms[] = new MTRoom[Variables.N];
    private int state;
    private int n_paintings;
    private int roomToAP[] = new int[Variables.NAP];
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
     */
    public MasterThief(ItfCcs ccs, ItfLogger logger, ItfCs cs , ItfTAssaultParty[] APs, ItfMuseum museum){
        this.ccs = ccs;
        this.cs = cs;
        this.APs = APs;
        this.museum = museum;
        this.logger = logger;
        this.n_paintings = 0;
        this.state = Variables.masterThief.planning_the_heist;
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
                switch(state){
                    case Variables.masterThief.planning_the_heist:
                        logger.MTLog(state);
                        ccs.startOperations(); //muda para deciding_what_to_do
                        this.state = Variables.masterThief.deciding_what_to_do; 
                        break;
                    case Variables.masterThief.deciding_what_to_do:
                        logger.MTLog(state);
                        int next = ccs.appraiseSit(allEmpty());
                        switch(next){
                            case 1: //prepareAssaultParty();
                                cs.prepareAssaultParty(); //muda para assembling_a_group;
                                this.state = Variables.masterThief.assembling_a_group;
                                break;
                            case 2: //takeARest();
                                this.state = Variables.masterThief.waiting_for_arrival;
                                logger.MTLog(state);
                                ccs.takeARest(); //muda para waiting_for_arrival;
                                break;
                            case 3: //sumUpResults();
                                this.state = Variables.masterThief.presenting_the_report;
                                break;
                            default:
                                break;
                        }
                        break;
                    case Variables.masterThief.assembling_a_group: 
                        logger.MTLog(state);
                        for(int j =0; j<Variables.NAP;j++){
                            int nextr = nextRoom();
                            int distance = museum.getDistanceToRoom(nextr);
                            APs[j].reset(nextr, distance); // target , distancia
                            roomToAP[j] = nextr;
                            rooms[nextr].beingHeisted = true;
                        }
                        ccs.sendAssaultParty(); //muda para deciding_what_to_do;
                        this.state = Variables.masterThief.deciding_what_to_do;
                        break;
                    case Variables.masterThief.presenting_the_report:
                        n_paintings=ccs.sumUpResults();
                        cs.warnHeistOver();
                        logger.MTLog(state);
                        logger.finalReport(n_paintings);
                        over = true;
                        break;
                    case Variables.masterThief.waiting_for_arrival:
                        boolean[] empty = ccs.collectCanvas(); //muda para deciding_what_to_do;
                        for(int i=0;i<Variables.N;i++){
                            rooms[i].beingHeisted = false;
                        }
                        for(int i=0; i<Variables.NAP;i++){
                            if(empty[i]){
                                //System.out.println("quarto n:" +roomToAP[i] + " está vazia");
                                rooms[roomToAP[i]].roomEmpty = true;
                            }
                        }
                        this.state = Variables.masterThief.deciding_what_to_do;
                        break;
                }
            } catch (Exception e) { System.out.println(e.getStackTrace()[0].toString()+ ": masterthief exception"); }
        }
    }
}
