package org.apache.tools.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TimeZone;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.LoaderUtils;
import org.xml.sax.XMLReader;

public final class Diagnostics {
   private static final int BIG_DRIFT_LIMIT = 10000;
   private static final int TEST_FILE_SIZE = 32;
   private static final int KILOBYTE = 1024;
   private static final int SECONDS_PER_MILLISECOND = 1000;
   private static final int SECONDS_PER_MINUTE = 60;
   private static final int MINUTES_PER_HOUR = 60;
   private static final String TEST_CLASS = "org.apache.tools.ant.taskdefs.optional.Test";
   protected static final String ERROR_PROPERTY_ACCESS_BLOCKED = "Access to this property blocked by a security manager";
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Main;
   // $FF: synthetic field
   static Class array$Ljava$lang$String;

   private Diagnostics() {
   }

   public static boolean isOptionalAvailable() {
      try {
         Class.forName("org.apache.tools.ant.taskdefs.optional.Test");
         return true;
      } catch (ClassNotFoundException var1) {
         return false;
      }
   }

   public static void validateVersion() throws BuildException {
      try {
         Class optional = Class.forName("org.apache.tools.ant.taskdefs.optional.Test");
         String coreVersion = getImplementationVersion(class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main);
         String optionalVersion = getImplementationVersion(optional);
         if (coreVersion != null && !coreVersion.equals(optionalVersion)) {
            throw new BuildException("Invalid implementation version between Ant core and Ant optional tasks.\n core    : " + coreVersion + "\n" + " optional: " + optionalVersion);
         }
      } catch (ClassNotFoundException var3) {
         ignoreThrowable(var3);
      }

   }

   public static File[] listLibraries() {
      String home = System.getProperty("ant.home");
      if (home == null) {
         return null;
      } else {
         File libDir = new File(home, "lib");
         return listJarFiles(libDir);
      }
   }

   private static File[] listJarFiles(File libDir) {
      FilenameFilter filter = new FilenameFilter() {
         public boolean accept(File dir, String name) {
            return name.endsWith(".jar");
         }
      };
      File[] files = libDir.listFiles(filter);
      return files;
   }

   public static void main(String[] args) {
      doReport(System.out);
   }

   private static String getImplementationVersion(Class clazz) {
      Package pkg = clazz.getPackage();
      return pkg.getImplementationVersion();
   }

   private static String getXmlParserName() {
      SAXParser saxParser = getSAXParser();
      if (saxParser == null) {
         return "Could not create an XML Parser";
      } else {
         String saxParserName = saxParser.getClass().getName();
         return saxParserName;
      }
   }

   private static SAXParser getSAXParser() {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      if (saxParserFactory == null) {
         return null;
      } else {
         SAXParser saxParser = null;

         try {
            saxParser = saxParserFactory.newSAXParser();
         } catch (Exception var3) {
            ignoreThrowable(var3);
         }

         return saxParser;
      }
   }

   private static String getXMLParserLocation() {
      SAXParser saxParser = getSAXParser();
      if (saxParser == null) {
         return null;
      } else {
         String location = getClassLocation(saxParser.getClass());
         return location;
      }
   }

   private static String getNamespaceParserName() {
      try {
         XMLReader reader = JAXPUtils.getNamespaceXMLReader();
         return reader.getClass().getName();
      } catch (BuildException var1) {
         ignoreThrowable(var1);
         return null;
      }
   }

   private static String getNamespaceParserLocation() {
      try {
         XMLReader reader = JAXPUtils.getNamespaceXMLReader();
         return getClassLocation(reader.getClass());
      } catch (BuildException var1) {
         ignoreThrowable(var1);
         return null;
      }
   }

   private static void ignoreThrowable(Throwable thrown) {
   }

   private static String getClassLocation(Class clazz) {
      File f = LoaderUtils.getClassSource(clazz);
      return f == null ? null : f.getAbsolutePath();
   }

   public static void doReport(PrintStream out) {
      out.println("------- Ant diagnostics report -------");
      out.println(Main.getAntVersion());
      header(out, "Implementation Version");
      out.println("core tasks     : " + getImplementationVersion(class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main));
      Class optional = null;

      try {
         optional = Class.forName("org.apache.tools.ant.taskdefs.optional.Test");
         out.println("optional tasks : " + getImplementationVersion(optional));
      } catch (ClassNotFoundException var3) {
         ignoreThrowable(var3);
         out.println("optional tasks : not available");
      }

      header(out, "ANT PROPERTIES");
      doReportAntProperties(out);
      header(out, "ANT_HOME/lib jar listing");
      doReportAntHomeLibraries(out);
      header(out, "USER_HOME/.ant/lib jar listing");
      doReportUserHomeLibraries(out);
      header(out, "Tasks availability");
      doReportTasksAvailability(out);
      header(out, "org.apache.env.Which diagnostics");
      doReportWhich(out);
      header(out, "XML Parser information");
      doReportParserInfo(out);
      header(out, "System properties");
      doReportSystemProperties(out);
      header(out, "Temp dir");
      doReportTempDir(out);
      header(out, "Locale information");
      doReportLocale(out);
      header(out, "Proxy information");
      doReportProxy(out);
      out.println();
   }

