package org.testng.remote.adapter;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.internal.remote.SlavePool;
import org.testng.internal.thread.ThreadUtil;
import org.testng.remote.RemoteSuiteWorker;
import org.testng.xml.XmlSuite;

public class DefaultMastertAdapter implements IMasterAdapter {
   public static final String HOSTS = "testng.hosts";
   private String[] m_hosts;
   private final SlavePool m_slavePool = new SlavePool();
   private final List<Runnable> m_workers = Lists.newArrayList();

   public void init(Properties properties) {
      String hostLine = properties.getProperty("testng.hosts");
      this.m_hosts = hostLine.split(" ");
      Socket[] sockets = new Socket[this.m_hosts.length];

      for(int i = 0; i < this.m_hosts.length; ++i) {
         String host = this.m_hosts[i];
         String[] s = host.split(":");

         try {
            sockets[i] = new Socket(s[0], Integer.parseInt(s[1]));
         } catch (UnknownHostException | NumberFormatException var9) {
            var9.printStackTrace(System.out);
         } catch (IOException var10) {
            Utils.error("Couldn't connect to " + host + ": " + var10.getMessage());
         }
      }

      try {
         this.m_slavePool.addSlaves(sockets);
      } catch (IOException var8) {
         var8.printStackTrace(System.out);
      }

   }

   public void runSuitesRemotely(XmlSuite suite, RemoteResultListener listener) throws IOException {
      this.m_workers.add(new RemoteSuiteWorker(suite, this.m_slavePool, listener));
   }

   public void awaitTermination(long timeout) throws InterruptedException {
      ThreadUtil.execute(this.m_workers, 1, 10000L, false);
   }
}
