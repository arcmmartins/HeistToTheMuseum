/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.Interfaces;
import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
/**
 *
 * @author Alvaro e Nelson
 */
public interface ProxyInterface {

    /**
     *
     * @param inMessage message to be processed
     * @return a reply of type Message
     * @throws MessageException required
     */
    public Message processAndReply (Message inMessage) throws MessageException;
}
