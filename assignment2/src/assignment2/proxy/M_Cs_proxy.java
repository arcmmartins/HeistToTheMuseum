
package assignment2.proxy;
import assignment2.Monitors.*;
import assignment2.*;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.DataStructures.*;
import assignment2.Interfaces.*;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_Cs_proxy implements ItfCs{
    private String SERVER_HOST;
    private int SERVER_PORT;
    private Variables v;

    /**
     * Constructor
     * @param v variables instance
     * @param host host where respective server is
     * @param port port where respective server is
     */
    public M_Cs_proxy(Variables v, String host, int port){
        this.v = v;
        this.SERVER_HOST = host;
        this.SERVER_PORT = port;
    }
    
    @Override
    public void prepareAssaultParty() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(PREPARE_ASSAULT_PARTY);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public int amINeeded(int thiefID) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(AM_I_NEEDED,thiefID);
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
    public void warnHeistOver() {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(WARN_HEIST_OVER);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }
}
