package org.apache.maven.scm.provider.integrity.repository;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.apache.maven.scm.provider.integrity.APISession;
import org.apache.maven.scm.provider.integrity.Project;
import org.apache.maven.scm.provider.integrity.Sandbox;

public class IntegrityScmProviderRepository extends ScmProviderRepositoryWithHost {
   private String configurationPath;
   private APISession api;
   private Project siProject;
   private Sandbox siSandbox;

   public IntegrityScmProviderRepository(String host, int port, String user, String paswd, String configPath, ScmLogger logger) {
      this.setHost(host);
      this.setPort(port);
      this.setUser(user);
      this.setPassword(paswd);
      this.configurationPath = configPath;
      this.api = new APISession(logger);
      logger.debug("Configuration Path: " + this.configurationPath);
   }

   public Project getProject() {
      return this.siProject;
   }

   public void setProject(Project project) {
      this.siProject = project;
   }

   public Sandbox getSandbox() {
      return this.siSandbox;
   }

   public void setSandbox(Sandbox sandbox) {
      this.siSandbox = sandbox;
   }

   public APISession getAPISession() {
      return this.api;
   }

   public String getConfigruationPath() {
      return this.configurationPath;
   }
}
