package java_cup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java_cup.runtime.ComplexSymbolFactory;

public class Main {
   protected static boolean print_progress = false;
   protected static boolean opt_dump_states = false;
   protected static boolean opt_dump_tables = false;
   protected static boolean opt_dump_grammar = false;
   protected static boolean opt_show_timing = false;
   protected static boolean opt_do_debug = false;
   protected static boolean opt_do_debugsymbols = false;
   protected static boolean opt_compact_red = false;
   protected static boolean include_non_terms = false;
   protected static boolean no_summary = false;
   protected static int expect_conflicts = 0;
   protected static boolean lr_values = true;
   protected static boolean locations = false;
   protected static boolean xmlactions = false;
   protected static boolean genericlabels = false;
   protected static boolean sym_interface = false;
   protected static boolean suppress_scanner = false;
   protected static long start_time = 0L;
   protected static long prelim_end = 0L;
   protected static long parse_end = 0L;
   protected static long check_end = 0L;
   protected static long dump_end = 0L;
   protected static long build_end = 0L;
   protected static long nullability_end = 0L;
   protected static long first_end = 0L;
   protected static long machine_end = 0L;
   protected static long table_end = 0L;
   protected static long reduce_check_end = 0L;
   protected static long emit_end = 0L;
   protected static long final_time = 0L;
   protected static BufferedInputStream input_file;
   protected static PrintWriter parser_class_file;
   protected static PrintWriter symbol_class_file;
   protected static File dest_dir = null;
   protected static lalr_state start_state;
   protected static parse_action_table action_table;
   protected static parse_reduce_table reduce_table;

   private Main() {
   }

   public static void main(String[] argv) throws internal_error, IOException, Exception {
      boolean did_output = false;
      start_time = System.currentTimeMillis();
      terminal.clear();
      production.clear();
      action_production.clear();
      emit.clear();
      non_terminal.clear();
      parse_reduce_row.clear();
      parse_action_row.clear();
      lalr_state.clear();
      parse_args(argv);
      emit.set_lr_values(lr_values);
      emit.set_locations(locations);
      emit.set_xmlactions(xmlactions);
      emit.set_genericlabels(genericlabels);
      if (print_progress) {
         System.err.println("Opening files...");
      }

      input_file = new BufferedInputStream(System.in);
      prelim_end = System.currentTimeMillis();
      if (print_progress) {
         System.err.println("Parsing specification from standard input...");
      }

      parse_grammar_spec();
      parse_end = System.currentTimeMillis();
      if (ErrorManager.getManager().getErrorCount() == 0) {
         if (print_progress) {
            System.err.println("Checking specification...");
         }

         check_unused();
         check_end = System.currentTimeMillis();
         if (print_progress) {
            System.err.println("Building parse tables...");
         }

         build_parser();
         build_end = System.currentTimeMillis();
         if (ErrorManager.getManager().getErrorCount() != 0) {
            opt_dump_tables = false;
         } else {
            if (print_progress) {
               System.err.println("Writing parser...");
            }

            open_files();
            emit_parser();
            did_output = true;
         }
      }

      emit_end = System.currentTimeMillis();
      if (opt_dump_grammar) {
         dump_grammar();
      }

      if (opt_dump_states) {
         dump_machine();
      }

      if (opt_dump_tables) {
         dump_tables();
      }

      dump_end = System.currentTimeMillis();
      if (print_progress) {
         System.err.println("Closing files...");
      }

      close_files();
      if (!no_summary) {
         emit_summary(did_output);
      }

      if (ErrorManager.getManager().getErrorCount() != 0) {
         System.exit(100);
      }

   }

