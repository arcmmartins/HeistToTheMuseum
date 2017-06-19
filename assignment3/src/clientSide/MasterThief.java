package clientSide;

import Datastructures.*;
import interfaces.*;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class MasterThief extends Thread {

    private final ItfCcs ccs;
    private final ItfCs cs;
    private final ItfLogger logger;
    private final ItfMuseum museum;
    private final ItfTAssaultParty[] APs;

    private class MTRoom {

        boolean roomEmpty = false;
        boolean beingHeisted = false;
    }
    private MTRoom rooms[] = new MTRoom[Variables.N];
    private int state;
    private int n_paintings;
    private int roomToAP[] = new int[Variables.NAP];
    private boolean over = false;
    private VectorClock clock;

    /**
     * getter for n_paintings
     * @return the number of paintings stolen
     */
    public int getN_paintings() {
        return n_paintings;
    }

    /**
     * setter for n_paintings
     * @param n_paintings number of paintings
     */
    public void setN_paintings(int n_paintings) {
        this.n_paintings = n_paintings;
    }

    /**
     * setter for the states
     * @param state state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Constructor for the master thief,
     * it requires an interface of all monitors as arguments
     * @param ccs control and collection site monitor
     * @param logger logger monitor
     * @param cs concentration site monitor
     * @param APs Assault parties monitors
     * @param museum museum monitor
     */
    public MasterThief(ItfCcs ccs, ItfLogger logger, ItfCs cs, ItfTAssaultParty[] APs, ItfMuseum museum) {
        this.ccs = ccs;
        this.cs = cs;
        this.APs = APs;
        this.museum = museum;
        this.logger = logger;
        this.n_paintings = 0;
        this.state = Variables.masterThief.planning_the_heist;
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new MTRoom();
        }
        clock = new VectorClock(Variables.M);
    }

    /**
     * method that calculates the next room to steal sends each party to a
     * different room except if they are at the last room and sends the parties
     * to the same
     *
     * @return the next room to steal
     */
    public int nextRoom() {
        for (int i = 0; i < rooms.length; i++) {
            if (!rooms[i].beingHeisted && !rooms[i].roomEmpty) {
                return i;
            }
        }
        return rooms.length - 1;
    }

    /**
     * method that checks if all the rooms are empty
     *
     * @return if all the rooms are empty
     */
    public boolean allEmpty() {
        for (MTRoom room : rooms) {
            if (!room.roomEmpty) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        ReturnStruct rt;
        while (!over) {
            try {
                switch (state) {
                    case Variables.masterThief.planning_the_heist:
                        this.clock.tick();
                        rt = logger.MTLog(state,this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        ccs.startOperations(clock.clone()); //muda para deciding_what_to_do
                        this.state = Variables.masterThief.deciding_what_to_do;
                        break;
                    case Variables.masterThief.deciding_what_to_do:
                        this.clock.tick();
                        rt = logger.MTLog(state,this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        this.clock.tick();
                        rt = ccs.appraiseSit(allEmpty(), clock.clone());
                        this.clock.update(rt.getRetClock());
                        int next = rt.getInteger();

                        switch (next) {
                            case 1: //prepareAssaultParty();
                                this.clock.tick();
                                rt = cs.prepareAssaultParty(clock.clone()); //muda para assembling_a_group;
                                this.clock.update(rt.getRetClock());
                                this.state = Variables.masterThief.assembling_a_group;
                                break;
                            case 2: //takeARest();
                                this.state = Variables.masterThief.waiting_for_arrival;
                                this.clock.tick();
                                rt = logger.MTLog(state,this.clock.getCopy());
                                this.clock.update(rt.getRetClock());
                                this.clock.tick();
                                rt = ccs.takeARest(clock.clone()); //muda para waiting_for_arrival;
                                this.clock.update(rt.getRetClock());
                                break;
                            case 3: //sumUpResults();
                                this.state = Variables.masterThief.presenting_the_report;
                                break;
                            default:
                                break;
                        }
                        break;
                    case Variables.masterThief.assembling_a_group:
                        this.clock.tick();
                        rt = logger.MTLog(state,this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        for (int j = 0; j < Variables.NAP; j++) {
                            int nextr = nextRoom();
                            this.clock.tick();
                            rt = museum.getDistanceToRoom(nextr, clock.clone());
                            this.clock.update(rt.getRetClock());
                            int distance = rt.getInteger();
                            APs[j].reset(nextr, distance, clock.clone()); // target , distancia
                            roomToAP[j] = nextr;
                            rooms[nextr].beingHeisted = true;
                        }
                        ccs.sendAssaultParty(clock.clone()); //muda para deciding_what_to_do;
                        this.state = Variables.masterThief.deciding_what_to_do;
                        break;
                    case Variables.masterThief.presenting_the_report:
                        this.clock.tick();
                        rt = ccs.sumUpResults(clock.clone());
                        this.clock.update(rt.getRetClock());
                        n_paintings = rt.getInteger();

                        this.clock.tick();
                        rt = cs.warnHeistOver(clock.clone());
                        this.clock.update(rt.getRetClock());

                        this.clock.tick();
                        rt = logger.MTLog(state,this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        
                        this.clock.tick();
                        rt = logger.finalReport(n_paintings,this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        over = true;
                        break;
                    case Variables.masterThief.waiting_for_arrival:
                        this.clock.tick();
                        rt = ccs.collectCanvas(clock.clone()); //muda para deciding_what_to_do;
                        this.clock.update(rt.getRetClock());
                        boolean[] empty = rt.getBoolarray();
                        for (int i = 0; i < Variables.N; i++) {
                            rooms[i].beingHeisted = false;
                        }
                        for (int i = 0; i < Variables.NAP; i++) {
                            if (empty[i]) {
                                //System.out.println("quarto n:" +roomToAP[i] + " está vazia");
                                rooms[roomToAP[i]].roomEmpty = true;
                            }
                        }
                        this.state = Variables.masterThief.deciding_what_to_do;
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace()[0].toString() + ": masterthief exception");
            }
        }
    }
}
