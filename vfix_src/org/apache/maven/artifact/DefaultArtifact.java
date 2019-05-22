package org.apache.maven.artifact;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.codehaus.plexus.util.StringUtils;

public class DefaultArtifact implements Artifact {
   private String groupId;
   private String artifactId;
   private String baseVersion;
   private final String type;
   private final String classifier;
   private String scope;
   private File file;
   private ArtifactRepository repository;
   private String downloadUrl;
   private ArtifactFilter dependencyFilter;
   private ArtifactHandler artifactHandler;
   private List<String> dependencyTrail;
   private String version;
   private VersionRange versionRange;
   private boolean resolved;
   private boolean release;
   private List<ArtifactVersion> availableVersions;
   private Map<Object, ArtifactMetadata> metadataMap;
   private boolean optional;

   public DefaultArtifact(String groupId, String artifactId, VersionRange versionRange, String scope, String type, String classifier, ArtifactHandler artifactHandler) {
      this(groupId, artifactId, versionRange, scope, type, classifier, artifactHandler, false);
   }

   public DefaultArtifact(String groupId, String artifactId, VersionRange versionRange, String scope, String type, String classifier, ArtifactHandler artifactHandler, boolean optional) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.versionRange = versionRange;
      this.selectVersionFromNewRangeIfAvailable();
      this.artifactHandler = artifactHandler;
      this.scope = scope;
      this.type = type;
      if (classifier == null) {
         classifier = artifactHandler.getClassifier();
      }

