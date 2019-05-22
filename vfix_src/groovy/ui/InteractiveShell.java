package groovy.ui;

import com.gzoltar.shaded.jline.ConsoleReader;
import com.gzoltar.shaded.jline.SimpleCompletor;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovyjarjarcommonscli.CommandLine;
import groovyjarjarcommonscli.CommandLineParser;
import groovyjarjarcommonscli.HelpFormatter;
import groovyjarjarcommonscli.OptionBuilder;
import groovyjarjarcommonscli.Options;
import groovyjarjarcommonscli.PosixParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.tools.ErrorReporter;
import org.codehaus.groovy.tools.shell.util.MessageSource;

/** @deprecated */
@Deprecated
public class InteractiveShell implements Runnable {
   private static final String NEW_LINE = System.getProperty("line.separator");
   private static final MessageSource MESSAGES = new MessageSource(InteractiveShell.class);
   private final GroovyShell shell;
   private final InputStream in;
   private final PrintStream out;
   private final PrintStream err;
   private final ConsoleReader reader;
   private Object lastResult;
   private Closure beforeExecution;
   private Closure afterExecution;
   private StringBuffer accepted;
   private String pending;
   private int line;
   private boolean stale;
   private SourceUnit parser;
   private Exception error;
   private static final int COMMAND_ID_EXIT = 0;
   private static final int COMMAND_ID_HELP = 1;
   private static final int COMMAND_ID_DISCARD = 2;
   private static final int COMMAND_ID_DISPLAY = 3;
   private static final int COMMAND_ID_EXPLAIN = 4;
   private static final int COMMAND_ID_EXECUTE = 5;
   private static final int COMMAND_ID_BINDING = 6;
   private static final int COMMAND_ID_DISCARD_LOADED_CLASSES = 7;
   private static final int COMMAND_ID_INSPECT = 8;
   private static final int LAST_COMMAND_ID = 8;
   private static final String[] COMMANDS = new String[]{"exit", "help", "discard", "display", "explain", "execute", "binding", "discardclasses", "inspect"};
   private static final Map<String, Integer> COMMAND_MAPPINGS = new HashMap();
   private static final Map<String, String> COMMAND_HELP;

   public static void main(String[] args) {
      try {
         processCommandLineArguments(args);
         InteractiveShell groovy = new InteractiveShell();
         groovy.run();
      } catch (Exception var2) {
         System.err.println("FATAL: " + var2);
         var2.printStackTrace();
         System.exit(1);
      }

      System.exit(0);
   }

   private static void processCommandLineArguments(String[] args) throws Exception {
      assert args != null;

      Options options = new Options();
      OptionBuilder.withLongOpt("help");
      OptionBuilder.withDescription(MESSAGES.getMessage("cli.option.help.description"));
      options.addOption(OptionBuilder.create('h'));
      OptionBuilder.withLongOpt("version");
      OptionBuilder.withDescription(MESSAGES.getMessage("cli.option.version.description"));
      options.addOption(OptionBuilder.create('V'));
      CommandLineParser parser = new PosixParser();
      CommandLine line = parser.parse(options, args, true);
      String[] lineargs = line.getArgs();
      if (lineargs.length != 0) {
         System.err.println(MESSAGES.format("cli.info.unexpected_args", new Object[]{DefaultGroovyMethods.join((Object[])lineargs, " ")}));
         System.exit(1);
      }

      PrintWriter writer = new PrintWriter(System.out);
      if (line.hasOption('h')) {
         HelpFormatter formatter = new HelpFormatter();
         formatter.printHelp(writer, 80, "groovysh [options]", "", options, 4, 4, "", false);
         writer.flush();
         System.exit(0);
      }

      if (line.hasOption('V')) {
         writer.println(MESSAGES.format("cli.info.version", new Object[]{GroovySystem.getVersion()}));
         writer.flush();
         System.exit(0);
      }

   }

   public InteractiveShell() throws IOException {
      this(System.in, System.out, System.err);
   }

   public InteractiveShell(InputStream in, PrintStream out, PrintStream err) throws IOException {
      this((ClassLoader)null, new Binding(), in, out, err);
   }

   public InteractiveShell(Binding binding, InputStream in, PrintStream out, PrintStream err) throws IOException {
      this((ClassLoader)null, binding, in, out, err);
   }

