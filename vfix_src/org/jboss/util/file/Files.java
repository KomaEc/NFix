package org.jboss.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.jboss.logging.Logger;
import org.jboss.util.stream.Streams;

public final class Files {
   private static final Logger log = Logger.getLogger(Files.class);
   private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   public static final int DEFAULT_BUFFER_SIZE = 8192;

   public static boolean delete(File dir) {
      boolean success = true;
      File[] files = dir.listFiles();
      if (files != null) {
         for(int i = 0; i < files.length; ++i) {
            File f = files[i];
            if (f.isDirectory()) {
               if (!delete(f)) {
                  success = false;
                  log.debug("Failed to delete dir: " + f.getAbsolutePath());
               }
            } else if (!f.delete()) {
               success = false;
               log.debug("Failed to delete file: " + f.getAbsolutePath());
            }
         }
      }

      if (!dir.delete()) {
         success = false;
         log.debug("Failed to delete dir: " + dir.getAbsolutePath());
      }

      return success;
   }

   public static boolean delete(String dirname) {
      return delete(new File(dirname));
   }

   public static boolean deleteContaining(String filename) {
      File file = new File(filename);
      File containingDir = file.getParentFile();
      return delete(containingDir);
   }

   public static void copy(File source, File target, byte[] buff) throws IOException {
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
      BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));

      int read;
      try {
         while((read = in.read(buff)) != -1) {
            out.write(buff, 0, read);
         }
      } finally {
         Streams.flush((OutputStream)out);
         Streams.close((InputStream)in);
         Streams.close((OutputStream)out);
      }

   }

   public static void copy(File source, File target, int size) throws IOException {
      copy(source, target, new byte[size]);
   }

   public static void copy(File source, File target) throws IOException {
      copy(source, target, 8192);
   }

   public static void copy(URL src, File dest) throws IOException {
      log.debug("Copying " + src + " -> " + dest);
      File dir = dest.getParentFile();
      if (!dir.exists() && !dir.mkdirs()) {
         throw new IOException("mkdirs failed for: " + dir.getAbsolutePath());
      } else if (dest.exists() && !delete(dest)) {
         throw new IOException("delete of previous content failed for: " + dest.getAbsolutePath());
      } else {
         InputStream in = new BufferedInputStream(src.openStream());
         OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
         Streams.copy(in, out);
         out.flush();
         out.close();
         in.close();
      }
   }

   public static String encodeFileName(String name) {
      return encodeFileName(name, '@');
   }

   public static String decodeFileName(String name) {
      return decodeFileName(name, '@');
   }

   public static String encodeFileName(String name, char escape) {
      StringBuffer rc = new StringBuffer();

      for(int i = 0; i < name.length(); ++i) {
         switch(name.charAt(i)) {
         case '-':
         case '.':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '_':
         case 'a':
         case 'b':
         case 'c':
         case 'd':
         case 'e':
         case 'f':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'n':
         case 'o':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         case 't':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
            rc.append(name.charAt(i));
            break;
         case '/':
         case ':':
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '@':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '`':
         default:
            try {
               byte[] data = ("" + name.charAt(i)).getBytes("UTF8");

               for(int j = 0; j < data.length; ++j) {
                  rc.append(escape);
                  rc.append(hexDigits[data[j] >> 4 & 15]);
                  rc.append(hexDigits[data[j] & 15]);
               }
            } catch (UnsupportedEncodingException var6) {
            }
         }
      }

      return rc.toString();
   }

   public static String decodeFileName(String name, char escape) {
      if (name == null) {
         return null;
      } else {
         StringBuffer sbuf = new StringBuffer(name.length());

         for(int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (c == escape) {
               ++i;
               char h1 = name.charAt(i);
               ++i;
               char h2 = name.charAt(i);
               int d1 = h1 >= 'a' ? 10 + h1 - 97 : (h1 >= 'A' ? 10 + h1 - 65 : h1 - 48);
               int d2 = h2 >= 'a' ? 10 + h2 - 97 : (h2 >= 'A' ? 10 + h2 - 65 : h2 - 48);
               byte[] bytes = new byte[]{(byte)(d1 * 16 + d2)};

               try {
                  String s = new String(bytes, "UTF8");
                  sbuf.append(s);
               } catch (UnsupportedEncodingException var11) {
               }
            } else {
               sbuf.append(c);
            }
         }

         return sbuf.toString();
      }
   }

   public static String findRelativePath(String base, String path) throws IOException {
      String a = (new File(base)).getCanonicalFile().toURI().getPath();
      String b = (new File(path)).getCanonicalFile().toURI().getPath();
      String[] basePaths = a.split("/");
      String[] otherPaths = b.split("/");

      int n;
      for(n = 0; n < basePaths.length && n < otherPaths.length && basePaths[n].equals(otherPaths[n]); ++n) {
      }

      System.out.println("Common length: " + n);
      StringBuffer tmp = new StringBuffer("../");

      int m;
      for(m = n; m < basePaths.length - 1; ++m) {
         tmp.append("../");
      }

      for(m = n; m < otherPaths.length; ++m) {
         tmp.append(otherPaths[m]);
         tmp.append("/");
      }

      return tmp.toString();
   }
}
