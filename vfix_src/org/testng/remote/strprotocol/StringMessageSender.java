package org.testng.remote.strprotocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.testng.remote.RemoteTestNG;

public class StringMessageSender extends BaseMessageSender {
   private PrintWriter writer;

   public StringMessageSender(String host, int port) {
      super(host, port, false);
   }

   public StringMessageSender(String host, int port, boolean ack) {
      super(host, port, ack);
   }

   public void sendMessage(IMessage message) {
      if (this.m_outStream == null) {
         throw new IllegalStateException("Trying to send a message on a shutdown sender");
      } else {
         if (this.writer == null) {
            try {
               this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.m_outStream, "UTF-8")), false);
            } catch (UnsupportedEncodingException var7) {
               this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.m_outStream)), false);
            }
         }

         String msg = ((IStringMessage)message).getMessageAsString();
         if (RemoteTestNG.isVerbose()) {
            p("Sending message:" + message);
            p("  String version:" + msg);
            StringBuffer buf = new StringBuffer();

            for(int i = 0; i < msg.length(); ++i) {
               if (1 == msg.charAt(i)) {
                  p("  word:[" + buf.toString() + "]");
                  buf.delete(0, buf.length());
               } else {
                  buf.append(msg.charAt(i));
               }
            }

            p("  word:[" + buf.toString() + "]");
         }

         synchronized(this.m_ackLock) {
            this.writer.println(msg);
            this.writer.flush();
            this.waitForAck();
         }
      }
   }

   private static void p(String msg) {
      if (RemoteTestNG.isVerbose()) {
         System.out.println("[StringMessageSender] " + msg);
      }

   }

   public IMessage receiveMessage() {
      IMessage result = null;
      if (this.m_inReader == null) {
         try {
            this.m_inReader = new BufferedReader(new InputStreamReader(this.m_inStream, "UTF-8"));
         } catch (UnsupportedEncodingException var4) {
            this.m_inReader = new BufferedReader(new InputStreamReader(this.m_inStream));
         }
      }

      try {
         result = this.receiveMessage(this.m_inReader.readLine());
      } catch (IOException var3) {
         this.handleThrowable(var3);
      }

      return result;
   }

   protected void handleThrowable(Throwable cause) {
      if (RemoteTestNG.isVerbose()) {
         cause.printStackTrace();
      }

   }

   private IMessage receiveMessage(String message) {
      if (message == null) {
         return null;
      } else {
         IMessage result = null;
         int messageType = MessageHelper.getMessageType(message);
         if (messageType < 10) {
            result = MessageHelper.unmarshallGenericMessage(message);
         } else if (messageType < 100) {
            result = MessageHelper.createSuiteMessage(message);
         } else if (messageType < 1000) {
            result = MessageHelper.createTestMessage(message);
         } else {
            result = MessageHelper.unmarshallTestResultMessage(message);
         }

         p("receiveMessage() received:" + result);
         return (IMessage)result;
      }
   }
}
