/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;

import assignment2.DataStructures.Message;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.DataStructures.MessageException;
import assignment2.Monitors.M_Cs;

/**
 *
 * @author Alvaro e Nelson
 */
public class Cs_Interface implements ProxyInterface{
    private M_Cs cs;

    /**
     * Cosntructor for the cs interface
     * @param cs the cs monitor
     */
    public Cs_Interface(M_Cs cs){
        this.cs = cs;
    }
    
    /**
     *
     * @param inMessage message to be processed
     * @return a reply of type Message
     * @throws MessageException required
     */
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;
        int ret;
        switch(inMessage.getType()){
            case PREPARE_ASSAULT_PARTY:
                cs.prepareAssaultParty();
                outMessage = new Message(ACK);
                break;
            case AM_I_NEEDED:
                ret = cs.amINeeded(inMessage.getInteger());
                outMessage = new Message(ACK, ret);
                break;
            case WARN_HEIST_OVER:
                cs.warnHeistOver();
                outMessage = new Message(ACK);
                break;
        }
        return outMessage;
    }
    
    
}