   private static void header(PrintStream out, String section) {
      out.println();
      out.println("-------------------------------------------");
      out.print(" ");
      out.println(section);
      out.println("-------------------------------------------");
   }

   private static void doReportSystemProperties(PrintStream out) {
      Properties sysprops = null;

      try {
         sysprops = System.getProperties();
      } catch (SecurityException var5) {
         ignoreThrowable(var5);
         out.println("Access to System.getProperties() blocked by a security manager");
      }

      Enumeration keys = sysprops.propertyNames();

      while(keys.hasMoreElements()) {
         String key = (String)keys.nextElement();
         String value = getProperty(key);
         out.println(key + " : " + value);
      }

   }

   private static String getProperty(String key) {
      String value;
      try {
         value = System.getProperty(key);
      } catch (SecurityException var3) {
         value = "Access to this property blocked by a security manager";
      }

      return value;
   }

   private static void doReportAntProperties(PrintStream out) {
      Project p = new Project();
      p.initProperties();
      out.println("ant.version: " + p.getProperty("ant.version"));
      out.println("ant.java.version: " + p.getProperty("ant.java.version"));
      out.println("ant.core.lib: " + p.getProperty("ant.core.lib"));
      out.println("ant.home: " + p.getProperty("ant.home"));
   }

   private static void doReportAntHomeLibraries(PrintStream out) {
      out.println("ant.home: " + System.getProperty("ant.home"));
      File[] libs = listLibraries();
      printLibraries(libs, out);
   }

   private static void doReportUserHomeLibraries(PrintStream out) {
      String home = System.getProperty("user.home");
      out.println("user.home: " + home);
      File libDir = new File(home, Launcher.USER_LIBDIR);
      File[] libs = listJarFiles(libDir);
      printLibraries(libs, out);
   }

   private static void printLibraries(File[] libs, PrintStream out) {
      if (libs == null) {
         out.println("No such directory.");
      } else {
         for(int i = 0; i < libs.length; ++i) {
            out.println(libs[i].getName() + " (" + libs[i].length() + " bytes)");
         }

      }
   }

   private static void doReportWhich(PrintStream out) {
      Object error = null;

      try {
         Class which = Class.forName("org.apache.env.Which");
         Method method = which.getMethod("main", array$Ljava$lang$String == null ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String);
         method.invoke((Object)null, new String[0]);
      } catch (ClassNotFoundException var4) {
         out.println("Not available.");
         out.println("Download it at http://xml.apache.org/commons/");
      } catch (InvocationTargetException var5) {
         error = var5.getTargetException() == null ? var5 : var5.getTargetException();
      } catch (Throwable var6) {
         error = var6;
      }

      if (error != null) {
         out.println("Error while running org.apache.env.Which");
         ((Throwable)error).printStackTrace();
      }

   }

   private static void doReportTasksAvailability(PrintStream out) {
      InputStream is = (class$org$apache$tools$ant$Main == null ? (class$org$apache$tools$ant$Main = class$("org.apache.tools.ant.Main")) : class$org$apache$tools$ant$Main).getResourceAsStream("/org/apache/tools/ant/taskdefs/defaults.properties");
      if (is == null) {
         out.println("None available");
      } else {
         Properties props = new Properties();

         try {
            props.load(is);
            Enumeration keys = props.keys();

            while(keys.hasMoreElements()) {
               String key = (String)keys.nextElement();
               String classname = props.getProperty(key);

               try {
                  Class.forName(classname);
                  props.remove(key);
               } catch (ClassNotFoundException var8) {
                  out.println(key + " : Not Available " + "(the implementation class is not present)");
               } catch (NoClassDefFoundError var9) {
                  String pkg = var9.getMessage().replace('/', '.');
                  out.println(key + " : Missing dependency " + pkg);
               } catch (LinkageError var10) {
                  out.println(key + " : Initialization error");
               }
            }

            if (props.size() == 0) {
               out.println("All defined tasks are available");
            } else {
               out.println("A task being missing/unavailable should only matter if you are trying to use it");
            }
         } catch (IOException var11) {
            out.println(var11.getMessage());
         }
      }

   }

