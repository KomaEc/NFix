package org.apache.maven.plugin.surefire;

import java.io.File;
import java.lang.reflect.Constructor;
import javax.annotation.Nonnull;
import org.apache.maven.plugin.surefire.report.DefaultReporterFactory;
import org.apache.maven.surefire.util.ReflectionUtils;
import org.apache.maven.surefire.util.SurefireReflectionException;

public class CommonReflector {
   private final Class<?> startupReportConfiguration;
   private final ClassLoader surefireClassLoader;

   public CommonReflector(@Nonnull ClassLoader surefireClassLoader) {
      this.surefireClassLoader = surefireClassLoader;

      try {
         this.startupReportConfiguration = surefireClassLoader.loadClass(StartupReportConfiguration.class.getName());
      } catch (ClassNotFoundException var3) {
         throw new SurefireReflectionException(var3);
      }
   }

   public Object createReportingReporterFactory(@Nonnull StartupReportConfiguration startupReportConfiguration) {
      Class<?>[] args = new Class[]{this.startupReportConfiguration};
      Object src = this.createStartupReportConfiguration(startupReportConfiguration);
      Object[] params = new Object[]{src};
      return ReflectionUtils.instantiateObject(DefaultReporterFactory.class.getName(), args, params, this.surefireClassLoader);
   }

   Object createStartupReportConfiguration(@Nonnull StartupReportConfiguration reporterConfiguration) {
      Constructor<?> constructor = ReflectionUtils.getConstructor(this.startupReportConfiguration, new Class[]{Boolean.TYPE, Boolean.TYPE, String.class, Boolean.TYPE, Boolean.TYPE, File.class, Boolean.TYPE, String.class, String.class, Boolean.TYPE});
      Object[] params = new Object[]{reporterConfiguration.isUseFile(), reporterConfiguration.isPrintSummary(), reporterConfiguration.getReportFormat(), reporterConfiguration.isRedirectTestOutputToFile(), reporterConfiguration.isDisableXmlReport(), reporterConfiguration.getReportsDirectory(), reporterConfiguration.isTrimStackTrace(), reporterConfiguration.getReportNameSuffix(), reporterConfiguration.getConfigurationHash(), reporterConfiguration.isRequiresRunHistory()};
      return ReflectionUtils.newInstance(constructor, params);
   }
}
