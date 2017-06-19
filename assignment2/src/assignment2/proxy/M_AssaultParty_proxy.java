
package assignment2.proxy;

import assignment2.Interfaces.ItfTAssaultParty;
import assignment2.Interfaces.ItfLogger;
import assignment2.DataStructures.*;
import static assignment2.DataStructures.MessageConstants.*;
import java.util.Arrays;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_AssaultParty_proxy implements ItfTAssaultParty{
    private String SERVER_HOST;
    private int SERVER_PORT;
    private Variables v;
    private int id;
    
    /**
     * Constructor
     * @param v variables instance
     * @param host host where respective server is
     * @param id the id of the assault party
     * @param port port where respective server is
     */
    public M_AssaultParty_proxy(Variables v, int id, String host, int port){
        this.v = v;
        this.id = id;
        this.SERVER_HOST = host;
        this.SERVER_PORT = port;
    }
    @Override
    public int crawlIn(int thiefID, boolean in) throws Exception {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(CRAWL_IN, thiefID);
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
    public int crawlOut(int thiefID) throws Exception {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(CRAWL_OUT, thiefID);
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
    public void reverseDirection(int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(REVERSE_DIRECTION, thiefID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
    }

    @Override
    public void reset(int target, int distance) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(RESET, target, distance);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void joinGroup(int thiefID, int disp) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(JOIN_GROUP, thiefID, disp);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public int getID() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(GET_ID);
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
    public int getTarget() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(GET_TARGET);
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
    public void waitAllArrived() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(WAIT_ALL_ARRIVED);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    
}
