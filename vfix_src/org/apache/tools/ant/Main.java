package org.apache.tools.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.launch.AntMain;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ProxySetup;

public class Main implements AntMain {
   public static final String DEFAULT_BUILD_FILENAME = "build.xml";
   private int msgOutputLevel = 2;
   private File buildFile;
   private static PrintStream out;
   private static PrintStream err;
   private Vector targets = new Vector();
   private Properties definedProps = new Properties();
   private Vector listeners = new Vector(1);
   private Vector propertyFiles = new Vector(1);
   private boolean allowInput = true;
   private boolean keepGoingMode = false;
   private String loggerClassname = null;
   private String inputHandlerClassname = null;
   private boolean emacsMode = false;
   private boolean readyToRun = false;
   private boolean projectHelp = false;
   private static boolean isLogFileUsed;
   private Integer threadPriority = null;
   private boolean proxy = false;
   private static String antVersion;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Main;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$BuildListener;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$input$InputHandler;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$BuildLogger;

   private static void printMessage(Throwable t) {
      String message = t.getMessage();
      if (message != null) {
         System.err.println(message);
      }

   }

   public static void start(String[] args, Properties additionalUserProperties, ClassLoader coreLoader) {
      Main m = new Main();
      m.startAnt(args, additionalUserProperties, coreLoader);
   }

   public void startAnt(String[] args, Properties additionalUserProperties, ClassLoader coreLoader) {
      try {
         Diagnostics.validateVersion();
         this.processArgs(args);
      } catch (Throwable var13) {
         handleLogfile();
         printMessage(var13);
         this.exit(1);
         return;
      }

      if (additionalUserProperties != null) {
         Enumeration e = additionalUserProperties.keys();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String property = additionalUserProperties.getProperty(key);
            this.definedProps.put(key, property);
         }
      }

      int exitCode = 1;

      try {
         try {
            this.runBuild(coreLoader);
            exitCode = 0;
         } catch (ExitStatusException var14) {
            exitCode = var14.getStatus();
            if (exitCode != 0) {
               throw var14;
            }
         }
      } catch (BuildException var15) {
         if (err != System.err) {
            printMessage(var15);
         }
      } catch (Throwable var16) {
         var16.printStackTrace();
         printMessage(var16);
      } finally {
         handleLogfile();
      }

