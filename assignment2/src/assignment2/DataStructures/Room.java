package assignment2.DataStructures;

import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Room {
    private int nPaintings;
    private int distanceToSite;
    private Variables v;

    /**
     * Room constructor, initializes the number of rooms and the distance
     * to site based on the Variables params
     * @param v config variable
     */
    public Room(Variables v){
        this.v = v;
        this.nPaintings = ThreadLocalRandom.current().nextInt(v.Q[0], v.Q[1]+1);
        this.distanceToSite = ThreadLocalRandom.current().nextInt(v.D[0], v.D[1]+1);
    }

    /**
     *
     * @return true or false if there are paintings
     */
    public boolean decrement() {
        if(this.nPaintings>0){
            this.nPaintings--;
            return true;
        }
        return false;
    }

    /**
     *
     * @return the distance to the site
     */
    public int getDistanceToSite() {
        return distanceToSite;
    }

    /**
     *
     * @return the number of paintings
     */
    public int getnPaintings() {
        return nPaintings;
    }
    
    
}
