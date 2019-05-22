package org.apache.maven.surefire.booter;

public class ClassLoaderConfiguration {
   private final boolean useSystemClassLoader;
   private final boolean useManifestOnlyJar;

   public ClassLoaderConfiguration(boolean useSystemClassLoader, boolean useManifestOnlyJar) {
      this.useSystemClassLoader = useSystemClassLoader;
      this.useManifestOnlyJar = useManifestOnlyJar;
   }

   public boolean isUseSystemClassLoader() {
      return this.useSystemClassLoader;
   }

   public boolean isUseManifestOnlyJar() {
      return this.useManifestOnlyJar;
   }

   public boolean isManifestOnlyJarRequestedAndUsable() {
      return this.isUseSystemClassLoader() && this.useManifestOnlyJar;
   }
}
