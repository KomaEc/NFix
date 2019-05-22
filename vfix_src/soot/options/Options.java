package soot.options;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.G;
import soot.PackManager;
import soot.Singletons;

public class Options extends OptionsBase {
   public static final int src_prec_c = 1;
   public static final int src_prec_class = 1;
   public static final int src_prec_only_class = 2;
   public static final int src_prec_J = 3;
   public static final int src_prec_jimple = 3;
   public static final int src_prec_java = 4;
   public static final int src_prec_apk = 5;
   public static final int src_prec_apk_class_jimple = 6;
   public static final int src_prec_apk_c_j = 6;
   public static final int output_format_J = 1;
   public static final int output_format_jimple = 1;
   public static final int output_format_j = 2;
   public static final int output_format_jimp = 2;
   public static final int output_format_S = 3;
   public static final int output_format_shimple = 3;
   public static final int output_format_s = 4;
   public static final int output_format_shimp = 4;
   public static final int output_format_B = 5;
   public static final int output_format_baf = 5;
   public static final int output_format_b = 6;
   public static final int output_format_G = 7;
   public static final int output_format_grimple = 7;
   public static final int output_format_g = 8;
   public static final int output_format_grimp = 8;
   public static final int output_format_X = 9;
   public static final int output_format_xml = 9;
   public static final int output_format_dex = 10;
   public static final int output_format_force_dex = 11;
   public static final int output_format_n = 12;
   public static final int output_format_none = 12;
   public static final int output_format_jasmin = 13;
   public static final int output_format_c = 14;
   public static final int output_format_class = 14;
   public static final int output_format_d = 15;
   public static final int output_format_dava = 15;
   public static final int output_format_t = 16;
   public static final int output_format_template = 16;
   public static final int output_format_a = 17;
   public static final int output_format_asm = 17;
   public static final int java_version_default = 1;
   public static final int java_version_1_1 = 2;
   public static final int java_version_1 = 2;
   public static final int java_version_1_2 = 3;
   public static final int java_version_2 = 3;
   public static final int java_version_1_3 = 4;
   public static final int java_version_3 = 4;
   public static final int java_version_1_4 = 5;
   public static final int java_version_4 = 5;
   public static final int java_version_1_5 = 6;
   public static final int java_version_5 = 6;
   public static final int java_version_1_6 = 7;
   public static final int java_version_6 = 7;
   public static final int java_version_1_7 = 8;
   public static final int java_version_7 = 8;
   public static final int java_version_1_8 = 9;
   public static final int java_version_8 = 9;
   public static final int wrong_staticness_fail = 1;
   public static final int wrong_staticness_ignore = 2;
   public static final int wrong_staticness_fix = 3;
   public static final int wrong_staticness_fixstrict = 4;
   public static final int field_type_mismatches_fail = 1;
   public static final int field_type_mismatches_ignore = 2;
   public static final int field_type_mismatches_null = 3;
   public static final int throw_analysis_pedantic = 1;
   public static final int throw_analysis_unit = 2;
   public static final int throw_analysis_dalvik = 3;
   public static final int check_init_throw_analysis_auto = 1;
   public static final int check_init_throw_analysis_pedantic = 2;
   public static final int check_init_throw_analysis_unit = 3;
   public static final int check_init_throw_analysis_dalvik = 4;
   private boolean coffi = false;
   private boolean jasmin_backend = false;
   private boolean help = false;
   private boolean phase_list = false;
   private List<String> phase_help = null;
   private boolean version = false;
   private boolean verbose = false;
   private boolean interactive_mode = false;
   private boolean unfriendly_mode = false;
   private boolean app = false;
   private boolean whole_program = false;
   private boolean whole_shimple = false;
   private boolean on_the_fly = false;
   private boolean validate = false;
   private boolean debug = false;
   private boolean debug_resolver = false;
   private boolean ignore_resolving_levels = false;
   private String soot_classpath = "";
   private boolean prepend_classpath = false;
   private boolean ignore_classpath_errors = false;
   private boolean process_multiple_dex = false;
   private boolean search_dex_in_archives = false;
   private List<String> process_dir = null;
   private boolean oaat = false;
   private String android_jars = "";
   private String force_android_jar = "";
   private int android_api_version = -1;
   private boolean ast_metrics = false;
   private int src_prec = 0;
   private boolean full_resolver = false;
   private boolean allow_phantom_refs = false;
   private boolean no_bodies_for_excluded = false;
   private boolean j2me = false;
   private String main_class = "";
   private boolean polyglot = false;
   private boolean permissive_resolving = false;
   private boolean drop_bodies_after_load = true;
   private String output_dir = "";
   private int output_format = 0;
   private int java_version = 0;
   private boolean output_jar = false;
   private boolean xml_attributes = false;
   private boolean print_tags_in_output = false;
   private boolean no_output_source_file_attribute = false;
   private boolean no_output_inner_classes_attribute = false;
   private List<String> dump_body = null;
   private List<String> dump_cfg = null;
   private boolean show_exception_dests = true;
   private boolean gzip = false;
   private boolean force_overwrite = false;
   private List<String> plugin = null;
   private int wrong_staticness = 0;
   private int field_type_mismatches = 0;
   private boolean via_grimp = false;
   private boolean via_shimple = false;
   private int throw_analysis = 0;
   private int check_init_throw_analysis = 0;
   private boolean omit_excepting_unit_edges = false;
   private boolean ignore_resolution_errors = false;
   private List<String> include = null;
   private List<String> exclude = null;
   private boolean include_all = false;
   private List<String> dynamic_class = null;
   private List<String> dynamic_dir = null;
   private List<String> dynamic_package = null;
   private boolean keep_line_number = false;
   private boolean keep_offset = false;
   private boolean write_local_annotations = false;
   private boolean time = false;
   private boolean subtract_gc = false;
   private boolean no_writeout_body_releasing = false;

   public Options(Singletons.Global g) {
   }

   public static Options v() {
      return G.v().soot_options_Options();
   }

