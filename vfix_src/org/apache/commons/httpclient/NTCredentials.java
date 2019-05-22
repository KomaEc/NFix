package org.apache.commons.httpclient;

public class NTCredentials extends UsernamePasswordCredentials {
   private String domain;
   private String host;

   public NTCredentials() {
   }

   public NTCredentials(String userName, String password, String host, String domain) {
      super(userName, password);
      this.domain = domain;
      this.host = host;
   }

   public void setDomain(String domain) {
      this.domain = domain;
   }

   public String getDomain() {
      return this.domain;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getHost() {
      return this.host;
   }

   public String toString() {
      StringBuffer sbResult = new StringBuffer(super.toString());
      sbResult.append(":");
      sbResult.append(this.host == null ? "null" : this.host);
      sbResult.append(this.domain == null ? "null" : this.domain);
      return sbResult.toString();
   }
}
