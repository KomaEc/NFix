package com.gzoltar.client;

import com.gzoltar.client.rmi.IMessage;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.client.utils.ClassUtils;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.runtime.JaCoCoWrapper;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class ListComponents {
   public static void main(String... var0) {
      try {
         IMessage var5 = (IMessage)LocateRegistry.getRegistry(Integer.parseInt(var0[0])).lookup(var0[1]);
         Response var1 = new Response();
         Properties.getInstance().setValues(var5.getProperties());
         Logger.getInstance().setLogLevel(Properties.LOGLEVEL);
         Spectra var2;
         (var2 = Spectra.getInstance()).setGranularity(Properties.GRANULARITY);
         List var3 = ClassUtils.getNonTestClasses();
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_CLASSES, var3.size());
         JaCoCoWrapper.executionParameters = new ExecutionParameters(var5.getClassPath(), Properties.TARGETPACKAGES, (String[])var3.toArray(new String[0]), Properties.TESTPACKAGES, (String[])null, (String)null, (String)null, Logger.getInstance().getLogLevel(), Properties.INSTRUMENT_TEST_CLASSES, Properties.INCLUDE_DEPRECATED_CLASSES, Properties.INCLUDE_DEPRECATED_METHODS, Properties.UNIQUE_LINE_NUMBERS, Properties.GRANULARITY, Properties.TEST_TIMEOUT);
         var2.setComponentCounts(JaCoCoWrapper.registerCoverage(new byte[1], true).values());
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_COMPONENTS, var2.getNumberOfComponents());
         Logger.getInstance().info("* Collected " + var2.getNumberOfComponents() + " components");
         if (Properties.PRINT_SPECTRA) {
            Logger.getInstance().info("* Writing components to '" + Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.SPECTRA_FILE + "'");
            var2.printComponents(Properties.GZOLTAR_DATA_DIR, Properties.SPECTRA_FILE, Properties.VERBOSE_SPECTRA, false);
         }

         var1.setSpectra(var2);
         var1.setListOfComponents(var2.getComponents());
         var5.setResponse(var1);
      } catch (NotBoundException | IllegalArgumentException | IllegalAccessException | RemoteException var4) {
         Logger.getInstance().err(var4.getMessage(), var4);
         System.exit(1);
      }

      System.exit(0);
   }
}
