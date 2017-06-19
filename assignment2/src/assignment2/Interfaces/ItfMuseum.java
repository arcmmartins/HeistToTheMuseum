

package assignment2.Interfaces;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfMuseum {

    /**
     *
     * @param roomID the room that is being stolen
     * @return wether he successfuly stolen a painting or not
     */
    boolean rollACanvas(int roomID);

    /**
     * method to indicate the total number of rooms
     * @return the number of rooms
     */
    int getNRooms();

    /**
     * method that indicates the total distance to a room for the MT
     * @param roomID the id of the room
     * @return the distance to the room
     */
    int getDistanceToRoom(int roomID);
}
