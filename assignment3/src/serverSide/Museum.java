package serverSide;
import Datastructures.*;
import interfaces.*;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import registry.RegistryConfiguration;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Museum implements ItfMuseum {
    private final int nRooms;
    private final ItfLogger logger;
    ArrayList<Room> rooms;
    private VectorClock clock;
    private String reghostname;
    /**
     * constructor of the museum monitor
     * @param logger the logging monitor
     * @param reghostname the registry hostname
     * @throws RemoteException because rmi
     */
    public Museum(ItfLogger logger, String reghostname) throws RemoteException{
        this.reghostname = reghostname;
        this.nRooms=Variables.N;
        this.rooms = new ArrayList<Room>();
        this.logger=logger;
        this.clock = new VectorClock(0);
        for(int i=0;i<nRooms;i++){
            Room r = new Room();
            this.rooms.add(r);
            logger.MuseumLog(Variables.logger.DT, i, r.getDistanceToSite(), clock);
            logger.MuseumLog(Variables.logger.NP, i, r.getnPaintings(), clock);
        }     
                
    }

    /**
     * method to indicate the total number of rooms
     * @return the number of rooms
     */
    @Override
    public ReturnStruct getNRooms(VectorClock clk){
        this.clock.update(clk);
        return new ReturnStruct(clock, nRooms);
    }

    /**
     * method that indicates the total distance to a room for the MT
     * @param roomID the id of the room
     * @return the distance to the room
     */
    @Override
    public synchronized ReturnStruct getDistanceToRoom(int roomID, VectorClock clk){
        this.clock.update(clk);
        return new ReturnStruct(clock,rooms.get(roomID).getDistanceToSite());
    }
    
    /**
     *
     * @param roomID the room that is being stolen
     * @return whether he successfully stolen a painting or not
     * @throws RemoteException because rmi
     */
    @Override
    public synchronized ReturnStruct rollACanvas(int roomID, VectorClock clk) throws RemoteException{
        this.clock.update(clk);
        boolean ret= rooms.get(roomID).decrement();
        logger.MuseumLog(Variables.logger.NP, roomID, rooms.get(roomID).getnPaintings(), clk);
        return new ReturnStruct(clock,ret);
    }
    
        /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    @Override
    public void shutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;
        
        rmiRegHostName = reghostname;
        rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfiguration.REGISTRY_MUSEUM_NAME ;

        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Museum registration exception: " + e.getMessage());
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Museum not bound exception: " + e.getMessage());
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            Logger.getLogger(Museum.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Museum shutdown.");
    }

}
