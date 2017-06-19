package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Datastructures.*;
/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfLogger extends Remote{

    /**
     *method used by the ordinary thief to log
     * @param kind kind of parameter being logged
     * @param Tid id of the thief
     * @param val value of the parameter
     * @param clk the clock
     * @throws RemoteException remote exception because of rmi
     * @return the return structure
     */
    ReturnStruct OTLog(int kind, int Tid, int val, VectorClock clk) throws RemoteException;

    /**
     * method used by the master thief to log
     * @param val value of the status
     * @param clk the clock
     * @throws RemoteException  remote exception because of rmi
     * @return the return structure
     */
    ReturnStruct MTLog(int val, VectorClock clk) throws RemoteException;

    /**
     * method used by the museum to log
     * @param kind kind of parameter being logged
     * @param Rid id of the room
     * @param val value of the parameter
     * @param clk the clock
     * @throws RemoteException  remote exception because of rmi
     * @return the return structure
     */
    ReturnStruct MuseumLog(int kind, int Rid, int val, VectorClock clk) throws RemoteException;

    /**
     * method used to log the report
     * @param total_paint total paintings
     * @param clk the clock
     * @throws RemoteException  remote exception because of rmi
     * @return the return structure
     */
    ReturnStruct finalReport(int total_paint, VectorClock clk) throws RemoteException;

    /**
     *method used by the assault party to log
     * @param kind kind of parameter being logged
     * @param APid id of the assault party
     * @param Eid  id of the thief
     * @param val  value of the parameter
     * @param clk the clock
     * @return the return structure
     * @throws RemoteException  remote exception because of rmi
     */
    ReturnStruct APLog(int kind, int APid, int Eid , int val, VectorClock clk) throws RemoteException;
    
        /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    public void shutdown() throws RemoteException;
}
