package org.testng.internal.remote;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import org.testng.collections.Maps;
import org.testng.remote.ConnectionInfo;

public class SlavePool {
   private static SocketLinkedBlockingQueue m_hosts = new SocketLinkedBlockingQueue();
   private static Map<Socket, ConnectionInfo> m_connectionInfos = Maps.newHashMap();

   public void addSlaves(Socket[] slaves) throws IOException {
      Socket[] arr$ = slaves;
      int len$ = slaves.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Socket s = arr$[i$];
         this.addSlave(s);
      }

   }

   public void addSlave(Socket s) {
      if (s != null) {
         ConnectionInfo ci = new ConnectionInfo();
         ci.setSocket(s);
         this.addSlave(s, ci);
      }
   }

   private void addSlave(Socket s, ConnectionInfo ci) {
      m_hosts.add(s);
      m_connectionInfos.put(s, ci);
   }

   public ConnectionInfo getSlave() {
      ConnectionInfo result = null;
      Socket host = null;

      try {
         host = (Socket)m_hosts.take();
         result = (ConnectionInfo)m_connectionInfos.get(host);
      } catch (InterruptedException var4) {
         var4.printStackTrace();
         Thread.currentThread().interrupt();
      }

      return result;
   }

   public void returnSlave(ConnectionInfo slave) throws IOException {
      m_hosts.add(slave.getSocket());
   }
}
