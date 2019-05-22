package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class NPCOptions {
   private Map<String, String> options;

   public NPCOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean only_array_ref() {
      return PhaseOptions.getBoolean(this.options, "only-array-ref");
   }

   public boolean profiling() {
      return PhaseOptions.getBoolean(this.options, "profiling");
   }
}
