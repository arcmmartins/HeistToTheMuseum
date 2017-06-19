package assignment2.server;

/**
 *
 * @author Alvaro e Nelson
 *
 */
import assignment2.DataStructures.*;
import assignment2.Interfaces.*;
import assignment2.proxy.ClientProxy;
import java.io.FileReader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

/**
 *
 * @author arcmm
 */
public class Config_server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Variables v = new Variables();
        JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(new FileReader("/home/sd0104/files/distro.json"));
            JSONObject json = (JSONObject) obj;
            Set<String> set = json.keySet();
            set.forEach((key) -> {
                    String value = (String) json.get(key);
                    if(key.contains("Museum_server")){
                        v.sockets.Museum_socket = v.new sk(value, v.sockets.Museum_socket.port);
                    }
                    else if(key.contains("Logger_server")){
                        v.sockets.Logger_socket = v.new sk(value, v.sockets.Logger_socket.port);
                    }
                    else if(key.contains("MasterThiefClient")){
                        v.sockets.MT_socket = v.new sk(value, v.sockets.MT_socket.port);
                    }
                    else if(key.contains("M_Ccs_server")){
                        v.sockets.Ccs_socket = v.new sk(value, v.sockets.Ccs_socket.port);
                    }
                    else if(key.contains("ThiefClient")){
                        v.sockets.OT0_socket = v.new sk(value, v.sockets.OT0_socket.port);
                        v.sockets.OT1_socket = v.new sk(value, v.sockets.OT1_socket.port);
                        v.sockets.OT2_socket = v.new sk(value, v.sockets.OT2_socket.port);
                        v.sockets.OT3_socket = v.new sk(value, v.sockets.OT3_socket.port);
                        v.sockets.OT4_socket = v.new sk(value, v.sockets.OT4_socket.port);
                        v.sockets.OT5_socket = v.new sk(value, v.sockets.OT5_socket.port);
                    }
                    else if(key.contains("M_AssaultParty_server") && key.contains(" 1 ")){
                        v.sockets.AssaultPart1_socket = v.new sk(value, v.sockets.AssaultPart1_socket.port);
                    }
                    else if(key.contains("M_AssaultParty_server" ) && key.contains(" 0 ")){
                        v.sockets.AssaultParty0_socket = v.new sk(value, v.sockets.AssaultParty0_socket.port);
                    }
                    else if(key.contains("M_Cs_server")){
                        v.sockets.Cs_socket = v.new sk(value, v.sockets.Cs_socket.port);
                    }
                });
            
        
            


            
        } catch (Exception ex) {
            Logger.getLogger(Config_server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ClientProxy cliProxy;                                // thread agente prestador do serviço
        ServerCom scon, sconi;                               // canais de comunicação
        Config_Interface itfc = new Config_Interface(v);
        //wait 4 all requests
        //complete file 
        //serialize file 
        //reply
        
        scon = new ServerCom (22149);                     // criação do canal de escuta e sua associação
        scon.start ();                                       // com o endereço público
        while (true)
        { sconi = scon.accept ();                            // entrada em processo de escuta
          cliProxy = new ClientProxy(sconi,itfc);    // lançamento do agente prestador do serviço
          cliProxy.start ();
        }
        
    }

}
