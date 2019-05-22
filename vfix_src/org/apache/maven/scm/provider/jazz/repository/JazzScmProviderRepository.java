package org.apache.maven.scm.provider.jazz.repository;

import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.codehaus.plexus.util.StringUtils;

public class JazzScmProviderRepository extends ScmProviderRepositoryWithHost {
   private String fRepositoryURI;
   private String fRepositoryWorkspace;
   private int fWorkspaceAlias;
   private String fWorkspace;
   private int fFlowTargetAlias;
   private String fFlowTarget;
   private String fComponent;
   private String fBaseline;

   public JazzScmProviderRepository(String repositoryURI, String userName, String password, String hostName, int port, String repositoryWorkspace) {
      this.fRepositoryURI = repositoryURI;
      this.setUser(userName);
      this.setPassword(password);
      this.setHost(hostName);
      this.setPort(port);
      this.fRepositoryWorkspace = repositoryWorkspace;
   }

   public boolean isPushChangesAndHaveFlowTargets() {
      return !this.isPushChanges() ? this.isPushChanges() : this.isHaveFlowTargets();
   }

   public boolean isHaveFlowTargets() {
      return StringUtils.isNotEmpty(this.getWorkspace()) && StringUtils.isNotEmpty(this.getFlowTarget()) && !this.getWorkspace().equals(this.getFlowTarget()) && this.getWorkspaceAlias() != this.getFlowTargetAlias();
   }

   public String getRepositoryURI() {
      return this.fRepositoryURI;
   }

   public String getRepositoryWorkspace() {
      return this.fRepositoryWorkspace;
   }

   public int getWorkspaceAlias() {
      return this.fWorkspaceAlias;
   }

   public void setWorkspaceAlias(int workspaceAlias) {
      this.fWorkspaceAlias = workspaceAlias;
   }

   public String getWorkspace() {
      return this.fWorkspace;
   }

   public void setWorkspace(String fWorkspace) {
      this.fWorkspace = fWorkspace;
   }

   public int getFlowTargetAlias() {
      return this.fFlowTargetAlias;
   }

   public void setFlowTargetAlias(int flowTargetAlias) {
      this.fFlowTargetAlias = flowTargetAlias;
   }

   public String getFlowTarget() {
      return this.fFlowTarget;
   }

   public void setFlowTarget(String flowTarget) {
      this.fFlowTarget = flowTarget;
   }

   public String getComponent() {
      return this.fComponent;
   }

   public void setComponent(String component) {
      this.fComponent = component;
   }

   public String getBaseline() {
      return this.fBaseline;
   }

   public void setBaseline(String baseline) {
      this.fBaseline = baseline;
   }

   public String toString() {
      return this.getRepositoryURI() + ":" + this.getRepositoryWorkspace();
   }
}
