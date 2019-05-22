package org.apache.maven.settings;

import java.io.Serializable;

public class Server extends IdentifiableBase implements Serializable {
   private String username;
   private String password;
   private String privateKey;
   private String passphrase;
   private String filePermissions;
   private String directoryPermissions;
   private Object configuration;

   public Object getConfiguration() {
      return this.configuration;
   }

   public String getDirectoryPermissions() {
      return this.directoryPermissions;
   }

   public String getFilePermissions() {
      return this.filePermissions;
   }

   public String getPassphrase() {
      return this.passphrase;
   }

   public String getPassword() {
      return this.password;
   }

   public String getPrivateKey() {
      return this.privateKey;
   }

   public String getUsername() {
      return this.username;
   }

   public void setConfiguration(Object configuration) {
      this.configuration = configuration;
   }

   public void setDirectoryPermissions(String directoryPermissions) {
      this.directoryPermissions = directoryPermissions;
   }

   public void setFilePermissions(String filePermissions) {
      this.filePermissions = filePermissions;
   }

   public void setPassphrase(String passphrase) {
      this.passphrase = passphrase;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setPrivateKey(String privateKey) {
      this.privateKey = privateKey;
   }

   public void setUsername(String username) {
      this.username = username;
   }
}
