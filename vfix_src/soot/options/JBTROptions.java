package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class JBTROptions {
   private Map<String, String> options;

   public JBTROptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean use_older_type_assigner() {
      return PhaseOptions.getBoolean(this.options, "use-older-type-assigner");
   }

   public boolean compare_type_assigners() {
      return PhaseOptions.getBoolean(this.options, "compare-type-assigners");
   }

   public boolean ignore_nullpointer_dereferences() {
      return PhaseOptions.getBoolean(this.options, "ignore-nullpointer-dereferences");
   }
}
