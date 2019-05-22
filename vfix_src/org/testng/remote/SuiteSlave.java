package org.testng.remote;

import java.util.List;
import java.util.Properties;
import org.testng.ISuite;
import org.testng.TestNG;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.internal.PropertiesFile;
import org.testng.internal.Utils;
import org.testng.remote.adapter.DefaultWorkerAdapter;
import org.testng.remote.adapter.IWorkerAdapter;
import org.testng.xml.XmlSuite;

public class SuiteSlave {
   public static final String VERBOSE = "testng.verbose";
   public static final String SLAVE_ADPATER = "testng.slave.adpter";
   private final int m_verbose;
   private final IWorkerAdapter m_slaveAdpter;
   private final TestNG m_testng;

   public SuiteSlave(String propertiesFile, TestNG testng) throws TestNGException {
      try {
         this.m_testng = testng;
         PropertiesFile file = new PropertiesFile(propertiesFile);
         Properties properties = file.getProperties();
         this.m_verbose = Integer.parseInt(properties.getProperty("testng.verbose", "1"));
         String adapter = properties.getProperty("testng.slave.adpter");
         if (adapter == null) {
            this.m_slaveAdpter = new DefaultWorkerAdapter();
         } else {
            Class clazz = Class.forName(adapter);
            this.m_slaveAdpter = (IWorkerAdapter)clazz.newInstance();
         }

         this.m_slaveAdpter.init(properties);
      } catch (Exception var7) {
         throw new TestNGException("Fail to initialize slave mode", var7);
      }
   }

   public void waitForSuites() {
      try {
         while(true) {
            XmlSuite s = this.m_slaveAdpter.getSuite(Long.MAX_VALUE);
            if (s != null) {
               log("Processing " + s.getName());
               List<XmlSuite> suites = Lists.newArrayList();
               suites.add(s);
               this.m_testng.setXmlSuites(suites);
               List<ISuite> suiteRunners = this.m_testng.runSuitesLocally();
               ISuite sr = (ISuite)suiteRunners.get(0);
               log("Done processing " + s.getName());
               this.m_slaveAdpter.returnResult(sr);
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace(System.out);
      }
   }

   private static void log(String string) {
      Utils.log("", 2, string);
   }
}
