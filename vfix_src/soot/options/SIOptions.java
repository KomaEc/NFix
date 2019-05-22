package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class SIOptions {
   private Map<String, String> options;
   public static final int allowed_modifier_changes_unsafe = 1;
   public static final int allowed_modifier_changes_safe = 2;
   public static final int allowed_modifier_changes_none = 3;

   public SIOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean rerun_jb() {
      return PhaseOptions.getBoolean(this.options, "rerun-jb");
   }

   public boolean insert_null_checks() {
      return PhaseOptions.getBoolean(this.options, "insert-null-checks");
   }

   public boolean insert_redundant_casts() {
      return PhaseOptions.getBoolean(this.options, "insert-redundant-casts");
   }

   public int max_container_size() {
      return PhaseOptions.getInt(this.options, "max-container-size");
   }

   public int max_inlinee_size() {
      return PhaseOptions.getInt(this.options, "max-inlinee-size");
   }

   public float expansion_factor() {
      return PhaseOptions.getFloat(this.options, "expansion-factor");
   }

   public int allowed_modifier_changes() {
      String s = PhaseOptions.getString(this.options, "allowed-modifier-changes");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("unsafe")) {
            return 1;
         } else if (s.equalsIgnoreCase("safe")) {
            return 2;
         } else if (s.equalsIgnoreCase("none")) {
            return 3;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option allowed-modifier-changes", s));
         }
      } else {
         return 1;
      }
   }
}
