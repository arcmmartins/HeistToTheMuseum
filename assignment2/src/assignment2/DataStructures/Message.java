package assignment2.DataStructures;

import java.io.*;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   dos Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 *   estático dos threads barbeiro.
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable
{
    private static final long serialVersionUID = 1001L;

    private int msgType = 100;
    private int msgInteger = 100;
    private int msgInteger1 = 100;
    private int msgInteger2 = 100;
    private int msgInteger3 = 100;
    private String msgString= " ";

    private boolean[] msgArray = new boolean[2];

    private boolean msgBoolean = false;

    /**
     * message default constructor
     */
    public Message() {

    }

    /**
     * constructor for message that carries 1 int
     * @param msgType type of the message
     * @param msgInteger a integer
     */
    public Message(int msgType, int msgInteger) {
        this.msgType = msgType;
        this.msgInteger = msgInteger;

    }
    /**
     * Message that carries only a string
     * @param msgType type of the message
     * @param msgString the string to be carried
     */
    public Message(int msgType, String msgString) {
        this.msgType = msgType;
        this.msgString=msgString;

    }
    /**
     * Constructor for message that carries a int e 1 string
     * @param msgType type of the message
     * @param msgInteger a integer
     * @param msgString a string
     */
    public Message(int msgType, int msgInteger, String msgString) {
        this.msgType = msgType;
        this.msgInteger = msgInteger;
        this.msgString = msgString;
    }
    
    /**
     * constructor for message that carries 3 ints
     * @param msgType type of the message
     * @param msgInteger first integer
     * @param msgInteger1 second integer 
     * @param msgInteger2 third integer
     */
    public Message(int msgType, int msgInteger, int msgInteger1, int msgInteger2) {
        this.msgType = msgType;
        this.msgInteger = msgInteger;
        this.msgInteger1 = msgInteger1;
        this.msgInteger2 = msgInteger2;

    }
    
    /**
     * constructor for message that carries 4 ints
     * @param msgType type of the message
     * @param msgInteger first integer
     * @param msgInteger1 second integer 
     * @param msgInteger2 third integer
     * @param msgInteger3 fourth integer
     */
    public Message(int msgType, int msgInteger, int msgInteger1, int msgInteger2,int msgInteger3) {
        this.msgType = msgType;
        this.msgInteger = msgInteger;
        this.msgInteger1 = msgInteger1;
        this.msgInteger2 = msgInteger2;
        this.msgInteger3 = msgInteger3;

    }
    /**
     * Constructor for message that carries an array of booleans
     * @param msgType the type of the message
     * @param msgArray the array of booleans to be carried
     */
    public Message(int msgType, boolean[] msgArray) {
        this.msgType = msgType;
        this.msgArray = msgArray;

    }
    /**
     * Message for the void methods, carries only type
     * @param msgType the type of the message
     */
    public Message(int msgType) {
        this.msgType = msgType;

    }
    
    

    /**
     * Message that carries a boolean and a integer
     * @param msgType the type of the message
     * @param msgBoolean the boolean
     * @param msgInteger the integer
     */
    public Message(int msgType, boolean msgBoolean, int msgInteger) {
        this.msgType = msgType;
        this.msgBoolean = msgBoolean;
        this.msgInteger = msgInteger;

    }

    /**
     * message that carries a boolean
     * @param msgType the type of the message
     * @param msgBoolean the boolean
     */
    public Message(int msgType, boolean msgBoolean) {
        this.msgType = msgType;
        this.msgBoolean = msgBoolean;
    }

    /**
     * Message that carries 2 integers
     * @param msgType the type of the message
     * @param msgInteger first integer
     * @param msgInteger2 second integer
     */
    public Message(int msgType, int msgInteger, int msgInteger2) {
        this.msgType = msgType;
        this.msgInteger = msgInteger;
        this.msgInteger1 = msgInteger2;

    }

    /**
     * getter for type
     * @return the type
     */
    public int getType() {
        return this.msgType;
    }

    /**
     * getter for the first integer
     * @return the first integer
     */
    public int getInteger() {
        return this.msgInteger;
    }

    /**
     * getter for the second integer
     * @return the second integer
     */
    public int getInteger1() {
        return this.msgInteger1;
    }

    /**
     * getter for the third integer
     * @return the third integer
     */
    public int getInteger2() {
        return this.msgInteger2;
    }

    /**
     * getter for the third integer
     * @return the third integer
     */
    public int getInteger3() {
        return this.msgInteger3;
    }

    /**
     * getter for the boolean
     * @return the boolean
     */
    public boolean getBoolean() {
        return this.msgBoolean;
    }

    /**
     * getter for the boolean array
     * @return the boolean array
     */
    public boolean[] getBooleanArray() {
        return this.msgArray;
    }
    
    /**
     * getter for the string
     * @return the string
     */
    public String getString() {
        return this.msgString;
    }
}
