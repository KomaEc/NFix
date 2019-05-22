package org.apache.maven.settings;

import java.io.Serializable;

public class Mirror extends IdentifiableBase implements Serializable {
   private String mirrorOf;
   private String name;
   private String url;

   public String getMirrorOf() {
      return this.mirrorOf;
   }

   public String getName() {
      return this.name;
   }

   public String getUrl() {
      return this.url;
   }

   public void setMirrorOf(String mirrorOf) {
      this.mirrorOf = mirrorOf;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
