package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class CPOptions {
   private Map<String, String> options;

   public CPOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean only_regular_locals() {
      return PhaseOptions.getBoolean(this.options, "only-regular-locals");
   }

   public boolean only_stack_locals() {
      return PhaseOptions.getBoolean(this.options, "only-stack-locals");
   }
}
