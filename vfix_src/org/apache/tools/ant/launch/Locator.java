package org.apache.tools.ant.launch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public final class Locator {
   public static final String URI_ENCODING = "UTF-8";
   private static boolean[] gNeedEscaping = new boolean[128];
   private static char[] gAfterEscaping1 = new char[128];
   private static char[] gAfterEscaping2 = new char[128];
   private static char[] gHexChs = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$launch$Locator;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$io$File;

   private Locator() {
   }

   public static File getClassSource(Class c) {
      String classResource = c.getName().replace('.', '/') + ".class";
      return getResourceSource(c.getClassLoader(), classResource);
   }

   public static File getResourceSource(ClassLoader c, String resource) {
      if (c == null) {
         c = (class$org$apache$tools$ant$launch$Locator == null ? (class$org$apache$tools$ant$launch$Locator = class$("org.apache.tools.ant.launch.Locator")) : class$org$apache$tools$ant$launch$Locator).getClassLoader();
      }

      URL url = null;
      if (c == null) {
         url = ClassLoader.getSystemResource(resource);
      } else {
         url = c.getResource(resource);
      }

      if (url != null) {
         String u = url.toString();
         int tail;
         String dirName;
         if (u.startsWith("jar:file:")) {
            tail = u.indexOf("!");
            dirName = u.substring(4, tail);
            return new File(fromURI(dirName));
         }

         if (u.startsWith("file:")) {
            tail = u.indexOf(resource);
            dirName = u.substring(0, tail);
            return new File(fromURI(dirName));
         }
      }

      return null;
   }

   public static String fromURI(String uri) {
      Class uriClazz = null;

      try {
         uriClazz = Class.forName("java.net.URI");
      } catch (ClassNotFoundException var11) {
      }

      if (uriClazz != null && uri.startsWith("file:/")) {
         try {
            Method createMethod = uriClazz.getMethod("create", class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            Object uriObj = createMethod.invoke((Object)null, uri);
            Constructor fileConst = (class$java$io$File == null ? (class$java$io$File = class$("java.io.File")) : class$java$io$File).getConstructor(uriClazz);
            File f = (File)fileConst.newInstance(uriObj);
            return f.getAbsolutePath();
         } catch (InvocationTargetException var12) {
            Throwable e2 = var12.getTargetException();
            if (e2 instanceof IllegalArgumentException) {
               throw (IllegalArgumentException)e2;
            }

            e2.printStackTrace();
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

      URL url = null;

      try {
         url = new URL(uri);
      } catch (MalformedURLException var10) {
      }

      if (url != null && "file".equals(url.getProtocol())) {
         StringBuffer buf = new StringBuffer(url.getHost());
         if (buf.length() > 0) {
            buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
         }

         String file = url.getFile();
         int queryPos = file.indexOf(63);
         buf.append(queryPos < 0 ? file : file.substring(0, queryPos));
         uri = buf.toString().replace('/', File.separatorChar);
         if (File.pathSeparatorChar == ';' && uri.startsWith("\\") && uri.length() > 2 && Character.isLetter(uri.charAt(1)) && uri.lastIndexOf(58) > -1) {
            uri = uri.substring(1);
         }

         String path = null;

         try {
            path = decodeUri(uri);
            String cwd = System.getProperty("user.dir");
            int posi = cwd.indexOf(":");
            if (posi > 0 && path.startsWith(File.separator)) {
               path = cwd.substring(0, posi + 1) + path;
            }

            return path;
         } catch (UnsupportedEncodingException var9) {
            throw new IllegalStateException("Could not convert URI to path: " + var9.getMessage());
         }
      } else {
         throw new IllegalArgumentException("Can only handle valid file: URIs");
      }
   }

   public static String decodeUri(String uri) throws UnsupportedEncodingException {
      if (uri.indexOf(37) == -1) {
         return uri;
      } else {
         ByteArrayOutputStream sb = new ByteArrayOutputStream(uri.length());
         CharacterIterator iter = new StringCharacterIterator(uri);

         for(char c = iter.first(); c != '\uffff'; c = iter.next()) {
            if (c == '%') {
               char c1 = iter.next();
               if (c1 != '\uffff') {
                  int i1 = Character.digit(c1, 16);
                  char c2 = iter.next();
                  if (c2 != '\uffff') {
                     int i2 = Character.digit(c2, 16);
                     sb.write((char)((i1 << 4) + i2));
                  }
               }
            } else {
               sb.write(c);
            }
         }

         return sb.toString("UTF-8");
      }
   }

   public static String encodeURI(String path) throws UnsupportedEncodingException {
      int i = 0;
      int len = path.length();
      int ch = false;

      StringBuffer sb;
      for(sb = null; i < len; ++i) {
         int ch = path.charAt(i);
         if (ch >= 128) {
            break;
         }

         if (gNeedEscaping[ch]) {
            if (sb == null) {
               sb = new StringBuffer(path.substring(0, i));
            }

            sb.append('%');
            sb.append(gAfterEscaping1[ch]);
            sb.append(gAfterEscaping2[ch]);
         } else if (sb != null) {
            sb.append((char)ch);
         }
      }

      if (i < len) {
         if (sb == null) {
            sb = new StringBuffer(path.substring(0, i));
         }

         byte[] bytes = null;
         byte[] bytes = path.substring(i).getBytes("UTF-8");
         len = bytes.length;

         for(i = 0; i < len; ++i) {
            byte b = bytes[i];
            if (b < 0) {
               int ch = b + 256;
               sb.append('%');
               sb.append(gHexChs[ch >> 4]);
               sb.append(gHexChs[ch & 15]);
            } else if (gNeedEscaping[b]) {
               sb.append('%');
               sb.append(gAfterEscaping1[b]);
               sb.append(gAfterEscaping2[b]);
            } else {
               sb.append((char)b);
            }
         }
      }

      return sb == null ? path : sb.toString();
   }

   public static URL fileToURL(File file) throws MalformedURLException {
      try {
         return new URL(encodeURI(file.toURL().toString()));
      } catch (UnsupportedEncodingException var2) {
         throw new MalformedURLException(var2.toString());
      }
   }

   public static File getToolsJar() {
      boolean toolsJarAvailable = false;

      try {
         Class.forName("com.sun.tools.javac.Main");
         toolsJarAvailable = true;
      } catch (Exception var4) {
         try {
            Class.forName("sun.tools.javac.Main");
            toolsJarAvailable = true;
         } catch (Exception var3) {
         }
      }

      if (toolsJarAvailable) {
         return null;
      } else {
         String javaHome = System.getProperty("java.home");
         File toolsJar = new File(javaHome + "/lib/tools.jar");
         if (toolsJar.exists()) {
            return toolsJar;
         } else {
            if (javaHome.toLowerCase(Locale.US).endsWith(File.separator + "jre")) {
               javaHome = javaHome.substring(0, javaHome.length() - 4);
               toolsJar = new File(javaHome + "/lib/tools.jar");
            }

            if (!toolsJar.exists()) {
               System.out.println("Unable to locate tools.jar. Expected to find it in " + toolsJar.getPath());
               return null;
            } else {
               return toolsJar;
            }
         }
      }
   }

   public static URL[] getLocationURLs(File location) throws MalformedURLException {
      return getLocationURLs(location, new String[]{".jar"});
   }

   public static URL[] getLocationURLs(File location, final String[] extensions) throws MalformedURLException {
      URL[] urls = new URL[0];
      if (!location.exists()) {
         return urls;
      } else {
         int i;
         if (!location.isDirectory()) {
            urls = new URL[1];
            String path = location.getPath();

            for(i = 0; i < extensions.length; ++i) {
               if (path.toLowerCase().endsWith(extensions[i])) {
                  urls[0] = fileToURL(location);
                  break;
               }
            }

            return urls;
         } else {
            File[] matches = location.listFiles(new FilenameFilter() {
               public boolean accept(File dir, String name) {
                  for(int i = 0; i < extensions.length; ++i) {
                     if (name.toLowerCase().endsWith(extensions[i])) {
                        return true;
                     }
                  }

                  return false;
               }
            });
            urls = new URL[matches.length];

            for(i = 0; i < matches.length; ++i) {
               urls[i] = fileToURL(matches[i]);
            }

            return urls;
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

   static {
      for(int i = 0; i <= 31; ++i) {
         gNeedEscaping[i] = true;
         gAfterEscaping1[i] = gHexChs[i >> 4];
         gAfterEscaping2[i] = gHexChs[i & 15];
      }

      gNeedEscaping[127] = true;
      gAfterEscaping1[127] = '7';
      gAfterEscaping2[127] = 'F';
      char[] escChs = new char[]{' ', '<', '>', '#', '%', '"', '{', '}', '|', '\\', '^', '~', '[', ']', '`'};
      int len = escChs.length;

      for(int i = 0; i < len; ++i) {
         char ch = escChs[i];
         gNeedEscaping[ch] = true;
         gAfterEscaping1[ch] = gHexChs[ch >> 4];
         gAfterEscaping2[ch] = gHexChs[ch & 15];
      }

   }
}
