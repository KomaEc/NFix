package org.apache.maven.artifact.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;

public class ResolutionNode {
   private Artifact artifact;
   private List children;
   private final List parents;
   private final int depth;
   private final ResolutionNode parent;
   private final List remoteRepositories;
   private boolean active = true;
   private List trail;

   public ResolutionNode(Artifact artifact, List remoteRepositories) {
      this.artifact = artifact;
      this.remoteRepositories = remoteRepositories;
      this.depth = 0;
      this.parents = Collections.EMPTY_LIST;
      this.parent = null;
   }

   public ResolutionNode(Artifact artifact, List remoteRepositories, ResolutionNode parent) {
      this.artifact = artifact;
      this.remoteRepositories = remoteRepositories;
      this.depth = parent.depth + 1;
      this.parents = new ArrayList();
      this.parents.addAll(parent.parents);
      this.parents.add(parent.getKey());
      this.parent = parent;
   }

   public void setArtifact(Artifact artifact) {
      this.artifact = artifact;
   }

   public Artifact getArtifact() {
      return this.artifact;
   }

   public Object getKey() {
      return this.artifact.getDependencyConflictId();
   }

   public void addDependencies(Set artifacts, List remoteRepositories, ArtifactFilter filter) throws CyclicDependencyException, OverConstrainedVersionException {
      if (!artifacts.isEmpty()) {
         this.children = new ArrayList(artifacts.size());
         Iterator i = artifacts.iterator();

         while(i.hasNext()) {
            Artifact a = (Artifact)i.next();
            if (this.parents.contains(a.getDependencyConflictId())) {
               a.setDependencyTrail(this.getDependencyTrail());
               throw new CyclicDependencyException("A dependency has introduced a cycle", a);
            }

            this.children.add(new ResolutionNode(a, remoteRepositories, this));
         }
      } else {
         this.children = Collections.EMPTY_LIST;
      }

      this.trail = null;
   }

   public List getDependencyTrail() throws OverConstrainedVersionException {
      List trial = this.getTrail();
      List ret = new ArrayList(trial.size());
      Iterator i = trial.iterator();

      while(i.hasNext()) {
         Artifact artifact = (Artifact)i.next();
         ret.add(artifact.getId());
      }

      return ret;
   }

   private List getTrail() throws OverConstrainedVersionException {
      if (this.trail == null) {
         List ids = new LinkedList();

         for(ResolutionNode node = this; node != null; node = node.parent) {
            Artifact artifact = node.getArtifact();
            if (artifact.getVersion() == null) {
               ArtifactVersion selected = artifact.getSelectedVersion();
               if (selected == null) {
                  throw new OverConstrainedVersionException("Unable to get a selected Version for " + artifact.getArtifactId(), artifact);
               }

               artifact.selectVersion(selected.toString());
            }

            ids.add(0, artifact);
         }

         this.trail = ids;
      }

      return this.trail;
   }

   public boolean isResolved() {
      return this.children != null;
   }

   public boolean isChildOfRootNode() {
      return this.parent != null && this.parent.parent == null;
   }

   public Iterator getChildrenIterator() {
      return this.children.iterator();
   }

   public int getDepth() {
      return this.depth;
   }

   public List getRemoteRepositories() {
      return this.remoteRepositories;
   }

   public boolean isActive() {
      return this.active;
   }

   public void enable() {
      this.active = true;
      if (this.children != null) {
         Iterator i = this.children.iterator();

         while(i.hasNext()) {
            ResolutionNode node = (ResolutionNode)i.next();
            node.enable();
         }
      }

   }

   public void disable() {
      this.active = false;
      if (this.children != null) {
         Iterator i = this.children.iterator();

         while(i.hasNext()) {
            ResolutionNode node = (ResolutionNode)i.next();
            node.disable();
         }
      }

   }

   public boolean filterTrail(ArtifactFilter filter) throws OverConstrainedVersionException {
      boolean success = true;
      if (filter != null) {
         Iterator i = this.getTrail().iterator();

         while(i.hasNext() && success) {
            Artifact artifact = (Artifact)i.next();
            if (!filter.include(artifact)) {
               success = false;
            }
         }
      }

      return success;
   }

   public String toString() {
      return this.artifact.toString() + " (" + this.depth + "; " + (this.active ? "enabled" : "disabled") + ")";
   }
}
