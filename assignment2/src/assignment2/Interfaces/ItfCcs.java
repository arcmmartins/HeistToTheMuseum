
package assignment2.Interfaces;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfCcs {

    /**
     *
     * @param over if all rooms are empty
     * @return the next action to take
     */
    int appraiseSit(boolean over);

    /**
     * the start of operations initializes some variables
     */
    void startOperations();

    /**
     * waits for the groups to be full and then sends the thieves
     * to the assault
     */
    void sendAssaultParty();

    /**
     * blocks the masterthief waiting for all the groups to arrive
     */
    void takeARest();

    /**
     * method called after all the thieves returned
     * collects all the canvas and resets the groups for the next
     * assault to start
     * @return wether each assault party emptied the assigned room
     */
    boolean[] collectCanvas();

    /**
     * @return the total paintings stolen
     */
    int sumUpResults();
    
    /**
     *
     * @param hasPainting flag that indicates wether thief has painting
     * @param thiefID the id of the thief handing the canvas
     */
    void handACanvas(boolean hasPainting, int thiefID);

    /**
     * method that indicates the assault party for the thief and blocks it
     * while he waits for the theft to start
     * 
     * @param thiefID the id of the thief
     * @return the assault party that will incorporate the thief for the
     *  next excursion
     */
    int prepareExcursion(int thiefID);
}
