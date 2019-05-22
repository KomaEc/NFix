package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class ShimpleOptions {
   private Map<String, String> options;

   public ShimpleOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean node_elim_opt() {
      return PhaseOptions.getBoolean(this.options, "node-elim-opt");
   }

   public boolean standard_local_names() {
      return PhaseOptions.getBoolean(this.options, "standard-local-names");
   }

   public boolean extended() {
      return PhaseOptions.getBoolean(this.options, "extended");
   }

   public boolean debug() {
      return PhaseOptions.getBoolean(this.options, "debug");
   }
}
