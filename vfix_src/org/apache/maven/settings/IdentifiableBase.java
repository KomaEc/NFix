package org.apache.maven.settings;

import java.io.Serializable;

public class IdentifiableBase extends TrackableBase implements Serializable {
   private String id = "default";

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }
}
