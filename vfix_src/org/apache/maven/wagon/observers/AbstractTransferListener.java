package org.apache.maven.wagon.observers;

import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;

public abstract class AbstractTransferListener implements TransferListener {
   public void transferInitiated(TransferEvent transferEvent) {
   }

   public void transferStarted(TransferEvent transferEvent) {
   }

   public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
   }

   public void transferCompleted(TransferEvent transferEvent) {
   }

   public void transferError(TransferEvent transferEvent) {
   }

   public void debug(String message) {
   }
}
