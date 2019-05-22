package org.apache.maven.wagon.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TransferEventSupport {
   private final List listeners = new ArrayList();

   public synchronized void addTransferListener(TransferListener listener) {
      if (listener != null) {
         this.listeners.add(listener);
      }

   }

   public synchronized void removeTransferListener(TransferListener listener) {
      this.listeners.remove(listener);
   }

   public synchronized boolean hasTransferListener(TransferListener listener) {
      return this.listeners.contains(listener);
   }

   public synchronized void fireTransferStarted(TransferEvent transferEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         TransferListener listener = (TransferListener)iter.next();
         listener.transferStarted(transferEvent);
      }

   }

   public synchronized void fireTransferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         TransferListener listener = (TransferListener)iter.next();
         listener.transferProgress(transferEvent, buffer, length);
      }

   }

   public synchronized void fireTransferCompleted(TransferEvent transferEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         TransferListener listener = (TransferListener)iter.next();
         listener.transferCompleted(transferEvent);
      }

   }

   public synchronized void fireTransferError(TransferEvent transferEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         TransferListener listener = (TransferListener)iter.next();
         listener.transferError(transferEvent);
      }

   }

   public synchronized void fireDebug(String message) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         TransferListener listener = (TransferListener)iter.next();
         listener.debug(message);
      }

   }

   public synchronized void fireTransferInitiated(TransferEvent transferEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         TransferListener listener = (TransferListener)iter.next();
         listener.transferInitiated(transferEvent);
      }

   }
}
