package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class CHAOptions {
   private Map<String, String> options;

   public CHAOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean verbose() {
      return PhaseOptions.getBoolean(this.options, "verbose");
   }

   public boolean apponly() {
      return PhaseOptions.getBoolean(this.options, "apponly");
   }
}
