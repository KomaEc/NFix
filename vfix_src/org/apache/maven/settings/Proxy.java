package org.apache.maven.settings;

import java.io.Serializable;

public class Proxy extends IdentifiableBase implements Serializable {
   private boolean active = true;
   private String protocol = "http";
   private String username;
   private String password;
   private int port = 8080;
   private String host;
   private String nonProxyHosts;

   public String getHost() {
      return this.host;
   }

   public String getNonProxyHosts() {
      return this.nonProxyHosts;
   }

   public String getPassword() {
      return this.password;
   }

   public int getPort() {
      return this.port;
   }

   public String getProtocol() {
      return this.protocol;
   }

   public String getUsername() {
      return this.username;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public void setNonProxyHosts(String nonProxyHosts) {
      this.nonProxyHosts = nonProxyHosts;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }

   public void setUsername(String username) {
      this.username = username;
   }
}
