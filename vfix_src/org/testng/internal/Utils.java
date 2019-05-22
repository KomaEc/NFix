package org.testng.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.ITestNGMethod;
import org.testng.TestNGException;
import org.testng.TestRunner;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Lists;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.log.TextFormatter;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.XmlClass;

public final class Utils {
   private static final String LINE_SEP = System.getProperty("line.separator");
   public static final char[] SPECIAL_CHARACTERS = new char[]{'*', '/', '\\', '?', '%', ':', ';', '<', '>', '&', '~', '|'};
   public static final char CHAR_REPLACEMENT = '_';
   public static final char UNICODE_REPLACEMENT = '�';
   private static final Map<Character, String> ESCAPES = new HashMap<Character, String>() {
      private static final long serialVersionUID = 1285607660247157523L;

      {
         this.put('<', "&lt;");
         this.put('>', "&gt;");
         this.put('\'', "&apos;");
         this.put('"', "&quot;");
         this.put('&', "&amp;");
      }
   };

   private Utils() {
   }

   public static String[] stringToArray(String s) {
      StringTokenizer st = new StringTokenizer(s, " ,");
      String[] result = new String[st.countTokens()];

      for(int i = 0; i < result.length; ++i) {
         result[i] = st.nextToken();
      }

      return result;
   }

   public static XmlClass[] classesToXmlClasses(Class<?>[] classes) {
      List<XmlClass> result = Lists.newArrayList();
      Class[] arr$ = classes;
      int len$ = classes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> cls = arr$[i$];
         result.add(new XmlClass(cls, true));
      }

