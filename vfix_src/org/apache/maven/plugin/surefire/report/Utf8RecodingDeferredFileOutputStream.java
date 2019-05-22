package org.apache.maven.plugin.surefire.report;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import org.apache.maven.surefire.shade.org.apache.commons.io.output.DeferredFileOutputStream;

class Utf8RecodingDeferredFileOutputStream {
   private DeferredFileOutputStream deferredFileOutputStream;
   private static final Charset UTF8 = Charset.forName("UTF-8");

   public Utf8RecodingDeferredFileOutputStream(String channel) {
      this.deferredFileOutputStream = new DeferredFileOutputStream(1000000, channel, "deferred", (File)null);
   }

   public void write(byte[] buf, int off, int len) throws IOException {
      if (!Charset.defaultCharset().equals(UTF8)) {
         CharBuffer decodedFromDefaultCharset = Charset.defaultCharset().decode(ByteBuffer.wrap(buf, off, len));
         ByteBuffer utf8Encoded = UTF8.encode(decodedFromDefaultCharset);
         byte[] convertedBytes;
         if (utf8Encoded.hasArray()) {
            convertedBytes = utf8Encoded.array();
            this.deferredFileOutputStream.write(convertedBytes, utf8Encoded.position(), utf8Encoded.remaining());
         } else {
            convertedBytes = new byte[utf8Encoded.remaining()];
            utf8Encoded.get(convertedBytes, 0, utf8Encoded.remaining());
            this.deferredFileOutputStream.write(convertedBytes, 0, convertedBytes.length);
         }
      } else {
         this.deferredFileOutputStream.write(buf, off, len);
      }

   }

   public long getByteCount() {
      return this.deferredFileOutputStream.getByteCount();
   }

   public void close() throws IOException {
      this.deferredFileOutputStream.close();
   }

   public void writeTo(OutputStream out) throws IOException {
      this.deferredFileOutputStream.writeTo(out);
   }

   public void free() {
      if (null != this.deferredFileOutputStream && null != this.deferredFileOutputStream.getFile()) {
         try {
            this.deferredFileOutputStream.close();
            if (!this.deferredFileOutputStream.getFile().delete()) {
               this.deferredFileOutputStream.getFile().deleteOnExit();
            }
         } catch (IOException var2) {
            this.deferredFileOutputStream.getFile().deleteOnExit();
         }
      }

   }
}
