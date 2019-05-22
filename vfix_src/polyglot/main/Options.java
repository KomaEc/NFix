package polyglot.main;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;
import polyglot.frontend.ExtensionInfo;

public class Options {
   public static Options global;
   protected ExtensionInfo extension = null;
   public int error_count = 100;
   public Collection source_path;
   public File output_directory;
   public String default_classpath;
   public String classpath;
   public String bootclasspath = null;
   public boolean assertions = false;
   public String[] source_ext = null;
   public String output_ext = "java";
   public boolean output_stdout = false;
   public String post_compiler;
   public int output_width = 120;
   public boolean fully_qualified_names = false;
   public boolean serialize_type_info = true;
   public Set dump_ast = new HashSet();
   public Set print_ast = new HashSet();
   public Set disable_passes = new HashSet();
   public boolean keep_output_files = true;
   protected int USAGE_SCREEN_WIDTH = 76;
   protected int USAGE_FLAG_WIDTH = 27;
   protected int USAGE_SUBSECTION_INDENT = 8;

   public boolean cppBackend() {
      return false;
   }

   public Options(ExtensionInfo extension) {
      this.extension = extension;
      this.setDefaultValues();
   }

   public void setDefaultValues() {
      String default_bootpath = System.getProperty("sun.boot.class.path");
      if (default_bootpath == null) {
         default_bootpath = System.getProperty("java.home") + File.separator + "jre" + File.separator + "lib" + File.separator + "rt.jar";
      }

      this.default_classpath = System.getProperty("java.class.path") + File.pathSeparator + default_bootpath;
      this.classpath = this.default_classpath;
      String java_home = System.getProperty("java.home");
      String current_dir = System.getProperty("user.dir");
      this.source_path = new LinkedList();
      this.source_path.add(new File(current_dir));
      this.output_directory = new File(current_dir);
      this.post_compiler = java_home + File.separator + ".." + File.separator + "bin" + File.separator + "javac";
      if (!(new File(this.post_compiler)).exists()) {
         this.post_compiler = java_home + File.separator + "bin" + File.separator + "javac";
         if (!(new File(this.post_compiler)).exists()) {
            this.post_compiler = "javac";
         }
      }

   }

   public void parseCommandLine(String[] args, Set source) throws UsageError {
      if (args.length < 1) {
         throw new UsageError("No command line arguments given");
      } else {
         int i = 0;

         while(i < args.length) {
            try {
               int ni = this.parseCommand(args, i, source);
               if (ni == i) {
                  throw new UsageError("illegal option -- " + args[i]);
               }

               i = ni;
            } catch (ArrayIndexOutOfBoundsException var5) {
               throw new UsageError("missing argument");
            }
         }

         if (source.size() < 1) {
            throw new UsageError("must specify at least one source file");
         }
      }
   }

