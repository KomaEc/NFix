package org.apache.maven.artifact.resolver;

import edu.emory.mathcs.backport.java.util.concurrent.CountDownLatch;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.Snapshot;
import org.apache.maven.artifact.repository.metadata.SnapshotArtifactRepositoryMetadata;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.transform.ArtifactTransformationManager;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.apache.maven.wagon.TransferFailedException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.FileUtils;

public class DefaultArtifactResolver extends AbstractLogEnabled implements ArtifactResolver {
   private static final int DEFAULT_POOL_SIZE = 5;
   private WagonManager wagonManager;
   private ArtifactTransformationManager transformationManager;
   protected ArtifactFactory artifactFactory;
   private ArtifactCollector artifactCollector;
   private final ThreadPoolExecutor resolveArtifactPool;

   public DefaultArtifactResolver() {
      this.resolveArtifactPool = new ThreadPoolExecutor(5, 5, 3L, TimeUnit.SECONDS, new LinkedBlockingQueue());
   }

   public void resolve(Artifact artifact, List remoteRepositories, ArtifactRepository localRepository) throws ArtifactResolutionException, ArtifactNotFoundException {
      this.resolve(artifact, remoteRepositories, localRepository, false);
   }

   public void resolveAlways(Artifact artifact, List remoteRepositories, ArtifactRepository localRepository) throws ArtifactResolutionException, ArtifactNotFoundException {
      this.resolve(artifact, remoteRepositories, localRepository, true);
   }

   private void resolve(Artifact artifact, List remoteRepositories, ArtifactRepository localRepository, boolean force) throws ArtifactResolutionException, ArtifactNotFoundException {
      if (artifact != null) {
         if ("system".equals(artifact.getScope())) {
            File systemFile = artifact.getFile();
            if (systemFile == null) {
               throw new ArtifactNotFoundException("System artifact: " + artifact + " has no file attached", artifact);
            }

            if (!systemFile.isFile()) {
               throw new ArtifactNotFoundException("System artifact: " + artifact + " is not a file: " + systemFile, artifact);
            }

            if (!systemFile.exists()) {
               throw new ArtifactNotFoundException("System artifact: " + artifact + " not found in path: " + systemFile, artifact);
            }

            artifact.setResolved(true);
         } else if (!artifact.isResolved()) {
            String localPath = localRepository.pathOf(artifact);
            artifact.setFile(new File(localRepository.getBasedir(), localPath));
            this.transformationManager.transformForResolve(artifact, remoteRepositories, localRepository);
            boolean localCopy = false;
            Iterator i = artifact.getMetadataList().iterator();

            while(i.hasNext()) {
               ArtifactMetadata m = (ArtifactMetadata)i.next();
               if (m instanceof SnapshotArtifactRepositoryMetadata) {
                  SnapshotArtifactRepositoryMetadata snapshotMetadata = (SnapshotArtifactRepositoryMetadata)m;
                  Metadata metadata = snapshotMetadata.getMetadata();
                  if (metadata != null) {
                     Versioning versioning = metadata.getVersioning();
                     if (versioning != null) {
                        Snapshot snapshot = versioning.getSnapshot();
                        if (snapshot != null) {
                           localCopy = snapshot.isLocalCopy();
                        }
                     }
                  }
               }
            }

            File destination = artifact.getFile();
            List repositories = remoteRepositories;
            if (artifact.isSnapshot() && artifact.getBaseVersion().equals(artifact.getVersion()) && destination.exists() && !localCopy && this.wagonManager.isOnline()) {
               Date comparisonDate = new Date(destination.lastModified());
               repositories = new ArrayList(remoteRepositories);
               Iterator i = ((List)repositories).iterator();

               label118:
               while(true) {
                  ArtifactRepositoryPolicy policy;
                  do {
                     if (!i.hasNext()) {
                        if (!((List)repositories).isEmpty()) {
                           force = true;
                        }
                        break label118;
                     }

                     ArtifactRepository repository = (ArtifactRepository)i.next();
                     policy = repository.getSnapshots();
                  } while(policy.isEnabled() && policy.checkOutOfDate(comparisonDate));

                  i.remove();
               }
            }

            if (destination.exists() && !force) {
               if (destination.exists()) {
                  artifact.setResolved(true);
               }
            } else {
               if (!this.wagonManager.isOnline()) {
                  throw new ArtifactNotFoundException("System is offline.", artifact);
               }

               try {
                  if (artifact.getRepository() != null) {
                     this.wagonManager.getArtifact(artifact, artifact.getRepository());
                  } else {
                     this.wagonManager.getArtifact(artifact, (List)repositories);
                  }

                  if (!artifact.isResolved() && !destination.exists()) {
                     throw new ArtifactResolutionException("Failed to resolve artifact, possibly due to a repository list that is not appropriately equipped for this artifact's metadata.", artifact, this.getMirroredRepositories(remoteRepositories));
                  }
               } catch (ResourceDoesNotExistException var14) {
                  throw new ArtifactNotFoundException(var14.getMessage(), artifact, this.getMirroredRepositories(remoteRepositories), var14);
               } catch (TransferFailedException var15) {
                  throw new ArtifactResolutionException(var15.getMessage(), artifact, this.getMirroredRepositories(remoteRepositories), var15);
               }
            }

            if (artifact.isSnapshot() && !artifact.getBaseVersion().equals(artifact.getVersion())) {
               String version = artifact.getVersion();
               artifact.selectVersion(artifact.getBaseVersion());
               File copy = new File(localRepository.getBasedir(), localRepository.pathOf(artifact));
               if (!copy.exists() || copy.lastModified() != destination.lastModified() || copy.length() != destination.length()) {
                  try {
                     FileUtils.copyFile(destination, copy);
                     copy.setLastModified(destination.lastModified());
                  } catch (IOException var13) {
                     throw new ArtifactResolutionException("Unable to copy resolved artifact for local use: " + var13.getMessage(), artifact, this.getMirroredRepositories(remoteRepositories), var13);
                  }
               }

               artifact.setFile(copy);
               artifact.selectVersion(version);
            }
         }

      }
   }

