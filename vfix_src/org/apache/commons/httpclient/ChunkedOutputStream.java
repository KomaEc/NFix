package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChunkedOutputStream extends OutputStream {
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final byte[] ENDCHUNK;
   private static final byte[] ZERO;
   private static final byte[] ONE;
   private static final Log LOG;
   private boolean closed = false;
   private OutputStream stream = null;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$ChunkedOutputStream;

   public ChunkedOutputStream(OutputStream stream) {
      if (stream == null) {
         throw new NullPointerException("stream parameter is null");
      } else {
         this.stream = stream;
      }
   }

   public void print(String s) throws IOException {
      LOG.trace("enter ChunckedOutputStream.print(String)");
      if (s == null) {
         s = "null";
      }

      this.write(HttpConstants.getBytes(s));
   }

   public void println() throws IOException {
      this.print("\r\n");
   }

   public void println(String s) throws IOException {
      this.print(s);
      this.println();
   }

   public void write(int b) throws IOException, IllegalStateException {
      if (this.closed) {
         throw new IllegalStateException("Output stream already closed");
      } else {
         this.stream.write(ONE, 0, ONE.length);
         this.stream.write(CRLF, 0, CRLF.length);
         this.stream.write(b);
         this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
         LOG.debug("Writing chunk (length: 1)");
      }
   }

   public void write(byte[] b, int off, int len) throws IOException {
      LOG.trace("enter ChunckedOutputStream.write(byte[], int, int)");
      if (this.closed) {
         throw new IllegalStateException("Output stream already closed");
      } else {
         byte[] chunkHeader = HttpConstants.getBytes(Integer.toHexString(len) + "\r\n");
         this.stream.write(chunkHeader, 0, chunkHeader.length);
         this.stream.write(b, off, len);
         this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
         if (LOG.isDebugEnabled()) {
            LOG.debug("Writing chunk (length: " + len + ")");
         }

      }
   }

   public void writeClosingChunk() throws IOException {
      LOG.trace("enter ChunkedOutputStream.writeClosingChunk()");
      if (!this.closed) {
         try {
            this.stream.write(ZERO, 0, ZERO.length);
            this.stream.write(CRLF, 0, CRLF.length);
            this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
            LOG.debug("Writing closing chunk");
         } catch (IOException var6) {
            LOG.debug("Unexpected exception caught when closing output stream", var6);
            throw var6;
         } finally {
            this.closed = true;
         }
      }

   }

   public void flush() throws IOException {
      this.stream.flush();
   }

   public void close() throws IOException {
      LOG.trace("enter ChunkedOutputStream.close()");
      this.writeClosingChunk();
      super.close();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      ENDCHUNK = CRLF;
      ZERO = new byte[]{48};
      ONE = new byte[]{49};
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$ChunkedOutputStream == null ? (class$org$apache$commons$httpclient$ChunkedOutputStream = class$("org.apache.commons.httpclient.ChunkedOutputStream")) : class$org$apache$commons$httpclient$ChunkedOutputStream);
   }
}
