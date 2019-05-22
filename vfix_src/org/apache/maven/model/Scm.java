package org.apache.maven.model;

import java.io.Serializable;

public class Scm implements Serializable {
   private String connection;
   private String developerConnection;
   private String tag = "HEAD";
   private String url;

   public String getConnection() {
      return this.connection;
   }

   public String getDeveloperConnection() {
      return this.developerConnection;
   }

   public String getTag() {
      return this.tag;
   }

   public String getUrl() {
      return this.url;
   }

   public void setConnection(String connection) {
      this.connection = connection;
   }

   public void setDeveloperConnection(String developerConnection) {
      this.developerConnection = developerConnection;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
