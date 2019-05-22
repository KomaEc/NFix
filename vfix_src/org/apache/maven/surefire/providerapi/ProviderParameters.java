package org.apache.maven.surefire.providerapi;

import java.util.Properties;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.util.DirectoryScanner;
import org.apache.maven.surefire.util.RunOrderCalculator;
import org.apache.maven.surefire.util.ScanResult;

public interface ProviderParameters {
   /** @deprecated */
   DirectoryScanner getDirectoryScanner();

   ScanResult getScanResult();

   RunOrderCalculator getRunOrderCalculator();

   ReporterFactory getReporterFactory();

   ConsoleLogger getConsoleLogger();

   /** @deprecated */
   DirectoryScannerParameters getDirectoryScannerParameters();

   ReporterConfiguration getReporterConfiguration();

   TestRequest getTestRequest();

   ClassLoader getTestClassLoader();

   Properties getProviderProperties();

   TestArtifactInfo getTestArtifactInfo();
}
