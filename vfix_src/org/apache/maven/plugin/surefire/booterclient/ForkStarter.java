package org.apache.maven.plugin.surefire.booterclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.surefire.AbstractSurefireMojo;
import org.apache.maven.plugin.surefire.CommonReflector;
import org.apache.maven.plugin.surefire.StartupReportConfiguration;
import org.apache.maven.plugin.surefire.SurefireProperties;
import org.apache.maven.plugin.surefire.booterclient.lazytestprovider.OutputStreamFlushableCommandline;
import org.apache.maven.plugin.surefire.booterclient.lazytestprovider.TestProvidingInputStream;
import org.apache.maven.plugin.surefire.booterclient.output.ForkClient;
import org.apache.maven.plugin.surefire.booterclient.output.ThreadedStreamConsumer;
import org.apache.maven.plugin.surefire.report.DefaultReporterFactory;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.booter.ClasspathConfiguration;
import org.apache.maven.surefire.booter.KeyValueSource;
import org.apache.maven.surefire.booter.PropertiesWrapper;
import org.apache.maven.surefire.booter.ProviderConfiguration;
import org.apache.maven.surefire.booter.ProviderFactory;
import org.apache.maven.surefire.booter.StartupConfiguration;
import org.apache.maven.surefire.booter.SurefireBooterForkException;
import org.apache.maven.surefire.booter.SurefireExecutionException;
import org.apache.maven.surefire.booter.SystemPropertyManager;
import org.apache.maven.surefire.providerapi.SurefireProvider;
import org.apache.maven.surefire.report.StackTraceWriter;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.CommandLineException;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.CommandLineTimeOutException;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.CommandLineUtils;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.ShutdownHookUtils;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.util.DefaultScanResult;

public class ForkStarter {
   private final int forkedProcessTimeoutInSeconds;
   private final ProviderConfiguration providerConfiguration;
   private final StartupConfiguration startupConfiguration;
   private final ForkConfiguration forkConfiguration;
   private final StartupReportConfiguration startupReportConfiguration;
   private Log log;
   private final DefaultReporterFactory defaultReporterFactory;
   private static volatile int systemPropertiesFileCounter = 0;

   public ForkStarter(ProviderConfiguration providerConfiguration, StartupConfiguration startupConfiguration, ForkConfiguration forkConfiguration, int forkedProcessTimeoutInSeconds, StartupReportConfiguration startupReportConfiguration, Log log) {
      this.forkConfiguration = forkConfiguration;
      this.providerConfiguration = providerConfiguration;
      this.forkedProcessTimeoutInSeconds = forkedProcessTimeoutInSeconds;
      this.startupConfiguration = startupConfiguration;
      this.startupReportConfiguration = startupReportConfiguration;
      this.log = log;
      this.defaultReporterFactory = new DefaultReporterFactory(startupReportConfiguration);
   }

   public RunResult run(SurefireProperties effectiveSystemProperties, DefaultScanResult scanResult) throws SurefireBooterForkException, SurefireExecutionException {
      RunResult result;
      try {
         Properties providerProperties = this.providerConfiguration.getProviderProperties();
         scanResult.writeTo(providerProperties);
         if (this.isForkOnce()) {
            ForkClient forkClient = new ForkClient(this.defaultReporterFactory, this.startupReportConfiguration.getTestVmSystemProperties());
            result = this.fork((Object)null, new PropertiesWrapper(providerProperties), forkClient, effectiveSystemProperties, (TestProvidingInputStream)null);
         } else if (this.forkConfiguration.isReuseForks()) {
            result = this.runSuitesForkOnceMultiple(effectiveSystemProperties, this.forkConfiguration.getForkCount());
         } else {
            result = this.runSuitesForkPerTestSet(effectiveSystemProperties, this.forkConfiguration.getForkCount());
         }
      } finally {
         this.defaultReporterFactory.close();
      }

      return result;
   }

   private boolean isForkOnce() {
      return this.forkConfiguration.isReuseForks() && 1 == this.forkConfiguration.getForkCount();
   }