   public boolean parse(String[] argv) {
      List<String> phaseOptions = new LinkedList();

      for(int i = argv.length; i > 0; --i) {
         this.pushOption(argv[i - 1]);
      }

      while(true) {
         String value;
         String phaseOption;
         while(this.hasMoreOptions()) {
            String option = this.nextOption();
            if (option.charAt(0) != '-') {
               this.classes.add(option);
            } else {
               while(option.charAt(0) == '-') {
                  option = option.substring(1);
               }

               if (option.equals("coffi")) {
                  this.coffi = true;
               } else if (option.equals("jasmin-backend")) {
                  this.jasmin_backend = true;
               } else if (!option.equals("h") && !option.equals("help")) {
                  if (!option.equals("pl") && !option.equals("phase-list")) {
                     if (!option.equals("ph") && !option.equals("phase-help")) {
                        if (option.equals("version")) {
                           this.version = true;
                        } else if (!option.equals("v") && !option.equals("verbose")) {
                           if (option.equals("interactive-mode")) {
                              this.interactive_mode = true;
                           } else if (option.equals("unfriendly-mode")) {
                              this.unfriendly_mode = true;
                           } else if (option.equals("app")) {
                              this.app = true;
                           } else if (!option.equals("w") && !option.equals("whole-program")) {
                              if (!option.equals("ws") && !option.equals("whole-shimple")) {
                                 if (!option.equals("fly") && !option.equals("on-the-fly")) {
                                    if (option.equals("validate")) {
                                       this.validate = true;
                                    } else if (option.equals("debug")) {
                                       this.debug = true;
                                    } else if (option.equals("debug-resolver")) {
                                       this.debug_resolver = true;
                                    } else if (option.equals("ignore-resolving-levels")) {
                                       this.ignore_resolving_levels = true;
                                    } else if (!option.equals("cp") && !option.equals("soot-class-path") && !option.equals("soot-classpath")) {
                                       if (!option.equals("pp") && !option.equals("prepend-classpath")) {
                                          if (!option.equals("ice") && !option.equals("ignore-classpath-errors")) {
                                             if (option.equals("process-multiple-dex")) {
                                                this.process_multiple_dex = true;
                                             } else if (option.equals("search-dex-in-archives")) {
                                                this.search_dex_in_archives = true;
                                             } else if (!option.equals("process-path") && !option.equals("process-dir")) {
                                                if (option.equals("oaat")) {
                                                   this.oaat = true;
                                                } else if (option.equals("android-jars")) {
                                                   if (!this.hasMoreOptions()) {
                                                      G.v().out.println("No value given for option -" + option);
                                                      return false;
                                                   }

                                                   value = this.nextOption();
                                                   if (!this.android_jars.isEmpty()) {
                                                      G.v().out.println("Duplicate values " + this.android_jars + " and " + value + " for option -" + option);
                                                      return false;
                                                   }

                                                   this.android_jars = value;
                                                } else if (option.equals("force-android-jar")) {
                                                   if (!this.hasMoreOptions()) {
                                                      G.v().out.println("No value given for option -" + option);
                                                      return false;
                                                   }

                                                   value = this.nextOption();
                                                   if (!this.force_android_jar.isEmpty()) {
                                                      G.v().out.println("Duplicate values " + this.force_android_jar + " and " + value + " for option -" + option);
                                                      return false;
                                                   }

                                                   this.force_android_jar = value;
                                                } else if (option.equals("android-api-version")) {
                                                   if (!this.hasMoreOptions()) {
                                                      G.v().out.println("No value given for option -" + option);
                                                      return false;
                                                   }

                                                   value = this.nextOption();
                                                   if (this.android_api_version != -1) {
                                                      G.v().out.println("Duplicate values " + this.android_api_version + " and " + value + " for option -" + option);
                                                      return false;
                                                   }

                                                   this.android_api_version = Integer.valueOf(value);
                                                } else if (option.equals("ast-metrics")) {
                                                   this.ast_metrics = true;
                                                } else if (option.equals("src-prec")) {
                                                   if (!this.hasMoreOptions()) {
                                                      G.v().out.println("No value given for option -" + option);
                                                      return false;
                                                   }

                                                   value = this.nextOption();
                                                   if (!value.equals("c") && !value.equals("class")) {
                                                      if (value.equals("only-class")) {
                                                         if (this.src_prec != 0 && this.src_prec != 2) {
                                                            G.v().out.println("Multiple values given for option " + option);
                                                            return false;
                                                         }

                                                         this.src_prec = 2;
                                                      } else if (!value.equals("J") && !value.equals("jimple")) {
                                                         if (value.equals("java")) {
                                                            if (this.src_prec != 0 && this.src_prec != 4) {
                                                               G.v().out.println("Multiple values given for option " + option);
                                                               return false;
                                                            }

                                                            this.src_prec = 4;
                                                         } else if (value.equals("apk")) {
                                                            if (this.src_prec != 0 && this.src_prec != 5) {
                                                               G.v().out.println("Multiple values given for option " + option);
                                                               return false;
                                                            }

                                                            this.src_prec = 5;
                                                         } else {
                                                            if (!value.equals("apk-class-jimple") && !value.equals("apk-c-j")) {
                                                               G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                               return false;
                                                            }

                                                            if (this.src_prec != 0 && this.src_prec != 6) {
                                                               G.v().out.println("Multiple values given for option " + option);
                                                               return false;
                                                            }

                                                            this.src_prec = 6;
                                                         }
                                                      } else {
                                                         if (this.src_prec != 0 && this.src_prec != 3) {
                                                            G.v().out.println("Multiple values given for option " + option);
                                                            return false;
                                                         }

                                                         this.src_prec = 3;
                                                      }
                                                   } else {
                                                      if (this.src_prec != 0 && this.src_prec != 1) {
                                                         G.v().out.println("Multiple values given for option " + option);
                                                         return false;
                                                      }

                                                      this.src_prec = 1;
                                                   }
                                                } else if (option.equals("full-resolver")) {
                                                   this.full_resolver = true;
                                                } else if (option.equals("allow-phantom-refs")) {
                                                   this.allow_phantom_refs = true;
                                                } else if (option.equals("no-bodies-for-excluded")) {
                                                   this.no_bodies_for_excluded = true;
                                                } else if (option.equals("j2me")) {
                                                   this.j2me = true;
                                                } else if (option.equals("main-class")) {
                                                   if (!this.hasMoreOptions()) {
                                                      G.v().out.println("No value given for option -" + option);
                                                      return false;
                                                   }

                                                   value = this.nextOption();
                                                   if (!this.main_class.isEmpty()) {
                                                      G.v().out.println("Duplicate values " + this.main_class + " and " + value + " for option -" + option);
                                                      return false;
                                                   }

                                                   this.main_class = value;
                                                } else if (option.equals("polyglot")) {
                                                   this.polyglot = true;
                                                } else if (option.equals("permissive-resolving")) {
                                                   this.permissive_resolving = true;
                                                } else if (option.equals("no-drop-bodies-after-load")) {
                                                   this.drop_bodies_after_load = false;
                                                } else if (!option.equals("d") && !option.equals("output-dir")) {
                                                   if (!option.equals("f") && !option.equals("output-format")) {
                                                      if (option.equals("java-version")) {
                                                         if (!this.hasMoreOptions()) {
                                                            G.v().out.println("No value given for option -" + option);
                                                            return false;
                                                         }

                                                         value = this.nextOption();
                                                         if (value.equals("default")) {
                                                            if (this.java_version != 0 && this.java_version != 1) {
                                                               G.v().out.println("Multiple values given for option " + option);
                                                               return false;
                                                            }

                                                            this.java_version = 1;
                                                         } else if (!value.equals("1.1") && !value.equals("1")) {
                                                            if (!value.equals("1.2") && !value.equals("2")) {
                                                               if (!value.equals("1.3") && !value.equals("3")) {
                                                                  if (!value.equals("1.4") && !value.equals("4")) {
                                                                     if (!value.equals("1.5") && !value.equals("5")) {
                                                                        if (!value.equals("1.6") && !value.equals("6")) {
                                                                           if (!value.equals("1.7") && !value.equals("7")) {
                                                                              if (!value.equals("1.8") && !value.equals("8")) {
                                                                                 G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                                                 return false;
                                                                              }

                                                                              if (this.java_version != 0 && this.java_version != 9) {
                                                                                 G.v().out.println("Multiple values given for option " + option);
                                                                                 return false;
                                                                              }

                                                                              this.java_version = 9;
                                                                           } else {
                                                                              if (this.java_version != 0 && this.java_version != 8) {
                                                                                 G.v().out.println("Multiple values given for option " + option);
                                                                                 return false;
                                                                              }

                                                                              this.java_version = 8;
                                                                           }
                                                                        } else {
                                                                           if (this.java_version != 0 && this.java_version != 7) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.java_version = 7;
                                                                        }
                                                                     } else {
                                                                        if (this.java_version != 0 && this.java_version != 6) {
                                                                           G.v().out.println("Multiple values given for option " + option);
                                                                           return false;
                                                                        }

                                                                        this.java_version = 6;
                                                                     }
                                                                  } else {
                                                                     if (this.java_version != 0 && this.java_version != 5) {
                                                                        G.v().out.println("Multiple values given for option " + option);
                                                                        return false;
                                                                     }

                                                                     this.java_version = 5;
                                                                  }
                                                               } else {
                                                                  if (this.java_version != 0 && this.java_version != 4) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.java_version = 4;
                                                               }
                                                            } else {
                                                               if (this.java_version != 0 && this.java_version != 3) {
                                                                  G.v().out.println("Multiple values given for option " + option);
                                                                  return false;
                                                               }

                                                               this.java_version = 3;
                                                            }
                                                         } else {
                                                            if (this.java_version != 0 && this.java_version != 2) {
                                                               G.v().out.println("Multiple values given for option " + option);
                                                               return false;
                                                            }

                                                            this.java_version = 2;
                                                         }
                                                      } else if (!option.equals("outjar") && !option.equals("output-jar")) {
                                                         if (option.equals("xml-attributes")) {
                                                            this.xml_attributes = true;
                                                         } else if (!option.equals("print-tags") && !option.equals("print-tags-in-output")) {
                                                            if (option.equals("no-output-source-file-attribute")) {
                                                               this.no_output_source_file_attribute = true;
                                                            } else if (option.equals("no-output-inner-classes-attribute")) {
                                                               this.no_output_inner_classes_attribute = true;
                                                            } else if (option.equals("dump-body")) {
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No value given for option -" + option);
                                                                  return false;
                                                               }

                                                               value = this.nextOption();
                                                               if (this.dump_body == null) {
                                                                  this.dump_body = new LinkedList();
                                                               }

                                                               this.dump_body.add(value);
                                                            } else if (option.equals("dump-cfg")) {
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No value given for option -" + option);
                                                                  return false;
                                                               }

                                                               value = this.nextOption();
                                                               if (this.dump_cfg == null) {
                                                                  this.dump_cfg = new LinkedList();
                                                               }

                                                               this.dump_cfg.add(value);
                                                            } else if (option.equals("no-show-exception-dests")) {
                                                               this.show_exception_dests = false;
                                                            } else if (option.equals("gzip")) {
                                                               this.gzip = true;
                                                            } else if (option.equals("force-overwrite")) {
                                                               this.force_overwrite = true;
                                                            } else if (option.equals("plugin")) {
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No value given for option -" + option);
                                                                  return false;
                                                               }

                                                               value = this.nextOption();
                                                               if (this.plugin == null) {
                                                                  this.plugin = new LinkedList();
                                                               }

                                                               this.plugin.add(value);
                                                               if (!this.loadPluginConfiguration(value)) {
                                                                  G.v().out.println("Failed to load plugin " + value);
                                                                  return false;
                                                               }
                                                            } else if (option.equals("wrong-staticness")) {
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No value given for option -" + option);
                                                                  return false;
                                                               }

                                                               value = this.nextOption();
                                                               if (value.equals("fail")) {
                                                                  if (this.wrong_staticness != 0 && this.wrong_staticness != 1) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.wrong_staticness = 1;
                                                               } else if (value.equals("ignore")) {
                                                                  if (this.wrong_staticness != 0 && this.wrong_staticness != 2) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.wrong_staticness = 2;
                                                               } else if (value.equals("fix")) {
                                                                  if (this.wrong_staticness != 0 && this.wrong_staticness != 3) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.wrong_staticness = 3;
                                                               } else {
                                                                  if (!value.equals("fixstrict")) {
                                                                     G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                                     return false;
                                                                  }

                                                                  if (this.wrong_staticness != 0 && this.wrong_staticness != 4) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.wrong_staticness = 4;
                                                               }
                                                            } else if (option.equals("field-type-mismatches")) {
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No value given for option -" + option);
                                                                  return false;
                                                               }

                                                               value = this.nextOption();
                                                               if (value.equals("fail")) {
                                                                  if (this.field_type_mismatches != 0 && this.field_type_mismatches != 1) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.field_type_mismatches = 1;
                                                               } else if (value.equals("ignore")) {
                                                                  if (this.field_type_mismatches != 0 && this.field_type_mismatches != 2) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.field_type_mismatches = 2;
                                                               } else {
                                                                  if (!value.equals("null")) {
                                                                     G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                                     return false;
                                                                  }

                                                                  if (this.field_type_mismatches != 0 && this.field_type_mismatches != 3) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.field_type_mismatches = 3;
                                                               }
                                                            } else if (!option.equals("p") && !option.equals("phase-option")) {
                                                               if (!option.equals("O") && !option.equals("optimize")) {
                                                                  if (!option.equals("W") && !option.equals("whole-optimize")) {
                                                                     if (option.equals("via-grimp")) {
                                                                        this.via_grimp = true;
                                                                     } else if (option.equals("via-shimple")) {
                                                                        this.via_shimple = true;
                                                                     } else if (option.equals("throw-analysis")) {
                                                                        if (!this.hasMoreOptions()) {
                                                                           G.v().out.println("No value given for option -" + option);
                                                                           return false;
                                                                        }

                                                                        value = this.nextOption();
                                                                        if (value.equals("pedantic")) {
                                                                           if (this.throw_analysis != 0 && this.throw_analysis != 1) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.throw_analysis = 1;
                                                                        } else if (value.equals("unit")) {
                                                                           if (this.throw_analysis != 0 && this.throw_analysis != 2) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.throw_analysis = 2;
                                                                        } else {
                                                                           if (!value.equals("dalvik")) {
                                                                              G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                                              return false;
                                                                           }

                                                                           if (this.throw_analysis != 0 && this.throw_analysis != 3) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.throw_analysis = 3;
                                                                        }
                                                                     } else if (!option.equals("check-init-ta") && !option.equals("check-init-throw-analysis")) {
                                                                        if (option.equals("omit-excepting-unit-edges")) {
                                                                           this.omit_excepting_unit_edges = true;
                                                                        } else if (option.equals("trim-cfgs")) {
                                                                           this.pushOption("enabled:true");
                                                                           this.pushOption("jb.tt");
                                                                           this.pushOption("-p");
                                                                           this.pushOption("-omit-excepting-unit-edges");
                                                                           this.pushOption("unit");
                                                                           this.pushOption("-throw-analysis");
                                                                        } else if (!option.equals("ire") && !option.equals("ignore-resolution-errors")) {
                                                                           if (!option.equals("i") && !option.equals("include")) {
                                                                              if (!option.equals("x") && !option.equals("exclude")) {
                                                                                 if (option.equals("include-all")) {
                                                                                    this.include_all = true;
                                                                                 } else if (option.equals("dynamic-class")) {
                                                                                    if (!this.hasMoreOptions()) {
                                                                                       G.v().out.println("No value given for option -" + option);
                                                                                       return false;
                                                                                    }

                                                                                    value = this.nextOption();
                                                                                    if (this.dynamic_class == null) {
                                                                                       this.dynamic_class = new LinkedList();
                                                                                    }

                                                                                    this.dynamic_class.add(value);
                                                                                 } else if (option.equals("dynamic-dir")) {
                                                                                    if (!this.hasMoreOptions()) {
                                                                                       G.v().out.println("No value given for option -" + option);
                                                                                       return false;
                                                                                    }

                                                                                    value = this.nextOption();
                                                                                    if (this.dynamic_dir == null) {
                                                                                       this.dynamic_dir = new LinkedList();
                                                                                    }

                                                                                    this.dynamic_dir.add(value);
                                                                                 } else if (option.equals("dynamic-package")) {
                                                                                    if (!this.hasMoreOptions()) {
                                                                                       G.v().out.println("No value given for option -" + option);
                                                                                       return false;
                                                                                    }

                                                                                    value = this.nextOption();
                                                                                    if (this.dynamic_package == null) {
                                                                                       this.dynamic_package = new LinkedList();
                                                                                    }

                                                                                    this.dynamic_package.add(value);
                                                                                 } else if (option.equals("keep-line-number")) {
                                                                                    this.keep_line_number = true;
                                                                                 } else if (!option.equals("keep-bytecode-offset") && !option.equals("keep-offset")) {
                                                                                    if (option.equals("write-local-annotations")) {
                                                                                       this.write_local_annotations = true;
                                                                                    } else if (option.equals("annot-purity")) {
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("wjap.purity");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("cg.spark");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("-w");
                                                                                    } else if (option.equals("annot-nullpointer")) {
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("tag.an");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("jap.npc");
                                                                                       this.pushOption("-p");
                                                                                    } else if (option.equals("annot-arraybounds")) {
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("tag.an");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("jap.abc");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("wjap.ra");
                                                                                       this.pushOption("-p");
                                                                                    } else if (option.equals("annot-side-effect")) {
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("tag.dep");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("jap.sea");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("-w");
                                                                                    } else if (option.equals("annot-fieldrw")) {
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("tag.fieldrw");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("enabled:true");
                                                                                       this.pushOption("jap.fieldrw");
                                                                                       this.pushOption("-p");
                                                                                       this.pushOption("-w");
                                                                                    } else if (option.equals("time")) {
                                                                                       this.time = true;
                                                                                    } else if (option.equals("subtract-gc")) {
                                                                                       this.subtract_gc = true;
                                                                                    } else {
                                                                                       if (!option.equals("no-writeout-body-releasing")) {
                                                                                          G.v().out.println("Invalid option -" + option);
                                                                                          return false;
                                                                                       }

                                                                                       this.no_writeout_body_releasing = true;
                                                                                    }
                                                                                 } else {
                                                                                    this.keep_offset = true;
                                                                                 }
                                                                              } else {
                                                                                 if (!this.hasMoreOptions()) {
                                                                                    G.v().out.println("No value given for option -" + option);
                                                                                    return false;
                                                                                 }

                                                                                 value = this.nextOption();
                                                                                 if (this.exclude == null) {
                                                                                    this.exclude = new LinkedList();
                                                                                 }

                                                                                 this.exclude.add(value);
                                                                              }
                                                                           } else {
                                                                              if (!this.hasMoreOptions()) {
                                                                                 G.v().out.println("No value given for option -" + option);
                                                                                 return false;
                                                                              }

                                                                              value = this.nextOption();
                                                                              if (this.include == null) {
                                                                                 this.include = new LinkedList();
                                                                              }

                                                                              this.include.add(value);
                                                                           }
                                                                        } else {
                                                                           this.ignore_resolution_errors = true;
                                                                        }
                                                                     } else {
                                                                        if (!this.hasMoreOptions()) {
                                                                           G.v().out.println("No value given for option -" + option);
                                                                           return false;
                                                                        }

                                                                        value = this.nextOption();
                                                                        if (value.equals("auto")) {
                                                                           if (this.check_init_throw_analysis != 0 && this.check_init_throw_analysis != 1) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.check_init_throw_analysis = 1;
                                                                        } else if (value.equals("pedantic")) {
                                                                           if (this.check_init_throw_analysis != 0 && this.check_init_throw_analysis != 2) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.check_init_throw_analysis = 2;
                                                                        } else if (value.equals("unit")) {
                                                                           if (this.check_init_throw_analysis != 0 && this.check_init_throw_analysis != 3) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.check_init_throw_analysis = 3;
                                                                        } else {
                                                                           if (!value.equals("dalvik")) {
                                                                              G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                                              return false;
                                                                           }

                                                                           if (this.check_init_throw_analysis != 0 && this.check_init_throw_analysis != 4) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.check_init_throw_analysis = 4;
                                                                        }
                                                                     }
                                                                  } else {
                                                                     this.pushOption("-O");
                                                                     this.pushOption("-w");
                                                                     this.pushOption("enabled:true");
                                                                     this.pushOption("wsop");
                                                                     this.pushOption("-p");
                                                                     this.pushOption("enabled:true");
                                                                     this.pushOption("wjop");
                                                                     this.pushOption("-p");
                                                                  }
                                                               } else {
                                                                  this.pushOption("enabled:true");
                                                                  this.pushOption("sop");
                                                                  this.pushOption("-p");
                                                                  this.pushOption("enabled:true");
                                                                  this.pushOption("jop");
                                                                  this.pushOption("-p");
                                                                  this.pushOption("enabled:true");
                                                                  this.pushOption("gop");
                                                                  this.pushOption("-p");
                                                                  this.pushOption("enabled:true");
                                                                  this.pushOption("bop");
                                                                  this.pushOption("-p");
                                                                  this.pushOption("only-stack-locals:false");
                                                                  this.pushOption("gb.a2");
                                                                  this.pushOption("-p");
                                                                  this.pushOption("only-stack-locals:false");
                                                                  this.pushOption("gb.a1");
                                                                  this.pushOption("-p");
                                                               }
                                                            } else {
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No phase name given for option -" + option);
                                                                  return false;
                                                               }

                                                               value = this.nextOption();
                                                               if (!this.hasMoreOptions()) {
                                                                  G.v().out.println("No phase option given for option -" + option + " " + value);
                                                                  return false;
                                                               }

                                                               phaseOption = this.nextOption();
                                                               phaseOptions.add(value);
                                                               phaseOptions.add(phaseOption);
                                                            }
                                                         } else {
                                                            this.print_tags_in_output = true;
                                                         }
                                                      } else {
                                                         this.output_jar = true;
                                                      }
                                                   } else {
                                                      if (!this.hasMoreOptions()) {
                                                         G.v().out.println("No value given for option -" + option);
                                                         return false;
                                                      }

                                                      value = this.nextOption();
                                                      if (!value.equals("J") && !value.equals("jimple")) {
                                                         if (!value.equals("j") && !value.equals("jimp")) {
                                                            if (!value.equals("S") && !value.equals("shimple")) {
                                                               if (!value.equals("s") && !value.equals("shimp")) {
                                                                  if (!value.equals("B") && !value.equals("baf")) {
                                                                     if (value.equals("b")) {
                                                                        if (this.output_format != 0 && this.output_format != 6) {
                                                                           G.v().out.println("Multiple values given for option " + option);
                                                                           return false;
                                                                        }

                                                                        this.output_format = 6;
                                                                     } else if (!value.equals("G") && !value.equals("grimple")) {
                                                                        if (!value.equals("g") && !value.equals("grimp")) {
                                                                           if (!value.equals("X") && !value.equals("xml")) {
                                                                              if (value.equals("dex")) {
                                                                                 if (this.output_format != 0 && this.output_format != 10) {
                                                                                    G.v().out.println("Multiple values given for option " + option);
                                                                                    return false;
                                                                                 }

                                                                                 this.output_format = 10;
                                                                              } else if (value.equals("force-dex")) {
                                                                                 if (this.output_format != 0 && this.output_format != 11) {
                                                                                    G.v().out.println("Multiple values given for option " + option);
                                                                                    return false;
                                                                                 }

                                                                                 this.output_format = 11;
                                                                              } else if (!value.equals("n") && !value.equals("none")) {
                                                                                 if (value.equals("jasmin")) {
                                                                                    if (this.output_format != 0 && this.output_format != 13) {
                                                                                       G.v().out.println("Multiple values given for option " + option);
                                                                                       return false;
                                                                                    }

                                                                                    this.output_format = 13;
                                                                                 } else if (!value.equals("c") && !value.equals("class")) {
                                                                                    if (!value.equals("d") && !value.equals("dava")) {
                                                                                       if (!value.equals("t") && !value.equals("template")) {
                                                                                          if (!value.equals("a") && !value.equals("asm")) {
                                                                                             G.v().out.println(String.format("Invalid value %s given for option -%s", option, value));
                                                                                             return false;
                                                                                          }

                                                                                          if (this.output_format != 0 && this.output_format != 17) {
                                                                                             G.v().out.println("Multiple values given for option " + option);
                                                                                             return false;
                                                                                          }

                                                                                          this.output_format = 17;
                                                                                       } else {
                                                                                          if (this.output_format != 0 && this.output_format != 16) {
                                                                                             G.v().out.println("Multiple values given for option " + option);
                                                                                             return false;
                                                                                          }

                                                                                          this.output_format = 16;
                                                                                       }
                                                                                    } else {
                                                                                       if (this.output_format != 0 && this.output_format != 15) {
                                                                                          G.v().out.println("Multiple values given for option " + option);
                                                                                          return false;
                                                                                       }

                                                                                       this.output_format = 15;
                                                                                    }
                                                                                 } else {
                                                                                    if (this.output_format != 0 && this.output_format != 14) {
                                                                                       G.v().out.println("Multiple values given for option " + option);
                                                                                       return false;
                                                                                    }

                                                                                    this.output_format = 14;
                                                                                 }
                                                                              } else {
                                                                                 if (this.output_format != 0 && this.output_format != 12) {
                                                                                    G.v().out.println("Multiple values given for option " + option);
                                                                                    return false;
                                                                                 }

                                                                                 this.output_format = 12;
                                                                              }
                                                                           } else {
                                                                              if (this.output_format != 0 && this.output_format != 9) {
                                                                                 G.v().out.println("Multiple values given for option " + option);
                                                                                 return false;
                                                                              }

                                                                              this.output_format = 9;
                                                                           }
                                                                        } else {
                                                                           if (this.output_format != 0 && this.output_format != 8) {
                                                                              G.v().out.println("Multiple values given for option " + option);
                                                                              return false;
                                                                           }

                                                                           this.output_format = 8;
                                                                        }
                                                                     } else {
                                                                        if (this.output_format != 0 && this.output_format != 7) {
                                                                           G.v().out.println("Multiple values given for option " + option);
                                                                           return false;
                                                                        }

                                                                        this.output_format = 7;
                                                                     }
                                                                  } else {
                                                                     if (this.output_format != 0 && this.output_format != 5) {
                                                                        G.v().out.println("Multiple values given for option " + option);
                                                                        return false;
                                                                     }

                                                                     this.output_format = 5;
                                                                  }
                                                               } else {
                                                                  if (this.output_format != 0 && this.output_format != 4) {
                                                                     G.v().out.println("Multiple values given for option " + option);
                                                                     return false;
                                                                  }

                                                                  this.output_format = 4;
                                                               }
                                                            } else {
                                                               if (this.output_format != 0 && this.output_format != 3) {
                                                                  G.v().out.println("Multiple values given for option " + option);
                                                                  return false;
                                                               }

                                                               this.output_format = 3;
                                                            }
                                                         } else {
                                                            if (this.output_format != 0 && this.output_format != 2) {
                                                               G.v().out.println("Multiple values given for option " + option);
                                                               return false;
                                                            }

                                                            this.output_format = 2;
                                                         }
                                                      } else {
                                                         if (this.output_format != 0 && this.output_format != 1) {
                                                            G.v().out.println("Multiple values given for option " + option);
                                                            return false;
                                                         }

                                                         this.output_format = 1;
                                                      }
                                                   }
                                                } else {
                                                   if (!this.hasMoreOptions()) {
                                                      G.v().out.println("No value given for option -" + option);
                                                      return false;
                                                   }

                                                   value = this.nextOption();
                                                   if (!this.output_dir.isEmpty()) {
                                                      G.v().out.println("Duplicate values " + this.output_dir + " and " + value + " for option -" + option);
                                                      return false;
                                                   }

                                                   this.output_dir = value;
                                                }
                                             } else {
                                                if (!this.hasMoreOptions()) {
                                                   G.v().out.println("No value given for option -" + option);
                                                   return false;
                                                }

                                                value = this.nextOption();
                                                if (this.process_dir == null) {
                                                   this.process_dir = new LinkedList();
                                                }

                                                this.process_dir.add(value);
                                             }
                                          } else {
                                             this.ignore_classpath_errors = true;
                                          }
                                       } else {
                                          this.prepend_classpath = true;
                                       }
                                    } else {
                                       if (!this.hasMoreOptions()) {
                                          G.v().out.println("No value given for option -" + option);
                                          return false;
                                       }

                                       value = this.nextOption();
                                       if (!this.soot_classpath.isEmpty()) {
                                          G.v().out.println("Duplicate values " + this.soot_classpath + " and " + value + " for option -" + option);
                                          return false;
                                       }

                                       this.soot_classpath = value;
                                    }
                                 } else {
                                    this.on_the_fly = true;
                                 }
                              } else {
                                 this.whole_shimple = true;
                              }
                           } else {
                              this.whole_program = true;
                           }
                        } else {
                           this.verbose = true;
                        }
                     } else {
                        if (!this.hasMoreOptions()) {
                           G.v().out.println("No value given for option -" + option);
                           return false;
                        }

                        value = this.nextOption();
                        if (this.phase_help == null) {
                           this.phase_help = new LinkedList();
                        }

                        this.phase_help.add(value);
                     }
                  } else {
                     this.phase_list = true;
                  }
               } else {
                  this.help = true;
               }
            }
         }

         Iterator it = phaseOptions.iterator();

         do {
            if (!it.hasNext()) {
               it = phaseOptions.iterator();

               do {
                  if (!it.hasNext()) {
                     return true;
                  }

                  value = (String)it.next();
                  phaseOption = (String)it.next();
               } while(this.setPhaseOption(value, phaseOption));

               return false;
            }

            value = (String)it.next();
            phaseOption = (String)it.next();
         } while(this.setPhaseOption(value, "enabled:true"));

         return false;
      }
   }

   public boolean coffi() {
      return this.coffi;
   }

   public void set_coffi(boolean setting) {
      this.coffi = setting;
   }

   public boolean jasmin_backend() {
      return this.jasmin_backend;
   }

   public void set_jasmin_backend(boolean setting) {
      this.jasmin_backend = setting;
   }

   public boolean help() {
      return this.help;
   }

   public void set_help(boolean setting) {
      this.help = setting;
   }

   public boolean phase_list() {
      return this.phase_list;
   }

   public void set_phase_list(boolean setting) {
      this.phase_list = setting;
   }

   public List<String> phase_help() {
      return this.phase_help == null ? Collections.emptyList() : this.phase_help;
   }

   public void set_phase_help(List<String> setting) {
      this.phase_help = setting;
   }

   public boolean version() {
      return this.version;
   }

   public void set_version(boolean setting) {
      this.version = setting;
   }

   public boolean verbose() {
      return this.verbose;
   }

   public void set_verbose(boolean setting) {
      this.verbose = setting;
   }

   public boolean interactive_mode() {
      return this.interactive_mode;
   }

   public void set_interactive_mode(boolean setting) {
      this.interactive_mode = setting;
   }

   public boolean unfriendly_mode() {
      return this.unfriendly_mode;
   }

   public void set_unfriendly_mode(boolean setting) {
      this.unfriendly_mode = setting;
   }

   public boolean app() {
      return this.app;
   }

   public void set_app(boolean setting) {
      this.app = setting;
   }

   public boolean whole_program() {
      return this.whole_program;
   }

   public void set_whole_program(boolean setting) {
      this.whole_program = setting;
   }

   public boolean whole_shimple() {
      return this.whole_shimple;
   }

   public void set_whole_shimple(boolean setting) {
      this.whole_shimple = setting;
   }

   public boolean on_the_fly() {
      return this.on_the_fly;
   }

   public void set_on_the_fly(boolean setting) {
      this.on_the_fly = setting;
   }

   public boolean validate() {
      return this.validate;
   }

   public void set_validate(boolean setting) {
      this.validate = setting;
   }

   public boolean debug() {
      return this.debug;
   }

   public void set_debug(boolean setting) {
      this.debug = setting;
   }

   public boolean debug_resolver() {
      return this.debug_resolver;
   }

   public void set_debug_resolver(boolean setting) {
      this.debug_resolver = setting;
   }

   public boolean ignore_resolving_levels() {
      return this.ignore_resolving_levels;
   }

   public void set_ignore_resolving_levels(boolean setting) {
      this.ignore_resolving_levels = setting;
   }

   public String soot_classpath() {
      return this.soot_classpath;
   }

   public void set_soot_classpath(String setting) {
      this.soot_classpath = setting;
   }

   public boolean prepend_classpath() {
      return this.prepend_classpath;
   }

   public void set_prepend_classpath(boolean setting) {
      this.prepend_classpath = setting;
   }

   public boolean ignore_classpath_errors() {
      return this.ignore_classpath_errors;
   }

   public void set_ignore_classpath_errors(boolean setting) {
      this.ignore_classpath_errors = setting;
   }

   public boolean process_multiple_dex() {
      return this.process_multiple_dex;
   }

   public void set_process_multiple_dex(boolean setting) {
      this.process_multiple_dex = setting;
   }

   public boolean search_dex_in_archives() {
      return this.search_dex_in_archives;
   }

   public void set_search_dex_in_archives(boolean setting) {
      this.search_dex_in_archives = setting;
   }

   public List<String> process_dir() {
      return this.process_dir == null ? Collections.emptyList() : this.process_dir;
   }

   public void set_process_dir(List<String> setting) {
      this.process_dir = setting;
   }

   public boolean oaat() {
      return this.oaat;
   }

   public void set_oaat(boolean setting) {
      this.oaat = setting;
   }

   public String android_jars() {
      return this.android_jars;
   }

   public void set_android_jars(String setting) {
      this.android_jars = setting;
   }

   public String force_android_jar() {
      return this.force_android_jar;
   }

   public void set_force_android_jar(String setting) {
      this.force_android_jar = setting;
   }

   public int android_api_version() {
      return this.android_api_version;
   }

   public void set_android_api_version(int setting) {
      this.android_api_version = setting;
   }

   public boolean ast_metrics() {
      return this.ast_metrics;
   }

   public void set_ast_metrics(boolean setting) {
      this.ast_metrics = setting;
   }

   public int src_prec() {
      return this.src_prec == 0 ? 1 : this.src_prec;
   }

   public void set_src_prec(int setting) {
      this.src_prec = setting;
   }

   public boolean full_resolver() {
      return this.full_resolver;
   }

   public void set_full_resolver(boolean setting) {
      this.full_resolver = setting;
   }

   public boolean allow_phantom_refs() {
      return this.allow_phantom_refs;
   }

   public void set_allow_phantom_refs(boolean setting) {
      this.allow_phantom_refs = setting;
   }

   public boolean no_bodies_for_excluded() {
      return this.no_bodies_for_excluded;
   }

   public void set_no_bodies_for_excluded(boolean setting) {
      this.no_bodies_for_excluded = setting;
   }

   public boolean j2me() {
      return this.j2me;
   }

   public void set_j2me(boolean setting) {
      this.j2me = setting;
   }

   public String main_class() {
      return this.main_class;
   }

   public void set_main_class(String setting) {
      this.main_class = setting;
   }

   public boolean polyglot() {
      return this.polyglot;
   }

   public void set_polyglot(boolean setting) {
      this.polyglot = setting;
   }

   public boolean permissive_resolving() {
      return this.permissive_resolving;
   }

   public void set_permissive_resolving(boolean setting) {
      this.permissive_resolving = setting;
   }

   public boolean drop_bodies_after_load() {
      return this.drop_bodies_after_load;
   }

   public void set_drop_bodies_after_load(boolean setting) {
      this.drop_bodies_after_load = setting;
   }

   public String output_dir() {
      return this.output_dir;
   }

   public void set_output_dir(String setting) {
      this.output_dir = setting;
   }

   public int output_format() {
      return this.output_format == 0 ? 14 : this.output_format;
   }

   public void set_output_format(int setting) {
      this.output_format = setting;
   }

   public int java_version() {
      return this.java_version;
   }

   public void set_java_version(int setting) {
      this.java_version = setting;
   }

   public boolean output_jar() {
      return this.output_jar;
   }

   public void set_output_jar(boolean setting) {
      this.output_jar = setting;
   }

   public boolean xml_attributes() {
      return this.xml_attributes;
   }

   public void set_xml_attributes(boolean setting) {
      this.xml_attributes = setting;
   }

   public boolean print_tags_in_output() {
      return this.print_tags_in_output;
   }

   public void set_print_tags_in_output(boolean setting) {
      this.print_tags_in_output = setting;
   }

   public boolean no_output_source_file_attribute() {
      return this.no_output_source_file_attribute;
   }

   public void set_no_output_source_file_attribute(boolean setting) {
      this.no_output_source_file_attribute = setting;
   }

   public boolean no_output_inner_classes_attribute() {
      return this.no_output_inner_classes_attribute;
   }

   public void set_no_output_inner_classes_attribute(boolean setting) {
      this.no_output_inner_classes_attribute = setting;
   }

   public List<String> dump_body() {
      return this.dump_body == null ? Collections.emptyList() : this.dump_body;
   }

   public void set_dump_body(List<String> setting) {
      this.dump_body = setting;
   }

   public List<String> dump_cfg() {
      return this.dump_cfg == null ? Collections.emptyList() : this.dump_cfg;
   }

   public void set_dump_cfg(List<String> setting) {
      this.dump_cfg = setting;
   }

   public boolean show_exception_dests() {
      return this.show_exception_dests;
   }

   public void set_show_exception_dests(boolean setting) {
      this.show_exception_dests = setting;
   }

   public boolean gzip() {
      return this.gzip;
   }

   public void set_gzip(boolean setting) {
      this.gzip = setting;
   }

   public boolean force_overwrite() {
      return this.force_overwrite;
   }

   public void set_force_overwrite(boolean setting) {
      this.force_overwrite = setting;
   }

   public List<String> plugin() {
      return this.plugin == null ? Collections.emptyList() : this.plugin;
   }

   public void set_plugin(List<String> setting) {
      this.plugin = setting;
   }

   public int wrong_staticness() {
      if (this.wrong_staticness == 0) {
         return 3;
      } else {
         return this.wrong_staticness == 0 ? 4 : this.wrong_staticness;
      }
   }

   public void set_wrong_staticness(int setting) {
      this.wrong_staticness = setting;
   }

   public int field_type_mismatches() {
      return this.field_type_mismatches == 0 ? 3 : this.field_type_mismatches;
   }

   public void set_field_type_mismatches(int setting) {
      this.field_type_mismatches = setting;
   }

   public boolean via_grimp() {
      return this.via_grimp;
   }

   public void set_via_grimp(boolean setting) {
      this.via_grimp = setting;
   }

   public boolean via_shimple() {
      return this.via_shimple;
   }

   public void set_via_shimple(boolean setting) {
      this.via_shimple = setting;
   }

   public int throw_analysis() {
      return this.throw_analysis == 0 ? 2 : this.throw_analysis;
   }

   public void set_throw_analysis(int setting) {
      this.throw_analysis = setting;
   }

   public int check_init_throw_analysis() {
      return this.check_init_throw_analysis == 0 ? 1 : this.check_init_throw_analysis;
   }

   public void set_check_init_throw_analysis(int setting) {
      this.check_init_throw_analysis = setting;
   }

   public boolean omit_excepting_unit_edges() {
      return this.omit_excepting_unit_edges;
   }

   public void set_omit_excepting_unit_edges(boolean setting) {
      this.omit_excepting_unit_edges = setting;
   }

   public boolean ignore_resolution_errors() {
      return this.ignore_resolution_errors;
   }

   public void set_ignore_resolution_errors(boolean setting) {
      this.ignore_resolution_errors = setting;
   }

   public List<String> include() {
      return this.include == null ? Collections.emptyList() : this.include;
   }

   public void set_include(List<String> setting) {
      this.include = setting;
   }

   public List<String> exclude() {
      return this.exclude == null ? Collections.emptyList() : this.exclude;
   }

   public void set_exclude(List<String> setting) {
      this.exclude = setting;
   }

   public boolean include_all() {
      return this.include_all;
   }

   public void set_include_all(boolean setting) {
      this.include_all = setting;
   }

   public List<String> dynamic_class() {
      return this.dynamic_class == null ? Collections.emptyList() : this.dynamic_class;
   }

   public void set_dynamic_class(List<String> setting) {
      this.dynamic_class = setting;
   }

   public List<String> dynamic_dir() {
      return this.dynamic_dir == null ? Collections.emptyList() : this.dynamic_dir;
   }

   public void set_dynamic_dir(List<String> setting) {
      this.dynamic_dir = setting;
   }

   public List<String> dynamic_package() {
      return this.dynamic_package == null ? Collections.emptyList() : this.dynamic_package;
   }

   public void set_dynamic_package(List<String> setting) {
      this.dynamic_package = setting;
   }

   public boolean keep_line_number() {
      return this.keep_line_number;
   }

   public void set_keep_line_number(boolean setting) {
      this.keep_line_number = setting;
   }

   public boolean keep_offset() {
      return this.keep_offset;
   }

   public void set_keep_offset(boolean setting) {
      this.keep_offset = setting;
   }

   public boolean write_local_annotations() {
      return this.write_local_annotations;
   }

   public void set_write_local_annotations(boolean setting) {
      this.write_local_annotations = setting;
   }

   public boolean time() {
      return this.time;
   }

   public void set_time(boolean setting) {
      this.time = setting;
   }

   public boolean subtract_gc() {
      return this.subtract_gc;
   }

   public void set_subtract_gc(boolean setting) {
      this.subtract_gc = setting;
   }

   public boolean no_writeout_body_releasing() {
      return this.no_writeout_body_releasing;
   }

   public void set_no_writeout_body_releasing(boolean setting) {
      this.no_writeout_body_releasing = setting;
   }

   public String getUsage() {
      return "\nGeneral Options:\n" + this.padOpt("-coffi", "Use the good old Coffi front end for parsing Java bytecode (instead of using ASM).") + this.padOpt("-jasmin-backend", "Use the Jasmin back end for generating Java bytecode (instead of using ASM).") + this.padOpt("-h, -help", "Display help and exit") + this.padOpt("-pl, -phase-list", "Print list of available phases") + this.padOpt("-ph ARG -phase-help ARG", "Print help for specified ARG") + this.padOpt("-version", "Display version information and exit") + this.padOpt("-v, -verbose", "Verbose mode") + this.padOpt("-interactive-mode", "Run in interactive mode") + this.padOpt("-unfriendly-mode", "Allow Soot to run with no command-line options") + this.padOpt("-app", "Run in application mode") + this.padOpt("-w, -whole-program", "Run in whole-program mode") + this.padOpt("-ws, -whole-shimple", "Run in whole-shimple mode") + this.padOpt("-fly, -on-the-fly", "Run in on-the-fly mode") + this.padOpt("-validate", "Run internal validation on bodies") + this.padOpt("-debug", "Print various Soot debugging info") + this.padOpt("-debug-resolver", "Print debugging info from SootResolver") + this.padOpt("-ignore-resolving-levels", "Ignore mismatching resolving levels") + "\nInput Options:\n" + this.padOpt("-cp ARG -soot-class-path ARG -soot-classpath ARG", "Use ARG as the classpath for finding classes.") + this.padOpt("-pp, -prepend-classpath", "Prepend the given soot classpath to the default classpath.") + this.padOpt("-ice, -ignore-classpath-errors", "Ignores invalid entries on the Soot classpath.") + this.padOpt("-process-multiple-dex", "Process all DEX files found in APK.") + this.padOpt("-search-dex-in-archives", "Also includes Jar and Zip files when searching for DEX files under the provided classpath.") + this.padOpt("-process-path ARG -process-dir ARG", "Process all classes found in ARG") + this.padOpt("-oaat", "From the process-dir, processes one class at a time.") + this.padOpt("-android-jars ARG", "Use ARG as the path for finding the android.jar file") + this.padOpt("-force-android-jar ARG", "Force Soot to use ARG as the path for the android.jar file.") + this.padOpt("-ast-metrics", "Compute AST Metrics if performing java to jimple") + this.padOpt("-src-prec ARG", "Sets source precedence to ARG files") + this.padVal("c class (default)", "Favour class files as Soot source") + this.padVal("only-class", "Use only class files as Soot source") + this.padVal("J jimple", "Favour Jimple files as Soot source") + this.padVal("java", "Favour Java files as Soot source") + this.padVal("apk", "Favour APK files as Soot source") + this.padVal("apk-class-jimple apk-c-j", "Favour APK files as Soot source, disregard Java files") + this.padOpt("-full-resolver", "Force transitive resolving of referenced classes") + this.padOpt("-allow-phantom-refs", "Allow unresolved classes; may cause errors") + this.padOpt("-no-bodies-for-excluded", "Do not load bodies for excluded classes") + this.padOpt("-j2me", "Use J2ME mode; changes assignment of types") + this.padOpt("-main-class ARG", "Sets the main class for whole-program analysis.") + this.padOpt("-polyglot", "Use Java 1.4 Polyglot frontend instead of JastAdd") + this.padOpt("-permissive-resolving", "Use alternative sources when classes cannot be found using the normal resolving strategy") + this.padOpt("-drop-bodies-after-load", "Drop the method source after it has served its purpose of loading the method body") + "\nOutput Options:\n" + this.padOpt("-d ARG -output-dir ARG", "Store output files in ARG") + this.padOpt("-f ARG -output-format ARG", "Set output format for Soot") + this.padVal("J jimple", "Produce .jimple Files") + this.padVal("j jimp", "Produce .jimp (abbreviated Jimple) files") + this.padVal("S shimple", "Produce .shimple files") + this.padVal("s shimp", "Produce .shimp (abbreviated Shimple) files") + this.padVal("B baf", "Produce .baf files") + this.padVal("b", "Produce .b (abbreviated Baf) files") + this.padVal("G grimple", "Produce .grimple files") + this.padVal("g grimp", "Produce .grimp (abbreviated Grimp) files") + this.padVal("X xml", "Produce .xml Files") + this.padVal("dex", "Produce Dalvik Virtual Machine files") + this.padVal("force-dex", "Produce Dalvik DEX files") + this.padVal("n none", "Produce no output") + this.padVal("jasmin", "Produce .jasmin files") + this.padVal("c class (default)", "Produce .class Files") + this.padVal("d dava", "Produce dava-decompiled .java files") + this.padVal("t template", "Produce .java files with Jimple templates.") + this.padVal("a asm", "Produce .asm files as textual bytecode representation generated with the ASM back end.") + this.padOpt("-java-version ARG", "Force Java version of bytecode generated by Soot.") + this.padVal("default", "Let Soot determine Java version of generated bytecode.") + this.padVal("1.1 1", "Force Java 1.1 as output version.") + this.padVal("1.2 2", "Force Java 1.2 as output version.") + this.padVal("1.3 3", "Force Java 1.3 as output version.") + this.padVal("1.4 4", "Force Java 1.4 as output version.") + this.padVal("1.5 5", "Force Java 1.5 as output version.") + this.padVal("1.6 6", "Force Java 1.6 as output version.") + this.padVal("1.7 7", "Force Java 1.7 as output version.") + this.padVal("1.8 8", "Force Java 1.8 as output version.") + this.padOpt("-outjar, -output-jar", "Make output dir a Jar file instead of dir") + this.padOpt("-xml-attributes", "Save tags to XML attributes for Eclipse") + this.padOpt("-print-tags, -print-tags-in-output", "Print tags in output files after stmt") + this.padOpt("-no-output-source-file-attribute", "Don't output Source File Attribute when producing class files") + this.padOpt("-no-output-inner-classes-attribute", "Don't output inner classes attribute in class files") + this.padOpt("-dump-body ARG", "Dump the internal representation of each method before and after phase ARG") + this.padOpt("-dump-cfg ARG", "Dump the internal representation of each CFG constructed during phase ARG") + this.padOpt("-show-exception-dests", "Include exception destination edges as well as CFG edges in dumped CFGs") + this.padOpt("-gzip", "GZip IR output files") + this.padOpt("-force-overwrite", "Force Overwrite Output Files") + "\nProcessing Options:\n" + this.padOpt("-plugin ARG", "Load all plugins found in ARG") + this.padOpt("-wrong-staticness ARG", "Ignores or fixes errors due to wrong staticness") + this.padVal("fail", "Raise an error when wrong staticness is detected") + this.padVal("ignore", "Ignore errors caused by wrong staticness") + this.padVal("fix (default)", "Transparently fix staticness errors") + this.padVal("fixstrict (default)", "Transparently fix staticness errors, but do not ignore remaining errors") + this.padOpt("-field-type-mismatches ARG", "Specifies how errors shall be handled when resolving field references with mismatching types") + this.padVal("fail", "Raise an error when a field type mismatch is detected") + this.padVal("ignore", "Ignore field type mismatches") + this.padVal("null (default)", "Return null in case of type mismatch") + this.padOpt("-p ARG -phase-option ARG", "Set PHASE 's OPT option to VALUE") + this.padOpt("-O, -optimize", "Perform intraprocedural optimizations") + this.padOpt("-W, -whole-optimize", "Perform whole program optimizations") + this.padOpt("-via-grimp", "Convert to bytecode via Grimp instead of via Baf") + this.padOpt("-via-shimple", "Enable Shimple SSA representation") + this.padOpt("-throw-analysis ARG", "") + this.padVal("pedantic", "Pedantically conservative throw analysis") + this.padVal("unit (default)", "Unit Throw Analysis") + this.padVal("dalvik", "Dalvik Throw Analysis") + this.padOpt("-check-init-ta ARG -check-init-throw-analysis ARG", "") + this.padVal("auto (default)", "Automatically select a throw analysis") + this.padVal("pedantic", "Pedantically conservative throw analysis") + this.padVal("unit", "Unit Throw Analysis") + this.padVal("dalvik", "Dalvik Throw Analysis") + this.padOpt("-omit-excepting-unit-edges", "Omit CFG edges to handlers from excepting units which lack side effects") + this.padOpt("-trim-cfgs", "Trim unrealizable exceptional edges from CFGs") + this.padOpt("-ire, -ignore-resolution-errors", "Does not throw an exception when a program references an undeclared field or method.") + "\nApplication Mode Options:\n" + this.padOpt("-i ARG -include ARG", "Include classes in ARG as application classes") + this.padOpt("-x ARG -exclude ARG", "Exclude classes in ARG from application classes") + this.padOpt("-include-all", "Set default excluded packages to empty list") + this.padOpt("-dynamic-class ARG", "Note that ARG may be loaded dynamically") + this.padOpt("-dynamic-dir ARG", "Mark all classes in ARG as potentially dynamic") + this.padOpt("-dynamic-package ARG", "Marks classes in ARG as potentially dynamic") + "\nInput Attribute Options:\n" + this.padOpt("-keep-line-number", "Keep line number tables") + this.padOpt("-keep-bytecode-offset, -keep-offset", "Attach bytecode offset to IR") + "\nOutput Attribute Options:\n" + this.padOpt("-write-local-annotations", "Write out debug annotations on local names") + "\nAnnotation Options:\n" + this.padOpt("-annot-purity", "Emit purity attributes") + this.padOpt("-annot-nullpointer", "Emit null pointer attributes") + this.padOpt("-annot-arraybounds", "Emit array bounds check attributes") + this.padOpt("-annot-side-effect", "Emit side-effect attributes") + this.padOpt("-annot-fieldrw", "Emit field read/write attributes") + "\nMiscellaneous Options:\n" + this.padOpt("-time", "Report time required for transformations") + this.padOpt("-subtract-gc", "Subtract gc from time") + this.padOpt("-no-writeout-body-releasing", "Disables the release of method bodies after writeout. This flag is used internally.");
   }

   public String getPhaseList() {
      return "" + this.padOpt("jb", "Creates a JimpleBody for each method") + this.padVal("jb.dtr", "Reduces chains of catch-all traps") + this.padVal("jb.ese", "Removes empty switch statements") + this.padVal("jb.ls", "Local splitter: one local per DU-UD web") + this.padVal("jb.a", "Aggregator: removes some unnecessary copies") + this.padVal("jb.ule", "Unused local eliminator") + this.padVal("jb.tr", "Assigns types to locals") + this.padVal("jb.ulp", "Local packer: minimizes number of locals") + this.padVal("jb.lns", "Local name standardizer") + this.padVal("jb.cp", "Copy propagator") + this.padVal("jb.dae", "Dead assignment eliminator") + this.padVal("jb.cp-ule", "Post-copy propagation unused local eliminator") + this.padVal("jb.lp", "Local packer: minimizes number of locals") + this.padVal("jb.ne", "Nop eliminator") + this.padVal("jb.uce", "Unreachable code eliminator") + this.padVal("jb.tt", "Trap Tightener") + this.padOpt("jj", "Creates a JimpleBody for each method directly from source") + this.padVal("jj.ls", "Local splitter: one local per DU-UD web") + this.padVal("jj.a", "Aggregator: removes some unnecessary copies") + this.padVal("jj.ule", "Unused local eliminator") + this.padVal("jj.tr", "Assigns types to locals") + this.padVal("jj.ulp", "Local packer: minimizes number of locals") + this.padVal("jj.lns", "Local name standardizer") + this.padVal("jj.cp", "Copy propagator") + this.padVal("jj.dae", "Dead assignment eliminator") + this.padVal("jj.cp-ule", "Post-copy propagation unused local eliminator") + this.padVal("jj.lp", "Local packer: minimizes number of locals") + this.padVal("jj.ne", "Nop eliminator") + this.padVal("jj.uce", "Unreachable code eliminator") + this.padOpt("wjpp", "Whole Jimple Pre-processing Pack") + this.padVal("wjpp.cimbt", "Replaces base objects of calls to Method.invoke() that are string constants by locals") + this.padOpt("wspp", "Whole Shimple Pre-processing Pack") + this.padOpt("cg", "Call graph constructor") + this.padVal("cg.cha", "Builds call graph using Class Hierarchy Analysis") + this.padVal("cg.spark", "Spark points-to analysis framework") + this.padVal("cg.paddle", "Paddle points-to analysis framework") + this.padOpt("wstp", "Whole-shimple transformation pack") + this.padOpt("wsop", "Whole-shimple optimization pack") + this.padOpt("wjtp", "Whole-jimple transformation pack") + this.padVal("wjtp.mhp", "Determines what statements may be run concurrently") + this.padVal("wjtp.tn", "Finds critical sections, allocates locks") + this.padVal("wjtp.rdc", "Rename duplicated classes when the file system is not case sensitive") + this.padOpt("wjop", "Whole-jimple optimization pack") + this.padVal("wjop.smb", "Static method binder: Devirtualizes monomorphic calls") + this.padVal("wjop.si", "Static inliner: inlines monomorphic calls") + this.padOpt("wjap", "Whole-jimple annotation pack: adds interprocedural tags") + this.padVal("wjap.ra", "Rectangular array finder") + this.padVal("wjap.umt", "Tags all unreachable methods") + this.padVal("wjap.uft", "Tags all unreachable fields") + this.padVal("wjap.tqt", "Tags all qualifiers that could be tighter") + this.padVal("wjap.cgg", "Creates graphical call graph.") + this.padVal("wjap.purity", "Emit purity attributes") + this.padOpt("shimple", "Sets parameters for Shimple SSA form") + this.padOpt("stp", "Shimple transformation pack") + this.padOpt("sop", "Shimple optimization pack") + this.padVal("sop.cpf", "Shimple constant propagator and folder") + this.padOpt("jtp", "Jimple transformation pack: intraprocedural analyses added to Soot") + this.padOpt("jop", "Jimple optimization pack (intraprocedural)") + this.padVal("jop.cse", "Common subexpression eliminator") + this.padVal("jop.bcm", "Busy code motion: unaggressive partial redundancy elimination") + this.padVal("jop.lcm", "Lazy code motion: aggressive partial redundancy elimination") + this.padVal("jop.cp", "Copy propagator") + this.padVal("jop.cpf", "Constant propagator and folder") + this.padVal("jop.cbf", "Conditional branch folder") + this.padVal("jop.dae", "Dead assignment eliminator") + this.padVal("jop.nce", "Null Check Eliminator") + this.padVal("jop.uce1", "Unreachable code eliminator, pass 1") + this.padVal("jop.ubf1", "Unconditional branch folder, pass 1") + this.padVal("jop.uce2", "Unreachable code eliminator, pass 2") + this.padVal("jop.ubf2", "Unconditional branch folder, pass 2") + this.padVal("jop.ule", "Unused local eliminator") + this.padOpt("jap", "Jimple annotation pack: adds intraprocedural tags") + this.padVal("jap.npc", "Null pointer checker") + this.padVal("jap.npcolorer", "Null pointer colourer: tags references for eclipse") + this.padVal("jap.abc", "Array bound checker") + this.padVal("jap.profiling", "Instruments null pointer and array checks") + this.padVal("jap.sea", "Side effect tagger") + this.padVal("jap.fieldrw", "Field read/write tagger") + this.padVal("jap.cgtagger", "Call graph tagger") + this.padVal("jap.parity", "Parity tagger") + this.padVal("jap.pat", "Colour-codes method parameters that may be aliased") + this.padVal("jap.lvtagger", "Creates color tags for live variables") + this.padVal("jap.rdtagger", "Creates link tags for reaching defs") + this.padVal("jap.che", "Indicates whether cast checks can be eliminated") + this.padVal("jap.umt", "Inserts assertions into unreachable methods") + this.padVal("jap.lit", "Tags loop invariants") + this.padVal("jap.aet", "Tags statements with sets of available expressions") + this.padVal("jap.dmt", "Tags dominators of statement") + this.padOpt("gb", "Creates a GrimpBody for each method") + this.padVal("gb.a1", "Aggregator: removes some copies, pre-folding") + this.padVal("gb.cf", "Constructor folder") + this.padVal("gb.a2", "Aggregator: removes some copies, post-folding") + this.padVal("gb.ule", "Unused local eliminator") + this.padOpt("gop", "Grimp optimization pack") + this.padOpt("bb", "Creates Baf bodies") + this.padVal("bb.lso", "Load store optimizer") + this.padVal("bb.sco", "Store chain optimizer") + this.padVal("bb.pho", "Peephole optimizer") + this.padVal("bb.ule", "Unused local eliminator") + this.padVal("bb.lp", "Local packer: minimizes number of locals") + this.padOpt("bop", "Baf optimization pack") + this.padOpt("tag", "Tag aggregator: turns tags into attributes") + this.padVal("tag.ln", "Line number aggregator") + this.padVal("tag.an", "Array bounds and null pointer check aggregator") + this.padVal("tag.dep", "Dependence aggregator") + this.padVal("tag.fieldrw", "Field read/write aggregator") + this.padOpt("db", "Dummy phase to store options for Dava") + this.padVal("db.transformations", "The Dava back-end with all its transformations") + this.padVal("db.renamer", "Apply heuristics based naming of local variables") + this.padVal("db.deobfuscate", "Apply de-obfuscation analyses") + this.padVal("db.force-recompile", "Try to get recompilable code.");
   }

   public String getPhaseHelp(String phaseName) {
      if (phaseName.equals("jb")) {
         return "Phase " + phaseName + ":\n\nJimple Body Creation creates a JimpleBody for each input method, \nusing either coffi, to read .class files, or the jimple parser, \nto read .jimple files.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("use-original-names (false)", "") + this.padOpt("preserve-source-annotations (false)", "") + this.padOpt("stabilize-local-names (false)", "");
      } else if (phaseName.equals("jb.dtr")) {
         return "Phase " + phaseName + ":\n\nThis transformer detects cases in which the same code block is \ncovered by two different catch all traps with different \nexception handlers (A and B), and if there is at the same time a \nthird catch all trap that covers the second handler B and jumps \nto A, then the second trap is unnecessary, because it is already \ncovered by a combination of the other two traps. This \ntransformer removes the unnecessary trap.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jb.ese")) {
         return "Phase " + phaseName + ":\n\nThe Empty Switch Eliminator detects and removes switch \nstatements that have no data labels. Instead, the code is \ntransformed to contain a single jump statement to the default \nlabel.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jb.ls")) {
         return "Phase " + phaseName + ":\n\nThe Local Splitter identifies DU-UD webs for local variables and \nintroduces new variables so that each disjoint web is associated \nwith a single local.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jb.a")) {
         return "Phase " + phaseName + ":\n\nThe Jimple Local Aggregator removes some unnecessary copies by \ncombining local variables. Essentially, it finds definitions \nwhich have only a single use and, if it is safe to do so, \nremoves the original definition after replacing the use with the \ndefinition's right-hand side. At this stage in JimpleBody \nconstruction, local aggregation serves largely to remove the \ncopies to and from stack variables which simulate load and store \ninstructions in the original bytecode.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("jb.ule")) {
         return "Phase " + phaseName + ":\n\nThe Unused Local Eliminator removes any unused locals from the \nmethod.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jb.tr")) {
         return "Phase " + phaseName + ":\n\nThe Type Assigner gives local variables types which will \naccommodate the values stored in them over the course of the \nmethod.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("use-older-type-assigner (false)", "Enables the older type assigner") + this.padOpt("compare-type-assigners (false)", "Compares Ben Bellamy's and the older type assigner") + this.padOpt("ignore-nullpointer-dereferences (false)", "Ignores virtual method calls on base objects that may only be null");
      } else if (phaseName.equals("jb.ulp")) {
         return "Phase " + phaseName + ":\n\nThe Unsplit-originals Local Packer executes only when the \n`use-original-names' option is chosen for the `jb' phase. The \nLocal Packer attempts to minimize the number of local variables \nrequired in a method by reusing the same variable for disjoint \nDU-UD webs. Conceptually, it is the inverse of the Local \nSplitter.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("unsplit-original-locals (true)", "");
      } else if (phaseName.equals("jb.lns")) {
         return "Phase " + phaseName + ":\n\nThe Local Name Standardizer assigns generic names to local \nvariables.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (false)", "") + this.padOpt("sort-locals (false)", "Specifies whether the locals shall be ordered.");
      } else if (phaseName.equals("jb.cp")) {
         return "Phase " + phaseName + ":\n\nThis phase performs cascaded copy propagation. If the propagator \nencounters situations of the form: A: a = ...; ... B: x = a; ... \nC: ... = ... x; where a and x are each defined only once (at A \nand B, respectively), then it can propagate immediately without \nchecking between B and C for redefinitions of a. In this case \nthe propagator is global. Otherwise, if a has multiple \ndefinitions then the propagator checks for redefinitions and \npropagates copies only within extended basic blocks.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-regular-locals (false)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("jb.dae")) {
         return "Phase " + phaseName + ":\n\nThe Dead Assignment Eliminator eliminates assignment statements \nto locals whose values are not subsequently used, unless \nevaluating the right-hand side of the assignment may cause \nside-effects.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("jb.cp-ule")) {
         return "Phase " + phaseName + ":\n\nThis phase removes any locals that are unused after copy \npropagation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jb.lp")) {
         return "Phase " + phaseName + ":\n\nThe Local Packer attempts to minimize the number of local \nvariables required in a method by reusing the same variable for \ndisjoint DU-UD webs. Conceptually, it is the inverse of the \nLocal Splitter.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("unsplit-original-locals (false)", "");
      } else if (phaseName.equals("jb.ne")) {
         return "Phase " + phaseName + ":\n\nThe Nop Eliminator removes nop statements from the method.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jb.uce")) {
         return "Phase " + phaseName + ":\n\nThe Unreachable Code Eliminator removes unreachable code and \ntraps whose catch blocks are empty.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("remove-unreachable-traps (true)", "");
      } else if (phaseName.equals("jb.tt")) {
         return "Phase " + phaseName + ":\n\nThe Trap Tightener changes the area protected by each exception \nhandler, so that it begins with the first instruction in the old \nprotected area which is actually capable of throwing an \nexception caught by the handler, and ends just after the last \ninstruction in the old protected area which can throw an \nexception caught by the handler. This reduces the chance of \nproducing unverifiable code as a byproduct of pruning \nexceptional control flow within CFGs.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jj")) {
         return "Phase " + phaseName + ":\n\nJimple Body Creation creates a JimpleBody for each input method, \nusing polyglot, to read .java files.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("use-original-names (true)", "");
      } else if (phaseName.equals("jj.ls")) {
         return "Phase " + phaseName + ":\n\nThe Local Splitter identifies DU-UD webs for local variables and \nintroduces new variables so that each disjoint web is associated \nwith a single local.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jj.a")) {
         return "Phase " + phaseName + ":\n\nThe Jimple Local Aggregator removes some unnecessary copies by \ncombining local variables. Essentially, it finds definitions \nwhich have only a single use and, if it is safe to do so, \nremoves the original definition after replacing the use with the \ndefinition's right-hand side. At this stage in JimpleBody \nconstruction, local aggregation serves largely to remove the \ncopies to and from stack variables which simulate load and store \ninstructions in the original bytecode.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("jj.ule")) {
         return "Phase " + phaseName + ":\n\nThe Unused Local Eliminator removes any unused locals from the \nmethod.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jj.tr")) {
         return "Phase " + phaseName + ":\n\nThe Type Assigner gives local variables types which will \naccommodate the values stored in them over the course of the \nmethod.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jj.ulp")) {
         return "Phase " + phaseName + ":\n\nThe Unsplit-originals Local Packer executes only when the \n`use-original-names' option is chosen for the `jb' phase. The \nLocal Packer attempts to minimize the number of local variables \nrequired in a method by reusing the same variable for disjoint \nDU-UD webs. Conceptually, it is the inverse of the Local \nSplitter.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("unsplit-original-locals (false)", "");
      } else if (phaseName.equals("jj.lns")) {
         return "Phase " + phaseName + ":\n\nThe Local Name Standardizer assigns generic names to local \nvariables.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (false)", "");
      } else if (phaseName.equals("jj.cp")) {
         return "Phase " + phaseName + ":\n\nThis phase performs cascaded copy propagation. If the propagator \nencounters situations of the form: A: a = ...; ... B: x = a; ... \nC: ... = ... x; where a and x are each defined only once (at A \nand B, respectively), then it can propagate immediately without \nchecking between B and C for redefinitions of a. In this case \nthe propagator is global. Otherwise, if a has multiple \ndefinitions then the propagator checks for redefinitions and \npropagates copies only within extended basic blocks.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-regular-locals (false)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("jj.dae")) {
         return "Phase " + phaseName + ":\n\nThe Dead Assignment Eliminator eliminates assignment statements \nto locals whose values are not subsequently used, unless \nevaluating the right-hand side of the assignment may cause \nside-effects.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("jj.cp-ule")) {
         return "Phase " + phaseName + ":\n\nThis phase removes any locals that are unused after copy \npropagation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jj.lp")) {
         return "Phase " + phaseName + ":\n\nThe Local Packer attempts to minimize the number of local \nvariables required in a method by reusing the same variable for \ndisjoint DU-UD webs. Conceptually, it is the inverse of the \nLocal Splitter.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("unsplit-original-locals (false)", "");
      } else if (phaseName.equals("jj.ne")) {
         return "Phase " + phaseName + ":\n\nThe Nop Eliminator removes nop statements from the method.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jj.uce")) {
         return "Phase " + phaseName + ":\n\nThe Unreachable Code Eliminator removes unreachable code and \ntraps whose catch blocks are empty.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("wjpp")) {
         return "Phase " + phaseName + ":\n\nThis pack allows you to insert pre-processors that are run \nbefore call-graph construction. Only enabled in whole-program \nmode.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("wjpp.cimbt")) {
         return "Phase " + phaseName + ":\n\nWhen using the types-for-invoke option of the cg phase, problems \nmight occur if the base object of a call to Method.invoke() (the \nfirst argument) is a string constant. This option replaces all \nstring constants of such calls by locals and, therefore, allows \nthe static resolution of reflective call targets on constant \nstring objects.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("verbose (false)", "");
      } else if (phaseName.equals("wspp")) {
         return "Phase " + phaseName + ":\n\nThis pack allows you to insert pre-processors that are run \nbefore call-graph construction. Only enabled in whole-program \nShimple mode. In an unmodified copy of Soot, this pack is empty.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("cg")) {
         return "Phase " + phaseName + ":\n\nThe Call Graph Constructor computes a call graph for whole \nprogram analysis. When this pack finishes, a call graph is \navailable in the Scene. The different phases in this pack are \ndifferent ways to construct the call graph. Exactly one phase in \nthis pack must be enabled; Soot will raise an error otherwise.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("safe-forname (false)", "Handle Class.forName() calls conservatively") + this.padOpt("safe-newinstance (false)", "Handle Class.newInstance() calls conservatively") + this.padOpt("library", "Specifies whether the target classes should be treated as an application or a library.") + this.padVal("disabled (default)", "Call(and pointer assignment) graph construction treat the target classes as application starting from the entry points.") + this.padVal("any-subtype", "In this mode types of any accessible field, method parameter, this local, or caugth exception is set to any possible sub type according to the class hierarchy of the target library.") + this.padVal("signature-resolution", "In this mode types of any accessible field, method parameter, this local, or caugth exception is set to any possible sub type according to a possible extended class hierarchy of the target library.") + this.padOpt("verbose (false)", "Print warnings about where the call graph may be incomplete") + this.padOpt("jdkver (3)", "JDK version for native methods") + this.padOpt("all-reachable (false)", "Assume all methods of application classes are reachable.") + this.padOpt("implicit-entry (true)", "Include methods called implicitly by the VM as entry points") + this.padOpt("trim-clinit (true)", "Removes redundant static initializer calls") + this.padOpt("reflection-log", "Uses a reflection log to resolve reflective calls.") + this.padOpt("guards (ignore)", "Describes how to guard the program from unsound assumptions.") + this.padOpt("types-for-invoke (false)", "Uses reaching types inferred by the pointer analysis to resolve reflective calls.");
      } else if (phaseName.equals("cg.cha")) {
         return "Phase " + phaseName + ":\n\nThis phase uses Class Hierarchy Analysis to generate a call \ngraph.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("verbose (false)", "Print statistics about the resulting call graph") + this.padOpt("apponly (false)", "Consider only application classes");
      } else if (phaseName.equals("cg.spark")) {
         return "Phase " + phaseName + ":\n\nSpark is a flexible points-to analysis framework. Aside from \nbuilding a call graph, it also generates information about the \ntargets of pointers. For details about Spark, please see Ondrej \nLhotak's M.Sc. thesis.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("verbose (false)", "Print detailed information about the execution of Spark") + this.padOpt("ignore-types (false)", "Make Spark completely ignore declared types of variables") + this.padOpt("force-gc (false)", "Force garbage collection for measuring memory usage") + this.padOpt("pre-jimplify (false)", "Jimplify all methods before starting Spark") + this.padOpt("apponly (false)", "Consider only application classes") + this.padOpt("vta (false)", "Emulate Variable Type Analysis") + this.padOpt("rta (false)", "Emulate Rapid Type Analysis") + this.padOpt("field-based (false)", "Use a field-based rather than field-sensitive representation") + this.padOpt("types-for-sites (false)", "Represent objects by their actual type rather than allocation site") + this.padOpt("merge-stringbuffer (true)", "Represent all StringBuffers as one object") + this.padOpt("string-constants (false)", "Propagate all string constants, not just class names") + this.padOpt("simulate-natives (true)", "Simulate effects of native methods in standard class library") + this.padOpt("empties-as-allocs (false)", "Treat singletons for empty sets etc. as allocation sites") + this.padOpt("simple-edges-bidirectional (false)", "Equality-based analysis between variable nodes") + this.padOpt("on-fly-cg (true)", "Build call graph as receiver types become known") + this.padOpt("simplify-offline (false)", "Collapse single-entry subgraphs of the PAG") + this.padOpt("simplify-sccs (false)", "Collapse strongly-connected components of the PAG") + this.padOpt("ignore-types-for-sccs (false)", "Ignore declared types when determining node equivalence for SCCs") + this.padOpt("propagator", "Select propagation algorithm") + this.padVal("iter", "Simple iterative algorithm") + this.padVal("worklist (default)", "Fast, worklist-based algorithm") + this.padVal("cycle", "Unfinished on-the-fly cycle detection algorithm") + this.padVal("merge", "Unfinished field reference merging algorithms") + this.padVal("alias", "Alias-edge based algorithm") + this.padVal("none", "Disable propagation") + this.padOpt("set-impl", "Select points-to set implementation") + this.padVal("hash", "Use Java HashSet") + this.padVal("bit", "Bit vector") + this.padVal("hybrid", "Hybrid representation using bit vector for large sets") + this.padVal("array", "Sorted array representation") + this.padVal("heintze", "Heintze's shared bit-vector and overflow list representation") + this.padVal("sharedlist", "Shared list representation") + this.padVal("double (default)", "Double set representation for incremental propagation") + this.padOpt("double-set-old", "Select implementation of points-to set for old part of double set") + this.padVal("hash", "Use Java HashSet") + this.padVal("bit", "Bit vector") + this.padVal("hybrid (default)", "Hybrid representation using bit vector for large sets") + this.padVal("array", "Sorted array representation") + this.padVal("heintze", "Heintze's shared bit-vector and overflow list representation") + this.padVal("sharedlist", "Shared list representation") + this.padOpt("double-set-new", "Select implementation of points-to set for new part of double set") + this.padVal("hash", "Use Java HashSet") + this.padVal("bit", "Bit vector") + this.padVal("hybrid (default)", "Hybrid representation using bit vector for large sets") + this.padVal("array", "Sorted array representation") + this.padVal("heintze", "Heintze's shared bit-vector and overflow list representation") + this.padVal("sharedlist", "Shared list representation") + this.padOpt("dump-html (false)", "Dump pointer assignment graph to HTML for debugging") + this.padOpt("dump-pag (false)", "Dump pointer assignment graph for other solvers") + this.padOpt("dump-solution (false)", "Dump final solution for comparison with other solvers") + this.padOpt("topo-sort (false)", "Sort variable nodes in dump") + this.padOpt("dump-types (true)", "Include declared types in dump") + this.padOpt("class-method-var (true)", "In dump, label variables by class and method") + this.padOpt("dump-answer (false)", "Dump computed reaching types for comparison with other solvers") + this.padOpt("add-tags (false)", "Output points-to results in tags for viewing with the Jimple") + this.padOpt("set-mass (false)", "Calculate statistics about points-to set sizes") + this.padOpt("cs-demand (false)", "After running Spark, refine points-to sets on demand with context information") + this.padOpt("lazy-pts (true)", "Create lazy points-to sets that create context information only when needed.") + this.padOpt("traversal (75000)", "Make the analysis traverse at most this number of nodes per query.") + this.padOpt("passes (10)", "Perform at most this number of refinement iterations.") + this.padOpt("geom-pta (false)", "This switch enables/disables the geometric analysis.") + this.padOpt("geom-encoding (Geom)", "Encoding methodology") + this.padVal("Geom (default)", "Geometric Encoding") + this.padVal("HeapIns", "Heap Insensitive Encoding") + this.padVal("PtIns", "PtIns") + this.padOpt("geom-worklist (PQ)", "Worklist type") + this.padVal("PQ (default)", "Priority Queue") + this.padVal("FIFO", "FIFO Queue") + this.padOpt("geom-dump-verbose ()", "Filename for detailed execution log") + this.padOpt("geom-verify-name ()", "Filename for verification file") + this.padOpt("geom-eval (0)", "Precision evaluation methodologies") + this.padOpt("geom-trans (false)", "Transform to context-insensitive result") + this.padOpt("geom-frac-base (40)", "Fractional parameter for precision/performance trade-off") + this.padOpt("geom-blocking (true)", "Enable blocking strategy for recursive calls") + this.padOpt("geom-runs (1)", "Iterations of analysis") + this.padOpt("geom-app-only (true)", "Processing pointers that impact pointers in application code only");
      } else if (phaseName.equals("cg.paddle")) {
         return "Phase " + phaseName + ":\n\nPaddle is a BDD-based interprocedural analysis framework. It \nincludes points-to analysis, call graph construction, and \nvarious client analyses.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("verbose (false)", "Print detailed information about the execution of Paddle") + this.padOpt("conf", "Select Paddle configuration") + this.padVal("ofcg (default)", "On-the fly call graph") + this.padVal("cha", "CHA only") + this.padVal("cha-aot", "CHA ahead-of-time callgraph") + this.padVal("ofcg-aot", "OFCG-AOT callgraph") + this.padVal("cha-context-aot", "CHA-Context-AOT callgraph") + this.padVal("ofcg-context-aot", "OFCG-Context-AOT callgraph") + this.padVal("cha-context", "CHA-Context callgraph") + this.padVal("ofcg-context", "OFCG-Context callgraph") + this.padOpt("bdd (false)", "Use BDD version of Paddle") + this.padOpt("order (32)", "") + this.padOpt("dynamic-order", "") + this.padOpt("profile (false)", "Profile BDDs using JeddProfiler") + this.padOpt("verbosegc (false)", "Print memory usage at each BDD garbage collection.") + this.padOpt("q", "Select queue implementation") + this.padVal("auto (default)", "Select queue implementation based on bdd option") + this.padVal("trad", "Normal worklist queue implementation") + this.padVal("bdd", "BDD-based queue implementation") + this.padVal("debug", "Debugging queue implementation") + this.padVal("trace", "Tracing queue implementation") + this.padVal("numtrace", "Number-tracing queue implementation") + this.padOpt("backend", "Select BDD backend") + this.padVal("auto (default)", "Select backend based on bdd option") + this.padVal("buddy", "BuDDy backend") + this.padVal("cudd", "CUDD backend") + this.padVal("sable", "SableJBDD backend") + this.padVal("javabdd", "JavaBDD backend") + this.padVal("none", "No BDDs") + this.padOpt("bdd-nodes (0)", "Number of BDD nodes to allocate (0=unlimited)") + this.padOpt("ignore-types (false)", "Make Paddle completely ignore declared types of variables") + this.padOpt("pre-jimplify (false)", "Jimplify all methods before starting Paddle") + this.padOpt("context", "Select context-sensitivity level") + this.padVal("insens (default)", "Builds a context-insensitive call graph") + this.padVal("1cfa", "Builds a 1-CFA call graph") + this.padVal("kcfa", "Builds a k-CFA call graph") + this.padVal("objsens", "Builds an object-sensitive call graph") + this.padVal("kobjsens", "Builds a k-object-sensitive call graph") + this.padVal("uniqkobjsens", "Builds a unique-k-object-sensitive call graph") + this.padVal("threadkobjsens", "Experimental option for thread-entry-point sensitivity") + this.padOpt("k (2)", "") + this.padOpt("context-heap (false)", "Treat allocation sites context-sensitively") + this.padOpt("rta (false)", "Emulate Rapid Type Analysis") + this.padOpt("field-based (false)", "Use a field-based rather than field-sensitive representation") + this.padOpt("types-for-sites (false)", "Represent objects by their actual type rather than allocation site") + this.padOpt("merge-stringbuffer (true)", "Represent all StringBuffers as one object") + this.padOpt("string-constants (false)", "Propagate all string constants, not just class names") + this.padOpt("simulate-natives (true)", "Simulate effects of native methods in standard class library") + this.padOpt("global-nodes-in-natives (false)", "Use global node to model variables in simulations of native methods") + this.padOpt("simple-edges-bidirectional (false)", "Equality-based analysis between variable nodes") + this.padOpt("this-edges (false)", "Use pointer assignment edges to model this parameters") + this.padOpt("precise-newinstance (true)", "Make newInstance only allocate objects of dynamic classes") + this.padOpt("propagator", "Select propagation algorithm") + this.padVal("auto (default)", "Select propagation algorithm based on bdd option") + this.padVal("iter", "Simple iterative algorithm") + this.padVal("worklist", "Fast, worklist-based algorithm") + this.padVal("alias", "Alias-edge based algorithm") + this.padVal("bdd", "BDD-based propagator") + this.padVal("incbdd", "Incrementalized BDD-based propagator") + this.padOpt("set-impl", "Select points-to set implementation") + this.padVal("hash", "Use Java HashSet") + this.padVal("bit", "Bit vector") + this.padVal("hybrid", "Hybrid representation using bit vector for large sets") + this.padVal("array", "Sorted array representation") + this.padVal("heintze", "Heintze's shared bit-vector and overflow list representation") + this.padVal("double (default)", "Double set representation for incremental propagation") + this.padOpt("double-set-old", "Select implementation of points-to set for old part of double set") + this.padVal("hash", "Use Java HashSet") + this.padVal("bit", "Bit vector") + this.padVal("hybrid (default)", "Hybrid representation using bit vector for large sets") + this.padVal("array", "Sorted array representation") + this.padVal("heintze", "Heintze's shared bit-vector and overflow list representation") + this.padOpt("double-set-new", "Select implementation of points-to set for new part of double set") + this.padVal("hash", "Use Java HashSet") + this.padVal("bit", "Bit vector") + this.padVal("hybrid (default)", "Hybrid representation using bit vector for large sets") + this.padVal("array", "Sorted array representation") + this.padVal("heintze", "Heintze's shared bit-vector and overflow list representation") + this.padOpt("context-counts (false)", "Print number of contexts for each method") + this.padOpt("total-context-counts (false)", "Print total number of contexts") + this.padOpt("method-context-counts (false)", "Print number of contexts for each method") + this.padOpt("set-mass (false)", "Calculate statistics about points-to set sizes") + this.padOpt("number-nodes (true)", "Print node numbers in dumps");
      } else if (phaseName.equals("wstp")) {
         return "Phase " + phaseName + ":\n\nSoot can perform whole-program analyses. In whole-shimple mode, \nSoot applies the contents of the Whole-Shimple Transformation \nPack to the scene as a whole after constructing a call graph for \nthe program. In an unmodified copy of Soot the Whole-Shimple \nTransformation Pack is empty.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("wsop")) {
         return "Phase " + phaseName + ":\n\nIf Soot is running in whole shimple mode and the Whole-Shimple \nOptimization Pack is enabled, the pack's transformations are \napplied to the scene as a whole after construction of the call \ngraph and application of any enabled Whole-Shimple \nTransformations. In an unmodified copy of Soot the Whole-Shimple \nOptimization Pack is empty.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjtp")) {
         return "Phase " + phaseName + ":\n\nSoot can perform whole-program analyses. In whole-program mode, \nSoot applies the contents of the Whole-Jimple Transformation \nPack to the scene as a whole after constructing a call graph for \nthe program.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("wjtp.mhp")) {
         return "Phase " + phaseName + ":\n\nMay Happen in Parallel (MHP) Analyses determine what program \nstatements may be run by different threads concurrently. This \nphase does not perform any transformation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjtp.tn")) {
         return "Phase " + phaseName + ":\n\nThe Lock Allocator finds critical sections (synchronized \nregions) in Java programs and assigns locks for execution on \nboth optimistic and pessimistic JVMs. It can also be used to \nanalyze the existing locks.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("locking-scheme", "Selects the granularity of the generated lock allocation") + this.padVal("medium-grained (default)", "Use a runtime object for synchronization where possible") + this.padVal("coarse-grained", "Use static objects for synchronization") + this.padVal("single-static", "Use just one static synchronization object for all transactional regions") + this.padVal("leave-original", "Analyse the existing lock structure without making changes") + this.padOpt("avoid-deadlock (true)", "Perform Deadlock Avoidance") + this.padOpt("open-nesting (true)", "Use an open nesting model") + this.padOpt("do-mhp (true)", "Perform a May-Happen-in-Parallel analysis") + this.padOpt("do-tlo (true)", "Perform a Local-Objects analysis") + this.padOpt("print-graph (false)", "Print topological graph of transactions") + this.padOpt("print-table (false)", "Print table of transactions") + this.padOpt("print-debug (false)", "Print debugging info");
      } else if (phaseName.equals("wjtp.rdc")) {
         return "Phase " + phaseName + ":\n\nRename duplicated classes when the file system is not case \nsensitive. If the file system is case sensitive, this phase does \nnothing.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("fcn fixed-class-names", "Set ARG for the fixed class names.");
      } else if (phaseName.equals("wjop")) {
         return "Phase " + phaseName + ":\n\nIf Soot is running in whole program mode and the Whole-Jimple \nOptimization Pack is enabled, the pack's transformations are \napplied to the scene as a whole after construction of the call \ngraph and application of any enabled Whole-Jimple \nTransformations.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjop.smb")) {
         return "Phase " + phaseName + ":\n\nThe Static Method Binder statically binds monomorphic call \nsites. That is, it searches the call graph for virtual method \ninvocations that can be determined statically to call only a \nsingle implementation of the called method. Then it replaces \nsuch virtual invocations with invocations of a static copy of \nthe single called implementation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("insert-null-checks (true)", "") + this.padOpt("insert-redundant-casts (true)", "") + this.padOpt("allowed-modifier-changes", "") + this.padVal("unsafe (default)", "") + this.padVal("safe", "") + this.padVal("none", "");
      } else if (phaseName.equals("wjop.si")) {
         return "Phase " + phaseName + ":\n\nThe Static Inliner visits all call sites in the call graph in a \nbottom-up fashion, replacing monomorphic calls with inlined \ncopies of the invoked methods.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("rerun-jb (true)", "") + this.padOpt("insert-null-checks (true)", "") + this.padOpt("insert-redundant-casts (true)", "") + this.padOpt("allowed-modifier-changes", "") + this.padVal("unsafe (default)", "") + this.padVal("safe", "") + this.padVal("none", "") + this.padOpt("expansion-factor (3)", "") + this.padOpt("max-container-size (5000)", "") + this.padOpt("max-inlinee-size (20)", "");
      } else if (phaseName.equals("wjap")) {
         return "Phase " + phaseName + ":\n\nSome analyses do not transform Jimple body directly, but \nannotate statements or values with tags. Whole-Jimple annotation \npack provides a place for annotation-oriented analyses in whole \nprogram mode.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("wjap.ra")) {
         return "Phase " + phaseName + ":\n\nThe Rectangular Array Finder traverses Jimple statements based \non the static call graph, and finds array variables which always \nhold rectangular two-dimensional array objects. In Java, a \nmulti-dimensional array is an array of arrays, which means the \nshape of the array can be ragged. Nevertheless, many \napplications use rectangular arrays. Knowing that an array is \nrectangular can be very helpful in proving safe array bounds \nchecks. The Rectangular Array Finder does not change the program \nbeing analyzed. Its results are used by the Array Bound Checker.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjap.umt")) {
         return "Phase " + phaseName + ":\n\nUses the call graph to determine which methods are unreachable \nand adds color tags so they can be highlighted in a source \nbrowser.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjap.uft")) {
         return "Phase " + phaseName + ":\n\nUses the call graph to determine which fields are unreachable \nand adds color tags so they can be highlighted in a source \nbrowser.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjap.tqt")) {
         return "Phase " + phaseName + ":\n\nDetermines which methods and fields have qualifiers that could \nbe tightened. For example: if a field or method has the \nqualifier of public but is only used within the declaring class \nit could be private. This, this field or method is tagged with \ncolor tags so that the results can be highlighted in a source \nbrowser.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("wjap.cgg")) {
         return "Phase " + phaseName + ":\n\nCreates graphical call graph.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("show-lib-meths (false)", "");
      } else if (phaseName.equals("wjap.purity")) {
         return "Phase " + phaseName + ":\n\nPurity anaysis implemented by Antoine Mine and based on the \npaper A Combined Pointer and Purity Analysis for Java Programs \nby Alexandru Salcianu and Martin Rinard.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("dump-summaries (true)", "") + this.padOpt("dump-cg (false)", "") + this.padOpt("dump-intra (false)", "") + this.padOpt("print (true)", "") + this.padOpt("annotate (true)", "Marks pure methods with a purity bytecode attribute") + this.padOpt("verbose (false)", "");
      } else if (phaseName.equals("shimple")) {
         return "Phase " + phaseName + ":\n\nShimple Control sets parameters which apply throughout the \ncreation and manipulation of Shimple bodies. Shimple is Soot's \nSSA representation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("node-elim-opt (true)", "Node elimination optimizations") + this.padOpt("standard-local-names (false)", "Uses naming scheme of the Local Name Standardizer.") + this.padOpt("extended (false)", "Compute extended SSA (SSI) form.") + this.padOpt("debug (false)", "Enables debugging output, if any.");
      } else if (phaseName.equals("stp")) {
         return "Phase " + phaseName + ":\n\nWhen the Shimple representation is produced, Soot applies the \ncontents of the Shimple Transformation Pack to each method under \nanalysis. This pack contains no transformations in an unmodified \nversion of Soot.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("sop")) {
         return "Phase " + phaseName + ":\n\nThe Shimple Optimization Pack contains transformations that \nperform optimizations on Shimple, Soot's SSA representation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("sop.cpf")) {
         return "Phase " + phaseName + ":\n\nA powerful constant propagator and folder based on an algorithm \nsketched by Cytron et al that takes conditional control flow \ninto account. This optimization demonstrates some of the \nbenefits of SSA -- particularly the fact that Phi nodes \nrepresent natural merge points in the control flow.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("prune-cfg (true)", "Take advantage of CFG optimization opportunities.");
      } else if (phaseName.equals("jtp")) {
         return "Phase " + phaseName + ":\n\nSoot applies the contents of the Jimple Transformation Pack to \neach method under analysis. This pack contains no \ntransformations in an unmodified version of Soot.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jop")) {
         return "Phase " + phaseName + ":\n\nWhen Soot's Optimize option is on, Soot applies the Jimple \nOptimization Pack to every JimpleBody in application classes. \nThis section lists the default transformations in the Jimple \nOptimization Pack.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "Eliminates common subexpressions");
      } else if (phaseName.equals("jop.cse")) {
         return "Phase " + phaseName + ":\n\nThe Common Subexpression Eliminator runs an available \nexpressions analysis on the method body, then eliminates common \nsubexpressions. This implementation is especially slow, as it \nruns on individual statements rather than on basic blocks. A \nbetter implementation (which would find most common \nsubexpressions, but not all) would use basic blocks instead. \nThis implementation is also slow because the flow universe is \nexplicitly created; it need not be. A better implementation \nwould implicitly compute the kill sets at every node. Because of \nits current slowness, this transformation is not enabled by \ndefault.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("naive-side-effect (false)", "Use naive side effect analysis even if interprocedural information is available");
      } else if (phaseName.equals("jop.bcm")) {
         return "Phase " + phaseName + ":\n\nBusy Code Motion is a straightforward implementation of Partial \nRedundancy Elimination. This implementation is not very \naggressive. Lazy Code Motion is an improved version which should \nbe used instead of Busy Code Motion.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("naive-side-effect (false)", "Use a naive side effect analysis even if interprocedural information is available");
      } else if (phaseName.equals("jop.lcm")) {
         return "Phase " + phaseName + ":\n\nLazy Code Motion is an enhanced version of Busy Code Motion, a \nPartial Redundancy Eliminator. Before doing Partial Redundancy \nElimination, this optimization performs loop inversion (turning \nwhile loops into do while loops inside an if statement). This \nallows the Partial Redundancy Eliminator to optimize loop \ninvariants of while loops.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("safety", "") + this.padVal("safe (default)", "") + this.padVal("medium", "") + this.padVal("unsafe", "") + this.padOpt("unroll (true)", "") + this.padOpt("naive-side-effect (false)", "Use a naive side effect analysis even if interprocedural information is available");
      } else if (phaseName.equals("jop.cp")) {
         return "Phase " + phaseName + ":\n\nThis phase performs cascaded copy propagation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-regular-locals (false)", "") + this.padOpt("only-stack-locals (false)", "");
      } else if (phaseName.equals("jop.cpf")) {
         return "Phase " + phaseName + ":\n\nThe Jimple Constant Propagator and Folder evaluates any \nexpressions consisting entirely of compile-time constants, for \nexample 2 * 3, and replaces the expression with the constant \nresult, in this case 6.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jop.cbf")) {
         return "Phase " + phaseName + ":\n\nThe Conditional Branch Folder statically evaluates the \nconditional expression of Jimple if statements. If the condition \nis identically true or false, the Folder replaces the \nconditional branch statement with an unconditional goto \nstatement.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jop.dae")) {
         return "Phase " + phaseName + ":\n\nThe Dead Assignment Eliminator eliminates assignment statements \nto locals whose values are not subsequently used, unless \nevaluating the right-hand side of the assignment may cause \nside-effects.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-tag (false)", "") + this.padOpt("only-stack-locals (false)", "");
      } else if (phaseName.equals("jop.nce")) {
         return "Phase " + phaseName + ":\n\nReplaces statements 'if(x!=null) goto y' with 'goto y' if x is \nknown to be non-null or with 'nop' if it is known to be null, \netc. Generates dead code and is hence followed by unreachable \ncode elimination. Disabled by default because it can be \nexpensive on methods with many locals.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jop.uce1")) {
         return "Phase " + phaseName + ":\n\nThe Unreachable Code Eliminator removes unreachable code and \ntraps whose catch blocks are empty.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("remove-unreachable-traps (false)", "");
      } else if (phaseName.equals("jop.ubf1")) {
         return "Phase " + phaseName + ":\n\nThe Unconditional Branch Folder removes unnecessary `goto' \nstatements from a JimpleBody. If a goto statement's target is \nthe next instruction, then the statement is removed. If a goto's \ntarget is another goto, with target y, then the first \nstatement's target is changed to y. If some if statement's \ntarget is a goto statement, then the if's target can be replaced \nwith the goto's target. (These situations can result from other \noptimizations, and branch folding may itself generate more \nunreachable code.)\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jop.uce2")) {
         return "Phase " + phaseName + ":\n\nAnother iteration of the Unreachable Code Eliminator.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("remove-unreachable-traps (false)", "");
      } else if (phaseName.equals("jop.ubf2")) {
         return "Phase " + phaseName + ":\n\nAnother iteration of the Unconditional Branch Folder.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jop.ule")) {
         return "Phase " + phaseName + ":\n\nThe Unused Local Eliminator phase removes any unused locals from \nthe method.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jap")) {
         return "Phase " + phaseName + ":\n\nThe Jimple Annotation Pack contains phases which add annotations \nto Jimple bodies individually (as opposed to the Whole-Jimple \nAnnotation Pack, which adds annotations based on the analysis of \nthe whole program).\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("jap.npc")) {
         return "Phase " + phaseName + ":\n\nThe Null Pointer Checker finds instruction which have the \npotential to throw NullPointerExceptions and adds annotations \nindicating whether or not the pointer being dereferenced can be \ndetermined statically not to be null.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("only-array-ref (false)", "Annotate only array references") + this.padOpt("profiling (false)", "Insert instructions to count safe pointer accesses");
      } else if (phaseName.equals("jap.npcolorer")) {
         return "Phase " + phaseName + ":\n\nProduce colour tags that the Soot plug-in for Eclipse can use to \nhighlight null and non-null references.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.abc")) {
         return "Phase " + phaseName + ":\n\nThe Array Bound Checker performs a static analysis to determine \nwhich array bounds checks may safely be eliminated and then \nannotates statements with the results of the analysis. If Soot \nis in whole-program mode, the Array Bound Checker can use the \nresults provided by the Rectangular Array Finder.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("with-all (false)", "") + this.padOpt("with-cse (false)", "") + this.padOpt("with-arrayref (false)", "") + this.padOpt("with-fieldref (false)", "") + this.padOpt("with-classfield (false)", "") + this.padOpt("with-rectarray (false)", "") + this.padOpt("profiling (false)", "Profile the results of array bounds check analysis.") + this.padOpt("add-color-tags (false)", "Add color tags to results of array bound check analysis.");
      } else if (phaseName.equals("jap.profiling")) {
         return "Phase " + phaseName + ":\n\nThe Profiling Generator inserts the method invocations required \nto initialize and to report the results of any profiling \nperformed by the Null Pointer Checker and Array Bound Checker. \nUsers of the Profiling Generator must provide a MultiCounter \nclass implementing the methods invoked. For details, see the \nProfilingGenerator source code.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("notmainentry (false)", "Instrument runBenchmark() instead of main()");
      } else if (phaseName.equals("jap.sea")) {
         return "Phase " + phaseName + ":\n\nThe Side Effect Tagger uses the active invoke graph to produce \nside-effect attributes, as described in the Spark thesis, \nchapter 6.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("naive (false)", "");
      } else if (phaseName.equals("jap.fieldrw")) {
         return "Phase " + phaseName + ":\n\nThe Field Read/Write Tagger uses the active invoke graph to \nproduce tags indicating which fields may be read or written by \neach statement, including invoke statements.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("threshold (100)", "");
      } else if (phaseName.equals("jap.cgtagger")) {
         return "Phase " + phaseName + ":\n\nThe Call Graph Tagger produces LinkTags based on the call graph. \nThe Eclipse plugin uses these tags to produce linked popup lists \nwhich indicate the source and target methods of the statement. \nSelecting a link from the list moves the cursor to the indicated \nmethod.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.parity")) {
         return "Phase " + phaseName + ":\n\nThe Parity Tagger produces StringTags and ColorTags indicating \nthe parity of a variable (even, odd, top, or bottom). The \neclipse plugin can use tooltips and variable colouring to \ndisplay the information in these tags. For example, even \nvariables (such as x in x = 2) are coloured yellow.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.pat")) {
         return "Phase " + phaseName + ":\n\nFor each method with parameters of reference type, this tagger \nindicates the aliasing relationships between the parameters \nusing colour tags. Parameters that may be aliased are the same \ncolour. Parameters that may not be aliased are in different \ncolours.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.lvtagger")) {
         return "Phase " + phaseName + ":\n\nColors live variables.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.rdtagger")) {
         return "Phase " + phaseName + ":\n\nFor each use of a local in a stmt creates a link to the reaching \ndef.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.che")) {
         return "Phase " + phaseName + ":\n\nIndicates whether cast checks can be eliminated.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.umt")) {
         return "Phase " + phaseName + ":\n\nWhen the whole-program analysis determines a method to be \nunreachable, this transformer inserts an assertion into the \nmethod to check that it is indeed unreachable.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.lit")) {
         return "Phase " + phaseName + ":\n\nAn expression whose operands are constant or have reaching \ndefinitions from outside the loop body are tagged as loop \ninvariant.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("jap.aet")) {
         return "Phase " + phaseName + ":\n\nA each statement a set of available expressions is after the \nstatement is added as a tag.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "") + this.padOpt("kind", "") + this.padVal("optimistic (default)", "") + this.padVal("pessimistic", "");
      } else if (phaseName.equals("jap.dmt")) {
         return "Phase " + phaseName + ":\n\nProvides link tags at a statement to all of the satements \ndominators.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("gb")) {
         return "Phase " + phaseName + ":\n\nThe Grimp Body Creation phase creates a GrimpBody for each \nsource method. It is run only if the output format is grimp or \ngrimple, or if class files are being output and the Via Grimp \noption has been specified.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("gb.a1")) {
         return "Phase " + phaseName + ":\n\nThe Grimp Pre-folding Aggregator combines some local variables, \nfinding definitions with only a single use and removing the \ndefinition after replacing the use with the definition's \nright-hand side, if it is safe to do so. While the mechanism is \nthe same as that employed by the Jimple Local Aggregator, there \nis more scope for aggregation because of Grimp's more \ncomplicated expressions.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("gb.cf")) {
         return "Phase " + phaseName + ":\n\nThe Grimp Constructor Folder combines new statements with the \nspecialinvoke statement that calls the new object's constructor. \nFor example, it turns r2 = new java.util.ArrayList; r2.init(); \ninto r2 = new java.util.ArrayList();\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("gb.a2")) {
         return "Phase " + phaseName + ":\n\nThe Grimp Post-folding Aggregator combines local variables after \nconstructors have been folded. Constructor folding typically \nintroduces new opportunities for aggregation, since when a \nsequence of instructions like r2 = new java.util.ArrayList; \nr2.init(); r3 = r2 is replaced by r2 = new \njava.util.ArrayList(); r3 = r2 the invocation of init no longer \nrepresents a potential side-effect separating the two \ndefinitions, so they can be combined into r3 = new \njava.util.ArrayList(); (assuming there are no subsequent uses of \nr2).\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("only-stack-locals (true)", "");
      } else if (phaseName.equals("gb.ule")) {
         return "Phase " + phaseName + ":\n\nThis phase removes any locals that are unused after constructor \nfolding and aggregation.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("gop")) {
         return "Phase " + phaseName + ":\n\nThe Grimp Optimization pack performs optimizations on GrimpBodys \n(currently there are no optimizations performed specifically on \nGrimpBodys, and the pack is empty). It is run only if the output \nformat is grimp or grimple, or if class files are being output \nand the Via Grimp option has been specified.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("bb")) {
         return "Phase " + phaseName + ":\n\nThe Baf Body Creation phase creates a BafBody from each source \nmethod. It is run if the output format is baf or b or asm or a, \nor if class files are being output and the Via Grimp option has \nnot been specified.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("bb.lso")) {
         return "Phase " + phaseName + ":\n\nThe Load Store Optimizer replaces some combinations of loads to \nand stores from local variables with stack instructions. A \nsimple example would be the replacement of store.r $r2; load.r \n$r2; with dup1.r in cases where the value of r2 is not used \nsubsequently.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("debug (false)", "") + this.padOpt("inter (false)", "") + this.padOpt("sl (true)", "") + this.padOpt("sl2 (false)", "") + this.padOpt("sll (true)", "") + this.padOpt("sll2 (false)", "");
      } else if (phaseName.equals("bb.sco")) {
         return "Phase " + phaseName + ":\n\nThe store chain optimizer detects chains of push/store pairs \nthat write to the same variable and only retains the last store. \nIt removes the unnecessary previous push/stores that are \nsubsequently overwritten.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("bb.pho")) {
         return "Phase " + phaseName + ":\n\nApplies peephole optimizations to the Baf intermediate \nrepresentation. Individual optimizations must be implemented by \nclasses implementing the Peephole interface. The Peephole \nOptimizer reads the names of the Peephole classes at runtime \nfrom the file peephole.dat and loads them dynamically. Then it \ncontinues to apply the Peepholes repeatedly until none of them \nare able to perform any further optimizations. Soot provides \nonly one Peephole, named ExamplePeephole, which is not enabled \nby the delivered peephole.dat file. ExamplePeephole removes all \ncheckcast instructions.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("bb.ule")) {
         return "Phase " + phaseName + ":\n\nThis phase removes any locals that are unused after load store \noptimization and peephole optimization.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("bb.lp")) {
         return "Phase " + phaseName + ":\n\nThe Local Packer attempts to minimize the number of local \nvariables required in a method by reusing the same variable for \ndisjoint DU-UD webs. Conceptually, it is the inverse of the \nLocal Splitter.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("unsplit-original-locals (false)", "");
      } else if (phaseName.equals("bop")) {
         return "Phase " + phaseName + ":\n\nThe Baf Optimization pack performs optimizations on BafBodys \n(currently there are no optimizations performed specifically on \nBafBodys, and the pack is empty). It is run only if the output \nformat is baf or b or asm or a, or if class files are being \noutput and the Via Grimp option has not been specified.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("tag")) {
         return "Phase " + phaseName + ":\n\nThe Tag Aggregator pack aggregates tags attached to individual \nunits into a code attribute for each method, so that these \nattributes can be encoded in Java class files.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("tag.ln")) {
         return "Phase " + phaseName + ":\n\nThe Line Number Tag Aggregator aggregates line number tags.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("tag.an")) {
         return "Phase " + phaseName + ":\n\nThe Array Bounds and Null Pointer Tag Aggregator aggregates tags \nproduced by the Array Bound Checker and Null Pointer Checker.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("tag.dep")) {
         return "Phase " + phaseName + ":\n\nThe Dependence Tag Aggregator aggregates tags produced by the \nSide Effect Tagger.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("tag.fieldrw")) {
         return "Phase " + phaseName + ":\n\nThe Field Read/Write Tag Aggregator aggregates field read/write \ntags produced by the Field Read/Write Tagger, phase jap.fieldrw.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("db")) {
         return "Phase " + phaseName + ":\n\nThe decompile (Dava) option is set using the -f dava options in \nSoot. Options provided by Dava are added to this dummy phase so \nas not to clutter the soot general arguments. -p db (option \nname):(value) will be used to set all required values for Dava.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") + this.padOpt("source-is-javac (true)", "");
      } else if (phaseName.equals("db.transformations")) {
         return "Phase " + phaseName + ":\n\nThe transformations implemented using AST Traversal and \nstructural flow analses on Dava's AST\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else if (phaseName.equals("db.renamer")) {
         return "Phase " + phaseName + ":\n\nIf set, the renaming analyses implemented in Dava are applied to \neach method body being decompiled. The analyses use heuristics \nto choose potentially better names for local variables. (As of \nFebruary 14th 2006, work is still under progress on these \nanalyses (dava.toolkits.base.renamer).\n\nRecognized options (with default values):\n" + this.padOpt("enabled (false)", "");
      } else if (phaseName.equals("db.deobfuscate")) {
         return "Phase " + phaseName + ":\n\nCertain analyses make sense only when the bytecode is obfuscated \ncode. There are plans to implement such analyses and apply them \non methods only if this flag is set. Dead Code elimination which \nincludes removing code guarded by some condition which is always \nfalse or always true is one such analysis. Another suggested \nanalysis is giving default names to classes and fields. \nOnfuscators love to use weird names for fields and classes and \neven a simple re-naming of these could be a good help to the \nuser. Another more advanced analysis would be to check for \nredundant constant fields added by obfuscators and then remove \nuses of these constant fields from the code.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "");
      } else {
         return phaseName.equals("db.force-recompile") ? "Phase " + phaseName + ":\n\nWhile decompiling we have to be clear what our aim is: do we \nwant to convert bytecode to Java syntax and stay as close to the \nactual execution of bytecode or do we want recompilably Java \nsource representing the bytecode. This distinction is important \nbecause some restrictions present in Java source are absent from \nthe bytecode. Examples of this include that fact that in Java a \ncall to a constructor or super needs to be the first statement \nin a constructors body. This restriction is absent from the \nbytecode. Similarly final fields HAVE to be initialized once and \nonly once in either the static initializer (static fields) or \nall the constructors (non-static fields). Additionally the \nfields should be initialized on all possible execution paths. \nThese restrictions are again absent from the bytecode. In doing \na one-one conversion of bytecode to Java source then no attempt \nshould be made to fix any of these and similar problems in the \nJava source. However, if the aim is to get recompilable code \nthen these and similar issues need to be fixed. Setting the \nforce-recompilability flag will ensure that the decompiler tries \nits best to produce recompilable Java source.\n\nRecognized options (with default values):\n" + this.padOpt("enabled (true)", "") : "Unrecognized phase: " + phaseName;
      }
   }

   public static String getDeclaredOptionsForPhase(String phaseName) {
      if (phaseName.equals("jb")) {
         return String.join(" ", "enabled", "use-original-names", "preserve-source-annotations", "stabilize-local-names");
      } else if (phaseName.equals("jb.dtr")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jb.ese")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jb.ls")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jb.a")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("jb.ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jb.tr")) {
         return String.join(" ", "enabled", "use-older-type-assigner", "compare-type-assigners", "ignore-nullpointer-dereferences");
      } else if (phaseName.equals("jb.ulp")) {
         return String.join(" ", "enabled", "unsplit-original-locals");
      } else if (phaseName.equals("jb.lns")) {
         return String.join(" ", "enabled", "only-stack-locals", "sort-locals");
      } else if (phaseName.equals("jb.cp")) {
         return String.join(" ", "enabled", "only-regular-locals", "only-stack-locals");
      } else if (phaseName.equals("jb.dae")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("jb.cp-ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jb.lp")) {
         return String.join(" ", "enabled", "unsplit-original-locals");
      } else if (phaseName.equals("jb.ne")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jb.uce")) {
         return String.join(" ", "enabled", "remove-unreachable-traps");
      } else if (phaseName.equals("jb.tt")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jj")) {
         return String.join(" ", "enabled", "use-original-names");
      } else if (phaseName.equals("jj.ls")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jj.a")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("jj.ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jj.tr")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jj.ulp")) {
         return String.join(" ", "enabled", "unsplit-original-locals");
      } else if (phaseName.equals("jj.lns")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("jj.cp")) {
         return String.join(" ", "enabled", "only-regular-locals", "only-stack-locals");
      } else if (phaseName.equals("jj.dae")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("jj.cp-ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jj.lp")) {
         return String.join(" ", "enabled", "unsplit-original-locals");
      } else if (phaseName.equals("jj.ne")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jj.uce")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjpp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjpp.cimbt")) {
         return String.join(" ", "enabled", "verbose");
      } else if (phaseName.equals("wspp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("cg")) {
         return String.join(" ", "enabled", "safe-forname", "safe-newinstance", "library", "verbose", "jdkver", "all-reachable", "implicit-entry", "trim-clinit", "reflection-log", "guards", "types-for-invoke");
      } else if (phaseName.equals("cg.cha")) {
         return String.join(" ", "enabled", "verbose", "apponly");
      } else if (phaseName.equals("cg.spark")) {
         return String.join(" ", "enabled", "verbose", "ignore-types", "force-gc", "pre-jimplify", "apponly", "vta", "rta", "field-based", "types-for-sites", "merge-stringbuffer", "string-constants", "simulate-natives", "empties-as-allocs", "simple-edges-bidirectional", "on-fly-cg", "simplify-offline", "simplify-sccs", "ignore-types-for-sccs", "propagator", "set-impl", "double-set-old", "double-set-new", "dump-html", "dump-pag", "dump-solution", "topo-sort", "dump-types", "class-method-var", "dump-answer", "add-tags", "set-mass", "cs-demand", "lazy-pts", "traversal", "passes", "geom-pta", "geom-encoding", "geom-worklist", "geom-dump-verbose", "geom-verify-name", "geom-eval", "geom-trans", "geom-frac-base", "geom-blocking", "geom-runs", "geom-app-only");
      } else if (phaseName.equals("cg.paddle")) {
         return String.join(" ", "enabled", "verbose", "conf", "bdd", "order", "dynamic-order", "profile", "verbosegc", "q", "backend", "bdd-nodes", "ignore-types", "pre-jimplify", "context", "k", "context-heap", "rta", "field-based", "types-for-sites", "merge-stringbuffer", "string-constants", "simulate-natives", "global-nodes-in-natives", "simple-edges-bidirectional", "this-edges", "precise-newinstance", "propagator", "set-impl", "double-set-old", "double-set-new", "context-counts", "total-context-counts", "method-context-counts", "set-mass", "number-nodes");
      } else if (phaseName.equals("wstp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wsop")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjtp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjtp.mhp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjtp.tn")) {
         return String.join(" ", "enabled", "locking-scheme", "avoid-deadlock", "open-nesting", "do-mhp", "do-tlo", "print-graph", "print-table", "print-debug");
      } else if (phaseName.equals("wjtp.rdc")) {
         return String.join(" ", "enabled", "fcn fixed-class-names");
      } else if (phaseName.equals("wjop")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjop.smb")) {
         return String.join(" ", "enabled", "insert-null-checks", "insert-redundant-casts", "allowed-modifier-changes");
      } else if (phaseName.equals("wjop.si")) {
         return String.join(" ", "enabled", "rerun-jb", "insert-null-checks", "insert-redundant-casts", "allowed-modifier-changes", "expansion-factor", "max-container-size", "max-inlinee-size");
      } else if (phaseName.equals("wjap")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjap.ra")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjap.umt")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjap.uft")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjap.tqt")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("wjap.cgg")) {
         return String.join(" ", "enabled", "show-lib-meths");
      } else if (phaseName.equals("wjap.purity")) {
         return String.join(" ", "enabled", "dump-summaries", "dump-cg", "dump-intra", "print", "annotate", "verbose");
      } else if (phaseName.equals("shimple")) {
         return String.join(" ", "enabled", "node-elim-opt", "standard-local-names", "extended", "debug");
      } else if (phaseName.equals("stp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("sop")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("sop.cpf")) {
         return String.join(" ", "enabled", "prune-cfg");
      } else if (phaseName.equals("jtp")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop.cse")) {
         return String.join(" ", "enabled", "naive-side-effect");
      } else if (phaseName.equals("jop.bcm")) {
         return String.join(" ", "enabled", "naive-side-effect");
      } else if (phaseName.equals("jop.lcm")) {
         return String.join(" ", "enabled", "safety", "unroll", "naive-side-effect");
      } else if (phaseName.equals("jop.cp")) {
         return String.join(" ", "enabled", "only-regular-locals", "only-stack-locals");
      } else if (phaseName.equals("jop.cpf")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop.cbf")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop.dae")) {
         return String.join(" ", "enabled", "only-tag", "only-stack-locals");
      } else if (phaseName.equals("jop.nce")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop.uce1")) {
         return String.join(" ", "enabled", "remove-unreachable-traps");
      } else if (phaseName.equals("jop.ubf1")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop.uce2")) {
         return String.join(" ", "enabled", "remove-unreachable-traps");
      } else if (phaseName.equals("jop.ubf2")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jop.ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.npc")) {
         return String.join(" ", "enabled", "only-array-ref", "profiling");
      } else if (phaseName.equals("jap.npcolorer")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.abc")) {
         return String.join(" ", "enabled", "with-all", "with-cse", "with-arrayref", "with-fieldref", "with-classfield", "with-rectarray", "profiling", "add-color-tags");
      } else if (phaseName.equals("jap.profiling")) {
         return String.join(" ", "enabled", "notmainentry");
      } else if (phaseName.equals("jap.sea")) {
         return String.join(" ", "enabled", "naive");
      } else if (phaseName.equals("jap.fieldrw")) {
         return String.join(" ", "enabled", "threshold");
      } else if (phaseName.equals("jap.cgtagger")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.parity")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.pat")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.lvtagger")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.rdtagger")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.che")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.umt")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.lit")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("jap.aet")) {
         return String.join(" ", "enabled", "kind");
      } else if (phaseName.equals("jap.dmt")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("gb")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("gb.a1")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("gb.cf")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("gb.a2")) {
         return String.join(" ", "enabled", "only-stack-locals");
      } else if (phaseName.equals("gb.ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("gop")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("bb")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("bb.lso")) {
         return String.join(" ", "enabled", "debug", "inter", "sl", "sl2", "sll", "sll2");
      } else if (phaseName.equals("bb.sco")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("bb.pho")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("bb.ule")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("bb.lp")) {
         return String.join(" ", "enabled", "unsplit-original-locals");
      } else if (phaseName.equals("bop")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("tag")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("tag.ln")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("tag.an")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("tag.dep")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("tag.fieldrw")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("db")) {
         return String.join(" ", "enabled", "source-is-javac");
      } else if (phaseName.equals("db.transformations")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("db.renamer")) {
         return String.join(" ", "enabled");
      } else if (phaseName.equals("db.deobfuscate")) {
         return String.join(" ", "enabled");
      } else {
         return phaseName.equals("db.force-recompile") ? String.join(" ", "enabled") : "enabled";
      }
   }

   public static String getDefaultOptionsForPhase(String phaseName) {
      if (phaseName.equals("jb")) {
         return "enabled:true use-original-names:false preserve-source-annotations:false stabilize-local-names:false ";
      } else if (phaseName.equals("jb.dtr")) {
         return "enabled:true ";
      } else if (phaseName.equals("jb.ese")) {
         return "enabled:true ";
      } else if (phaseName.equals("jb.ls")) {
         return "enabled:true ";
      } else if (phaseName.equals("jb.a")) {
         return "enabled:true only-stack-locals:true ";
      } else if (phaseName.equals("jb.ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("jb.tr")) {
         return "enabled:true use-older-type-assigner:false compare-type-assigners:false ignore-nullpointer-dereferences:false ";
      } else if (phaseName.equals("jb.ulp")) {
         return "enabled:true unsplit-original-locals:true ";
      } else if (phaseName.equals("jb.lns")) {
         return "enabled:true only-stack-locals:false sort-locals:false ";
      } else if (phaseName.equals("jb.cp")) {
         return "enabled:true only-regular-locals:false only-stack-locals:true ";
      } else if (phaseName.equals("jb.dae")) {
         return "enabled:true only-stack-locals:true ";
      } else if (phaseName.equals("jb.cp-ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("jb.lp")) {
         return "enabled:false unsplit-original-locals:false ";
      } else if (phaseName.equals("jb.ne")) {
         return "enabled:true ";
      } else if (phaseName.equals("jb.uce")) {
         return "enabled:true remove-unreachable-traps:true ";
      } else if (phaseName.equals("jb.tt")) {
         return "enabled:false ";
      } else if (phaseName.equals("jj")) {
         return "enabled:true use-original-names:true ";
      } else if (phaseName.equals("jj.ls")) {
         return "enabled:false ";
      } else if (phaseName.equals("jj.a")) {
         return "enabled:true only-stack-locals:true ";
      } else if (phaseName.equals("jj.ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("jj.tr")) {
         return "enabled:false ";
      } else if (phaseName.equals("jj.ulp")) {
         return "enabled:false unsplit-original-locals:false ";
      } else if (phaseName.equals("jj.lns")) {
         return "enabled:true only-stack-locals:false ";
      } else if (phaseName.equals("jj.cp")) {
         return "enabled:true only-regular-locals:false only-stack-locals:true ";
      } else if (phaseName.equals("jj.dae")) {
         return "enabled:true only-stack-locals:true ";
      } else if (phaseName.equals("jj.cp-ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("jj.lp")) {
         return "enabled:false unsplit-original-locals:false ";
      } else if (phaseName.equals("jj.ne")) {
         return "enabled:true ";
      } else if (phaseName.equals("jj.uce")) {
         return "enabled:true ";
      } else if (phaseName.equals("wjpp")) {
         return "enabled:true ";
      } else if (phaseName.equals("wjpp.cimbt")) {
         return "enabled:false verbose:false ";
      } else if (phaseName.equals("wspp")) {
         return "enabled:true ";
      } else if (phaseName.equals("cg")) {
         return "enabled:true safe-forname:false safe-newinstance:false library:disabled verbose:false jdkver:3 all-reachable:false implicit-entry:true trim-clinit:true guards:ignore types-for-invoke:false ";
      } else if (phaseName.equals("cg.cha")) {
         return "enabled:true verbose:false apponly:false ";
      } else if (phaseName.equals("cg.spark")) {
         return "enabled:false verbose:false ignore-types:false force-gc:false pre-jimplify:false apponly:false vta:false rta:false field-based:false types-for-sites:false merge-stringbuffer:true string-constants:false simulate-natives:true empties-as-allocs:false simple-edges-bidirectional:false on-fly-cg:true simplify-offline:false simplify-sccs:false ignore-types-for-sccs:false propagator:worklist set-impl:double double-set-old:hybrid double-set-new:hybrid dump-html:false dump-pag:false dump-solution:false topo-sort:false dump-types:true class-method-var:true dump-answer:false add-tags:false set-mass:false cs-demand:false lazy-pts:true traversal:75000 passes:10 geom-pta:false geom-encoding:Geom geom-encoding:Geom geom-worklist:PQ geom-worklist:PQ geom-dump-verbose: geom-verify-name: geom-eval:0 geom-trans:false geom-frac-base:40 geom-blocking:true geom-runs:1 geom-app-only:true ";
      } else if (phaseName.equals("cg.paddle")) {
         return "enabled:false verbose:false conf:ofcg bdd:false order:32 profile:false verbosegc:false q:auto backend:auto bdd-nodes:0 ignore-types:false pre-jimplify:false context:insens k:2 context-heap:false rta:false field-based:false types-for-sites:false merge-stringbuffer:true string-constants:false simulate-natives:true global-nodes-in-natives:false simple-edges-bidirectional:false this-edges:false precise-newinstance:true propagator:auto set-impl:double double-set-old:hybrid double-set-new:hybrid context-counts:false total-context-counts:false method-context-counts:false set-mass:false number-nodes:true ";
      } else if (phaseName.equals("wstp")) {
         return "enabled:true ";
      } else if (phaseName.equals("wsop")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjtp")) {
         return "enabled:true ";
      } else if (phaseName.equals("wjtp.mhp")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjtp.tn")) {
         return "enabled:false locking-scheme:medium-grained avoid-deadlock:true open-nesting:true do-mhp:true do-tlo:true print-graph:false print-table:false print-debug:false ";
      } else if (phaseName.equals("wjtp.rdc")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjop")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjop.smb")) {
         return "enabled:false insert-null-checks:true insert-redundant-casts:true allowed-modifier-changes:unsafe ";
      } else if (phaseName.equals("wjop.si")) {
         return "enabled:true rerun-jb:true insert-null-checks:true insert-redundant-casts:true allowed-modifier-changes:unsafe expansion-factor:3 max-container-size:5000 max-inlinee-size:20 ";
      } else if (phaseName.equals("wjap")) {
         return "enabled:true ";
      } else if (phaseName.equals("wjap.ra")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjap.umt")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjap.uft")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjap.tqt")) {
         return "enabled:false ";
      } else if (phaseName.equals("wjap.cgg")) {
         return "enabled:false show-lib-meths:false ";
      } else if (phaseName.equals("wjap.purity")) {
         return "enabled:false dump-summaries:true dump-cg:false dump-intra:false print:true annotate:true verbose:false ";
      } else if (phaseName.equals("shimple")) {
         return "enabled:true node-elim-opt:true standard-local-names:false extended:false debug:false ";
      } else if (phaseName.equals("stp")) {
         return "enabled:true ";
      } else if (phaseName.equals("sop")) {
         return "enabled:false ";
      } else if (phaseName.equals("sop.cpf")) {
         return "enabled:true prune-cfg:true ";
      } else if (phaseName.equals("jtp")) {
         return "enabled:true ";
      } else if (phaseName.equals("jop")) {
         return "enabled:false ";
      } else if (phaseName.equals("jop.cse")) {
         return "enabled:false naive-side-effect:false ";
      } else if (phaseName.equals("jop.bcm")) {
         return "enabled:false naive-side-effect:false ";
      } else if (phaseName.equals("jop.lcm")) {
         return "enabled:false safety:safe unroll:true naive-side-effect:false ";
      } else if (phaseName.equals("jop.cp")) {
         return "enabled:true only-regular-locals:false only-stack-locals:false ";
      } else if (phaseName.equals("jop.cpf")) {
         return "enabled:true ";
      } else if (phaseName.equals("jop.cbf")) {
         return "enabled:true ";
      } else if (phaseName.equals("jop.dae")) {
         return "enabled:true only-tag:false only-stack-locals:false ";
      } else if (phaseName.equals("jop.nce")) {
         return "enabled:false ";
      } else if (phaseName.equals("jop.uce1")) {
         return "enabled:true remove-unreachable-traps:false ";
      } else if (phaseName.equals("jop.ubf1")) {
         return "enabled:true ";
      } else if (phaseName.equals("jop.uce2")) {
         return "enabled:true remove-unreachable-traps:false ";
      } else if (phaseName.equals("jop.ubf2")) {
         return "enabled:true ";
      } else if (phaseName.equals("jop.ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("jap")) {
         return "enabled:true ";
      } else if (phaseName.equals("jap.npc")) {
         return "enabled:false only-array-ref:false profiling:false ";
      } else if (phaseName.equals("jap.npcolorer")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.abc")) {
         return "enabled:false with-all:false with-cse:false with-arrayref:false with-fieldref:false with-classfield:false with-rectarray:false profiling:false add-color-tags:false ";
      } else if (phaseName.equals("jap.profiling")) {
         return "enabled:false notmainentry:false ";
      } else if (phaseName.equals("jap.sea")) {
         return "enabled:false naive:false ";
      } else if (phaseName.equals("jap.fieldrw")) {
         return "enabled:false threshold:100 ";
      } else if (phaseName.equals("jap.cgtagger")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.parity")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.pat")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.lvtagger")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.rdtagger")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.che")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.umt")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.lit")) {
         return "enabled:false ";
      } else if (phaseName.equals("jap.aet")) {
         return "enabled:false kind:optimistic ";
      } else if (phaseName.equals("jap.dmt")) {
         return "enabled:false ";
      } else if (phaseName.equals("gb")) {
         return "enabled:true ";
      } else if (phaseName.equals("gb.a1")) {
         return "enabled:true only-stack-locals:true ";
      } else if (phaseName.equals("gb.cf")) {
         return "enabled:true ";
      } else if (phaseName.equals("gb.a2")) {
         return "enabled:true only-stack-locals:true ";
      } else if (phaseName.equals("gb.ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("gop")) {
         return "enabled:false ";
      } else if (phaseName.equals("bb")) {
         return "enabled:true ";
      } else if (phaseName.equals("bb.lso")) {
         return "enabled:true debug:false inter:false sl:true sl2:false sll:true sll2:false ";
      } else if (phaseName.equals("bb.sco")) {
         return "enabled:true ";
      } else if (phaseName.equals("bb.pho")) {
         return "enabled:true ";
      } else if (phaseName.equals("bb.ule")) {
         return "enabled:true ";
      } else if (phaseName.equals("bb.lp")) {
         return "enabled:true unsplit-original-locals:false ";
      } else if (phaseName.equals("bop")) {
         return "enabled:false ";
      } else if (phaseName.equals("tag")) {
         return "enabled:true ";
      } else if (phaseName.equals("tag.ln")) {
         return "enabled:true ";
      } else if (phaseName.equals("tag.an")) {
         return "enabled:false ";
      } else if (phaseName.equals("tag.dep")) {
         return "enabled:false ";
      } else if (phaseName.equals("tag.fieldrw")) {
         return "enabled:false ";
      } else if (phaseName.equals("db")) {
         return "enabled:true source-is-javac:true ";
      } else if (phaseName.equals("db.transformations")) {
         return "enabled:true ";
      } else if (phaseName.equals("db.renamer")) {
         return "enabled:false ";
      } else if (phaseName.equals("db.deobfuscate")) {
         return "enabled:true ";
      } else {
         return phaseName.equals("db.force-recompile") ? "enabled:true " : "enabled";
      }
   }

   public void warnForeignPhase(String phaseName) {
      if (!phaseName.equals("jb") && !phaseName.equals("jb.dtr") && !phaseName.equals("jb.ese") && !phaseName.equals("jb.ls") && !phaseName.equals("jb.a") && !phaseName.equals("jb.ule") && !phaseName.equals("jb.tr") && !phaseName.equals("jb.ulp") && !phaseName.equals("jb.lns") && !phaseName.equals("jb.cp") && !phaseName.equals("jb.dae") && !phaseName.equals("jb.cp-ule") && !phaseName.equals("jb.lp") && !phaseName.equals("jb.ne") && !phaseName.equals("jb.uce") && !phaseName.equals("jb.tt") && !phaseName.equals("jj") && !phaseName.equals("jj.ls") && !phaseName.equals("jj.a") && !phaseName.equals("jj.ule") && !phaseName.equals("jj.tr") && !phaseName.equals("jj.ulp") && !phaseName.equals("jj.lns") && !phaseName.equals("jj.cp") && !phaseName.equals("jj.dae") && !phaseName.equals("jj.cp-ule") && !phaseName.equals("jj.lp") && !phaseName.equals("jj.ne") && !phaseName.equals("jj.uce") && !phaseName.equals("wjpp") && !phaseName.equals("wjpp.cimbt") && !phaseName.equals("wspp") && !phaseName.equals("cg") && !phaseName.equals("cg.cha") && !phaseName.equals("cg.spark") && !phaseName.equals("cg.paddle") && !phaseName.equals("wstp") && !phaseName.equals("wsop") && !phaseName.equals("wjtp") && !phaseName.equals("wjtp.mhp") && !phaseName.equals("wjtp.tn") && !phaseName.equals("wjtp.rdc") && !phaseName.equals("wjop") && !phaseName.equals("wjop.smb") && !phaseName.equals("wjop.si") && !phaseName.equals("wjap") && !phaseName.equals("wjap.ra") && !phaseName.equals("wjap.umt") && !phaseName.equals("wjap.uft") && !phaseName.equals("wjap.tqt") && !phaseName.equals("wjap.cgg") && !phaseName.equals("wjap.purity") && !phaseName.equals("shimple") && !phaseName.equals("stp") && !phaseName.equals("sop") && !phaseName.equals("sop.cpf") && !phaseName.equals("jtp") && !phaseName.equals("jop") && !phaseName.equals("jop.cse") && !phaseName.equals("jop.bcm") && !phaseName.equals("jop.lcm") && !phaseName.equals("jop.cp") && !phaseName.equals("jop.cpf") && !phaseName.equals("jop.cbf") && !phaseName.equals("jop.dae") && !phaseName.equals("jop.nce") && !phaseName.equals("jop.uce1") && !phaseName.equals("jop.ubf1") && !phaseName.equals("jop.uce2") && !phaseName.equals("jop.ubf2") && !phaseName.equals("jop.ule") && !phaseName.equals("jap") && !phaseName.equals("jap.npc") && !phaseName.equals("jap.npcolorer") && !phaseName.equals("jap.abc") && !phaseName.equals("jap.profiling") && !phaseName.equals("jap.sea") && !phaseName.equals("jap.fieldrw") && !phaseName.equals("jap.cgtagger") && !phaseName.equals("jap.parity") && !phaseName.equals("jap.pat") && !phaseName.equals("jap.lvtagger") && !phaseName.equals("jap.rdtagger") && !phaseName.equals("jap.che") && !phaseName.equals("jap.umt") && !phaseName.equals("jap.lit") && !phaseName.equals("jap.aet") && !phaseName.equals("jap.dmt") && !phaseName.equals("gb") && !phaseName.equals("gb.a1") && !phaseName.equals("gb.cf") && !phaseName.equals("gb.a2") && !phaseName.equals("gb.ule") && !phaseName.equals("gop") && !phaseName.equals("bb") && !phaseName.equals("bb.lso") && !phaseName.equals("bb.sco") && !phaseName.equals("bb.pho") && !phaseName.equals("bb.ule") && !phaseName.equals("bb.lp") && !phaseName.equals("bop") && !phaseName.equals("tag") && !phaseName.equals("tag.ln") && !phaseName.equals("tag.an") && !phaseName.equals("tag.dep") && !phaseName.equals("tag.fieldrw") && !phaseName.equals("db") && !phaseName.equals("db.transformations") && !phaseName.equals("db.renamer") && !phaseName.equals("db.deobfuscate") && !phaseName.equals("db.force-recompile")) {
         G.v().out.println("Warning: Phase " + phaseName + " is not a standard Soot phase listed in XML files.");
      }
   }

   public void warnNonexistentPhase() {
      if (!PackManager.v().hasPhase("jb")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb");
      }

      if (!PackManager.v().hasPhase("jb.dtr")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.dtr");
      }

      if (!PackManager.v().hasPhase("jb.ese")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.ese");
      }

      if (!PackManager.v().hasPhase("jb.ls")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.ls");
      }

      if (!PackManager.v().hasPhase("jb.a")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.a");
      }

      if (!PackManager.v().hasPhase("jb.ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.ule");
      }

      if (!PackManager.v().hasPhase("jb.tr")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.tr");
      }

      if (!PackManager.v().hasPhase("jb.ulp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.ulp");
      }

      if (!PackManager.v().hasPhase("jb.lns")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.lns");
      }

      if (!PackManager.v().hasPhase("jb.cp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.cp");
      }

      if (!PackManager.v().hasPhase("jb.dae")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.dae");
      }

      if (!PackManager.v().hasPhase("jb.cp-ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.cp-ule");
      }

      if (!PackManager.v().hasPhase("jb.lp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.lp");
      }

      if (!PackManager.v().hasPhase("jb.ne")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.ne");
      }

      if (!PackManager.v().hasPhase("jb.uce")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.uce");
      }

      if (!PackManager.v().hasPhase("jb.tt")) {
         G.v().out.println("Warning: Options exist for non-existent phase jb.tt");
      }

      if (!PackManager.v().hasPhase("jj")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj");
      }

      if (!PackManager.v().hasPhase("jj.ls")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.ls");
      }

      if (!PackManager.v().hasPhase("jj.a")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.a");
      }

      if (!PackManager.v().hasPhase("jj.ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.ule");
      }

      if (!PackManager.v().hasPhase("jj.tr")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.tr");
      }

      if (!PackManager.v().hasPhase("jj.ulp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.ulp");
      }

      if (!PackManager.v().hasPhase("jj.lns")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.lns");
      }

      if (!PackManager.v().hasPhase("jj.cp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.cp");
      }

      if (!PackManager.v().hasPhase("jj.dae")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.dae");
      }

      if (!PackManager.v().hasPhase("jj.cp-ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.cp-ule");
      }

      if (!PackManager.v().hasPhase("jj.lp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.lp");
      }

      if (!PackManager.v().hasPhase("jj.ne")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.ne");
      }

      if (!PackManager.v().hasPhase("jj.uce")) {
         G.v().out.println("Warning: Options exist for non-existent phase jj.uce");
      }

      if (!PackManager.v().hasPhase("wjpp")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjpp");
      }

      if (!PackManager.v().hasPhase("wjpp.cimbt")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjpp.cimbt");
      }

      if (!PackManager.v().hasPhase("wspp")) {
         G.v().out.println("Warning: Options exist for non-existent phase wspp");
      }

      if (!PackManager.v().hasPhase("cg")) {
         G.v().out.println("Warning: Options exist for non-existent phase cg");
      }

      if (!PackManager.v().hasPhase("cg.cha")) {
         G.v().out.println("Warning: Options exist for non-existent phase cg.cha");
      }

      if (!PackManager.v().hasPhase("cg.spark")) {
         G.v().out.println("Warning: Options exist for non-existent phase cg.spark");
      }

      if (!PackManager.v().hasPhase("cg.paddle")) {
         G.v().out.println("Warning: Options exist for non-existent phase cg.paddle");
      }

      if (!PackManager.v().hasPhase("wstp")) {
         G.v().out.println("Warning: Options exist for non-existent phase wstp");
      }

      if (!PackManager.v().hasPhase("wsop")) {
         G.v().out.println("Warning: Options exist for non-existent phase wsop");
      }

      if (!PackManager.v().hasPhase("wjtp")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjtp");
      }

      if (!PackManager.v().hasPhase("wjtp.mhp")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjtp.mhp");
      }

      if (!PackManager.v().hasPhase("wjtp.tn")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjtp.tn");
      }

      if (!PackManager.v().hasPhase("wjtp.rdc")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjtp.rdc");
      }

      if (!PackManager.v().hasPhase("wjop")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjop");
      }

      if (!PackManager.v().hasPhase("wjop.smb")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjop.smb");
      }

      if (!PackManager.v().hasPhase("wjop.si")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjop.si");
      }

      if (!PackManager.v().hasPhase("wjap")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap");
      }

      if (!PackManager.v().hasPhase("wjap.ra")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap.ra");
      }

      if (!PackManager.v().hasPhase("wjap.umt")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap.umt");
      }

      if (!PackManager.v().hasPhase("wjap.uft")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap.uft");
      }

      if (!PackManager.v().hasPhase("wjap.tqt")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap.tqt");
      }

      if (!PackManager.v().hasPhase("wjap.cgg")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap.cgg");
      }

      if (!PackManager.v().hasPhase("wjap.purity")) {
         G.v().out.println("Warning: Options exist for non-existent phase wjap.purity");
      }

      if (!PackManager.v().hasPhase("shimple")) {
         G.v().out.println("Warning: Options exist for non-existent phase shimple");
      }

      if (!PackManager.v().hasPhase("stp")) {
         G.v().out.println("Warning: Options exist for non-existent phase stp");
      }

      if (!PackManager.v().hasPhase("sop")) {
         G.v().out.println("Warning: Options exist for non-existent phase sop");
      }

      if (!PackManager.v().hasPhase("sop.cpf")) {
         G.v().out.println("Warning: Options exist for non-existent phase sop.cpf");
      }

      if (!PackManager.v().hasPhase("jtp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jtp");
      }

      if (!PackManager.v().hasPhase("jop")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop");
      }

      if (!PackManager.v().hasPhase("jop.cse")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.cse");
      }

      if (!PackManager.v().hasPhase("jop.bcm")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.bcm");
      }

      if (!PackManager.v().hasPhase("jop.lcm")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.lcm");
      }

      if (!PackManager.v().hasPhase("jop.cp")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.cp");
      }

      if (!PackManager.v().hasPhase("jop.cpf")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.cpf");
      }

      if (!PackManager.v().hasPhase("jop.cbf")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.cbf");
      }

      if (!PackManager.v().hasPhase("jop.dae")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.dae");
      }

      if (!PackManager.v().hasPhase("jop.nce")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.nce");
      }

      if (!PackManager.v().hasPhase("jop.uce1")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.uce1");
      }

      if (!PackManager.v().hasPhase("jop.ubf1")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.ubf1");
      }

      if (!PackManager.v().hasPhase("jop.uce2")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.uce2");
      }

      if (!PackManager.v().hasPhase("jop.ubf2")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.ubf2");
      }

      if (!PackManager.v().hasPhase("jop.ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase jop.ule");
      }

      if (!PackManager.v().hasPhase("jap")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap");
      }

      if (!PackManager.v().hasPhase("jap.npc")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.npc");
      }

      if (!PackManager.v().hasPhase("jap.npcolorer")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.npcolorer");
      }

      if (!PackManager.v().hasPhase("jap.abc")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.abc");
      }

      if (!PackManager.v().hasPhase("jap.profiling")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.profiling");
      }

      if (!PackManager.v().hasPhase("jap.sea")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.sea");
      }

      if (!PackManager.v().hasPhase("jap.fieldrw")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.fieldrw");
      }

      if (!PackManager.v().hasPhase("jap.cgtagger")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.cgtagger");
      }

      if (!PackManager.v().hasPhase("jap.parity")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.parity");
      }

      if (!PackManager.v().hasPhase("jap.pat")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.pat");
      }

      if (!PackManager.v().hasPhase("jap.lvtagger")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.lvtagger");
      }

      if (!PackManager.v().hasPhase("jap.rdtagger")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.rdtagger");
      }

      if (!PackManager.v().hasPhase("jap.che")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.che");
      }

      if (!PackManager.v().hasPhase("jap.umt")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.umt");
      }

      if (!PackManager.v().hasPhase("jap.lit")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.lit");
      }

      if (!PackManager.v().hasPhase("jap.aet")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.aet");
      }

      if (!PackManager.v().hasPhase("jap.dmt")) {
         G.v().out.println("Warning: Options exist for non-existent phase jap.dmt");
      }

      if (!PackManager.v().hasPhase("gb")) {
         G.v().out.println("Warning: Options exist for non-existent phase gb");
      }

      if (!PackManager.v().hasPhase("gb.a1")) {
         G.v().out.println("Warning: Options exist for non-existent phase gb.a1");
      }

      if (!PackManager.v().hasPhase("gb.cf")) {
         G.v().out.println("Warning: Options exist for non-existent phase gb.cf");
      }

      if (!PackManager.v().hasPhase("gb.a2")) {
         G.v().out.println("Warning: Options exist for non-existent phase gb.a2");
      }

      if (!PackManager.v().hasPhase("gb.ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase gb.ule");
      }

      if (!PackManager.v().hasPhase("gop")) {
         G.v().out.println("Warning: Options exist for non-existent phase gop");
      }

      if (!PackManager.v().hasPhase("bb")) {
         G.v().out.println("Warning: Options exist for non-existent phase bb");
      }

      if (!PackManager.v().hasPhase("bb.lso")) {
         G.v().out.println("Warning: Options exist for non-existent phase bb.lso");
      }

      if (!PackManager.v().hasPhase("bb.sco")) {
         G.v().out.println("Warning: Options exist for non-existent phase bb.sco");
      }

      if (!PackManager.v().hasPhase("bb.pho")) {
         G.v().out.println("Warning: Options exist for non-existent phase bb.pho");
      }

      if (!PackManager.v().hasPhase("bb.ule")) {
         G.v().out.println("Warning: Options exist for non-existent phase bb.ule");
      }

      if (!PackManager.v().hasPhase("bb.lp")) {
         G.v().out.println("Warning: Options exist for non-existent phase bb.lp");
      }

      if (!PackManager.v().hasPhase("bop")) {
         G.v().out.println("Warning: Options exist for non-existent phase bop");
      }

      if (!PackManager.v().hasPhase("tag")) {
         G.v().out.println("Warning: Options exist for non-existent phase tag");
      }

      if (!PackManager.v().hasPhase("tag.ln")) {
         G.v().out.println("Warning: Options exist for non-existent phase tag.ln");
      }

      if (!PackManager.v().hasPhase("tag.an")) {
         G.v().out.println("Warning: Options exist for non-existent phase tag.an");
      }

      if (!PackManager.v().hasPhase("tag.dep")) {
         G.v().out.println("Warning: Options exist for non-existent phase tag.dep");
      }

      if (!PackManager.v().hasPhase("tag.fieldrw")) {
         G.v().out.println("Warning: Options exist for non-existent phase tag.fieldrw");
      }

      if (!PackManager.v().hasPhase("db")) {
         G.v().out.println("Warning: Options exist for non-existent phase db");
      }

      if (!PackManager.v().hasPhase("db.transformations")) {
         G.v().out.println("Warning: Options exist for non-existent phase db.transformations");
      }

      if (!PackManager.v().hasPhase("db.renamer")) {
         G.v().out.println("Warning: Options exist for non-existent phase db.renamer");
      }

      if (!PackManager.v().hasPhase("db.deobfuscate")) {
         G.v().out.println("Warning: Options exist for non-existent phase db.deobfuscate");
      }

      if (!PackManager.v().hasPhase("db.force-recompile")) {
         G.v().out.println("Warning: Options exist for non-existent phase db.force-recompile");
      }

   }
}
