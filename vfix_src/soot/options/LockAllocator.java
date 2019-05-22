package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class LockAllocator {
   private Map<String, String> options;
   public static final int locking_scheme_medium_grained = 1;
   public static final int locking_scheme_coarse_grained = 2;
   public static final int locking_scheme_single_static = 3;
   public static final int locking_scheme_leave_original = 4;

   public LockAllocator(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean avoid_deadlock() {
      return PhaseOptions.getBoolean(this.options, "avoid-deadlock");
   }

   public boolean open_nesting() {
      return PhaseOptions.getBoolean(this.options, "open-nesting");
   }

   public boolean do_mhp() {
      return PhaseOptions.getBoolean(this.options, "do-mhp");
   }

   public boolean do_tlo() {
      return PhaseOptions.getBoolean(this.options, "do-tlo");
   }

   public boolean print_graph() {
      return PhaseOptions.getBoolean(this.options, "print-graph");
   }

   public boolean print_table() {
      return PhaseOptions.getBoolean(this.options, "print-table");
   }

   public boolean print_debug() {
      return PhaseOptions.getBoolean(this.options, "print-debug");
   }

   public int locking_scheme() {
      String s = PhaseOptions.getString(this.options, "locking-scheme");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("medium-grained")) {
            return 1;
         } else if (s.equalsIgnoreCase("coarse-grained")) {
            return 2;
         } else if (s.equalsIgnoreCase("single-static")) {
            return 3;
         } else if (s.equalsIgnoreCase("leave-original")) {
            return 4;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option locking-scheme", s));
         }
      } else {
         return 1;
      }
   }
}
