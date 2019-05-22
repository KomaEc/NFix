package org.apache.maven.artifact.deployer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataDeploymentException;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataManager;
import org.apache.maven.artifact.transform.ArtifactTransformationManager;
import org.apache.maven.wagon.TransferFailedException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.FileUtils;

public class DefaultArtifactDeployer extends AbstractLogEnabled implements ArtifactDeployer {
   private WagonManager wagonManager;
   private ArtifactTransformationManager transformationManager;
   private RepositoryMetadataManager repositoryMetadataManager;

   /** @deprecated */
   public void deploy(String basedir, String finalName, Artifact artifact, ArtifactRepository deploymentRepository, ArtifactRepository localRepository) throws ArtifactDeploymentException {
      String extension = artifact.getArtifactHandler().getExtension();
      File source = new File(basedir, finalName + "." + extension);
      this.deploy(source, artifact, deploymentRepository, localRepository);
   }

   public void deploy(File source, Artifact artifact, ArtifactRepository deploymentRepository, ArtifactRepository localRepository) throws ArtifactDeploymentException {
      if (!this.wagonManager.isOnline()) {
         throw new ArtifactDeploymentException("System is offline. Cannot deploy artifact: " + artifact + ".");
      } else {
         boolean useArtifactFile = false;
         File oldArtifactFile = artifact.getFile();
         if ("pom".equals(artifact.getType())) {
            artifact.setFile(source);
            useArtifactFile = true;
         }

         try {
            this.transformationManager.transformForDeployment(artifact, deploymentRepository, localRepository);
            if (useArtifactFile) {
               source = artifact.getFile();
               artifact.setFile(oldArtifactFile);
            }

            File artifactFile = new File(localRepository.getBasedir(), localRepository.pathOf(artifact));
            if (!artifactFile.equals(source)) {
               FileUtils.copyFile(source, artifactFile);
            }

            this.wagonManager.putArtifact(source, artifact, deploymentRepository);
            Iterator i = artifact.getMetadataList().iterator();

            while(i.hasNext()) {
               ArtifactMetadata metadata = (ArtifactMetadata)i.next();
               this.repositoryMetadataManager.deploy(metadata, localRepository, deploymentRepository);
            }

         } catch (TransferFailedException var10) {
            throw new ArtifactDeploymentException("Error deploying artifact: " + var10.getMessage(), var10);
         } catch (IOException var11) {
            throw new ArtifactDeploymentException("Error deploying artifact: " + var11.getMessage(), var11);
         } catch (RepositoryMetadataDeploymentException var12) {
            throw new ArtifactDeploymentException("Error installing artifact's metadata: " + var12.getMessage(), var12);
         }
      }
   }
}
