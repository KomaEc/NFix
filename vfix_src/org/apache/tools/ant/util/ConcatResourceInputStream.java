package org.apache.tools.ant.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

public class ConcatResourceInputStream extends InputStream {
   private static final int EOF = -1;
   private boolean eof = false;
   private Iterator iter;
   private InputStream currentStream;
   private ProjectComponent managingPc;
   private boolean ignoreErrors = false;

   public ConcatResourceInputStream(ResourceCollection rc) {
      this.iter = rc.iterator();
   }

   public void setIgnoreErrors(boolean b) {
      this.ignoreErrors = b;
   }

   public boolean isIgnoreErrors() {
      return this.ignoreErrors;
   }

   public void close() throws IOException {
      this.closeCurrent();
      this.eof = true;
   }

   public int read() throws IOException {
      if (this.eof) {
         return -1;
      } else {
         int result = this.readCurrent();
         if (result == -1) {
            this.nextResource();
            result = this.readCurrent();
         }

         return result;
      }
   }

   public void setManagingComponent(ProjectComponent pc) {
      this.managingPc = pc;
   }

   public void log(String message, int loglevel) {
      if (this.managingPc != null) {
         this.managingPc.log(message, loglevel);
      } else {
         (loglevel > 1 ? System.out : System.err).println(message);
      }

   }

   private int readCurrent() throws IOException {
      return !this.eof && this.currentStream != null ? this.currentStream.read() : -1;
   }

   private void nextResource() throws IOException {
      this.closeCurrent();

      while(true) {
         Resource r;
         do {
            if (!this.iter.hasNext()) {
               this.eof = true;
               return;
            }

            r = (Resource)this.iter.next();
         } while(!r.isExists());

         this.log("Concating " + r.toLongString(), 3);

         try {
            this.currentStream = new BufferedInputStream(r.getInputStream());
            return;
         } catch (IOException var3) {
            if (!this.ignoreErrors) {
               this.log("Failed to get input stream for " + r, 0);
               throw var3;
            }
         }
      }
   }

   private void closeCurrent() {
      FileUtils.close(this.currentStream);
      this.currentStream = null;
   }
}
