package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class CGOptions {
   private Map<String, String> options;
   public static final int library_disabled = 1;
   public static final int library_any_subtype = 2;
   public static final int library_signature_resolution = 3;

   public CGOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean safe_forname() {
      return PhaseOptions.getBoolean(this.options, "safe-forname");
   }

   public boolean safe_newinstance() {
      return PhaseOptions.getBoolean(this.options, "safe-newinstance");
   }

   public boolean verbose() {
      return PhaseOptions.getBoolean(this.options, "verbose");
   }

   public boolean all_reachable() {
      return PhaseOptions.getBoolean(this.options, "all-reachable");
   }

   public boolean implicit_entry() {
      return PhaseOptions.getBoolean(this.options, "implicit-entry");
   }

   public boolean trim_clinit() {
      return PhaseOptions.getBoolean(this.options, "trim-clinit");
   }

   public boolean types_for_invoke() {
      return PhaseOptions.getBoolean(this.options, "types-for-invoke");
   }

   public int jdkver() {
      return PhaseOptions.getInt(this.options, "jdkver");
   }

   public String reflection_log() {
      return PhaseOptions.getString(this.options, "reflection-log");
   }

   public String guards() {
      return PhaseOptions.getString(this.options, "guards");
   }

   public int library() {
      String s = PhaseOptions.getString(this.options, "library");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("disabled")) {
            return 1;
         } else if (s.equalsIgnoreCase("any-subtype")) {
            return 2;
         } else if (s.equalsIgnoreCase("signature-resolution")) {
            return 3;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option library", s));
         }
      } else {
         return 1;
      }
   }
}
