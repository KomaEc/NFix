package org.apache.maven.scm.provider.perforce.repository;

import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;

public class PerforceScmProviderRepository extends ScmProviderRepositoryWithHost {
   private String protocol;
   private String path;

   public PerforceScmProviderRepository(String host, int port, String path, String user, String password) {
      this.setHost(host);
      this.setPort(port);
      this.path = path;
      this.setUser(user);
      this.setPassword(password);
   }

   public PerforceScmProviderRepository(String protocol, String host, int port, String path, String user, String password) {
      this(host, port, path, user, password);
      this.protocol = protocol;
   }

   public String getPath() {
      return this.path;
   }

   public String getProtocol() {
      return this.protocol;
   }
}
