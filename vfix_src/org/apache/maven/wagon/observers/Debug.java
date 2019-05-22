package org.apache.maven.wagon.observers;

import java.io.PrintStream;
import org.apache.maven.wagon.events.SessionEvent;
import org.apache.maven.wagon.events.SessionListener;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;

public class Debug implements SessionListener, TransferListener {
   private PrintStream out;
   long timestamp;
   long transfer;

   public Debug() {
      this(System.out);
   }

   public Debug(PrintStream out) {
      this.out = out;
   }

   public void sessionOpening(SessionEvent sessionEvent) {
   }

   public void sessionOpened(SessionEvent sessionEvent) {
      this.out.println(sessionEvent.getWagon().getRepository().getUrl() + " - Session: Opened  ");
   }

   public void sessionDisconnecting(SessionEvent sessionEvent) {
      this.out.println(sessionEvent.getWagon().getRepository().getUrl() + " - Session: Disconnecting  ");
   }

   public void sessionDisconnected(SessionEvent sessionEvent) {
      this.out.println(sessionEvent.getWagon().getRepository().getUrl() + " - Session: Disconnected");
   }

   public void sessionConnectionRefused(SessionEvent sessionEvent) {
      this.out.println(sessionEvent.getWagon().getRepository().getUrl() + " - Session: Connection refused");
   }

   public void sessionLoggedIn(SessionEvent sessionEvent) {
      this.out.println(sessionEvent.getWagon().getRepository().getUrl() + " - Session: Logged in");
   }

   public void sessionLoggedOff(SessionEvent sessionEvent) {
      this.out.println(sessionEvent.getWagon().getRepository().getUrl() + " - Session: Logged off");
   }

   public void debug(String message) {
      this.out.println(message);
   }

   public void transferInitiated(TransferEvent transferEvent) {
   }

   public void transferStarted(TransferEvent transferEvent) {
      this.timestamp = transferEvent.getTimestamp();
      this.transfer = 0L;
      String message;
      if (transferEvent.getRequestType() == 5) {
         message = "Downloading: " + transferEvent.getResource().getName() + " from " + transferEvent.getWagon().getRepository().getUrl();
         this.out.println(message);
         this.out.println("");
      } else {
         message = "Uploading: " + transferEvent.getResource().getName() + " to " + transferEvent.getWagon().getRepository().getUrl();
         this.out.println(message);
         this.out.println("");
      }

   }

   public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
      this.out.print("#");
      this.transfer += (long)length;
   }

   public void transferCompleted(TransferEvent transferEvent) {
      double duration = (double)(transferEvent.getTimestamp() - this.timestamp) / 1000.0D;
      this.out.println();
      String message = "Transfer finished. " + this.transfer + " bytes copied in " + duration + " seconds";
      this.out.println(message);
   }

   public void transferError(TransferEvent transferEvent) {
      this.out.println(" Transfer error: " + transferEvent.getException());
   }

   public void sessionError(SessionEvent sessionEvent) {
      this.out.println(" Session error: " + sessionEvent.getException());
   }

   public PrintStream getOut() {
      return this.out;
   }
}
