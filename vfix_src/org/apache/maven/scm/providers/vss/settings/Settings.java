package org.apache.maven.scm.providers.vss.settings;

import java.io.Serializable;

public class Settings implements Serializable {
   private String vssDirectory;
   private String modelEncoding = "UTF-8";

   public String getVssDirectory() {
      return this.vssDirectory;
   }

   public void setVssDirectory(String vssDirectory) {
      this.vssDirectory = vssDirectory;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
