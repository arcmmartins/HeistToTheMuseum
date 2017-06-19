
package assignment2.server;
import assignment2.DataStructures.ClientCom;
import assignment2.DataStructures.Message;
import static assignment2.DataStructures.MessageConstants.ACK;
import static assignment2.DataStructures.MessageConstants.CONF;
import assignment2.DataStructures.Serializer;
import assignment2.DataStructures.ServerCom;
import assignment2.DataStructures.Variables;
import assignment2.Interfaces.Cs_Interface;
import assignment2.Monitors.M_Cs;
import assignment2.proxy.ClientProxy;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_Cs_server {
     
    /**
     * main
     * @param args hostname where config server is
     */
    public static void main(String[] args) {
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
        Variables v = null;
        try {
            v = Serializer.Deserialize(inMessage.getString());  
            ////
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Museum_server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Museum_server.class.getName()).log(Level.SEVERE, null, ex);
        }
        con.close();
        
        //String config_ip = "l040101-ws"+args[0]+".ua.pt";
        //int config_port = Integer.parseInt(args[1]);
        ServerCom scon, sconi;
        
        M_Cs cs = new M_Cs(v);
        Cs_Interface itfc = new Cs_Interface(cs);
        scon = new ServerCom (v.sockets.Cs_socket.port);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        ClientProxy cp;
        while (true)
        { sconi = scon.accept ();                            // entrada em processo de escuta
          cp = new ClientProxy(sconi, itfc);    // lançamento do agente prestador do serviço
          cp.start ();
        }
    }
    
    
    
}
