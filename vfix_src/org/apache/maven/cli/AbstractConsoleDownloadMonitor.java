package org.apache.maven.cli;

import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;
import org.codehaus.plexus.logging.AbstractLogEnabled;

public abstract class AbstractConsoleDownloadMonitor extends AbstractLogEnabled implements TransferListener {
   public void transferInitiated(TransferEvent transferEvent) {
      String message = transferEvent.getRequestType() == 6 ? "Uploading" : "Downloading";
      String url = transferEvent.getWagon().getRepository().getUrl();
      System.out.println(message + ": " + url + "/" + transferEvent.getResource().getName());
   }

   public void transferStarted(TransferEvent transferEvent) {
   }

   public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
   }

   public void transferCompleted(TransferEvent transferEvent) {
      long contentLength = transferEvent.getResource().getContentLength();
      if (contentLength != -1L) {
         String type = transferEvent.getRequestType() == 6 ? "uploaded" : "downloaded";
         String l = contentLength >= 1024L ? contentLength / 1024L + "K" : contentLength + "b";
         System.out.println(l + " " + type);
      }

   }

   public void transferError(TransferEvent transferEvent) {
      transferEvent.getException().printStackTrace();
   }

   public void debug(String message) {
   }
}
