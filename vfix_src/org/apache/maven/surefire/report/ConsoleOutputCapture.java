package org.apache.maven.surefire.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.maven.surefire.util.internal.ByteBuffer;

public class ConsoleOutputCapture {
   public static void startCapture(ConsoleOutputReceiver target) {
      System.setOut(new ConsoleOutputCapture.ForwardingPrintStream(true, target));
      System.setErr(new ConsoleOutputCapture.ForwardingPrintStream(false, target));
   }

   private static class ForwardingPrintStream extends PrintStream {
      private final boolean isStdout;
      private final ConsoleOutputReceiver target;
      static final byte[] newline = new byte[]{10};

      ForwardingPrintStream(boolean stdout, ConsoleOutputReceiver target) {
         super(new ByteArrayOutputStream());
         this.isStdout = stdout;
         this.target = target;
      }

      public void write(byte[] buf, int off, int len) {
         this.target.writeTestOutput(buf, off, len, this.isStdout);
      }

      public void write(byte[] b) throws IOException {
         this.target.writeTestOutput(b, 0, b.length, this.isStdout);
      }

      public void write(int b) {
         byte[] buf = new byte[]{(byte)b};

         try {
            this.write(buf);
         } catch (IOException var4) {
            this.setError();
         }

      }

      public void println(String s) {
         if (s == null) {
            s = "null";
         }

         byte[] bytes = s.getBytes();
         byte[] join = ByteBuffer.join(bytes, 0, bytes.length, newline, 0, 1);
         this.target.writeTestOutput(join, 0, join.length, this.isStdout);
      }

      public void close() {
      }

      public void flush() {
      }
   }
}
