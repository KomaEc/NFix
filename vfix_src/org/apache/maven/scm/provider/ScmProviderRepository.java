package org.apache.maven.scm.provider;

public abstract class ScmProviderRepository {
   private String user;
   private String password;
   private boolean persistCheckout = false;
   private boolean pushChanges = true;

   public String getUser() {
      return this.user;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public boolean isPushChanges() {
      return this.pushChanges;
   }

   public void setPushChanges(boolean pushChanges) {
      this.pushChanges = pushChanges;
   }

   public boolean isPersistCheckout() {
      String persist = System.getProperty("maven.scm.persistcheckout");
      return persist != null ? Boolean.valueOf(persist) : this.persistCheckout;
   }

   public void setPersistCheckout(boolean persistCheckout) {
      this.persistCheckout = persistCheckout;
   }

   public ScmProviderRepository getParent() {
      throw new UnsupportedOperationException();
   }

   public String getRelativePath(ScmProviderRepository ancestor) {
      throw new UnsupportedOperationException();
   }
}
