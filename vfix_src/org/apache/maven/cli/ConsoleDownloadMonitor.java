package org.apache.maven.cli;

import org.apache.maven.wagon.events.TransferEvent;

public class ConsoleDownloadMonitor extends AbstractConsoleDownloadMonitor {
   private long complete;

   public void transferInitiated(TransferEvent transferEvent) {
      String message = transferEvent.getRequestType() == 6 ? "Uploading" : "Downloading";
      String url = transferEvent.getWagon().getRepository().getUrl();
      System.out.println(message + ": " + url + "/" + transferEvent.getResource().getName());
      this.complete = 0L;
   }

   public void transferStarted(TransferEvent transferEvent) {
   }

   public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
      long total = transferEvent.getResource().getContentLength();
      this.complete += (long)length;
      if (total >= 1024L) {
         System.out.print(this.complete / 1024L + "/" + (total == -1L ? "?" : total / 1024L + "K") + "\r");
      } else {
         System.out.print(this.complete + "/" + (total == -1L ? "?" : total + "b") + "\r");
      }

   }
}
