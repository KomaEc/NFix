package bsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Interpreter implements Runnable, ConsoleInterface, Serializable {
   public static final String VERSION = "2.0b4";
   public static boolean DEBUG;
   public static boolean TRACE;
   public static boolean LOCALSCOPING;
   static transient PrintStream debug;
   static String systemLineSeparator = "\n";
   static This sharedObject;
   private boolean strictJava;
   transient Parser parser;
   NameSpace globalNameSpace;
   transient Reader in;
   transient PrintStream out;
   transient PrintStream err;
   ConsoleInterface console;
   Interpreter parent;
   String sourceFileInfo;
   private boolean exitOnEOF;
   protected boolean evalOnly;
   protected boolean interactive;
   private boolean showResults;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   public Interpreter(Reader var1, PrintStream var2, PrintStream var3, boolean var4, NameSpace var5, Interpreter var6, String var7) {
      this.strictJava = false;
      this.exitOnEOF = true;
      this.parser = new Parser(var1);
      long var8 = System.currentTimeMillis();
      this.in = var1;
      this.out = var2;
      this.err = var3;
      this.interactive = var4;
      debug = var3;
      this.parent = var6;
      if (var6 != null) {
         this.setStrictJava(var6.getStrictJava());
      }

      this.sourceFileInfo = var7;
      BshClassManager var10 = BshClassManager.createClassManager(this);
      if (var5 == null) {
         this.globalNameSpace = new NameSpace(var10, "global");
      } else {
         this.globalNameSpace = var5;
      }

      if (!(this.getu("bsh") instanceof This)) {
         this.initRootSystemObject();
      }

      if (var4) {
         this.loadRCFiles();
      }

      long var11 = System.currentTimeMillis();
      if (DEBUG) {
         debug("Time to initialize interpreter: " + (var11 - var8));
      }

   }

   public Interpreter(Reader var1, PrintStream var2, PrintStream var3, boolean var4, NameSpace var5) {
      this(var1, var2, var3, var4, var5, (Interpreter)null, (String)null);
   }

   public Interpreter(Reader var1, PrintStream var2, PrintStream var3, boolean var4) {
      this(var1, var2, var3, var4, (NameSpace)null);
   }

   public Interpreter(ConsoleInterface var1, NameSpace var2) {
      this(var1.getIn(), var1.getOut(), var1.getErr(), true, var2);
      this.setConsole(var1);
   }

   public Interpreter(ConsoleInterface var1) {
      this(var1, (NameSpace)null);
   }

   public Interpreter() {
      this(new StringReader(""), System.out, System.err, false, (NameSpace)null);
      this.evalOnly = true;
      this.setu("bsh.evalOnly", new Primitive(true));
   }

   public void setConsole(ConsoleInterface var1) {
      this.console = var1;
      this.setu("bsh.console", var1);
      this.setOut(var1.getOut());
      this.setErr(var1.getErr());
   }

   private void initRootSystemObject() {
      BshClassManager var1 = this.getClassManager();
      this.setu("bsh", (new NameSpace(var1, "Bsh Object")).getThis(this));
      if (sharedObject == null) {
         sharedObject = (new NameSpace(var1, "Bsh Shared System Object")).getThis(this);
      }

      this.setu("bsh.system", sharedObject);
      this.setu("bsh.shared", sharedObject);
      This var2 = (new NameSpace(var1, "Bsh Command Help Text")).getThis(this);
      this.setu("bsh.help", var2);

      try {
         this.setu("bsh.cwd", System.getProperty("user.dir"));
      } catch (SecurityException var4) {
         this.setu("bsh.cwd", ".");
      }

      this.setu("bsh.interactive", new Primitive(this.interactive));
      this.setu("bsh.evalOnly", new Primitive(this.evalOnly));
   }

   public void setNameSpace(NameSpace var1) {
      this.globalNameSpace = var1;
   }

   public NameSpace getNameSpace() {
      return this.globalNameSpace;
   }

   public static void main(String[] var0) {
      Interpreter var3;
      if (var0.length > 0) {
         String var1 = var0[0];
         String[] var2;
         if (var0.length > 1) {
            var2 = new String[var0.length - 1];
            System.arraycopy(var0, 1, var2, 0, var0.length - 1);
         } else {
            var2 = new String[0];
         }

         var3 = new Interpreter();
         var3.setu("bsh.args", var2);

         try {
            Object var4 = var3.source(var1, var3.globalNameSpace);
            if (var4 instanceof Class) {
               try {
                  invokeMain((Class)var4, var2);
               } catch (Exception var8) {
                  Object var6 = var8;
                  if (var8 instanceof InvocationTargetException) {
                     var6 = ((InvocationTargetException)var8).getTargetException();
                  }

                  System.err.println("Class: " + var4 + " main method threw exception:" + var6);
               }
            }
         } catch (FileNotFoundException var9) {
            System.out.println("File not found: " + var9);
         } catch (TargetError var10) {
            System.out.println("Script threw exception: " + var10);
            if (var10.inNativeCode()) {
               var10.printStackTrace(DEBUG, System.err);
            }
         } catch (EvalError var11) {
            System.out.println("Evaluation Error: " + var11);
         } catch (IOException var12) {
            System.out.println("I/O Error: " + var12);
         }
      } else {
         Object var13;
         if (System.getProperty("os.name").startsWith("Windows") && System.getProperty("java.version").startsWith("1.1.")) {
            var13 = new FilterInputStream(System.in) {
               public int available() throws IOException {
                  return 0;
               }
            };
         } else {
            var13 = System.in;
         }

         CommandLineReader var14 = new CommandLineReader(new InputStreamReader((InputStream)var13));
         var3 = new Interpreter(var14, System.out, System.err, true);
         var3.run();
      }

   }

   public static void invokeMain(Class var0, String[] var1) throws Exception {
      Method var2 = Reflect.resolveJavaMethod((BshClassManager)null, var0, "main", new Class[]{array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String}, true);
      if (var2 != null) {
         var2.invoke((Object)null, var1);
      }

   }

   public void run() {
      if (this.evalOnly) {
         throw new RuntimeException("bsh Interpreter: No stream");
      } else {
         if (this.interactive) {
            try {
               this.eval("printBanner();");
            } catch (EvalError var18) {
               this.println("BeanShell 2.0b4 - by Pat Niemeyer (pat@pat.net)");
            }
         }

         CallStack var1 = new CallStack(this.globalNameSpace);
         boolean var2 = false;

         while(!var2) {
            try {
               System.out.flush();
               System.err.flush();
               Thread.yield();
               if (this.interactive) {
                  this.print(this.getBshPrompt());
               }

               var2 = this.Line();
               if (this.get_jjtree().nodeArity() > 0) {
                  SimpleNode var3 = (SimpleNode)this.get_jjtree().rootNode();
                  if (DEBUG) {
                     var3.dump(">");
                  }

                  Object var4 = var3.eval(var1, this);
                  if (var1.depth() > 1) {
                     throw new InterpreterError("Callstack growing: " + var1);
                  }

                  if (var4 instanceof ReturnControl) {
                     var4 = ((ReturnControl)var4).value;
                  }

                  if (var4 != Primitive.VOID) {
                     this.setu("$_", var4);
                     if (this.showResults) {
                        this.println("<" + var4 + ">");
                     }
                  }
               }
            } catch (ParseException var19) {
               this.error("Parser Error: " + var19.getMessage(DEBUG));
               if (DEBUG) {
                  var19.printStackTrace();
               }

               if (!this.interactive) {
                  var2 = true;
               }

               this.parser.reInitInput(this.in);
            } catch (InterpreterError var20) {
               this.error("Internal Error: " + var20.getMessage());
               var20.printStackTrace();
               if (!this.interactive) {
                  var2 = true;
               }
            } catch (TargetError var21) {
               this.error("// Uncaught Exception: " + var21);
               if (var21.inNativeCode()) {
                  var21.printStackTrace(DEBUG, this.err);
               }

               if (!this.interactive) {
                  var2 = true;
               }

               this.setu("$_e", var21.getTarget());
            } catch (EvalError var22) {
               if (this.interactive) {
                  this.error("EvalError: " + var22.toString());
               } else {
                  this.error("EvalError: " + var22.getMessage());
               }

               if (DEBUG) {
                  var22.printStackTrace();
               }

               if (!this.interactive) {
                  var2 = true;
               }
            } catch (Exception var23) {
               this.error("Unknown error: " + var23);
               if (DEBUG) {
                  var23.printStackTrace();
               }

               if (!this.interactive) {
                  var2 = true;
               }
            } catch (TokenMgrError var24) {
               this.error("Error parsing input: " + var24);
               this.parser.reInitTokenInput(this.in);
               if (!this.interactive) {
                  var2 = true;
               }
            } finally {
               this.get_jjtree().reset();
               if (var1.depth() > 1) {
                  var1.clear();
                  var1.push(this.globalNameSpace);
               }

            }
         }

         if (this.interactive && this.exitOnEOF) {
            System.exit(0);
         }

      }
   }

   public Object source(String var1, NameSpace var2) throws FileNotFoundException, IOException, EvalError {
      File var3 = this.pathToFile(var1);
      if (DEBUG) {
         debug("Sourcing file: " + var3);
      }

      BufferedReader var4 = new BufferedReader(new FileReader(var3));

      Object var5;
      try {
         var5 = this.eval(var4, var2, var1);
      } finally {
         var4.close();
      }

      return var5;
   }

   public Object source(String var1) throws FileNotFoundException, IOException, EvalError {
      return this.source(var1, this.globalNameSpace);
   }

   public Object eval(Reader var1, NameSpace var2, String var3) throws EvalError {
      Object var4 = null;
      if (DEBUG) {
         debug("eval: nameSpace = " + var2);
      }

      Interpreter var5 = new Interpreter(var1, this.out, this.err, false, var2, this, var3);
      CallStack var6 = new CallStack(var2);
      boolean var7 = false;

      while(!var7) {
         SimpleNode var8 = null;

         try {
            var7 = var5.Line();
            if (var5.get_jjtree().nodeArity() > 0) {
               var8 = (SimpleNode)var5.get_jjtree().rootNode();
               var8.setSourceFile(var3);
               if (TRACE) {
                  this.println("// " + var8.getText());
               }

               var4 = var8.eval(var6, var5);
               if (var6.depth() > 1) {
                  throw new InterpreterError("Callstack growing: " + var6);
               }

               if (var4 instanceof ReturnControl) {
                  var4 = ((ReturnControl)var4).value;
                  break;
               }

               if (var5.showResults && var4 != Primitive.VOID) {
                  this.println("<" + var4 + ">");
               }
            }
         } catch (ParseException var23) {
            if (DEBUG) {
               this.error(var23.getMessage(DEBUG));
            }

            var23.setErrorSourceFile(var3);
            throw var23;
         } catch (InterpreterError var24) {
            var24.printStackTrace();
            throw new EvalError("Sourced file: " + var3 + " internal Error: " + var24.getMessage(), var8, var6);
         } catch (TargetError var25) {
            if (var25.getNode() == null) {
               var25.setNode(var8);
            }

            var25.reThrow("Sourced file: " + var3);
         } catch (EvalError var26) {
            if (DEBUG) {
               var26.printStackTrace();
            }

            if (var26.getNode() == null) {
               var26.setNode(var8);
            }

            var26.reThrow("Sourced file: " + var3);
         } catch (Exception var27) {
            if (DEBUG) {
               var27.printStackTrace();
            }

            throw new EvalError("Sourced file: " + var3 + " unknown error: " + var27.getMessage(), var8, var6);
         } catch (TokenMgrError var28) {
            throw new EvalError("Sourced file: " + var3 + " Token Parsing Error: " + var28.getMessage(), var8, var6);
         } finally {
            var5.get_jjtree().reset();
            if (var6.depth() > 1) {
               var6.clear();
               var6.push(var2);
            }

         }
      }

      return Primitive.unwrap(var4);
   }

   public Object eval(Reader var1) throws EvalError {
      return this.eval(var1, this.globalNameSpace, "eval stream");
   }

   public Object eval(String var1) throws EvalError {
      if (DEBUG) {
         debug("eval(String): " + var1);
      }

      return this.eval(var1, this.globalNameSpace);
   }

   public Object eval(String var1, NameSpace var2) throws EvalError {
      String var3 = var1.endsWith(";") ? var1 : var1 + ";";
      return this.eval(new StringReader(var3), var2, "inline evaluation of: ``" + this.showEvalString(var3) + "''");
   }

   private String showEvalString(String var1) {
      var1 = var1.replace('\n', ' ');
      var1 = var1.replace('\r', ' ');
      if (var1.length() > 80) {
         var1 = var1.substring(0, 80) + " . . . ";
      }

      return var1;
   }

   public final void error(Object var1) {
      if (this.console != null) {
         this.console.error("// Error: " + var1 + "\n");
      } else {
         this.err.println("// Error: " + var1);
         this.err.flush();
      }

   }

   public Reader getIn() {
      return this.in;
   }

   public PrintStream getOut() {
      return this.out;
   }

   public PrintStream getErr() {
      return this.err;
   }

   public final void println(Object var1) {
      this.print(var1 + systemLineSeparator);
   }

   public final void print(Object var1) {
      if (this.console != null) {
         this.console.print(var1);
      } else {
         this.out.print(var1);
         this.out.flush();
      }

   }

   public static final void debug(String var0) {
      if (DEBUG) {
         debug.println("// Debug: " + var0);
      }

   }

   public Object get(String var1) throws EvalError {
      try {
         Object var2 = this.globalNameSpace.get(var1, this);
         return Primitive.unwrap(var2);
      } catch (UtilEvalError var3) {
         throw var3.toEvalError(SimpleNode.JAVACODE, new CallStack());
      }
   }

   Object getu(String var1) {
      try {
         return this.get(var1);
      } catch (EvalError var3) {
         throw new InterpreterError("set: " + var3);
      }
   }

   public void set(String var1, Object var2) throws EvalError {
      if (var2 == null) {
         var2 = Primitive.NULL;
      }

      CallStack var3 = new CallStack();

      try {
         if (Name.isCompound(var1)) {
            LHS var4 = this.globalNameSpace.getNameResolver(var1).toLHS(var3, this);
            var4.assign(var2, false);
         } else {
            this.globalNameSpace.setVariable(var1, var2, false);
         }

      } catch (UtilEvalError var5) {
         throw var5.toEvalError(SimpleNode.JAVACODE, var3);
      }
   }

   void setu(String var1, Object var2) {
      try {
         this.set(var1, var2);
      } catch (EvalError var4) {
         throw new InterpreterError("set: " + var4);
      }
   }

   public void set(String var1, long var2) throws EvalError {
      this.set(var1, new Primitive(var2));
   }

   public void set(String var1, int var2) throws EvalError {
      this.set(var1, new Primitive(var2));
   }

   public void set(String var1, double var2) throws EvalError {
      this.set(var1, new Primitive(var2));
   }

   public void set(String var1, float var2) throws EvalError {
      this.set(var1, new Primitive(var2));
   }

   public void set(String var1, boolean var2) throws EvalError {
      this.set(var1, new Primitive(var2));
   }

   public void unset(String var1) throws EvalError {
      CallStack var2 = new CallStack();

      try {
         LHS var3 = this.globalNameSpace.getNameResolver(var1).toLHS(var2, this);
         if (var3.type != 0) {
            throw new EvalError("Can't unset, not a variable: " + var1, SimpleNode.JAVACODE, new CallStack());
         } else {
            var3.nameSpace.unsetVariable(var1);
         }
      } catch (UtilEvalError var4) {
         throw new EvalError(var4.getMessage(), SimpleNode.JAVACODE, new CallStack());
      }
   }

   public Object getInterface(Class var1) throws EvalError {
      try {
         return this.globalNameSpace.getThis(this).getInterface(var1);
      } catch (UtilEvalError var3) {
         throw var3.toEvalError(SimpleNode.JAVACODE, new CallStack());
      }
   }

   private JJTParserState get_jjtree() {
      return this.parser.jjtree;
   }

   private JavaCharStream get_jj_input_stream() {
      return this.parser.jj_input_stream;
   }

   private boolean Line() throws ParseException {
      return this.parser.Line();
   }

   void loadRCFiles() {
      try {
         String var1 = System.getProperty("user.home") + File.separator + ".bshrc";
         this.source(var1, this.globalNameSpace);
      } catch (Exception var2) {
         if (DEBUG) {
            debug("Could not find rc file: " + var2);
         }
      }

   }

   public File pathToFile(String var1) throws IOException {
      File var2 = new File(var1);
      if (!var2.isAbsolute()) {
         String var3 = (String)this.getu("bsh.cwd");
         var2 = new File(var3 + File.separator + var1);
      }

      return new File(var2.getCanonicalPath());
   }

   public static void redirectOutputToFile(String var0) {
      try {
         PrintStream var1 = new PrintStream(new FileOutputStream(var0));
         System.setOut(var1);
         System.setErr(var1);
      } catch (IOException var2) {
         System.err.println("Can't redirect output to file: " + var0);
      }

   }

   public void setClassLoader(ClassLoader var1) {
      this.getClassManager().setClassLoader(var1);
   }

   public BshClassManager getClassManager() {
      return this.getNameSpace().getClassManager();
   }

   public void setStrictJava(boolean var1) {
      this.strictJava = var1;
   }

   public boolean getStrictJava() {
      return this.strictJava;
   }

   static void staticInit() {
      try {
         systemLineSeparator = System.getProperty("line.separator");
         debug = System.err;
         DEBUG = Boolean.getBoolean("debug");
         TRACE = Boolean.getBoolean("trace");
         LOCALSCOPING = Boolean.getBoolean("localscoping");
         String var0 = System.getProperty("outfile");
         if (var0 != null) {
            redirectOutputToFile(var0);
         }
      } catch (SecurityException var3) {
         System.err.println("Could not init static:" + var3);
      } catch (Exception var4) {
         System.err.println("Could not init static(2):" + var4);
      } catch (Throwable var5) {
         System.err.println("Could not init static(3):" + var5);
      }

   }

   public String getSourceFileInfo() {
      return this.sourceFileInfo != null ? this.sourceFileInfo : "<unknown source>";
   }

   public Interpreter getParent() {
      return this.parent;
   }

   public void setOut(PrintStream var1) {
      this.out = var1;
   }

   public void setErr(PrintStream var1) {
      this.err = var1;
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.console != null) {
         this.setOut(this.console.getOut());
         this.setErr(this.console.getErr());
      } else {
         this.setOut(System.out);
         this.setErr(System.err);
      }

   }

   private String getBshPrompt() {
      try {
         return (String)this.eval("getBshPrompt()");
      } catch (Exception var2) {
         return "bsh % ";
      }
   }

   public void setExitOnEOF(boolean var1) {
      this.exitOnEOF = var1;
   }

   public void setShowResults(boolean var1) {
      this.showResults = var1;
   }

   public boolean getShowResults() {
      return this.showResults;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      staticInit();
   }
}
