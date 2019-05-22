package org.apache.maven.scm.providers.svn.settings;

import java.io.Serializable;

public class Settings implements Serializable {
   private String configDirectory;
   private boolean useCygwinPath = false;
   private String cygwinMountPath = "/cygwin";
   private boolean useNonInteractive = true;
   private boolean useAuthCache = false;
   private boolean trustServerCert = false;
   private String modelEncoding = "UTF-8";

   public String getConfigDirectory() {
      return this.configDirectory;
   }

   public String getCygwinMountPath() {
      return this.cygwinMountPath;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public boolean isTrustServerCert() {
      return this.trustServerCert;
   }

   public boolean isUseAuthCache() {
      return this.useAuthCache;
   }

   public boolean isUseCygwinPath() {
      return this.useCygwinPath;
   }

   public boolean isUseNonInteractive() {
      return this.useNonInteractive;
   }

   public void setConfigDirectory(String configDirectory) {
      this.configDirectory = configDirectory;
   }

   public void setCygwinMountPath(String cygwinMountPath) {
      this.cygwinMountPath = cygwinMountPath;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setTrustServerCert(boolean trustServerCert) {
      this.trustServerCert = trustServerCert;
   }

   public void setUseAuthCache(boolean useAuthCache) {
      this.useAuthCache = useAuthCache;
   }

   public void setUseCygwinPath(boolean useCygwinPath) {
      this.useCygwinPath = useCygwinPath;
   }

   public void setUseNonInteractive(boolean useNonInteractive) {
      this.useNonInteractive = useNonInteractive;
   }
}
