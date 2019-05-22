package org.apache.maven.artifact.factory;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.artifact.versioning.VersionRange;

public class DefaultArtifactFactory implements ArtifactFactory {
   private ArtifactHandlerManager artifactHandlerManager;

   public Artifact createArtifact(String groupId, String artifactId, String version, String scope, String type) {
      return this.createArtifact(groupId, artifactId, (String)version, scope, type, (String)null, (String)null);
   }

   public Artifact createArtifactWithClassifier(String groupId, String artifactId, String version, String type, String classifier) {
      return this.createArtifact(groupId, artifactId, (String)version, (String)null, type, classifier, (String)null);
   }

   public Artifact createDependencyArtifact(String groupId, String artifactId, VersionRange versionRange, String type, String classifier, String scope) {
      return this.createArtifact(groupId, artifactId, (VersionRange)versionRange, type, classifier, scope, (String)null);
   }

   public Artifact createDependencyArtifact(String groupId, String artifactId, VersionRange versionRange, String type, String classifier, String scope, boolean optional) {
      return this.createArtifact(groupId, artifactId, versionRange, type, classifier, scope, (String)null, optional);
   }

   public Artifact createDependencyArtifact(String groupId, String artifactId, VersionRange versionRange, String type, String classifier, String scope, String inheritedScope) {
      return this.createArtifact(groupId, artifactId, versionRange, type, classifier, scope, inheritedScope);
   }

   public Artifact createDependencyArtifact(String groupId, String artifactId, VersionRange versionRange, String type, String classifier, String scope, String inheritedScope, boolean optional) {
      return this.createArtifact(groupId, artifactId, versionRange, type, classifier, scope, inheritedScope, optional);
   }

   public Artifact createBuildArtifact(String groupId, String artifactId, String version, String packaging) {
      return this.createArtifact(groupId, artifactId, (String)version, (String)null, packaging, (String)null, (String)null);
   }

   public Artifact createProjectArtifact(String groupId, String artifactId, String version) {
      return this.createProjectArtifact(groupId, artifactId, version, (String)null);
   }

   public Artifact createParentArtifact(String groupId, String artifactId, String version) {
      return this.createProjectArtifact(groupId, artifactId, version);
   }

   public Artifact createPluginArtifact(String groupId, String artifactId, VersionRange versionRange) {
      return this.createArtifact(groupId, artifactId, (VersionRange)versionRange, "maven-plugin", (String)null, "runtime", (String)null);
   }

   public Artifact createProjectArtifact(String groupId, String artifactId, String version, String scope) {
      return this.createArtifact(groupId, artifactId, version, scope, "pom");
   }

   public Artifact createExtensionArtifact(String groupId, String artifactId, VersionRange versionRange) {
      return this.createArtifact(groupId, artifactId, (VersionRange)versionRange, "jar", (String)null, "runtime", (String)null);
   }

   private Artifact createArtifact(String groupId, String artifactId, String version, String scope, String type, String classifier, String inheritedScope) {
      VersionRange versionRange = null;
      if (version != null) {
         versionRange = VersionRange.createFromVersion(version);
      }

      return this.createArtifact(groupId, artifactId, versionRange, type, classifier, scope, inheritedScope);
   }

   private Artifact createArtifact(String groupId, String artifactId, VersionRange versionRange, String type, String classifier, String scope, String inheritedScope) {
      return this.createArtifact(groupId, artifactId, versionRange, type, classifier, scope, inheritedScope, false);
   }

   private Artifact createArtifact(String groupId, String artifactId, VersionRange versionRange, String type, String classifier, String scope, String inheritedScope, boolean optional) {
      String desiredScope = "runtime";
      if (inheritedScope == null) {
         desiredScope = scope;
      } else {
         if ("test".equals(scope) || "provided".equals(scope)) {
            return null;
         }

         if ("compile".equals(scope) && "compile".equals(inheritedScope)) {
            desiredScope = "compile";
         }
      }

      if ("test".equals(inheritedScope)) {
         desiredScope = "test";
      }

      if ("provided".equals(inheritedScope)) {
         desiredScope = "provided";
      }

      if ("system".equals(scope)) {
         desiredScope = "system";
      }

      ArtifactHandler handler = this.artifactHandlerManager.getArtifactHandler(type);
      return new DefaultArtifact(groupId, artifactId, versionRange, desiredScope, type, classifier, handler, optional);
   }

   protected ArtifactHandlerManager getArtifactHandlerManager() {
      return this.artifactHandlerManager;
   }
}
