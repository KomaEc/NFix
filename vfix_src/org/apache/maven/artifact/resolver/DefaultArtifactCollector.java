package org.apache.maven.artifact.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.metadata.ResolutionGroup;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.ManagedVersionMap;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;

public class DefaultArtifactCollector implements ArtifactCollector {
   public ArtifactResolutionResult collect(Set artifacts, Artifact originatingArtifact, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source, ArtifactFilter filter, List listeners) throws ArtifactResolutionException {
      return this.collect(artifacts, originatingArtifact, Collections.EMPTY_MAP, localRepository, remoteRepositories, source, filter, listeners);
   }

   public ArtifactResolutionResult collect(Set artifacts, Artifact originatingArtifact, Map managedVersions, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source, ArtifactFilter filter, List listeners) throws ArtifactResolutionException {
      Map resolvedArtifacts = new LinkedHashMap();
      ResolutionNode root = new ResolutionNode(originatingArtifact, remoteRepositories);
      root.addDependencies(artifacts, remoteRepositories, filter);
      ManagedVersionMap versionMap = this.getManagedVersionsMap(originatingArtifact, managedVersions);
      this.recurse(originatingArtifact, root, resolvedArtifacts, versionMap, localRepository, remoteRepositories, source, filter, listeners);
      Set set = new LinkedHashSet();
      Iterator i = resolvedArtifacts.values().iterator();

      label42:
      while(i.hasNext()) {
         List nodes = (List)i.next();
         Iterator j = nodes.iterator();

         while(true) {
            ResolutionNode node;
            Artifact artifact;
            do {
               do {
                  do {
                     do {
                        if (!j.hasNext()) {
                           continue label42;
                        }

                        node = (ResolutionNode)j.next();
                     } while(node.equals(root));
                  } while(!node.isActive());

                  artifact = node.getArtifact();
               } while(!node.filterTrail(filter));
            } while(!node.isChildOfRootNode() && artifact.isOptional());

            artifact.setDependencyTrail(node.getDependencyTrail());
            set.add(node);
         }
      }

      ArtifactResolutionResult result = new ArtifactResolutionResult();
      result.setArtifactResolutionNodes(set);
      return result;
   }

   private ManagedVersionMap getManagedVersionsMap(Artifact originatingArtifact, Map managedVersions) {
      ManagedVersionMap versionMap;
      if (managedVersions != null && managedVersions instanceof ManagedVersionMap) {
         versionMap = (ManagedVersionMap)managedVersions;
      } else {
         versionMap = new ManagedVersionMap(managedVersions);
      }

      Artifact managedOriginatingArtifact = (Artifact)versionMap.get(originatingArtifact.getDependencyConflictId());
      if (managedOriginatingArtifact != null) {
         if (managedVersions instanceof ManagedVersionMap) {
            versionMap = new ManagedVersionMap(managedVersions);
         }

         versionMap.remove(originatingArtifact.getDependencyConflictId());
      }

      return versionMap;
   }

