package soot.JastAddJ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFilePart extends PathPart {
   private Set<String> set = new HashSet();
   private File file;

   public boolean hasPackage(String name) {
      return this.set.contains(name);
   }

   public ZipFilePart(File file) throws IOException {
      this.file = file;
      ZipFile zipFile = null;

      try {
         zipFile = new ZipFile(file);

         ZipEntry entry;
         for(Enumeration e = zipFile.entries(); e.hasMoreElements(); this.set.add(entry.getName())) {
            entry = (ZipEntry)e.nextElement();
            String pathName = (new File(entry.getName())).getParent();
            if (pathName != null) {
               pathName = pathName.replace(File.separatorChar, '.');
            }

            if (!this.set.contains(pathName)) {
               int pos = 0;

               while(pathName != null && -1 != (pos = pathName.indexOf(46, pos + 1))) {
                  String n = pathName.substring(0, pos);
                  if (!this.set.contains(n)) {
                     this.set.add(n);
                  }
               }

               this.set.add(pathName);
            }
         }
      } finally {
         if (zipFile != null) {
            zipFile.close();
         }

      }

   }

   public boolean selectCompilationUnit(String canonicalName) throws IOException {
      ZipFile zipFile = null;
      boolean success = false;

      try {
         zipFile = new ZipFile(this.file);
         String name = canonicalName.replace('.', '/');
         name = name + this.fileSuffix();
         if (this.set.contains(name)) {
            ZipEntry zipEntry = zipFile.getEntry(name);
            if (zipEntry != null && !zipEntry.isDirectory()) {
               this.is = new ZipFilePart.ZipEntryInputStreamWrapper(zipFile, zipEntry);
               this.age = zipEntry.getTime();
               this.pathName = zipFile.getName();
               this.relativeName = name + this.fileSuffix();
               this.fullName = canonicalName;
               success = true;
            }
         }
      } finally {
         if (zipFile != null && !success) {
            zipFile.close();
         }

      }

      return success;
   }

   public static class ZipEntryInputStreamWrapper extends InputStream {
      private ZipFile zipFile;
      private InputStream entryInputStream;

      public ZipEntryInputStreamWrapper(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
         this.zipFile = zipFile;
         this.entryInputStream = zipFile.getInputStream(zipEntry);
      }

      public int read() throws IOException {
         return this.entryInputStream.read();
      }

      public int read(byte[] b) throws IOException {
         return this.entryInputStream.read(b);
      }

      public int read(byte[] b, int off, int len) throws IOException {
         return this.entryInputStream.read(b, off, len);
      }

      public long skip(long n) throws IOException {
         return this.entryInputStream.skip(n);
      }

      public int available() throws IOException {
         return this.entryInputStream.available();
      }

      public void close() throws IOException {
         try {
            this.entryInputStream.close();
         } finally {
            if (this.zipFile != null) {
               this.zipFile.close();
               this.zipFile = null;
            }

         }

      }

      public synchronized void mark(int readlimit) {
         this.entryInputStream.mark(readlimit);
      }

      public synchronized void reset() throws IOException {
         this.entryInputStream.reset();
      }

      public boolean markSupported() {
         return this.entryInputStream.markSupported();
      }
   }
}
