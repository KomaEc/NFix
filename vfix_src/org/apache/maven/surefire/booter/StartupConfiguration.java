package org.apache.maven.surefire.booter;

public class StartupConfiguration {
   private final String providerClassName;
   private final ClasspathConfiguration classpathConfiguration;
   private final ClassLoaderConfiguration classLoaderConfiguration;
   private final boolean isForkRequested;
   private final boolean isInForkedVm;
   private static final String SUREFIRE_TEST_CLASSPATH = "surefire.test.class.path";

   public StartupConfiguration(String providerClassName, ClasspathConfiguration classpathConfiguration, ClassLoaderConfiguration classLoaderConfiguration, boolean isForkRequested, boolean inForkedVm) {
      this.classpathConfiguration = classpathConfiguration;
      this.classLoaderConfiguration = classLoaderConfiguration;
      this.isForkRequested = isForkRequested;
      this.providerClassName = providerClassName;
      this.isInForkedVm = inForkedVm;
   }

   public boolean isProviderMainClass() {
      return this.providerClassName.endsWith("#main");
   }

   public static StartupConfiguration inForkedVm(String providerClassName, ClasspathConfiguration classpathConfiguration, ClassLoaderConfiguration classLoaderConfiguration) {
      return new StartupConfiguration(providerClassName, classpathConfiguration, classLoaderConfiguration, true, true);
   }

   public ClasspathConfiguration getClasspathConfiguration() {
      return this.classpathConfiguration;
   }

   public boolean useSystemClassLoader() {
      return this.classLoaderConfiguration.isUseSystemClassLoader() && (this.isInForkedVm || this.isForkRequested);
   }

   public boolean isManifestOnlyJarRequestedAndUsable() {
      return this.classLoaderConfiguration.isManifestOnlyJarRequestedAndUsable();
   }

   public String getProviderClassName() {
      return this.providerClassName;
   }

   public String getActualClassName() {
      return this.isProviderMainClass() ? stripEnd(this.providerClassName, "#main") : this.providerClassName;
   }

   public static String stripEnd(String str, String strip) {
      if (str == null) {
         return null;
      } else {
         int end = str.length();
         if (strip == null) {
            while(end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
               --end;
            }
         } else {
            while(end != 0 && strip.indexOf(str.charAt(end - 1)) != -1) {
               --end;
            }
         }

         return str.substring(0, end);
      }
   }

   public ClassLoaderConfiguration getClassLoaderConfiguration() {
      return this.classLoaderConfiguration;
   }

   public boolean isShadefire() {
      return this.providerClassName.startsWith("org.apache.maven.surefire.shadefire");
   }

   public void writeSurefireTestClasspathProperty() {
      ClasspathConfiguration classpathConfiguration = this.getClasspathConfiguration();
      classpathConfiguration.getTestClasspath().writeToSystemProperty("surefire.test.class.path");
   }
}
