package soot.JastAddJ;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Frontend {
   private static final Logger logger = LoggerFactory.getLogger(Frontend.class);
   protected Program program = new Program();

   protected Frontend() {
      this.program.state().reset();
   }

   public boolean process(String[] args, BytecodeReader reader, JavaParser parser) {
      this.program.initBytecodeReader(reader);
      this.program.initJavaParser(parser);
      this.initOptions();
      this.processArgs(args);
      Collection files = this.program.options().files();
      if (this.program.options().hasOption("-version")) {
         this.printVersion();
         return true;
      } else if (!this.program.options().hasOption("-help") && !files.isEmpty()) {
         try {
            Iterator iter;
            String name;
            for(iter = files.iterator(); iter.hasNext(); this.program.addSourceFile(name)) {
               name = (String)iter.next();
               if (!(new File(name)).exists()) {
                  System.err.println("WARNING: file \"" + name + "\" does not exist");
               }
            }

            iter = this.program.compilationUnitIterator();

            while(true) {
               CompilationUnit unit;
               do {
                  if (!iter.hasNext()) {
                     return true;
                  }

                  unit = (CompilationUnit)iter.next();
               } while(!unit.fromSource());

               try {
                  Collection errors = unit.parseErrors();
                  Collection warnings = new LinkedList();
                  if (errors.isEmpty() || this.program.options().hasOption("-recover")) {
                     unit.errorCheck(errors, warnings);
                  }

                  if (!errors.isEmpty()) {
                     this.processErrors(errors, unit);
                     return false;
                  }

                  if (!warnings.isEmpty()) {
                     this.processWarnings(warnings, unit);
                  }

                  this.processNoErrors(unit);
               } catch (Throwable var9) {
                  System.err.println("Errors:");
                  System.err.println("Fatal exception while processing " + unit.pathName() + ":");
                  logger.error(var9.getMessage(), var9);
                  return false;
               }
            }
         } catch (Throwable var10) {
            System.err.println("Errors:");
            System.err.println("Fatal exception:");
            logger.error(var10.getMessage(), var10);
            return false;
         }
      } else {
         this.printUsage();
         return true;
      }
   }

   protected void initOptions() {
      Options options = this.program.options();
      options.initOptions();
      options.addKeyOption("-version");
      options.addKeyOption("-print");
      options.addKeyOption("-g");
      options.addKeyOption("-g:none");
      options.addKeyOption("-g:lines,vars,source");
      options.addKeyOption("-nowarn");
      options.addKeyOption("-verbose");
      options.addKeyOption("-deprecation");
      options.addKeyValueOption("-classpath");
      options.addKeyValueOption("-cp");
      options.addKeyValueOption("-sourcepath");
      options.addKeyValueOption("-bootclasspath");
      options.addKeyValueOption("-extdirs");
      options.addKeyValueOption("-d");
      options.addKeyValueOption("-encoding");
      options.addKeyValueOption("-source");
      options.addKeyValueOption("-target");
      options.addKeyOption("-help");
      options.addKeyOption("-O");
      options.addKeyOption("-J-Xmx128M");
      options.addKeyOption("-recover");
   }

   protected void processArgs(String[] args) {
      this.program.options().addOptions(args);
   }

   protected void processErrors(Collection errors, CompilationUnit unit) {
      System.err.println("Errors:");
      Iterator iter2 = errors.iterator();

      while(iter2.hasNext()) {
         System.err.println(iter2.next());
      }

   }

   protected void processWarnings(Collection warnings, CompilationUnit unit) {
      System.err.println("Warnings:");
      Iterator iter2 = warnings.iterator();

      while(iter2.hasNext()) {
         System.err.println(iter2.next());
      }

   }

   protected void processNoErrors(CompilationUnit unit) {
   }

   protected void printUsage() {
      this.printLongVersion();
      System.out.println("\n" + this.name() + "\n\nUsage: java " + this.name() + " <options> <source files>\n  -verbose                  Output messages about what the compiler is doing\n  -classpath <path>         Specify where to find user class files\n  -sourcepath <path>        Specify where to find input source files\n  -bootclasspath <path>     Override location of bootstrap class files\n  -extdirs <dirs>           Override location of installed extensions\n  -d <directory>            Specify where to place generated class files\n  -help                     Print a synopsis of standard options\n  -version                  Print version information\n");
   }

   protected void printLongVersion() {
      System.out.println(this.name() + " " + this.url() + " Version " + this.version());
   }

   protected void printVersion() {
      System.out.println(this.name() + " " + this.version());
   }

   protected String name() {
      return "Java1.4Frontend";
   }

   protected String url() {
      return "(http://jastadd.cs.lth.se)";
   }

   protected String version() {
      return "R20070504";
   }
}