   private void recurse(Artifact originatingArtifact, ResolutionNode node, Map resolvedArtifacts, ManagedVersionMap managedVersions, ArtifactRepository localRepository, List remoteRepositories, ArtifactMetadataSource source, ArtifactFilter filter, List listeners) throws CyclicDependencyException, ArtifactResolutionException, OverConstrainedVersionException {
      this.fireEvent(1, listeners, node);
      Object key = node.getKey();
      if (managedVersions.containsKey(key)) {
         this.manageArtifact(node, managedVersions, listeners);
      }

      List previousNodes = (List)resolvedArtifacts.get(key);
      if (previousNodes != null) {
         Iterator i = ((List)previousNodes).iterator();

         label168:
         while(true) {
            ResolutionNode previous;
            do {
               if (!i.hasNext()) {
                  break label168;
               }

               previous = (ResolutionNode)i.next();
            } while(!previous.isActive());

            VersionRange previousRange = previous.getArtifact().getVersionRange();
            VersionRange currentRange = node.getArtifact().getVersionRange();
            if (previousRange != null && currentRange != null) {
               VersionRange newRange = previousRange.restrict(currentRange);
               if (newRange.isSelectedVersionKnown(previous.getArtifact())) {
                  this.fireEvent(11, listeners, node, previous.getArtifact(), newRange);
               }

               previous.getArtifact().setVersionRange(newRange);
               node.getArtifact().setVersionRange(currentRange.restrict(previousRange));
               ResolutionNode[] resetNodes = new ResolutionNode[]{previous, node};

               for(int j = 0; j < 2; ++j) {
                  Artifact resetArtifact = resetNodes[j].getArtifact();
                  if (resetArtifact.getVersion() == null && resetArtifact.getVersionRange() != null) {
                     List versions = resetArtifact.getAvailableVersions();
                     if (versions == null) {
                        try {
                           versions = source.retrieveAvailableVersions(resetArtifact, localRepository, remoteRepositories);
                           resetArtifact.setAvailableVersions(versions);
                        } catch (ArtifactMetadataRetrievalException var22) {
                           resetArtifact.setDependencyTrail(node.getDependencyTrail());
                           throw new ArtifactResolutionException("Unable to get dependency information: " + var22.getMessage(), resetArtifact, remoteRepositories, var22);
                        }
                     }

                     ArtifactVersion selectedVersion = resetArtifact.getVersionRange().matchVersion(resetArtifact.getAvailableVersions());
                     if (selectedVersion == null) {
                        throw new OverConstrainedVersionException(" Unable to find a version in " + resetArtifact.getAvailableVersions() + " to match the range " + resetArtifact.getVersionRange(), resetArtifact);
                     }

                     resetArtifact.selectVersion(selectedVersion.toString());
                     this.fireEvent(10, listeners, resetNodes[j]);
                  }
               }
            }

            ResolutionNode nearest;
            ResolutionNode farthest;
            if (previous.getDepth() <= node.getDepth()) {
               nearest = previous;
               farthest = node;
            } else {
               nearest = node;
               farthest = previous;
            }

            if (this.checkScopeUpdate(farthest, nearest, listeners)) {
               nearest.disable();
               farthest.getArtifact().setVersion(nearest.getArtifact().getVersion());
               this.fireEvent(5, listeners, nearest, farthest.getArtifact());
            } else {
               farthest.disable();
               this.fireEvent(5, listeners, farthest, nearest.getArtifact());
            }
         }
      } else {
         previousNodes = new ArrayList();
         resolvedArtifacts.put(key, previousNodes);
      }

      ((List)previousNodes).add(node);
      if (node.isActive()) {
         this.fireEvent(4, listeners, node);
      }

      if (node.isActive() && !"system".equals(node.getArtifact().getScope())) {
         this.fireEvent(2, listeners, node);
         Artifact parentArtifact = node.getArtifact();
         Iterator i = node.getChildrenIterator();

         while(true) {
            ResolutionNode child;
            List childRemoteRepositories;
            while(true) {
               do {
                  do {
                     if (!i.hasNext()) {
                        this.fireEvent(3, listeners, node);
                        return;
                     }

                     child = (ResolutionNode)i.next();
                  } while(child.isResolved());
               } while(child.getArtifact().isOptional() && !child.isChildOfRootNode());

               Artifact artifact = child.getArtifact();
               artifact.setDependencyTrail(node.getDependencyTrail());
               childRemoteRepositories = child.getRemoteRepositories();

               try {
                  Object childKey;
                  do {
                     childKey = child.getKey();
                     Artifact relocated;
                     if (managedVersions.containsKey(childKey)) {
                        this.manageArtifact(child, managedVersions, listeners);
                        relocated = (Artifact)managedVersions.get(childKey);
                        ArtifactFilter managedExclusionFilter = relocated.getDependencyFilter();
                        if (null != managedExclusionFilter) {
                           if (null != artifact.getDependencyFilter()) {
                              AndArtifactFilter aaf = new AndArtifactFilter();
                              aaf.add(artifact.getDependencyFilter());
                              aaf.add(managedExclusionFilter);
                              artifact.setDependencyFilter(aaf);
                           } else {
                              artifact.setDependencyFilter(managedExclusionFilter);
                           }
                        }
                     }

                     if (artifact.getVersion() == null) {
                        ArtifactVersion version;
                        if (artifact.isSelectedVersionKnown()) {
                           version = artifact.getSelectedVersion();
                        } else {
                           List versions = artifact.getAvailableVersions();
                           if (versions == null) {
                              versions = source.retrieveAvailableVersions(artifact, localRepository, childRemoteRepositories);
                              artifact.setAvailableVersions(versions);
                           }

                           Collections.sort(versions);
                           VersionRange versionRange = artifact.getVersionRange();
                           version = versionRange.matchVersion(versions);
                           if (version == null) {
                              if (versions.isEmpty()) {
                                 throw new OverConstrainedVersionException("No versions are present in the repository for the artifact with a range " + versionRange, artifact, childRemoteRepositories);
                              }

                              throw new OverConstrainedVersionException("Couldn't find a version in " + versions + " to match range " + versionRange, artifact, childRemoteRepositories);
                           }
                        }

                        artifact.selectVersion(version.toString());
                        this.fireEvent(10, listeners, child);
                     }

                     relocated = source.retrieveRelocatedArtifact(artifact, localRepository, childRemoteRepositories);
                     if (relocated != null && !artifact.equals(relocated)) {
                        relocated.setDependencyFilter(artifact.getDependencyFilter());
                        artifact = relocated;
                        child.setArtifact(relocated);
                     }
                  } while(!childKey.equals(child.getKey()));

                  if (parentArtifact == null || parentArtifact.getDependencyFilter() == null || parentArtifact.getDependencyFilter().include(artifact)) {
                     ResolutionGroup rGroup = source.retrieve(artifact, localRepository, childRemoteRepositories);
                     if (rGroup != null) {
                        child.addDependencies(rGroup.getArtifacts(), rGroup.getResolutionRepositories(), filter);
                        break;
                     }
                  }
               } catch (CyclicDependencyException var23) {
                  this.fireEvent(8, listeners, new ResolutionNode(var23.getArtifact(), childRemoteRepositories, child));
                  break;
               } catch (ArtifactMetadataRetrievalException var24) {
                  artifact.setDependencyTrail(node.getDependencyTrail());
                  throw new ArtifactResolutionException("Unable to get dependency information: " + var24.getMessage(), artifact, childRemoteRepositories, var24);
               }
            }

            this.recurse(originatingArtifact, child, resolvedArtifacts, managedVersions, localRepository, childRemoteRepositories, source, filter, listeners);
         }
      }
   }

