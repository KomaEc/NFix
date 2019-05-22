package org.jboss.util.stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jboss.logging.Logger;

public final class Streams {
   private static final Logger log = Logger.getLogger(Streams.class);
   public static final int DEFAULT_BUFFER_SIZE = 2048;

   public static boolean close(InputStream stream) {
      if (stream == null) {
         return true;
      } else {
         boolean success = true;

         try {
            stream.close();
         } catch (IOException var3) {
            success = false;
         }

         return success;
      }
   }

   public static boolean close(OutputStream stream) {
      if (stream == null) {
         return true;
      } else {
         boolean success = true;

         try {
            stream.close();
         } catch (IOException var3) {
            success = false;
         }

         return success;
      }
   }

   public static boolean close(Object stream) {
      boolean success = false;
      if (stream instanceof InputStream) {
         success = close((InputStream)stream);
      } else {
         if (!(stream instanceof OutputStream)) {
            throw new IllegalArgumentException("stream is not an InputStream or OutputStream");
         }

         success = close((OutputStream)stream);
      }

      return success;
   }

   public static boolean close(InputStream[] streams) {
      boolean success = true;

      for(int i = 0; i < streams.length; ++i) {
         boolean rv = close(streams[i]);
         if (!rv) {
            success = false;
         }
      }

      return success;
   }

   public static boolean close(OutputStream[] streams) {
      boolean success = true;

      for(int i = 0; i < streams.length; ++i) {
         boolean rv = close(streams[i]);
         if (!rv) {
            success = false;
         }
      }

      return success;
   }

   public static boolean close(Object[] streams) {
      boolean success = true;

      for(int i = 0; i < streams.length; ++i) {
         boolean rv = close(streams[i]);
         if (!rv) {
            success = false;
         }
      }

      return success;
   }

   public static boolean fclose(OutputStream stream) {
      return flush(stream) && close(stream);
   }

   public static boolean fclose(OutputStream[] streams) {
      boolean success = true;

      for(int i = 0; i < streams.length; ++i) {
         boolean rv = fclose(streams[i]);
         if (!rv) {
            success = false;
         }
      }

      return success;
   }

   public static boolean flush(OutputStream stream) {
      if (stream == null) {
         return true;
      } else {
         boolean success = true;

         try {
            stream.flush();
         } catch (IOException var3) {
            success = false;
         }

         return success;
      }
   }

   public static boolean flush(OutputStream[] streams) {
      boolean success = true;

      for(int i = 0; i < streams.length; ++i) {
         boolean rv = flush(streams[i]);
         if (!rv) {
            success = false;
         }
      }

      return success;
   }

   public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
      long total = 0L;
      boolean trace = log.isTraceEnabled();
      if (trace) {
         log.trace("copying " + input + " to " + output + " with buffer size: " + buffer.length);
      }

      int read;
      while((read = input.read(buffer)) != -1) {
         output.write(buffer, 0, read);
         total += (long)read;
         if (trace) {
            log.trace("bytes read: " + read + "; total bytes read: " + total);
         }
      }

      return total;
   }

   public static long copy(InputStream input, OutputStream output, int size) throws IOException {
      return copy(input, output, new byte[size]);
   }

   public static long copy(InputStream input, OutputStream output) throws IOException {
      return copy(input, output, 2048);
   }

   public static long copyb(InputStream input, OutputStream output) throws IOException {
      if (!(input instanceof BufferedInputStream)) {
         input = new BufferedInputStream((InputStream)input);
      }

      if (!(output instanceof BufferedOutputStream)) {
         output = new BufferedOutputStream((OutputStream)output);
      }

      long bytes = copy((InputStream)input, (OutputStream)output, 2048);
      ((OutputStream)output).flush();
      return bytes;
   }

   public static long copySome(InputStream input, OutputStream output, byte[] buffer, long length) throws IOException {
      long total = 0L;
      boolean trace = log.isTraceEnabled();
      int readLength = Math.min((int)length, buffer.length);
      if (trace) {
         log.trace("initial read length: " + readLength);
      }

      int read;
      while(readLength != 0 && (read = input.read(buffer, 0, readLength)) != -1) {
         if (trace) {
            log.trace("read bytes: " + read);
         }

         output.write(buffer, 0, read);
         total += (long)read;
         if (trace) {
            log.trace("total bytes read: " + total);
         }

         readLength = Math.min((int)(length - total), buffer.length);
         if (trace) {
            log.trace("next read length: " + readLength);
         }
      }

      return total;
   }

   public static long copySome(InputStream input, OutputStream output, int size, long length) throws IOException {
      return copySome(input, output, new byte[size], length);
   }

   public static long copySome(InputStream input, OutputStream output, long length) throws IOException {
      return copySome(input, output, 2048, length);
   }
}
