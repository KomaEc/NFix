package org.apache.maven.surefire.booter;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import org.apache.maven.surefire.providerapi.ProviderParameters;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.RunOrderParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.util.ReflectionUtils;
import org.apache.maven.surefire.util.RunOrder;
import org.apache.maven.surefire.util.SurefireReflectionException;

public class SurefireReflector {
   private final ClassLoader surefireClassLoader;
   private final Class reporterConfiguration;
   private final Class testRequest;
   private final Class testArtifactInfo;
   private final Class testArtifactInfoAware;
   private final Class directoryScannerParameters;
   private final Class runOrderParameters;
   private final Class directoryScannerParametersAware;
   private final Class testSuiteDefinitionAware;
   private final Class testClassLoaderAware;
   private final Class reporterConfigurationAware;
   private final Class providerPropertiesAware;
   private final Class runResult;
   private final Class booterParameters;
   private final Class reporterFactory;

   public SurefireReflector(ClassLoader surefireClassLoader) {
      this.surefireClassLoader = surefireClassLoader;

      try {
         this.reporterConfiguration = surefireClassLoader.loadClass(ReporterConfiguration.class.getName());
         this.testRequest = surefireClassLoader.loadClass(TestRequest.class.getName());
         this.testArtifactInfo = surefireClassLoader.loadClass(TestArtifactInfo.class.getName());
         this.testArtifactInfoAware = surefireClassLoader.loadClass(TestArtifactInfoAware.class.getName());
         this.directoryScannerParameters = surefireClassLoader.loadClass(DirectoryScannerParameters.class.getName());
         this.runOrderParameters = surefireClassLoader.loadClass(RunOrderParameters.class.getName());
         this.directoryScannerParametersAware = surefireClassLoader.loadClass(DirectoryScannerParametersAware.class.getName());
         this.testSuiteDefinitionAware = surefireClassLoader.loadClass(TestRequestAware.class.getName());
         this.testClassLoaderAware = surefireClassLoader.loadClass(SurefireClassLoadersAware.class.getName());
         this.reporterConfigurationAware = surefireClassLoader.loadClass(ReporterConfigurationAware.class.getName());
         this.providerPropertiesAware = surefireClassLoader.loadClass(ProviderPropertiesAware.class.getName());
         this.reporterFactory = surefireClassLoader.loadClass(ReporterFactory.class.getName());
         this.runResult = surefireClassLoader.loadClass(RunResult.class.getName());
         this.booterParameters = surefireClassLoader.loadClass(ProviderParameters.class.getName());
      } catch (ClassNotFoundException var3) {
         throw new SurefireReflectionException(var3);
      }
   }

   public Object convertIfRunResult(Object result) {
      if (result != null && this.isRunResult(result)) {
         Integer getCompletedCount1 = (Integer)ReflectionUtils.invokeGetter(result, "getCompletedCount");
         Integer getErrors = (Integer)ReflectionUtils.invokeGetter(result, "getErrors");
         Integer getSkipped = (Integer)ReflectionUtils.invokeGetter(result, "getSkipped");
         Integer getFailures = (Integer)ReflectionUtils.invokeGetter(result, "getFailures");
         return new RunResult(getCompletedCount1, getErrors, getFailures, getSkipped);
      } else {
         return result;
      }
   }

   Object createTestRequest(TestRequest suiteDefinition) {
      if (suiteDefinition == null) {
         return null;
      } else {
         Class[] arguments = new Class[]{List.class, File.class, String.class, String.class};
         Constructor constructor = ReflectionUtils.getConstructor(this.testRequest, arguments);
         return ReflectionUtils.newInstance(constructor, new Object[]{suiteDefinition.getSuiteXmlFiles(), suiteDefinition.getTestSourceDirectory(), suiteDefinition.getRequestedTest(), suiteDefinition.getRequestedTestMethod()});
      }
   }

   Object createDirectoryScannerParameters(DirectoryScannerParameters directoryScannerParameters) {
      if (directoryScannerParameters == null) {
         return null;
      } else {
         Class[] arguments = new Class[]{File.class, List.class, List.class, List.class, Boolean.class, String.class};
         Constructor constructor = ReflectionUtils.getConstructor(this.directoryScannerParameters, arguments);
         return ReflectionUtils.newInstance(constructor, new Object[]{directoryScannerParameters.getTestClassesDirectory(), directoryScannerParameters.getIncludes(), directoryScannerParameters.getExcludes(), directoryScannerParameters.getSpecificTests(), directoryScannerParameters.isFailIfNoTests(), RunOrder.asString(directoryScannerParameters.getRunOrder())});
      }
   }

   Object createRunOrderParameters(RunOrderParameters runOrderParameters) {
      if (runOrderParameters == null) {
         return null;
      } else {
         Class[] arguments = new Class[]{String.class, String.class};
         Constructor constructor = ReflectionUtils.getConstructor(this.runOrderParameters, arguments);
         File runStatisticsFile = runOrderParameters.getRunStatisticsFile();
         return ReflectionUtils.newInstance(constructor, new Object[]{RunOrder.asString(runOrderParameters.getRunOrder()), runStatisticsFile != null ? runStatisticsFile.getAbsolutePath() : null});
      }
   }

