/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;

import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.Monitors.Logger;

/**
 *
 * @author arcmm
 */
public class Logger_Interface implements ProxyInterface{
    private Logger log;

    /**
     * Constructor for the log interface
     * @param log monitor instance
     */
    public Logger_Interface(Logger log){
        this.log = log;
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
        switch(inMessage.getType()){
            case OTLOG:
                log.OTLog(inMessage.getInteger(), inMessage.getInteger1(), inMessage.getInteger2());
                outMessage = new Message(ACK);
                break;
            case MTLOG:
                log.MTLog(inMessage.getInteger());
                outMessage = new Message(ACK);
                break;
            case MUSEMLOG:
                log.MuseumLog(inMessage.getInteger(), inMessage.getInteger1(), inMessage.getInteger2());
                outMessage = new Message(ACK);
                break;
            case APLOG:
                log.APLog(inMessage.getInteger(), inMessage.getInteger1(), inMessage.getInteger2(), inMessage.getInteger3());
                outMessage = new Message(ACK);
                break;
            case FINAL_REPORT:
                log.finalReport(inMessage.getInteger());
                outMessage = new Message(ACK);
                break;
        }
        return outMessage;
        
    }
    
}
