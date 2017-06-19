/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2.proxy;

import assignment2.DataStructures.Message;
import assignment2.DataStructures.MessageException;
import assignment2.DataStructures.ServerCom;
import assignment2.Interfaces.ProxyInterface;

/**
 *
 * @author Alvaro e Nelson
 */
public class ClientProxy extends Thread{
    private ProxyInterface itfc;
    private ServerCom sconi;
    private static int nProxy;

    /**
     * Client proxy constructor
     * @param sconi server connection instance
     * @param itfc interface that implements proxyinterface
     */
    public ClientProxy(ServerCom sconi, ProxyInterface itfc){
        super ("Proxy_" + getProxyId ());
        this.itfc = itfc;
        this.sconi = sconi;
    }
    
   @Override
   public void run ()
   {
      Message inMessage = null,                                      // mensagem de entrada
              outMessage = null;                      // mensagem de saída

      inMessage = (Message) sconi.readObject ();                     // ler pedido do cliente
      try
      { outMessage = itfc.processAndReply (inMessage);         // processá-lo
      }
      catch (MessageException e)
      { System.out.println("Thread " + getName () + ": " + e.getMessage () + "!");
        System.out.println (e.getMessageVal ().toString ());
        System.exit (1);
      }
      sconi.writeObject (outMessage);                                // enviar resposta ao cliente
      sconi.close ();                                                // fechar canal de comunicação
   }

  /**
   *  Geração do identificador da instanciação.
   *
   *    @return identificador da instanciação
   */

   private static int getProxyId ()
   {
      Class<assignment2.proxy.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
                                                           //   virtual de Java
      int proxyId;                                         // identificador da instanciação

      try
      { cl = (Class<assignment2.proxy.ClientProxy>) Class.forName ("assignment2.proxy.ClientProxy");
      }
      catch (ClassNotFoundException e)
      { System.out.println("O tipo de dados ClientProxy não foi encontrado!");
        e.printStackTrace ();
        System.exit (1);
      }

      synchronized (cl)
      { proxyId = nProxy;
        nProxy += 1;
      }

      return proxyId;
   }
    
}
