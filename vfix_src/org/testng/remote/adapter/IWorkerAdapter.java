package org.testng.remote.adapter;

import java.io.IOException;
import java.util.Properties;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

public interface IWorkerAdapter {
   void init(Properties var1) throws Exception;

   XmlSuite getSuite(long var1) throws InterruptedException, IOException;

   void returnResult(ISuite var1) throws IOException;
}
