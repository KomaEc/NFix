package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class PaddleOptions {
   private Map<String, String> options;
   public static final int conf_ofcg = 1;
   public static final int conf_cha = 2;
   public static final int conf_cha_aot = 3;
   public static final int conf_ofcg_aot = 4;
   public static final int conf_cha_context_aot = 5;
   public static final int conf_ofcg_context_aot = 6;
   public static final int conf_cha_context = 7;
   public static final int conf_ofcg_context = 8;
   public static final int q_auto = 1;
   public static final int q_trad = 2;
   public static final int q_bdd = 3;
   public static final int q_debug = 4;
   public static final int q_trace = 5;
   public static final int q_numtrace = 6;
   public static final int backend_auto = 1;
   public static final int backend_buddy = 2;
   public static final int backend_cudd = 3;
   public static final int backend_sable = 4;
   public static final int backend_javabdd = 5;
   public static final int backend_none = 6;
   public static final int context_insens = 1;
   public static final int context_1cfa = 2;
   public static final int context_kcfa = 3;
   public static final int context_objsens = 4;
   public static final int context_kobjsens = 5;
   public static final int context_uniqkobjsens = 6;
   public static final int context_threadkobjsens = 7;
   public static final int propagator_auto = 1;
   public static final int propagator_iter = 2;
   public static final int propagator_worklist = 3;
   public static final int propagator_alias = 4;
   public static final int propagator_bdd = 5;
   public static final int propagator_incbdd = 6;
   public static final int set_impl_hash = 1;
   public static final int set_impl_bit = 2;
   public static final int set_impl_hybrid = 3;
   public static final int set_impl_array = 4;
   public static final int set_impl_heintze = 5;
   public static final int set_impl_double = 6;
   public static final int double_set_old_hash = 1;
   public static final int double_set_old_bit = 2;
   public static final int double_set_old_hybrid = 3;
   public static final int double_set_old_array = 4;
   public static final int double_set_old_heintze = 5;
   public static final int double_set_new_hash = 1;
   public static final int double_set_new_bit = 2;
   public static final int double_set_new_hybrid = 3;
   public static final int double_set_new_array = 4;
   public static final int double_set_new_heintze = 5;

   public PaddleOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean verbose() {
      return PhaseOptions.getBoolean(this.options, "verbose");
   }

   public boolean bdd() {
      return PhaseOptions.getBoolean(this.options, "bdd");
   }

   public boolean dynamic_order() {
      return PhaseOptions.getBoolean(this.options, "dynamic-order");
   }

   public boolean profile() {
      return PhaseOptions.getBoolean(this.options, "profile");
   }

   public boolean verbosegc() {
      return PhaseOptions.getBoolean(this.options, "verbosegc");
   }

   public boolean ignore_types() {
      return PhaseOptions.getBoolean(this.options, "ignore-types");
   }

   public boolean pre_jimplify() {
      return PhaseOptions.getBoolean(this.options, "pre-jimplify");
   }

   public boolean context_heap() {
      return PhaseOptions.getBoolean(this.options, "context-heap");
   }

   public boolean rta() {
      return PhaseOptions.getBoolean(this.options, "rta");
   }

   public boolean field_based() {
      return PhaseOptions.getBoolean(this.options, "field-based");
   }

   public boolean types_for_sites() {
      return PhaseOptions.getBoolean(this.options, "types-for-sites");
   }

   public boolean merge_stringbuffer() {
      return PhaseOptions.getBoolean(this.options, "merge-stringbuffer");
   }

   public boolean string_constants() {
      return PhaseOptions.getBoolean(this.options, "string-constants");
   }

   public boolean simulate_natives() {
      return PhaseOptions.getBoolean(this.options, "simulate-natives");
   }

   public boolean global_nodes_in_natives() {
      return PhaseOptions.getBoolean(this.options, "global-nodes-in-natives");
   }

   public boolean simple_edges_bidirectional() {
      return PhaseOptions.getBoolean(this.options, "simple-edges-bidirectional");
   }

   public boolean this_edges() {
      return PhaseOptions.getBoolean(this.options, "this-edges");
   }

   public boolean precise_newinstance() {
      return PhaseOptions.getBoolean(this.options, "precise-newinstance");
   }

   public boolean context_counts() {
      return PhaseOptions.getBoolean(this.options, "context-counts");
   }

   public boolean total_context_counts() {
      return PhaseOptions.getBoolean(this.options, "total-context-counts");
   }

   public boolean method_context_counts() {
      return PhaseOptions.getBoolean(this.options, "method-context-counts");
   }

   public boolean set_mass() {
      return PhaseOptions.getBoolean(this.options, "set-mass");
   }

   public boolean number_nodes() {
      return PhaseOptions.getBoolean(this.options, "number-nodes");
   }

   public int order() {
      return PhaseOptions.getInt(this.options, "order");
   }

   public int bdd_nodes() {
      return PhaseOptions.getInt(this.options, "bdd-nodes");
   }

   public int k() {
      return PhaseOptions.getInt(this.options, "k");
   }

   public int conf() {
      String s = PhaseOptions.getString(this.options, "conf");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("ofcg")) {
            return 1;
         } else if (s.equalsIgnoreCase("cha")) {
            return 2;
         } else if (s.equalsIgnoreCase("cha-aot")) {
            return 3;
         } else if (s.equalsIgnoreCase("ofcg-aot")) {
            return 4;
         } else if (s.equalsIgnoreCase("cha-context-aot")) {
            return 5;
         } else if (s.equalsIgnoreCase("ofcg-context-aot")) {
            return 6;
         } else if (s.equalsIgnoreCase("cha-context")) {
            return 7;
         } else if (s.equalsIgnoreCase("ofcg-context")) {
            return 8;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option conf", s));
         }
      } else {
         return 1;
      }
   }

   public int q() {
      String s = PhaseOptions.getString(this.options, "q");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("auto")) {
            return 1;
         } else if (s.equalsIgnoreCase("trad")) {
            return 2;
         } else if (s.equalsIgnoreCase("bdd")) {
            return 3;
         } else if (s.equalsIgnoreCase("debug")) {
            return 4;
         } else if (s.equalsIgnoreCase("trace")) {
            return 5;
         } else if (s.equalsIgnoreCase("numtrace")) {
            return 6;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option q", s));
         }
      } else {
         return 1;
      }
   }

   public int backend() {
      String s = PhaseOptions.getString(this.options, "backend");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("auto")) {
            return 1;
         } else if (s.equalsIgnoreCase("buddy")) {
            return 2;
         } else if (s.equalsIgnoreCase("cudd")) {
            return 3;
         } else if (s.equalsIgnoreCase("sable")) {
            return 4;
         } else if (s.equalsIgnoreCase("javabdd")) {
            return 5;
         } else if (s.equalsIgnoreCase("none")) {
            return 6;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option backend", s));
         }
      } else {
         return 1;
      }
   }

   public int context() {
      String s = PhaseOptions.getString(this.options, "context");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("insens")) {
            return 1;
         } else if (s.equalsIgnoreCase("1cfa")) {
            return 2;
         } else if (s.equalsIgnoreCase("kcfa")) {
            return 3;
         } else if (s.equalsIgnoreCase("objsens")) {
            return 4;
         } else if (s.equalsIgnoreCase("kobjsens")) {
            return 5;
         } else if (s.equalsIgnoreCase("uniqkobjsens")) {
            return 6;
         } else if (s.equalsIgnoreCase("threadkobjsens")) {
            return 7;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option context", s));
         }
      } else {
         return 1;
      }
   }

   public int propagator() {
      String s = PhaseOptions.getString(this.options, "propagator");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("auto")) {
            return 1;
         } else if (s.equalsIgnoreCase("iter")) {
            return 2;
         } else if (s.equalsIgnoreCase("worklist")) {
            return 3;
         } else if (s.equalsIgnoreCase("alias")) {
            return 4;
         } else if (s.equalsIgnoreCase("bdd")) {
            return 5;
         } else if (s.equalsIgnoreCase("incbdd")) {
            return 6;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option propagator", s));
         }
      } else {
         return 1;
      }
   }

   public int set_impl() {
      String s = PhaseOptions.getString(this.options, "set-impl");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("hash")) {
            return 1;
         } else if (s.equalsIgnoreCase("bit")) {
            return 2;
         } else if (s.equalsIgnoreCase("hybrid")) {
            return 3;
         } else if (s.equalsIgnoreCase("array")) {
            return 4;
         } else if (s.equalsIgnoreCase("heintze")) {
            return 5;
         } else if (s.equalsIgnoreCase("double")) {
            return 6;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option set-impl", s));
         }
      } else {
         return 6;
      }
   }

   public int double_set_old() {
      String s = PhaseOptions.getString(this.options, "double-set-old");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("hash")) {
            return 1;
         } else if (s.equalsIgnoreCase("bit")) {
            return 2;
         } else if (s.equalsIgnoreCase("hybrid")) {
            return 3;
         } else if (s.equalsIgnoreCase("array")) {
            return 4;
         } else if (s.equalsIgnoreCase("heintze")) {
            return 5;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option double-set-old", s));
         }
      } else {
         return 3;
      }
   }

   public int double_set_new() {
      String s = PhaseOptions.getString(this.options, "double-set-new");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("hash")) {
            return 1;
         } else if (s.equalsIgnoreCase("bit")) {
            return 2;
         } else if (s.equalsIgnoreCase("hybrid")) {
            return 3;
         } else if (s.equalsIgnoreCase("array")) {
            return 4;
         } else if (s.equalsIgnoreCase("heintze")) {
            return 5;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option double-set-new", s));
         }
      } else {
         return 3;
      }
   }
}
