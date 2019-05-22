package org.apache.maven.scm.provider.starteam.repository;

import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;

public class StarteamScmProviderRepository extends ScmProviderRepositoryWithHost {
   private String path;

   public StarteamScmProviderRepository(String user, String password, String host, int port, String path) {
      this.setUser(user);
      this.setPassword(password);
      this.setHost(host);
      this.setPort(port);
      if (!path.startsWith("/")) {
         throw new IllegalArgumentException("The path must be start with a slash?");
      } else {
         this.path = path;
      }
   }

   public String getUrl() {
      return this.getHost() + ":" + this.getPort() + this.path;
   }

   public String getFullUrl() {
      String fullUrl = this.getUser() + ":";
      if (this.getPassword() != null) {
         fullUrl = fullUrl + this.getPassword();
      }

      fullUrl = fullUrl + "@" + this.getUrl();
      return fullUrl;
   }

   public String getPath() {
      return this.path;
   }
}