   protected int parseCommand(String[] args, int index, Set source) throws UsageError, Main.TerminationException {
      int i = index;
      if (!args[index].equals("-h") && !args[index].equals("-help") && !args[index].equals("--help")) {
         if (args[index].equals("-version")) {
            StringBuffer sb = new StringBuffer();
            if (this.extension != null) {
               sb.append(this.extension.compilerName() + " version " + this.extension.version() + "\n");
            }

            sb.append("Polyglot compiler toolkit version " + new polyglot.ext.jl.Version());
            throw new Main.TerminationException(sb.toString(), 0);
         } else {
            if (args[index].equals("-d")) {
               i = index + 1;
               this.output_directory = new File(args[i]);
               ++i;
            } else if (!args[index].equals("-classpath") && !args[index].equals("-cp")) {
               if (args[index].equals("-bootclasspath")) {
                  i = index + 1;
                  this.bootclasspath = args[i];
                  ++i;
               } else if (args[index].equals("-sourcepath")) {
                  i = index + 1;
                  StringTokenizer st = new StringTokenizer(args[i], File.pathSeparator);

                  while(st.hasMoreTokens()) {
                     File f = new File(st.nextToken());
                     if (f != null && !this.source_path.contains(f)) {
                        this.source_path.add(f);
                     }
                  }

                  ++i;
               } else if (args[index].equals("-assert")) {
                  i = index + 1;
                  this.assertions = true;
               } else if (args[index].equals("-fqcn")) {
                  i = index + 1;
                  this.fully_qualified_names = true;
               } else if (args[index].equals("-c")) {
                  this.post_compiler = null;
                  i = index + 1;
               } else if (args[index].equals("-errors")) {
                  i = index + 1;

                  try {
                     this.error_count = Integer.parseInt(args[i]);
                  } catch (NumberFormatException var12) {
                  }

                  ++i;
               } else if (args[index].equals("-w")) {
                  i = index + 1;

                  try {
                     this.output_width = Integer.parseInt(args[i]);
                  } catch (NumberFormatException var11) {
                  }

                  ++i;
               } else if (args[index].equals("-post")) {
                  i = index + 1;
                  this.post_compiler = args[i];
                  ++i;
               } else if (args[index].equals("-stdout")) {
                  i = index + 1;
                  this.output_stdout = true;
               } else if (args[index].equals("-sx")) {
                  i = index + 1;
                  if (this.source_ext == null) {
                     this.source_ext = new String[]{args[i]};
                  } else {
                     String[] s = new String[this.source_ext.length + 1];
                     System.arraycopy(this.source_ext, 0, s, 0, this.source_ext.length);
                     s[s.length - 1] = args[i];
                     this.source_ext = s;
                  }

                  ++i;
               } else if (args[index].equals("-ox")) {
                  i = index + 1;
                  this.output_ext = args[i];
                  ++i;
               } else if (args[index].equals("-noserial")) {
                  i = index + 1;
                  this.serialize_type_info = false;
               } else {
                  String pass_name;
                  if (args[index].equals("-dump")) {
                     i = index + 1;
                     pass_name = args[i];
                     this.dump_ast.add(pass_name);
                     ++i;
                  } else if (args[index].equals("-print")) {
                     i = index + 1;
                     pass_name = args[i];
                     this.print_ast.add(pass_name);
                     ++i;
                  } else if (args[index].equals("-disable")) {
                     i = index + 1;
                     pass_name = args[i];
                     this.disable_passes.add(pass_name);
                     ++i;
                  } else if (args[index].equals("-nooutput")) {
                     i = index + 1;
                     this.keep_output_files = false;
                     this.output_width = 1000;
                  } else if (!args[index].equals("-v") && !args[index].equals("-verbose")) {
                     if (args[index].equals("-report")) {
                        i = index + 1;
                        String var10000 = args[i];
                        StringTokenizer st = new StringTokenizer(args[i], "=");
                        String topic = "";
                        int level = 0;
                        if (st.hasMoreTokens()) {
                           topic = st.nextToken();
                        }

                        if (st.hasMoreTokens()) {
                           try {
                              level = Integer.parseInt(st.nextToken());
                           } catch (NumberFormatException var10) {
                           }
                        }

                        Report.addTopic(topic, level);
                        ++i;
                     } else if (!args[index].startsWith("-")) {
                        source.add(args[index]);
                        File f = (new File(args[index])).getParentFile();
                        if (f != null && !this.source_path.contains(f)) {
                           this.source_path.add(f);
                        }

                        i = index + 1;
                     }
                  } else {
                     i = index + 1;
                     Report.addTopic("verbose", 1);
                  }
               }
            } else {
               i = index + 1;
               this.classpath = args[i] + System.getProperty("path.separator") + this.default_classpath;
               ++i;
            }

            return i;
         }
      } else {
         throw new UsageError("", 0);
      }
   }

