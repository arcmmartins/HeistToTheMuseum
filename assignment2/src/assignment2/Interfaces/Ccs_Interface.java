/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;

import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
import assignment2.Monitors.M_Ccs;
import static assignment2.DataStructures.MessageConstants.*;
/**
 *
 * @author Alvaro e Nelson
 */
public class Ccs_Interface  implements ProxyInterface{
    private M_Ccs ccs;

    /**
     * Constructor for the interface
     * @param ccs the monitor instance
     */
    public Ccs_Interface(M_Ccs ccs){
        this.ccs = ccs;
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
            case START_OPERATIONS:
                ccs.startOperations();
                outMessage = new Message(ACK);
                break;
            case APPRAISE_SIT:
                ret = ccs.appraiseSit(inMessage.getBoolean());
                outMessage = new Message(ACK, ret);
                break;
            case SEND_ASSAULT_PARTY:
                ccs.sendAssaultParty();
                outMessage = new Message(ACK);
                break;
            case TAKE_A_REST:
                ccs.takeARest();
                outMessage = new Message(ACK);
                break;
            case COLLECT_CANVAS:
                outMessage = new Message(ACK, ccs.collectCanvas());
                break;
            case SUM_UP:
                ret = ccs.sumUpResults();
                outMessage = new Message(ACK, ret);
                break;
            case HAND_A_CANVAS:
                ccs.handACanvas(inMessage.getBoolean(), inMessage.getInteger());
                outMessage = new Message(ACK);
                break;
            case PREPARE_EXCURSION:
                ret = ccs.prepareExcursion(inMessage.getInteger());
                outMessage = new Message(ACK,ret );
                break;
                
        }
        return outMessage;
    }
    
}