      this.classifier = classifier;
      this.optional = optional;
      this.validateIdentity();
   }

   private void validateIdentity() {
      if (this.empty(this.groupId)) {
         throw new InvalidArtifactRTException(this.groupId, this.artifactId, this.getVersion(), this.type, "The groupId cannot be empty.");
      } else if (this.artifactId == null) {
         throw new InvalidArtifactRTException(this.groupId, this.artifactId, this.getVersion(), this.type, "The artifactId cannot be empty.");
      } else if (this.type == null) {
         throw new InvalidArtifactRTException(this.groupId, this.artifactId, this.getVersion(), this.type, "The type cannot be empty.");
      } else if (this.version == null && this.versionRange == null) {
         throw new InvalidArtifactRTException(this.groupId, this.artifactId, this.getVersion(), this.type, "The version cannot be empty.");
      }
   }

   private boolean empty(String value) {
      return value == null || value.trim().length() < 1;
   }

   public String getClassifier() {
      return this.classifier;
   }

   public boolean hasClassifier() {
      return StringUtils.isNotEmpty(this.classifier);
   }

   public String getScope() {
      return this.scope;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
      this.setBaseVersionInternal(version);
      this.versionRange = null;
   }

   public String getType() {
      return this.type;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }

   public ArtifactRepository getRepository() {
      return this.repository;
   }

   public void setRepository(ArtifactRepository repository) {
      this.repository = repository;
   }

   public String getId() {
      return this.getDependencyConflictId() + ":" + this.getBaseVersion();
   }

   public String getDependencyConflictId() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.getGroupId());
      sb.append(":");
      this.appendArtifactTypeClassifierString(sb);
      return sb.toString();
   }

   private void appendArtifactTypeClassifierString(StringBuffer sb) {
      sb.append(this.getArtifactId());
      sb.append(":");
      sb.append(this.getType());
      if (this.hasClassifier()) {
         sb.append(":");
         sb.append(this.getClassifier());
      }

   }

   public void addMetadata(ArtifactMetadata metadata) {
      if (this.metadataMap == null) {
         this.metadataMap = new HashMap();
      }

      ArtifactMetadata m = (ArtifactMetadata)this.metadataMap.get(metadata.getKey());
      if (m != null) {
         m.merge(metadata);
      } else {
         this.metadataMap.put(metadata.getKey(), metadata);
      }

   }

   public ArtifactMetadata getMetadata(Class<?> metadataClass) {
      Collection<ArtifactMetadata> metadata = this.getMetadataList();
      if (metadata != null) {
         Iterator i$ = metadata.iterator();

         while(i$.hasNext()) {
            ArtifactMetadata m = (ArtifactMetadata)i$.next();
            if (metadataClass.isAssignableFrom(m.getClass())) {
               return m;
            }
         }
      }

      return null;
   }

   public Collection<ArtifactMetadata> getMetadataList() {
      Object result;
      if (this.metadataMap == null) {
         result = Collections.emptyList();
      } else {
         result = this.metadataMap.values();
      }

      return (Collection)result;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      if (this.getGroupId() != null) {
         sb.append(this.getGroupId());
         sb.append(":");
      }

      this.appendArtifactTypeClassifierString(sb);
      sb.append(":");
      if (this.getBaseVersionInternal() != null) {
         sb.append(this.getBaseVersionInternal());
      } else {
         sb.append(this.versionRange.toString());
      }

      if (this.scope != null) {
         sb.append(":");
         sb.append(this.scope);
      }

      return sb.toString();
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + this.groupId.hashCode();
      result = 37 * result + this.artifactId.hashCode();
      result = 37 * result + this.type.hashCode();
      if (this.version != null) {
         result = 37 * result + this.version.hashCode();
      }

      result = 37 * result + (this.classifier != null ? this.classifier.hashCode() : 0);
      return result;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Artifact)) {
         return false;
      } else {
         Artifact a = (Artifact)o;
         if (!a.getGroupId().equals(this.groupId)) {
            return false;
         } else if (!a.getArtifactId().equals(this.artifactId)) {
            return false;
         } else if (!a.getVersion().equals(this.version)) {
            return false;
         } else if (!a.getType().equals(this.type)) {
            return false;
         } else {
            if (a.getClassifier() == null) {
               if (this.classifier != null) {
                  return false;
               }
            } else if (!a.getClassifier().equals(this.classifier)) {
               return false;
            }

            return true;
         }
      }
   }

   public String getBaseVersion() {
      if (this.baseVersion == null) {
         if (this.version == null) {
            throw new NullPointerException("version was null for " + this.groupId + ":" + this.artifactId);
         }

         this.setBaseVersionInternal(this.version);
      }

      return this.baseVersion;
   }

   protected String getBaseVersionInternal() {
      if (this.baseVersion == null && this.version != null) {
         this.setBaseVersionInternal(this.version);
      }

      return this.baseVersion;
   }

   public void setBaseVersion(String baseVersion) {
      this.setBaseVersionInternal(baseVersion);
   }

   protected void setBaseVersionInternal(String baseVersion) {
      Matcher m = VERSION_FILE_PATTERN.matcher(baseVersion);
      if (m.matches()) {
         this.baseVersion = m.group(1) + "-" + "SNAPSHOT";
      } else {
         this.baseVersion = baseVersion;
      }

   }

   public int compareTo(Artifact a) {
      int result = this.groupId.compareTo(a.getGroupId());
      if (result == 0) {
         result = this.artifactId.compareTo(a.getArtifactId());
         if (result == 0) {
            result = this.type.compareTo(a.getType());
            if (result == 0) {
               if (this.classifier == null) {
                  if (a.getClassifier() != null) {
                     result = 1;
                  }
               } else if (a.getClassifier() != null) {
                  result = this.classifier.compareTo(a.getClassifier());
               } else {
                  result = -1;
               }

               if (result == 0) {
                  result = this.version.compareTo(a.getVersion());
               }
            }
         }
      }

      return result;
   }

   public void updateVersion(String version, ArtifactRepository localRepository) {
      this.setResolvedVersion(version);
      this.setFile(new File(localRepository.getBasedir(), localRepository.pathOf(this)));
   }

   public String getDownloadUrl() {
      return this.downloadUrl;
   }

   public void setDownloadUrl(String downloadUrl) {
      this.downloadUrl = downloadUrl;
   }

   public ArtifactFilter getDependencyFilter() {
      return this.dependencyFilter;
   }

   public void setDependencyFilter(ArtifactFilter artifactFilter) {
      this.dependencyFilter = artifactFilter;
   }

   public ArtifactHandler getArtifactHandler() {
      return this.artifactHandler;
   }

   public List<String> getDependencyTrail() {
      return this.dependencyTrail;
   }

   public void setDependencyTrail(List<String> dependencyTrail) {
      this.dependencyTrail = dependencyTrail;
   }

   public void setScope(String scope) {
      this.scope = scope;
   }

   public VersionRange getVersionRange() {
      if (this.versionRange == null) {
         this.versionRange = VersionRange.createFromVersion(this.version);
      }

      return this.versionRange;
   }

   public void setVersionRange(VersionRange versionRange) {
      this.versionRange = versionRange;
      this.selectVersionFromNewRangeIfAvailable();
   }

   private void selectVersionFromNewRangeIfAvailable() {
      if (this.versionRange != null && this.versionRange.getRecommendedVersion() != null) {
         this.selectVersion(this.versionRange.getRecommendedVersion().toString());
      } else {
         this.version = null;
         this.baseVersion = null;
      }

   }

   public void selectVersion(String version) {
      this.version = version;
      this.setBaseVersionInternal(version);
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public boolean isSnapshot() {
      if (this.getBaseVersion() == null) {
         return false;
      } else {
         return this.getBaseVersion().endsWith("SNAPSHOT") || this.getBaseVersion().equals("LATEST");
      }
   }

   public void setResolved(boolean resolved) {
      this.resolved = resolved;
   }

   public boolean isResolved() {
      return this.resolved;
   }

   public void setResolvedVersion(String version) {
      this.version = version;
   }

   public void setArtifactHandler(ArtifactHandler artifactHandler) {
      this.artifactHandler = artifactHandler;
   }

   public void setRelease(boolean release) {
      this.release = release;
   }

   public boolean isRelease() {
      return this.release;
   }

   public List<ArtifactVersion> getAvailableVersions() {
      return this.availableVersions;
   }

   public void setAvailableVersions(List<ArtifactVersion> availableVersions) {
      this.availableVersions = availableVersions;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
      return this.versionRange.getSelectedVersion(this);
   }

   public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
      return this.versionRange.isSelectedVersionKnown(this);
   }

   public void setOptional(boolean optional) {
      this.optional = optional;
   }
}