      this.exit(exitCode);
   }

   protected void exit(int exitCode) {
      System.exit(exitCode);
   }

   private static void handleLogfile() {
      if (isLogFileUsed) {
         FileUtils.close((OutputStream)out);
         FileUtils.close((OutputStream)err);
      }

   }

   public static void main(String[] args) {
      start(args, (Properties)null, (ClassLoader)null);
   }

   public Main() {
   }

   /** @deprecated */
   protected Main(String[] args) throws BuildException {
      this.processArgs(args);
   }

   private void processArgs(String[] args) {
      String searchForThis = null;
      PrintStream logTo = null;
      HashMap launchCommands = new HashMap();
      launchCommands.put("-lib", "");
      launchCommands.put("-cp", "");
      launchCommands.put("-noclasspath", "");
      launchCommands.put("--noclasspath", "");
      launchCommands.put("-nouserlib", "");
      launchCommands.put("--nouserlib", "");
      launchCommands.put("-main", "");

      int i;
      String arg;
      for(i = 0; i < args.length; ++i) {
         arg = args[i];
         if (arg.equals("-help") || arg.equals("-h")) {
            printUsage();
            return;
         }

         if (arg.equals("-version")) {
            printVersion();
            return;
         }

         if (arg.equals("-diagnostics")) {
            Diagnostics.doReport(System.out);
            return;
         }

         if (!arg.equals("-quiet") && !arg.equals("-q")) {
            if (!arg.equals("-verbose") && !arg.equals("-v")) {
               if (!arg.equals("-debug") && !arg.equals("-d")) {
                  if (arg.equals("-noinput")) {
                     this.allowInput = false;
                  } else {
                     String msg;
                     if (!arg.equals("-logfile") && !arg.equals("-l")) {
                        if (!arg.equals("-buildfile") && !arg.equals("-file") && !arg.equals("-f")) {
                           if (arg.equals("-listener")) {
                              try {
                                 this.listeners.addElement(args[i + 1]);
                                 ++i;
                              } catch (ArrayIndexOutOfBoundsException var29) {
                                 msg = "You must specify a classname when using the -listener argument";
                                 throw new BuildException(msg);
                              }
                           } else {
                              String msg;
                              if (arg.startsWith("-D")) {
                                 msg = arg.substring(2, arg.length());
                                 msg = null;
                                 int posEq = msg.indexOf("=");
                                 if (posEq > 0) {
                                    msg = msg.substring(posEq + 1);
                                    msg = msg.substring(0, posEq);
                                 } else {
                                    if (i >= args.length - 1) {
                                       throw new BuildException("Missing value for property " + msg);
                                    }

                                    ++i;
                                    msg = args[i];
                                 }

                                 this.definedProps.put(msg, msg);
                              } else if (arg.equals("-logger")) {
                                 if (this.loggerClassname != null) {
                                    throw new BuildException("Only one logger class may  be specified.");
                                 }

                                 try {
                                    ++i;
                                    this.loggerClassname = args[i];
                                 } catch (ArrayIndexOutOfBoundsException var28) {
                                    throw new BuildException("You must specify a classname when using the -logger argument");
                                 }
                              } else if (arg.equals("-inputhandler")) {
                                 if (this.inputHandlerClassname != null) {
                                    throw new BuildException("Only one input handler class may be specified.");
                                 }

                                 try {
                                    ++i;
                                    this.inputHandlerClassname = args[i];
                                 } catch (ArrayIndexOutOfBoundsException var27) {
                                    throw new BuildException("You must specify a classname when using the -inputhandler argument");
                                 }
                              } else if (!arg.equals("-emacs") && !arg.equals("-e")) {
                                 if (!arg.equals("-projecthelp") && !arg.equals("-p")) {
                                    if (!arg.equals("-find") && !arg.equals("-s")) {
                                       if (arg.startsWith("-propertyfile")) {
                                          try {
                                             this.propertyFiles.addElement(args[i + 1]);
                                             ++i;
                                          } catch (ArrayIndexOutOfBoundsException var26) {
                                             msg = "You must specify a property filename when using the -propertyfile argument";
                                             throw new BuildException(msg);
                                          }
                                       } else if (!arg.equals("-k") && !arg.equals("-keep-going")) {
                                          if (arg.equals("-nice")) {
                                             try {
                                                this.threadPriority = Integer.decode(args[i + 1]);
                                             } catch (ArrayIndexOutOfBoundsException var24) {
                                                throw new BuildException("You must supply a niceness value (1-10) after the -nice option");
                                             } catch (NumberFormatException var25) {
                                                throw new BuildException("Unrecognized niceness value: " + args[i + 1]);
                                             }

                                             ++i;
                                             if (this.threadPriority < 1 || this.threadPriority > 10) {
                                                throw new BuildException("Niceness value is out of the range 1-10");
                                             }
                                          } else {
                                             if (launchCommands.get(arg) != null) {
                                                msg = "Ant's Main method is being handed an option " + arg + " that is only for the launcher class." + "\nThis can be caused by a version mismatch between " + "the ant script/.bat file and Ant itself.";
                                                throw new BuildException(msg);
                                             }

                                             if (arg.equals("-autoproxy")) {
                                                this.proxy = false;
                                             } else {
                                                if (arg.startsWith("-")) {
                                                   msg = "Unknown argument: " + arg;
                                                   System.err.println(msg);
                                                   printUsage();
                                                   throw new BuildException("");
                                                }

                                                this.targets.addElement(arg);
                                             }
                                          }
                                       } else {
                                          this.keepGoingMode = true;
                                       }
                                    } else if (i < args.length - 1) {
                                       ++i;
                                       searchForThis = args[i];
                                    } else {
                                       searchForThis = "build.xml";
                                    }
                                 } else {
                                    this.projectHelp = true;
                                 }
                              } else {
                                 this.emacsMode = true;
                              }
                           }
                        } else {
                           try {
                              this.buildFile = new File(args[i + 1].replace('/', File.separatorChar));
                              ++i;
                           } catch (ArrayIndexOutOfBoundsException var30) {
                              msg = "You must specify a buildfile when using the -buildfile argument";
                              throw new BuildException(msg);
                           }
                        }
                     } else {
                        try {
                           File logFile = new File(args[i + 1]);
                           ++i;
                           logTo = new PrintStream(new FileOutputStream(logFile));
                           isLogFileUsed = true;
                        } catch (IOException var31) {
                           msg = "Cannot write on the specified log file. Make sure the path exists and you have write permissions.";
                           throw new BuildException(msg);
                        } catch (ArrayIndexOutOfBoundsException var32) {
                           msg = "You must specify a log file when using the -log argument";
                           throw new BuildException(msg);
                        }
                     }
                  }
               } else {
                  printVersion();
                  this.msgOutputLevel = 4;
               }
            } else {
               printVersion();
               this.msgOutputLevel = 3;
            }
         } else {
            this.msgOutputLevel = 1;
         }
      }

      if (this.buildFile == null) {
         if (searchForThis != null) {
            this.buildFile = this.findBuildFile(System.getProperty("user.dir"), searchForThis);
         } else {
            this.buildFile = new File("build.xml");
         }
      }

      if (!this.buildFile.exists()) {
         System.out.println("Buildfile: " + this.buildFile + " does not exist!");
         throw new BuildException("Build failed");
      } else if (this.buildFile.isDirectory()) {
         System.out.println("What? Buildfile: " + this.buildFile + " is a dir!");
         throw new BuildException("Build failed");
      } else {
         for(i = 0; i < this.propertyFiles.size(); ++i) {
            arg = (String)this.propertyFiles.elementAt(i);
            Properties props = new Properties();
            FileInputStream fis = null;

            try {
               fis = new FileInputStream(arg);
               props.load(fis);
            } catch (IOException var22) {
               System.out.println("Could not load property file " + arg + ": " + var22.getMessage());
            } finally {
               FileUtils.close((InputStream)fis);
            }

            Enumeration propertyNames = props.propertyNames();

            while(propertyNames.hasMoreElements()) {
               String name = (String)propertyNames.nextElement();
               if (this.definedProps.getProperty(name) == null) {
                  this.definedProps.put(name, props.getProperty(name));
               }
            }
         }

         if (this.msgOutputLevel >= 2) {
            System.out.println("Buildfile: " + this.buildFile);
         }

         if (logTo != null) {
            out = logTo;
            err = logTo;
            System.setOut(out);
            System.setErr(err);
         }

         this.readyToRun = true;
      }
   }

   /** @deprecated */
   private File getParentFile(File file) {
      File parent = file.getParentFile();
      if (parent != null && this.msgOutputLevel >= 3) {
         System.out.println("Searching in " + parent.getAbsolutePath());
      }

      return parent;
   }

   private File findBuildFile(String start, String suffix) throws BuildException {
      if (this.msgOutputLevel >= 2) {
         System.out.println("Searching for " + suffix + " ...");
      }

      File parent = new File((new File(start)).getAbsolutePath());

      File file;
      for(file = new File(parent, suffix); !file.exists(); file = new File(parent, suffix)) {
         parent = this.getParentFile(parent);
         if (parent == null) {
            throw new BuildException("Could not locate a build file!");
         }
      }

      return file;
   }

   private void runBuild(ClassLoader coreLoader) throws BuildException {
      if (this.readyToRun) {
         Project project = new Project();
         project.setCoreLoader(coreLoader);
         Object error = null;

         try {
            this.addBuildListeners(project);
            this.addInputHandler(project);
            PrintStream savedErr = System.err;
            PrintStream savedOut = System.out;
            InputStream savedIn = System.in;
            SecurityManager oldsm = null;
            oldsm = System.getSecurityManager();

            try {
               if (this.allowInput) {
                  project.setDefaultInputStream(System.in);
               }

               System.setIn(new DemuxInputStream(project));
               System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
               System.setErr(new PrintStream(new DemuxOutputStream(project, true)));
               if (!this.projectHelp) {
                  project.fireBuildStarted();
               }

               if (this.threadPriority != null) {
                  try {
                     project.log("Setting Ant's thread priority to " + this.threadPriority, 3);
                     Thread.currentThread().setPriority(this.threadPriority);
                  } catch (SecurityException var23) {
                     project.log("A security manager refused to set the -nice value");
                  }
               }

               project.init();
               Enumeration e = this.definedProps.keys();

               while(e.hasMoreElements()) {
                  String arg = (String)e.nextElement();
                  String value = (String)this.definedProps.get(arg);
                  project.setUserProperty(arg, value);
               }

               project.setUserProperty("ant.file", this.buildFile.getAbsolutePath());
               project.setKeepGoingMode(this.keepGoingMode);
               if (this.proxy) {
                  ProxySetup proxySetup = new ProxySetup(project);
                  proxySetup.enableProxies();
               }

               ProjectHelper.configureProject(project, this.buildFile);
               if (this.projectHelp) {
                  printDescription(project);
                  printTargets(project, this.msgOutputLevel > 2);
               } else {
                  if (this.targets.size() == 0 && project.getDefaultTarget() != null) {
                     this.targets.addElement(project.getDefaultTarget());
                  }

                  project.executeTargets(this.targets);
               }
            } finally {
               if (oldsm != null) {
                  System.setSecurityManager(oldsm);
               }

               System.setOut(savedOut);
               System.setErr(savedErr);
               System.setIn(savedIn);
            }
         } catch (RuntimeException var25) {
            error = var25;
            throw var25;
         } catch (Error var26) {
            error = var26;
            throw var26;
         } finally {
            if (!this.projectHelp) {
               project.fireBuildFinished((Throwable)error);
            } else if (error != null) {
               project.log(((Throwable)error).toString(), 0);
            }

         }
      }
   }

   protected void addBuildListeners(Project project) {
      project.addBuildListener(this.createLogger());

      for(int i = 0; i < this.listeners.size(); ++i) {
         String className = (String)this.listeners.elementAt(i);
         BuildListener listener = (BuildListener)ClasspathUtils.newInstance(className, (class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main).getClassLoader(), class$org$apache$tools$ant$BuildListener == null ? (class$org$apache$tools$ant$BuildListener = class$("org.apache.tools.ant.BuildListener")) : class$org$apache$tools$ant$BuildListener);
         if (project != null) {
            project.setProjectReference(listener);
         }

         project.addBuildListener(listener);
      }

   }

   private void addInputHandler(Project project) throws BuildException {
      InputHandler handler = null;
      if (this.inputHandlerClassname == null) {
         handler = new DefaultInputHandler();
      } else {
         handler = (InputHandler)ClasspathUtils.newInstance(this.inputHandlerClassname, (class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main).getClassLoader(), class$org$apache$tools$ant$input$InputHandler == null ? (class$org$apache$tools$ant$input$InputHandler = class$("org.apache.tools.ant.input.InputHandler")) : class$org$apache$tools$ant$input$InputHandler);
         if (project != null) {
            project.setProjectReference(handler);
         }
      }

      project.setInputHandler((InputHandler)handler);
   }

   private BuildLogger createLogger() {
      BuildLogger logger = null;
      if (this.loggerClassname != null) {
         try {
            logger = (BuildLogger)ClasspathUtils.newInstance(this.loggerClassname, (class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main).getClassLoader(), class$org$apache$tools$ant$BuildLogger == null ? (class$org$apache$tools$ant$BuildLogger = class$("org.apache.tools.ant.BuildLogger")) : class$org$apache$tools$ant$BuildLogger);
         } catch (BuildException var3) {
            System.err.println("The specified logger class " + this.loggerClassname + " could not be used because " + var3.getMessage());
            throw new RuntimeException();
         }
      } else {
         logger = new DefaultLogger();
      }

      ((BuildLogger)logger).setMessageOutputLevel(this.msgOutputLevel);
      ((BuildLogger)logger).setOutputPrintStream(out);
      ((BuildLogger)logger).setErrorPrintStream(err);
      ((BuildLogger)logger).setEmacsMode(this.emacsMode);
      return (BuildLogger)logger;
   }

   private static void printUsage() {
      String lSep = System.getProperty("line.separator");
      StringBuffer msg = new StringBuffer();
      msg.append("ant [options] [target [target2 [target3] ...]]" + lSep);
      msg.append("Options: " + lSep);
      msg.append("  -help, -h              print this message" + lSep);
      msg.append("  -projecthelp, -p       print project help information" + lSep);
      msg.append("  -version               print the version information and exit" + lSep);
      msg.append("  -diagnostics           print information that might be helpful to" + lSep);
      msg.append("                         diagnose or report problems." + lSep);
      msg.append("  -quiet, -q             be extra quiet" + lSep);
      msg.append("  -verbose, -v           be extra verbose" + lSep);
      msg.append("  -debug, -d             print debugging information" + lSep);
      msg.append("  -emacs, -e             produce logging information without adornments" + lSep);
      msg.append("  -lib <path>            specifies a path to search for jars and classes" + lSep);
      msg.append("  -logfile <file>        use given file for log" + lSep);
      msg.append("    -l     <file>                ''" + lSep);
      msg.append("  -logger <classname>    the class which is to perform logging" + lSep);
      msg.append("  -listener <classname>  add an instance of class as a project listener" + lSep);
      msg.append("  -noinput               do not allow interactive input" + lSep);
      msg.append("  -buildfile <file>      use given buildfile" + lSep);
      msg.append("    -file    <file>              ''" + lSep);
      msg.append("    -f       <file>              ''" + lSep);
      msg.append("  -D<property>=<value>   use value for given property" + lSep);
      msg.append("  -keep-going, -k        execute all targets that do not depend" + lSep);
      msg.append("                         on failed target(s)" + lSep);
      msg.append("  -propertyfile <name>   load all properties from file with -D" + lSep);
      msg.append("                         properties taking precedence" + lSep);
      msg.append("  -inputhandler <class>  the class which will handle input requests" + lSep);
      msg.append("  -find <file>           (s)earch for buildfile towards the root of" + lSep);
      msg.append("    -s  <file>           the filesystem and use it" + lSep);
      msg.append("  -nice  number          A niceness value for the main thread:" + lSep + "                         1 (lowest) to 10 (highest); 5 is the default" + lSep);
      msg.append("  -nouserlib             Run ant without using the jar files from" + lSep + "                         ${user.home}/.ant/lib" + lSep);
      msg.append("  -noclasspath           Run ant without using CLASSPATH" + lSep);
      msg.append("  -autoproxy             Java1.5+: use the OS proxy settings" + lSep);
      msg.append("  -main <class>          override Ant's normal entry point");
      System.out.println(msg.toString());
   }

   private static void printVersion() throws BuildException {
      System.out.println(getAntVersion());
   }

   public static synchronized String getAntVersion() throws BuildException {
      if (antVersion == null) {
         try {
            Properties props = new Properties();
            InputStream in = (class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main).getResourceAsStream("/org/apache/tools/ant/version.txt");
            props.load(in);
            in.close();
            StringBuffer msg = new StringBuffer();
            msg.append("Apache Ant version ");
            msg.append(props.getProperty("VERSION"));
            msg.append(" compiled on ");
            msg.append(props.getProperty("DATE"));
            antVersion = msg.toString();
         } catch (IOException var3) {
            throw new BuildException("Could not load the version information:" + var3.getMessage());
         } catch (NullPointerException var4) {
            throw new BuildException("Could not load the version information.");
         }
      }

      return antVersion;
   }

   private static void printDescription(Project project) {
      if (project.getDescription() != null) {
         project.log(project.getDescription());
      }

   }

   private static void printTargets(Project project, boolean printSubTargets) {
      int maxLength = 0;
      Enumeration ptargets = project.getTargets().elements();
      Vector topNames = new Vector();
      Vector topDescriptions = new Vector();
      Vector subNames = new Vector();

      while(ptargets.hasMoreElements()) {
         Target currentTarget = (Target)ptargets.nextElement();
         String targetName = currentTarget.getName();
         if (!targetName.equals("")) {
            String targetDescription = currentTarget.getDescription();
            int pos;
            if (targetDescription == null) {
               pos = findTargetPosition(subNames, targetName);
               subNames.insertElementAt(targetName, pos);
            } else {
               pos = findTargetPosition(topNames, targetName);
               topNames.insertElementAt(targetName, pos);
               topDescriptions.insertElementAt(targetDescription, pos);
               if (targetName.length() > maxLength) {
                  maxLength = targetName.length();
               }
            }
         }
      }

      printTargets(project, topNames, topDescriptions, "Main targets:", maxLength);
      if (topNames.size() == 0) {
         printSubTargets = true;
      }

      if (printSubTargets) {
         printTargets(project, subNames, (Vector)null, "Other targets:", 0);
      }

      String defaultTarget = project.getDefaultTarget();
      if (defaultTarget != null && !"".equals(defaultTarget)) {
         project.log("Default target: " + defaultTarget);
      }

   }

   private static int findTargetPosition(Vector names, String name) {
      int res = names.size();

      for(int i = 0; i < names.size() && res == names.size(); ++i) {
         if (name.compareTo((String)names.elementAt(i)) < 0) {
            res = i;
         }
      }

      return res;
   }

   private static void printTargets(Project project, Vector names, Vector descriptions, String heading, int maxlen) {
      String lSep = System.getProperty("line.separator");

      String spaces;
      for(spaces = "    "; spaces.length() <= maxlen; spaces = spaces + spaces) {
      }

      StringBuffer msg = new StringBuffer();
      msg.append(heading + lSep + lSep);

      for(int i = 0; i < names.size(); ++i) {
         msg.append(" ");
         msg.append(names.elementAt(i));
         if (descriptions != null) {
            msg.append(spaces.substring(0, maxlen - ((String)names.elementAt(i)).length() + 2));
            msg.append(descriptions.elementAt(i));
         }

         msg.append(lSep);
      }

      project.log(msg.toString(), 1);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      out = System.out;
      err = System.err;
      isLogFileUsed = false;
      antVersion = null;
   }
}
