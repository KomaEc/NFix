package org.apache.maven.scm.providers.clearcase.settings;

import java.io.Serializable;

public class Settings implements Serializable {
   private String viewstore;
   private boolean useVWSParameter = true;
   private String clearcaseType;
   private String changelogUserFormat;
   private String modelEncoding = "UTF-8";

   public String getChangelogUserFormat() {
      return this.changelogUserFormat;
   }

   public String getClearcaseType() {
      return this.clearcaseType;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public String getViewstore() {
      return this.viewstore;
   }

   public boolean isUseVWSParameter() {
      return this.useVWSParameter;
   }

   public void setChangelogUserFormat(String changelogUserFormat) {
      this.changelogUserFormat = changelogUserFormat;
   }

   public void setClearcaseType(String clearcaseType) {
      this.clearcaseType = clearcaseType;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setUseVWSParameter(boolean useVWSParameter) {
      this.useVWSParameter = useVWSParameter;
   }

   public void setViewstore(String viewstore) {
      this.viewstore = viewstore;
   }
}