   Object createTestArtifactInfo(TestArtifactInfo testArtifactInfo) {
      if (testArtifactInfo == null) {
         return null;
      } else {
         Class[] arguments = new Class[]{String.class, String.class};
         Constructor constructor = ReflectionUtils.getConstructor(this.testArtifactInfo, arguments);
         return ReflectionUtils.newInstance(constructor, new Object[]{testArtifactInfo.getVersion(), testArtifactInfo.getClassifier()});
      }
   }

   Object createReporterConfiguration(ReporterConfiguration reporterConfiguration) {
      Constructor constructor = ReflectionUtils.getConstructor(this.reporterConfiguration, new Class[]{File.class, Boolean.class});
      return ReflectionUtils.newInstance(constructor, new Object[]{reporterConfiguration.getReportsDirectory(), reporterConfiguration.isTrimStackTrace()});
   }

   public static ReporterFactory createForkingReporterFactoryInCurrentClassLoader(Boolean trimStackTrace, PrintStream originalSystemOut) {
      return new ForkingReporterFactory(trimStackTrace, originalSystemOut);
   }

   public Object createBooterConfiguration(ClassLoader surefireClassLoader, Object factoryInstance, boolean insideFork) {
      return ReflectionUtils.instantiateTwoArgs(surefireClassLoader, BaseProviderFactory.class.getName(), this.reporterFactory, factoryInstance, Boolean.class, insideFork ? Boolean.TRUE : Boolean.FALSE);
   }

   public Object instantiateProvider(String providerClassName, Object booterParameters) {
      return ReflectionUtils.instantiateOneArg(this.surefireClassLoader, providerClassName, this.booterParameters, booterParameters);
   }

   public void setIfDirScannerAware(Object o, DirectoryScannerParameters dirScannerParams) {
      if (this.directoryScannerParametersAware.isAssignableFrom(o.getClass())) {
         this.setDirectoryScannerParameters(o, dirScannerParams);
      }

   }

   public void setDirectoryScannerParameters(Object o, DirectoryScannerParameters dirScannerParams) {
      Object param = this.createDirectoryScannerParameters(dirScannerParams);
      ReflectionUtils.invokeSetter(o, "setDirectoryScannerParameters", this.directoryScannerParameters, param);
   }

   public void setRunOrderParameters(Object o, RunOrderParameters runOrderParameters) {
      Object param = this.createRunOrderParameters(runOrderParameters);
      ReflectionUtils.invokeSetter(o, "setRunOrderParameters", this.runOrderParameters, param);
   }

   public void setTestSuiteDefinitionAware(Object o, TestRequest testSuiteDefinition2) {
      if (this.testSuiteDefinitionAware.isAssignableFrom(o.getClass())) {
         this.setTestSuiteDefinition(o, testSuiteDefinition2);
      }

   }

   void setTestSuiteDefinition(Object o, TestRequest testSuiteDefinition1) {
      Object param = this.createTestRequest(testSuiteDefinition1);
      ReflectionUtils.invokeSetter(o, "setTestRequest", this.testRequest, param);
   }

   public void setProviderPropertiesAware(Object o, Properties properties) {
      if (this.providerPropertiesAware.isAssignableFrom(o.getClass())) {
         this.setProviderProperties(o, properties);
      }

   }

   void setProviderProperties(Object o, Properties providerProperties) {
      ReflectionUtils.invokeSetter(o, "setProviderProperties", Properties.class, providerProperties);
   }

   public void setReporterConfigurationAware(Object o, ReporterConfiguration reporterConfiguration1) {
      if (this.reporterConfigurationAware.isAssignableFrom(o.getClass())) {
         this.setReporterConfiguration(o, reporterConfiguration1);
      }

   }

   void setReporterConfiguration(Object o, ReporterConfiguration reporterConfiguration) {
      Object param = this.createReporterConfiguration(reporterConfiguration);
      ReflectionUtils.invokeSetter(o, "setReporterConfiguration", this.reporterConfiguration, param);
   }

   public void setTestClassLoaderAware(Object o, ClassLoader testClassLoader) {
      if (this.testClassLoaderAware.isAssignableFrom(o.getClass())) {
         this.setTestClassLoader(o, testClassLoader);
      }

   }

   void setTestClassLoader(Object o, ClassLoader testClassLoader) {
      Method setter = ReflectionUtils.getMethod(o, "setClassLoaders", new Class[]{ClassLoader.class});
      ReflectionUtils.invokeMethodWithArray(o, setter, new Object[]{testClassLoader});
   }

   public void setTestArtifactInfoAware(Object o, TestArtifactInfo testArtifactInfo1) {
      if (this.testArtifactInfoAware.isAssignableFrom(o.getClass())) {
         this.setTestArtifactInfo(o, testArtifactInfo1);
      }

   }

   void setTestArtifactInfo(Object o, TestArtifactInfo testArtifactInfo) {
      Object param = this.createTestArtifactInfo(testArtifactInfo);
      ReflectionUtils.invokeSetter(o, "setTestArtifactInfo", this.testArtifactInfo, param);
   }

   private boolean isRunResult(Object o) {
      return this.runResult.isAssignableFrom(o.getClass());
   }

   class ClassLoaderProxy implements InvocationHandler {
      private final Object target;

      public ClassLoaderProxy(Object delegate) {
         this.target = delegate;
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Method delegateMethod = this.target.getClass().getMethod(method.getName(), method.getParameterTypes());
         return delegateMethod.invoke(this.target, args);
      }
   }
}
