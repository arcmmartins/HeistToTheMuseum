package assignment2.server;
import assignment2.DataStructures.*;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.Interfaces.*;
import assignment2.Monitors.*;
import assignment2.proxy.*;
import java.io.IOException;
import java.util.logging.Level;
/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Logger_server {

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
        
        
        ////
        
        
        
        
        ServerCom scon, sconi;
        Logger log = new Logger(v);
        Logger_Interface itfc = new Logger_Interface(log);
        scon = new ServerCom (v.sockets.Logger_socket.port);
        scon.start ();                                       // com o endereço público
        ClientProxy cp;
        while (true)
        { sconi = scon.accept ();                            // entrada em processo de escuta
          cp = new ClientProxy(sconi, itfc);    // lançamento do agente prestador do serviço
          cp.start ();
        }
        
        
    
    
    }
    
}
