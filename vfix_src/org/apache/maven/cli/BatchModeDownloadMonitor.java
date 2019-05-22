package org.apache.maven.cli;

import org.apache.maven.wagon.events.TransferEvent;

public class BatchModeDownloadMonitor extends AbstractConsoleDownloadMonitor {
   public void transferInitiated(TransferEvent transferEvent) {
      String message = transferEvent.getRequestType() == 6 ? "Uploading" : "Downloading";
      String url = transferEvent.getWagon().getRepository().getUrl();
      System.out.println(message + ": " + url + "/" + transferEvent.getResource().getName());
   }
}