   public void usage(PrintStream out) {
      out.println("usage: " + this.extension.compilerName() + " [options] " + "<source-file>." + this.extension.fileExtensions()[0] + " ...");
      out.println("where [options] includes:");
      this.usageForFlag(out, "@<file>", "read options from <file>");
      this.usageForFlag(out, "-d <directory>", "output directory");
      this.usageForFlag(out, "-assert", "recognize the assert keyword");
      this.usageForFlag(out, "-sourcepath <path>", "source path");
      this.usageForFlag(out, "-bootclasspath <path>", "path for bootstrap class files");
      this.usageForFlag(out, "-ext <extension>", "use language extension");
      this.usageForFlag(out, "-extclass <ext-class>", "use language extension");
      this.usageForFlag(out, "-fqcn", "use fully-qualified class names");
      this.usageForFlag(out, "-sx <ext>", "set source extension");
      this.usageForFlag(out, "-ox <ext>", "set output extension");
      this.usageForFlag(out, "-errors <num>", "set the maximum number of errors");
      this.usageForFlag(out, "-w <num>", "set the maximum width of the .java output files");
      this.usageForFlag(out, "-dump <pass>", "dump the ast after pass <pass>");
      this.usageForFlag(out, "-print <pass>", "pretty-print the ast after pass <pass>");
      this.usageForFlag(out, "-disable <pass>", "disable pass <pass>");
      this.usageForFlag(out, "-noserial", "disable class serialization");
      this.usageForFlag(out, "-nooutput", "delete output files after compilation");
      this.usageForFlag(out, "-c", "compile only to .java");
      this.usageForFlag(out, "-post <compiler>", "run javac-like compiler after translation");
      this.usageForFlag(out, "-v -verbose", "print verbose debugging information");
      this.usageForFlag(out, "-report <topic>=<level>", "print verbose debugging information about topic at specified verbosity");
      StringBuffer allowedTopics = new StringBuffer("Allowed topics: ");
      Iterator iter = Report.topics.iterator();

      while(iter.hasNext()) {
         allowedTopics.append(iter.next().toString());
         if (iter.hasNext()) {
            allowedTopics.append(", ");
         }
      }

      this.usageSubsection(out, allowedTopics.toString());
      this.usageForFlag(out, "-version", "print version info");
      this.usageForFlag(out, "-h", "print this message");
   }

   protected void usageForFlag(PrintStream out, String flag, String description) {
      out.print("  ");
      out.print(flag);
      int cur = flag.length() + 2;
      if (cur < this.USAGE_FLAG_WIDTH) {
         printSpaces(out, this.USAGE_FLAG_WIDTH - cur);
      } else {
         out.println();
         printSpaces(out, this.USAGE_FLAG_WIDTH);
      }

      cur = this.USAGE_FLAG_WIDTH;
      StringTokenizer st = new StringTokenizer(description);

      while(st.hasMoreTokens()) {
         String s = st.nextToken();
         if (cur + s.length() > this.USAGE_SCREEN_WIDTH) {
            out.println();
            printSpaces(out, this.USAGE_FLAG_WIDTH);
            cur = this.USAGE_FLAG_WIDTH;
         }

         out.print(s);
         cur += s.length();
         if (st.hasMoreTokens()) {
            if (cur + 1 > this.USAGE_SCREEN_WIDTH) {
               out.println();
               printSpaces(out, this.USAGE_FLAG_WIDTH);
               cur = this.USAGE_FLAG_WIDTH;
            } else {
               out.print(" ");
               ++cur;
            }
         }
      }

      out.println();
   }

   protected void usageSubsection(PrintStream out, String text) {
      printSpaces(out, this.USAGE_SUBSECTION_INDENT);
      int cur = this.USAGE_SUBSECTION_INDENT;
      StringTokenizer st = new StringTokenizer(text);

      while(st.hasMoreTokens()) {
         String s = st.nextToken();
         if (cur + s.length() > this.USAGE_SCREEN_WIDTH) {
            out.println();
            printSpaces(out, this.USAGE_SUBSECTION_INDENT);
            cur = this.USAGE_SUBSECTION_INDENT;
         }

         out.print(s);
         cur += s.length();
         if (st.hasMoreTokens()) {
            if (cur + 1 > this.USAGE_SCREEN_WIDTH) {
               out.println();
               printSpaces(out, this.USAGE_SUBSECTION_INDENT);
               cur = this.USAGE_SUBSECTION_INDENT;
            } else {
               out.print(' ');
               ++cur;
            }
         }
      }

      out.println();
   }

   protected static void printSpaces(PrintStream out, int n) {
      while(n-- > 0) {
         out.print(' ');
      }

   }

   public String constructFullClasspath() {
      StringBuffer fullcp = new StringBuffer();
      if (this.bootclasspath != null) {
         fullcp.append(this.bootclasspath);
      }

      fullcp.append(this.classpath);
      return fullcp.toString();
   }

   public String constructPostCompilerClasspath() {
      return this.output_directory + File.pathSeparator + "." + File.pathSeparator + System.getProperty("java.class.path");
   }
}
