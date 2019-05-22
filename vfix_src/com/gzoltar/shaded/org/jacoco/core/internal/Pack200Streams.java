package com.gzoltar.shaded.org.jacoco.core.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

public final class Pack200Streams {
   public static InputStream unpack(InputStream input) throws IOException {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      JarOutputStream jar = new JarOutputStream(buffer);
      Pack200.newUnpacker().unpack(new Pack200Streams.NoCloseInput(input), jar);
      jar.finish();
      return new ByteArrayInputStream(buffer.toByteArray());
   }

   public static void pack(byte[] source, OutputStream output) throws IOException {
      JarInputStream jar = new JarInputStream(new ByteArrayInputStream(source));
      Pack200.newPacker().pack(jar, output);
   }

   private Pack200Streams() {
   }

   private static class NoCloseInput extends FilterInputStream {
      protected NoCloseInput(InputStream in) {
         super(in);
      }

      public void close() throws IOException {
      }
   }
}
