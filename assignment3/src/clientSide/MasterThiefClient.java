package clientSide;

import registry.*;
import Datastructures.*;
import interfaces.*;
import static java.lang.System.out;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Alvaro e Nelson
 */
public class MasterThiefClient {

    /**
     * main of the masterthief,
     * if fetches all the interfaces, instantiates the master thief and starts it
     * @param args requires the rmi Reg port
     */
    public static void main(String[] args) {

        // Initialise RMI configurations
        String rmiRegHostName =args[0];
        int rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;
        Registry registry = null;
        // Initialise RMI invocations
        ItfCcs itfCcs = null;
        ItfCs itfCs = null;
        ItfLogger itfLogger = null;
        ItfMuseum itfMuseum = null;
        ItfTAssaultParty aps[] = new ItfTAssaultParty[Variables.NAP];
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            itfCcs = (ItfCcs) registry.lookup(RegistryConfiguration.REGISTRY_CONTROL_COLLECTION_SITE_NAME);
            itfCs = (ItfCs) registry.lookup(RegistryConfiguration.REGISTRY_CONCENTRATION_SITE_NAME);
            itfLogger = (ItfLogger) registry.lookup(RegistryConfiguration.REGISTRY_LOGGER_NAME);
            itfMuseum = (ItfMuseum) registry.lookup(RegistryConfiguration.REGISTRY_MUSEUM_NAME);
            aps[0] = (ItfTAssaultParty) registry.lookup(RegistryConfiguration.REGISTRY_ASSAULT_PARTY0_NAME);
            aps[1] = (ItfTAssaultParty) registry.lookup(RegistryConfiguration.REGISTRY_ASSAULT_PARTY1_NAME);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ThiefClient.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        MasterThief mt = new MasterThief(itfCcs, itfLogger, itfCs, aps, itfMuseum);
        mt.start();
        try {
            mt.join();
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //sends the shutdown signal to all monitors
        try {
            itfCcs.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            itfCs.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            itfLogger.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            itfMuseum.shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aps[0].shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            aps[1].shutdown();
        } catch (RemoteException ex) {
            Logger.getLogger(MasterThiefClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Finished");
    }
}
