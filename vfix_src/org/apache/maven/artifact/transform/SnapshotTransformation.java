package org.apache.maven.artifact.transform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.deployer.ArtifactDeploymentException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.RepositoryMetadataResolutionException;
import org.apache.maven.artifact.repository.metadata.Snapshot;
import org.apache.maven.artifact.repository.metadata.SnapshotArtifactRepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.codehaus.plexus.util.StringUtils;

public class SnapshotTransformation extends AbstractVersionTransformation {
   private String deploymentTimestamp;
   private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
   private static final String UTC_TIMESTAMP_PATTERN = "yyyyMMdd.HHmmss";

   public void transformForResolve(Artifact artifact, List<ArtifactRepository> remoteRepositories, ArtifactRepository localRepository) throws ArtifactResolutionException {
      if (artifact.isSnapshot() && artifact.getBaseVersion().equals(artifact.getVersion())) {
         try {
            String version = this.resolveVersion(artifact, localRepository, remoteRepositories);
            artifact.updateVersion(version, localRepository);
         } catch (RepositoryMetadataResolutionException var5) {
            throw new ArtifactResolutionException(var5.getMessage(), artifact, var5);
         }
      }

   }

   public void transformForInstall(Artifact artifact, ArtifactRepository localRepository) {
      if (artifact.isSnapshot()) {
         Snapshot snapshot = new Snapshot();
         snapshot.setLocalCopy(true);
         RepositoryMetadata metadata = new SnapshotArtifactRepositoryMetadata(artifact, snapshot);
         artifact.addMetadata(metadata);
      }

   }

   public void transformForDeployment(Artifact artifact, ArtifactRepository remoteRepository, ArtifactRepository localRepository) throws ArtifactDeploymentException {
      if (artifact.isSnapshot()) {
         Snapshot snapshot = new Snapshot();
         if (remoteRepository.isUniqueVersion()) {
            snapshot.setTimestamp(this.getDeploymentTimestamp());
         }

         try {
            int buildNumber = this.resolveLatestSnapshotBuildNumber(artifact, localRepository, remoteRepository);
            snapshot.setBuildNumber(buildNumber + 1);
         } catch (RepositoryMetadataResolutionException var6) {
            throw new ArtifactDeploymentException("Error retrieving previous build number for artifact '" + artifact.getDependencyConflictId() + "': " + var6.getMessage(), var6);
         }

         RepositoryMetadata metadata = new SnapshotArtifactRepositoryMetadata(artifact, snapshot);
         artifact.setResolvedVersion(this.constructVersion(metadata.getMetadata().getVersioning(), artifact.getBaseVersion()));
         artifact.addMetadata(metadata);
      }

   }

   public String getDeploymentTimestamp() {
      if (this.deploymentTimestamp == null) {
         this.deploymentTimestamp = getUtcDateFormatter().format(new Date());
      }

      return this.deploymentTimestamp;
   }

   protected String constructVersion(Versioning versioning, String baseVersion) {
      String version = null;
      Snapshot snapshot = versioning.getSnapshot();
      if (snapshot != null) {
         if (snapshot.getTimestamp() != null && snapshot.getBuildNumber() > 0) {
            String newVersion = snapshot.getTimestamp() + "-" + snapshot.getBuildNumber();
            version = StringUtils.replace(baseVersion, "SNAPSHOT", newVersion);
         } else {
            version = baseVersion;
         }
      }

      return version;
   }

   private int resolveLatestSnapshotBuildNumber(Artifact artifact, ArtifactRepository localRepository, ArtifactRepository remoteRepository) throws RepositoryMetadataResolutionException {
      RepositoryMetadata metadata = new SnapshotArtifactRepositoryMetadata(artifact);
      if (!this.wagonManager.isOnline()) {
         throw new RepositoryMetadataResolutionException("System is offline. Cannot resolve metadata:\n" + metadata.extendedToString() + "\n\n");
      } else {
         this.getLogger().info("Retrieving previous build number from " + remoteRepository.getId());
         this.repositoryMetadataManager.resolveAlways(metadata, localRepository, remoteRepository);
         int buildNumber = 0;
         Metadata repoMetadata = metadata.getMetadata();
         if (repoMetadata != null && repoMetadata.getVersioning() != null && repoMetadata.getVersioning().getSnapshot() != null) {
            buildNumber = repoMetadata.getVersioning().getSnapshot().getBuildNumber();
         }

         return buildNumber;
      }
   }

   public static DateFormat getUtcDateFormatter() {
      DateFormat utcDateFormatter = new SimpleDateFormat("yyyyMMdd.HHmmss");
      utcDateFormatter.setTimeZone(UTC_TIME_ZONE);
      return utcDateFormatter;
   }
}
