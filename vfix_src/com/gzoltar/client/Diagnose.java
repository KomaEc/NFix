package com.gzoltar.client;

import com.gzoltar.client.agent.AgentJar;
import com.gzoltar.client.diag.strategy.CoverageStrategy;
import com.gzoltar.client.diag.strategy.MutationStrategy;
import com.gzoltar.client.mutation.CustomMutationBackend;
import com.gzoltar.client.mutation.Major;
import com.gzoltar.client.mutation.MutationBackend;
import com.gzoltar.client.mutation.Pit;
import com.gzoltar.client.rmi.IMessage;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.client.statistics.OutputStatisticsVariable;
import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.client.utils.ClassType;
import com.gzoltar.client.utils.ClassUtils;
import com.gzoltar.client.utils.ProgressBar;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.components.Mutant;
import com.gzoltar.instrumentation.runtime.JaCoCoWrapper;
import com.gzoltar.instrumentation.spectra.FilterComponentsTriggeredByFailingTestCases;
import com.gzoltar.instrumentation.spectra.FilterTestCasesWithoutCoverage;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.instrumentation.testing.TestRunner;
import com.gzoltar.instrumentation.testing.jobs.JobDefinition;
import com.gzoltar.instrumentation.testing.jobs.JobHandler;
import com.gzoltar.instrumentation.testing.junit.JUnitRunner;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import com.gzoltar.instrumentation.testing.testng.TestNGRunner;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.instrumentation.utils.WildcardMatcher;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Diagnose {
   public static void main(String... var0) {
      try {
         IMessage var20 = (IMessage)LocateRegistry.getRegistry(Integer.parseInt(var0[0])).lookup(var0[1]);
         Response var1 = new Response();
         Properties.getInstance().setValues(var20.getProperties());
         Logger.getInstance().setLogLevel(Properties.LOGLEVEL);
         Spectra var2;
         (var2 = Spectra.getInstance()).setGranularity(Properties.GRANULARITY);
         File var3;
         if ((var3 = (new AgentJar()).extract()) == null || !var3.exists()) {
            throw new IOException("Was not possible to create an Agent to run test cases and instrument the source-code.");
         }

         Logger.getInstance().info("* Collecting classes under test");
         List var4;
         if ((var4 = ClassUtils.getNonTestClasses()).isEmpty()) {
            Logger.getInstance().warn("No classes found");
         }

         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_CLASSES, var4.size());
         Logger.getInstance().info("* Collected " + var4.size() + " classes");
         Logger.getInstance().info("* Collecting test classes");
         Map var5;
         if ((var5 = ClassUtils.getTestClasses()).isEmpty()) {
            Logger.getInstance().warn("No test classes found");
         }

         Logger.getInstance().info("* Collected " + var5.size() + " test classes");
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_TEST_CLASSES, var5.size());
         JaCoCoWrapper.executionParameters = new ExecutionParameters(var20.getClassPath(), Properties.TARGETPACKAGES, (String[])var4.toArray(new String[0]), Properties.TESTPACKAGES, (String[])var5.keySet().toArray(new String[0]), var3.getAbsolutePath(), (String)null, Logger.getInstance().getLogLevel(), Properties.INSTRUMENT_TEST_CLASSES, Properties.INCLUDE_DEPRECATED_CLASSES, Properties.INCLUDE_DEPRECATED_METHODS, Properties.UNIQUE_LINE_NUMBERS, Properties.GRANULARITY, Properties.TEST_TIMEOUT);
         Spectra.getInstance().setComponentCounts(JaCoCoWrapper.registerCoverage(new byte[1], true).values());
         Logger.getInstance().info("* Collected " + Spectra.getInstance().getNumberOfComponents() + " components");
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_COMPONENTS, Spectra.getInstance().getNumberOfComponents());
         Logger.getInstance().info("* Performing analysis");
         ExecutionParameters var21 = new ExecutionParameters(var20.getClassPath(), Properties.TARGETPACKAGES, (String[])var4.toArray(new String[0]), Properties.TESTPACKAGES, (String[])var5.keySet().toArray(new String[0]), var3.getAbsolutePath(), (String)null, Logger.getInstance().getLogLevel(), Properties.INSTRUMENT_TEST_CLASSES, Properties.INCLUDE_DEPRECATED_CLASSES, Properties.INCLUDE_DEPRECATED_METHODS, Properties.UNIQUE_LINE_NUMBERS, Properties.GRANULARITY, Properties.TEST_TIMEOUT);
         ArrayList var6 = new ArrayList();
         Iterator var7 = var5.keySet().iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            Object var9 = null;
            switch((ClassType.Type)var5.get(var8)) {
            case JUNIT:
               var9 = new JUnitRunner(var21);
               break;
            case TESTNG:
               var9 = new TestNGRunner(var21);
               break;
            default:
               Logger.getInstance().err("Type of test cases not supported");
            }

            assert var9 != null;

            List var10 = ((TestRunner)var9).createJobs(var8);
            if (Properties.TESTMETHODS != null) {
               Iterator var11 = var10.iterator();

               while(var11.hasNext()) {
                  JobHandler var12 = (JobHandler)var11.next();

                  assert var8.equals(var12.getJobDefinition().getTestClassName());

                  String var13 = var8 + "#" + var12.getJobDefinition().getTestMethodName();
                  boolean var14 = false;
                  String[] var15;
                  int var16 = (var15 = Properties.TESTMETHODS).length;

                  for(int var17 = 0; var17 < var16; ++var17) {
                     String var18 = var15[var17];
                     if ((new WildcardMatcher(var18)).matches(var13)) {
                        var14 = true;
                        break;
                     }
                  }

                  if (!var14) {
                     var11.remove();
                  }
               }
            }

            if (!var10.isEmpty()) {
               var2.addTestResults(TestRunner.run(var10, Properties.NUMBER_OF_TEST_RUNS_IN_PARALLEL));
               var6.addAll(var10);
            }
         }

         var7 = var2.getComponents().iterator();

         while(var7.hasNext()) {
            ComponentCount var24 = (ComponentCount)var7.next();
            int var27 = 0;
            Iterator var26 = var2.getTestResults().iterator();

            while(var26.hasNext()) {
               if (((TestResult)var26.next()).covers(var24)) {
                  ++var27;
               }
            }

            var24.setCount(var27);
         }

         switch(Properties.FILTER_SPECTRA) {
         case TEST_CASES_WITHOUT_ANY_COVERAGE:
            Logger.getInstance().info("* Filtering test cases that do not cover any component");
            (new FilterTestCasesWithoutCoverage()).filter(var2);
            break;
         case COMPONENTS_TRIGGERED_BY_FAILING_TEST_CASES:
            Logger.getInstance().info("* Filtering components triggered by failing test cases");
            (new FilterComponentsTriggeredByFailingTestCases()).filter(var2);
         }

         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_UNIT_TEST_CASES, var2.getNumberOfTestResults());
         if (Properties.PRINT_MATRIX) {
            Logger.getInstance().info("* Writing coverage matrix to '" + Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MATRIX_FILE + "'");
            CoverageStrategy.printMatrix(var2, Properties.GZOLTAR_DATA_DIR, Properties.MATRIX_FILE);
         }

         Map var25 = var1.getStatistics();

         assert var25 != null;

         int var30;
         label147:
         switch(Properties.STRATEGY) {
         case MUTATION:
            MutationStrategy var29 = new MutationStrategy();
            Logger.getInstance().info("* Mutating classes under test");
            Object var28;
            switch(Properties.MUTATION_BACKEND) {
            case PIT:
               var28 = new Pit();
               break;
            case CUSTOM:
               var28 = new CustomMutationBackend();
               break;
            default:
               var28 = new Major();
            }

            var30 = ((MutationBackend)var28).createMutants(var4);
            var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_MUTANTS, var30);
            if (var30 == 0) {
               Logger.getInstance().warn("GZoltar was not able to generate any mutant");
               break;
            } else {
               Logger.getInstance().info("* Performing mutation analysis");
               ProgressBar var33;
               (var33 = new ProgressBar((Integer)((OutputStatisticsVariable)var25.get(StatisticsVariables.NUMBER_OF_MUTANTS.name())).getValue())).start();
               Iterator var35 = var2.getComponents().iterator();

               while(true) {
                  ComponentCount var37;
                  Map var38;
                  do {
                     if (!var35.hasNext()) {
                        var33.stop();
                        Logger.getInstance().setLogLevel(Properties.LOGLEVEL);
                        Logger.getInstance().info("* Performing fault localization");
                        var29.diagnose(var2);
                        break label147;
                     }

                     var37 = (ComponentCount)var35.next();
                  } while((var38 = getTestJobs(var2, var6, var37)).isEmpty());

                  Iterator var39 = var37.getComponent().getMutants().iterator();

                  while(var39.hasNext()) {
                     Mutant var40 = (Mutant)var39.next();
                     var21.setMutantID(var40.getId());
                     if (Properties.SHOW_PROGRESS_BAR) {
                        Logger.getInstance().setLogLevel(Logger.LogLevel.NONE);
                        var33.step();
                        var33.setExtraMessage("Executing Test Suite on Mutant [ " + var40.getFullClassName() + " : " + var40.getId() + " ]");
                     }

                     Iterator var41 = var38.keySet().iterator();

                     while(var41.hasNext()) {
                        String var22 = (String)var41.next();
                        Object var23 = null;
                        switch((ClassType.Type)var5.get(var22)) {
                        case JUNIT:
                           var23 = new JUnitRunner(var21);
                           break;
                        case TESTNG:
                           var23 = new TestNGRunner(var21);
                           break;
                        default:
                           Logger.getInstance().err("Type of test cases not supported");
                        }

                        assert var23 != null;

                        var40.addTests(TestRunner.run((List)var38.get(var22), Properties.NUMBER_OF_TEST_RUNS_IN_PARALLEL));
                     }

                     if (Properties.PRINT_MATRIX) {
                        Logger.getInstance().debug("Writing mutation matrix to '" + Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MUTANTS_DIR + SystemProperties.FILE_SEPARATOR + "matrix_" + var40.getFullClassName() + "_" + var40.getId() + "'");
                        var29.printMatrix(var2.getTestResultsMap(), var40.getTestResults(), Properties.GZOLTAR_DATA_DIR, Properties.MUTANTS_DIR + SystemProperties.FILE_SEPARATOR + "matrix_" + var40.getFullClassName() + "_" + var40.getId());
                     }
                  }
               }
            }
         default:
            CoverageStrategy var36 = new CoverageStrategy();
            Logger.getInstance().info("* Performing fault localization");
            var36.diagnose(var2);
         }

         if (Properties.NORMALIZE_SPECTRA) {
            Properties.Coefficient[] var31;
            int var32 = (var31 = Properties.COEFFICIENTS).length;
            var30 = 0;

            while(var30 < var32) {
               Properties.Coefficient var34 = var31[var30];
               switch(var34) {
               case DSTAR:
               case OPT:
                  Logger.getInstance().info("* Normalizing spectra, coefficient: " + var34.name());
                  var2.normalizeSuspiciousnessValues(var34.name());
               default:
                  ++var30;
               }
            }
         }

         if (Properties.PRINT_SPECTRA) {
            Logger.getInstance().info("* Writing spectra to '" + Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.SPECTRA_FILE + "'");
            var2.printComponents(Properties.GZOLTAR_DATA_DIR, Properties.SPECTRA_FILE, Properties.VERBOSE_SPECTRA, Properties.INCLUDE_SUSPICIOUSNESS_VALUE);
         }

         var2.printTestCases(Properties.GZOLTAR_DATA_DIR, Properties.TESTS_FILE);
         Logger.getInstance().info("* Calculating metrics");
         StatisticsVariables.augmentStatisticsData(var2, var25);
         if (Properties.SERIALIZE_SPECTRA) {
            Logger.getInstance().info("* Serializing the spectra object");
            var1.setSpectra(var2);
         }

         Logger.getInstance().info("* Sending data to master");
         var20.setResponse(var1);
      } catch (IllegalArgumentException | IllegalAccessException | IOException | NotBoundException var19) {
         Logger.getInstance().err("", var19);
         System.exit(1);
      }

      System.exit(0);
   }

   private static Map<String, List<JobHandler>> getTestJobs(Spectra var0, List<JobHandler> var1, ComponentCount var2) {
      HashMap var3 = new HashMap();
      Iterator var7 = var1.iterator();

      while(true) {
         JobHandler var4;
         JobDefinition var5;
         String var6;
         do {
            if (!var7.hasNext()) {
               return var3;
            }

            var5 = (var4 = (JobHandler)var7.next()).getJobDefinition();
            var6 = var5.getTestClassName() + "#" + var5.getTestMethodName();
         } while(Properties.ONLY_RUN_TESTS_THAT_COVER_COMPONENT && (!Properties.ONLY_RUN_TESTS_THAT_COVER_COMPONENT || !var0.getTestResult(var6).covers(var2)));

         if (var3.containsKey(var5.getTestClassName())) {
            ((List)var3.get(var5.getTestClassName())).add(var4);
         } else {
            ArrayList var8;
            (var8 = new ArrayList()).add(var4);
            var3.put(var5.getTestClassName(), var8);
         }
      }
   }
}