   private RunResult runSuitesForkOnceMultiple(final SurefireProperties effectiveSystemProperties, int forkCount) throws SurefireBooterForkException {
      ArrayList<Future<RunResult>> results = new ArrayList(forkCount);
      ThreadPoolExecutor executorService = new ThreadPoolExecutor(forkCount, forkCount, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(forkCount));

      try {
         RunResult globalResult = new RunResult(0, 0, 0, 0);
         List<Class<?>> suites = new ArrayList();
         Iterator suitesIterator = this.getSuitesIterator();

         while(suitesIterator.hasNext()) {
            suites.add(suitesIterator.next());
         }

         final Queue<String> messageQueue = new ConcurrentLinkedQueue();
         Iterator i$ = suites.iterator();

         while(i$.hasNext()) {
            Class<?> clazz = (Class)i$.next();
            messageQueue.add(clazz.getName());
         }

         for(int forkNum = 0; forkNum < forkCount && forkNum < suites.size(); ++forkNum) {
            Callable<RunResult> pf = new Callable<RunResult>() {
               public RunResult call() throws Exception {
                  TestProvidingInputStream testProvidingInputStream = new TestProvidingInputStream(messageQueue);
                  ForkClient forkClient = new ForkClient(ForkStarter.this.defaultReporterFactory, ForkStarter.this.startupReportConfiguration.getTestVmSystemProperties(), testProvidingInputStream);
                  return ForkStarter.this.fork((Object)null, new PropertiesWrapper(ForkStarter.this.providerConfiguration.getProviderProperties()), forkClient, effectiveSystemProperties, testProvidingInputStream);
               }
            };
            results.add(executorService.submit(pf));
         }

         i$ = results.iterator();

         while(i$.hasNext()) {
            Future result = (Future)i$.next();

            try {
               RunResult cur = (RunResult)result.get();
               if (cur == null) {
                  throw new SurefireBooterForkException("No results for " + result.toString());
               }

               globalResult = globalResult.aggregate(cur);
            } catch (InterruptedException var16) {
               throw new SurefireBooterForkException("Interrupted", var16);
            } catch (ExecutionException var17) {
               throw new SurefireBooterForkException("ExecutionException", var17);
            }
         }

         RunResult var20 = globalResult;
         return var20;
      } finally {
         this.closeExecutor(executorService);
      }
   }

   private RunResult runSuitesForkPerTestSet(final SurefireProperties effectiveSystemProperties, int forkCount) throws SurefireBooterForkException {
      ArrayList<Future<RunResult>> results = new ArrayList(500);
      ThreadPoolExecutor executorService = new ThreadPoolExecutor(forkCount, forkCount, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());

      try {
         RunResult globalResult = new RunResult(0, 0, 0, 0);
         Iterator suites = this.getSuitesIterator();

         while(suites.hasNext()) {
            final Object testSet = suites.next();
            Callable<RunResult> pf = new Callable<RunResult>() {
               public RunResult call() throws Exception {
                  ForkClient forkClient = new ForkClient(ForkStarter.this.defaultReporterFactory, ForkStarter.this.startupReportConfiguration.getTestVmSystemProperties());
                  return ForkStarter.this.fork(testSet, new PropertiesWrapper(ForkStarter.this.providerConfiguration.getProviderProperties()), forkClient, effectiveSystemProperties, (TestProvidingInputStream)null);
               }
            };
            results.add(executorService.submit(pf));
         }

         Iterator i$ = results.iterator();

         while(i$.hasNext()) {
            Future result = (Future)i$.next();

            try {
               RunResult cur = (RunResult)result.get();
               if (cur == null) {
                  throw new SurefireBooterForkException("No results for " + result.toString());
               }

               globalResult = globalResult.aggregate(cur);
            } catch (InterruptedException var14) {
               throw new SurefireBooterForkException("Interrupted", var14);
            } catch (ExecutionException var15) {
               throw new SurefireBooterForkException("ExecutionException", var15);
            }
         }

         RunResult var18 = globalResult;
         return var18;
      } finally {
         this.closeExecutor(executorService);
      }
   }

   private void closeExecutor(ExecutorService executorService) throws SurefireBooterForkException {
      executorService.shutdown();

      try {
         executorService.awaitTermination(3600L, TimeUnit.SECONDS);
      } catch (InterruptedException var3) {
         throw new SurefireBooterForkException("Interrupted", var3);
      }
   }

   private RunResult fork(Object testSet, KeyValueSource providerProperties, ForkClient forkClient, SurefireProperties effectiveSystemProperties, TestProvidingInputStream testProvidingInputStream) throws SurefireBooterForkException {
      int forkNumber = ForkNumberBucket.drawNumber();

      RunResult var7;
      try {
         var7 = this.fork(testSet, providerProperties, forkClient, effectiveSystemProperties, forkNumber, testProvidingInputStream);
      } finally {
         ForkNumberBucket.returnNumber(forkNumber);
      }

      return var7;
   }

