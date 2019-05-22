package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FileResourceIterator implements Iterator {
   private File basedir;
   private String[] files;
   private int pos;

   public FileResourceIterator() {
      this.pos = 0;
   }

   public FileResourceIterator(File f) {
      this.pos = 0;
      this.basedir = f;
   }

   public FileResourceIterator(File f, String[] s) {
      this(f);
      this.addFiles(s);
   }

   public void addFiles(String[] s) {
      int start = this.files == null ? 0 : this.files.length;
      String[] newfiles = new String[start + s.length];
      if (start > 0) {
         System.arraycopy(this.files, 0, newfiles, 0, start);
      }

      this.files = newfiles;
      System.arraycopy(s, 0, this.files, start, s.length);
   }

   public boolean hasNext() {
      return this.pos < this.files.length;
   }

   public Object next() {
      return this.nextResource();
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public FileResource nextResource() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      } else {
         return new FileResource(this.basedir, this.files[this.pos++]);
      }
   }
}
