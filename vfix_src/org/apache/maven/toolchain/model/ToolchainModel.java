package org.apache.maven.toolchain.model;

import java.io.Serializable;

public class ToolchainModel implements Serializable {
   private String type;
   private Object provides;
   private Object configuration;

   public Object getConfiguration() {
      return this.configuration;
   }

   public Object getProvides() {
      return this.provides;
   }

   public String getType() {
      return this.type;
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setProvides(Object provides) {
      this.provides = provides;
   }

   public void setType(String type) {
      this.type = type;
   }
}
