/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructures;
import java.io.Serializable;
import java.util.Arrays;
/**
 *
 * @author Alvaro e Nelson
 */
public class VectorClock implements Cloneable, Serializable, Comparable<VectorClock>{
        private static final long serialVersionUID = 1111L;
        private int[] ticks;
        private int index;

    /**
     * Constructor of the vector clock,
     * it requires a index for the position
     * to the tick
     * @param index the index to tick
     */
    public VectorClock(int index){
            /**
                  * number of thieves + master thief
                */
            this.index = index;
            ticks = new int[Variables.M + 1]; 
            for(int i=0; i <= Variables.M; i++)
                ticks[i] = 0;
        }
        
    /**
     * aux method that returns the internal integer array containing the ticks
     * @return the clock integer array
     */
    public int[] getRelogio() {
            return ticks;
        }
        
        /**
        * Returns a deepclone of the vector clock.
        * @return deepclone of the clock
        */
         public synchronized VectorClock getCopy(){
            return this.clone();
         }
        
    /**
     * ticks(++) the clock at the index passed in the constructor
     */
    public synchronized void tick(){
            ticks[index]++;
        }

    /**
     * updates the clock based on the other given clock,
     * always keeps the most up to date ticks
     * @param vt clock to update from
     */
    public synchronized void update(VectorClock vt) {
            for(int i = 0; i < ticks.length; i++)
                ticks[i] = Math.max(ticks[i], vt.ticks[i]);
        }
        
    @Override
    public synchronized VectorClock clone(){
        VectorClock copy = null;        
        try { 
            copy = (VectorClock) super.clone ();
        } catch (CloneNotSupportedException e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
        copy.index = this.index;
        copy.ticks= ticks.clone();
        return copy;
    }
    @Override
    public int compareTo(VectorClock other) {
        int[] otherTicks = other.getRelogio();

        int lessOrEqualCount = 0;
        boolean hasLessThan = false;
        boolean hasGreaterThan = false;

        for (int i = 0; i < ticks.length; i++) {
            if (ticks[i] <= otherTicks[i]) {
                lessOrEqualCount++;
                if (ticks[i] < otherTicks[i]) hasLessThan = true;
            } else {
                hasGreaterThan = true;
            }
        }

        if (lessOrEqualCount == ticks.length && hasLessThan) {
            return -1;
        } else if (hasLessThan && hasGreaterThan) {
            return 0;
        } else {
            return 1;
        }
    }
        
}