      return (XmlClass[])result.toArray(new XmlClass[classes.length]);
   }

   public static String[] parseMultiLine(String line) {
      List<String> vResult = Lists.newArrayList();
      if (isStringNotBlank(line)) {
         StringTokenizer st = new StringTokenizer(line, " ");

         while(st.hasMoreTokens()) {
            vResult.add(st.nextToken());
         }
      }

      return (String[])vResult.toArray(new String[vResult.size()]);
   }

   public static void writeUtf8File(String outputDir, String fileName, XMLStringBuffer xsb, String prefix) {
      try {
         File file = new File(outputDir, fileName);
         if (!file.exists()) {
            file.createNewFile();
         }

         OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
         if (prefix != null) {
            w.append(prefix);
         }

         xsb.toWriter(w);
         w.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public static void writeUtf8File(@Nullable String outputDir, String fileName, String sb) {
      String outDirPath = outputDir != null ? outputDir : "";
      File outDir = new File(outDirPath);
      writeFile(outDir, fileName, escapeUnicode(sb), "UTF-8", false);
   }

   public static void writeFile(@Nullable String outputDir, String fileName, String sb) {
      String outDirPath = outputDir != null ? outputDir : "";
      File outDir = new File(outDirPath);
      writeFile(outDir, fileName, sb, (String)null, false);
   }

   public static void appendToFile(@Nullable String outputDir, String fileName, String sb) {
      String outDirPath = outputDir != null ? outputDir : "";
      File outDir = new File(outDirPath);
      writeFile(outDir, fileName, sb, (String)null, true);
   }

   private static void writeFile(@Nullable File outDir, String fileName, String sb, @Nullable String encoding, boolean append) {
      try {
         if (outDir == null) {
            outDir = (new File("")).getAbsoluteFile();
         }

         if (!outDir.exists()) {
            outDir.mkdirs();
         }

         fileName = replaceSpecialCharacters(fileName);
         File outputFile = new File(outDir, fileName);
         if (!append) {
            outputFile.delete();
            outputFile.createNewFile();
         }

         writeFile(outputFile, sb, encoding, append);
      } catch (IOException var6) {
         if (TestRunner.getVerbose() > 1) {
            var6.printStackTrace();
         } else {
            log("[Utils]", 1, var6.getMessage());
         }
      }

   }

   private static void writeFile(File outputFile, String sb, @Nullable String encoding, boolean append) {
      BufferedWriter fw = null;

      try {
         if (!outputFile.exists()) {
            outputFile.createNewFile();
         }

         OutputStreamWriter osw = null;
         if (null != encoding) {
            osw = new OutputStreamWriter(new FileOutputStream(outputFile, append), encoding);
         } else {
            osw = new OutputStreamWriter(new FileOutputStream(outputFile, append));
         }

         fw = new BufferedWriter(osw);
         fw.write(sb);
         log("", 3, "Creating " + outputFile.getAbsolutePath());
      } catch (IOException var14) {
         if (TestRunner.getVerbose() > 1) {
            System.err.println("ERROR WHILE WRITING TO " + outputFile);
            var14.printStackTrace();
         } else {
            log("[Utils]", 1, "Error while writing to " + outputFile + ": " + var14.getMessage());
         }
      } finally {
         try {
            if (fw != null) {
               fw.close();
            }
         } catch (IOException var13) {
         }

      }

   }

   private static void ppp(String s) {
      log("Utils", 0, s);
   }

   public static void dumpMap(Map<?, ?> result) {
      System.out.println("vvvvv");
      Iterator i$ = result.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<?, ?> entry = (Entry)i$.next();
         System.out.println(entry.getKey() + " => " + entry.getValue());
      }

      System.out.println("^^^^^");
   }

   public static void dumpMethods(List<ITestNGMethod> allMethods) {
      ppp("======== METHODS:");
      Iterator i$ = allMethods.iterator();

      while(i$.hasNext()) {
         ITestNGMethod tm = (ITestNGMethod)i$.next();
         ppp("  " + tm);
      }

   }

   public static String[] dependentGroupsForThisMethodForTest(Method m, IAnnotationFinder finder) {
      List<String> vResult = Lists.newArrayList();
      Class<?> cls = m.getDeclaringClass();
      ITestAnnotation tc = AnnotationHelper.findTest(finder, cls);
      if (null != tc) {
         String[] arr$ = tc.getDependsOnGroups();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            vResult.add(group);
         }
      }

      ITestAnnotation tm = AnnotationHelper.findTest(finder, m);
      if (null != tm) {
         String[] groups = tm.getDependsOnGroups();
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            vResult.add(group);
         }
      }

      return (String[])vResult.toArray(new String[vResult.size()]);
   }

   public static String[] groupsForThisMethodForTest(Method m, IAnnotationFinder finder) {
      List<String> vResult = Lists.newArrayList();
      Class<?> cls = m.getDeclaringClass();
      ITestAnnotation tc = AnnotationHelper.findTest(finder, cls);
      if (null != tc) {
         String[] arr$ = tc.getGroups();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            vResult.add(group);
         }
      }

      ITestAnnotation tm = AnnotationHelper.findTest(finder, m);
      if (null != tm) {
         String[] groups = tm.getGroups();
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            vResult.add(group);
         }
      }

      return (String[])vResult.toArray(new String[vResult.size()]);
   }

   public static String[] groupsForThisMethodForConfiguration(Method m, IAnnotationFinder finder) {
      String[] result = new String[0];
      ITestAnnotation tm = AnnotationHelper.findTest(finder, m);
      if (null != tm) {
         result = tm.getGroups();
      }

      return result;
   }

   public static String[] dependentGroupsForThisMethodForConfiguration(Method m, IAnnotationFinder finder) {
      String[] result = new String[0];
      IConfigurationAnnotation tm = AnnotationHelper.findConfiguration(finder, m);
      if (null != tm) {
         result = tm.getDependsOnGroups();
      }

      return result;
   }

   public static void log(String msg) {
      log("Utils", 2, msg);
   }

   public static void log(String cls, int level, String msg) {
      if (TestRunner.getVerbose() >= level) {
         if (cls.length() > 0) {
            System.out.println("[" + cls + "] " + msg);
         } else {
            System.out.println(msg);
         }
      }

   }

   public static void error(String errorMessage) {
      System.err.println("[Error] " + errorMessage);
   }

   public static int calculateInvokedMethodCount(ITestNGMethod[] methods) {
      return methods.length;
   }

   public static String[] split(String string, String sep) {
      if (string != null && string.length() != 0) {
         int start = 0;
         int idx = string.indexOf(sep, start);
         int len = sep.length();

         List strings;
         for(strings = Lists.newArrayList(); idx != -1; idx = string.indexOf(sep, start)) {
            strings.add(string.substring(start, idx).trim());
            start = idx + len;
         }

         strings.add(string.substring(start).trim());
         return (String[])strings.toArray(new String[strings.size()]);
      } else {
         return new String[0];
      }
   }

   public static void initLogger(Logger logger, String outputLogPath) {
      try {
         logger.setUseParentHandlers(false);
         FileHandler fh = new FileHandler(outputLogPath);
         fh.setFormatter(new TextFormatter());
         fh.setLevel(Level.INFO);
         logger.addHandler(fh);
      } catch (IOException | SecurityException var3) {
         var3.printStackTrace();
      }

   }

   public static void logInvocation(String reason, Method thisMethod, Object[] parameters) {
      String clsName = thisMethod.getDeclaringClass().getName();
      int n = clsName.lastIndexOf(".");
      if (n >= 0) {
         clsName = clsName.substring(n + 1);
      }

      String methodName = clsName + '.' + thisMethod.getName();
      if (TestRunner.getVerbose() >= 2) {
         StringBuffer paramString = new StringBuffer();
         if (parameters != null) {
            Object[] arr$ = parameters;
            int len$ = parameters.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Object p = arr$[i$];
               paramString.append(p.toString()).append(' ');
            }
         }

         log("", 2, "Invoking " + reason + methodName + '(' + paramString + ')');
      }

   }

   public static void writeResourceToFile(File file, String resourceName, Class<?> clasz) throws IOException {
      InputStream inputStream = clasz.getResourceAsStream("/" + resourceName);
      if (inputStream == null) {
         System.err.println("Couldn't find resource on the class path: " + resourceName);
      } else {
         try {
            FileOutputStream outputStream = new FileOutputStream(file);

            try {
               byte[] buffer = new byte[4096];

               int nread;
               while(0 < (nread = inputStream.read(buffer))) {
                  outputStream.write(buffer, 0, nread);
               }
            } finally {
               outputStream.close();
            }
         } finally {
            inputStream.close();
         }
      }

   }

   public static String defaultIfStringEmpty(String s, String defaultValue) {
      return isStringEmpty(s) ? defaultValue : s;
   }

   public static boolean isStringBlank(String s) {
      return s == null || "".equals(s.trim());
   }

   public static boolean isStringEmpty(String s) {
      return s == null || "".equals(s);
   }

   public static boolean isStringNotBlank(String s) {
      return !isStringBlank(s);
   }

   public static boolean isStringNotEmpty(String s) {
      return !isStringEmpty(s);
   }

   public static String[] stackTrace(Throwable t, boolean toHtml) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.flush();
      String fullStackTrace = sw.getBuffer().toString();
      String shortStackTrace;
      if (!Boolean.getBoolean("testng.show.stack.frames") && TestRunner.getVerbose() < 2) {
         shortStackTrace = filterTrace(sw.getBuffer().toString());
      } else {
         shortStackTrace = fullStackTrace;
      }

      if (toHtml) {
         shortStackTrace = escapeHtml(shortStackTrace);
         fullStackTrace = escapeHtml(fullStackTrace);
      }

      return new String[]{shortStackTrace, fullStackTrace};
   }

   public static String escapeHtml(String s) {
      if (s == null) {
         return null;
      } else {
         StringBuilder result = new StringBuilder();

         for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            String nc = (String)ESCAPES.get(c);
            if (nc != null) {
               result.append(nc);
            } else {
               result.append(c);
            }
         }

         return result.toString();
      }
   }

   public static String escapeUnicode(String s) {
      if (s == null) {
         return null;
      } else {
         StringBuilder result = new StringBuilder();

         for(int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            char ca = Character.isDefined(c) ? c : '�';
            result.append(ca);
         }

         return result.toString();
      }
   }

   private static String filterTrace(String trace) {
      StringReader stringReader = new StringReader(trace);
      BufferedReader bufferedReader = new BufferedReader(stringReader);
      StringBuffer buf = new StringBuffer();

      try {
         String line = bufferedReader.readLine();
         if (line == null) {
            return "";
         }

         buf.append(line).append(LINE_SEP);
         String[] excludedStrings = new String[]{"org.testng", "reflect", "org.apache.maven.surefire"};
         int excludedCount = 0;

         while((line = bufferedReader.readLine()) != null) {
            boolean isExcluded = false;
            String[] arr$ = excludedStrings;
            int len$ = excludedStrings.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String excluded = arr$[i$];
               if (line.contains(excluded)) {
                  isExcluded = true;
                  ++excludedCount;
                  break;
               }
            }

            if (!isExcluded) {
               buf.append(line).append(LINE_SEP);
            }
         }

         if (excludedCount > 0) {
            buf.append("... Removed " + excludedCount + " stack frames");
         }
      } catch (IOException var12) {
      }

      return buf.toString();
   }

   public static String toString(Object object, Class<?> objectClass) {
      if (null == object) {
         return "null";
      } else {
         String toString = object.toString();
         if (isStringEmpty(toString)) {
            return "\"\"";
         } else {
            return String.class.equals(objectClass) ? "\"" + toString + '"' : toString;
         }
      }
   }

   public static String detailedMethodName(ITestNGMethod method, boolean fqn) {
      StringBuffer buf = new StringBuffer();
      if (method.isBeforeSuiteConfiguration()) {
         buf.append("@BeforeSuite ");
      } else if (method.isBeforeTestConfiguration()) {
         buf.append("@BeforeTest ");
      } else if (method.isBeforeClassConfiguration()) {
         buf.append("@BeforeClass ");
      } else if (method.isBeforeGroupsConfiguration()) {
         buf.append("@BeforeGroups ");
      } else if (method.isBeforeMethodConfiguration()) {
         buf.append("@BeforeMethod ");
      } else if (method.isAfterMethodConfiguration()) {
         buf.append("@AfterMethod ");
      } else if (method.isAfterGroupsConfiguration()) {
         buf.append("@AfterGroups ");
      } else if (method.isAfterClassConfiguration()) {
         buf.append("@AfterClass ");
      } else if (method.isAfterTestConfiguration()) {
         buf.append("@AfterTest ");
      } else if (method.isAfterSuiteConfiguration()) {
         buf.append("@AfterSuite ");
      }

      return buf.append(fqn ? method.toString() : method.getMethodName()).toString();
   }

   public static String arrayToString(String[] strings) {
      StringBuffer result = new StringBuffer("");
      if (strings != null && strings.length > 0) {
         for(int i = 0; i < strings.length; ++i) {
            result.append(strings[i]);
            if (i < strings.length - 1) {
               result.append(", ");
            }
         }
      }

      return result.toString();
   }

   public static String replaceSpecialCharacters(String fileName) {
      if (fileName != null && fileName.length() != 0) {
         char[] arr$ = SPECIAL_CHARACTERS;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            char element = arr$[i$];
            fileName = fileName.replace(element, '_');
         }

         return fileName;
      } else {
         return fileName;
      }
   }

   public static <T> String join(List<T> objects, String separator) {
      StringBuilder result = new StringBuilder();

      for(int i = 0; i < objects.size(); ++i) {
         if (i > 0) {
            result.append(separator);
         }

         result.append(objects.get(i).toString());
      }

      return result.toString();
   }

   public static void copyFile(File from, File to) {
      try {
         InputStream in = new FileInputStream(from);
         to.getParentFile().mkdirs();
         OutputStream out = new FileOutputStream(to);
         byte[] buf = new byte[1024];

         int len;
         while((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
         }

         in.close();
         out.close();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public static File createTempFile(String content) {
      try {
         File result = File.createTempFile("testng-tmp", "");
         result.deleteOnExit();
         BufferedWriter out = new BufferedWriter(new FileWriter(result));
         out.write(content);
         out.close();
         return result;
      } catch (IOException var3) {
         throw new TestNGException(var3);
      }
   }

   public static void checkInstanceOrStatic(Object instance, Method method) {
      if (instance == null && !Modifier.isStatic(method.getModifiers())) {
         throw new TestNGException("Can't invoke " + method + ": either make it static or add " + "a no-args constructor to your class");
      }
   }

   public static String toString(Object obj) {
      String result;
      if (obj != null) {
         if (obj instanceof boolean[]) {
            result = Arrays.toString((boolean[])((boolean[])obj));
         } else if (obj instanceof byte[]) {
            result = Arrays.toString((byte[])((byte[])obj));
         } else if (obj instanceof char[]) {
            result = Arrays.toString((char[])((char[])obj));
         } else if (obj instanceof double[]) {
            result = Arrays.toString((double[])((double[])obj));
         } else if (obj instanceof float[]) {
            result = Arrays.toString((float[])((float[])obj));
         } else if (obj instanceof int[]) {
            result = Arrays.toString((int[])((int[])obj));
         } else if (obj instanceof long[]) {
            result = Arrays.toString((long[])((long[])obj));
         } else if (obj instanceof Object[]) {
            result = Arrays.deepToString((Object[])((Object[])obj));
         } else if (obj instanceof short[]) {
            result = Arrays.toString((short[])((short[])obj));
         } else {
            result = obj.toString();
         }
      } else {
         result = "null";
      }

      return result;
   }
}