   public ArtifactResolutionResult resolveTransitively(Set artifacts, Artifact originatingArtifact, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source, ArtifactFilter filter) throws ArtifactResolutionException, ArtifactNotFoundException {
      return this.resolveTransitively(artifacts, originatingArtifact, Collections.EMPTY_MAP, localRepository, remoteRepositories, source, filter);
   }

   public ArtifactResolutionResult resolveTransitively(Set artifacts, Artifact originatingArtifact, Map managedVersions, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source) throws ArtifactResolutionException, ArtifactNotFoundException {
      return this.resolveTransitively(artifacts, originatingArtifact, managedVersions, localRepository, remoteRepositories, source, (ArtifactFilter)null);
   }

   public ArtifactResolutionResult resolveTransitively(Set artifacts, Artifact originatingArtifact, Map managedVersions, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source, ArtifactFilter filter) throws ArtifactResolutionException, ArtifactNotFoundException {
      List listeners = new ArrayList();
      if (this.getLogger().isDebugEnabled()) {
         listeners.add(new DebugResolutionListener(this.getLogger()));
      }

      listeners.add(new WarningResolutionListener(this.getLogger()));
      return this.resolveTransitively(artifacts, originatingArtifact, managedVersions, localRepository, remoteRepositories, source, filter, listeners);
   }

   public ArtifactResolutionResult resolveTransitively(Set artifacts, Artifact originatingArtifact, Map managedVersions, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source, ArtifactFilter filter, List listeners) throws ArtifactResolutionException, ArtifactNotFoundException {
      ArtifactResolutionResult artifactResolutionResult = this.artifactCollector.collect(artifacts, originatingArtifact, managedVersions, localRepository, remoteRepositories, source, filter, listeners);
      List resolvedArtifacts = Collections.synchronizedList(new ArrayList());
      List missingArtifacts = Collections.synchronizedList(new ArrayList());
      CountDownLatch latch = new CountDownLatch(artifactResolutionResult.getArtifactResolutionNodes().size());
      Map nodesByGroupId = new HashMap();

      ResolutionNode node;
      Object nodes;
      for(Iterator i = artifactResolutionResult.getArtifactResolutionNodes().iterator(); i.hasNext(); ((List)nodes).add(node)) {
         node = (ResolutionNode)i.next();
         nodes = (List)nodesByGroupId.get(node.getArtifact().getGroupId());
         if (nodes == null) {
            nodes = new ArrayList();
            nodesByGroupId.put(node.getArtifact().getGroupId(), nodes);
         }
      }

      List resolutionExceptions = Collections.synchronizedList(new ArrayList());

      try {
         Iterator i = nodesByGroupId.values().iterator();

         while(true) {
            if (!i.hasNext()) {
               latch.await();
               break;
            }

            List nodes = (List)i.next();
            this.resolveArtifactPool.execute(new DefaultArtifactResolver.ResolveArtifactTask(this.resolveArtifactPool, latch, nodes, localRepository, resolvedArtifacts, missingArtifacts, resolutionExceptions));
         }
      } catch (InterruptedException var17) {
         throw new ArtifactResolutionException("Resolution interrupted", originatingArtifact, var17);
      }

      if (!resolutionExceptions.isEmpty()) {
         throw (ArtifactResolutionException)resolutionExceptions.get(0);
      } else if (missingArtifacts.size() > 0) {
         throw new MultipleArtifactsNotFoundException(originatingArtifact, resolvedArtifacts, missingArtifacts, this.getMirroredRepositories(remoteRepositories));
      } else {
         return artifactResolutionResult;
      }
   }

