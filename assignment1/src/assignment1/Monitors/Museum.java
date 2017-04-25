package assignment1.Monitors;

import assignment1.DataStructures.*;
import assignment1.Interfaces.*;
import java.util.ArrayList;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Museum implements ItfMuseum {
    private final int nRooms;
    private final ItfLogger logger;
    ArrayList<Room> rooms;
    
    /**
     * constructor of the museum monitor
     * @param logger the logging monitor
     */
    public Museum(ItfLogger logger){
        this.nRooms=Variables.N;
        this.rooms = new ArrayList<Room>();
        this.logger=logger;
        
        for(int i=0;i<nRooms;i++){
            Room r = new Room();
            this.rooms.add(r);
            logger.MuseumLog(Variables.logger.DT, i, r.getDistanceToSite());
            logger.MuseumLog(Variables.logger.NP, i, r.getnPaintings());
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
        logger.MuseumLog(Variables.logger.NP, roomID, rooms.get(roomID).getnPaintings());
        return ret;
    }

    

}