   private void manageArtifact(ResolutionNode node, ManagedVersionMap managedVersions, List listeners) {
      Artifact artifact = (Artifact)managedVersions.get(node.getKey());
      if (artifact.getVersion() != null && (!node.isChildOfRootNode() || node.getArtifact().getVersion() == null)) {
         this.fireEvent(12, listeners, node, artifact);
         node.getArtifact().setVersion(artifact.getVersion());
      }

      if (artifact.getScope() != null && (!node.isChildOfRootNode() || node.getArtifact().getScope() == null)) {
         this.fireEvent(13, listeners, node, artifact);
         node.getArtifact().setScope(artifact.getScope());
      }

   }

   boolean checkScopeUpdate(ResolutionNode farthest, ResolutionNode nearest, List listeners) {
      boolean updateScope = false;
      Artifact farthestArtifact = farthest.getArtifact();
      Artifact nearestArtifact = nearest.getArtifact();
      if ("runtime".equals(farthestArtifact.getScope()) && ("test".equals(nearestArtifact.getScope()) || "provided".equals(nearestArtifact.getScope()))) {
         updateScope = true;
      }

      if ("compile".equals(farthestArtifact.getScope()) && !"compile".equals(nearestArtifact.getScope())) {
         updateScope = true;
      }

      if (nearest.getDepth() < 2 && updateScope) {
         updateScope = false;
         this.fireEvent(9, listeners, nearest, farthestArtifact);
      }

      if (updateScope) {
         this.fireEvent(6, listeners, nearest, farthestArtifact);
         nearestArtifact.setScope(farthestArtifact.getScope());
      }

      return updateScope;
   }

   private void fireEvent(int event, List listeners, ResolutionNode node) {
      this.fireEvent(event, listeners, node, (Artifact)null);
   }

   private void fireEvent(int event, List listeners, ResolutionNode node, Artifact replacement) {
      this.fireEvent(event, listeners, node, replacement, (VersionRange)null);
   }

   private void fireEvent(int event, List listeners, ResolutionNode node, Artifact replacement, VersionRange newRange) {
      Iterator i = listeners.iterator();

      while(i.hasNext()) {
         ResolutionListener listener = (ResolutionListener)i.next();
         ResolutionListenerForDepMgmt asImpl;
         switch(event) {
         case 1:
            listener.testArtifact(node.getArtifact());
            break;
         case 2:
            listener.startProcessChildren(node.getArtifact());
            break;
         case 3:
            listener.endProcessChildren(node.getArtifact());
            break;
         case 4:
            listener.includeArtifact(node.getArtifact());
            break;
         case 5:
            listener.omitForNearer(node.getArtifact(), replacement);
            break;
         case 6:
            listener.updateScope(node.getArtifact(), replacement.getScope());
            break;
         case 7:
         default:
            throw new IllegalStateException("Unknown event: " + event);
         case 8:
            listener.omitForCycle(node.getArtifact());
            break;
         case 9:
            listener.updateScopeCurrentPom(node.getArtifact(), replacement.getScope());
            break;
         case 10:
            listener.selectVersionFromRange(node.getArtifact());
            break;
         case 11:
            if (node.getArtifact().getVersionRange().hasRestrictions() || replacement.getVersionRange().hasRestrictions()) {
               listener.restrictRange(node.getArtifact(), replacement, newRange);
            }
            break;
         case 12:
            if (listener instanceof ResolutionListenerForDepMgmt) {
               asImpl = (ResolutionListenerForDepMgmt)listener;
               asImpl.manageArtifactVersion(node.getArtifact(), replacement);
            } else {
               listener.manageArtifact(node.getArtifact(), replacement);
            }
            break;
         case 13:
            if (listener instanceof ResolutionListenerForDepMgmt) {
               asImpl = (ResolutionListenerForDepMgmt)listener;
               asImpl.manageArtifactScope(node.getArtifact(), replacement);
            } else {
               listener.manageArtifact(node.getArtifact(), replacement);
            }
         }
      }

   }
}