   protected static void usage(String message) {
      System.err.println();
      System.err.println(message);
      System.err.println();
      System.err.println("CUP v0.11b beta 20140226\nUsage: java_cup [options] [filename]\n  and expects a specification file on standard input if no filename is given.\n  Legal options include:\n    -package name  specify package generated classes go in [default none]\n    -destdir name  specify the destination directory, to store the generated files in\n    -parser name   specify parser class name [default \"parser\"]\n    -typearg args  specify type arguments for parser class\n    -symbols name  specify name for symbol constant class [default \"sym\"]\n    -interface     put symbols in an interface, rather than a class\n    -nonterms      put non terminals in symbol constant class\n    -expect #      number of conflicts expected/allowed [default 0]\n    -compact_red   compact tables by defaulting to most frequent reduce\n    -nowarn        don't warn about useless productions, etc.\n    -nosummary     don't print the usual summary of parse states, etc.\n    -nopositions   don't propagate the left and right token position values\n    -locations     generate handles xleft/xright for symbol positions in actions\n    -xmlactions    make the generated parser yield its parse tree as XML\n    -genericlabels automatically generate labels to all symbols in XML mode\n    -noscanner     don't refer to java_cup.runtime.Scanner\n    -progress      print messages to indicate progress of the system\n    -time          print time usage summary\n    -dump_grammar  produce a human readable dump of the symbols and grammar\n    -dump_states   produce a dump of parse state machine\n    -dump_tables   produce a dump of the parse tables\n    -dump          produce a dump of all of the above\n    -version       print the version information for CUP and exit\n");
      System.exit(1);
   }

   protected static void parse_args(String[] argv) {
      int len = argv.length;

      for(int i = 0; i < len; ++i) {
         if (argv[i].equals("-package")) {
            ++i;
            if (i >= len || argv[i].startsWith("-") || argv[i].endsWith(".cup")) {
               usage("-package must have a name argument");
            }

            emit.package_name = argv[i];
         } else if (argv[i].equals("-destdir")) {
            ++i;
            if (i >= len || argv[i].startsWith("-") || argv[i].endsWith(".cup")) {
               usage("-destdir must have a name argument");
            }

            dest_dir = new File(argv[i]);
         } else if (argv[i].equals("-parser")) {
            ++i;
            if (i >= len || argv[i].startsWith("-") || argv[i].endsWith(".cup")) {
               usage("-parser must have a name argument");
            }

            emit.parser_class_name = argv[i];
         } else if (argv[i].equals("-symbols")) {
            ++i;
            if (i >= len || argv[i].startsWith("-") || argv[i].endsWith(".cup")) {
               usage("-symbols must have a name argument");
            }

            emit.symbol_const_class_name = argv[i];
         } else if (argv[i].equals("-nonterms")) {
            include_non_terms = true;
         } else if (argv[i].equals("-expect")) {
            ++i;
            if (i >= len || argv[i].startsWith("-") || argv[i].endsWith(".cup")) {
               usage("-expect must have a name argument");
            }

            try {
               expect_conflicts = Integer.parseInt(argv[i]);
            } catch (NumberFormatException var5) {
               usage("-expect must be followed by a decimal integer");
            }
         } else if (argv[i].equals("-compact_red")) {
            opt_compact_red = true;
         } else if (argv[i].equals("-nosummary")) {
            no_summary = true;
         } else if (argv[i].equals("-nowarn")) {
            emit.nowarn = true;
         } else if (argv[i].equals("-dump_states")) {
            opt_dump_states = true;
         } else if (argv[i].equals("-dump_tables")) {
            opt_dump_tables = true;
         } else if (argv[i].equals("-progress")) {
            print_progress = true;
         } else if (argv[i].equals("-dump_grammar")) {
            opt_dump_grammar = true;
         } else if (argv[i].equals("-dump")) {
            opt_dump_grammar = true;
            opt_dump_tables = true;
            opt_dump_states = true;
         } else if (argv[i].equals("-time")) {
            opt_show_timing = true;
         } else if (argv[i].equals("-debug")) {
            opt_do_debug = true;
         } else if (argv[i].equals("-debugsymbols")) {
            opt_do_debugsymbols = true;
         } else if (argv[i].equals("-nopositions")) {
            lr_values = false;
         } else if (argv[i].equals("-locations")) {
            locations = true;
         } else if (argv[i].equals("-xmlactions")) {
            xmlactions = true;
         } else if (argv[i].equals("-genericlabels")) {
            genericlabels = true;
         } else if (argv[i].equals("-interface")) {
            sym_interface = true;
         } else if (argv[i].equals("-noscanner")) {
            suppress_scanner = true;
         } else if (argv[i].equals("-version")) {
            System.out.println("CUP v0.11b beta 20140226");
            System.exit(1);
         } else if (!argv[i].equals("-typearg")) {
            if (!argv[i].startsWith("-") && i == len - 1) {
               try {
                  System.setIn(new FileInputStream(argv[i]));
               } catch (FileNotFoundException var4) {
                  usage("Unable to open \"" + argv[i] + "\" for input");
               }
            } else {
               usage("Unrecognized option \"" + argv[i] + "\"");
            }
         } else {
            ++i;
            if (i >= len || argv[i].startsWith("-") || argv[i].endsWith(".cup")) {
               usage("-symbols must have a name argument");
            }

            emit.class_type_argument = argv[i];
         }
      }

   }