   public InteractiveShell(ClassLoader parent, Binding binding, InputStream in, PrintStream out, PrintStream err) throws IOException {
      this.accepted = new StringBuffer();
      this.stale = false;

      assert binding != null;

      assert in != null;

      assert out != null;

      assert err != null;

      this.in = in;
      this.out = out;
      this.err = err;
      Writer writer = new OutputStreamWriter(out);
      this.reader = new ConsoleReader(in, writer);
      this.reader.setDefaultPrompt("groovy> ");
      this.reader.addCompletor(new InteractiveShell.CommandNameCompletor());
      if (parent != null) {
         this.shell = new GroovyShell(parent, binding);
      } else {
         this.shell = new GroovyShell(binding);
      }

      Map map = this.shell.getContext().getVariables();
      if (map.get("shell") != null) {
         map.put("shell", this.shell);
      }

   }

   public void run() {
      this.out.println(MESSAGES.format("startup_banner.0", new Object[]{GroovySystem.getVersion(), System.getProperty("java.version")}));
      this.out.println(MESSAGES.getMessage("startup_banner.1"));

      while(true) {
         String code;
         do {
            code = this.read();
            if (code == null) {
               return;
            }

            this.reset();
         } while(code.length() <= 0);

         try {
            if (this.beforeExecution != null) {
               this.beforeExecution.call();
            }

            this.lastResult = this.shell.evaluate(code);
            if (this.afterExecution != null) {
               this.afterExecution.call();
            }

            this.out.print("===> ");
            this.out.println(this.lastResult);
         } catch (CompilationFailedException var3) {
            this.err.println(var3);
         } catch (Throwable var4) {
            Throwable e = var4;
            if (var4 instanceof InvokerInvocationException) {
               e = var4.getCause();
            }

            this.filterAndPrintStackTrace(e);
         }
      }
   }

   public void setBeforeExecution(Closure beforeExecution) {
      this.beforeExecution = beforeExecution;
   }

   public void setAfterExecution(Closure afterExecution) {
      this.afterExecution = afterExecution;
   }

   private void filterAndPrintStackTrace(Throwable cause) {
      assert cause != null;

      this.err.print("ERROR: ");
      this.err.println(cause);
      cause.printStackTrace(this.err);
   }

   protected void reset() {
      this.stale = true;
      this.pending = null;
      this.line = 1;
      this.parser = null;
      this.error = null;
   }

   protected String read() {
      this.reset();
      boolean complete = false;
      boolean done = false;

      while(!done) {
         try {
            this.pending = this.reader.readLine();
         } catch (IOException var5) {
         }

         if (this.pending == null) {
            return null;
         }

         String command = this.pending.trim();
         if (COMMAND_MAPPINGS.containsKey(command)) {
            int code = (Integer)COMMAND_MAPPINGS.get(command);
            switch(code) {
            case 0:
               return null;
            case 1:
               this.displayHelp();
               break;
            case 2:
               this.reset();
               done = true;
               break;
            case 3:
               this.displayStatement();
               break;
            case 4:
               this.explainStatement();
               break;
            case 5:
               if (complete) {
                  done = true;
               } else {
                  this.err.println(MESSAGES.getMessage("command.execute.not_complete"));
               }
               break;
            case 6:
               this.displayBinding();
               break;
            case 7:
               this.resetLoadedClasses();
               break;
            case 8:
               this.inspect();
               break;
            default:
               throw new Error("BUG: Unknown command for code: " + code);
            }
         } else {
            this.freshen();
            if (this.pending.trim().length() == 0) {
               this.accept();
            } else {
               String code = this.current();
               if (this.parse(code)) {
                  this.accept();
                  complete = true;
               } else if (this.error == null) {
                  this.accept();
               } else {
                  this.report();
               }
            }
         }
      }

      return this.accepted(complete);
   }

   private void inspect() {
      if (this.lastResult == null) {
         this.err.println(MESSAGES.getMessage("command.inspect.no_result"));
      } else {
         try {
            Class type = Class.forName("groovy.inspect.swingui.ObjectBrowser");
            Method method = type.getMethod("inspect", Object.class);
            method.invoke(type, this.lastResult);
         } catch (Exception var3) {
            this.err.println("Cannot invoke ObjectBrowser");
            var3.printStackTrace();
         }

      }
   }

   private String accepted(boolean complete) {
      return complete ? this.accepted.toString() : "";
   }

   private String current() {
      return this.accepted.toString() + this.pending + NEW_LINE;
   }

   private void accept() {
      this.accepted.append(this.pending).append(NEW_LINE);
      ++this.line;
   }

   private void freshen() {
      if (this.stale) {
         this.accepted.setLength(0);
         this.stale = false;
      }

   }

