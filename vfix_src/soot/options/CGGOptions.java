package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class CGGOptions {
   private Map<String, String> options;

   public CGGOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean show_lib_meths() {
      return PhaseOptions.getBoolean(this.options, "show-lib-meths");
   }
}
