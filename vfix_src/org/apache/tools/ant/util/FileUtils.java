package org.apache.tools.ant.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PathTokenizer;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.resources.FileResource;

public class FileUtils {
   private static final FileUtils PRIMARY_INSTANCE = new FileUtils();
   private static Random rand = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
   private static boolean onNetWare = Os.isFamily("netware");
   private static boolean onDos = Os.isFamily("dos");
   private static boolean onWin9x = Os.isFamily("win9x");
   private static boolean onWindows = Os.isFamily("windows");
   static final int BUF_SIZE = 8192;
   public static final long FAT_FILE_TIMESTAMP_GRANULARITY = 2000L;
   public static final long UNIX_FILE_TIMESTAMP_GRANULARITY = 1000L;
   public static final long NTFS_FILE_TIMESTAMP_GRANULARITY = 1L;
   private Object cacheFromUriLock = new Object();
   private String cacheFromUriRequest = null;
   private String cacheFromUriResponse = null;
   // $FF: synthetic field
   static Class class$java$io$File;

   /** @deprecated */
   public static FileUtils newFileUtils() {
      return new FileUtils();
   }

   public static FileUtils getFileUtils() {
      return PRIMARY_INSTANCE;
   }

   protected FileUtils() {
   }

   public URL getFileURL(File file) throws MalformedURLException {
      return new URL(this.toURI(file.getAbsolutePath()));
   }

