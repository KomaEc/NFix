package org.apache.maven.surefire.shade.org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.surefire.shade.org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.maven.surefire.shade.org.apache.commons.io.output.StringBuilderWriter;

public class IOUtils {
   private static final int EOF = -1;
   public static final char DIR_SEPARATOR_UNIX = '/';
   public static final char DIR_SEPARATOR_WINDOWS = '\\';
   public static final char DIR_SEPARATOR;
   public static final String LINE_SEPARATOR_UNIX = "\n";
   public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
   public static final String LINE_SEPARATOR;
   private static final int DEFAULT_BUFFER_SIZE = 4096;
   private static final int SKIP_BUFFER_SIZE = 2048;
   private static char[] SKIP_CHAR_BUFFER;
   private static byte[] SKIP_BYTE_BUFFER;

   public static void closeQuietly(Reader input) {
      closeQuietly((Closeable)input);
   }

   public static void closeQuietly(Writer output) {
      closeQuietly((Closeable)output);
   }

   public static void closeQuietly(InputStream input) {
      closeQuietly((Closeable)input);
   }

   public static void closeQuietly(OutputStream output) {
      closeQuietly((Closeable)output);
   }

   public static void closeQuietly(Closeable closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException var2) {
      }

   }

   public static void closeQuietly(Socket sock) {
      if (sock != null) {
         try {
            sock.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void closeQuietly(Selector selector) {
      if (selector != null) {
         try {
            selector.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void closeQuietly(ServerSocket sock) {
      if (sock != null) {
         try {
            sock.close();
         } catch (IOException var2) {
         }
      }

   }

   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
      return ByteArrayOutputStream.toBufferedInputStream(input);
   }

   public static BufferedReader toBufferedReader(Reader reader) {
      return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
   }

   public static byte[] toByteArray(InputStream input) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy((InputStream)input, (OutputStream)output);
      return output.toByteArray();
   }

   public static byte[] toByteArray(InputStream input, long size) throws IOException {
      if (size > 2147483647L) {
         throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
      } else {
         return toByteArray(input, (int)size);
      }
   }

   public static byte[] toByteArray(InputStream input, int size) throws IOException {
      if (size < 0) {
         throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
      } else if (size == 0) {
         return new byte[0];
      } else {
         byte[] data = new byte[size];

         int offset;
         int readed;
         for(offset = 0; offset < size && (readed = input.read(data, offset, size - offset)) != -1; offset += readed) {
         }

         if (offset != size) {
            throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
         } else {
            return data;
         }
      }
   }

   public static byte[] toByteArray(Reader input) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy((Reader)input, (OutputStream)output);
      return output.toByteArray();
   }

   public static byte[] toByteArray(Reader input, String encoding) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      copy((Reader)input, (OutputStream)output, encoding);
      return output.toByteArray();
   }

   /** @deprecated */
   @Deprecated
   public static byte[] toByteArray(String input) throws IOException {
      return input.getBytes();
   }

   public static char[] toCharArray(InputStream is) throws IOException {
      CharArrayWriter output = new CharArrayWriter();
      copy((InputStream)is, (Writer)output);
      return output.toCharArray();
   }

   public static char[] toCharArray(InputStream is, String encoding) throws IOException {
      CharArrayWriter output = new CharArrayWriter();
      copy((InputStream)is, (Writer)output, encoding);
      return output.toCharArray();
   }

   public static char[] toCharArray(Reader input) throws IOException {
      CharArrayWriter sw = new CharArrayWriter();
      copy((Reader)input, (Writer)sw);
      return sw.toCharArray();
   }

   public static String toString(InputStream input) throws IOException {
      return toString((InputStream)input, (String)null);
   }

   public static String toString(InputStream input, String encoding) throws IOException {
      StringBuilderWriter sw = new StringBuilderWriter();
      copy((InputStream)input, (Writer)sw, encoding);
      return sw.toString();
   }

   public static String toString(Reader input) throws IOException {
      StringBuilderWriter sw = new StringBuilderWriter();
      copy((Reader)input, (Writer)sw);
      return sw.toString();
   }

   public static String toString(URI uri) throws IOException {
      return toString((URI)uri, (String)null);
   }

   public static String toString(URI uri, String encoding) throws IOException {
      return toString(uri.toURL(), encoding);
   }

   public static String toString(URL url) throws IOException {
      return toString((URL)url, (String)null);
   }

   public static String toString(URL url, String encoding) throws IOException {
      InputStream inputStream = url.openStream();

      String var3;
      try {
         var3 = toString(inputStream, encoding);
      } finally {
         inputStream.close();
      }

      return var3;
   }

   /** @deprecated */
   @Deprecated
   public static String toString(byte[] input) throws IOException {
      return new String(input);
   }

   /** @deprecated */
   @Deprecated
   public static String toString(byte[] input, String encoding) throws IOException {
      return encoding == null ? new String(input) : new String(input, encoding);
   }

   public static List<String> readLines(InputStream input) throws IOException {
      InputStreamReader reader = new InputStreamReader(input);
      return readLines((Reader)reader);
   }

   public static List<String> readLines(InputStream input, String encoding) throws IOException {
      if (encoding == null) {
         return readLines(input);
      } else {
         InputStreamReader reader = new InputStreamReader(input, encoding);
         return readLines((Reader)reader);
      }
   }

   public static List<String> readLines(Reader input) throws IOException {
      BufferedReader reader = toBufferedReader(input);
      List<String> list = new ArrayList();

      for(String line = reader.readLine(); line != null; line = reader.readLine()) {
         list.add(line);
      }

      return list;
   }

   public static LineIterator lineIterator(Reader reader) {
      return new LineIterator(reader);
   }

   public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
      Reader reader = null;
      if (encoding == null) {
         reader = new InputStreamReader(input);
      } else {
         reader = new InputStreamReader(input, encoding);
      }

      return new LineIterator(reader);
   }

   public static InputStream toInputStream(CharSequence input) {
      return toInputStream(input.toString());
   }

   public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
      return toInputStream(input.toString(), encoding);
   }

   public static InputStream toInputStream(String input) {
      byte[] bytes = input.getBytes();
      return new ByteArrayInputStream(bytes);
   }

   public static InputStream toInputStream(String input, String encoding) throws IOException {
      byte[] bytes = encoding != null ? input.getBytes(encoding) : input.getBytes();
      return new ByteArrayInputStream(bytes);
   }

   public static void write(byte[] data, OutputStream output) throws IOException {
      if (data != null) {
         output.write(data);
      }

   }

   public static void write(byte[] data, Writer output) throws IOException {
      if (data != null) {
         output.write(new String(data));
      }

   }

   public static void write(byte[] data, Writer output, String encoding) throws IOException {
      if (data != null) {
         if (encoding == null) {
            write(data, output);
         } else {
            output.write(new String(data, encoding));
         }
      }

   }

   public static void write(char[] data, Writer output) throws IOException {
      if (data != null) {
         output.write(data);
      }

   }

   public static void write(char[] data, OutputStream output) throws IOException {
      if (data != null) {
         output.write((new String(data)).getBytes());
      }

   }

   public static void write(char[] data, OutputStream output, String encoding) throws IOException {
      if (data != null) {
         if (encoding == null) {
            write(data, output);
         } else {
            output.write((new String(data)).getBytes(encoding));
         }
      }

   }

   public static void write(CharSequence data, Writer output) throws IOException {
      if (data != null) {
         write(data.toString(), output);
      }

   }

   public static void write(CharSequence data, OutputStream output) throws IOException {
      if (data != null) {
         write(data.toString(), output);
      }

   }

   public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
      if (data != null) {
         write(data.toString(), output, encoding);
      }

   }

   public static void write(String data, Writer output) throws IOException {
      if (data != null) {
         output.write(data);
      }

   }

   public static void write(String data, OutputStream output) throws IOException {
      if (data != null) {
         output.write(data.getBytes());
      }

   }

   public static void write(String data, OutputStream output, String encoding) throws IOException {
      if (data != null) {
         if (encoding == null) {
            write(data, output);
         } else {
            output.write(data.getBytes(encoding));
         }
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer data, Writer output) throws IOException {
      if (data != null) {
         output.write(data.toString());
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer data, OutputStream output) throws IOException {
      if (data != null) {
         output.write(data.toString().getBytes());
      }

   }

   /** @deprecated */
   @Deprecated
   public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
      if (data != null) {
         if (encoding == null) {
            write(data, output);
         } else {
            output.write(data.toString().getBytes(encoding));
         }
      }

   }

   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
      if (lines != null) {
         if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
         }

         for(Iterator i$ = lines.iterator(); i$.hasNext(); output.write(lineEnding.getBytes())) {
            Object line = i$.next();
            if (line != null) {
               output.write(line.toString().getBytes());
            }
         }

      }
   }

   public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
      if (encoding == null) {
         writeLines(lines, lineEnding, output);
      } else {
         if (lines == null) {
            return;
         }

         if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
         }

         for(Iterator i$ = lines.iterator(); i$.hasNext(); output.write(lineEnding.getBytes(encoding))) {
            Object line = i$.next();
            if (line != null) {
               output.write(line.toString().getBytes(encoding));
            }
         }
      }

   }

   public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
      if (lines != null) {
         if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
         }

         for(Iterator i$ = lines.iterator(); i$.hasNext(); writer.write(lineEnding)) {
            Object line = i$.next();
            if (line != null) {
               writer.write(line.toString());
            }
         }

      }
   }

   public static int copy(InputStream input, OutputStream output) throws IOException {
      long count = copyLarge(input, output);
      return count > 2147483647L ? -1 : (int)count;
   }

   public static long copyLarge(InputStream input, OutputStream output) throws IOException {
      return copyLarge(input, output, new byte[4096]);
   }

   public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
      long count = 0L;

      int n;
      for(boolean var5 = false; -1 != (n = input.read(buffer)); count += (long)n) {
         output.write(buffer, 0, n);
      }

      return count;
   }

   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
      return copyLarge(input, output, inputOffset, length, new byte[4096]);
   }

   public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
      if (inputOffset > 0L) {
         skipFully(input, inputOffset);
      }

      if (length == 0L) {
         return 0L;
      } else {
         int bufferLength = buffer.length;
         int bytesToRead = bufferLength;
         if (length > 0L && length < (long)bufferLength) {
            bytesToRead = (int)length;
         }

         long totalRead = 0L;

         int read;
         while(bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += (long)read;
            if (length > 0L) {
               bytesToRead = (int)Math.min(length - totalRead, (long)bufferLength);
            }
         }

         return totalRead;
      }
   }

   public static void copy(InputStream input, Writer output) throws IOException {
      InputStreamReader in = new InputStreamReader(input);
      copy((Reader)in, (Writer)output);
   }

   public static void copy(InputStream input, Writer output, String encoding) throws IOException {
      if (encoding == null) {
         copy(input, output);
      } else {
         InputStreamReader in = new InputStreamReader(input, encoding);
         copy((Reader)in, (Writer)output);
      }

   }

   public static int copy(Reader input, Writer output) throws IOException {
      long count = copyLarge(input, output);
      return count > 2147483647L ? -1 : (int)count;
   }

   public static long copyLarge(Reader input, Writer output) throws IOException {
      return copyLarge(input, output, new char[4096]);
   }

   public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
      long count = 0L;

      int n;
      for(boolean var5 = false; -1 != (n = input.read(buffer)); count += (long)n) {
         output.write(buffer, 0, n);
      }

      return count;
   }

   public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
      return copyLarge(input, output, inputOffset, length, new char[4096]);
   }

   public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
      if (inputOffset > 0L) {
         skipFully(input, inputOffset);
      }

      if (length == 0L) {
         return 0L;
      } else {
         int bytesToRead = buffer.length;
         if (length > 0L && length < (long)buffer.length) {
            bytesToRead = (int)length;
         }

         long totalRead = 0L;

         int read;
         while(bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += (long)read;
            if (length > 0L) {
               bytesToRead = (int)Math.min(length - totalRead, (long)buffer.length);
            }
         }

         return totalRead;
      }
   }

   public static void copy(Reader input, OutputStream output) throws IOException {
      OutputStreamWriter out = new OutputStreamWriter(output);
      copy((Reader)input, (Writer)out);
      out.flush();
   }

   public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
      if (encoding == null) {
         copy(input, output);
      } else {
         OutputStreamWriter out = new OutputStreamWriter(output, encoding);
         copy((Reader)input, (Writer)out);
         out.flush();
      }

   }

   public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
      if (!(input1 instanceof BufferedInputStream)) {
         input1 = new BufferedInputStream((InputStream)input1);
      }

      if (!(input2 instanceof BufferedInputStream)) {
         input2 = new BufferedInputStream((InputStream)input2);
      }

      int ch2;
      for(int ch = ((InputStream)input1).read(); -1 != ch; ch = ((InputStream)input1).read()) {
         ch2 = ((InputStream)input2).read();
         if (ch != ch2) {
            return false;
         }
      }

      ch2 = ((InputStream)input2).read();
      return ch2 == -1;
   }

   public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
      Reader input1 = toBufferedReader(input1);
      Reader input2 = toBufferedReader(input2);

      int ch2;
      for(int ch = input1.read(); -1 != ch; ch = input1.read()) {
         ch2 = input2.read();
         if (ch != ch2) {
            return false;
         }
      }

      ch2 = input2.read();
      return ch2 == -1;
   }

   public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
      BufferedReader br1 = toBufferedReader(input1);
      BufferedReader br2 = toBufferedReader(input2);
      String line1 = br1.readLine();

      String line2;
      for(line2 = br2.readLine(); line1 != null && line2 != null && line1.equals(line2); line2 = br2.readLine()) {
         line1 = br1.readLine();
      }

      return line1 == null ? line2 == null : line1.equals(line2);
   }

   public static long skip(InputStream input, long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
      } else {
         if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[2048];
         }

         long remain;
         long n;
         for(remain = toSkip; remain > 0L; remain -= n) {
            n = (long)input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, 2048L));
            if (n < 0L) {
               break;
            }
         }

         return toSkip - remain;
      }
   }

   public static long skip(Reader input, long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
      } else {
         if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[2048];
         }

         long remain;
         long n;
         for(remain = toSkip; remain > 0L; remain -= n) {
            n = (long)input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, 2048L));
            if (n < 0L) {
               break;
            }
         }

         return toSkip - remain;
      }
   }

   public static void skipFully(InputStream input, long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
      } else {
         long skipped = skip(input, toSkip);
         if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
         }
      }
   }

   public static void skipFully(Reader input, long toSkip) throws IOException {
      long skipped = skip(input, toSkip);
      if (skipped != toSkip) {
         throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
      }
   }

   public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
      if (length < 0) {
         throw new IllegalArgumentException("Length must not be negative: " + length);
      } else {
         int remaining;
         int count;
         for(remaining = length; remaining > 0; remaining -= count) {
            int location = length - remaining;
            count = input.read(buffer, offset + location, remaining);
            if (-1 == count) {
               break;
            }
         }

         return length - remaining;
      }
   }

   public static int read(Reader input, char[] buffer) throws IOException {
      return read((Reader)input, (char[])buffer, 0, buffer.length);
   }

   public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
      if (length < 0) {
         throw new IllegalArgumentException("Length must not be negative: " + length);
      } else {
         int remaining;
         int count;
         for(remaining = length; remaining > 0; remaining -= count) {
            int location = length - remaining;
            count = input.read(buffer, offset + location, remaining);
            if (-1 == count) {
               break;
            }
         }

         return length - remaining;
      }
   }

   public static int read(InputStream input, byte[] buffer) throws IOException {
      return read((InputStream)input, (byte[])buffer, 0, buffer.length);
   }

   public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
      int actual = read(input, buffer, offset, length);
      if (actual != length) {
         throw new EOFException("Length to read: " + length + " actual: " + actual);
      }
   }

   public static void readFully(Reader input, char[] buffer) throws IOException {
      readFully((Reader)input, (char[])buffer, 0, buffer.length);
   }

   public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
      int actual = read(input, buffer, offset, length);
      if (actual != length) {
         throw new EOFException("Length to read: " + length + " actual: " + actual);
      }
   }

   public static void readFully(InputStream input, byte[] buffer) throws IOException {
      readFully((InputStream)input, (byte[])buffer, 0, buffer.length);
   }

   static {
      DIR_SEPARATOR = File.separatorChar;
      StringBuilderWriter buf = new StringBuilderWriter(4);
      PrintWriter out = new PrintWriter(buf);
      out.println();
      LINE_SEPARATOR = buf.toString();
      out.close();
   }
}
