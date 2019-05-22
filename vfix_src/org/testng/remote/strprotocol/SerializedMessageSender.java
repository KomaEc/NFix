package org.testng.remote.strprotocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.testng.remote.RemoteTestNG;

public class SerializedMessageSender extends BaseMessageSender {
   public SerializedMessageSender(String host, int port) {
      super(host, port, false);
   }

   public SerializedMessageSender(String host, int port, boolean ack) {
      super(host, port, ack);
   }

   public void sendMessage(IMessage message) throws IOException {
      synchronized(this.m_outStream) {
         p("Sending message " + message);
         ObjectOutputStream oos = new ObjectOutputStream(this.m_outStream);
         oos.writeObject(message);
         oos.flush();
         this.waitForAck();
      }
   }

   public IMessage receiveMessage() throws IOException, ClassNotFoundException {
      IMessage result = null;

      try {
         ObjectInputStream ios = new ObjectInputStream(this.m_inStream);
         result = (IMessage)ios.readObject();
         p("Received message " + result);
      } catch (Exception var3) {
         if (RemoteTestNG.isVerbose()) {
            var3.printStackTrace();
         }
      }

      return result;
   }

   static void p(String s) {
      if (RemoteTestNG.isVerbose()) {
         System.out.println("[SerializedMessageSender] " + s);
      }

   }
}
