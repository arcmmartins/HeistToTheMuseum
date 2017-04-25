package assignment2.proxy;

import assignment2.DataStructures.Variables;
import assignment2.DataStructures.Room;
import assignment2.Interfaces.ItfLogger;
import assignment2.Interfaces.ItfMuseum;
import java.util.ArrayList;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Museum_proxy implements ItfMuseum {

    @Override
    public boolean rollACanvas(int roomID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNRooms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDistanceToRoom(int roomID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
