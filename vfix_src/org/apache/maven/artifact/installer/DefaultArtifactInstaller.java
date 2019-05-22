package org.apache.maven.artifact.installer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataInstallationException;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.transform.ArtifactTransformationManager;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.FileUtils;

public class DefaultArtifactInstaller extends AbstractLogEnabled implements ArtifactInstaller {
   private ArtifactTransformationManager transformationManager;
   private RepositoryMetadataManager repositoryMetadataManager;

   /** @deprecated */
   public void install(String basedir, String finalName, Artifact artifact, ArtifactRepository localRepository) throws ArtifactInstallationException {
      String extension = artifact.getArtifactHandler().getExtension();
      File source = new File(basedir, finalName + "." + extension);
      this.install(source, artifact, localRepository);
   }

   public void install(File source, Artifact artifact, ArtifactRepository localRepository) throws ArtifactInstallationException {
      boolean useArtifactFile = false;
      File oldArtifactFile = artifact.getFile();
      if ("pom".equals(artifact.getType())) {
         artifact.setFile(source);
         useArtifactFile = true;
      }

      try {
         this.transformationManager.transformForInstall(artifact, localRepository);
         if (useArtifactFile) {
            source = artifact.getFile();
            artifact.setFile(oldArtifactFile);
         }

         String localPath = localRepository.pathOf(artifact);
         File destination = new File(localRepository.getBasedir(), localPath);
         if (!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
         }

         this.getLogger().info("Installing " + source.getPath() + " to " + destination);
         FileUtils.copyFile(source, destination);
         if (useArtifactFile) {
            artifact.setFile(destination);
         }

         Iterator i = artifact.getMetadataList().iterator();

         while(i.hasNext()) {
            ArtifactMetadata metadata = (ArtifactMetadata)i.next();
            this.repositoryMetadataManager.install(metadata, localRepository);
         }

      } catch (IOException var10) {
         throw new ArtifactInstallationException("Error installing artifact: " + var10.getMessage(), var10);
      } catch (RepositoryMetadataInstallationException var11) {
         throw new ArtifactInstallationException("Error installing artifact's metadata: " + var11.getMessage(), var11);
      }
   }
}
