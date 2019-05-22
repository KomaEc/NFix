package org.jboss.util.file;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarArchiveBrowser implements Iterator {
   JarFile zip;
   Enumeration entries;
   JarEntry next;
   ArchiveBrowser.Filter filter;

   public JarArchiveBrowser(JarURLConnection url, ArchiveBrowser.Filter filter) {
      this.filter = filter;

      try {
         this.zip = url.getJarFile();
         this.entries = this.zip.entries();
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }

      this.setNext();
   }

   public JarArchiveBrowser(File f, ArchiveBrowser.Filter filter) {
      this.filter = filter;

      try {
         this.zip = new JarFile(f);
         this.entries = this.zip.entries();
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }

      this.setNext();
   }

   public boolean hasNext() {
      return this.next != null;
   }

   private void setNext() {
      this.next = null;

      while(this.entries.hasMoreElements() && this.next == null) {
         do {
            this.next = (JarEntry)this.entries.nextElement();
         } while(this.entries.hasMoreElements() && this.next.isDirectory());

         if (this.next.isDirectory()) {
            this.next = null;
         }

         if (this.next != null && !this.filter.accept(this.next.getName())) {
            this.next = null;
         }
      }

   }

   public Object next() {
      ZipEntry entry = this.next;
      this.setNext();

      try {
         return this.zip.getInputStream(entry);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public void remove() {
      throw new RuntimeException("Illegal operation on ArchiveBrowser");
   }
}
