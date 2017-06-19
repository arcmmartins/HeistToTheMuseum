package serverSide;

import interfaces.ItfCcs;
import interfaces.ItfLogger;
import interfaces.Register;
import registry.RegistryConfiguration;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Alvaro e Nelson
 */
public class M_CcsServer {

    /**
     * Server that instantiates the Control and Collection site, starts it and registers it on the rmi registry
     * @param args the rmi registry host name
     */
    public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = args[0];
        rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;

        /* localização por nome do objecto remoto no serviço de registos RMI */
        ItfLogger loggerInterface = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            loggerInterface = (ItfLogger) registry.lookup(RegistryConfiguration.REGISTRY_LOGGER_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception finding logger:" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Logger is not registed:  " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        M_Ccs ccs = null;
        ItfCcs itfCcs = null;

        ccs = new M_Ccs(loggerInterface,rmiRegHostName);
        
        try {
            itfCcs = (ItfCcs) UnicastRemoteObject.exportObject(ccs, RegistryConfiguration.REGISTRY_CONTROL_COLLECTION_SITE_PORT);
        } catch (RemoteException e) {
            System.out.println("Control and collection site Stub Exception " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfiguration.REGISTRY_CONTROL_COLLECTION_SITE_NAME;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI register exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, itfCcs);
        } catch (RemoteException e) {
            System.out.println("Exception registing control and collection site " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Ccs is already registed " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
