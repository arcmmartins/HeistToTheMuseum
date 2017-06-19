/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;

import static assignment2.DataStructures.MessageConstants.*;
import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
import assignment2.DataStructures.Variables;
import assignment2.DataStructures.Serializer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alvaro e Nelson
 */
public class Config_Interface implements ProxyInterface{
    private Variables v;

    /**
     * Constructor for the config interface
     * @param v variables instance
     */
    public Config_Interface(Variables v){
        this.v = v;
    }

    /**
     *
     * @param inMessage message to be processed
     * @return a reply of type Message
     * @throws MessageException required
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message m = null;
        try {
            m = new Message(CONF, Serializer.Serialize(v));
        } catch (IOException ex) {
            Logger.getLogger(Config_Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return m;
    }
    
}
