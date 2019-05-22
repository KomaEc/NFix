package org.codehaus.plexus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.configuration.PlexusConfigurationResourceException;

public class PlexusContainerHost implements Runnable {
   private DefaultPlexusContainer container;
   private boolean shouldStop;
   private boolean isStopped;
   private Object shutdownSignal = new Object();

   public PlexusContainer start(ClassWorld classWorld, String configurationResource) throws FileNotFoundException, PlexusConfigurationResourceException, PlexusContainerException {
      this.container = this.getPlexusContainer();
      this.container.setClassWorld(classWorld);
      this.container.setConfigurationResource(new FileReader(configurationResource));
      this.customizeContainer(this.container);
      File plexusLogs = new File(System.getProperty("plexus.home") + "/logs");
      if (!plexusLogs.exists()) {
         plexusLogs.mkdirs();
      }

      this.container.initialize();
      this.container.start();
      Thread thread = new Thread(this);
      thread.setDaemon(false);
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
         public void run() {
            try {
               PlexusContainerHost.this.shutdown();
            } catch (Exception var2) {
            }

         }
      }));
      thread.start();
      return this.container;
   }

   protected DefaultPlexusContainer getPlexusContainer() {
      return new DefaultPlexusContainer();
   }

   protected void customizeContainer(PlexusContainer container) {
      container.addContextValue("plexus.home", System.getProperty("plexus.home"));
      container.addContextValue("plexus.work", System.getProperty("plexus.home") + "/work");
      container.addContextValue("plexus.logs", System.getProperty("plexus.home") + "/logs");
   }

   public void run() {
      synchronized(this) {
         while(!this.shouldStop) {
            try {
               this.wait();
            } catch (InterruptedException var6) {
            }
         }
      }

      synchronized(this) {
         this.isStopped = true;
         this.notifyAll();
      }
   }

   public void shutdown() {
      synchronized(this) {
         this.shouldStop = true;
         this.container.dispose();
         this.notifyAll();
      }

      synchronized(this) {
         while(!this.isStopped()) {
            try {
               this.wait();
            } catch (InterruptedException var6) {
            }
         }

         synchronized(this.shutdownSignal) {
            this.shutdownSignal.notifyAll();
         }

      }
   }

   public void waitForContainerShutdown() {
      while(!this.isStopped()) {
         try {
            synchronized(this.shutdownSignal) {
               this.shutdownSignal.wait();
            }
         } catch (InterruptedException var4) {
         }
      }

   }

   public boolean isStopped() {
      return this.isStopped;
   }

   public static void main(String[] args, ClassWorld classWorld) {
      if (args.length != 1) {
         System.err.println("usage: plexus <plexus.conf>");
         System.exit(1);
      }

      try {
         PlexusContainerHost host = new PlexusContainerHost();
         host.start(classWorld, args[0]);
         host.waitForContainerShutdown();
      } catch (Exception var3) {
         var3.printStackTrace();
         System.exit(2);
      }

   }
}
