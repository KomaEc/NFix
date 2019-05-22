package org.apache.maven.surefire.booter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.RunOrderParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;

public class BooterDeserializer implements BooterConstants {
   private final PropertiesWrapper properties;

   public BooterDeserializer(InputStream inputStream) throws IOException {
      this.properties = SystemPropertyManager.loadProperties(inputStream);
   }

   public ProviderConfiguration deserialize() {
      File reportsDirectory = new File(this.properties.getProperty("reportsDirectory"));
      String testNgVersion = this.properties.getProperty("testFwJarVersion");
      String testArtifactClassifier = this.properties.getProperty("testFwJarClassifier");
      TypeEncodedValue typeEncodedTestForFork = this.properties.getTypeEncodedValue("forkTestSet");
      boolean preferTestsFromInStream = this.properties.getBooleanProperty("preferTestsFromInStream");
      String requestedTest = this.properties.getProperty("requestedTest");
      String requestedTestMethod = this.properties.getProperty("requestedTestMethod");
      File sourceDirectory = this.properties.getFileProperty("testSuiteDefinitionTestSourceDirectory");
      List excludesList = this.properties.getStringList("excludes");
      List includesList = this.properties.getStringList("includes");
      List specificTestsList = this.properties.getStringList("specificTest");
      List testSuiteXmlFiles = this.properties.getStringList("testSuiteXmlFiles");
      File testClassesDirectory = this.properties.getFileProperty("testClassesDirectory");
      String runOrder = this.properties.getProperty("runOrder");
      String runStatisticsFile = this.properties.getProperty("runStatisticsFile");
      DirectoryScannerParameters dirScannerParams = new DirectoryScannerParameters(testClassesDirectory, includesList, excludesList, specificTestsList, this.properties.getBooleanObjectProperty("failIfNoTests"), runOrder);
      RunOrderParameters runOrderParameters = new RunOrderParameters(runOrder, runStatisticsFile);
      TestArtifactInfo testNg = new TestArtifactInfo(testNgVersion, testArtifactClassifier);
      TestRequest testSuiteDefinition = new TestRequest(testSuiteXmlFiles, sourceDirectory, requestedTest, requestedTestMethod);
      ReporterConfiguration reporterConfiguration = new ReporterConfiguration(reportsDirectory, this.properties.getBooleanObjectProperty("isTrimStackTrace"));
      return new ProviderConfiguration(dirScannerParams, runOrderParameters, this.properties.getBooleanProperty("failIfNoTests"), reporterConfiguration, testNg, testSuiteDefinition, this.properties.getProperties(), typeEncodedTestForFork, preferTestsFromInStream);
   }

   public StartupConfiguration getProviderConfiguration() {
      boolean useSystemClassLoader = this.properties.getBooleanProperty("useSystemClassLoader");
      boolean useManifestOnlyJar = this.properties.getBooleanProperty("useManifestOnlyJar");
      String providerConfiguration = this.properties.getProperty("providerConfiguration");
      ClassLoaderConfiguration classLoaderConfiguration = new ClassLoaderConfiguration(useSystemClassLoader, useManifestOnlyJar);
      ClasspathConfiguration classpathConfiguration = new ClasspathConfiguration(this.properties);
      return StartupConfiguration.inForkedVm(providerConfiguration, classpathConfiguration, classLoaderConfiguration);
   }
}
