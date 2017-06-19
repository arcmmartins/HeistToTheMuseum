package assignment2.server;

import assignment2.DataStructures.*;
import static assignment2.DataStructures.MessageConstants.ACK;
import static assignment2.DataStructures.MessageConstants.CONF;
import assignment2.Interfaces.*;
import assignment2.Monitors.*;
import assignment2.proxy.*;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Museum_server {

    /**
     * main
     * @param args hostname where config server is
     */
    public static void main(String[] args) {
//        String config_ip = "l040101-ws"+args[0]+".ua.pt";
//        int config_port = Integer.parseInt(args[1]);
        //String config_ip = "l040101-ws"+args[0]+".ua.pt";
        //int config_port = Integer.parseInt(args[1]);
        
        String config_ip = args[0];
        int config_port = 22149;//Integer.parseInt(args[1]);
        
        ///
        
        Message inMessage, outMessage;
        ClientCom con = new ClientCom(config_ip, config_port);
        if (!con.open()) {
            System.exit(1);
        }
        outMessage = new Message(CONF);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != CONF) {
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        
        con.close();
        Variables v = null;
        try {
            v = Serializer.Deserialize(inMessage.getString());  
            ////
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Museum_server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Museum_server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        ServerCom scon, sconi;
        
        Logger_proxy log = new Logger_proxy(v, v.sockets.Logger_socket.ip, v.sockets.Logger_socket.port);
        Museum m = new Museum(log, v);
        Museum_Interface itfc = new Museum_Interface(m);
        scon = new ServerCom (v.sockets.Museum_socket.port);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        ClientProxy cp;
        while (true)
        { sconi = scon.accept ();                            // entrada em processo de escuta
          cp = new ClientProxy(sconi, itfc);    // lançamento do agente prestador do serviço
          cp.start ();
        }
    }
}
