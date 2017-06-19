

package interfaces;

import Datastructures.ReturnStruct;
import Datastructures.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfMuseum extends Remote{

    /**
     *
     * @param roomID the room that is being stolen
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return wether he successfuly stolen a painting or not
     */
    ReturnStruct rollACanvas(int roomID, VectorClock clk) throws RemoteException;

    /**
     * method to indicate the total number of rooms
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return the number of rooms
     */
    ReturnStruct getNRooms(VectorClock clk) throws RemoteException;

    /**
     * method that indicates the total distance to a room for the MT
     * @param roomID the id of the room
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return the distance to the room
     */
    ReturnStruct getDistanceToRoom(int roomID, VectorClock clk) throws RemoteException;
    
        /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    public void shutdown() throws RemoteException;
}
