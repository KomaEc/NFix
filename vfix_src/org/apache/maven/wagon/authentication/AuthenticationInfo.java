package org.apache.maven.wagon.authentication;

import java.io.Serializable;

public class AuthenticationInfo implements Serializable {
   private String userName;
   private String password;
   private String passphrase;
   private String privateKey;

   public String getPassphrase() {
      return this.passphrase;
   }

   public void setPassphrase(String passphrase) {
      this.passphrase = passphrase;
   }

   public String getPrivateKey() {
      return this.privateKey;
   }

   public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }
}
