package org.jboss.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public final class JarUtils {
   private JarUtils() {
   }

   public static void jar(OutputStream out, File src) throws IOException {
      jar(out, new File[]{src}, (FileFilter)null, (String)null, (Manifest)null);
   }

   public static void jar(OutputStream out, File[] src) throws IOException {
      jar(out, src, (FileFilter)null, (String)null, (Manifest)null);
   }

   public static void jar(OutputStream out, File[] src, FileFilter filter) throws IOException {
      jar(out, src, filter, (String)null, (Manifest)null);
   }

   public static void jar(OutputStream out, File[] src, FileFilter filter, String prefix, Manifest man) throws IOException {
      for(int i = 0; i < src.length; ++i) {
         if (!src[i].exists()) {
            throw new FileNotFoundException(src.toString());
         }
      }

      JarOutputStream jout;
      if (man == null) {
         jout = new JarOutputStream(out);
      } else {
         jout = new JarOutputStream(out, man);
      }

      if (prefix != null && prefix.length() > 0 && !prefix.equals("/")) {
         if (prefix.charAt(0) == '/') {
            prefix = prefix.substring(1);
         }

         if (prefix.charAt(prefix.length() - 1) != '/') {
            prefix = prefix + "/";
         }
      } else {
         prefix = "";
      }

      JarUtils.JarInfo info = new JarUtils.JarInfo(jout, filter);

      for(int i = 0; i < src.length; ++i) {
         jar(src[i], prefix, info);
      }

      jout.close();
   }

   private static void jar(File src, String prefix, JarUtils.JarInfo info) throws IOException {
      JarOutputStream jout = info.out;
      if (src.isDirectory()) {
         prefix = prefix + src.getName() + "/";
         ZipEntry entry = new ZipEntry(prefix);
         entry.setTime(src.lastModified());
         entry.setMethod(0);
         entry.setSize(0L);
         entry.setCrc(0L);
         jout.putNextEntry(entry);
         jout.closeEntry();
         File[] files = src.listFiles(info.filter);

         for(int i = 0; i < files.length; ++i) {
            jar(files[i], prefix, info);
         }
      } else if (src.isFile()) {
         byte[] buffer = info.buffer;
         ZipEntry entry = new ZipEntry(prefix + src.getName());
         entry.setTime(src.lastModified());
         jout.putNextEntry(entry);
         FileInputStream in = new FileInputStream(src);

         int len;
         while((len = in.read(buffer, 0, buffer.length)) != -1) {
            jout.write(buffer, 0, len);
         }

         in.close();
         jout.closeEntry();
      }

   }

   public static void unjar(InputStream in, File dest) throws IOException {
      if (!dest.exists()) {
         dest.mkdirs();
      }

      if (!dest.isDirectory()) {
         throw new IOException("Destination must be a directory.");
      } else {
         JarInputStream jin = new JarInputStream(in);
         byte[] buffer = new byte[1024];
         String canonicalDocBasePrefix = dest.getCanonicalPath();
         if (!canonicalDocBasePrefix.endsWith(File.separator)) {
            canonicalDocBasePrefix = canonicalDocBasePrefix + File.separator;
         }

         File file;
         File parent;
         FileOutputStream out;
         for(ZipEntry entry = jin.getNextEntry(); entry != null; entry = jin.getNextEntry()) {
            String fileName = entry.getName();
            if (fileName.charAt(fileName.length() - 1) == '/') {
               fileName = fileName.substring(0, fileName.length() - 1);
            }

            if (fileName.charAt(0) == '/') {
               fileName = fileName.substring(1);
            }

            if (File.separatorChar != '/') {
               fileName = fileName.replace('/', File.separatorChar);
            }

            file = new File(dest, fileName);
            if (!file.getCanonicalPath().startsWith(canonicalDocBasePrefix)) {
               throw new IOException("Illegal path: " + fileName);
            }

            if (entry.isDirectory()) {
               file.mkdirs();
               jin.closeEntry();
            } else {
               parent = file.getParentFile();
               if (parent != null && !parent.exists()) {
                  parent.mkdirs();
               }

               out = new FileOutputStream(file);
               boolean var10 = false;

               int len;
               while((len = jin.read(buffer, 0, buffer.length)) != -1) {
                  out.write(buffer, 0, len);
               }

               out.flush();
               out.close();
               jin.closeEntry();
               file.setLastModified(entry.getTime());
            }
         }

         Manifest mf = jin.getManifest();
         if (mf != null) {
            file = new File(dest, "META-INF/MANIFEST.MF");
            parent = file.getParentFile();
            if (!parent.exists()) {
               parent.mkdirs();
            }

            out = new FileOutputStream(file);
            mf.write(out);
            out.flush();
            out.close();
         }

         jin.close();
      }
   }

   public static URL extractNestedJar(URL jarURL, File dest) throws IOException {
      if (!jarURL.getProtocol().equals("jar")) {
         return jarURL;
      } else {
         String destPath = dest.getAbsolutePath();
         URLConnection urlConn = jarURL.openConnection();
         JarURLConnection jarConn = (JarURLConnection)urlConn;
         String parentArchiveName = jarConn.getJarFile().getName();
         int length = Math.min(destPath.length(), parentArchiveName.length());

         int n;
         for(n = 0; n < length; ++n) {
            char a = destPath.charAt(n);
            char b = parentArchiveName.charAt(n);
            if (a != b) {
               break;
            }
         }

         parentArchiveName = parentArchiveName.substring(n);
         File archiveDir = new File(dest, parentArchiveName + "-contents");
         if (!archiveDir.exists() && !archiveDir.mkdirs()) {
            throw new IOException("Failed to create contents directory for archive, path=" + archiveDir.getAbsolutePath());
         } else {
            String archiveName = jarConn.getEntryName();
            File archiveFile = new File(archiveDir, archiveName);
            File archiveParentDir = archiveFile.getParentFile();
            if (!archiveParentDir.exists() && !archiveParentDir.mkdirs()) {
               throw new IOException("Failed to create parent directory for archive, path=" + archiveParentDir.getAbsolutePath());
            } else {
               InputStream archiveIS = jarConn.getInputStream();
               FileOutputStream fos = new FileOutputStream(archiveFile);
               BufferedOutputStream bos = new BufferedOutputStream(fos);
               byte[] buffer = new byte[4096];

               int read;
               while((read = archiveIS.read(buffer)) > 0) {
                  bos.write(buffer, 0, read);
               }

               archiveIS.close();
               bos.close();
               return archiveFile.toURL();
            }
         }
      }
   }

   public static void main(String[] args) throws Exception {
      if (args.length == 0) {
         System.out.println("usage: <x or c> <jar-archive> <files...>");
         System.exit(0);
      }

      if (args[0].equals("x")) {
         BufferedInputStream in = new BufferedInputStream(new FileInputStream(args[1]));
         File dest = new File(args[2]);
         unjar(in, dest);
      } else if (args[0].equals("c")) {
         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(args[1]));
         File[] src = new File[args.length - 2];

         for(int i = 0; i < src.length; ++i) {
            src[i] = new File(args[2 + i]);
         }

         jar(out, (File[])src);
      } else {
         System.out.println("Need x or c as first argument");
      }

   }

   private static class JarInfo {
      public JarOutputStream out;
      public FileFilter filter;
      public byte[] buffer;

      public JarInfo(JarOutputStream out, FileFilter filter) {
         this.out = out;
         this.filter = filter;
         this.buffer = new byte[1024];
      }
   }
}
