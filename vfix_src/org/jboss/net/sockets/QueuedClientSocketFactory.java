package org.jboss.net.sockets;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.util.concurrent.Semaphore;

public class QueuedClientSocketFactory implements RMIClientSocketFactory, Externalizable {
   private transient Semaphore permits;
   private long numPermits;

   public QueuedClientSocketFactory() {
   }

   public QueuedClientSocketFactory(long nPermits) {
      this.permits = new Semaphore((int)nPermits, true);
      this.numPermits = nPermits;
   }

   public Socket createSocket(String host, int port) throws IOException {
      Socket var3;
      try {
         this.permits.acquire();
         var3 = new Socket(host, port);
      } catch (InterruptedException var7) {
         throw new IOException("Failed to acquire FIFOSemaphore for ClientSocketFactory");
      } finally {
         this.permits.release();
      }

      return var3;
   }

   public boolean equals(Object obj) {
      return obj instanceof QueuedClientSocketFactory;
   }

   public int hashCode() {
      return this.getClass().getName().hashCode();
   }

   public void writeExternal(ObjectOutput out) throws IOException {
      out.writeLong(this.numPermits);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
      this.numPermits = in.readLong();
      this.permits = new Semaphore((int)this.numPermits, true);
   }
}
