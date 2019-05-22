package org.apache.commons.httpclient;

public class UsernamePasswordCredentials implements Credentials {
   private String userName;
   private String password;

   public UsernamePasswordCredentials() {
   }

   public UsernamePasswordCredentials(String usernamePassword) {
      int atColon = usernamePassword.indexOf(58);
      if (atColon >= 0) {
         this.userName = usernamePassword.substring(0, atColon);
         this.password = usernamePassword.substring(atColon + 1);
      } else {
         this.userName = usernamePassword;
      }

   }

   public UsernamePasswordCredentials(String userName, String password) {
      this.userName = userName;
      this.password = password;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPassword() {
      return this.password;
   }

   public String toString() {
      StringBuffer result = new StringBuffer();
      result.append(this.userName == null ? "null" : this.userName);
      result.append(":");
      result.append(this.password == null ? "null" : this.password);
      return result.toString();
   }
}
