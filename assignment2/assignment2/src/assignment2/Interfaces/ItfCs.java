
package assignment2.Interfaces;


/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public interface ItfCs {

    /**
     * masterthief method that blocks the MT waiting for all the thieves to be
     * at the concentration site
     * and then marks them as needed
     */
    void prepareAssaultParty();

    /**
     * thief method that blocks the thief waiting for the next action
     * @param thiefID the id of the thief
     * @return status code indicating wether the thief is going to join party
     *  or go home
     */
    int amINeeded(int thiefID);

    /**
     * masterthief method that tells all the thieves to go home
     */
    public void warnHeistOver();
}
