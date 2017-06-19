package clientSide;

import Datastructures.*;
import interfaces.*;
import java.rmi.RemoteException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Thief extends Thread{
    private final ItfMuseum museum;
    private ItfTAssaultParty APs[];
    private ItfTAssaultParty AP;
    private final ItfCcs ccs;
    private final ItfCs cs;
    private final ItfLogger logger;
    private int state;
    private final int ID;
    private final int displacement;
    private int dist=0;
    private boolean hasPainting;
    private boolean arrived;
    private final VectorClock clock;
    /**
     * setter for the thief AP
     * @param id id of the AP
     */
    public void setAP(int id) {
        this.AP = APs[id];
    }
    /**
     * wether heist is over or not
     */
    private boolean over = false;

    /**
     * Constructor for the thief,
     * it requires  an interface for all monitores and the thief ID
     * @param museum museum monitor
     * @param APs AP monitors, although the thief has access to all he only accesses one at a time
     *              depending on the set AP
     * @param ccs control and collection site monitors
     * @param cs concentration site monitor
     * @param ID thief ID
     * @param logger logger monitor
     */
    public Thief(ItfMuseum museum, ItfTAssaultParty APs[], ItfCcs ccs, ItfCs cs, int ID, ItfLogger logger){
        this.museum = museum;
        this.APs = APs;
        this.logger = logger;
        this.ccs = ccs;
        this.ID = ID;
        this.cs = cs;
        this.state = Variables.thief.outside;
        this.displacement = ThreadLocalRandom.current().nextInt(Variables.MD[0], Variables.MD[1]+1);
        clock = new VectorClock(ID);
        try {
            ReturnStruct rt;
            this.clock.tick();
            rt = logger.OTLog(Variables.logger.MD, ID, displacement, clock);
            this.clock.update(rt.getRetClock());
        } catch (RemoteException ex) {
            Logger.getLogger(Thief.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @Override
    public void run() {
        ReturnStruct rt;
        while (!over) {
            try {
                switch(state){
                    case Variables.thief.atARoom:
                        this.clock.tick();
                        rt = AP.getTarget(clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        int tgt = rt.getInteger();
                        
                        
                        this.clock.tick();
                        rt = museum.rollACanvas(tgt, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        hasPainting= rt.isBool();
                        //////////////////////////////////
                        int hp=0;
                        if(hasPainting) hp=1;
                        ////////////////////////////////////+
                        this.clock.tick();
                        rt = AP.getID(this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        int apid = rt.getInteger();
                        
                        this.clock.tick();
                         rt = logger.APLog(Variables.logger.Cv, apid, ID , hp, this.clock.getCopy());
                         this.clock.update(rt.getRetClock());
                        
                        this.clock.tick();
                        rt = AP.reverseDirection(ID, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        
                        this.state = Variables.thief.crawling_outwards;
                        break;
                    case Variables.thief.crawling_inwards:
                        
                         this.clock.tick();
                        rt = logger.OTLog(Variables.logger.Stat, ID, state, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        this.clock.tick();
                        rt = AP.crawlIn(ID,true, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        this.dist = rt.getInteger();
                        if (dist ==0){
                            
                            this.clock.tick();
                            rt = AP.waitAllArrived(this.clock.getCopy());
                            this.clock.update(rt.getRetClock());
                            
                            this.state=Variables.thief.atARoom;
                            this.clock.tick();
                             rt = logger.OTLog(Variables.logger.Stat, ID, state, this.clock.getCopy());
                             this.clock.update(rt.getRetClock());
                        }
                        break;
                    case Variables.thief.crawling_outwards:
                        this.clock.tick();
                        rt = logger.OTLog(Variables.logger.Stat, ID, state, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        this.clock.tick();
                        rt = AP.crawlOut(ID, this.clock.getCopy());
                        this.clock.getCopy();
                        this.dist = rt.getInteger();
                        
                        if(dist == 0){
                            this.clock.tick();
                            rt = AP.waitAllArrived(this.clock.getCopy());
                            this.clock.update(rt.getRetClock());
                            
                            arrived = true;
                            this.state = Variables.thief.outside; 
                        }
                        break;
                    case Variables.thief.outside: //initial state
                        this.clock.tick();
                         rt = logger.OTLog(Variables.logger.Stat, ID, state, this.clock.getCopy());
                         this.clock.update(rt.getRetClock());
                        this.clock.tick();
                        rt = logger.OTLog(Variables.logger.S, ID, 0, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        
                        if(arrived){
                            arrived=false;
                            
                            this.clock.tick();
                            rt = ccs.handACanvas(hasPainting, ID, this.clock.getCopy());
                            this.clock.update(rt.getRetClock());
                            
                            hasPainting = false;
                        }
                        this.clock.tick();
                        rt = cs.amINeeded(ID, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        int ret = rt.getInteger();
                        if(ret == 1){
                            over = true;
                            break;
                        }
                        this.clock.tick();
                        rt = ccs.prepareExcursion(ID, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        setAP(rt.getInteger());
                        
                        this.clock.tick();
                        rt = AP.joinGroup(ID, displacement, this.clock.getCopy());
                        this.clock.update(rt.getRetClock());
                        this.state = Variables.thief.crawling_inwards;
                        
                        break;
                    default:
                        System.out.println("It should not get here, but hey its java");
                }
            } catch (Exception e) { System.out.println(e + "; ; ; ; \n" + e.getStackTrace()[0].toString());}
        }
    }

}
