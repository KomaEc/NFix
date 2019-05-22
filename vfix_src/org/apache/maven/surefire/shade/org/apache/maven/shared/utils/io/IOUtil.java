package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.Channel;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class IOUtil {
   private static final int DEFAULT_BUFFER_SIZE = 4096;

   private IOUtil() {
   }

   public static void copy(@Nonnull InputStream input, @Nonnull OutputStream output) throws IOException {
      copy((InputStream)input, (OutputStream)output, 4096);
   }

   public static void copy(@Nonnull InputStream input, @Nonnull OutputStream output, int bufferSize) throws IOException {
      byte[] buffer = new byte[bufferSize];

      int n;
      while(-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }

   }

   public static void copy(@Nonnull Reader input, @Nonnull Writer output) throws IOException {
      copy((Reader)input, (Writer)output, 4096);
   }

   public static void copy(@Nonnull Reader input, @Nonnull Writer output, int bufferSize) throws IOException {
      char[] buffer = new char[bufferSize];

      int n;
      while(-1 != (n = input.read(buffer))) {
         output.write(buffer, 0, n);
      }

      output.flush();
   }

   public static void copy(@Nonnull InputStream input, @Nonnull Writer output) throws IOException {
      copy((InputStream)input, (Writer)output, 4096);
   }

   public static void copy(@Nonnull InputStream input, @Nonnull Writer output, int bufferSize) throws IOException {
      InputStreamReader in = new InputStreamReader(input);
      copy((Reader)in, (Writer)output, bufferSize);
   }

   public static void copy(@Nonnull InputStream input, @Nonnull Writer output, @Nonnull String encoding) throws IOException {
      InputStreamReader in = new InputStreamReader(input, encoding);
      copy((Reader)in, (Writer)output);
   }

   public static void copy(@Nonnull InputStream input, @Nonnull Writer output, @Nonnull String encoding, int bufferSize) throws IOException {
      InputStreamReader in = new InputStreamReader(input, encoding);
      copy((Reader)in, (Writer)output, bufferSize);
   }

   @Nonnull
   public static String toString(@Nonnull InputStream input) throws IOException {
      return toString((InputStream)input, 4096);
   }

   @Nonnull
   public static String toString(@Nonnull InputStream input, int bufferSize) throws IOException {
      StringWriter sw = new StringWriter();
      copy((InputStream)input, (Writer)sw, bufferSize);
      return sw.toString();
   }

   @Nonnull
   public static String toString(@Nonnull InputStream input, @Nonnull String encoding) throws IOException {
      return toString((InputStream)input, encoding, 4096);
   }

   @Nonnull
   public static String toString(@Nonnull InputStream input, @Nonnull String encoding, int bufferSize) throws IOException {
      StringWriter sw = new StringWriter();
      copy((InputStream)input, sw, encoding, bufferSize);
      return sw.toString();
   }

   @Nonnull
   public static byte[] toByteArray(@Nonnull InputStream input) throws IOException {
      return toByteArray((InputStream)input, 4096);
   }

   @Nonnull
   public static byte[] toByteArray(@Nonnull InputStream input, int bufferSize) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy((InputStream)input, (OutputStream)output, bufferSize);
      return output.toByteArray();
   }

   public static void copy(@Nonnull Reader input, @Nonnull OutputStream output) throws IOException {
      copy((Reader)input, (OutputStream)output, 4096);
   }

   public static void copy(@Nonnull Reader input, @Nonnull OutputStream output, int bufferSize) throws IOException {
      OutputStreamWriter out = new OutputStreamWriter(output);
      copy((Reader)input, (Writer)out, bufferSize);
      out.flush();
   }

   @Nonnull
   public static String toString(@Nonnull Reader input) throws IOException {
      return toString((Reader)input, 4096);
   }

   @Nonnull
   public static String toString(@Nonnull Reader input, int bufferSize) throws IOException {
      StringWriter sw = new StringWriter();
      copy((Reader)input, (Writer)sw, bufferSize);
      return sw.toString();
   }

   @Nonnull
   public static byte[] toByteArray(@Nonnull Reader input) throws IOException {
      return toByteArray((Reader)input, 4096);
   }

   @Nonnull
   public static byte[] toByteArray(@Nonnull Reader input, int bufferSize) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy((Reader)input, (OutputStream)output, bufferSize);
      return output.toByteArray();
   }

   public static void copy(@Nonnull String input, @Nonnull OutputStream output) throws IOException {
      copy((String)input, (OutputStream)output, 4096);
   }

   public static void copy(@Nonnull String input, @Nonnull OutputStream output, int bufferSize) throws IOException {
      StringReader in = new StringReader(input);
      OutputStreamWriter out = new OutputStreamWriter(output);
      copy((Reader)in, (Writer)out, bufferSize);
      out.flush();
   }

   public static void copy(@Nonnull String input, @Nonnull Writer output) throws IOException {
      output.write(input);
   }

   @Nonnull
   public static byte[] toByteArray(@Nonnull String input) throws IOException {
      return toByteArray((String)input, 4096);
   }

   @Nonnull
   public static byte[] toByteArray(@Nonnull String input, int bufferSize) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy((String)input, (OutputStream)output, bufferSize);
      return output.toByteArray();
   }

   public static void copy(@Nonnull byte[] input, @Nonnull Writer output) throws IOException {
      copy((byte[])input, (Writer)output, 4096);
   }

   public static void copy(@Nonnull byte[] input, @Nonnull Writer output, int bufferSize) throws IOException {
      ByteArrayInputStream in = new ByteArrayInputStream(input);
      copy((InputStream)in, (Writer)output, bufferSize);
   }

   public static void copy(@Nonnull byte[] input, @Nonnull Writer output, String encoding) throws IOException {
      ByteArrayInputStream in = new ByteArrayInputStream(input);
      copy((InputStream)in, output, encoding);
   }

   public static void copy(@Nonnull byte[] input, @Nonnull Writer output, @Nonnull String encoding, int bufferSize) throws IOException {
      ByteArrayInputStream in = new ByteArrayInputStream(input);
      copy((InputStream)in, output, encoding, bufferSize);
   }

   @Nonnull
   public static String toString(@Nonnull byte[] input) throws IOException {
      return toString((byte[])input, 4096);
   }

   @Nonnull
   public static String toString(@Nonnull byte[] input, int bufferSize) throws IOException {
      StringWriter sw = new StringWriter();
      copy((byte[])input, (Writer)sw, bufferSize);
      return sw.toString();
   }

   @Nonnull
   public static String toString(@Nonnull byte[] input, @Nonnull String encoding) throws IOException {
      return toString((byte[])input, encoding, 4096);
   }

   @Nonnull
   public static String toString(@Nonnull byte[] input, @Nonnull String encoding, int bufferSize) throws IOException {
      StringWriter sw = new StringWriter();
      copy((byte[])input, sw, encoding, bufferSize);
      return sw.toString();
   }

   public static void copy(@Nonnull byte[] input, @Nonnull OutputStream output) throws IOException {
      output.write(input);
   }

   public static boolean contentEquals(@Nonnull InputStream input1, @Nonnull InputStream input2) throws IOException {
      InputStream bufferedInput1 = new BufferedInputStream(input1);
      InputStream bufferedInput2 = new BufferedInputStream(input2);

      int ch2;
      for(int ch = bufferedInput1.read(); -1 != ch; ch = bufferedInput1.read()) {
         ch2 = bufferedInput2.read();
         if (ch != ch2) {
            return false;
         }
      }

      ch2 = bufferedInput2.read();
      return -1 == ch2;
   }

   public static void close(@Nullable Channel channel) {
      if (channel != null) {
         try {
            channel.close();
         } catch (IOException var2) {
         }

      }
   }

   public static void close(@Nullable InputStream inputStream) {
      if (inputStream != null) {
         try {
            inputStream.close();
         } catch (IOException var2) {
         }

      }
   }

   public static void close(@Nullable OutputStream outputStream) {
      if (outputStream != null) {
         try {
            outputStream.close();
         } catch (IOException var2) {
         }

      }
   }

   public static void close(@Nullable Reader reader) {
      if (reader != null) {
         try {
            reader.close();
         } catch (IOException var2) {
         }

      }
   }

   public static void close(@Nullable Writer writer) {
      if (writer != null) {
         try {
            writer.close();
         } catch (IOException var2) {
         }

      }
   }
}
