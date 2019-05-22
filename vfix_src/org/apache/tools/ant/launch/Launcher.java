package org.apache.tools.ant.launch;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class Launcher {
   public static final String ANTHOME_PROPERTY = "ant.home";
   public static final String ANTLIBDIR_PROPERTY = "ant.library.dir";
   public static final String ANT_PRIVATEDIR = ".ant";
   public static final String ANT_PRIVATELIB = "lib";
   public static final String USER_LIBDIR;
   public static final String MAIN_CLASS = "org.apache.tools.ant.Main";
   public static final String USER_HOMEDIR = "user.home";
   private static final String JAVA_CLASS_PATH = "java.class.path";
   protected static final int EXIT_CODE_ERROR = 2;

   public static void main(String[] args) {
      int exitCode;
      try {
         Launcher launcher = new Launcher();
         exitCode = launcher.run(args);
      } catch (LaunchException var3) {
         exitCode = 2;
         System.err.println(var3.getMessage());
      } catch (Throwable var4) {
         exitCode = 2;
         var4.printStackTrace(System.err);
      }

      if (exitCode != 0) {
         System.exit(exitCode);
      }

   }

   private void addPath(String path, boolean getJars, List libPathURLs) throws MalformedURLException {
      StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator);

      while(true) {
         String elementName;
         File element;
         do {
            if (!tokenizer.hasMoreElements()) {
               return;
            }

            elementName = tokenizer.nextToken();
            element = new File(elementName);
         } while(elementName.indexOf("%") != -1 && !element.exists());

         if (getJars && element.isDirectory()) {
            URL[] dirURLs = Locator.getLocationURLs(element);

            for(int j = 0; j < dirURLs.length; ++j) {
               libPathURLs.add(dirURLs[j]);
            }
         }

         libPathURLs.add(Locator.fileToURL(element));
      }
   }

   private int run(String[] args) throws LaunchException, MalformedURLException {
      String antHomeProperty = System.getProperty("ant.home");
      File antHome = null;
      File sourceJar = Locator.getClassSource(this.getClass());
      File jarDir = sourceJar.getParentFile();
      String mainClassname = "org.apache.tools.ant.Main";
      if (antHomeProperty != null) {
         antHome = new File(antHomeProperty);
      }

      if (antHome == null || !antHome.exists()) {
         antHome = jarDir.getParentFile();
         System.setProperty("ant.home", antHome.getAbsolutePath());
      }

      if (!antHome.exists()) {
         throw new LaunchException("Ant home is set incorrectly or ant could not be located");
      } else {
         List libPaths = new ArrayList();
         String cpString = null;
         List argList = new ArrayList();
         boolean noUserLib = false;
         boolean noClassPath = false;

         for(int i = 0; i < args.length; ++i) {
            if (args[i].equals("-lib")) {
               if (i == args.length - 1) {
                  throw new LaunchException("The -lib argument must be followed by a library location");
               }

               ++i;
               libPaths.add(args[i]);
            } else if (args[i].equals("-cp")) {
               if (i == args.length - 1) {
                  throw new LaunchException("The -cp argument must be followed by a classpath expression");
               }

               if (cpString != null) {
                  throw new LaunchException("The -cp argument must not be repeated");
               }

               ++i;
               cpString = args[i];
            } else if (!args[i].equals("--nouserlib") && !args[i].equals("-nouserlib")) {
               if (!args[i].equals("--noclasspath") && !args[i].equals("-noclasspath")) {
                  if (args[i].equals("-main")) {
                     if (i == args.length - 1) {
                        throw new LaunchException("The -main argument must be followed by a library location");
                     }

                     ++i;
                     mainClassname = args[i];
                  } else {
                     argList.add(args[i]);
                  }
               } else {
                  noClassPath = true;
               }
            } else {
               noUserLib = true;
            }
         }

         String[] newArgs;
         if (argList.size() == args.length) {
            newArgs = args;
         } else {
            newArgs = (String[])((String[])argList.toArray(new String[argList.size()]));
         }

         URL[] libURLs = this.getLibPathURLs(noClassPath ? null : cpString, libPaths);
         URL[] systemURLs = this.getSystemURLs(jarDir);
         URL[] userURLs = noUserLib ? new URL[0] : this.getUserURLs();
         URL[] jars = this.getJarArray(libURLs, userURLs, systemURLs, Locator.getToolsJar());
         StringBuffer baseClassPath = new StringBuffer(System.getProperty("java.class.path"));
         if (baseClassPath.charAt(baseClassPath.length() - 1) == File.pathSeparatorChar) {
            baseClassPath.setLength(baseClassPath.length() - 1);
         }

         for(int i = 0; i < jars.length; ++i) {
            baseClassPath.append(File.pathSeparatorChar);
            baseClassPath.append(Locator.fromURI(jars[i].toString()));
         }

         System.setProperty("java.class.path", baseClassPath.toString());
         URLClassLoader loader = new URLClassLoader(jars);
         Thread.currentThread().setContextClassLoader(loader);
         Class mainClass = null;
         byte exitCode = 0;

         try {
            mainClass = loader.loadClass(mainClassname);
            AntMain main = (AntMain)mainClass.newInstance();
            main.startAnt(newArgs, (Properties)null, (ClassLoader)null);
         } catch (InstantiationException var23) {
            System.err.println("Incompatible version of " + mainClassname + " detected");
            File mainJar = Locator.getClassSource(mainClass);
            System.err.println("Location of this class " + mainJar);
            exitCode = 2;
         } catch (Throwable var24) {
            var24.printStackTrace(System.err);
            exitCode = 2;
         }

         return exitCode;
      }
   }

   private URL[] getLibPathURLs(String cpString, List libPaths) throws MalformedURLException {
      List libPathURLs = new ArrayList();
      if (cpString != null) {
         this.addPath(cpString, false, libPathURLs);
      }

      Iterator i = libPaths.iterator();

      while(i.hasNext()) {
         String libPath = (String)i.next();
         this.addPath(libPath, true, libPathURLs);
      }

      return (URL[])((URL[])libPathURLs.toArray(new URL[libPathURLs.size()]));
   }

   private URL[] getSystemURLs(File antLauncherDir) throws MalformedURLException {
      File antLibDir = null;
      String antLibDirProperty = System.getProperty("ant.library.dir");
      if (antLibDirProperty != null) {
         antLibDir = new File(antLibDirProperty);
      }

      if (antLibDir == null || !antLibDir.exists()) {
         antLibDir = antLauncherDir;
         System.setProperty("ant.library.dir", antLauncherDir.getAbsolutePath());
      }

      return Locator.getLocationURLs(antLibDir);
   }

   private URL[] getUserURLs() throws MalformedURLException {
      File userLibDir = new File(System.getProperty("user.home"), USER_LIBDIR);
      return Locator.getLocationURLs(userLibDir);
   }

   private URL[] getJarArray(URL[] libJars, URL[] userJars, URL[] systemJars, File toolsJar) throws MalformedURLException {
      int numJars = libJars.length + userJars.length + systemJars.length;
      if (toolsJar != null) {
         ++numJars;
      }

      URL[] jars = new URL[numJars];
      System.arraycopy(libJars, 0, jars, 0, libJars.length);
      System.arraycopy(userJars, 0, jars, libJars.length, userJars.length);
      System.arraycopy(systemJars, 0, jars, userJars.length + libJars.length, systemJars.length);
      if (toolsJar != null) {
         jars[jars.length - 1] = Locator.fileToURL(toolsJar);
      }

      return jars;
   }

   static {
      USER_LIBDIR = ".ant" + File.separatorChar + "lib";
   }
}