   private static void doReportParserInfo(PrintStream out) {
      String parserName = getXmlParserName();
      String parserLocation = getXMLParserLocation();
      printParserInfo(out, "XML Parser", parserName, parserLocation);
      printParserInfo(out, "Namespace-aware parser", getNamespaceParserName(), getNamespaceParserLocation());
   }

   private static void printParserInfo(PrintStream out, String parserType, String parserName, String parserLocation) {
      if (parserName == null) {
         parserName = "unknown";
      }

      if (parserLocation == null) {
         parserLocation = "unknown";
      }

      out.println(parserType + " : " + parserName);
      out.println(parserType + " Location: " + parserLocation);
   }

   private static void doReportTempDir(PrintStream out) {
      String tempdir = System.getProperty("java.io.tmpdir");
      if (tempdir == null) {
         out.println("Warning: java.io.tmpdir is undefined");
      } else {
         out.println("Temp dir is " + tempdir);
         File tempDirectory = new File(tempdir);
         if (!tempDirectory.exists()) {
            out.println("Warning, java.io.tmpdir directory does not exist: " + tempdir);
         } else {
            long now = System.currentTimeMillis();
            File tempFile = null;
            FileOutputStream fileout = null;

            try {
               tempFile = File.createTempFile("diag", "txt", tempDirectory);
               fileout = new FileOutputStream(tempFile);
               byte[] buffer = new byte[1024];

               for(int i = 0; i < 32; ++i) {
                  fileout.write(buffer);
               }

               fileout.close();
               fileout = null;
               long filetime = tempFile.lastModified();
               tempFile.delete();
               out.println("Temp dir is writeable");
               long drift = filetime - now;
               out.println("Temp dir alignment with system clock is " + drift + " ms");
               if (Math.abs(drift) > 10000L) {
                  out.println("Warning: big clock drift -maybe a network filesystem");
               }
            } catch (IOException var16) {
               ignoreThrowable(var16);
               out.println("Failed to create a temporary file in the temp dir " + tempdir);
               out.println("File  " + tempFile + " could not be created/written to");
            } finally {
               FileUtils.close((OutputStream)fileout);
               if (tempFile != null && tempFile.exists()) {
                  tempFile.delete();
               }

            }

         }
      }
   }

   private static void doReportLocale(PrintStream out) {
      Calendar cal = Calendar.getInstance();
      TimeZone tz = cal.getTimeZone();
      out.println("Timezone " + tz.getDisplayName() + " offset=" + tz.getOffset(cal.get(0), cal.get(1), cal.get(2), cal.get(5), cal.get(7), ((cal.get(11) * 60 + cal.get(12)) * 60 + cal.get(13)) * 1000 + cal.get(14)));
   }

   private static void printProperty(PrintStream out, String key) {
      String value = getProperty(key);
      if (value != null) {
         out.print(key);
         out.print(" = ");
         out.print('"');
         out.print(value);
         out.println('"');
      }

   }

   private static void doReportProxy(PrintStream out) {
      printProperty(out, "http.proxyHost");
      printProperty(out, "http.proxyPort");
      printProperty(out, "http.proxyUser");
      printProperty(out, "http.proxyPassword");
      printProperty(out, "http.nonProxyHosts");
      printProperty(out, "https.proxyHost");
      printProperty(out, "https.proxyPort");
      printProperty(out, "https.nonProxyHosts");
      printProperty(out, "ftp.proxyHost");
      printProperty(out, "ftp.proxyPort");
      printProperty(out, "ftp.nonProxyHosts");
      printProperty(out, "socksProxyHost");
      printProperty(out, "socksProxyPort");
      printProperty(out, "java.net.socks.username");
      printProperty(out, "java.net.socks.password");
      if (JavaEnvUtils.getJavaVersionNumber() >= 15) {
         printProperty(out, "java.net.useSystemProxies");
         String var1 = "org.apache.tools.ant.util.java15.ProxyDiagnostics";

         try {
            Class proxyDiagClass = Class.forName("org.apache.tools.ant.util.java15.ProxyDiagnostics");
            Object instance = proxyDiagClass.newInstance();
            out.println("Java1.5+ proxy settings:");
            out.println(instance.toString());
         } catch (ClassNotFoundException var4) {
         } catch (IllegalAccessException var5) {
         } catch (InstantiationException var6) {
         } catch (NoClassDefFoundError var7) {
         }

      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
