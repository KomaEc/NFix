package com.gzoltar.shaded.org.jacoco.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipMultiReportOutput implements IMultiReportOutput {
   private final ZipOutputStream zip;
   private OutputStream currentEntry;

   public ZipMultiReportOutput(ZipOutputStream zip) {
      this.zip = zip;
   }

   public ZipMultiReportOutput(OutputStream out) {
      this(new ZipOutputStream(out));
   }

   public OutputStream createFile(String path) throws IOException {
      if (this.currentEntry != null) {
         this.currentEntry.close();
      }

      ZipEntry entry = new ZipEntry(path);
      this.zip.putNextEntry(entry);
      this.currentEntry = new ZipMultiReportOutput.EntryOutput();
      return this.currentEntry;
   }

   public void close() throws IOException {
      this.zip.close();
   }

   private final class EntryOutput extends OutputStream {
      private boolean closed;

      private EntryOutput() {
         this.closed = false;
      }

      public void write(byte[] b, int off, int len) throws IOException {
         this.ensureNotClosed();
         ZipMultiReportOutput.this.zip.write(b, off, len);
      }

      public void write(byte[] b) throws IOException {
         this.ensureNotClosed();
         ZipMultiReportOutput.this.zip.write(b);
      }

      public void write(int b) throws IOException {
         this.ensureNotClosed();
         ZipMultiReportOutput.this.zip.write(b);
      }

      public void flush() throws IOException {
         this.ensureNotClosed();
         ZipMultiReportOutput.this.zip.flush();
      }

      public void close() throws IOException {
         if (!this.closed) {
            this.closed = true;
            ZipMultiReportOutput.this.zip.closeEntry();
         }

      }

      private void ensureNotClosed() throws IOException {
         if (this.closed) {
            throw new IOException("Zip entry already closed.");
         }
      }

      // $FF: synthetic method
      EntryOutput(Object x1) {
         this();
      }
   }
}
