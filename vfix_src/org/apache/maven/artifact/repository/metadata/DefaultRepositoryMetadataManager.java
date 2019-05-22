package org.apache.maven.artifact.repository.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Reader;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.TransferFailedException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultRepositoryMetadataManager extends AbstractLogEnabled implements RepositoryMetadataManager {
   private WagonManager wagonManager;
   private Set cachedMetadata = new HashSet();

   public void resolve(RepositoryMetadata metadata, List remoteRepositories, ArtifactRepository localRepository) throws RepositoryMetadataResolutionException {
      boolean alreadyResolved = this.alreadyResolved(metadata);
      if (!alreadyResolved) {
         Iterator i = remoteRepositories.iterator();

         while(true) {
            while(i.hasNext()) {
               ArtifactRepository repository = (ArtifactRepository)i.next();
               ArtifactRepositoryPolicy policy = metadata.isSnapshot() ? repository.getSnapshots() : repository.getReleases();
               if (!policy.isEnabled()) {
                  this.getLogger().debug("Skipping disabled repository " + repository.getId());
               } else if (repository.isBlacklisted()) {
                  this.getLogger().debug("Skipping blacklisted repository " + repository.getId());
               } else {
                  File file = new File(localRepository.getBasedir(), localRepository.pathOfLocalRepositoryMetadata(metadata, repository));
                  boolean checkForUpdates = !file.exists() || policy.checkOutOfDate(new Date(file.lastModified()));
                  if (checkForUpdates) {
                     if (this.wagonManager.isOnline()) {
                        this.getLogger().info(metadata.getKey() + ": checking for updates from " + repository.getId());
                        boolean storeMetadata = false;

                        try {
                           this.wagonManager.getArtifactMetadata(metadata, repository, file, policy.getChecksumPolicy());
                           storeMetadata = true;
                        } catch (ResourceDoesNotExistException var15) {
                           this.getLogger().debug(metadata + " could not be found on repository: " + repository.getId());
                           if (file.exists()) {
                              file.delete();
                           }

                           storeMetadata = true;
                        } catch (TransferFailedException var16) {
                           this.getLogger().warn(metadata + " could not be retrieved from repository: " + repository.getId() + " due to an error: " + var16.getMessage());
                           this.getLogger().debug("Exception", var16);
                           this.getLogger().info("Repository '" + repository.getId() + "' will be blacklisted");
                           repository.setBlacklisted(true);
                        }

                        if (storeMetadata) {
                           if (file.exists()) {
                              file.setLastModified(System.currentTimeMillis());
                           } else {
                              try {
                                 metadata.storeInLocalRepository(localRepository, repository);
                              } catch (RepositoryMetadataStoreException var14) {
                                 throw new RepositoryMetadataResolutionException("Unable to store local copy of metadata: " + var14.getMessage(), var14);
                              }
                           }
                        }
                     } else {
                        this.getLogger().debug("System is offline. Cannot resolve metadata:\n" + metadata.extendedToString() + "\n\n");
                     }
                  }
               }
            }

            this.cachedMetadata.add(metadata.getKey());
            break;
         }
      }

      try {
         this.mergeMetadata(metadata, remoteRepositories, localRepository);
      } catch (RepositoryMetadataStoreException var12) {
         throw new RepositoryMetadataResolutionException("Unable to store local copy of metadata: " + var12.getMessage(), var12);
      } catch (RepositoryMetadataReadException var13) {
         throw new RepositoryMetadataResolutionException("Unable to read local copy of metadata: " + var13.getMessage(), var13);
      }
   }

   private void mergeMetadata(RepositoryMetadata metadata, List remoteRepositories, ArtifactRepository localRepository) throws RepositoryMetadataStoreException, RepositoryMetadataReadException {
      Map previousMetadata = new HashMap();
      ArtifactRepository selected = null;
      Iterator i = remoteRepositories.iterator();

      while(i.hasNext()) {
         ArtifactRepository repository = (ArtifactRepository)i.next();
         ArtifactRepositoryPolicy policy = metadata.isSnapshot() ? repository.getSnapshots() : repository.getReleases();
         if (policy.isEnabled() && this.loadMetadata(metadata, repository, localRepository, previousMetadata)) {
            metadata.setRepository(repository);
            selected = repository;
         }
      }

      if (this.loadMetadata(metadata, localRepository, localRepository, previousMetadata)) {
         metadata.setRepository((ArtifactRepository)null);
         selected = localRepository;
      }

      this.updateSnapshotMetadata(metadata, previousMetadata, selected, localRepository);
   }

   private void updateSnapshotMetadata(RepositoryMetadata metadata, Map previousMetadata, ArtifactRepository selected, ArtifactRepository localRepository) throws RepositoryMetadataStoreException {
      if (metadata.isSnapshot()) {
         Metadata prevMetadata = metadata.getMetadata();
         Iterator i = previousMetadata.keySet().iterator();

         while(i.hasNext()) {
            ArtifactRepository repository = (ArtifactRepository)i.next();
            Metadata m = (Metadata)previousMetadata.get(repository);
            if (repository.equals(selected)) {
               if (m.getVersioning() == null) {
                  m.setVersioning(new Versioning());
               }

               if (m.getVersioning().getSnapshot() == null) {
                  m.getVersioning().setSnapshot(new Snapshot());
               }
            } else if (m.getVersioning() != null && m.getVersioning().getSnapshot() != null && m.getVersioning().getSnapshot().isLocalCopy()) {
               m.getVersioning().getSnapshot().setLocalCopy(false);
               metadata.setMetadata(m);
               metadata.storeInLocalRepository(localRepository, repository);
            }
         }

         metadata.setMetadata(prevMetadata);
      }

   }

   private boolean loadMetadata(RepositoryMetadata repoMetadata, ArtifactRepository remoteRepository, ArtifactRepository localRepository, Map previousMetadata) throws RepositoryMetadataReadException {
      boolean setRepository = false;
      File metadataFile = new File(localRepository.getBasedir(), localRepository.pathOfLocalRepositoryMetadata(repoMetadata, remoteRepository));
      if (metadataFile.exists()) {
         Metadata metadata = readMetadata(metadataFile);
         if (repoMetadata.isSnapshot() && previousMetadata != null) {
            previousMetadata.put(remoteRepository, metadata);
         }

         if (repoMetadata.getMetadata() != null) {
            setRepository = repoMetadata.getMetadata().merge(metadata);
         } else {
            repoMetadata.setMetadata(metadata);
            setRepository = true;
         }
      }

      return setRepository;
   }

   protected static Metadata readMetadata(File mappingFile) throws RepositoryMetadataReadException {
      XmlStreamReader reader = null;

      Metadata result;
      try {
         reader = ReaderFactory.newXmlReader(mappingFile);
         MetadataXpp3Reader mappingReader = new MetadataXpp3Reader();
         result = mappingReader.read((Reader)reader, false);
      } catch (FileNotFoundException var9) {
         throw new RepositoryMetadataReadException("Cannot read metadata from '" + mappingFile + "'", var9);
      } catch (IOException var10) {
         throw new RepositoryMetadataReadException("Cannot read metadata from '" + mappingFile + "': " + var10.getMessage(), var10);
      } catch (XmlPullParserException var11) {
         throw new RepositoryMetadataReadException("Cannot read metadata from '" + mappingFile + "': " + var11.getMessage(), var11);
      } finally {
         IOUtil.close((Reader)reader);
      }

      return result;
   }

   public void resolveAlways(RepositoryMetadata metadata, ArtifactRepository localRepository, ArtifactRepository remoteRepository) throws RepositoryMetadataResolutionException {
      if (!this.wagonManager.isOnline()) {
         throw new RepositoryMetadataResolutionException("System is offline. Cannot resolve required metadata:\n" + metadata.extendedToString());
      } else {
         File file;
         try {
            file = this.getArtifactMetadataFromDeploymentRepository(metadata, localRepository, remoteRepository);
         } catch (TransferFailedException var7) {
            throw new RepositoryMetadataResolutionException(metadata + " could not be retrieved from repository: " + remoteRepository.getId() + " due to an error: " + var7.getMessage(), var7);
         }

         try {
            if (file.exists()) {
               Metadata prevMetadata = readMetadata(file);
               metadata.setMetadata(prevMetadata);
            }

         } catch (RepositoryMetadataReadException var6) {
            throw new RepositoryMetadataResolutionException(var6.getMessage(), var6);
         }
      }
   }

   private File getArtifactMetadataFromDeploymentRepository(ArtifactMetadata metadata, ArtifactRepository localRepository, ArtifactRepository remoteRepository) throws TransferFailedException {
      File file = new File(localRepository.getBasedir(), localRepository.pathOfLocalRepositoryMetadata(metadata, remoteRepository));

      try {
         this.wagonManager.getArtifactMetadataFromDeploymentRepository(metadata, remoteRepository, file, "warn");
      } catch (ResourceDoesNotExistException var6) {
         this.getLogger().info(metadata + " could not be found on repository: " + remoteRepository.getId() + ", so will be created");
         if (file.exists()) {
            file.delete();
         }
      }

      return file;
   }

   private boolean alreadyResolved(ArtifactMetadata metadata) {
      return this.cachedMetadata.contains(metadata.getKey());
   }

   public void deploy(ArtifactMetadata metadata, ArtifactRepository localRepository, ArtifactRepository deploymentRepository) throws RepositoryMetadataDeploymentException {
      if (!this.wagonManager.isOnline()) {
         throw new RepositoryMetadataDeploymentException("System is offline. Cannot deploy metadata:\n" + metadata.extendedToString());
      } else {
         File file;
         if (metadata instanceof RepositoryMetadata) {
            this.getLogger().info("Retrieving previous metadata from " + deploymentRepository.getId());

            try {
               file = this.getArtifactMetadataFromDeploymentRepository(metadata, localRepository, deploymentRepository);
            } catch (TransferFailedException var8) {
               throw new RepositoryMetadataDeploymentException(metadata + " could not be retrieved from repository: " + deploymentRepository.getId() + " due to an error: " + var8.getMessage(), var8);
            }
         } else {
            file = new File(localRepository.getBasedir(), localRepository.pathOfLocalRepositoryMetadata(metadata, deploymentRepository));
         }

         try {
            metadata.storeInLocalRepository(localRepository, deploymentRepository);
         } catch (RepositoryMetadataStoreException var7) {
            throw new RepositoryMetadataDeploymentException("Error installing metadata: " + var7.getMessage(), var7);
         }

         try {
            this.wagonManager.putArtifactMetadata(file, metadata, deploymentRepository);
         } catch (TransferFailedException var6) {
            throw new RepositoryMetadataDeploymentException("Error while deploying metadata: " + var6.getMessage(), var6);
         }
      }
   }

   public void install(ArtifactMetadata metadata, ArtifactRepository localRepository) throws RepositoryMetadataInstallationException {
      try {
         metadata.storeInLocalRepository(localRepository, localRepository);
      } catch (RepositoryMetadataStoreException var4) {
         throw new RepositoryMetadataInstallationException("Error installing metadata: " + var4.getMessage(), var4);
      }
   }
}
