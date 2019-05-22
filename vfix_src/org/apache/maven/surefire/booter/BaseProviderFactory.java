package org.apache.maven.surefire.booter;

import java.util.Properties;
import org.apache.maven.surefire.providerapi.ProviderParameters;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.DefaultDirectConsoleReporter;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.RunOrderParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.util.DefaultDirectoryScanner;
import org.apache.maven.surefire.util.DefaultRunOrderCalculator;
import org.apache.maven.surefire.util.DefaultScanResult;
import org.apache.maven.surefire.util.DirectoryScanner;
import org.apache.maven.surefire.util.RunOrderCalculator;
import org.apache.maven.surefire.util.ScanResult;

public class BaseProviderFactory implements DirectoryScannerParametersAware, ReporterConfigurationAware, SurefireClassLoadersAware, TestRequestAware, ProviderPropertiesAware, ProviderParameters, TestArtifactInfoAware, RunOrderParametersAware {
   private Properties providerProperties;
   private DirectoryScannerParameters directoryScannerParameters;
   private ReporterConfiguration reporterConfiguration;
   private RunOrderParameters runOrderParameters;
   private ClassLoader testClassLoader;
   private TestRequest testRequest;
   private TestArtifactInfo testArtifactInfo;
   private static final Integer ROOT_CHANNEl = 0;
   private final ReporterFactory reporterFactory;
   private final boolean insideFork;

   public BaseProviderFactory(ReporterFactory reporterFactory, Boolean insideFork) {
      this.reporterFactory = reporterFactory;
      this.insideFork = insideFork;
   }

   public DirectoryScanner getDirectoryScanner() {
      return this.directoryScannerParameters == null ? null : new DefaultDirectoryScanner(this.directoryScannerParameters.getTestClassesDirectory(), this.directoryScannerParameters.getIncludes(), this.directoryScannerParameters.getExcludes(), this.directoryScannerParameters.getSpecificTests());
   }

   public ScanResult getScanResult() {
      return DefaultScanResult.from(this.providerProperties);
   }

   private int getThreadCount() {
      String threadcount = (String)this.providerProperties.get("threadcount");
      return threadcount == null ? 1 : Math.max(Integer.parseInt(threadcount), 1);
   }

   public RunOrderCalculator getRunOrderCalculator() {
      return this.directoryScannerParameters == null ? null : new DefaultRunOrderCalculator(this.runOrderParameters, this.getThreadCount());
   }

   public ReporterFactory getReporterFactory() {
      return this.reporterFactory;
   }

   public void setDirectoryScannerParameters(DirectoryScannerParameters directoryScannerParameters) {
      this.directoryScannerParameters = directoryScannerParameters;
   }

   public void setReporterConfiguration(ReporterConfiguration reporterConfiguration) {
      this.reporterConfiguration = reporterConfiguration;
   }

   public void setClassLoaders(ClassLoader testClassLoader) {
      this.testClassLoader = testClassLoader;
   }

   public ConsoleLogger getConsoleLogger() {
      return (ConsoleLogger)(this.insideFork ? new ForkingRunListener(this.reporterConfiguration.getOriginalSystemOut(), ROOT_CHANNEl, this.reporterConfiguration.isTrimStackTrace()) : new DefaultDirectConsoleReporter(this.reporterConfiguration.getOriginalSystemOut()));
   }

   public void setTestRequest(TestRequest testRequest) {
      this.testRequest = testRequest;
   }

   public DirectoryScannerParameters getDirectoryScannerParameters() {
      return this.directoryScannerParameters;
   }

   public ReporterConfiguration getReporterConfiguration() {
      return this.reporterConfiguration;
   }

   public TestRequest getTestRequest() {
      return this.testRequest;
   }

   public ClassLoader getTestClassLoader() {
      return this.testClassLoader;
   }

   public void setProviderProperties(Properties providerProperties) {
      this.providerProperties = providerProperties;
   }

   public Properties getProviderProperties() {
      return this.providerProperties;
   }

   public TestArtifactInfo getTestArtifactInfo() {
      return this.testArtifactInfo;
   }

   public void setTestArtifactInfo(TestArtifactInfo testArtifactInfo) {
      this.testArtifactInfo = testArtifactInfo;
   }

   public void setRunOrderParameters(RunOrderParameters runOrderParameters) {
      this.runOrderParameters = runOrderParameters;
   }
}
