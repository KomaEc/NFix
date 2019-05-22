package org.testng.remote.adapter;

import java.io.IOException;
import java.util.Properties;
import org.testng.xml.XmlSuite;

public interface IMasterAdapter {
   void init(Properties var1) throws Exception;

   void runSuitesRemotely(XmlSuite var1, RemoteResultListener var2) throws IOException;

   void awaitTermination(long var1) throws InterruptedException;
}
