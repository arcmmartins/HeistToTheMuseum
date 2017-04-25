package assignment2;

import assignment2.DataStructures.*;
import assignment2.Interfaces.*;
import assignment2.proxy.*;
import assignment2.Threads.*;
import java.util.logging.Level;

/**
 *
 * @author Alvaro Martins e Nelson Reverendo
 */
public class Assignment2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //instantiate monitors|threads
        Logger_proxy log = new Logger_proxy();
        M_AssaultParty_proxy ap[] = new M_AssaultParty_proxy[Variables.NAP];
        M_Ccs_proxy ccs = new M_Ccs_proxy((ItfLogger) log);
        M_Cs_proxy cs = new M_Cs_proxy();
        Museum_proxy museum = new Museum_proxy((ItfLogger) log);
        Thief thief[] = new Thief[Variables.M];

        //initialize Monitors
        for (int i = 0; i < ap.length; i++) {
            ap[i] = new M_AssaultParty_proxy((ItfLogger) log, i);
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
            java.util.logging.Logger.getLogger(Assignment2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
