package soot.options;

import java.util.Map;
import soot.PhaseOptions;

public class SparkOptions {
   private Map<String, String> options;
   public static final int propagator_iter = 1;
   public static final int propagator_worklist = 2;
   public static final int propagator_cycle = 3;
   public static final int propagator_merge = 4;
   public static final int propagator_alias = 5;
   public static final int propagator_none = 6;
   public static final int set_impl_hash = 1;
   public static final int set_impl_bit = 2;
   public static final int set_impl_hybrid = 3;
   public static final int set_impl_array = 4;
   public static final int set_impl_heintze = 5;
   public static final int set_impl_sharedlist = 6;
   public static final int set_impl_double = 7;
   public static final int double_set_old_hash = 1;
   public static final int double_set_old_bit = 2;
   public static final int double_set_old_hybrid = 3;
   public static final int double_set_old_array = 4;
   public static final int double_set_old_heintze = 5;
   public static final int double_set_old_sharedlist = 6;
   public static final int double_set_new_hash = 1;
   public static final int double_set_new_bit = 2;
   public static final int double_set_new_hybrid = 3;
   public static final int double_set_new_array = 4;
   public static final int double_set_new_heintze = 5;
   public static final int double_set_new_sharedlist = 6;
   public static final int geom_encoding_Geom = 1;
   public static final int geom_encoding_HeapIns = 2;
   public static final int geom_encoding_PtIns = 3;
   public static final int geom_worklist_PQ = 1;
   public static final int geom_worklist_FIFO = 2;

   public SparkOptions(Map<String, String> options) {
      this.options = options;
   }

   public boolean enabled() {
      return PhaseOptions.getBoolean(this.options, "enabled");
   }

   public boolean verbose() {
      return PhaseOptions.getBoolean(this.options, "verbose");
   }

   public boolean ignore_types() {
      return PhaseOptions.getBoolean(this.options, "ignore-types");
   }

   public boolean force_gc() {
      return PhaseOptions.getBoolean(this.options, "force-gc");
   }

   public boolean pre_jimplify() {
      return PhaseOptions.getBoolean(this.options, "pre-jimplify");
   }

   public boolean apponly() {
      return PhaseOptions.getBoolean(this.options, "apponly");
   }

   public boolean vta() {
      return PhaseOptions.getBoolean(this.options, "vta");
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

   public boolean empties_as_allocs() {
      return PhaseOptions.getBoolean(this.options, "empties-as-allocs");
   }

   public boolean simple_edges_bidirectional() {
      return PhaseOptions.getBoolean(this.options, "simple-edges-bidirectional");
   }

   public boolean on_fly_cg() {
      return PhaseOptions.getBoolean(this.options, "on-fly-cg");
   }

   public boolean simplify_offline() {
      return PhaseOptions.getBoolean(this.options, "simplify-offline");
   }

   public boolean simplify_sccs() {
      return PhaseOptions.getBoolean(this.options, "simplify-sccs");
   }

   public boolean ignore_types_for_sccs() {
      return PhaseOptions.getBoolean(this.options, "ignore-types-for-sccs");
   }

   public boolean dump_html() {
      return PhaseOptions.getBoolean(this.options, "dump-html");
   }

   public boolean dump_pag() {
      return PhaseOptions.getBoolean(this.options, "dump-pag");
   }

   public boolean dump_solution() {
      return PhaseOptions.getBoolean(this.options, "dump-solution");
   }

   public boolean topo_sort() {
      return PhaseOptions.getBoolean(this.options, "topo-sort");
   }

   public boolean dump_types() {
      return PhaseOptions.getBoolean(this.options, "dump-types");
   }

   public boolean class_method_var() {
      return PhaseOptions.getBoolean(this.options, "class-method-var");
   }

   public boolean dump_answer() {
      return PhaseOptions.getBoolean(this.options, "dump-answer");
   }

   public boolean add_tags() {
      return PhaseOptions.getBoolean(this.options, "add-tags");
   }

   public boolean set_mass() {
      return PhaseOptions.getBoolean(this.options, "set-mass");
   }

   public boolean cs_demand() {
      return PhaseOptions.getBoolean(this.options, "cs-demand");
   }

   public boolean lazy_pts() {
      return PhaseOptions.getBoolean(this.options, "lazy-pts");
   }

   public boolean geom_pta() {
      return PhaseOptions.getBoolean(this.options, "geom-pta");
   }

   public boolean geom_trans() {
      return PhaseOptions.getBoolean(this.options, "geom-trans");
   }

   public boolean geom_blocking() {
      return PhaseOptions.getBoolean(this.options, "geom-blocking");
   }

   public boolean geom_app_only() {
      return PhaseOptions.getBoolean(this.options, "geom-app-only");
   }

   public int traversal() {
      return PhaseOptions.getInt(this.options, "traversal");
   }

   public int passes() {
      return PhaseOptions.getInt(this.options, "passes");
   }

   public int geom_eval() {
      return PhaseOptions.getInt(this.options, "geom-eval");
   }

   public int geom_frac_base() {
      return PhaseOptions.getInt(this.options, "geom-frac-base");
   }

   public int geom_runs() {
      return PhaseOptions.getInt(this.options, "geom-runs");
   }

   public String geom_dump_verbose() {
      return PhaseOptions.getString(this.options, "geom-dump-verbose");
   }

   public String geom_verify_name() {
      return PhaseOptions.getString(this.options, "geom-verify-name");
   }

   public int propagator() {
      String s = PhaseOptions.getString(this.options, "propagator");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("iter")) {
            return 1;
         } else if (s.equalsIgnoreCase("worklist")) {
            return 2;
         } else if (s.equalsIgnoreCase("cycle")) {
            return 3;
         } else if (s.equalsIgnoreCase("merge")) {
            return 4;
         } else if (s.equalsIgnoreCase("alias")) {
            return 5;
         } else if (s.equalsIgnoreCase("none")) {
            return 6;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option propagator", s));
         }
      } else {
         return 2;
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
         } else if (s.equalsIgnoreCase("sharedlist")) {
            return 6;
         } else if (s.equalsIgnoreCase("double")) {
            return 7;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option set-impl", s));
         }
      } else {
         return 7;
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
         } else if (s.equalsIgnoreCase("sharedlist")) {
            return 6;
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
         } else if (s.equalsIgnoreCase("sharedlist")) {
            return 6;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option double-set-new", s));
         }
      } else {
         return 3;
      }
   }

   public int geom_encoding() {
      String s = PhaseOptions.getString(this.options, "geom-encoding");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("Geom")) {
            return 1;
         } else if (s.equalsIgnoreCase("HeapIns")) {
            return 2;
         } else if (s.equalsIgnoreCase("PtIns")) {
            return 3;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option geom-encoding", s));
         }
      } else {
         return 1;
      }
   }

   public int geom_worklist() {
      String s = PhaseOptions.getString(this.options, "geom-worklist");
      if (s != null && !s.isEmpty()) {
         if (s.equalsIgnoreCase("PQ")) {
            return 1;
         } else if (s.equalsIgnoreCase("FIFO")) {
            return 2;
         } else {
            throw new RuntimeException(String.format("Invalid value %s of phase option geom-worklist", s));
         }
      } else {
         return 1;
      }
   }
}
