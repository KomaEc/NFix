package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class PurityOptions {
   private Map<String, String> options;

   public PurityOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean dump_summaries() {
      return PhaseOptions.getBoolean(this.options, "dump-summaries");
   }

   public boolean dump_cg() {
      return PhaseOptions.getBoolean(this.options, "dump-cg");
   }

   public boolean dump_intra() {
      return PhaseOptions.getBoolean(this.options, "dump-intra");
   }

   public boolean print() {
      return PhaseOptions.getBoolean(this.options, "print");
   }

   public boolean annotate() {
      return PhaseOptions.getBoolean(this.options, "annotate");
   }

   public boolean verbose() {
      return PhaseOptions.getBoolean(this.options, "verbose");
   }
}
