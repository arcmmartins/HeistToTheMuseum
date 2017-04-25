
package assignment2.Monitors;
import assignment2.DataStructures.Variables;
import assignment2.Interfaces.ItfCs;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class M_Cs implements ItfCs{

    
    
    /**
     * number of free thieves
     */
    private int freeThieves = 0;
    
    /**
     * thieves status
     * if 1 thief is present but not needed
     * 2 thief is present and needed
     * 0 thief is not present
     */
    private final int NEEDED = 2, GOHOME=1, AWAY=0; 
    private int status[] = new int[Variables.M];

    /**
     * constructor initializes all the thieves as away
     */
    public M_Cs(){
        for(int i=0; i<Variables.M ; i++){
            status[i] = AWAY;
        }
    }

    /**
     * masterthief method that blocks the MT waiting for all the thieves to be
     * at the concentration site
     * and then marks them as needed
     */
    @Override
    public synchronized void prepareAssaultParty() {
        while (freeThieves != Variables.M) {
            try {
                wait(); // espera que haja ladrões disponiveis para formar os grupos
            } catch (Exception e) {
            }
        }
        
        //depois de haverem indica que são precisos
        
        for(int i =0; i<Variables.M;i++)
            status[i] = NEEDED;
        notifyAll();
    }

    /**
     * thief method that blocks the thief waiting for the next action
     * @param thiefID the id of the thief
     * @return status code indicating wether the thief is going to join party
     *  or go home
     */
    @Override
    public synchronized int amINeeded(int thiefID) {
        if(status[thiefID] == AWAY){
            freeThieves++;
        }
        notifyAll();
        while (status[thiefID]!= NEEDED && status[thiefID]!= GOHOME) {
            try {
                wait(); // espera que haja ladrões disponiveis para formar os grupos
            } catch (Exception e) {
            }
        }
        if(status[thiefID] == NEEDED){
            freeThieves--;
            status[thiefID] = AWAY;        
        }
        return status[thiefID];
    }

    /**
     * masterthief method that tells all the thieves to go home
     */
    @Override
    public synchronized void warnHeistOver() {
        for(int i =0; i<Variables.M;i++){
            status[i] = GOHOME;
        }
        notifyAll();
    }
    

}
