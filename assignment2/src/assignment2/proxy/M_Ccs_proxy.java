
package assignment2.proxy;
import assignment2.DataStructures.ClientCom;
import assignment2.DataStructures.Message;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.DataStructures.Variables;
import assignment2.Interfaces.ItfLogger;
import assignment2.Interfaces.ItfCcs;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_Ccs_proxy implements ItfCcs{
    private String SERVER_HOST;
    private int SERVER_PORT;
    private Variables v;
    
    /**
     * Constructor
     * @param v variables instance
     * @param host host where respective server is
     * @param port port where respective server is
     */
    public M_Ccs_proxy(Variables v, String host, int port){
        this.v = v;
        this.SERVER_HOST = host;
        this.SERVER_PORT = port;
    }
    @Override
    public int appraiseSit(boolean over) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(APPRAISE_SIT, over);
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
    public void startOperations() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(START_OPERATIONS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void sendAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(SEND_ASSAULT_PARTY);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void takeARest() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(TAKE_A_REST);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public boolean[] collectCanvas() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(COLLECT_CANVAS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        return inMessage.getBooleanArray();
    }

    @Override
    public int sumUpResults() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(SUM_UP);
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
    public void handACanvas(boolean hasPainting, int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(HAND_A_CANVAS,hasPainting, thiefID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public int prepareExcursion(int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(PREPARE_EXCURSION, thiefID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        return inMessage.getInteger();
    }

}
