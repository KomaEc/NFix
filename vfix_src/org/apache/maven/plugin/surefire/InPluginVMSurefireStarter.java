package org.apache.maven.plugin.surefire;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import javax.annotation.Nonnull;
import org.apache.maven.surefire.booter.ProviderConfiguration;
import org.apache.maven.surefire.booter.ProviderFactory;
import org.apache.maven.surefire.booter.StartupConfiguration;
import org.apache.maven.surefire.booter.SurefireExecutionException;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.apache.maven.surefire.util.DefaultScanResult;

public class InPluginVMSurefireStarter {
   private final StartupConfiguration startupConfiguration;
   private final StartupReportConfiguration startupReportConfiguration;
   private final ProviderConfiguration providerConfiguration;

   public InPluginVMSurefireStarter(@Nonnull StartupConfiguration startupConfiguration, @Nonnull ProviderConfiguration providerConfiguration, @Nonnull StartupReportConfiguration startupReportConfiguration) {
      this.startupConfiguration = startupConfiguration;
      this.startupReportConfiguration = startupReportConfiguration;
      this.providerConfiguration = providerConfiguration;
   }

   public RunResult runSuitesInProcess(@Nonnull DefaultScanResult scanResult) throws SurefireExecutionException, TestSetFailedException {
      Properties providerProperties = this.providerConfiguration.getProviderProperties();
      scanResult.writeTo(providerProperties);
      this.startupConfiguration.writeSurefireTestClasspathProperty();
      ClassLoader testsClassLoader = this.startupConfiguration.getClasspathConfiguration().createMergedClassLoader();
      CommonReflector surefireReflector = new CommonReflector(testsClassLoader);
      Object factory = surefireReflector.createReportingReporterFactory(this.startupReportConfiguration);

      try {
         return ProviderFactory.invokeProvider((Object)null, testsClassLoader, factory, this.providerConfiguration, false, this.startupConfiguration, true);
      } catch (InvocationTargetException var7) {
         throw new SurefireExecutionException("Exception in provider", var7.getTargetException());
      }
   }
}
