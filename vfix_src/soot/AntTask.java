package soot;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;

public class AntTask extends MatchingTask {
   public static final boolean DEBUG = true;
   private ArrayList args = new ArrayList();
   private List phaseopts = new ArrayList();
   private Path phase_help = null;
   private Path process_dir = null;
   private Path dump_body = null;
   private Path dump_cfg = null;
   private Path plugin = null;
   private Path include = null;
   private Path exclude = null;
   private Path dynamic_class = null;
   private Path dynamic_dir = null;
   private Path dynamic_package = null;

   private void debug(String s) {
      System.err.println(s);
   }

   public List args() {
      return this.args;
   }

   private void addArg(String s) {
      this.args.add(s);
   }

   private void addArg(String s, String s2) {
      this.args.add(s);
      this.args.add(s2);
   }

   private Path appendToPath(Path old, Path newPath) {
      if (old == null) {
         return newPath;
      } else {
         old.append(newPath);
         return old;
      }
   }

   private void addPath(String option, Path path) {
      if (path.size() != 0) {
         this.addArg(option);
         this.addArg(path.toString());
      }
   }

   public void execute() throws BuildException {
      if (this.phase_help != null) {
         this.addPath("-phase-help", this.phase_help);
      }

      if (this.process_dir != null) {
         this.addPath("-process-dir", this.process_dir);
      }

      if (this.dump_body != null) {
         this.addPath("-dump-body", this.dump_body);
      }

      if (this.dump_cfg != null) {
         this.addPath("-dump-cfg", this.dump_cfg);
      }

      if (this.plugin != null) {
         this.addPath("-plugin", this.plugin);
      }

      if (this.include != null) {
         this.addPath("-include", this.include);
      }

      if (this.exclude != null) {
         this.addPath("-exclude", this.exclude);
      }

      if (this.dynamic_class != null) {
         this.addPath("-dynamic-class", this.dynamic_class);
      }

      if (this.dynamic_dir != null) {
         this.addPath("-dynamic-dir", this.dynamic_dir);
      }

      if (this.dynamic_package != null) {
         this.addPath("-dynamic-package", this.dynamic_package);
      }

      System.out.println(this.args);

      try {
         Main.main((String[])((String[])this.args.toArray(new String[0])));
         G.v();
         G.reset();
      } catch (Exception var2) {
         var2.printStackTrace();
         throw new BuildException(var2);
      }
   }

   public void setcoffi(boolean arg) {
      if (arg) {
         this.addArg("-coffi");
      }

   }

   public void setjasmin_backend(boolean arg) {
      if (arg) {
         this.addArg("-jasmin-backend");
      }

   }

   public void sethelp(boolean arg) {
      if (arg) {
         this.addArg("-help");
      }

   }

   public void setphase_list(boolean arg) {
      if (arg) {
         this.addArg("-phase-list");
      }

   }

   public void setphase_help(Path arg) {
      if (this.phase_help == null) {
         this.phase_help = new Path(this.getProject());
      }

      this.phase_help = this.appendToPath(this.phase_help, arg);
   }

   public Path createphase_help() {
      if (this.phase_help == null) {
         this.phase_help = new Path(this.getProject());
      }

      return this.phase_help.createPath();
   }

   public void setversion(boolean arg) {
      if (arg) {
         this.addArg("-version");
      }

   }

   public void setverbose(boolean arg) {
      if (arg) {
         this.addArg("-verbose");
      }

   }

   public void setinteractive_mode(boolean arg) {
      if (arg) {
         this.addArg("-interactive-mode");
      }

   }

   public void setunfriendly_mode(boolean arg) {
      if (arg) {
         this.addArg("-unfriendly-mode");
      }

   }

   public void setapp(boolean arg) {
      if (arg) {
         this.addArg("-app");
      }

   }

   public void setwhole_program(boolean arg) {
      if (arg) {
         this.addArg("-whole-program");
      }

   }

   public void setwhole_shimple(boolean arg) {
      if (arg) {
         this.addArg("-whole-shimple");
      }

   }

   public void seton_the_fly(boolean arg) {
      if (arg) {
         this.addArg("-on-the-fly");
      }

   }

   public void setvalidate(boolean arg) {
      if (arg) {
         this.addArg("-validate");
      }

   }

   public void setdebug(boolean arg) {
      if (arg) {
         this.addArg("-debug");
      }

   }

   public void setdebug_resolver(boolean arg) {
      if (arg) {
         this.addArg("-debug-resolver");
      }

   }

   public void setignore_resolving_levels(boolean arg) {
      if (arg) {
         this.addArg("-ignore-resolving-levels");
      }

   }

   public void setsoot_classpath(String arg) {
      this.addArg("-soot-classpath");
      this.addArg(arg);
   }

   public void setprepend_classpath(boolean arg) {
      if (arg) {
         this.addArg("-prepend-classpath");
      }

   }

   public void setignore_classpath_errors(boolean arg) {
      if (arg) {
         this.addArg("-ignore-classpath-errors");
      }

   }

   public void setprocess_multiple_dex(boolean arg) {
      if (arg) {
         this.addArg("-process-multiple-dex");
      }

   }

   public void setsearch_dex_in_archives(boolean arg) {
      if (arg) {
         this.addArg("-search-dex-in-archives");
      }

   }

   public void setprocess_dir(Path arg) {
      if (this.process_dir == null) {
         this.process_dir = new Path(this.getProject());
      }

      this.process_dir = this.appendToPath(this.process_dir, arg);
   }

   public Path createprocess_dir() {
      if (this.process_dir == null) {
         this.process_dir = new Path(this.getProject());
      }

      return this.process_dir.createPath();
   }

