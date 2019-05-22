package org.apache.maven.artifact.repository.layout;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;

public class LegacyRepositoryLayout implements ArtifactRepositoryLayout {
   private static final String PATH_SEPARATOR = "/";

   public String pathOf(Artifact artifact) {
      ArtifactHandler artifactHandler = artifact.getArtifactHandler();
      StringBuffer path = new StringBuffer();
      path.append(artifact.getGroupId()).append('/');
      path.append(artifactHandler.getDirectory()).append('/');
      path.append(artifact.getArtifactId()).append('-').append(artifact.getVersion());
      if (artifact.hasClassifier()) {
         path.append('-').append(artifact.getClassifier());
      }

      if (artifactHandler.getExtension() != null && artifactHandler.getExtension().length() > 0) {
         path.append('.').append(artifactHandler.getExtension());
      }

      return path.toString();
   }

   public String pathOfLocalRepositoryMetadata(ArtifactMetadata metadata, ArtifactRepository repository) {
      return this.pathOfRepositoryMetadata(metadata, metadata.getLocalFilename(repository));
   }

   private String pathOfRepositoryMetadata(ArtifactMetadata metadata, String filename) {
      StringBuffer path = new StringBuffer();
      path.append(metadata.getGroupId()).append("/").append("poms").append("/");
      path.append(filename);
      return path.toString();
   }

   public String pathOfRemoteRepositoryMetadata(ArtifactMetadata metadata) {
      return this.pathOfRepositoryMetadata(metadata, metadata.getRemoteFilename());
   }
}
