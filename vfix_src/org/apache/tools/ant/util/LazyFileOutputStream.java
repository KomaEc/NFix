package org.apache.tools.ant.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LazyFileOutputStream extends OutputStream {
   private FileOutputStream fos;
   private File file;
   private boolean append;
   private boolean alwaysCreate;
   private boolean opened;
   private boolean closed;

   public LazyFileOutputStream(String name) {
      this(name, false);
   }

   public LazyFileOutputStream(String name, boolean append) {
      this(new File(name), append);
   }

   public LazyFileOutputStream(File f) {
      this(f, false);
   }

   public LazyFileOutputStream(File file, boolean append) {
      this(file, append, false);
   }

   public LazyFileOutputStream(File file, boolean append, boolean alwaysCreate) {
      this.opened = false;
      this.closed = false;
      this.file = file;
      this.append = append;
      this.alwaysCreate = alwaysCreate;
   }

   public void open() throws IOException {
      this.ensureOpened();
   }

   public synchronized void close() throws IOException {
      if (this.alwaysCreate && !this.closed) {
         this.ensureOpened();
      }

      if (this.opened) {
         this.fos.close();
      }

      this.closed = true;
   }

   public void write(byte[] b) throws IOException {
      this.write(b, 0, b.length);
   }

   public synchronized void write(byte[] b, int offset, int len) throws IOException {
      this.ensureOpened();
      this.fos.write(b, offset, len);
   }

   public synchronized void write(int b) throws IOException {
      this.ensureOpened();
      this.fos.write(b);
   }

   private synchronized void ensureOpened() throws IOException {
      if (this.closed) {
         throw new IOException(this.file + " has already been closed.");
      } else {
         if (!this.opened) {
            this.fos = new FileOutputStream(this.file.getAbsolutePath(), this.append);
            this.opened = true;
         }

      }
   }
}