   private boolean parse(String code, int tolerance) {
      assert code != null;

      boolean parsed = false;
      this.parser = null;
      this.error = null;

      try {
         this.parser = SourceUnit.create("groovysh-script", code, tolerance);
         this.parser.parse();
         parsed = true;
      } catch (CompilationFailedException var5) {
         if (this.parser.getErrorCollector().getErrorCount() > 1 || !this.parser.failedWithUnexpectedEOF()) {
            this.error = var5;
         }
      } catch (Exception var6) {
         this.error = var6;
      }

      return parsed;
   }

   private boolean parse(String code) {
      return this.parse(code, 1);
   }

   private void report() {
      this.err.println("Discarding invalid text:");
      (new ErrorReporter(this.error, false)).write(this.err);
   }

   private void displayHelp() {
      this.out.println(MESSAGES.getMessage("command.help.available_commands"));

      for(int i = 0; i <= 8; ++i) {
         this.out.print("    ");
         this.out.println((String)COMMAND_HELP.get(COMMANDS[i]));
      }

   }

   private void displayStatement() {
      String[] lines = this.accepted.toString().split(NEW_LINE);
      if (lines.length == 1 && lines[0].trim().equals("")) {
         this.out.println(MESSAGES.getMessage("command.display.buffer_empty"));
      } else {
         int padsize = 2;
         if (lines.length >= 10) {
            ++padsize;
         }

         if (lines.length >= 100) {
            ++padsize;
         }

         if (lines.length >= 1000) {
            ++padsize;
         }

         for(int i = 0; i < lines.length; ++i) {
            String lineno = DefaultGroovyMethods.padLeft(String.valueOf(i + 1), padsize, " ");
            this.out.print(lineno);
            this.out.print("> ");
            this.out.println(lines[i]);
         }
      }

   }

   private void displayBinding() {
      Binding context = this.shell.getContext();
      Map variables = context.getVariables();
      Set set = variables.keySet();
      if (set.isEmpty()) {
         this.out.println(MESSAGES.getMessage("command.binding.binding_empty"));
      } else {
         this.out.println(MESSAGES.getMessage("command.binding.available_variables"));
         Iterator i$ = set.iterator();

         while(i$.hasNext()) {
            Object key = i$.next();
            this.out.print("    ");
            this.out.print(key);
            this.out.print(" = ");
            this.out.println(variables.get(key));
         }
      }

   }

   private void explainStatement() {
      if (!this.parse(this.accepted(true), 10) && this.error != null) {
         this.out.println(MESSAGES.getMessage("command.explain.unparsable"));
      } else {
         this.out.println(MESSAGES.getMessage("command.explain.tree_header"));
      }

   }

   private void resetLoadedClasses() {
      this.shell.resetLoadedClasses();
      this.out.println(MESSAGES.getMessage("command.discardclasses.classdefs_discarded"));
   }

   static {
      for(int i = 0; i <= 8; ++i) {
         COMMAND_MAPPINGS.put(COMMANDS[i], i);
      }

      COMMAND_MAPPINGS.put("quit", 0);
      COMMAND_MAPPINGS.put("go", 5);
      COMMAND_HELP = new HashMap();
      COMMAND_HELP.put(COMMANDS[0], "exit/quit         - " + MESSAGES.getMessage("command.exit.descripion"));
      COMMAND_HELP.put(COMMANDS[1], "help              - " + MESSAGES.getMessage("command.help.descripion"));
      COMMAND_HELP.put(COMMANDS[2], "discard           - " + MESSAGES.getMessage("command.discard.descripion"));
      COMMAND_HELP.put(COMMANDS[3], "display           - " + MESSAGES.getMessage("command.display.descripion"));
      COMMAND_HELP.put(COMMANDS[4], "explain           - " + MESSAGES.getMessage("command.explain.descripion"));
      COMMAND_HELP.put(COMMANDS[5], "execute/go        - " + MESSAGES.getMessage("command.execute.descripion"));
      COMMAND_HELP.put(COMMANDS[6], "binding           - " + MESSAGES.getMessage("command.binding.descripion"));
      COMMAND_HELP.put(COMMANDS[7], "discardclasses    - " + MESSAGES.getMessage("command.discardclasses.descripion"));
      COMMAND_HELP.put(COMMANDS[8], "inspect           - " + MESSAGES.getMessage("command.inspect.descripion"));
   }

   private class CommandNameCompletor extends SimpleCompletor {
      public CommandNameCompletor() {
         super(new String[0]);
         Iterator iter = InteractiveShell.COMMAND_MAPPINGS.keySet().iterator();

         while(iter.hasNext()) {
            this.addCandidateString((String)iter.next());
         }

      }
   }
}
