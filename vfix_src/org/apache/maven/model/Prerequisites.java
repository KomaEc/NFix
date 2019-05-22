package org.apache.maven.model;

import java.io.Serializable;

public class Prerequisites implements Serializable {
   private String maven = "2.0";

   public String getMaven() {
      return this.maven;
   }

   public void setMaven(String maven) {
      this.maven = maven;
   }
}
