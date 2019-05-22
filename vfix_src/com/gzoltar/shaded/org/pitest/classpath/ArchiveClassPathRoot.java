package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.util.StreamUtil;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ArchiveClassPathRoot implements ClassPathRoot {
   private final File file;

   public ArchiveClassPathRoot(File file) {
      this.file = file;
   }

   public InputStream getData(String name) throws IOException {
      ZipFile zip = this.getRoot();

      InputStream var4;
      try {
         ZipEntry entry = zip.getEntry(name.replace('.', '/') + ".class");
         if (entry == null) {
            var4 = null;
            return var4;
         }

         var4 = StreamUtil.copyStream(zip.getInputStream(entry));
      } finally {
         zip.close();
      }

      return var4;
   }

   public URL getResource(String name) throws MalformedURLException {
      ZipFile zip = this.getRoot();

      URL var4;
      try {
         ZipEntry entry = zip.getEntry(name);
         if (entry == null) {
            var4 = null;
            return var4;
         }

         var4 = new URL("jar:file:" + zip.getName() + "!/" + entry.getName());
      } finally {
         this.closeQuietly(zip);
      }

      return var4;
   }

   private void closeQuietly(ZipFile zip) {
      try {
         zip.close();
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public String toString() {
      return "ArchiveClassPathRoot [file=" + this.file.getName() + "]";
   }

   public Collection<String> classNames() {
      List<String> names = new ArrayList();
      ZipFile root = this.getRoot();

      ArrayList var8;
      try {
         Enumeration entries = root.entries();

         while(entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry)entries.nextElement();
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
               names.add(this.stringToClassName(entry.getName()));
            }
         }

         var8 = names;
      } finally {
         this.closeQuietly(root);
      }

      return var8;
   }

   private String stringToClassName(String name) {
      return name.substring(0, name.length() - ".class".length()).replace('/', '.');
   }

   public Option<String> cacheLocation() {
      return Option.some(this.file.getAbsolutePath());
   }

   private ZipFile getRoot() {
      try {
         return new ZipFile(this.file);
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2.getMessage() + " (" + this.file + ")", var2);
      }
   }
}
