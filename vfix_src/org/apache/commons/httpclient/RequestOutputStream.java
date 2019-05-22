package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @deprecated */
public class RequestOutputStream extends OutputStream {
   private static final Log LOG;
   private boolean closed;
   private OutputStream stream;
   private boolean useChunking;
   private static final byte[] CRLF;
   private static final byte[] ENDCHUNK;
   private static final byte[] ZERO;
   private static final byte[] ONE;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$RequestOutputStream;

   /** @deprecated */
   public RequestOutputStream(OutputStream stream) {
      this(stream, false);
   }

   /** @deprecated */
   public RequestOutputStream(OutputStream stream, boolean useChunking) {
      this.closed = false;
      this.stream = null;
      this.useChunking = false;
      if (stream == null) {
         throw new NullPointerException("stream parameter is null");
      } else {
         this.stream = stream;
         this.useChunking = useChunking;
      }
   }

   /** @deprecated */
   public void setUseChunking(boolean useChunking) {
      this.useChunking = useChunking;
   }

   /** @deprecated */
   public boolean isUseChunking() {
      return this.useChunking;
   }

   /** @deprecated */
   public void print(String s) throws IOException {
      LOG.trace("enter RequestOutputStream.print(String)");
      if (s == null) {
         s = "null";
      }

      int len = s.length();

      for(int i = 0; i < len; ++i) {
         this.write(s.charAt(i));
      }

   }

   /** @deprecated */
   public void println() throws IOException {
      this.print("\r\n");
   }

   /** @deprecated */
   public void println(String s) throws IOException {
      this.print(s);
      this.println();
   }

   /** @deprecated */
   public void write(int b) throws IOException {
      if (this.useChunking) {
         this.stream.write(ONE, 0, ONE.length);
         this.stream.write(CRLF, 0, CRLF.length);
         this.stream.write(b);
         this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
      } else {
         this.stream.write(b);
      }

   }

   /** @deprecated */
   public void write(byte[] b, int off, int len) throws IOException {
      LOG.trace("enter RequestOutputStream.write(byte[], int, int)");
      if (this.useChunking) {
         byte[] chunkHeader = HttpConstants.getBytes(Integer.toHexString(len) + "\r\n");
         this.stream.write(chunkHeader, 0, chunkHeader.length);
         this.stream.write(b, off, len);
         this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
      } else {
         this.stream.write(b, off, len);
      }

   }

   /** @deprecated */
   public void close() throws IOException {
      LOG.trace("enter RequestOutputStream.close()");
      if (!this.closed) {
         try {
            if (this.useChunking) {
               this.stream.write(ZERO, 0, ZERO.length);
               this.stream.write(CRLF, 0, CRLF.length);
               this.stream.write(ENDCHUNK, 0, ENDCHUNK.length);
            }
         } catch (IOException var6) {
            LOG.debug("Unexpected exception caught when closing output  stream", var6);
            throw var6;
         } finally {
            this.closed = true;
            super.close();
         }
      }

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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$RequestOutputStream == null ? (class$org$apache$commons$httpclient$RequestOutputStream = class$("org.apache.commons.httpclient.RequestOutputStream")) : class$org$apache$commons$httpclient$RequestOutputStream);
      CRLF = new byte[]{13, 10};
      ENDCHUNK = CRLF;
      ZERO = new byte[]{48};
      ONE = new byte[]{49};
   }
}
