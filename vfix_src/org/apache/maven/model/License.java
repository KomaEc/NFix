package org.apache.maven.model;

import java.io.Serializable;

public class License implements Serializable {
   private String name;
   private String url;
   private String distribution;
   private String comments;

   public String getComments() {
      return this.comments;
   }

   public String getDistribution() {
      return this.distribution;
   }

   public String getName() {
      return this.name;
   }

   public String getUrl() {
      return this.url;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }

   public void setDistribution(String distribution) {
      this.distribution = distribution;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
