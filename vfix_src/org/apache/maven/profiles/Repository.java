package org.apache.maven.profiles;

import java.io.Serializable;

public class Repository extends RepositoryBase implements Serializable {
   private RepositoryPolicy releases;
   private RepositoryPolicy snapshots;

   public RepositoryPolicy getReleases() {
      return this.releases;
   }

   public RepositoryPolicy getSnapshots() {
      return this.snapshots;
   }

   public void setReleases(RepositoryPolicy releases) {
      this.releases = releases;
   }

   public void setSnapshots(RepositoryPolicy snapshots) {
      this.snapshots = snapshots;
   }

   public boolean equals(Object obj) {
      return super.equals(obj);
   }
}
