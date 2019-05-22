package org.apache.maven.scm.provider.accurev;

public enum AccuRevCapability {
   DIFF_BETWEEN_STREAMS("4.7.2", (String)null),
   POPULATE_TO_TRANSACTION("4.9.0", "4.9.9"),
   STAT_ADDED_NOT_PROMOTED_BUG("4.9.0", "4.9.9");

   public static final String DEFAULT_VERSION_FOR_TESTS = "4.9.0";
   private String fromVersion;
   private String toVersion;

   private AccuRevCapability(String fromVersion, String toVersion) {
      this.fromVersion = fromVersion;
      this.toVersion = toVersion;
   }

   public boolean isSupported(String version) {
      return (this.fromVersion == null || this.fromVersion.compareTo(version) <= 0) && (this.toVersion == null || this.toVersion.compareTo(version) >= 0);
   }
}
