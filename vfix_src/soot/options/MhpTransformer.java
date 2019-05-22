package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class MhpTransformer {
   private Map<String, String> options;

   public MhpTransformer(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }
}
