package org.apache.maven.model;

import java.io.Serializable;

public class IssueManagement implements Serializable {
   private String system;
   private String url;

   public String getSystem() {
      return this.system;
   }

   public String getUrl() {
      return this.url;
   }

   public void setSystem(String system) {
      this.system = system;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