   public void setoaat(boolean arg) {
      if (arg) {
         this.addArg("-oaat");
      }

   }

   public void setandroid_jars(String arg) {
      this.addArg("-android-jars");
      this.addArg(arg);
   }

   public void setforce_android_jar(String arg) {
      this.addArg("-force-android-jar");
      this.addArg(arg);
   }

   public void setast_metrics(boolean arg) {
      if (arg) {
         this.addArg("-ast-metrics");
      }

   }

   public void setsrc_prec(String arg) {
      if (!arg.equals("c") && !arg.equals("class") && !arg.equals("only-class") && !arg.equals("J") && !arg.equals("jimple") && !arg.equals("java") && !arg.equals("apk") && !arg.equals("apk-class-jimple") && !arg.equals("apk-c-j")) {
         throw new BuildException("Bad value " + arg + " for option src_prec");
      } else {
         this.addArg("-src-prec");
         this.addArg(arg);
      }
   }

   public void setfull_resolver(boolean arg) {
      if (arg) {
         this.addArg("-full-resolver");
      }

   }

   public void setallow_phantom_refs(boolean arg) {
      if (arg) {
         this.addArg("-allow-phantom-refs");
      }

   }

   public void setno_bodies_for_excluded(boolean arg) {
      if (arg) {
         this.addArg("-no-bodies-for-excluded");
      }

   }

   public void setj2me(boolean arg) {
      if (arg) {
         this.addArg("-j2me");
      }

   }

   public void setmain_class(String arg) {
      this.addArg("-main-class");
      this.addArg(arg);
   }

   public void setpolyglot(boolean arg) {
      if (arg) {
         this.addArg("-polyglot");
      }

   }

   public void setpermissive_resolving(boolean arg) {
      if (arg) {
         this.addArg("-permissive-resolving");
      }

   }

   public void setdrop_bodies_after_load(boolean arg) {
      if (arg) {
         this.addArg("-drop-bodies-after-load");
      }

   }

   public void setoutput_dir(String arg) {
      this.addArg("-output-dir");
      this.addArg(arg);
   }

   public void setoutput_format(String arg) {
      if (!arg.equals("J") && !arg.equals("jimple") && !arg.equals("j") && !arg.equals("jimp") && !arg.equals("S") && !arg.equals("shimple") && !arg.equals("s") && !arg.equals("shimp") && !arg.equals("B") && !arg.equals("baf") && !arg.equals("b") && !arg.equals("G") && !arg.equals("grimple") && !arg.equals("g") && !arg.equals("grimp") && !arg.equals("X") && !arg.equals("xml") && !arg.equals("dex") && !arg.equals("force-dex") && !arg.equals("n") && !arg.equals("none") && !arg.equals("jasmin") && !arg.equals("c") && !arg.equals("class") && !arg.equals("d") && !arg.equals("dava") && !arg.equals("t") && !arg.equals("template") && !arg.equals("a") && !arg.equals("asm")) {
         throw new BuildException("Bad value " + arg + " for option output_format");
      } else {
         this.addArg("-output-format");
         this.addArg(arg);
      }
   }

   public void setjava_version(String arg) {
      if (!arg.equals("default") && !arg.equals("1.1") && !arg.equals("1") && !arg.equals("1.2") && !arg.equals("2") && !arg.equals("1.3") && !arg.equals("3") && !arg.equals("1.4") && !arg.equals("4") && !arg.equals("1.5") && !arg.equals("5") && !arg.equals("1.6") && !arg.equals("6") && !arg.equals("1.7") && !arg.equals("7") && !arg.equals("1.8") && !arg.equals("8")) {
         throw new BuildException("Bad value " + arg + " for option java_version");
      } else {
         this.addArg("-java-version");
         this.addArg(arg);
      }
   }

   public void setoutput_jar(boolean arg) {
      if (arg) {
         this.addArg("-output-jar");
      }

   }

   public void setxml_attributes(boolean arg) {
      if (arg) {
         this.addArg("-xml-attributes");
      }

   }

   public void setprint_tags_in_output(boolean arg) {
      if (arg) {
         this.addArg("-print-tags-in-output");
      }

   }

   public void setno_output_source_file_attribute(boolean arg) {
      if (arg) {
         this.addArg("-no-output-source-file-attribute");
      }

   }

   public void setno_output_inner_classes_attribute(boolean arg) {
      if (arg) {
         this.addArg("-no-output-inner-classes-attribute");
      }

   }

   public void setdump_body(Path arg) {
      if (this.dump_body == null) {
         this.dump_body = new Path(this.getProject());
      }

      this.dump_body = this.appendToPath(this.dump_body, arg);
   }

   public Path createdump_body() {
      if (this.dump_body == null) {
         this.dump_body = new Path(this.getProject());
      }

      return this.dump_body.createPath();
   }

   public void setdump_cfg(Path arg) {
      if (this.dump_cfg == null) {
         this.dump_cfg = new Path(this.getProject());
      }

      this.dump_cfg = this.appendToPath(this.dump_cfg, arg);
   }

   public Path createdump_cfg() {
      if (this.dump_cfg == null) {
         this.dump_cfg = new Path(this.getProject());
      }

      return this.dump_cfg.createPath();
   }

   public void setshow_exception_dests(boolean arg) {
      if (arg) {
         this.addArg("-show-exception-dests");
      }

   }

   public void setgzip(boolean arg) {
      if (arg) {
         this.addArg("-gzip");
      }

   }

   public void setforce_overwrite(boolean arg) {
      if (arg) {
         this.addArg("-force-overwrite");
      }

   }

   public void setplugin(Path arg) {
      if (this.plugin == null) {
         this.plugin = new Path(this.getProject());
      }

      this.plugin = this.appendToPath(this.plugin, arg);
   }

