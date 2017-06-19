package assignment2.Monitors;

import assignment2.DataStructures.Variables;
import assignment2.DataStructures.Room;
import assignment2.Interfaces.ItfLogger;
import assignment2.Interfaces.ItfMuseum;
import java.util.ArrayList;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Museum implements ItfMuseum {
    private final int nRooms;
    private final ItfLogger logger;
    ArrayList<Room> rooms;
    private Variables v;
    
    /**
     * constructor of the museum monitor
     * @param logger the logging monitor
     * @param v config variable
     */
    public Museum(ItfLogger logger, Variables v){
        this.v=v;
        this.nRooms=v.N;
        this.rooms = new ArrayList<Room>();
        this.logger=logger;
        
        for(int i=0;i<nRooms;i++){
            Room r = new Room(v);
            this.rooms.add(r);
            logger.MuseumLog(v.logger.DT, i, r.getDistanceToSite());
            logger.MuseumLog(v.logger.NP, i, r.getnPaintings());
        }     
                
    }

    /**
     * method to indicate the total number of rooms
     * @return the number of rooms
     */
    public int getNRooms(){
        return nRooms;
    }

    /**
     * method that indicates the total distance to a room for the MT
     * @param roomID the id of the room
     * @return the distance to the room
     */
    public synchronized int getDistanceToRoom(int roomID){
        return rooms.get(roomID).getDistanceToSite();
    }
    
    /**
     *
     * @param roomID the room that is being stolen
     * @return whether he successfully stolen a painting or not
     */
    @Override
    public synchronized boolean rollACanvas(int roomID) {
        boolean ret= rooms.get(roomID).decrement();
        logger.MuseumLog(v.logger.NP, roomID, rooms.get(roomID).getnPaintings());
        return ret;
    }

    

}
