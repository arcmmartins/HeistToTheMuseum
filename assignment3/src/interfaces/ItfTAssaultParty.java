
package interfaces;

import Datastructures.ReturnStruct;
import Datastructures.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfTAssaultParty extends Remote{

    /**
     * The crawl operation, it must ensure all thieves form a line and do not
     * go further than the specified distance between them
     * @param thiefID the id of the thief crawling
     * @param in whether its crawling in or crawling out
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct crawlIn(int thiefID, boolean in, VectorClock clk) throws RemoteException;

    /**
     * inverse of crawlin
     * @param thiefID the id of the thief crawling out
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct crawlOut(int thiefID, VectorClock clk) throws RemoteException;

    /**
     * reverts the direction of the thief so he can return to the base
     * @param thiefID the of the thief reversing direction
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct reverseDirection(int thiefID, VectorClock clk) throws RemoteException;

    /**
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct getDistance(VectorClock clk) throws RemoteException;

    /**
     * resets the AP when the theft is over and we need to restart
     * @param target the target room for this AP
     * @param distance distance to the room
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct reset(int target, int distance, VectorClock clk) throws RemoteException;

    /**
     * method that inserts the thief into the AP data structure
     * @param thiefID id of the thief
     * @param disp agility of the thief
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct joinGroup(int thiefID, int disp, VectorClock clk) throws RemoteException;

    /**
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct getID(VectorClock clk) throws RemoteException;

    /**
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct getTarget(VectorClock clk) throws RemoteException;

    /**
     * blocks the thieves while other thieves are not at the room
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct waitAllArrived(VectorClock clk) throws RemoteException;
    
        /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    public void shutdown() throws RemoteException;
}