   protected static void open_files() {
      String out_name = emit.parser_class_name + ".java";
      File fil = new File(dest_dir, out_name);

      try {
         parser_class_file = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fil), 4096));
      } catch (Exception var4) {
         System.err.println("Can't open \"" + out_name + "\" for output");
         System.exit(3);
      }

      out_name = emit.symbol_const_class_name + ".java";
      fil = new File(dest_dir, out_name);

      try {
         symbol_class_file = new PrintWriter(new BufferedOutputStream(new FileOutputStream(fil), 4096));
      } catch (Exception var3) {
         System.err.println("Can't open \"" + out_name + "\" for output");
         System.exit(4);
      }

   }

   protected static void close_files() throws IOException {
      if (input_file != null) {
         input_file.close();
      }

      if (parser_class_file != null) {
         parser_class_file.close();
      }

      if (symbol_class_file != null) {
         symbol_class_file.close();
      }

   }

   protected static void parse_grammar_spec() throws Exception {
      ComplexSymbolFactory csf = new ComplexSymbolFactory();
      parser parser_obj = new parser(new Lexer(csf), csf);
      parser_obj.setDebugSymbols(opt_do_debugsymbols);

      try {
         if (opt_do_debug) {
            parser_obj.debug_parse();
         } else {
            parser_obj.parse();
         }

      } catch (Exception var3) {
         ErrorManager.getManager().emit_error("Internal error: Unexpected exception");
         throw var3;
      }
   }

   protected static void check_unused() {
      Enumeration n = terminal.all();

      while(n.hasMoreElements()) {
         terminal term = (terminal)n.nextElement();
         if (term != terminal.EOF && term != terminal.error && term.use_count() == 0) {
            ++emit.unused_term;
            if (!emit.nowarn) {
               ErrorManager.getManager().emit_warning("Terminal \"" + term.name() + "\" was declared but never used");
            }
         }
      }

      n = non_terminal.all();

      while(n.hasMoreElements()) {
         non_terminal nt = (non_terminal)n.nextElement();
         if (nt.use_count() == 0) {
            ++emit.unused_term;
            if (!emit.nowarn) {
               ErrorManager.getManager().emit_warning("Non terminal \"" + nt.name() + "\" was declared but never used");
            }
         }
      }

   }

   protected static void build_parser() throws internal_error {
      if (opt_do_debug || print_progress) {
         System.err.println("  Computing non-terminal nullability...");
      }

      non_terminal.compute_nullability();
      nullability_end = System.currentTimeMillis();
      if (opt_do_debug || print_progress) {
         System.err.println("  Computing first sets...");
      }

      non_terminal.compute_first_sets();
      first_end = System.currentTimeMillis();
      if (opt_do_debug || print_progress) {
         System.err.println("  Building state machine...");
      }

      start_state = lalr_state.build_machine(emit.start_production);
      machine_end = System.currentTimeMillis();
      if (opt_do_debug || print_progress) {
         System.err.println("  Filling in tables...");
      }

      action_table = new parse_action_table();
      reduce_table = new parse_reduce_table();
      Enumeration st = lalr_state.all();

      while(st.hasMoreElements()) {
         lalr_state lst = (lalr_state)st.nextElement();
         lst.build_table_entries(action_table, reduce_table);
      }

      table_end = System.currentTimeMillis();
      if (opt_do_debug || print_progress) {
         System.err.println("  Checking for non-reduced productions...");
      }

      action_table.check_reductions();
      reduce_check_end = System.currentTimeMillis();
      if (emit.num_conflicts > expect_conflicts) {
         ErrorManager.getManager().emit_error("*** More conflicts encountered than expected -- parser generation aborted");
      }

   }

   protected static void emit_parser() throws internal_error {
      emit.symbols(symbol_class_file, include_non_terms, sym_interface);
      emit.parser(parser_class_file, action_table, reduce_table, start_state.index(), emit.start_production, opt_compact_red, suppress_scanner);
   }

   protected static String plural(int val) {
      return val == 1 ? "" : "s";
   }

   protected static void emit_summary(boolean output_produced) {
      final_time = System.currentTimeMillis();
      if (!no_summary) {
         System.err.println("------- CUP v0.11b beta 20140226 Parser Generation Summary -------");
         System.err.println("  " + ErrorManager.getManager().getErrorCount() + " error" + plural(ErrorManager.getManager().getErrorCount()) + " and " + ErrorManager.getManager().getWarningCount() + " warning" + plural(ErrorManager.getManager().getWarningCount()));
         System.err.print("  " + terminal.number() + " terminal" + plural(terminal.number()) + ", ");
         System.err.print(non_terminal.number() + " non-terminal" + plural(non_terminal.number()) + ", and ");
         System.err.println(production.number() + " production" + plural(production.number()) + " declared, ");
         System.err.println("  producing " + lalr_state.number() + " unique parse states.");
         System.err.println("  " + emit.unused_term + " terminal" + plural(emit.unused_term) + " declared but not used.");
         System.err.println("  " + emit.unused_non_term + " non-terminal" + plural(emit.unused_term) + " declared but not used.");
         System.err.println("  " + emit.not_reduced + " production" + plural(emit.not_reduced) + " never reduced.");
         System.err.println("  " + emit.num_conflicts + " conflict" + plural(emit.num_conflicts) + " detected" + " (" + expect_conflicts + " expected).");
         if (output_produced) {
            System.err.println("  Code written to \"" + emit.parser_class_name + ".java\", and \"" + emit.symbol_const_class_name + ".java\".");
         } else {
            System.err.println("  No code produced.");
         }

         if (opt_show_timing) {
            show_times();
         }

         System.err.println("---------------------------------------------------- (CUP v0.11b beta 20140226)");
      }
   }

   protected static void show_times() {
      long total_time = final_time - start_time;
      System.err.println(". . . . . . . . . . . . . . . . . . . . . . . . . ");
      System.err.println("  Timing Summary");
      System.err.println("    Total time       " + timestr(final_time - start_time, total_time));
      System.err.println("      Startup        " + timestr(prelim_end - start_time, total_time));
      System.err.println("      Parse          " + timestr(parse_end - prelim_end, total_time));
      if (check_end != 0L) {
         System.err.println("      Checking       " + timestr(check_end - parse_end, total_time));
      }

      if (check_end != 0L && build_end != 0L) {
         System.err.println("      Parser Build   " + timestr(build_end - check_end, total_time));
      }

      if (nullability_end != 0L && check_end != 0L) {
         System.err.println("        Nullability  " + timestr(nullability_end - check_end, total_time));
      }

      if (first_end != 0L && nullability_end != 0L) {
         System.err.println("        First sets   " + timestr(first_end - nullability_end, total_time));
      }

      if (machine_end != 0L && first_end != 0L) {
         System.err.println("        State build  " + timestr(machine_end - first_end, total_time));
      }

      if (table_end != 0L && machine_end != 0L) {
         System.err.println("        Table build  " + timestr(table_end - machine_end, total_time));
      }

      if (reduce_check_end != 0L && table_end != 0L) {
         System.err.println("        Checking     " + timestr(reduce_check_end - table_end, total_time));
      }

      if (emit_end != 0L && build_end != 0L) {
         System.err.println("      Code Output    " + timestr(emit_end - build_end, total_time));
      }

      if (emit.symbols_time != 0L) {
         System.err.println("        Symbols      " + timestr(emit.symbols_time, total_time));
      }

      if (emit.parser_time != 0L) {
         System.err.println("        Parser class " + timestr(emit.parser_time, total_time));
      }

      if (emit.action_code_time != 0L) {
         System.err.println("          Actions    " + timestr(emit.action_code_time, total_time));
      }

      if (emit.production_table_time != 0L) {
         System.err.println("          Prod table " + timestr(emit.production_table_time, total_time));
      }

      if (emit.action_table_time != 0L) {
         System.err.println("          Action tab " + timestr(emit.action_table_time, total_time));
      }

      if (emit.goto_table_time != 0L) {
         System.err.println("          Reduce tab " + timestr(emit.goto_table_time, total_time));
      }

      System.err.println("      Dump Output    " + timestr(dump_end - emit_end, total_time));
   }

   protected static String timestr(long time_val, long total_time) {
      long ms = 0L;
      long sec = 0L;
      boolean neg = time_val < 0L;
      if (neg) {
         time_val = -time_val;
      }

      ms = time_val % 1000L;
      sec = time_val / 1000L;
      String pad;
      if (sec < 10L) {
         pad = "   ";
      } else if (sec < 100L) {
         pad = "  ";
      } else if (sec < 1000L) {
         pad = " ";
      } else {
         pad = "";
      }

      long percent10 = time_val * 1000L / total_time;
      return (neg ? "-" : "") + pad + sec + "." + ms % 1000L / 100L + ms % 100L / 10L + ms % 10L + "sec" + " (" + percent10 / 10L + "." + percent10 % 10L + "%)";
   }

   public static void dump_grammar() throws internal_error {
      System.err.println("===== Terminals =====");
      int pidx = 0;

      int cnt;
      for(cnt = 0; pidx < terminal.number(); ++cnt) {
         System.err.print("[" + pidx + "]" + terminal.find(pidx).name() + " ");
         if ((cnt + 1) % 5 == 0) {
            System.err.println();
         }

         ++pidx;
      }

      System.err.println();
      System.err.println();
      System.err.println("===== Non terminals =====");
      pidx = 0;

      for(cnt = 0; pidx < non_terminal.number(); ++cnt) {
         System.err.print("[" + pidx + "]" + non_terminal.find(pidx).name() + " ");
         if ((cnt + 1) % 5 == 0) {
            System.err.println();
         }

         ++pidx;
      }

      System.err.println();
      System.err.println();
      System.err.println("===== Productions =====");

      for(pidx = 0; pidx < production.number(); ++pidx) {
         production prod = production.find(pidx);
         System.err.print("[" + pidx + "] " + prod.lhs().the_symbol().name() + " ::= ");

         for(int i = 0; i < prod.rhs_length(); ++i) {
            if (prod.rhs(i).is_action()) {
               System.err.print("{action} ");
            } else {
               System.err.print(((symbol_part)prod.rhs(i)).the_symbol().name() + " ");
            }
         }

         System.err.println();
      }

      System.err.println();
   }

   public static void dump_machine() {
      lalr_state[] ordered = new lalr_state[lalr_state.number()];

      lalr_state st;
      for(Enumeration s = lalr_state.all(); s.hasMoreElements(); ordered[st.index()] = st) {
         st = (lalr_state)s.nextElement();
      }

      System.err.println("===== Viable Prefix Recognizer =====");

      for(int i = 0; i < lalr_state.number(); ++i) {
         if (ordered[i] == start_state) {
            System.err.print("START ");
         }

         System.err.println(ordered[i]);
         System.err.println("-------------------");
      }

   }

   public static void dump_tables() {
      System.err.println(action_table);
      System.err.println(reduce_table);
   }
}
