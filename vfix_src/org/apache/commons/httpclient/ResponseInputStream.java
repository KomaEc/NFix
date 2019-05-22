package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @deprecated */
public class ResponseInputStream extends InputStream {
   public static final Log LOG;
   private boolean closed = false;
   private boolean chunk = false;
   private boolean endChunk = false;
   private byte[] buffer = null;
   private int length = 0;
   private int pos = 0;
   private int count = 0;
   private int contentLength = -1;
   private InputStream stream = null;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$ResponseInputStream;

   /** @deprecated */
   public ResponseInputStream(InputStream stream, boolean chunked, int contentLength) {
      LOG.trace("enter ResponseInputStream(InputStream, boolean, int)");
      if (null == stream) {
         throw new NullPointerException("InputStream parameter is null");
      } else {
         this.closed = false;
         this.count = 0;
         this.chunk = chunked;
         this.contentLength = contentLength;
         this.stream = stream;
      }
   }

   /** @deprecated */
   public ResponseInputStream(InputStream stream, HttpMethod method) {
      LOG.trace("enter ResponseInputStream(InputStream, HttpMethod)");
      if (null == stream) {
         throw new NullPointerException("InputStream parameter is null");
      } else if (null == method) {
         throw new NullPointerException("HttpMethod parameter is null");
      } else {
         this.closed = false;
         this.count = 0;
         Header transferEncoding = method.getResponseHeader("transfer-encoding");
         if (null != transferEncoding && transferEncoding.getValue().toLowerCase().indexOf("chunked") != -1) {
            this.chunk = true;
         }

         Header contentLengthHeader = method.getResponseHeader("content-length");
         if (null != contentLengthHeader) {
            try {
               this.contentLength = Integer.parseInt(contentLengthHeader.getValue());
            } catch (NumberFormatException var6) {
            }
         }

         this.stream = stream;
      }
   }

   /** @deprecated */
   public void close() throws IOException {
      LOG.trace("enter ResponseInputStream.close()");
      if (!this.closed) {
         try {
            int b;
            if (this.chunk) {
               while(!this.endChunk) {
                  b = this.read();
                  if (b < 0) {
                     break;
                  }
               }
            } else if (this.length > 0) {
               while(this.count < this.length) {
                  b = this.read();
                  if (b < 0) {
                     break;
                  }
               }
            }
         } catch (IOException var6) {
            throw var6;
         } finally {
            this.closed = true;
         }
      }

   }

   /** @deprecated */
   public int read(byte[] b, int off, int len) throws IOException {
      LOG.trace("enter ResponseInputStream.read(byte, int, int)");
      int avail = this.length - this.pos;
      if (avail == 0 && !this.fillBuffer()) {
         return -1;
      } else {
         avail = this.length - this.pos;
         if (avail == 0) {
            return -1;
         } else {
            int toCopy = avail;
            if (avail < 0) {
               return -1;
            } else {
               if (avail > len) {
                  toCopy = len;
               }

               System.arraycopy(this.buffer, this.pos, b, off, toCopy);
               this.pos += toCopy;
               return toCopy;
            }
         }
      }
   }

   /** @deprecated */
   public int read() throws IOException {
      LOG.trace("enter ResponseInputStream.read()");
      return this.pos == this.length && !this.fillBuffer() ? -1 : this.buffer[this.pos++] & 255;
   }

   /** @deprecated */
   private boolean fillBuffer() throws IOException {
      LOG.trace("enter ResponseInputStream.fillBuffer()");
      if (this.closed) {
         return false;
      } else if (this.endChunk) {
         return false;
      } else if (this.contentLength >= 0 && this.count >= this.contentLength) {
         return false;
      } else {
         this.pos = 0;
         if (!this.chunk) {
            try {
               if (this.buffer == null) {
                  this.buffer = new byte[4096];
               }

               this.length = this.stream.read(this.buffer);
               this.count += this.length;
            } catch (Throwable var4) {
               LOG.debug("Exception thrown reading from response", var4);
               throw new IOException(var4.getMessage());
            }
         } else {
            String trailingLine;
            try {
               trailingLine = this.readLineFromStream();
               if (trailingLine == null) {
                  throw new NumberFormatException("unable to find chunk length");
               }

               this.length = Integer.parseInt(trailingLine.trim(), 16);
            } catch (NumberFormatException var6) {
               this.length = -1;
               this.chunk = false;
               this.endChunk = true;
               this.closed = true;
               return false;
            }

            if (this.length == 0) {
               for(trailingLine = this.readLineFromStream(); !trailingLine.equals(""); trailingLine = this.readLineFromStream()) {
               }

               this.endChunk = true;
               return false;
            }

            if (this.buffer == null || this.length > this.buffer.length) {
               this.buffer = new byte[this.length];
            }

            int nbRead = 0;

            int currentRead;
            for(boolean var2 = false; nbRead < this.length; nbRead += currentRead) {
               try {
                  currentRead = this.stream.read(this.buffer, nbRead, this.length - nbRead);
               } catch (Throwable var5) {
                  LOG.debug("Exception thrown reading chunk from response", var5);
                  throw new IOException();
               }

               if (currentRead < 0) {
                  throw new IOException("Not enough bytes read");
               }
            }

            this.readLineFromStream();
         }

         return true;
      }
   }

   /** @deprecated */
   private String readLineFromStream() throws IOException {
      LOG.trace("enter ResponseInputStream.ReadLineFromStream()");
      StringBuffer sb = new StringBuffer();

      while(true) {
         int ch = this.stream.read();
         if (ch < 0) {
            if (sb.length() == 0) {
               return null;
            }
            break;
         }

         if (ch != 13) {
            if (ch == 10) {
               break;
            }

            sb.append((char)ch);
         }
      }

      return sb.toString();
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$ResponseInputStream == null ? (class$org$apache$commons$httpclient$ResponseInputStream = class$("org.apache.commons.httpclient.ResponseInputStream")) : class$org$apache$commons$httpclient$ResponseInputStream);
   }
}
