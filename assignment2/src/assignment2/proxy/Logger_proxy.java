package assignment2.proxy;
import assignment2.DataStructures.ClientCom;
import assignment2.DataStructures.Message;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.DataStructures.Variables;
import assignment2.Interfaces.ItfLogger;
import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import java.util.ArrayList;
import java.util.logging.Level;
/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Logger_proxy implements ItfLogger{
    private String SERVER_HOST;
    private int SERVER_PORT;
    private Variables v;
    
    /**
     * Constructor for the logger proxy
     * @param v variables instance
     * @param host hostname where logger server is
     * @param port port where logger server is
     */
    public Logger_proxy(Variables v, String host, int port){
        this.v = v;
        this.SERVER_HOST = host;
        this.SERVER_PORT = port;
    }
    
    @Override
    public void OTLog(int kind, int Tid, int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(OTLOG, kind, Tid, val);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void MTLog(int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(MTLOG, val);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void MuseumLog(int kind, int Rid, int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(MUSEMLOG, kind, Rid, val);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void finalReport(int total_paint) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(FINAL_REPORT, total_paint);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    @Override
    public void APLog(int kind, int APid, int Eid, int val) {
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(this.SERVER_HOST, this.SERVER_PORT);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(APLOG, kind, APid, Eid, val);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != ACK) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

}
