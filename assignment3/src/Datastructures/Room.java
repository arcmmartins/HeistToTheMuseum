package Datastructures;

import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Room {
    private int nPaintings;
    private int distanceToSite;

    /**
     * Room constructor, initializes the number of rooms and the distance
     * to site based on the Variables Struct
     */
    public Room(){
        this.nPaintings = ThreadLocalRandom.current().nextInt(Variables.Q[0], Variables.Q[1]+1);
        this.distanceToSite = ThreadLocalRandom.current().nextInt(Variables.D[0], Variables.D[1]+1);
    }

    /**
     * decrements the total number of paintings
     * @return true if there were paintings otherwise false 
     */
    public boolean decrement() {
        if(this.nPaintings>0){
            this.nPaintings--;
            return true;
        }
        return false;
    }

    /**
     * Returns the total distance to the site
     * @return the distance to the site
     */
    public int getDistanceToSite() {
        return distanceToSite;
    }

    /**
     * Returns the number of paintings
     * @return the number of paintings
     */
    public int getnPaintings() {
        return nPaintings;
    }
    
    
}
