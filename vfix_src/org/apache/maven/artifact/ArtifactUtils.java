package org.apache.maven.artifact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.apache.maven.artifact.versioning.VersionRange;

public final class ArtifactUtils {
   private ArtifactUtils() {
   }

   public static boolean isSnapshot(String version) {
      return version != null && (version.toUpperCase().endsWith("SNAPSHOT") || Artifact.VERSION_FILE_PATTERN.matcher(version).matches());
   }

   public static String toSnapshotVersion(String version) {
      Matcher m = Artifact.VERSION_FILE_PATTERN.matcher(version);
      return m.matches() ? m.group(1) + '-' + "SNAPSHOT" : version;
   }

   public static String versionlessKey(Artifact artifact) {
      return versionlessKey(artifact.getGroupId(), artifact.getArtifactId());
   }

   public static String versionlessKey(String groupId, String artifactId) {
      if (groupId == null) {
         throw new NullPointerException("groupId was null");
      } else if (artifactId == null) {
         throw new NullPointerException("artifactId was null");
      } else {
         return groupId + ":" + artifactId;
      }
   }

   public static String artifactId(String groupId, String artifactId, String type, String version) {
      return artifactId(groupId, artifactId, type, (String)null, version);
   }

   public static String artifactId(String groupId, String artifactId, String type, String classifier, String baseVersion) {
      return groupId + ":" + artifactId + ":" + type + (classifier != null ? ":" + classifier : "") + ":" + baseVersion;
   }

   public static Map artifactMapByVersionlessId(Collection artifacts) {
      Map artifactMap = new LinkedHashMap();
      if (artifacts != null) {
         Iterator it = artifacts.iterator();

         while(it.hasNext()) {
            Artifact artifact = (Artifact)it.next();
            artifactMap.put(versionlessKey(artifact), artifact);
         }
      }

      return artifactMap;
   }

   public static Map artifactMapByArtifactId(Collection artifacts) {
      Map artifactMap = new LinkedHashMap();
      if (artifacts != null) {
         Iterator it = artifacts.iterator();

         while(it.hasNext()) {
            Artifact artifact = (Artifact)it.next();
            artifactMap.put(artifact.getId(), artifact);
         }
      }

      return artifactMap;
   }

   public static Artifact copyArtifact(Artifact artifact) {
      VersionRange range = artifact.getVersionRange();
      DefaultArtifact clone = new DefaultArtifact(artifact.getGroupId(), artifact.getArtifactId(), range.cloneOf(), artifact.getScope(), artifact.getType(), artifact.getClassifier(), artifact.getArtifactHandler(), artifact.isOptional());
      clone.setRelease(artifact.isRelease());
      clone.setResolvedVersion(artifact.getVersion());
      clone.setResolved(artifact.isResolved());
      clone.setFile(artifact.getFile());
      clone.setAvailableVersions(copyList(artifact.getAvailableVersions()));
      clone.setBaseVersion(artifact.getBaseVersion());
      clone.setDependencyFilter(artifact.getDependencyFilter());
      clone.setDependencyTrail(copyList(artifact.getDependencyTrail()));
      clone.setDownloadUrl(artifact.getDownloadUrl());
      clone.setRepository(artifact.getRepository());
      return clone;
   }

   private static List copyList(List original) {
      List copy = null;
      if (original != null) {
         copy = new ArrayList();
         if (!original.isEmpty()) {
            copy.addAll(original);
         }
      }

      return copy;
   }
}
