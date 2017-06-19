package serverSide;
import Datastructures.*;
import interfaces.*;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import registry.RegistryConfiguration;
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
    private String reghostname;
    
    private VectorClock clock;
    /**
     * constructor initializes all the thieves as away
     * @param rmiRegHostName the registry hostname
     */
    public M_Cs(String rmiRegHostName){
        this.reghostname = rmiRegHostName;
        for(int i=0; i<Variables.M ; i++){
            status[i] = AWAY;
        }
        this.clock = new VectorClock(0);
    }

    /**
     * masterthief method that blocks the MT waiting for all the thieves to be
     * at the concentration site
     * and then marks them as needed
     */
    @Override
    public synchronized ReturnStruct prepareAssaultParty(VectorClock clk) {
        this.clock.update(clk);
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
        
        return new ReturnStruct(clock);
    }

    /**
     * thief method that blocks the thief waiting for the next action
     * @param thiefID the id of the thief
     * @return status code indicating wether the thief is going to join party
     *  or go home
     */
    @Override
    public synchronized ReturnStruct amINeeded(int thiefID, VectorClock clk) {
         this.clock.update(clk);
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
        return new ReturnStruct(clock, status[thiefID]);
    }

    /**
     * masterthief method that tells all the thieves to go home
     */
    @Override
    public synchronized ReturnStruct warnHeistOver(VectorClock clk) {
        this.clock.update(clk);
        for(int i =0; i<Variables.M;i++){
            status[i] = GOHOME;
        }
        notifyAll();
        return new ReturnStruct(clock);
    }

        /**
     * deregisters on rmi
     * @throws RemoteException because rmi
     */
    @Override
    public void shutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;
        
        rmiRegHostName = reghostname;
        rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger(M_Cs.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfiguration.REGISTRY_CONCENTRATION_SITE_NAME ;

        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Cs.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Cs.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("M_Cs registration exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Cs.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("M_Cs not bound exception: " + e.getMessage());
            java.util.logging.Logger.getLogger(M_Cs.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            java.util.logging.Logger.getLogger(M_Cs.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("M_Cs shutdown.");
    }

}
