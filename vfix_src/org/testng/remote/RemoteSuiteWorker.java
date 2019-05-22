package org.testng.remote;

import org.testng.SuiteRunner;
import org.testng.internal.remote.SlavePool;
import org.testng.remote.adapter.RemoteResultListener;
import org.testng.xml.XmlSuite;

public class RemoteSuiteWorker extends RemoteWorker implements Runnable {
   private XmlSuite m_suite;

   public RemoteSuiteWorker(XmlSuite suite, SlavePool slavePool, RemoteResultListener listener) {
      super(listener, slavePool);
      this.m_suite = suite;
   }

   public void run() {
      try {
         SuiteRunner result = this.sendSuite(this.getSlavePool().getSlave(), this.m_suite);
         this.m_listener.onResult(result);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
