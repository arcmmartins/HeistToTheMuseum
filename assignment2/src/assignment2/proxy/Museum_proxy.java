package assignment2.proxy;

import assignment2.DataStructures.ClientCom;
import assignment2.DataStructures.Message;
import static assignment2.DataStructures.MessageConstants.*;
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
    private String SERVER_HOST;
    private int SERVER_PORT;
    private Variables v;

    /**
     * Constructor
     * @param v variables instance
     * @param host host where respective server is
     * @param port port where respective server is
     */
    public Museum_proxy(Variables v, String host, int port){
        this.v = v;
        this.SERVER_HOST = host;
        this.SERVER_PORT = port;
    }
    @Override
    public boolean rollACanvas(int roomID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(ROLL_A_CANVAS, roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        return inMessage.getBoolean();
    }

    @Override
    public int getNRooms() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(GET_N_ROOMS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        return inMessage.getInteger();
    }

    @Override
    public int getDistanceToRoom(int roomID) {

        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(GET_DISTANCE_TO_ROOM,roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        System.out.println(inMessage.getInteger());
        return inMessage.getInteger();
    }
}
