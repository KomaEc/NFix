package org.apache.maven.scm.provider;

public abstract class ScmProviderRepositoryWithHost extends ScmProviderRepository {
   private String host;
   private int port;
   private String privateKey;
   private String passphrase;

   public String getHost() {
      return this.host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public String getPrivateKey() {
      return this.privateKey;
   }

   public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
   }

   public String getPassphrase() {
      return this.passphrase;
   }

   public void setPassphrase(String passphrase) {
      this.passphrase = passphrase;
   }
}
