package org.apache.tools.ant.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Vector;
import org.apache.tools.ant.taskdefs.condition.Os;

public final class JavaEnvUtils {
   private static final boolean IS_DOS = Os.isFamily("dos");
   private static final boolean IS_NETWARE = Os.isName("netware");
   private static final boolean IS_AIX = Os.isName("aix");
   private static final String JAVA_HOME = System.getProperty("java.home");
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static String javaVersion;
   private static int javaVersionNumber;
   public static final String JAVA_1_0 = "1.0";
   public static final String JAVA_1_1 = "1.1";
   public static final String JAVA_1_2 = "1.2";
   public static final String JAVA_1_3 = "1.3";
   public static final String JAVA_1_4 = "1.4";
   public static final String JAVA_1_5 = "1.5";
   public static final String JAVA_1_6 = "1.6";
   private static boolean kaffeDetected;
   private static Vector jrePackages;

   private JavaEnvUtils() {
   }

   public static String getJavaVersion() {
      return javaVersion;
   }

   public static int getJavaVersionNumber() {
      return javaVersionNumber;
   }

   public static boolean isJavaVersion(String version) {
      return javaVersion.equals(version);
   }

   public static boolean isAtLeastJavaVersion(String version) {
      return javaVersion.compareTo(version) >= 0;
   }

   public static boolean isKaffe() {
      return kaffeDetected;
   }

   public static String getJreExecutable(String command) {
      if (IS_NETWARE) {
         return command;
      } else {
         File jExecutable = null;
         if (IS_AIX) {
            jExecutable = findInDir(JAVA_HOME + "/sh", command);
         }

         if (jExecutable == null) {
            jExecutable = findInDir(JAVA_HOME + "/bin", command);
         }

         return jExecutable != null ? jExecutable.getAbsolutePath() : addExtension(command);
      }
   }

   public static String getJdkExecutable(String command) {
      if (IS_NETWARE) {
         return command;
      } else {
         File jExecutable = null;
         if (IS_AIX) {
            jExecutable = findInDir(JAVA_HOME + "/../sh", command);
         }

         if (jExecutable == null) {
            jExecutable = findInDir(JAVA_HOME + "/../bin", command);
         }

         return jExecutable != null ? jExecutable.getAbsolutePath() : getJreExecutable(command);
      }
   }

   private static String addExtension(String command) {
      return command + (IS_DOS ? ".exe" : "");
   }

   private static File findInDir(String dirName, String commandName) {
      File dir = FILE_UTILS.normalize(dirName);
      File executable = null;
      if (dir.exists()) {
         executable = new File(dir, addExtension(commandName));
         if (!executable.exists()) {
            executable = null;
         }
      }

      return executable;
   }

   private static void buildJrePackages() {
      jrePackages = new Vector();
      switch(javaVersionNumber) {
      case 15:
      case 16:
         jrePackages.addElement("com.sun.org.apache");
      case 14:
         if (javaVersionNumber == 14) {
            jrePackages.addElement("org.apache.crimson");
            jrePackages.addElement("org.apache.xalan");
            jrePackages.addElement("org.apache.xml");
            jrePackages.addElement("org.apache.xpath");
         }

         jrePackages.addElement("org.ietf.jgss");
         jrePackages.addElement("org.w3c.dom");
         jrePackages.addElement("org.xml.sax");
      case 13:
         jrePackages.addElement("org.omg");
         jrePackages.addElement("com.sun.corba");
         jrePackages.addElement("com.sun.jndi");
         jrePackages.addElement("com.sun.media");
         jrePackages.addElement("com.sun.naming");
         jrePackages.addElement("com.sun.org.omg");
         jrePackages.addElement("com.sun.rmi");
         jrePackages.addElement("sunw.io");
         jrePackages.addElement("sunw.util");
      case 12:
         jrePackages.addElement("com.sun.java");
         jrePackages.addElement("com.sun.image");
      case 11:
      default:
         jrePackages.addElement("sun");
         jrePackages.addElement("java");
         jrePackages.addElement("javax");
      }
   }

   public static Vector getJrePackageTestCases() {
      Vector tests = new Vector();
      tests.addElement("java.lang.Object");
      switch(javaVersionNumber) {
      case 15:
      case 16:
         tests.addElement("com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl ");
      case 14:
         tests.addElement("sun.audio.AudioPlayer");
         if (javaVersionNumber == 14) {
            tests.addElement("org.apache.crimson.parser.ContentModel");
            tests.addElement("org.apache.xalan.processor.ProcessorImport");
            tests.addElement("org.apache.xml.utils.URI");
            tests.addElement("org.apache.xpath.XPathFactory");
         }

         tests.addElement("org.ietf.jgss.Oid");
         tests.addElement("org.w3c.dom.Attr");
         tests.addElement("org.xml.sax.XMLReader");
      case 13:
         tests.addElement("org.omg.CORBA.Any");
         tests.addElement("com.sun.corba.se.internal.corba.AnyImpl");
         tests.addElement("com.sun.jndi.ldap.LdapURL");
         tests.addElement("com.sun.media.sound.Printer");
         tests.addElement("com.sun.naming.internal.VersionHelper");
         tests.addElement("com.sun.org.omg.CORBA.Initializer");
         tests.addElement("sunw.io.Serializable");
         tests.addElement("sunw.util.EventListener");
      case 12:
         tests.addElement("javax.accessibility.Accessible");
         tests.addElement("sun.misc.BASE64Encoder");
         tests.addElement("com.sun.image.codec.jpeg.JPEGCodec");
      case 11:
      default:
         tests.addElement("sun.reflect.SerializationConstructorAccessorImpl");
         tests.addElement("sun.net.www.http.HttpClient");
         tests.addElement("sun.audio.AudioPlayer");
         return tests;
      }
   }

   public static Vector getJrePackages() {
      if (jrePackages == null) {
         buildJrePackages();
      }

      return jrePackages;
   }

   public static File createVmsJavaOptionFile(String[] cmd) throws IOException {
      File script = FILE_UTILS.createTempFile("ANT", ".JAVA_OPTS", (File)null);
      PrintWriter out = null;

      try {
         out = new PrintWriter(new BufferedWriter(new FileWriter(script)));

         for(int i = 0; i < cmd.length; ++i) {
            out.println(cmd[i]);
         }
      } finally {
         FileUtils.close((Writer)out);
      }

      return script;
   }

   public static String getJavaHome() {
      return JAVA_HOME;
   }

   static {
      try {
         javaVersion = "1.0";
         javaVersionNumber = 10;
         Class.forName("java.lang.Void");
         javaVersion = "1.1";
         ++javaVersionNumber;
         Class.forName("java.lang.ThreadLocal");
         javaVersion = "1.2";
         ++javaVersionNumber;
         Class.forName("java.lang.StrictMath");
         javaVersion = "1.3";
         ++javaVersionNumber;
         Class.forName("java.lang.CharSequence");
         javaVersion = "1.4";
         ++javaVersionNumber;
         Class.forName("java.net.Proxy");
         javaVersion = "1.5";
         ++javaVersionNumber;
         Class.forName("java.util.ServiceLoader");
         javaVersion = "1.6";
         ++javaVersionNumber;
      } catch (Throwable var2) {
      }

      kaffeDetected = false;

      try {
         Class.forName("kaffe.util.NotImplemented");
         kaffeDetected = true;
      } catch (Throwable var1) {
      }

   }
}
