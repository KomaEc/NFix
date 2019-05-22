package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class ABCOptions {
   private Map<String, String> options;

   public ABCOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean with_all() {
      return PhaseOptions.getBoolean(this.options, "with-all");
   }

   public boolean with_cse() {
      return PhaseOptions.getBoolean(this.options, "with-cse");
   }

   public boolean with_arrayref() {
      return PhaseOptions.getBoolean(this.options, "with-arrayref");
   }

   public boolean with_fieldref() {
      return PhaseOptions.getBoolean(this.options, "with-fieldref");
   }

   public boolean with_classfield() {
      return PhaseOptions.getBoolean(this.options, "with-classfield");
   }

   public boolean with_rectarray() {
      return PhaseOptions.getBoolean(this.options, "with-rectarray");
   }

   public boolean profiling() {
      return PhaseOptions.getBoolean(this.options, "profiling");
   }

   public boolean add_color_tags() {
      return PhaseOptions.getBoolean(this.options, "add-color-tags");
   }
}
