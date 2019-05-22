package org.apache.maven.settings;

import java.io.Serializable;

public class TrackableBase implements Serializable {
   public static final String USER_LEVEL = "user-level";
   public static final String GLOBAL_LEVEL = "global-level";
   private String sourceLevel = "user-level";
   private boolean sourceLevelSet = false;

   public void setSourceLevel(String sourceLevel) {
      if (this.sourceLevelSet) {
         throw new IllegalStateException("Cannot reset sourceLevel attribute; it is already set to: " + sourceLevel);
      } else if (!"user-level".equals(sourceLevel) && !"global-level".equals(sourceLevel)) {
         throw new IllegalArgumentException("sourceLevel must be one of: {user-level,global-level}");
      } else {
         this.sourceLevel = sourceLevel;
         this.sourceLevelSet = true;
      }
   }

   public String getSourceLevel() {
      return this.sourceLevel;
   }
}
