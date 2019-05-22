package org.apache.maven.wagon.proxy;

import java.io.Serializable;

public class ProxyInfo implements Serializable {
   public static final String PROXY_SOCKS5 = "SOCKS_5";
   public static final String PROXY_SOCKS4 = "SOCKS4";
   public static final String PROXY_HTTP = "HTTP";
   private String host = null;
   private String userName = null;
   private String password = null;
   private int port = -1;
   private String type = null;
   private String nonProxyHosts;
   private String ntlmHost;
   private String ntlmDomain;

   public String getHost() {
      return this.host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getNonProxyHosts() {
      return this.nonProxyHosts;
   }

   public void setNonProxyHosts(String nonProxyHosts) {
      this.nonProxyHosts = nonProxyHosts;
   }

   public String getNtlmHost() {
      return this.ntlmHost;
   }

   public void setNtlmHost(String ntlmHost) {
      this.ntlmHost = ntlmHost;
   }

   public void setNtlmDomain(String ntlmDomain) {
      this.ntlmDomain = ntlmDomain;
   }

   public String getNtlmDomain() {
      return this.ntlmDomain;
   }
}
