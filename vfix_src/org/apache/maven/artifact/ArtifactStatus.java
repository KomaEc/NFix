package org.apache.maven.artifact;

import java.util.HashMap;
import java.util.Map;

public final class ArtifactStatus implements Comparable {
   public static final ArtifactStatus NONE = new ArtifactStatus("none", 0);
   public static final ArtifactStatus GENERATED = new ArtifactStatus("generated", 1);
   public static final ArtifactStatus CONVERTED = new ArtifactStatus("converted", 2);
   public static final ArtifactStatus PARTNER = new ArtifactStatus("partner", 3);
   public static final ArtifactStatus DEPLOYED = new ArtifactStatus("deployed", 4);
   public static final ArtifactStatus VERIFIED = new ArtifactStatus("verified", 5);
   private final int rank;
   private final String key;
   private static Map map;

   private ArtifactStatus(String key, int rank) {
      this.rank = rank;
      this.key = key;
      if (map == null) {
         map = new HashMap();
      }

      map.put(key, this);
   }

   public static ArtifactStatus valueOf(String status) {
      ArtifactStatus retVal = null;
      if (status != null) {
         retVal = (ArtifactStatus)map.get(status);
      }

      return retVal != null ? retVal : NONE;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ArtifactStatus that = (ArtifactStatus)o;
         return this.rank == that.rank;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.rank;
   }

   public String toString() {
      return this.key;
   }

   public int compareTo(Object o) {
      ArtifactStatus s = (ArtifactStatus)o;
      return this.rank - s.rank;
   }
}
