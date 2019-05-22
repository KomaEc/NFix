package com.gzoltar.shaded.org.pitest.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class StreamUtil {
   public static byte[] streamToByteArray(InputStream in) throws IOException {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      copy(in, result);
      result.close();
      return result.toByteArray();
   }

   public static InputStream copyStream(InputStream in) throws IOException {
      byte[] bs = streamToByteArray(in);
      return new ByteArrayInputStream(bs);
   }

   private static void copy(InputStream input, OutputStream output) throws IOException {
      ReadableByteChannel src = Channels.newChannel(input);
      WritableByteChannel dest = Channels.newChannel(output);
      ByteBuffer buffer = ByteBuffer.allocateDirect(16384);

      while(src.read(buffer) != -1) {
         buffer.flip();
         dest.write(buffer);
         buffer.compact();
      }

      buffer.flip();

      while(buffer.hasRemaining()) {
         dest.write(buffer);
      }

   }
}
