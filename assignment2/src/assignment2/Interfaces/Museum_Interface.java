/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;
import assignment2.Monitors.Museum;
import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
import static assignment2.DataStructures.MessageConstants.*;
/**
 *
 * @author Alvaro e Nelson
 */
public class Museum_Interface implements ProxyInterface{
    private Museum m;

    /**
     * Constructor for the museum interface
     * @param m the museum monitor
     */
    public Museum_Interface(Museum m){
        this.m = m;
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
            case ROLL_A_CANVAS:
                boolean rolled = m.rollACanvas(inMessage.getInteger());
                outMessage = new Message(ACK, rolled);
                break;
            case GET_N_ROOMS:
                ret = m.getNRooms();
                outMessage = new Message(ACK, ret);
                break;
            case GET_DISTANCE_TO_ROOM:
                ret = m.getDistanceToRoom(inMessage.getInteger());
                outMessage = new Message(ACK, ret);
                break;
        }
        return outMessage;
    }
    
}
