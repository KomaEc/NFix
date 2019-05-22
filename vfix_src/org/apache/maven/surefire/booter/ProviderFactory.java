package org.apache.maven.surefire.booter;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.apache.maven.surefire.providerapi.SurefireProvider;
import org.apache.maven.surefire.report.ReporterException;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.apache.maven.surefire.util.ReflectionUtils;

public class ProviderFactory {
   private final StartupConfiguration startupConfiguration;
   private final ProviderConfiguration providerConfiguration;
   private final ClassLoader classLoader;
   private final SurefireReflector surefireReflector;
   private final Object reporterManagerFactory;
   private static final Class[] invokeParamaters = new Class[]{Object.class};

   public ProviderFactory(StartupConfiguration startupConfiguration, ProviderConfiguration providerConfiguration, ClassLoader testsClassLoader, Object reporterManagerFactory) {
      this.providerConfiguration = providerConfiguration;
      this.startupConfiguration = startupConfiguration;
      this.surefireReflector = new SurefireReflector(testsClassLoader);
      this.classLoader = testsClassLoader;
      this.reporterManagerFactory = reporterManagerFactory;
   }

   public static RunResult invokeProvider(Object testSet, ClassLoader testsClassLoader, Object factory, ProviderConfiguration providerConfiguration, boolean insideFork, StartupConfiguration startupConfiguration1, boolean restoreStreams) throws TestSetFailedException, InvocationTargetException {
      PrintStream orgSystemOut = System.out;
      PrintStream orgSystemErr = System.err;
      ProviderFactory providerFactory = new ProviderFactory(startupConfiguration1, providerConfiguration, testsClassLoader, factory);
      SurefireProvider provider = providerFactory.createProvider(insideFork);

      RunResult var11;
      try {
         var11 = provider.invoke(testSet);
      } finally {
         if (restoreStreams && System.getSecurityManager() == null) {
            System.setOut(orgSystemOut);
            System.setErr(orgSystemErr);
         }

      }

      return var11;
   }

   public SurefireProvider createProvider(boolean isInsideFork) {
      ClassLoader systemClassLoader = Thread.currentThread().getContextClassLoader();
      Thread.currentThread().setContextClassLoader(this.classLoader);
      StartupConfiguration starterConfiguration = this.startupConfiguration;
      Object o = this.surefireReflector.createBooterConfiguration(this.classLoader, this.reporterManagerFactory, isInsideFork);
      this.surefireReflector.setTestSuiteDefinitionAware(o, this.providerConfiguration.getTestSuiteDefinition());
      this.surefireReflector.setProviderPropertiesAware(o, this.providerConfiguration.getProviderProperties());
      this.surefireReflector.setReporterConfigurationAware(o, this.providerConfiguration.getReporterConfiguration());
      this.surefireReflector.setTestClassLoaderAware(o, this.classLoader);
      this.surefireReflector.setTestArtifactInfoAware(o, this.providerConfiguration.getTestArtifact());
      this.surefireReflector.setRunOrderParameters(o, this.providerConfiguration.getRunOrderParameters());
      this.surefireReflector.setIfDirScannerAware(o, this.providerConfiguration.getDirScannerParams());
      Object provider = this.surefireReflector.instantiateProvider(starterConfiguration.getActualClassName(), o);
      Thread.currentThread().setContextClassLoader(systemClassLoader);
      return new ProviderFactory.ProviderProxy(provider, this.classLoader);
   }

   private class ProviderProxy implements SurefireProvider {
      private final Object providerInOtherClassLoader;
      private final ClassLoader testsClassLoader;

      private ProviderProxy(Object providerInOtherClassLoader, ClassLoader testsClassLoader) {
         this.providerInOtherClassLoader = providerInOtherClassLoader;
         this.testsClassLoader = testsClassLoader;
      }

      public Iterator getSuites() {
         ClassLoader current = this.swapClassLoader(this.testsClassLoader);

         Iterator var2;
         try {
            var2 = (Iterator)ReflectionUtils.invokeGetter(this.providerInOtherClassLoader, "getSuites");
         } finally {
            Thread.currentThread().setContextClassLoader(current);
         }

         return var2;
      }

      public RunResult invoke(Object forkTestSet) throws TestSetFailedException, ReporterException, InvocationTargetException {
         ClassLoader current = this.swapClassLoader(this.testsClassLoader);

         RunResult var5;
         try {
            Method invoke = ReflectionUtils.getMethod(this.providerInOtherClassLoader.getClass(), "invoke", ProviderFactory.invokeParamaters);
            Object result = ReflectionUtils.invokeMethodWithArray2(this.providerInOtherClassLoader, invoke, new Object[]{forkTestSet});
            var5 = (RunResult)ProviderFactory.this.surefireReflector.convertIfRunResult(result);
         } finally {
            if (System.getSecurityManager() == null) {
               Thread.currentThread().setContextClassLoader(current);
            }

         }

         return var5;
      }

      private ClassLoader swapClassLoader(ClassLoader newClassLoader) {
         ClassLoader current = Thread.currentThread().getContextClassLoader();
         Thread.currentThread().setContextClassLoader(newClassLoader);
         return current;
      }

      public void cancel() {
         Method invoke = ReflectionUtils.getMethod(this.providerInOtherClassLoader.getClass(), "cancel", new Class[0]);
         ReflectionUtils.invokeMethodWithArray(this.providerInOtherClassLoader, invoke, (Object[])null);
      }

      // $FF: synthetic method
      ProviderProxy(Object x1, ClassLoader x2, Object x3) {
         this(x1, x2);
      }
   }
}
