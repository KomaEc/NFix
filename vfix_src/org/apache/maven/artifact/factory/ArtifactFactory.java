package org.apache.maven.artifact.factory;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.VersionRange;

public interface ArtifactFactory {
   String ROLE = ArtifactFactory.class.getName();

   Artifact createArtifact(String var1, String var2, String var3, String var4, String var5);

   Artifact createArtifactWithClassifier(String var1, String var2, String var3, String var4, String var5);

   Artifact createDependencyArtifact(String var1, String var2, VersionRange var3, String var4, String var5, String var6);

   Artifact createDependencyArtifact(String var1, String var2, VersionRange var3, String var4, String var5, String var6, boolean var7);

   Artifact createDependencyArtifact(String var1, String var2, VersionRange var3, String var4, String var5, String var6, String var7);

   Artifact createDependencyArtifact(String var1, String var2, VersionRange var3, String var4, String var5, String var6, String var7, boolean var8);

   Artifact createBuildArtifact(String var1, String var2, String var3, String var4);

   Artifact createProjectArtifact(String var1, String var2, String var3);

   Artifact createParentArtifact(String var1, String var2, String var3);

   Artifact createPluginArtifact(String var1, String var2, VersionRange var3);

   Artifact createProjectArtifact(String var1, String var2, String var3, String var4);

   Artifact createExtensionArtifact(String var1, String var2, VersionRange var3);
}
