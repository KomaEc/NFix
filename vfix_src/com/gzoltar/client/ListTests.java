package com.gzoltar.client;

import com.gzoltar.client.rmi.IMessage;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.client.utils.ClassType;
import com.gzoltar.client.utils.ClassUtils;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.testing.TestRunner;
import com.gzoltar.instrumentation.testing.jobs.JobHandler;
import com.gzoltar.instrumentation.testing.junit.JUnitRunner;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import com.gzoltar.instrumentation.testing.testng.TestNGRunner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.Map;

public class ListTests {
   public static void main(String... var0) {
      try {
         IMessage var10 = (IMessage)LocateRegistry.getRegistry(Integer.parseInt(var0[0])).lookup(var0[1]);
         Properties.getInstance().setValues(var10.getProperties());
         Logger.getInstance().setLogLevel(Properties.LOGLEVEL);
         Response var1 = new Response();
         Map var2 = ClassUtils.getTestClasses();
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_TEST_CLASSES, var2.size());
         if (var2.isEmpty()) {
            Logger.getInstance().warn("No test classes to return");
            var1.setListOfTestClasses(var2);
            var10.setResponse(var1);
            System.exit(0);
         }

         Logger.getInstance().info("* Found " + var2.size() + " test classes");
         ExecutionParameters var3 = new ExecutionParameters((String)null, (String[])null, (String[])null, (String[])null, (String[])null, (String)null, (String)null, (Logger.LogLevel)null, (Boolean)null, (Boolean)null, (Boolean)null, (Boolean)null, (Component.Granularity)null, (Integer)null);
         (new File(Properties.GZOLTAR_DATA_DIR)).mkdirs();
         PrintWriter var4 = new PrintWriter(Properties.GZOLTAR_DATA_DIR + System.getProperty("file.separator") + Properties.TESTS_FILE, "UTF-8");
         int var5 = 0;
         Iterator var6 = var2.keySet().iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            Object var8 = null;
            switch((ClassType.Type)var2.get(var7)) {
            case JUNIT:
               var8 = new JUnitRunner(var3);
               break;
            case TESTNG:
               var8 = new TestNGRunner(var3);
               break;
            default:
               Logger.getInstance().err("Type of test cases not supported");
            }

            assert var8 != null;

            for(Iterator var11 = ((TestRunner)var8).createJobs(var7).iterator(); var11.hasNext(); ++var5) {
               JobHandler var12 = (JobHandler)var11.next();
               var4.println(var12.getJobDefinition().getTestClassName() + (var12.getJobDefinition().getTestMethodName() != null ? "::" + var12.getJobDefinition().getTestMethodName() : ""));
            }
         }

         var4.close();
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_UNIT_TEST_CASES, var5);
         Logger.getInstance().info("* Found " + var5 + " test methods");
         var1.setListOfTestClasses(var2);
         var10.setResponse(var1);
      } catch (NotBoundException | IllegalArgumentException | IllegalAccessException | FileNotFoundException | UnsupportedEncodingException | RemoteException var9) {
         Logger.getInstance().err("", var9);
         System.exit(1);
      }

      System.exit(0);
   }
}
