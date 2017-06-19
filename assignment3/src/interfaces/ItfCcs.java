
package interfaces;

import Datastructures.ReturnStruct;
import Datastructures.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfCcs extends Remote{

    /**
     *
     * @param over if all rooms are empty
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return the next action to take
     */
    ReturnStruct appraiseSit(boolean over, VectorClock clk) throws RemoteException;

    /**
     * the start of operations initializes some variables
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct startOperations(VectorClock clk) throws RemoteException;

    /**
     * waits for the groups to be full and then sends the thieves
     * to the assault
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct sendAssaultParty(VectorClock clk) throws RemoteException;

    /**
     * blocks the masterthief waiting for all the groups to arrive
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct takeARest(VectorClock clk) throws RemoteException;

    /**
     * method called after all the thieves returned
     * collects all the canvas and resets the groups for the next
     * assault to start
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return wether each assault party emptied the assigned room
     */
    ReturnStruct collectCanvas(VectorClock clk) throws RemoteException;

    /**
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return the total paintings stolen
     */
    ReturnStruct sumUpResults(VectorClock clk) throws RemoteException;
    
    /**
     *
     * @param hasPainting flag that indicates wether thief has painting
     * @param thiefID the id of the thief handing the canvas
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct handACanvas(boolean hasPainting, int thiefID, VectorClock clk) throws RemoteException;

    /**
     * method that indicates the assault party for the thief and blocks it
     * while he waits for the theft to start
     * 
     * @param thiefID the id of the thief
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return the assault party that will incorporate the thief for the
     *  next excursion
     */
    ReturnStruct prepareExcursion(int thiefID, VectorClock clk) throws RemoteException;
    
    /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    public void shutdown() throws RemoteException;
}