   private RunResult fork(Object testSet, KeyValueSource providerProperties, ForkClient forkClient, SurefireProperties effectiveSystemProperties, int forkNumber, TestProvidingInputStream testProvidingInputStream) throws SurefireBooterForkException {
      File systPropsFile = null;

      File surefireProperties;
      try {
         BooterSerializer booterSerializer = new BooterSerializer(this.forkConfiguration);
         surefireProperties = booterSerializer.serialize(providerProperties, this.providerConfiguration, this.startupConfiguration, testSet, null != testProvidingInputStream);
         if (effectiveSystemProperties != null) {
            SurefireProperties filteredProperties = AbstractSurefireMojo.createCopyAndReplaceForkNumPlaceholder(effectiveSystemProperties, forkNumber);
            systPropsFile = SystemPropertyManager.writePropertiesFile(filteredProperties, this.forkConfiguration.getTempDirectory(), "surefire_" + systemPropertiesFileCounter++, this.forkConfiguration.isDebug());
         }
      } catch (IOException var28) {
         throw new SurefireBooterForkException("Error creating properties files for forking", var28);
      }

      Classpath bootClasspathConfiguration = this.startupConfiguration.isProviderMainClass() ? this.startupConfiguration.getClasspathConfiguration().getProviderClasspath() : this.forkConfiguration.getBootClasspath();
      Classpath bootClasspath = Classpath.join(Classpath.join(bootClasspathConfiguration, this.startupConfiguration.getClasspathConfiguration().getTestClasspath()), this.startupConfiguration.getClasspathConfiguration().getProviderClasspath());
      if (this.log.isDebugEnabled()) {
         this.log.debug((CharSequence)bootClasspath.getLogMessage("boot"));
         this.log.debug((CharSequence)bootClasspath.getCompactLogMessage("boot(compact)"));
      }

      OutputStreamFlushableCommandline cli = this.forkConfiguration.createCommandLine(bootClasspath.getClassPath(), this.startupConfiguration, forkNumber);
      ForkStarter.InputStreamCloser inputStreamCloser;
      Thread inputStreamCloserHook;
      if (testProvidingInputStream != null) {
         testProvidingInputStream.setFlushReceiverProvider(cli);
         inputStreamCloser = new ForkStarter.InputStreamCloser(testProvidingInputStream);
         inputStreamCloserHook = new Thread(inputStreamCloser);
         ShutdownHookUtils.addShutDownHook(inputStreamCloserHook);
      } else {
         inputStreamCloser = null;
         inputStreamCloserHook = null;
      }

      cli.createArg().setFile(surefireProperties);
      if (systPropsFile != null) {
         cli.createArg().setFile(systPropsFile);
      }

      ThreadedStreamConsumer threadedStreamConsumer = new ThreadedStreamConsumer(forkClient);
      if (this.forkConfiguration.isDebug()) {
         System.out.println("Forking command line: " + cli);
      }

      RunResult runResult = null;

      try {
         int timeout = this.forkedProcessTimeoutInSeconds > 0 ? this.forkedProcessTimeoutInSeconds : 0;
         int result = CommandLineUtils.executeCommandLine(cli, testProvidingInputStream, threadedStreamConsumer, threadedStreamConsumer, timeout, inputStreamCloser);
         if (result != 0) {
            throw new SurefireBooterForkException("Error occurred in starting fork, check output in log");
         }
      } catch (CommandLineTimeOutException var25) {
         runResult = RunResult.timeout(this.defaultReporterFactory.getGlobalRunStatistics().getRunResult());
      } catch (CommandLineException var26) {
         runResult = RunResult.failure(this.defaultReporterFactory.getGlobalRunStatistics().getRunResult(), var26);
         throw new SurefireBooterForkException("Error while executing forked tests.", var26.getCause());
      } finally {
         threadedStreamConsumer.close();
         if (inputStreamCloser != null) {
            inputStreamCloser.run();
            ShutdownHookUtils.removeShutdownHook(inputStreamCloserHook);
         }

         if (runResult == null) {
            runResult = this.defaultReporterFactory.getGlobalRunStatistics().getRunResult();
         }

         if (!runResult.isTimeout()) {
            StackTraceWriter errorInFork = forkClient.getErrorInFork();
            if (errorInFork != null) {
               throw new RuntimeException("There was an error in the forked process\n" + errorInFork.writeTraceToString());
            }

            if (!forkClient.isSaidGoodBye()) {
               throw new RuntimeException("The forked VM terminated without properly saying goodbye. VM crash or System.exit called?\nCommand was " + cli.toString());
            }
         }

         forkClient.close(runResult.isTimeout());
      }

      return runResult;
   }

   private Iterator<Class<?>> getSuitesIterator() throws SurefireBooterForkException {
      try {
         ClasspathConfiguration classpathConfiguration = this.startupConfiguration.getClasspathConfiguration();
         ClassLoader unifiedClassLoader = classpathConfiguration.createMergedClassLoader();
         CommonReflector commonReflector = new CommonReflector(unifiedClassLoader);
         Object reporterFactory = commonReflector.createReportingReporterFactory(this.startupReportConfiguration);
         ProviderFactory providerFactory = new ProviderFactory(this.startupConfiguration, this.providerConfiguration, unifiedClassLoader, reporterFactory);
         SurefireProvider surefireProvider = providerFactory.createProvider(false);
         return surefireProvider.getSuites();
      } catch (SurefireExecutionException var7) {
         throw new SurefireBooterForkException("Unable to create classloader to find test suites", var7);
      }
   }

   private final class InputStreamCloser implements Runnable {
      private InputStream testProvidingInputStream;

      public InputStreamCloser(InputStream testProvidingInputStream) {
         this.testProvidingInputStream = testProvidingInputStream;
      }

      public synchronized void run() {
         if (this.testProvidingInputStream != null) {
            try {
               this.testProvidingInputStream.close();
            } catch (IOException var2) {
            }

            this.testProvidingInputStream = null;
         }

      }
   }
}
