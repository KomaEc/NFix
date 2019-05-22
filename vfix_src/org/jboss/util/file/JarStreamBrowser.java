package org.jboss.util.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarStreamBrowser implements Iterator {
   JarInputStream jar;
   JarEntry next;
   ArchiveBrowser.Filter filter;

   public JarStreamBrowser(File file, ArchiveBrowser.Filter filter) throws IOException {
      this((InputStream)(new FileInputStream(file)), filter);
   }

   public JarStreamBrowser(InputStream is, ArchiveBrowser.Filter filter) throws IOException {
      this.filter = filter;
      this.jar = new JarInputStream(is);
      this.setNext();
   }

   public boolean hasNext() {
      return this.next != null;
   }

   private void setNext() {
      try {
         if (this.next != null) {
            this.jar.closeEntry();
         }

         this.next = null;

         do {
            this.next = this.jar.getNextJarEntry();
         } while(this.next != null && (this.next.isDirectory() || !this.filter.accept(this.next.getName())));

         if (this.next == null) {
            this.jar.close();
         }

      } catch (IOException var2) {
         throw new RuntimeException("failed to browse jar", var2);
      }
   }

   public Object next() {
      int size = (int)this.next.getSize();
      byte[] buf = new byte[size];
      int count = 0;
      boolean var4 = false;

      try {
         int current;
         while((current = this.jar.read(buf, count, size - count)) != -1 && count < size) {
            count += current;
         }

         ByteArrayInputStream bais = new ByteArrayInputStream(buf);
         this.setNext();
         return bais;
      } catch (IOException var8) {
         try {
            this.jar.close();
         } catch (IOException var7) {
         }

         throw new RuntimeException(var8);
      }
   }

   public void remove() {
      throw new RuntimeException("Illegal operation on ArchiveBrowser");
   }
}
