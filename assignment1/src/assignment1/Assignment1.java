package assignment1;

import assignment1.DataStructures.*;
import assignment1.Interfaces.*;
import assignment1.Monitors.*;
import assignment1.Threads.*;
import java.util.logging.Level;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Assignment1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //instantiate monitors|threads
        Logger log = new Logger();
        M_AssaultParty ap[] = new M_AssaultParty[Variables.NAP];
        M_Ccs ccs = new M_Ccs((ItfLogger) log);
        M_Cs cs = new M_Cs();
        Museum museum = new Museum((ItfLogger) log);
        Thief thief[] = new Thief[Variables.M];

        //initialize Monitors
        for (int i = 0; i < ap.length; i++) {
            ap[i] = new M_AssaultParty((ItfLogger) log, i);
        }

        //initialize threads
        MasterThief mthief = new MasterThief((ItfCcs) ccs, (ItfLogger) log, (ItfCs) cs, (ItfTAssaultParty[]) ap, (ItfMuseum) museum);
        for (int i = 0; i < thief.length; i++) {
            thief[i] = new Thief((ItfMuseum) museum, (ItfTAssaultParty[]) ap, (ItfCcs) ccs, (ItfCs) cs, i, (ItfLogger) log);
        }

        //start them all
        mthief.start();
        for (Thief t : thief) {
            t.start();
        }

        /* aguardar o fim da simulação */
        for (int i = 0; i < Variables.M; i++) {
            try {
                thief[i].join();
            } catch (InterruptedException e) {
            }
        }
        try {
            mthief.join();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Assignment1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
