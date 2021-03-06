package assignment2.Threads;

import assignment2.DataStructures.Variables;
import assignment2.Interfaces.ItfTAssaultParty;
import assignment2.Interfaces.ItfCs;
import assignment2.Interfaces.ItfLogger;
import assignment2.Interfaces.ItfCcs;
import assignment2.Interfaces.ItfMuseum;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Thief extends Thread{
    private final ItfMuseum museum;
    private ItfTAssaultParty APs[];
    private ItfTAssaultParty AP;
    private final ItfCcs ccs;
    private Variables v;
    private final ItfCs cs;
    private final ItfLogger logger;
    private int state;
    private final int ID;
    private final int displacement;
    private int dist=0;
    private boolean hasPainting;
    private boolean arrived;

    /**
     * setter for the Thief AP
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
     *
     * @param museum museum monitor
     * @param APs AP monitors, although the Thief has access to all he only accesses one at a time
              depending on the set AP
     * @param ccs control and collection site monitors
     * @param cs concentration site monitor
     * @param ID Thief ID
     * @param logger Logger monitor
     * @param v config variable
     */
    public Thief(ItfMuseum museum, ItfTAssaultParty APs[], ItfCcs ccs, ItfCs cs, int ID, ItfLogger logger, Variables v){
        this.museum = museum;
        this.APs = APs;
        this.logger = logger;
        this.v = v;
        this.ccs = ccs;
        this.ID = ID;
        this.cs = cs;
        this.state = v.thief.outside;
        this.displacement = ThreadLocalRandom.current().nextInt(v.MD[0], v.MD[1]+1);
        logger.OTLog(v.logger.MD, ID, displacement);
    }
    @Override
    public void run() {
        while (!over) {
            try {
                
                if(state == v.thief.atARoom){
                    hasPainting=museum.rollACanvas(AP.getTarget());
                    //////////////////////////////////
                    int hp=0;
                    if(hasPainting) hp=1;
                    ////////////////////////////////////
                    logger.APLog(v.logger.Cv, AP.getID(), ID , hp);
                    AP.reverseDirection(ID);
                    this.state = v.thief.crawling_outwards;
                }
                else if(state==v.thief.crawling_inwards){
                    logger.OTLog(v.logger.Stat, ID, state);
                    this.dist = AP.crawlIn(ID,true);
                    if (dist ==0){
                        AP.waitAllArrived();
                        this.state=v.thief.atARoom;
                        logger.OTLog(v.logger.Stat, ID, state);
                    }
                }
                else if(state==v.thief.crawling_outwards){
                    logger.OTLog(v.logger.Stat, ID, state);
                    this.dist = AP.crawlOut(ID);
                    if(dist == 0){
                        AP.waitAllArrived();
                        arrived = true;
                        this.state = v.thief.outside; 
                    }
                }
                else if(state==v.thief.outside){ //initial state
                    logger.OTLog(v.logger.Stat, ID, state);
                    logger.OTLog(v.logger.S, ID, 0);
                    if(arrived){
                        arrived=false;
                        ccs.handACanvas(hasPainting, ID);
                        hasPainting = false;
                    }
                    int ret = cs.amINeeded(ID);
                    if(ret == 1){
                        over = true;
                        break;
                    }
                    setAP(ccs.prepareExcursion(ID));
                    AP.joinGroup(ID, displacement);
                    this.state = v.thief.crawling_inwards;

                }
                    
                
            } catch (Exception e) { System.out.println(e + "; ; ; ; \n" + e.getStackTrace()[0].toString());}
        }
    }

}
