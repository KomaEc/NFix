package org.apache.maven.artifact.repository.metadata;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Versioning implements Serializable {
   private String latest;
   private String release;
   private Snapshot snapshot;
   private List<String> versions;
   private String lastUpdated;

   public void addVersion(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Versioning.addVersions(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getVersions().add(string);
      }
   }

   public String getLastUpdated() {
      return this.lastUpdated;
   }

   public String getLatest() {
      return this.latest;
   }

   public String getRelease() {
      return this.release;
   }

   public Snapshot getSnapshot() {
      return this.snapshot;
   }

   public List<String> getVersions() {
      if (this.versions == null) {
         this.versions = new ArrayList();
      }

      return this.versions;
   }

   public void removeVersion(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("Versioning.removeVersions(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getVersions().remove(string);
      }
   }

   public void setLastUpdated(String lastUpdated) {
      this.lastUpdated = lastUpdated;
   }

   public void setLatest(String latest) {
      this.latest = latest;
   }

   public void setRelease(String release) {
      this.release = release;
   }

   public void setSnapshot(Snapshot snapshot) {
      this.snapshot = snapshot;
   }

   public void setVersions(List<String> versions) {
      this.versions = versions;
   }

   public void updateTimestamp() {
      this.setLastUpdatedTimestamp(new Date());
   }

   public void setLastUpdatedTimestamp(Date date) {
      TimeZone timezone = TimeZone.getTimeZone("UTC");
      DateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
      fmt.setTimeZone(timezone);
      this.setLastUpdated(fmt.format(date));
   }
}
