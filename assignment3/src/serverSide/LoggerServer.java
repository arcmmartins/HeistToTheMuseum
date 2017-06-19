/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import interfaces.*;
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
public class LoggerServer {

    /**
     * Server that instantiates the logger, starts it and registers it on the rmi registry
     * @param args the rmi registry host name
     */
    public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = args[0];
        rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        ItfLogger itfLogger = new Logger(rmiRegHostName);
        try {
            itfLogger = (ItfLogger) UnicastRemoteObject.exportObject(itfLogger, RegistryConfiguration.REGISTRY_LOGGER_PORT);
            
        } catch (RemoteException e) {
            System.out.println("Logger Stub Exception " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfiguration.REGISTRY_LOGGER_NAME;
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
            reg.bind(nameEntryObject, itfLogger);
        } catch (RemoteException e) {
            System.out.println("Logger register exception" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Logger is already registed " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
