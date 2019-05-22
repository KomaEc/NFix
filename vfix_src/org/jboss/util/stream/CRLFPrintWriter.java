package org.jboss.util.stream;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class CRLFPrintWriter extends PrintWriter {
   protected boolean autoFlush = false;

   public CRLFPrintWriter(Writer out) {
      super(out);
   }

   public CRLFPrintWriter(Writer out, boolean autoFlush) {
      super(out, autoFlush);
      this.autoFlush = autoFlush;
   }

   public CRLFPrintWriter(OutputStream out) {
      super(out);
   }

   public CRLFPrintWriter(OutputStream out, boolean autoFlush) {
      super(out, autoFlush);
      this.autoFlush = autoFlush;
   }

   protected void ensureOpen() throws IOException {
      if (this.out == null) {
         throw new IOException("Stream closed");
      }
   }

   public void println() {
      try {
         synchronized(this.lock) {
            this.ensureOpen();
            this.out.write("\r\n");
            if (this.autoFlush) {
               this.out.flush();
            }
         }
      } catch (InterruptedIOException var4) {
         Thread.currentThread().interrupt();
      } catch (IOException var5) {
         this.setError();
      }

   }
}
