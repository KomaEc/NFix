package org.apache.maven.scm.providers.starteam.settings;

import java.io.Serializable;

public class Settings implements Serializable {
   private boolean compressionEnable = false;
   private String eol = "on";
   private String modelEncoding = "UTF-8";

   public String getEol() {
      return this.eol;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public boolean isCompressionEnable() {
      return this.compressionEnable;
   }

   public void setCompressionEnable(boolean compressionEnable) {
      this.compressionEnable = compressionEnable;
   }

   public void setEol(String eol) {
      this.eol = eol;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }
}