   public Path createplugin() {
      if (this.plugin == null) {
         this.plugin = new Path(this.getProject());
      }

      return this.plugin.createPath();
   }

   public void setwrong_staticness(String arg) {
      if (!arg.equals("fail") && !arg.equals("ignore") && !arg.equals("fix") && !arg.equals("fixstrict")) {
         throw new BuildException("Bad value " + arg + " for option wrong_staticness");
      } else {
         this.addArg("-wrong-staticness");
         this.addArg(arg);
      }
   }

   public void setfield_type_mismatches(String arg) {
      if (!arg.equals("fail") && !arg.equals("ignore") && !arg.equals("null")) {
         throw new BuildException("Bad value " + arg + " for option field_type_mismatches");
      } else {
         this.addArg("-field-type-mismatches");
         this.addArg(arg);
      }
   }

   public void setoptimize(boolean arg) {
      if (arg) {
         this.addArg("-optimize");
      }

   }

   public void setwhole_optimize(boolean arg) {
      if (arg) {
         this.addArg("-whole-optimize");
      }

   }

   public void setvia_grimp(boolean arg) {
      if (arg) {
         this.addArg("-via-grimp");
      }

   }

   public void setvia_shimple(boolean arg) {
      if (arg) {
         this.addArg("-via-shimple");
      }

   }

   public void setthrow_analysis(String arg) {
      if (!arg.equals("pedantic") && !arg.equals("unit") && !arg.equals("dalvik")) {
         throw new BuildException("Bad value " + arg + " for option throw_analysis");
      } else {
         this.addArg("-throw-analysis");
         this.addArg(arg);
      }
   }

   public void setcheck_init_throw_analysis(String arg) {
      if (!arg.equals("auto") && !arg.equals("pedantic") && !arg.equals("unit") && !arg.equals("dalvik")) {
         throw new BuildException("Bad value " + arg + " for option check_init_throw_analysis");
      } else {
         this.addArg("-check-init-throw-analysis");
         this.addArg(arg);
      }
   }

   public void setomit_excepting_unit_edges(boolean arg) {
      if (arg) {
         this.addArg("-omit-excepting-unit-edges");
      }

   }

   public void settrim_cfgs(boolean arg) {
      if (arg) {
         this.addArg("-trim-cfgs");
      }

   }

   public void setignore_resolution_errors(boolean arg) {
      if (arg) {
         this.addArg("-ignore-resolution-errors");
      }

   }

   public void setinclude(Path arg) {
      if (this.include == null) {
         this.include = new Path(this.getProject());
      }

      this.include = this.appendToPath(this.include, arg);
   }

   public Path createinclude() {
      if (this.include == null) {
         this.include = new Path(this.getProject());
      }

      return this.include.createPath();
   }

   public void setexclude(Path arg) {
      if (this.exclude == null) {
         this.exclude = new Path(this.getProject());
      }

      this.exclude = this.appendToPath(this.exclude, arg);
   }

   public Path createexclude() {
      if (this.exclude == null) {
         this.exclude = new Path(this.getProject());
      }

      return this.exclude.createPath();
   }

   public void setinclude_all(boolean arg) {
      if (arg) {
         this.addArg("-include-all");
      }

   }

   public void setdynamic_class(Path arg) {
      if (this.dynamic_class == null) {
         this.dynamic_class = new Path(this.getProject());
      }

      this.dynamic_class = this.appendToPath(this.dynamic_class, arg);
   }

   public Path createdynamic_class() {
      if (this.dynamic_class == null) {
         this.dynamic_class = new Path(this.getProject());
      }

      return this.dynamic_class.createPath();
   }

   public void setdynamic_dir(Path arg) {
      if (this.dynamic_dir == null) {
         this.dynamic_dir = new Path(this.getProject());
      }

      this.dynamic_dir = this.appendToPath(this.dynamic_dir, arg);
   }

   public Path createdynamic_dir() {
      if (this.dynamic_dir == null) {
         this.dynamic_dir = new Path(this.getProject());
      }

      return this.dynamic_dir.createPath();
   }

   public void setdynamic_package(Path arg) {
      if (this.dynamic_package == null) {
         this.dynamic_package = new Path(this.getProject());
      }

      this.dynamic_package = this.appendToPath(this.dynamic_package, arg);
   }

   public Path createdynamic_package() {
      if (this.dynamic_package == null) {
         this.dynamic_package = new Path(this.getProject());
      }

      return this.dynamic_package.createPath();
   }

   public void setkeep_line_number(boolean arg) {
      if (arg) {
         this.addArg("-keep-line-number");
      }

   }

   public void setkeep_offset(boolean arg) {
      if (arg) {
         this.addArg("-keep-offset");
      }

   }

   public void setwrite_local_annotations(boolean arg) {
      if (arg) {
         this.addArg("-write-local-annotations");
      }

   }

   public void setannot_purity(boolean arg) {
      if (arg) {
         this.addArg("-annot-purity");
      }

   }

   public void setannot_nullpointer(boolean arg) {
      if (arg) {
         this.addArg("-annot-nullpointer");
      }

   }

   public void setannot_arraybounds(boolean arg) {
      if (arg) {
         this.addArg("-annot-arraybounds");
      }

   }

   public void setannot_side_effect(boolean arg) {
      if (arg) {
         this.addArg("-annot-side-effect");
      }

   }

   public void setannot_fieldrw(boolean arg) {
      if (arg) {
         this.addArg("-annot-fieldrw");
      }

   }

   public void settime(boolean arg) {
      if (arg) {
         this.addArg("-time");
      }

   }

