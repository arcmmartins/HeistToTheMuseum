/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructures;

import java.io.Serializable;

/**
 *
 * @author Alvaro e Nelson
 */
public class ReturnStruct  implements Serializable{
    private static final long serialVersionUID = 1110L;
    private VectorClock retClock;
    private int integer;
    private boolean bool;
    private int[] intarray;
    private boolean[] boolarray;
    
    /**
     * Constructor for void methods
     * @param retClock the updated clock
     */
    public ReturnStruct(VectorClock retClock){
        this.retClock = retClock;
    }
    
    /**
     * Constructor for methods that return an int
     * @param retClock the updated clock
     * @param integer the integer
     */
    public ReturnStruct(VectorClock retClock,int integer){
        this.retClock = retClock;
        this.integer = integer;
    }
    
    /**
     * Constructor for methods that return a boolean
     * @param retClock the updated clock
     * @param bool the boolean to return
     */
    public ReturnStruct(VectorClock retClock, boolean bool){
        this.retClock = retClock;
        this.bool = bool;
    }
    
    /**
     * constructor for methods that return an integer array
     * @param retClock the updated clock
     * @param intarray the int array
     */
    public ReturnStruct(VectorClock retClock, int[] intarray){
        this.retClock = retClock;
        this.intarray = intarray;
    }
    
    /**
     * constructor for methods that return a boolean array
     * @param retClock the updated clock
     * @param boolarray the boolean array
     */
    public ReturnStruct(VectorClock retClock, boolean[] boolarray){
        this.retClock = retClock;
        this.boolarray = boolarray;
    }

    /**
     * returns the updated clock,
     * it should never be null because
     * methods always return a clock
     * @return the updated clock
     */
    public VectorClock getRetClock() {
        return retClock;
    }

    /**
     * returns the integer carried by this struct
     * it may return null if this struct is returned by a method
     * that does not return an integer
     * @return the carried integer
     */
    public int getInteger() {
        return integer;
    }

    /**
     * returns the boolean carried by this struct
     * it may return null if this struct is returned by a method
     * that does not return a boolean
     * @return the carried boolean
     */
    public boolean isBool() {
        return bool;
    }

    /**
     * returns the integer array carried by this struct
     * it may return null if this struct is returned by a method
     * that does not return an integer array
     * @return the carried integer array
     */
    public int[] getIntarray() {
        return intarray;
    }

    /**
     * returns boolean array carried by this struct
     * it may return null if this struct is returned by a method
     * that does not return a boolean array
     * @return the carried boolean array
     */
    public boolean[] getBoolarray() {
        return boolarray;
    }
    
}
