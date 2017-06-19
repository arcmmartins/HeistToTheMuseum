/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import interfaces.ItfTAssaultParty;
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
public class M_AssaultPartyServer {

    /**
     * Server that instantiates the Assaul_Party, starts it and registers it on the rmi registry
     * @param args the ID of the Assault_Party and the rmi registry host name
     */
    public static void main(String args[]) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = args[1];
        rmiRegPortNumb = RegistryConfiguration.RMI_REGISTRY_PORT;

        /* localização por nome do objecto remoto no serviço de registos RMI */
        ItfLogger loggerInterface = null;

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            loggerInterface = (ItfLogger) registry.lookup(RegistryConfiguration.REGISTRY_LOGGER_NAME);
        } catch (RemoteException e) {
            System.out.println("Exception finding logger: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Logger is not registed: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        ItfTAssaultParty itfParty = null;
        int apid = Integer.parseInt(args[0]);
        itfParty = new M_AssaultParty(loggerInterface, apid,rmiRegHostName);
        
        try {
            if(apid==0)
                itfParty = (ItfTAssaultParty) UnicastRemoteObject.exportObject(itfParty, RegistryConfiguration.REGISTRY_ASSAULT_PARTY0_PORT);
            else
                itfParty = (ItfTAssaultParty) UnicastRemoteObject.exportObject(itfParty, RegistryConfiguration.REGISTRY_ASSAULT_PARTY1_PORT);
            
        } catch (RemoteException e) {
            System.out.println("Assault party  Stub Exception " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfiguration.RMI_REGISTER_NAME;
        String nameEntryObject;
        if(apid == 0)
            nameEntryObject = RegistryConfiguration.REGISTRY_ASSAULT_PARTY0_NAME;
        else
            nameEntryObject = RegistryConfiguration.REGISTRY_ASSAULT_PARTY1_NAME;
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
            reg.bind(nameEntryObject, itfParty);
        } catch (RemoteException e) {
            System.out.println("Assault party register exception:  " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Assault party already registed" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