   public void setsubtract_gc(boolean arg) {
      if (arg) {
         this.addArg("-subtract-gc");
      }

   }

   public void setno_writeout_body_releasing(boolean arg) {
      if (arg) {
         this.addArg("-no-writeout-body-releasing");
      }

   }

   public Object createp_jb() {
      Object ret = new AntTask.PhaseOptjb();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_dtr() {
      Object ret = new AntTask.PhaseOptjb_dtr();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_ese() {
      Object ret = new AntTask.PhaseOptjb_ese();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_ls() {
      Object ret = new AntTask.PhaseOptjb_ls();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_a() {
      Object ret = new AntTask.PhaseOptjb_a();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_ule() {
      Object ret = new AntTask.PhaseOptjb_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_tr() {
      Object ret = new AntTask.PhaseOptjb_tr();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_ulp() {
      Object ret = new AntTask.PhaseOptjb_ulp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_lns() {
      Object ret = new AntTask.PhaseOptjb_lns();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_cp() {
      Object ret = new AntTask.PhaseOptjb_cp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_dae() {
      Object ret = new AntTask.PhaseOptjb_dae();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_cp_ule() {
      Object ret = new AntTask.PhaseOptjb_cp_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_lp() {
      Object ret = new AntTask.PhaseOptjb_lp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_ne() {
      Object ret = new AntTask.PhaseOptjb_ne();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_uce() {
      Object ret = new AntTask.PhaseOptjb_uce();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jb_tt() {
      Object ret = new AntTask.PhaseOptjb_tt();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj() {
      Object ret = new AntTask.PhaseOptjj();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_ls() {
      Object ret = new AntTask.PhaseOptjj_ls();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_a() {
      Object ret = new AntTask.PhaseOptjj_a();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_ule() {
      Object ret = new AntTask.PhaseOptjj_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_tr() {
      Object ret = new AntTask.PhaseOptjj_tr();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_ulp() {
      Object ret = new AntTask.PhaseOptjj_ulp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_lns() {
      Object ret = new AntTask.PhaseOptjj_lns();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_cp() {
      Object ret = new AntTask.PhaseOptjj_cp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_dae() {
      Object ret = new AntTask.PhaseOptjj_dae();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_cp_ule() {
      Object ret = new AntTask.PhaseOptjj_cp_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_lp() {
      Object ret = new AntTask.PhaseOptjj_lp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_ne() {
      Object ret = new AntTask.PhaseOptjj_ne();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jj_uce() {
      Object ret = new AntTask.PhaseOptjj_uce();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjpp() {
      Object ret = new AntTask.PhaseOptwjpp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjpp_cimbt() {
      Object ret = new AntTask.PhaseOptwjpp_cimbt();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wspp() {
      Object ret = new AntTask.PhaseOptwspp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_cg() {
      Object ret = new AntTask.PhaseOptcg();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_cg_cha() {
      Object ret = new AntTask.PhaseOptcg_cha();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_cg_spark() {
      Object ret = new AntTask.PhaseOptcg_spark();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_cg_paddle() {
      Object ret = new AntTask.PhaseOptcg_paddle();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wstp() {
      Object ret = new AntTask.PhaseOptwstp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wsop() {
      Object ret = new AntTask.PhaseOptwsop();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjtp() {
      Object ret = new AntTask.PhaseOptwjtp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjtp_mhp() {
      Object ret = new AntTask.PhaseOptwjtp_mhp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjtp_tn() {
      Object ret = new AntTask.PhaseOptwjtp_tn();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjtp_rdc() {
      Object ret = new AntTask.PhaseOptwjtp_rdc();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjop() {
      Object ret = new AntTask.PhaseOptwjop();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjop_smb() {
      Object ret = new AntTask.PhaseOptwjop_smb();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjop_si() {
      Object ret = new AntTask.PhaseOptwjop_si();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap() {
      Object ret = new AntTask.PhaseOptwjap();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap_ra() {
      Object ret = new AntTask.PhaseOptwjap_ra();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap_umt() {
      Object ret = new AntTask.PhaseOptwjap_umt();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap_uft() {
      Object ret = new AntTask.PhaseOptwjap_uft();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap_tqt() {
      Object ret = new AntTask.PhaseOptwjap_tqt();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap_cgg() {
      Object ret = new AntTask.PhaseOptwjap_cgg();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_wjap_purity() {
      Object ret = new AntTask.PhaseOptwjap_purity();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_shimple() {
      Object ret = new AntTask.PhaseOptshimple();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_stp() {
      Object ret = new AntTask.PhaseOptstp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_sop() {
      Object ret = new AntTask.PhaseOptsop();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_sop_cpf() {
      Object ret = new AntTask.PhaseOptsop_cpf();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jtp() {
      Object ret = new AntTask.PhaseOptjtp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop() {
      Object ret = new AntTask.PhaseOptjop();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_cse() {
      Object ret = new AntTask.PhaseOptjop_cse();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_bcm() {
      Object ret = new AntTask.PhaseOptjop_bcm();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_lcm() {
      Object ret = new AntTask.PhaseOptjop_lcm();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_cp() {
      Object ret = new AntTask.PhaseOptjop_cp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_cpf() {
      Object ret = new AntTask.PhaseOptjop_cpf();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_cbf() {
      Object ret = new AntTask.PhaseOptjop_cbf();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_dae() {
      Object ret = new AntTask.PhaseOptjop_dae();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_nce() {
      Object ret = new AntTask.PhaseOptjop_nce();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_uce1() {
      Object ret = new AntTask.PhaseOptjop_uce1();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_ubf1() {
      Object ret = new AntTask.PhaseOptjop_ubf1();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_uce2() {
      Object ret = new AntTask.PhaseOptjop_uce2();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_ubf2() {
      Object ret = new AntTask.PhaseOptjop_ubf2();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jop_ule() {
      Object ret = new AntTask.PhaseOptjop_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap() {
      Object ret = new AntTask.PhaseOptjap();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_npc() {
      Object ret = new AntTask.PhaseOptjap_npc();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_npcolorer() {
      Object ret = new AntTask.PhaseOptjap_npcolorer();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_abc() {
      Object ret = new AntTask.PhaseOptjap_abc();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_profiling() {
      Object ret = new AntTask.PhaseOptjap_profiling();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_sea() {
      Object ret = new AntTask.PhaseOptjap_sea();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_fieldrw() {
      Object ret = new AntTask.PhaseOptjap_fieldrw();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_cgtagger() {
      Object ret = new AntTask.PhaseOptjap_cgtagger();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_parity() {
      Object ret = new AntTask.PhaseOptjap_parity();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_pat() {
      Object ret = new AntTask.PhaseOptjap_pat();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_lvtagger() {
      Object ret = new AntTask.PhaseOptjap_lvtagger();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_rdtagger() {
      Object ret = new AntTask.PhaseOptjap_rdtagger();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_che() {
      Object ret = new AntTask.PhaseOptjap_che();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_umt() {
      Object ret = new AntTask.PhaseOptjap_umt();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_lit() {
      Object ret = new AntTask.PhaseOptjap_lit();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_aet() {
      Object ret = new AntTask.PhaseOptjap_aet();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_jap_dmt() {
      Object ret = new AntTask.PhaseOptjap_dmt();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_gb() {
      Object ret = new AntTask.PhaseOptgb();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_gb_a1() {
      Object ret = new AntTask.PhaseOptgb_a1();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_gb_cf() {
      Object ret = new AntTask.PhaseOptgb_cf();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_gb_a2() {
      Object ret = new AntTask.PhaseOptgb_a2();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_gb_ule() {
      Object ret = new AntTask.PhaseOptgb_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_gop() {
      Object ret = new AntTask.PhaseOptgop();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bb() {
      Object ret = new AntTask.PhaseOptbb();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bb_lso() {
      Object ret = new AntTask.PhaseOptbb_lso();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bb_sco() {
      Object ret = new AntTask.PhaseOptbb_sco();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bb_pho() {
      Object ret = new AntTask.PhaseOptbb_pho();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bb_ule() {
      Object ret = new AntTask.PhaseOptbb_ule();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bb_lp() {
      Object ret = new AntTask.PhaseOptbb_lp();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_bop() {
      Object ret = new AntTask.PhaseOptbop();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_tag() {
      Object ret = new AntTask.PhaseOpttag();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_tag_ln() {
      Object ret = new AntTask.PhaseOpttag_ln();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_tag_an() {
      Object ret = new AntTask.PhaseOpttag_an();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_tag_dep() {
      Object ret = new AntTask.PhaseOpttag_dep();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_tag_fieldrw() {
      Object ret = new AntTask.PhaseOpttag_fieldrw();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_db() {
      Object ret = new AntTask.PhaseOptdb();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_db_transformations() {
      Object ret = new AntTask.PhaseOptdb_transformations();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_db_renamer() {
      Object ret = new AntTask.PhaseOptdb_renamer();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_db_deobfuscate() {
      Object ret = new AntTask.PhaseOptdb_deobfuscate();
      this.phaseopts.add(ret);
      return ret;
   }

   public Object createp_db_force_recompile() {
      Object ret = new AntTask.PhaseOptdb_force_recompile();
      this.phaseopts.add(ret);
      return ret;
   }

   public class PhaseOptdb_force_recompile {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("db.force-recompile");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptdb_deobfuscate {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("db.deobfuscate");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptdb_renamer {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("db.renamer");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptdb_transformations {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("db.transformations");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptdb {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("db");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setsource_is_javac(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("db");
         AntTask.this.addArg("source-is-javac:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOpttag_fieldrw {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("tag.fieldrw");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOpttag_dep {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("tag.dep");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOpttag_an {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("tag.an");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOpttag_ln {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("tag.ln");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOpttag {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("tag");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbop {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bop");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbb_lp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setunsplit_original_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lp");
         AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbb_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbb_pho {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.pho");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbb_sco {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.sco");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbb_lso {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setdebug(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("debug:" + (arg ? "true" : "false"));
      }

      public void setinter(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("inter:" + (arg ? "true" : "false"));
      }

      public void setsl(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("sl:" + (arg ? "true" : "false"));
      }

      public void setsl2(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("sl2:" + (arg ? "true" : "false"));
      }

      public void setsll(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("sll:" + (arg ? "true" : "false"));
      }

      public void setsll2(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb.lso");
         AntTask.this.addArg("sll2:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptbb {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("bb");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptgop {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gop");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptgb_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb.ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptgb_a2 {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb.a2");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb.a2");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptgb_cf {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb.cf");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptgb_a1 {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb.a1");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb.a1");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptgb {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("gb");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_dmt {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.dmt");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_aet {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.aet");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setkind(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.aet");
         AntTask.this.addArg("kind:" + arg);
      }
   }

   public class PhaseOptjap_lit {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.lit");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_umt {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.umt");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_che {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.che");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_rdtagger {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.rdtagger");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_lvtagger {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.lvtagger");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_pat {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.pat");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_parity {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.parity");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_cgtagger {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.cgtagger");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_fieldrw {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.fieldrw");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setthreshold(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.fieldrw");
         AntTask.this.addArg("threshold:" + arg);
      }
   }

   public class PhaseOptjap_sea {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.sea");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setnaive(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.sea");
         AntTask.this.addArg("naive:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_profiling {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.profiling");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setnotmainentry(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.profiling");
         AntTask.this.addArg("notmainentry:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_abc {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setwith_all(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("with-all:" + (arg ? "true" : "false"));
      }

      public void setwith_cse(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("with-cse:" + (arg ? "true" : "false"));
      }

      public void setwith_arrayref(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("with-arrayref:" + (arg ? "true" : "false"));
      }

      public void setwith_fieldref(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("with-fieldref:" + (arg ? "true" : "false"));
      }

      public void setwith_classfield(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("with-classfield:" + (arg ? "true" : "false"));
      }

      public void setwith_rectarray(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("with-rectarray:" + (arg ? "true" : "false"));
      }

      public void setprofiling(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("profiling:" + (arg ? "true" : "false"));
      }

      public void setadd_color_tags(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.abc");
         AntTask.this.addArg("add-color-tags:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_npcolorer {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.npcolorer");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap_npc {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.npc");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_array_ref(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.npc");
         AntTask.this.addArg("only-array-ref:" + (arg ? "true" : "false"));
      }

      public void setprofiling(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap.npc");
         AntTask.this.addArg("profiling:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjap {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jap");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_ubf2 {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.ubf2");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_uce2 {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.uce2");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setremove_unreachable_traps(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.uce2");
         AntTask.this.addArg("remove-unreachable-traps:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_ubf1 {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.ubf1");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_uce1 {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.uce1");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setremove_unreachable_traps(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.uce1");
         AntTask.this.addArg("remove-unreachable-traps:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_nce {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.nce");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_dae {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.dae");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_tag(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.dae");
         AntTask.this.addArg("only-tag:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.dae");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_cbf {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cbf");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_cpf {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cpf");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_cp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_regular_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cp");
         AntTask.this.addArg("only-regular-locals:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cp");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_lcm {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.lcm");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setunroll(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.lcm");
         AntTask.this.addArg("unroll:" + (arg ? "true" : "false"));
      }

      public void setnaive_side_effect(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.lcm");
         AntTask.this.addArg("naive-side-effect:" + (arg ? "true" : "false"));
      }

      public void setsafety(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.lcm");
         AntTask.this.addArg("safety:" + arg);
      }
   }

   public class PhaseOptjop_bcm {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.bcm");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setnaive_side_effect(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.bcm");
         AntTask.this.addArg("naive-side-effect:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop_cse {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cse");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setnaive_side_effect(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop.cse");
         AntTask.this.addArg("naive-side-effect:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjop {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jop");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjtp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jtp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptsop_cpf {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("sop.cpf");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setprune_cfg(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("sop.cpf");
         AntTask.this.addArg("prune-cfg:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptsop {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("sop");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptstp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("stp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptshimple {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("shimple");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setnode_elim_opt(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("shimple");
         AntTask.this.addArg("node-elim-opt:" + (arg ? "true" : "false"));
      }

      public void setstandard_local_names(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("shimple");
         AntTask.this.addArg("standard-local-names:" + (arg ? "true" : "false"));
      }

      public void setextended(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("shimple");
         AntTask.this.addArg("extended:" + (arg ? "true" : "false"));
      }

      public void setdebug(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("shimple");
         AntTask.this.addArg("debug:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap_purity {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setdump_summaries(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("dump-summaries:" + (arg ? "true" : "false"));
      }

      public void setdump_cg(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("dump-cg:" + (arg ? "true" : "false"));
      }

      public void setdump_intra(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("dump-intra:" + (arg ? "true" : "false"));
      }

      public void setprint(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("print:" + (arg ? "true" : "false"));
      }

      public void setannotate(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("annotate:" + (arg ? "true" : "false"));
      }

      public void setverbose(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.purity");
         AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap_cgg {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.cgg");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setshow_lib_meths(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.cgg");
         AntTask.this.addArg("show-lib-meths:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap_tqt {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.tqt");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap_uft {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.uft");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap_umt {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.umt");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap_ra {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap.ra");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjap {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjap");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjop_si {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setrerun_jb(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("rerun-jb:" + (arg ? "true" : "false"));
      }

      public void setinsert_null_checks(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("insert-null-checks:" + (arg ? "true" : "false"));
      }

      public void setinsert_redundant_casts(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("insert-redundant-casts:" + (arg ? "true" : "false"));
      }

      public void setallowed_modifier_changes(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("allowed-modifier-changes:" + arg);
      }

      public void setexpansion_factor(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("expansion-factor:" + arg);
      }

      public void setmax_container_size(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("max-container-size:" + arg);
      }

      public void setmax_inlinee_size(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.si");
         AntTask.this.addArg("max-inlinee-size:" + arg);
      }
   }

   public class PhaseOptwjop_smb {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.smb");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setinsert_null_checks(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.smb");
         AntTask.this.addArg("insert-null-checks:" + (arg ? "true" : "false"));
      }

      public void setinsert_redundant_casts(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.smb");
         AntTask.this.addArg("insert-redundant-casts:" + (arg ? "true" : "false"));
      }

      public void setallowed_modifier_changes(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop.smb");
         AntTask.this.addArg("allowed-modifier-changes:" + arg);
      }
   }

   public class PhaseOptwjop {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjop");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjtp_rdc {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.rdc");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setfixed_class_names(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.rdc");
         AntTask.this.addArg("fixed-class-names:" + arg);
      }
   }

   public class PhaseOptwjtp_tn {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setavoid_deadlock(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("avoid-deadlock:" + (arg ? "true" : "false"));
      }

      public void setopen_nesting(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("open-nesting:" + (arg ? "true" : "false"));
      }

      public void setdo_mhp(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("do-mhp:" + (arg ? "true" : "false"));
      }

      public void setdo_tlo(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("do-tlo:" + (arg ? "true" : "false"));
      }

      public void setprint_graph(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("print-graph:" + (arg ? "true" : "false"));
      }

      public void setprint_table(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("print-table:" + (arg ? "true" : "false"));
      }

      public void setprint_debug(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("print-debug:" + (arg ? "true" : "false"));
      }

      public void setlocking_scheme(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.tn");
         AntTask.this.addArg("locking-scheme:" + arg);
      }
   }

   public class PhaseOptwjtp_mhp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp.mhp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjtp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjtp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwsop {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wsop");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwstp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wstp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptcg_paddle {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setverbose(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
      }

      public void setbdd(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("bdd:" + (arg ? "true" : "false"));
      }

      public void setdynamic_order(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("dynamic-order:" + (arg ? "true" : "false"));
      }

      public void setprofile(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("profile:" + (arg ? "true" : "false"));
      }

      public void setverbosegc(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("verbosegc:" + (arg ? "true" : "false"));
      }

      public void setignore_types(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("ignore-types:" + (arg ? "true" : "false"));
      }

      public void setpre_jimplify(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("pre-jimplify:" + (arg ? "true" : "false"));
      }

      public void setcontext_heap(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("context-heap:" + (arg ? "true" : "false"));
      }

      public void setrta(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("rta:" + (arg ? "true" : "false"));
      }

      public void setfield_based(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("field-based:" + (arg ? "true" : "false"));
      }

      public void settypes_for_sites(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("types-for-sites:" + (arg ? "true" : "false"));
      }

      public void setmerge_stringbuffer(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("merge-stringbuffer:" + (arg ? "true" : "false"));
      }

      public void setstring_constants(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("string-constants:" + (arg ? "true" : "false"));
      }

      public void setsimulate_natives(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("simulate-natives:" + (arg ? "true" : "false"));
      }

      public void setglobal_nodes_in_natives(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("global-nodes-in-natives:" + (arg ? "true" : "false"));
      }

      public void setsimple_edges_bidirectional(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("simple-edges-bidirectional:" + (arg ? "true" : "false"));
      }

      public void setthis_edges(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("this-edges:" + (arg ? "true" : "false"));
      }

      public void setprecise_newinstance(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("precise-newinstance:" + (arg ? "true" : "false"));
      }

      public void setcontext_counts(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("context-counts:" + (arg ? "true" : "false"));
      }

      public void settotal_context_counts(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("total-context-counts:" + (arg ? "true" : "false"));
      }

      public void setmethod_context_counts(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("method-context-counts:" + (arg ? "true" : "false"));
      }

      public void setset_mass(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("set-mass:" + (arg ? "true" : "false"));
      }

      public void setnumber_nodes(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("number-nodes:" + (arg ? "true" : "false"));
      }

      public void setconf(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("conf:" + arg);
      }

      public void setorder(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("order:" + arg);
      }

      public void setq(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("q:" + arg);
      }

      public void setbackend(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("backend:" + arg);
      }

      public void setbdd_nodes(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("bdd-nodes:" + arg);
      }

      public void setcontext(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("context:" + arg);
      }

      public void setk(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("k:" + arg);
      }

      public void setpropagator(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("propagator:" + arg);
      }

      public void setset_impl(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("set-impl:" + arg);
      }

      public void setdouble_set_old(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("double-set-old:" + arg);
      }

      public void setdouble_set_new(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.paddle");
         AntTask.this.addArg("double-set-new:" + arg);
      }
   }

   public class PhaseOptcg_spark {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setverbose(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
      }

      public void setignore_types(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("ignore-types:" + (arg ? "true" : "false"));
      }

      public void setforce_gc(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("force-gc:" + (arg ? "true" : "false"));
      }

      public void setpre_jimplify(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("pre-jimplify:" + (arg ? "true" : "false"));
      }

      public void setapponly(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("apponly:" + (arg ? "true" : "false"));
      }

      public void setvta(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("vta:" + (arg ? "true" : "false"));
      }

      public void setrta(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("rta:" + (arg ? "true" : "false"));
      }

      public void setfield_based(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("field-based:" + (arg ? "true" : "false"));
      }

      public void settypes_for_sites(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("types-for-sites:" + (arg ? "true" : "false"));
      }

      public void setmerge_stringbuffer(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("merge-stringbuffer:" + (arg ? "true" : "false"));
      }

      public void setstring_constants(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("string-constants:" + (arg ? "true" : "false"));
      }

      public void setsimulate_natives(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("simulate-natives:" + (arg ? "true" : "false"));
      }

      public void setempties_as_allocs(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("empties-as-allocs:" + (arg ? "true" : "false"));
      }

      public void setsimple_edges_bidirectional(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("simple-edges-bidirectional:" + (arg ? "true" : "false"));
      }

      public void seton_fly_cg(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("on-fly-cg:" + (arg ? "true" : "false"));
      }

      public void setsimplify_offline(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("simplify-offline:" + (arg ? "true" : "false"));
      }

      public void setsimplify_sccs(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("simplify-sccs:" + (arg ? "true" : "false"));
      }

      public void setignore_types_for_sccs(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("ignore-types-for-sccs:" + (arg ? "true" : "false"));
      }

      public void setdump_html(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("dump-html:" + (arg ? "true" : "false"));
      }

      public void setdump_pag(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("dump-pag:" + (arg ? "true" : "false"));
      }

      public void setdump_solution(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("dump-solution:" + (arg ? "true" : "false"));
      }

      public void settopo_sort(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("topo-sort:" + (arg ? "true" : "false"));
      }

      public void setdump_types(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("dump-types:" + (arg ? "true" : "false"));
      }

      public void setclass_method_var(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("class-method-var:" + (arg ? "true" : "false"));
      }

      public void setdump_answer(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("dump-answer:" + (arg ? "true" : "false"));
      }

      public void setadd_tags(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("add-tags:" + (arg ? "true" : "false"));
      }

      public void setset_mass(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("set-mass:" + (arg ? "true" : "false"));
      }

      public void setcs_demand(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("cs-demand:" + (arg ? "true" : "false"));
      }

      public void setlazy_pts(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("lazy-pts:" + (arg ? "true" : "false"));
      }

      public void setgeom_pta(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-pta:" + (arg ? "true" : "false"));
      }

      public void setgeom_trans(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-trans:" + (arg ? "true" : "false"));
      }

      public void setgeom_blocking(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-blocking:" + (arg ? "true" : "false"));
      }

      public void setgeom_app_only(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-app-only:" + (arg ? "true" : "false"));
      }

      public void setpropagator(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("propagator:" + arg);
      }

      public void setset_impl(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("set-impl:" + arg);
      }

      public void setdouble_set_old(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("double-set-old:" + arg);
      }

      public void setdouble_set_new(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("double-set-new:" + arg);
      }

      public void settraversal(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("traversal:" + arg);
      }

      public void setpasses(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("passes:" + arg);
      }

      public void setgeom_encoding(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-encoding:" + arg);
      }

      public void setgeom_worklist(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-worklist:" + arg);
      }

      public void setgeom_dump_verbose(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-dump-verbose:" + arg);
      }

      public void setgeom_verify_name(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-verify-name:" + arg);
      }

      public void setgeom_eval(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-eval:" + arg);
      }

      public void setgeom_frac_base(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-frac-base:" + arg);
      }

      public void setgeom_runs(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.spark");
         AntTask.this.addArg("geom-runs:" + arg);
      }
   }

   public class PhaseOptcg_cha {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.cha");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setverbose(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.cha");
         AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
      }

      public void setapponly(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg.cha");
         AntTask.this.addArg("apponly:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptcg {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setsafe_forname(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("safe-forname:" + (arg ? "true" : "false"));
      }

      public void setsafe_newinstance(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("safe-newinstance:" + (arg ? "true" : "false"));
      }

      public void setverbose(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
      }

      public void setall_reachable(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("all-reachable:" + (arg ? "true" : "false"));
      }

      public void setimplicit_entry(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("implicit-entry:" + (arg ? "true" : "false"));
      }

      public void settrim_clinit(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("trim-clinit:" + (arg ? "true" : "false"));
      }

      public void settypes_for_invoke(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("types-for-invoke:" + (arg ? "true" : "false"));
      }

      public void setlibrary(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("library:" + arg);
      }

      public void setjdkver(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("jdkver:" + arg);
      }

      public void setreflection_log(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("reflection-log:" + arg);
      }

      public void setguards(String arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("cg");
         AntTask.this.addArg("guards:" + arg);
      }
   }

   public class PhaseOptwspp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wspp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjpp_cimbt {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjpp.cimbt");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setverbose(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjpp.cimbt");
         AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptwjpp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("wjpp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_uce {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.uce");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_ne {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.ne");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_lp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.lp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setunsplit_original_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.lp");
         AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_cp_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.cp-ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_dae {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.dae");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.dae");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_cp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.cp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_regular_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.cp");
         AntTask.this.addArg("only-regular-locals:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.cp");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_lns {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.lns");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.lns");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_ulp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.ulp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setunsplit_original_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.ulp");
         AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_tr {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.tr");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_a {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.a");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.a");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj_ls {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj.ls");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjj {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setuse_original_names(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jj");
         AntTask.this.addArg("use-original-names:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_tt {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.tt");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_uce {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.uce");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setremove_unreachable_traps(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.uce");
         AntTask.this.addArg("remove-unreachable-traps:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_ne {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.ne");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_lp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.lp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setunsplit_original_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.lp");
         AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_cp_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.cp-ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_dae {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.dae");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.dae");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_cp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.cp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_regular_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.cp");
         AntTask.this.addArg("only-regular-locals:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.cp");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_lns {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.lns");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.lns");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }

      public void setsort_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.lns");
         AntTask.this.addArg("sort-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_ulp {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.ulp");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setunsplit_original_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.ulp");
         AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_tr {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.tr");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setuse_older_type_assigner(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.tr");
         AntTask.this.addArg("use-older-type-assigner:" + (arg ? "true" : "false"));
      }

      public void setcompare_type_assigners(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.tr");
         AntTask.this.addArg("compare-type-assigners:" + (arg ? "true" : "false"));
      }

      public void setignore_nullpointer_dereferences(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.tr");
         AntTask.this.addArg("ignore-nullpointer-dereferences:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_ule {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.ule");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_a {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.a");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setonly_stack_locals(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.a");
         AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_ls {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.ls");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_ese {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.ese");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb_dtr {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb.dtr");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }
   }

   public class PhaseOptjb {
      public void setenabled(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb");
         AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
      }

      public void setuse_original_names(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb");
         AntTask.this.addArg("use-original-names:" + (arg ? "true" : "false"));
      }

      public void setpreserve_source_annotations(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb");
         AntTask.this.addArg("preserve-source-annotations:" + (arg ? "true" : "false"));
      }

      public void setstabilize_local_names(boolean arg) {
         AntTask.this.addArg("-p");
         AntTask.this.addArg("jb");
         AntTask.this.addArg("stabilize-local-names:" + (arg ? "true" : "false"));
      }
   }
}