   public void copyFile(String sourceFile, String destFile) throws IOException {
      this.copyFile((File)(new File(sourceFile)), (File)(new File(destFile)), (FilterSetCollection)null, false, false);
   }

   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters) throws IOException {
      this.copyFile(new File(sourceFile), new File(destFile), filters, false, false);
   }

   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite) throws IOException {
      this.copyFile(new File(sourceFile), new File(destFile), filters, overwrite, false);
   }

   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified) throws IOException {
      this.copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified);
   }

   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified, String encoding) throws IOException {
      this.copyFile(new File(sourceFile), new File(destFile), filters, overwrite, preserveLastModified, encoding);
   }

   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, Vector filterChains, boolean overwrite, boolean preserveLastModified, String encoding, Project project) throws IOException {
      this.copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, encoding, project);
   }

   public void copyFile(String sourceFile, String destFile, FilterSetCollection filters, Vector filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
      this.copyFile(new File(sourceFile), new File(destFile), filters, filterChains, overwrite, preserveLastModified, inputEncoding, outputEncoding, project);
   }

   public void copyFile(File sourceFile, File destFile) throws IOException {
      this.copyFile((File)sourceFile, (File)destFile, (FilterSetCollection)null, false, false);
   }

   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters) throws IOException {
      this.copyFile(sourceFile, destFile, filters, false, false);
   }

   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite) throws IOException {
      this.copyFile(sourceFile, destFile, filters, overwrite, false);
   }

   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified) throws IOException {
      this.copyFile((File)sourceFile, (File)destFile, filters, overwrite, preserveLastModified, (String)null);
   }

   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, boolean overwrite, boolean preserveLastModified, String encoding) throws IOException {
      this.copyFile((File)sourceFile, (File)destFile, filters, (Vector)null, overwrite, preserveLastModified, encoding, (Project)null);
   }

   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector filterChains, boolean overwrite, boolean preserveLastModified, String encoding, Project project) throws IOException {
      this.copyFile(sourceFile, destFile, filters, filterChains, overwrite, preserveLastModified, encoding, encoding, project);
   }

   public void copyFile(File sourceFile, File destFile, FilterSetCollection filters, Vector filterChains, boolean overwrite, boolean preserveLastModified, String inputEncoding, String outputEncoding, Project project) throws IOException {
      ResourceUtils.copyResource(new FileResource(sourceFile), new FileResource(destFile), filters, filterChains, overwrite, preserveLastModified, inputEncoding, outputEncoding, project);
   }

   public void setFileLastModified(File file, long time) {
      ResourceUtils.setLastModified(new FileResource(file), time);
   }

   public File resolveFile(File file, String filename) {
      if (!isAbsolutePath(filename)) {
         char sep = File.separatorChar;
         filename = filename.replace('/', sep).replace('\\', sep);
         if (isContextRelativePath(filename)) {
            file = null;
            String udir = System.getProperty("user.dir");
            if (filename.charAt(0) == sep && udir.charAt(0) == sep) {
               filename = this.dissect(udir)[0] + filename.substring(1);
            }
         }

         filename = (new File(file, filename)).getAbsolutePath();
      }

      return this.normalize(filename);
   }

   public static boolean isContextRelativePath(String filename) {
      if ((onDos || onNetWare) && filename.length() != 0) {
         char sep = File.separatorChar;
         filename = filename.replace('/', sep).replace('\\', sep);
         char c = filename.charAt(0);
         int len = filename.length();
         return c == sep && (len == 1 || filename.charAt(1) != sep) || Character.isLetter(c) && len > 1 && filename.indexOf(58) == 1 && (len == 2 || filename.charAt(2) != sep);
      } else {
         return false;
      }
   }

   public static boolean isAbsolutePath(String filename) {
      int len = filename.length();
      if (len == 0) {
         return false;
      } else {
         char sep = File.separatorChar;
         filename = filename.replace('/', sep).replace('\\', sep);
         char c = filename.charAt(0);
         if (!onDos && !onNetWare) {
            return c == sep;
         } else {
            int nextsep;
            if (c == sep) {
               if (onDos && len > 4 && filename.charAt(1) == sep) {
                  nextsep = filename.indexOf(sep, 2);
                  return nextsep > 2 && nextsep + 1 < len;
               } else {
                  return false;
               }
            } else {
               nextsep = filename.indexOf(58);
               return Character.isLetter(c) && nextsep == 1 && filename.length() > 2 && filename.charAt(2) == sep || onNetWare && nextsep > 0;
            }
         }
      }
   }

   public static String translatePath(String toProcess) {
      if (toProcess != null && toProcess.length() != 0) {
         StringBuffer path = new StringBuffer(toProcess.length() + 50);

         String pathComponent;
         for(PathTokenizer tokenizer = new PathTokenizer(toProcess); tokenizer.hasMoreTokens(); path.append(pathComponent)) {
            pathComponent = tokenizer.nextToken();
            pathComponent = pathComponent.replace('/', File.separatorChar);
            pathComponent = pathComponent.replace('\\', File.separatorChar);
            if (path.length() != 0) {
               path.append(File.pathSeparatorChar);
            }
         }

         return path.toString();
      } else {
         return "";
      }
   }

   public File normalize(String path) {
      Stack s = new Stack();
      String[] dissect = this.dissect(path);
      s.push(dissect[0]);
      java.util.StringTokenizer tok = new java.util.StringTokenizer(dissect[1], File.separator);

      while(tok.hasMoreTokens()) {
         String thisToken = tok.nextToken();
         if (!".".equals(thisToken)) {
            if ("..".equals(thisToken)) {
               if (s.size() < 2) {
                  return new File(path);
               }

               s.pop();
            } else {
               s.push(thisToken);
            }
         }
      }

      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < s.size(); ++i) {
         if (i > 1) {
            sb.append(File.separatorChar);
         }

         sb.append(s.elementAt(i));
      }

      return new File(sb.toString());
   }

   public String[] dissect(String path) {
      char sep = File.separatorChar;
      path = path.replace('/', sep).replace('\\', sep);
      if (!isAbsolutePath(path)) {
         throw new BuildException(path + " is not an absolute path");
      } else {
         String root = null;
         int colon = path.indexOf(58);
         int nextsep;
         if (colon > 0 && (onDos || onNetWare)) {
            nextsep = colon + 1;
            root = path.substring(0, nextsep);
            char[] ca = path.toCharArray();
            root = root + sep;
            nextsep = ca[nextsep] == sep ? nextsep + 1 : nextsep;
            StringBuffer sbPath = new StringBuffer();

            for(int i = nextsep; i < ca.length; ++i) {
               if (ca[i] != sep || ca[i - 1] != sep) {
                  sbPath.append(ca[i]);
               }
            }

            path = sbPath.toString();
         } else if (path.length() > 1 && path.charAt(1) == sep) {
            nextsep = path.indexOf(sep, 2);
            nextsep = path.indexOf(sep, nextsep + 1);
            root = nextsep > 2 ? path.substring(0, nextsep + 1) : path;
            path = path.substring(root.length());
         } else {
            root = File.separator;
            path = path.substring(1);
         }

         return new String[]{root, path};
      }
   }

   public String toVMSPath(File f) {
      String path = this.normalize(f.getAbsolutePath()).getPath();
      String name = f.getName();
      boolean isAbsolute = path.charAt(0) == File.separatorChar;
      boolean isDirectory = f.isDirectory() && !name.regionMatches(true, name.length() - 4, ".DIR", 0, 4);
      String device = null;
      StringBuffer directory = null;
      String file = null;
      int index = 0;
      if (isAbsolute) {
         index = path.indexOf(File.separatorChar, 1);
         if (index == -1) {
            return path.substring(1) + ":[000000]";
         }

         device = path.substring(1, index++);
      }

      if (isDirectory) {
         directory = new StringBuffer(path.substring(index).replace(File.separatorChar, '.'));
      } else {
         int dirEnd = path.lastIndexOf(File.separatorChar, path.length());
         if (dirEnd != -1 && dirEnd >= index) {
            directory = new StringBuffer(path.substring(index, dirEnd).replace(File.separatorChar, '.'));
            index = dirEnd + 1;
            if (path.length() > index) {
               file = path.substring(index);
            }
         } else {
            file = path.substring(index);
         }
      }

      if (!isAbsolute && directory != null) {
         directory.insert(0, '.');
      }

      String osPath = (device != null ? device + ":" : "") + (directory != null ? "[" + directory + "]" : "") + (file != null ? file : "");
      return osPath;
   }

   public File createTempFile(String prefix, String suffix, File parentDir) {
      return this.createTempFile(prefix, suffix, parentDir, false);
   }

   public File createTempFile(String prefix, String suffix, File parentDir, boolean deleteOnExit) {
      File result = null;
      String parent = parentDir == null ? System.getProperty("java.io.tmpdir") : parentDir.getPath();
      DecimalFormat fmt = new DecimalFormat("#####");
      synchronized(rand) {
         do {
            result = new File(parent, prefix + fmt.format((long)Math.abs(rand.nextInt())) + suffix);
         } while(result.exists());
      }

      if (deleteOnExit) {
         result.deleteOnExit();
      }

      return result;
   }

   public boolean contentEquals(File f1, File f2) throws IOException {
      return this.contentEquals(f1, f2, false);
   }

   public boolean contentEquals(File f1, File f2, boolean textfile) throws IOException {
      return ResourceUtils.contentEquals(new FileResource(f1), new FileResource(f2), textfile);
   }

   /** @deprecated */
   public File getParentFile(File f) {
      return f == null ? null : f.getParentFile();
   }

   public static final String readFully(Reader rdr) throws IOException {
      return readFully(rdr, 8192);
   }

   public static final String readFully(Reader rdr, int bufferSize) throws IOException {
      if (bufferSize <= 0) {
         throw new IllegalArgumentException("Buffer size must be greater than 0");
      } else {
         char[] buffer = new char[bufferSize];
         int bufferLength = 0;
         StringBuffer textBuffer = null;

         while(bufferLength != -1) {
            bufferLength = rdr.read(buffer);
            if (bufferLength > 0) {
               textBuffer = textBuffer == null ? new StringBuffer() : textBuffer;
               textBuffer.append(new String(buffer, 0, bufferLength));
            }
         }

         return textBuffer == null ? null : textBuffer.toString();
      }
   }

   public boolean createNewFile(File f) throws IOException {
      return f.createNewFile();
   }

   public boolean createNewFile(File f, boolean mkdirs) throws IOException {
      File parent = f.getParentFile();
      if (mkdirs && !parent.exists()) {
         parent.mkdirs();
      }

      return f.createNewFile();
   }

   public boolean isSymbolicLink(File parent, String name) throws IOException {
      File toTest;
      if (parent == null) {
         toTest = new File(name);
         parent = toTest.getParentFile();
         name = toTest.getName();
      }

      toTest = new File(parent.getCanonicalPath(), name);
      return !toTest.getAbsolutePath().equals(toTest.getCanonicalPath());
   }

   public String removeLeadingPath(File leading, File path) {
      String l = this.normalize(leading.getAbsolutePath()).getAbsolutePath();
      String p = this.normalize(path.getAbsolutePath()).getAbsolutePath();
      if (l.equals(p)) {
         return "";
      } else {
         if (!l.endsWith(File.separator)) {
            l = l + File.separator;
         }

         return p.startsWith(l) ? p.substring(l.length()) : p;
      }
   }

   public boolean isLeadingPath(File leading, File path) {
      String l = this.normalize(leading.getAbsolutePath()).getAbsolutePath();
      String p = this.normalize(path.getAbsolutePath()).getAbsolutePath();
      if (l.equals(p)) {
         return true;
      } else {
         if (!l.endsWith(File.separator)) {
            l = l + File.separator;
         }

         return p.startsWith(l);
      }
   }

   public String toURI(String path) {
      Class uriClazz = null;

      try {
         uriClazz = Class.forName("java.net.URI");
      } catch (ClassNotFoundException var8) {
      }

      if (uriClazz != null) {
         try {
            File f = (new File(path)).getAbsoluteFile();
            Method toURIMethod = (class$java$io$File == null ? (class$java$io$File = class$("java.io.File")) : class$java$io$File).getMethod("toURI");
            Object uriObj = toURIMethod.invoke(f);
            Method toASCIIStringMethod = uriClazz.getMethod("toASCIIString");
            return (String)toASCIIStringMethod.invoke(uriObj);
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }

      boolean isDir = (new File(path)).isDirectory();
      StringBuffer sb = new StringBuffer("file:");
      path = this.resolveFile((File)null, path).getPath();
      sb.append("//");
      if (!path.startsWith(File.separator)) {
         sb.append("/");
      }

      path = path.replace('\\', '/');

      try {
         sb.append(Locator.encodeURI(path));
      } catch (UnsupportedEncodingException var7) {
         throw new BuildException(var7);
      }

      if (isDir && !path.endsWith("/")) {
         sb.append('/');
      }

      return sb.toString();
   }

   public String fromURI(String uri) {
      synchronized(this.cacheFromUriLock) {
         if (uri.equals(this.cacheFromUriRequest)) {
            return this.cacheFromUriResponse;
         } else {
            String path = Locator.fromURI(uri);
            String ret = isAbsolutePath(path) ? this.normalize(path).getAbsolutePath() : path;
            this.cacheFromUriRequest = uri;
            this.cacheFromUriResponse = ret;
            return ret;
         }
      }
   }

   public boolean fileNameEquals(File f1, File f2) {
      return this.normalize(f1.getAbsolutePath()).equals(this.normalize(f2.getAbsolutePath()));
   }

   public void rename(File from, File to) throws IOException {
      if (to.exists() && !to.delete()) {
         throw new IOException("Failed to delete " + to + " while trying to rename " + from);
      } else {
         File parent = to.getParentFile();
         if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent + " while trying to rename " + from);
         } else {
            if (!from.renameTo(to)) {
               this.copyFile(from, to);
               if (!from.delete()) {
                  throw new IOException("Failed to delete " + from + " while trying to rename it.");
               }
            }

         }
      }
   }

   public long getFileTimestampGranularity() {
      if (onWin9x) {
         return 2000L;
      } else if (onWindows) {
         return 1L;
      } else {
         return onDos ? 2000L : 1000L;
      }
   }

   public boolean isUpToDate(File source, File dest, long granularity) {
      if (!dest.exists()) {
         return false;
      } else {
         long sourceTime = source.lastModified();
         long destTime = dest.lastModified();
         return this.isUpToDate(sourceTime, destTime, granularity);
      }
   }

   public boolean isUpToDate(File source, File dest) {
      return this.isUpToDate(source, dest, this.getFileTimestampGranularity());
   }

   public boolean isUpToDate(long sourceTime, long destTime, long granularity) {
      if (destTime == -1L) {
         return false;
      } else {
         return destTime >= sourceTime + granularity;
      }
   }

   public boolean isUpToDate(long sourceTime, long destTime) {
      return this.isUpToDate(sourceTime, destTime, this.getFileTimestampGranularity());
   }

   public static void close(Writer device) {
      if (device != null) {
         try {
            device.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void close(Reader device) {
      if (device != null) {
         try {
            device.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void close(OutputStream device) {
      if (device != null) {
         try {
            device.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void close(InputStream device) {
      if (device != null) {
         try {
            device.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void delete(File file) {
      if (file != null) {
         file.delete();
      }

   }

   public static String getRelativePath(File fromFile, File toFile) throws Exception {
      String fromPath = fromFile.getCanonicalPath();
      String toPath = toFile.getCanonicalPath();
      String[] fromPathStack = getPathStack(fromPath);
      String[] toPathStack = getPathStack(toPath);
      if (0 < toPathStack.length && 0 < fromPathStack.length) {
         if (!fromPathStack[0].equals(toPathStack[0])) {
            return getPath(Arrays.asList(toPathStack));
         } else {
            int minLength = Math.min(fromPathStack.length, toPathStack.length);

            int same;
            for(same = 1; same < minLength && fromPathStack[same].equals(toPathStack[same]); ++same) {
            }

            List relativePathStack = new ArrayList();

            int i;
            for(i = same; i < fromPathStack.length; ++i) {
               relativePathStack.add("..");
            }

            for(i = same; i < toPathStack.length; ++i) {
               relativePathStack.add(toPathStack[i]);
            }

            return getPath(relativePathStack);
         }
      } else {
         return getPath(Arrays.asList(toPathStack));
      }
   }

   public static String[] getPathStack(String path) {
      String normalizedPath = path.replace(File.separatorChar, '/');
      Object[] tokens = StringUtils.split(normalizedPath, 47).toArray();
      String[] rv = new String[tokens.length];
      System.arraycopy(tokens, 0, rv, 0, tokens.length);
      return rv;
   }

   public static String getPath(List pathStack) {
      return getPath(pathStack, '/');
   }

   public static String getPath(List pathStack, char separatorChar) {
      StringBuffer buffer = new StringBuffer();
      Iterator iter = pathStack.iterator();
      if (iter.hasNext()) {
         buffer.append(iter.next());
      }

      while(iter.hasNext()) {
         buffer.append(separatorChar);
         buffer.append(iter.next());
      }

      return buffer.toString();
   }

   public String getDefaultEncoding() {
      InputStreamReader is = new InputStreamReader(new InputStream() {
         public int read() {
            return -1;
         }
      });

      String var2;
      try {
         var2 = is.getEncoding();
      } finally {
         close((Reader)is);
      }

      return var2;
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
