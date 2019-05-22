package org.apache.maven.surefire.booter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import org.apache.maven.surefire.providerapi.ProviderParameters;
import org.apache.maven.surefire.providerapi.SurefireProvider;
import org.apache.maven.surefire.report.LegacyPojoStackTraceWriter;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.apache.maven.surefire.util.ReflectionUtils;

public class ForkedBooter {
   private static final long SYSTEM_EXIT_TIMEOUT = 30000L;

   public static void main(String[] args) throws Throwable {
      PrintStream originalOut = System.out;

      try {
         if (args.length > 1) {
            SystemPropertyManager.setSystemProperties(new File(args[1]));
         }

         File surefirePropertiesFile = new File(args[0]);
         InputStream stream = surefirePropertiesFile.exists() ? new FileInputStream(surefirePropertiesFile) : null;
         BooterDeserializer booterDeserializer = new BooterDeserializer(stream);
         ProviderConfiguration providerConfiguration = booterDeserializer.deserialize();
         StartupConfiguration startupConfiguration = booterDeserializer.getProviderConfiguration();
         TypeEncodedValue forkedTestSet = providerConfiguration.getTestForFork();
         boolean readTestsFromInputStream = providerConfiguration.isReadTestsFromInStream();
         ClasspathConfiguration classpathConfiguration = startupConfiguration.getClasspathConfiguration();
         if (startupConfiguration.isManifestOnlyJarRequestedAndUsable()) {
            classpathConfiguration.trickClassPathWhenManifestOnlyClasspath();
         }

         Thread.currentThread().getContextClassLoader().setDefaultAssertionStatus(classpathConfiguration.isEnableAssertions());
         startupConfiguration.writeSurefireTestClasspathProperty();
         Object testSet;
         if (forkedTestSet != null) {
            testSet = forkedTestSet.getDecodedValue(Thread.currentThread().getContextClassLoader());
         } else if (readTestsFromInputStream) {
            testSet = new LazyTestsToRun(System.in, originalOut);
         } else {
            testSet = null;
         }

         LegacyPojoStackTraceWriter stackTraceWriter;
         StringBuilder stringBuilder;
         try {
            runSuitesInProcess(testSet, startupConfiguration, providerConfiguration, originalOut);
         } catch (InvocationTargetException var14) {
            stackTraceWriter = new LegacyPojoStackTraceWriter("test subystem", "no method", var14.getTargetException());
            stringBuilder = new StringBuilder();
            ForkingRunListener.encode(stringBuilder, stackTraceWriter, false);
            originalOut.println("X,0," + stringBuilder.toString());
         } catch (Throwable var15) {
            stackTraceWriter = new LegacyPojoStackTraceWriter("test subystem", "no method", var15);
            stringBuilder = new StringBuilder();
            ForkingRunListener.encode(stringBuilder, stackTraceWriter, false);
            originalOut.println("X,0," + stringBuilder.toString());
         }

         originalOut.println("Z,0,BYE!");
         originalOut.flush();
         exit(0);
      } catch (Throwable var16) {
         var16.printStackTrace(System.err);
         exit(1);
      }

   }

   private static void exit(int returnCode) {
      launchLastDitchDaemonShutdownThread(returnCode);
      System.exit(returnCode);
   }

   private static RunResult runSuitesInProcess(Object testSet, StartupConfiguration startupConfiguration, ProviderConfiguration providerConfiguration, PrintStream originalSystemOut) throws SurefireExecutionException, TestSetFailedException, InvocationTargetException {
      ReporterFactory factory = createForkingReporterFactory(providerConfiguration, originalSystemOut);
      return invokeProviderInSameClassLoader(testSet, factory, providerConfiguration, true, startupConfiguration, false);
   }

   private static ReporterFactory createForkingReporterFactory(ProviderConfiguration providerConfiguration, PrintStream originalSystemOut) {
      Boolean trimStackTrace = providerConfiguration.getReporterConfiguration().isTrimStackTrace();
      return SurefireReflector.createForkingReporterFactoryInCurrentClassLoader(trimStackTrace, originalSystemOut);
   }

   private static void launchLastDitchDaemonShutdownThread(final int returnCode) {
      Thread lastExit = new Thread(new Runnable() {
         public void run() {
            try {
               Thread.sleep(30000L);
               Runtime.getRuntime().halt(returnCode);
            } catch (InterruptedException var2) {
            }

         }
      });
      lastExit.setDaemon(true);
      lastExit.start();
   }

   public static RunResult invokeProviderInSameClassLoader(Object testSet, Object factory, ProviderConfiguration providerConfiguration, boolean insideFork, StartupConfiguration startupConfiguration1, boolean restoreStreams) throws TestSetFailedException, InvocationTargetException {
      PrintStream orgSystemOut = System.out;
      PrintStream orgSystemErr = System.err;
      SurefireProvider provider = createProviderInCurrentClassloader(startupConfiguration1, insideFork, providerConfiguration, factory);

      RunResult var9;
      try {
         var9 = provider.invoke(testSet);
      } finally {
         if (restoreStreams && System.getSecurityManager() == null) {
            System.setOut(orgSystemOut);
            System.setErr(orgSystemErr);
         }

      }

      return var9;
   }

   public static SurefireProvider createProviderInCurrentClassloader(StartupConfiguration startupConfiguration1, boolean isInsideFork, ProviderConfiguration providerConfiguration, Object reporterManagerFactory1) {
      BaseProviderFactory bpf = new BaseProviderFactory((ReporterFactory)reporterManagerFactory1, isInsideFork);
      bpf.setTestRequest(providerConfiguration.getTestSuiteDefinition());
      bpf.setReporterConfiguration(providerConfiguration.getReporterConfiguration());
      ClassLoader clasLoader = Thread.currentThread().getContextClassLoader();
      bpf.setClassLoaders(clasLoader);
      bpf.setTestArtifactInfo(providerConfiguration.getTestArtifact());
      bpf.setProviderProperties(providerConfiguration.getProviderProperties());
      bpf.setRunOrderParameters(providerConfiguration.getRunOrderParameters());
      bpf.setDirectoryScannerParameters(providerConfiguration.getDirScannerParams());
      return (SurefireProvider)ReflectionUtils.instantiateOneArg(clasLoader, startupConfiguration1.getActualClassName(), ProviderParameters.class, bpf);
   }
}