   private List getMirroredRepositories(List remoteRepositories) {
      Map repos = new HashMap();
      Iterator i = remoteRepositories.iterator();

      while(i.hasNext()) {
         ArtifactRepository repository = (ArtifactRepository)i.next();
         ArtifactRepository repo = this.wagonManager.getMirrorRepository(repository);
         repos.put(repo.getId(), repo);
      }

      return new ArrayList(repos.values());
   }

   public ArtifactResolutionResult resolveTransitively(Set artifacts, Artifact originatingArtifact, List remoteRepositories, ArtifactRepository localRepository, ArtifactMetadataSource source) throws ArtifactResolutionException, ArtifactNotFoundException {
      return this.resolveTransitively(artifacts, originatingArtifact, (ArtifactRepository)localRepository, (List)remoteRepositories, (ArtifactMetadataSource)source, (ArtifactFilter)null);
   }

   public ArtifactResolutionResult resolveTransitively(Set artifacts, Artifact originatingArtifact, List remoteRepositories, ArtifactRepository localRepository, ArtifactMetadataSource source, List listeners) throws ArtifactResolutionException, ArtifactNotFoundException {
      return this.resolveTransitively(artifacts, originatingArtifact, Collections.EMPTY_MAP, localRepository, remoteRepositories, source, (ArtifactFilter)null, listeners);
   }

   public synchronized void configureNumberOfThreads(int threads) {
      this.resolveArtifactPool.setCorePoolSize(threads);
      this.resolveArtifactPool.setMaximumPoolSize(threads);
   }

   void setWagonManager(WagonManager wagonManager) {
      this.wagonManager = wagonManager;
   }

   private class ResolveArtifactTask implements Runnable {
      private List nodes;
      private ArtifactRepository localRepository;
      private List resolvedArtifacts;
      private List missingArtifacts;
      private CountDownLatch latch;
      private ThreadPoolExecutor pool;
      private List resolutionExceptions;

      public ResolveArtifactTask(ThreadPoolExecutor pool, CountDownLatch latch, List nodes, ArtifactRepository localRepository, List resolvedArtifacts, List missingArtifacts, List resolutionExceptions) {
         this.nodes = nodes;
         this.localRepository = localRepository;
         this.resolvedArtifacts = resolvedArtifacts;
         this.missingArtifacts = missingArtifacts;
         this.latch = latch;
         this.pool = pool;
         this.resolutionExceptions = resolutionExceptions;
      }

      public void run() {
         Iterator i = this.nodes.iterator();
         ResolutionNode node = (ResolutionNode)i.next();
         i.remove();

         try {
            this.resolveArtifact(node);
         } catch (ArtifactResolutionException var7) {
            this.resolutionExceptions.add(var7);
         } finally {
            this.latch.countDown();
            if (i.hasNext()) {
               this.pool.execute(DefaultArtifactResolver.this.new ResolveArtifactTask(this.pool, this.latch, this.nodes, this.localRepository, this.resolvedArtifacts, this.missingArtifacts, this.resolutionExceptions));
            }

         }

      }

      private void resolveArtifact(ResolutionNode node) throws ArtifactResolutionException {
         try {
            DefaultArtifactResolver.this.resolve(node.getArtifact(), node.getRemoteRepositories(), this.localRepository);
            this.resolvedArtifacts.add(node.getArtifact());
         } catch (ArtifactNotFoundException var3) {
            DefaultArtifactResolver.this.getLogger().debug(var3.getMessage(), var3);
            this.missingArtifacts.add(node.getArtifact());
         }

      }
   }
}
