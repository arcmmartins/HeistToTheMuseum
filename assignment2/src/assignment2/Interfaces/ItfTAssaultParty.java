
package assignment2.Interfaces;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfTAssaultParty {

    /**
     * The crawl operation, it must ensure all thieves form a line and do not
     * go further than the specified distance between them
     * @param thiefID the id of the thief crawling
     * @param in whether its crawling in or crawling out
     * @return the distance left to the room
     * @throws Exception for debug purposes
     */
    int crawlIn(int thiefID, boolean in) throws Exception;

    /**
     * inverse of crawlin
     * @param thiefID the id of the thief crawling out
     * @return the distance left to the base
     * @throws Exception for debug purpose
     */
    int crawlOut(int thiefID) throws Exception;

    /**
     * reverts the direction of the thief so he can return to the base
     * @param thiefID the of the thief reversing direction
     */
    void reverseDirection(int thiefID);

    /**
     * resets the AP when the theft is over and we need to restart
     * @param target the target room for this AP
     * @param distance distance to the room
     */
    void reset(int target, int distance);

    /**
     * method that inserts the thief into the AP data structure
     * @param thiefID id of the thief
     * @param disp agility of the thief
     */
    void joinGroup(int thiefID, int disp);

    /**
     *
     * @return the id of the AP
     */
    int getID();

    /**
     * 
     * @return the current AP target
     */
    int getTarget();

    /**
     * blocks the thieves while other thieves are not at the room
     */
    void waitAllArrived();
}
