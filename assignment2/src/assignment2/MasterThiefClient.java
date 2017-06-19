/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import assignment2.DataStructures.*;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.proxy.*;
import assignment2.Threads.MasterThief;
import assignment2.server.Museum_server;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Alvaro e Nelson
 */
public class MasterThiefClient {

    /**
     *
     * @param args hostname of the config server
     */
    public static void main(String[] args) {
        
        String config_ip = args[0];
        int config_port = 22149;//Integer.parseInt(args[1]);
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
        
        Logger_proxy log = new Logger_proxy(v, v.sockets.Logger_socket.ip, v.sockets.Logger_socket.port);
        M_AssaultParty_proxy APs[] = new M_AssaultParty_proxy[v.NAP];
        APs[0] = new M_AssaultParty_proxy(v, 0, v.sockets.AssaultParty0_socket.ip, v.sockets.AssaultParty0_socket.port);
        APs[1] = new M_AssaultParty_proxy(v, 1, v.sockets.AssaultPart1_socket.ip, v.sockets.AssaultPart1_socket.port);
        M_Ccs_proxy ccs = new M_Ccs_proxy(v, v.sockets.Ccs_socket.ip, v.sockets.Ccs_socket.port);
        M_Cs_proxy cs = new M_Cs_proxy(v, v.sockets.Cs_socket.ip, v.sockets.Cs_socket.port);
        Museum_proxy museum = new Museum_proxy(v, v.sockets.Museum_socket.ip, v.sockets.Museum_socket.port);
        
        MasterThief mt = new MasterThief(ccs, log, cs, APs, museum, v);
        mt.start();
    }
}
