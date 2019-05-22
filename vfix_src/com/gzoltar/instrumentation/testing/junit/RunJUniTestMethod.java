package com.gzoltar.instrumentation.testing.junit;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.runtime.JaCoCoWrapper;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import com.gzoltar.instrumentation.testing.launch.TestIMessage;
import com.gzoltar.instrumentation.testing.launch.TestResponse;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import major.mutation.Config;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

public class RunJUniTestMethod {
   public static void main(String... var0) {
      try {
         TestIMessage var6;
         String var1 = (var6 = (TestIMessage)LocateRegistry.getRegistry(Integer.parseInt(var0[0])).lookup(var0[1])).getTestClassName();
         String var2 = var6.getTestMethodName();
         ExecutionParameters var3;
         Config.__M_NO = (var3 = var6.getExecutionData()).getMutantID();
         Logger.LogLevel var4 = var3.getLogLevel();
         Logger.getInstance().setLogLevel(var4);
         Spectra.getInstance().setGranularity(var3.getGranularity());
         JaCoCoWrapper.executionParameters = var3;
         Request var7 = Request.method(Class.forName(var1, false, RunJUniTestMethod.class.getClassLoader()), var2);
         JUnitCore var9 = new JUnitCore();
         JUnitListener var10 = new JUnitListener();
         var9.addListener(var10);
         var9.run(var7);
         TestResponse var8;
         (var8 = new TestResponse()).setTestResult(var10.getTestResult());
         var6.setResponse(var8);
      } catch (NotBoundException | IllegalArgumentException | ClassNotFoundException | RemoteException var5) {
         Logger.getInstance().err("", var5);
         System.exit(1);
      }

      System.exit(0);
   }
}
