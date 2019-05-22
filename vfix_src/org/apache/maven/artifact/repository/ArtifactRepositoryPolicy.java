package org.apache.maven.artifact.repository;

import java.util.Calendar;
import java.util.Date;

public class ArtifactRepositoryPolicy {
   public static final String UPDATE_POLICY_NEVER = "never";
   public static final String UPDATE_POLICY_ALWAYS = "always";
   public static final String UPDATE_POLICY_DAILY = "daily";
   public static final String UPDATE_POLICY_INTERVAL = "interval";
   public static final String CHECKSUM_POLICY_FAIL = "fail";
   public static final String CHECKSUM_POLICY_WARN = "warn";
   public static final String CHECKSUM_POLICY_IGNORE = "ignore";
   private boolean enabled;
   private String updatePolicy;
   private String checksumPolicy;

   public ArtifactRepositoryPolicy() {
      this(true, (String)null, (String)null);
   }

   public ArtifactRepositoryPolicy(boolean enabled, String updatePolicy, String checksumPolicy) {
      this.enabled = enabled;
      if (updatePolicy == null) {
         updatePolicy = "daily";
      }

      this.updatePolicy = updatePolicy;
      if (checksumPolicy == null) {
         checksumPolicy = "warn";
      }

      this.checksumPolicy = checksumPolicy;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public void setUpdatePolicy(String updatePolicy) {
      this.updatePolicy = updatePolicy;
   }

   public void setChecksumPolicy(String checksumPolicy) {
      this.checksumPolicy = checksumPolicy;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public String getUpdatePolicy() {
      return this.updatePolicy;
   }

   public String getChecksumPolicy() {
      return this.checksumPolicy;
   }

   public boolean checkOutOfDate(Date lastModified) {
      boolean checkForUpdates = false;
      if ("always".equals(this.updatePolicy)) {
         checkForUpdates = true;
      } else if ("daily".equals(this.updatePolicy)) {
         Calendar cal = Calendar.getInstance();
         cal.set(11, 0);
         cal.set(12, 0);
         cal.set(13, 0);
         cal.set(14, 0);
         if (cal.getTime().after(lastModified)) {
            checkForUpdates = true;
         }
      } else if (this.updatePolicy.startsWith("interval")) {
         String s = this.updatePolicy.substring("interval".length() + 1);
         int minutes = Integer.valueOf(s);
         Calendar cal = Calendar.getInstance();
         cal.add(12, -minutes);
         if (cal.getTime().after(lastModified)) {
            checkForUpdates = true;
         }
      }

      return checkForUpdates;
   }
}
