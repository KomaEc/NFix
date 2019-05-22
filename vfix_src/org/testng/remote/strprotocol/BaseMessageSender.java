package org.testng.remote.strprotocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.testng.remote.RemoteTestNG;

public abstract class BaseMessageSender implements IMessageSender {
   private boolean m_debug = false;
   protected Socket m_clientSocket;
   private String m_host;
   private int m_port;
   protected Object m_ackLock = new Object();
   protected OutputStream m_outStream;
   private PrintWriter m_outWriter;
   protected volatile InputStream m_inStream;
   protected volatile BufferedReader m_inReader;
   private BaseMessageSender.ReaderThread m_readerThread;
   private boolean m_ack;
   private int m_serial = 0;
   private String m_latestAck;

   public BaseMessageSender(String host, int port, boolean ack) {
      this.m_host = host;
      this.m_port = port;
      this.m_ack = ack;
   }

   public void connect() throws IOException {
      p("Waiting for Eclipse client on " + this.m_host + ":" + this.m_port);

      while(true) {
         try {
            this.m_clientSocket = new Socket(this.m_host, this.m_port);
            p("Received a connection from Eclipse on " + this.m_host + ":" + this.m_port);
            this.m_outStream = this.m_clientSocket.getOutputStream();
            this.m_outWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.m_outStream)));
            this.m_inStream = this.m_clientSocket.getInputStream();

            try {
               this.m_inReader = new BufferedReader(new InputStreamReader(this.m_inStream, "UTF-8"));
            } catch (UnsupportedEncodingException var3) {
               this.m_inReader = new BufferedReader(new InputStreamReader(this.m_inStream));
            }

            p("Connection established, starting reader thread");
            this.m_readerThread = new BaseMessageSender.ReaderThread();
            this.m_readerThread.start();
            return;
         } catch (ConnectException var5) {
            try {
               Thread.sleep(4000L);
            } catch (InterruptedException var4) {
               Thread.currentThread().interrupt();
            }
         }
      }
   }

   private void sendAdminMessage(String message) {
      this.m_outWriter.println(message);
      this.m_outWriter.flush();
   }

   public void sendAck() {
      p("Sending ACK " + this.m_serial);
      this.sendAdminMessage(">ACK");
   }

   public void sendStop() {
      this.sendAdminMessage(">STOP");
   }

   public void initReceiver() throws SocketTimeoutException {
      if (this.m_inStream != null) {
         p("Receiver already initialized");
      }

      ServerSocket serverSocket = null;

      try {
         p("initReceiver on port " + this.m_port);
         serverSocket = new ServerSocket(this.m_port);
         serverSocket.setSoTimeout(5000);
         Socket socket = serverSocket.accept();
         this.m_inStream = socket.getInputStream();
         this.m_inReader = new BufferedReader(new InputStreamReader(this.m_inStream));
         this.m_outStream = socket.getOutputStream();
         this.m_outWriter = new PrintWriter(new OutputStreamWriter(this.m_outStream));
      } catch (SocketTimeoutException var5) {
         try {
            serverSocket.close();
         } catch (IOException var4) {
         }

         throw var5;
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public void shutDown() {
      if (null != this.m_outStream) {
         try {
            this.m_outStream.close();
         } catch (IOException var3) {
         }

         this.m_outStream = null;
      }

      try {
         if (null != this.m_readerThread) {
            this.m_readerThread.interrupt();
         }

         if (null != this.m_inReader) {
            this.m_inReader.close();
            this.m_inReader = null;
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      try {
         if (null != this.m_clientSocket) {
            this.m_clientSocket.close();
            this.m_clientSocket = null;
         }
      } catch (IOException var4) {
         if (this.m_debug) {
            var4.printStackTrace();
         }
      }

   }

   protected void waitForAck() {
      if (this.m_ack) {
         try {
            p("Message sent, waiting for ACK...");
            synchronized(this.m_ackLock) {
               this.m_ackLock.wait();
            }

            p("... ACK received:" + this.m_latestAck);
         } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
         }
      }

   }

   private static void p(String msg) {
      if (RemoteTestNG.isVerbose()) {
         System.out.println("[BaseMessageSender] " + msg);
      }

   }

   private class ReaderThread extends Thread {
      public ReaderThread() {
         super("ReaderThread");
      }

      public void run() {
         try {
            BaseMessageSender.p("ReaderThread waiting for an admin message");
            String message = BaseMessageSender.this.m_inReader.readLine();
            BaseMessageSender.p("ReaderThread received admin message:" + message);

            for(; message != null; message = BaseMessageSender.this.m_inReader != null ? BaseMessageSender.this.m_inReader.readLine() : null) {
               if (BaseMessageSender.this.m_debug) {
                  BaseMessageSender.p("Admin message:" + message);
               }

               boolean acknowledge = message.startsWith(">ACK");
               boolean stop = ">STOP".equals(message);
               if (!acknowledge && !stop) {
                  BaseMessageSender.p("Received unknown message: '" + message + "'");
               } else {
                  if (acknowledge) {
                     BaseMessageSender.p("Received ACK:" + message);
                     BaseMessageSender.this.m_latestAck = message;
                  }

                  synchronized(BaseMessageSender.this.m_ackLock) {
                     BaseMessageSender.this.m_ackLock.notifyAll();
                  }

                  if (stop) {
                     break;
                  }
               }
            }
         } catch (IOException var7) {
            if (RemoteTestNG.isVerbose()) {
               var7.printStackTrace();
            }
         }

      }
   }
}
