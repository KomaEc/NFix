package org.apache.commons.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStream extends InputStream {
   private InputStream in;
   private int chunkSize;
   private int pos;
   private boolean bof = true;
   private boolean eof = false;
   private boolean closed = false;
   private HttpMethod method;

   public ChunkedInputStream(InputStream in, HttpMethod method) throws IOException {
      if (in == null) {
         throw new IllegalArgumentException("InputStream parameter may not be null");
      } else if (method == null) {
         throw new IllegalArgumentException("HttpMethod parameter may not be null");
      } else {
         this.in = in;
         this.method = method;
         this.pos = 0;
      }
   }

   public int read() throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.eof) {
         return -1;
      } else {
         if (this.pos >= this.chunkSize) {
            this.nextChunk();
            if (this.eof) {
               return -1;
            }
         }

         ++this.pos;
         return this.in.read();
      }
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if (this.closed) {
         throw new IOException("Attempted read from closed stream.");
      } else if (this.eof) {
         return -1;
      } else {
         if (this.pos >= this.chunkSize) {
            this.nextChunk();
            if (this.eof) {
               return -1;
            }
         }

         len = Math.min(len, this.chunkSize - this.pos);
         int count = this.in.read(b, off, len);
         this.pos += count;
         return count;
      }
   }

   public int read(byte[] b) throws IOException {
      return this.read(b, 0, b.length);
   }

   private void readCRLF() throws IOException {
      int cr = this.in.read();
      int lf = this.in.read();
      if (cr != 13 || lf != 10) {
         throw new IOException("CRLF expected at end of chunk: " + cr + "/" + lf);
      }
   }

   private void nextChunk() throws IOException {
      if (!this.bof) {
         this.readCRLF();
      }

      this.chunkSize = getChunkSizeFromInputStream(this.in);
      this.bof = false;
      this.pos = 0;
      if (this.chunkSize == 0) {
         this.eof = true;
         this.parseTrailerHeaders();
      }

   }

   private static int getChunkSizeFromInputStream(InputStream in) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte state = 0;

      while(state != -1) {
         int b = in.read();
         if (b == -1) {
            throw new IOException("chunked stream ended unexpectedly");
         }

         switch(state) {
         case 0:
            switch(b) {
            case 13:
               state = 1;
               continue;
            case 34:
               state = 2;
            default:
               baos.write(b);
               continue;
            }
         case 1:
            if (b != 10) {
               throw new IOException("Protocol violation: Unexpected single newline character in chunk size");
            }

            state = -1;
            break;
         case 2:
            switch(b) {
            case 34:
               state = 0;
            default:
               baos.write(b);
               continue;
            case 92:
               b = in.read();
               baos.write(b);
               continue;
            }
         default:
            throw new RuntimeException("assertion failed");
         }
      }

      String dataString = HttpConstants.getString(baos.toByteArray());
      int separator = dataString.indexOf(59);
      dataString = separator > 0 ? dataString.substring(0, separator).trim() : dataString.trim();

      try {
         int result = Integer.parseInt(dataString.trim(), 16);
         return result;
      } catch (NumberFormatException var7) {
         throw new IOException("Bad chunk size: " + dataString);
      }
   }

   private void parseTrailerHeaders() throws IOException {
      Header[] footers = HttpParser.parseHeaders(this.in);

      for(int i = 0; i < footers.length; ++i) {
         this.method.addResponseFooter(footers[i]);
      }

   }

   public void close() throws IOException {
      if (!this.closed) {
         try {
            if (!this.eof) {
               exhaustInputStream(this);
            }
         } finally {
            this.eof = true;
            this.closed = true;
         }
      }

   }

   static void exhaustInputStream(InputStream inStream) throws IOException {
      byte[] buffer = new byte[1024];

      while(inStream.read(buffer) >= 0) {
      }

   }
}
