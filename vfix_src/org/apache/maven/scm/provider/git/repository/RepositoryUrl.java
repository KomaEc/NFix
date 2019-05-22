package org.apache.maven.scm.provider.git.repository;

public class RepositoryUrl {
   private String protocol;
   private String host;
   private String port;
   private String path;
   private String userName;
   private String password;

   public String getProtocol() {
      return this.protocol;
   }

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }

   public String getHost() {
      return this.host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getPort() {
      return this.port;
   }

   public void setPort(String port) {
      this.port = port;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }
}
