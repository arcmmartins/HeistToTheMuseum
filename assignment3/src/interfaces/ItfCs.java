
package interfaces;

import Datastructures.ReturnStruct;
import Datastructures.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfCs extends Remote{

    /**
     * masterthief method that blocks the MT waiting for all the thieves to be
     * at the concentration site
     * and then marks them as needed
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct prepareAssaultParty(VectorClock clk) throws RemoteException;

    /**
     * thief method that blocks the thief waiting for the next action
     * @param thiefID the id of the thief
     * @param clk the clock
     * @throws RemoteException because rmi
     * @return status code indicating wether the thief is going to join party
     *  or go home
     */
    ReturnStruct amINeeded(int thiefID, VectorClock clk) throws RemoteException;

    /**
     * masterthief method that tells all the thieves to go home
     * @param clk the clock
     * @return the return struct
     * @throws RemoteException because rmi
     */
    ReturnStruct warnHeistOver(VectorClock clk) throws RemoteException;
    
      /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    public void shutdown() throws RemoteException;
}
