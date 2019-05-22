package org.apache.maven.plugin.surefire.booterclient;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.surefire.SurefireProperties;
import org.apache.maven.surefire.booter.ClassLoaderConfiguration;
import org.apache.maven.surefire.booter.ClasspathConfiguration;
import org.apache.maven.surefire.booter.KeyValueSource;
import org.apache.maven.surefire.booter.ProviderConfiguration;
import org.apache.maven.surefire.booter.StartupConfiguration;
import org.apache.maven.surefire.booter.SystemPropertyManager;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.RunOrderParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.util.RunOrder;

class BooterSerializer {
   private final ForkConfiguration forkConfiguration;

   public BooterSerializer(ForkConfiguration forkConfiguration) {
      this.forkConfiguration = forkConfiguration;
   }

   public File serialize(KeyValueSource sourceProperties, ProviderConfiguration booterConfiguration, StartupConfiguration providerConfiguration, Object testSet, boolean readTestsFromInStream) throws IOException {
      SurefireProperties properties = new SurefireProperties(sourceProperties);
      ClasspathConfiguration cp = providerConfiguration.getClasspathConfiguration();
      properties.setClasspath("classPathUrl.", cp.getTestClasspath());
      properties.setClasspath("surefireClassPathUrl.", cp.getProviderClasspath());
      properties.setProperty("enableAssertions", (String)String.valueOf(cp.isEnableAssertions()));
      properties.setProperty("childDelegation", (String)String.valueOf(cp.isChildDelegation()));
      TestArtifactInfo testNg = booterConfiguration.getTestArtifact();
      if (testNg != null) {
         properties.setProperty("testFwJarVersion", (String)testNg.getVersion());
         properties.setNullableProperty("testFwJarClassifier", testNg.getClassifier());
      }

      properties.setProperty("preferTestsFromInStream", readTestsFromInStream);
      properties.setNullableProperty("forkTestSet", this.getTypeEncoded(testSet));
      TestRequest testSuiteDefinition = booterConfiguration.getTestSuiteDefinition();
      if (testSuiteDefinition != null) {
         properties.setProperty("testSuiteDefinitionTestSourceDirectory", testSuiteDefinition.getTestSourceDirectory());
         properties.addList(testSuiteDefinition.getSuiteXmlFiles(), "testSuiteXmlFiles");
         properties.setNullableProperty("requestedTest", testSuiteDefinition.getRequestedTest());
         properties.setNullableProperty("requestedTestMethod", testSuiteDefinition.getRequestedTestMethod());
      }

      DirectoryScannerParameters directoryScannerParameters = booterConfiguration.getDirScannerParams();
      if (directoryScannerParameters != null) {
         properties.setProperty("failIfNoTests", (String)String.valueOf(directoryScannerParameters.isFailIfNoTests()));
         properties.addList(directoryScannerParameters.getIncludes(), "includes");
         properties.addList(directoryScannerParameters.getExcludes(), "excludes");
         properties.addList(directoryScannerParameters.getSpecificTests(), "specificTest");
         properties.setProperty("testClassesDirectory", directoryScannerParameters.getTestClassesDirectory());
      }

      RunOrderParameters runOrderParameters = booterConfiguration.getRunOrderParameters();
      if (runOrderParameters != null) {
         properties.setProperty("runOrder", (String)RunOrder.asString(runOrderParameters.getRunOrder()));
         properties.setProperty("runStatisticsFile", runOrderParameters.getRunStatisticsFile());
      }

      ReporterConfiguration reporterConfiguration = booterConfiguration.getReporterConfiguration();
      Boolean rep = reporterConfiguration.isTrimStackTrace();
      properties.setProperty("isTrimStackTrace", rep);
      properties.setProperty("reportsDirectory", reporterConfiguration.getReportsDirectory());
      ClassLoaderConfiguration classLoaderConfiguration = providerConfiguration.getClassLoaderConfiguration();
      properties.setProperty("useSystemClassLoader", (String)String.valueOf(classLoaderConfiguration.isUseSystemClassLoader()));
      properties.setProperty("useManifestOnlyJar", (String)String.valueOf(classLoaderConfiguration.isUseManifestOnlyJar()));
      properties.setProperty("failIfNoTests", (String)String.valueOf(booterConfiguration.isFailIfNoTests()));
      properties.setProperty("providerConfiguration", (String)providerConfiguration.getProviderClassName());
      return SystemPropertyManager.writePropertiesFile(properties, this.forkConfiguration.getTempDirectory(), "surefire", this.forkConfiguration.isDebug());
   }

   private String getTypeEncoded(Object value) {
      if (value == null) {
         return null;
      } else {
         String valueToUse;
         if (value instanceof Class) {
            valueToUse = ((Class)value).getName();
         } else {
            valueToUse = value.toString();
         }

         return value.getClass().getName() + "|" + valueToUse;
      }
   }
}
