package com.gzoltar.instrumentation.testing.launch;

import com.gzoltar.instrumentation.Logger;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class RegistrySingleton {
   private static int port = 5000;
   private static Registry reg = null;

   public static synchronized void createSingleton() {
      if (reg == null) {
         while(true) {
            try {
               reg = LocateRegistry.createRegistry(port);
               return;
            } catch (ExportException var1) {
               ++port;
            } catch (RemoteException var2) {
               Logger.getInstance().err(var2.getMessage(), var2);
               reg = null;
               return;
            }
         }
      }
   }

   public static synchronized void register(String var0, Remote var1) {
      if (reg != null) {
         try {
            reg.rebind(var0, var1);
            return;
         } catch (Exception var2) {
            Logger.getInstance().err(var2.getMessage(), var2);
         }
      }

   }

   public static synchronized void unregister(String var0) {
      if (reg != null) {
         try {
            Remote var1 = reg.lookup(var0);
            reg.unbind(var0);
            if (var1 instanceof UnicastRemoteObject) {
               UnicastRemoteObject.unexportObject(var1, false);
            }

            return;
         } catch (NotBoundException | RemoteException var2) {
            Logger.getInstance().err(var2.getMessage(), var2);
         }
      }

   }

   public static synchronized int getPort() {
      return port;
   }
}
