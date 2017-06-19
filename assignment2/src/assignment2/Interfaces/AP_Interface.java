/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;
import static assignment2.DataStructures.MessageConstants.*;
import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
import assignment2.Monitors.M_AssaultParty;
/**
 *
 * @author Alvaro e Nelson
 */
public class AP_Interface implements ProxyInterface{
    private M_AssaultParty ap;

    /**
     * Constructor for the ap interface
     * @param ap the ap monitor
     */
    public AP_Interface(M_AssaultParty ap){
        this.ap = ap;
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
            case CRAWL_IN: 
                ret = ap.crawlIn(inMessage.getInteger(),
                                inMessage.getBoolean());
                outMessage = new Message(ACK, ret);
                break;
            case CRAWL_OUT: 
                ret = ap.crawlOut(inMessage.getInteger());
                outMessage = new Message(ACK, ret);
                break;
            case REVERSE_DIRECTION: 
                ap.reverseDirection(inMessage.getInteger());
                outMessage = new Message(ACK);
                break;
            case RESET: 
                ap.reset(inMessage.getInteger(), inMessage.getInteger1());
                outMessage = new Message(ACK);   
                break;
            case JOIN_GROUP: 
                ap.joinGroup(inMessage.getInteger(), inMessage.getInteger1());
                outMessage = new Message(ACK);   
                break;
            case GET_ID: 
                ret = ap.getID();
                outMessage = new Message(ACK,ret);   
                break;
            case GET_TARGET: 
                ret = ap.getTarget();
                System.out.println(ret);
                outMessage = new Message(ACK,ret);  
                break;
            case WAIT_ALL_ARRIVED: 
                ap.waitAllArrived();
                outMessage = new Message(ACK);
                break;
        }
        return outMessage;
    }
    
}
