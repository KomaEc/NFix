package org.testng.remote.adapter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import org.testng.ISuite;
import org.testng.internal.Utils;
import org.testng.remote.ConnectionInfo;
import org.testng.xml.XmlSuite;

public class DefaultWorkerAdapter implements IWorkerAdapter {
   public static final String SLAVE_PORT = "slave.port";
   private ConnectionInfo m_connectionInfo;
   private int m_clientPort;

   public void init(Properties prop) throws Exception {
      this.m_clientPort = Integer.parseInt(prop.getProperty("slave.port", "0"));
      this.m_connectionInfo = resetSocket(this.m_clientPort, (ConnectionInfo)null);
   }

   public XmlSuite getSuite(long timeout) throws InterruptedException, IOException {
      try {
         return (XmlSuite)this.m_connectionInfo.getOis().readObject();
      } catch (ClassNotFoundException var4) {
         var4.printStackTrace(System.out);
         throw new RuntimeException(var4);
      } catch (IOException var5) {
         log("Connection closed " + var5.getMessage());
         this.m_connectionInfo = resetSocket(this.m_clientPort, this.m_connectionInfo);
         throw var5;
      }
   }

   public void returnResult(ISuite result) throws IOException {
      try {
         this.m_connectionInfo.getOos().writeObject(result);
      } catch (IOException var3) {
         log("Connection closed " + var3.getMessage());
         this.m_connectionInfo = resetSocket(this.m_clientPort, this.m_connectionInfo);
         throw var3;
      }
   }

   private static ConnectionInfo resetSocket(int clientPort, ConnectionInfo oldCi) throws IOException {
      ConnectionInfo result = new ConnectionInfo();
      ServerSocket serverSocket = new ServerSocket(clientPort);
      serverSocket.setReuseAddress(true);
      log("Waiting for connections on port " + clientPort);
      Socket socket = serverSocket.accept();
      result.setSocket(socket);
      return result;
   }

   private static void log(String string) {
      Utils.log("", 2, string);
   }
}
