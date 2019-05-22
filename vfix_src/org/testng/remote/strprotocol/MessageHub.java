package org.testng.remote.strprotocol;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.testng.remote.RemoteTestNG;

public class MessageHub {
   private boolean m_debug = false;
   private IMessageSender m_messageSender;

   public MessageHub(IMessageSender messageSender) {
      this.m_messageSender = messageSender;
   }

   public void connect() throws IOException {
      this.m_messageSender.connect();
   }

   public void shutDown() {
      this.m_messageSender.shutDown();
   }

   public void sendMessage(IMessage message) {
      try {
         this.m_messageSender.sendMessage(message);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public IMessage receiveMessage() {
      IMessage result = null;

      try {
         result = this.m_messageSender.receiveMessage();
         this.m_messageSender.sendAck();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return result;
   }

   private static void p(String msg) {
      if (RemoteTestNG.isVerbose()) {
         System.out.println("[StringMessageSenderHelper] " + msg);
      }

   }

   public void setDebug(boolean debug) {
      this.m_debug = debug;
   }

   public void initReceiver() throws SocketTimeoutException {
      this.m_messageSender.initReceiver();
   }

   public IMessageSender getMessageSender() {
      return this.m_messageSender;
   }
}
