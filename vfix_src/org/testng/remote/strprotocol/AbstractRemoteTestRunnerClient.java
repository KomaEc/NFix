package org.testng.remote.strprotocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.testng.TestNGException;

public abstract class AbstractRemoteTestRunnerClient {
   protected IRemoteSuiteListener[] m_suiteListeners;
   protected IRemoteTestListener[] m_testListeners;
   private ServerSocket fServerSocket;
   private Socket fSocket;
   private AbstractRemoteTestRunnerClient.ServerConnection m_serverConnection;

   public synchronized void startListening(IRemoteSuiteListener[] suiteListeners, IRemoteTestListener[] testListeners, AbstractRemoteTestRunnerClient.ServerConnection serverConnection) {
      this.m_suiteListeners = suiteListeners;
      this.m_testListeners = testListeners;
      this.m_serverConnection = serverConnection;
      serverConnection.start();
   }

   public IRemoteSuiteListener[] getSuiteListeners() {
      return this.m_suiteListeners;
   }

   public IRemoteTestListener[] getTestListeners() {
      return this.m_testListeners;
   }

   private synchronized void shutdown() {
      try {
         if (this.fSocket != null) {
            this.fSocket.close();
            this.fSocket = null;
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      try {
         if (this.fServerSocket != null) {
            this.fServerSocket.close();
            this.fServerSocket = null;
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public boolean isRunning() {
      return this.m_serverConnection.getMessageSender() != null;
   }

   public synchronized void stopTest() {
      if (this.isRunning()) {
         this.m_serverConnection.getMessageSender().sendStop();
         this.shutdown();
      }

   }

   protected abstract void notifyStart(GenericMessage var1);

   protected abstract void notifySuiteEvents(SuiteMessage var1);

   protected abstract void notifyTestEvents(TestMessage var1);

   protected abstract void notifyResultEvents(TestResultMessage var1);

   public abstract class ServerConnection extends Thread {
      private MessageHub m_messageHub;

      public ServerConnection(IMessageSender messageMarshaller) {
         super("TestNG - ServerConnection");
         this.m_messageHub = new MessageHub(messageMarshaller);
      }

      IMessageSender getMessageSender() {
         return this.m_messageHub != null ? this.m_messageHub.getMessageSender() : null;
      }

      public void run() {
         try {
            for(IMessage message = this.m_messageHub.receiveMessage(); message != null; message = this.m_messageHub.receiveMessage()) {
               if (message instanceof GenericMessage) {
                  AbstractRemoteTestRunnerClient.this.notifyStart((GenericMessage)message);
               } else if (message instanceof SuiteMessage) {
                  AbstractRemoteTestRunnerClient.this.notifySuiteEvents((SuiteMessage)message);
               } else if (message instanceof TestMessage) {
                  AbstractRemoteTestRunnerClient.this.notifyTestEvents((TestMessage)message);
               } else {
                  if (!(message instanceof TestResultMessage)) {
                     throw new TestNGException("Unknown message type:" + message);
                  }

                  AbstractRemoteTestRunnerClient.this.notifyResultEvents((TestResultMessage)message);
               }
            }
         } finally {
            this.m_messageHub.shutDown();
            this.m_messageHub = null;
         }

      }

      protected abstract void handleThrowable(Throwable var1);
   }
}
