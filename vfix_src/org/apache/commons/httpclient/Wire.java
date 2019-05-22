package org.apache.commons.httpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class Wire {
   public static Wire HEADER_WIRE = new Wire(LogFactory.getLog("httpclient.wire.header"));
   public static Wire CONTENT_WIRE = new Wire(LogFactory.getLog("httpclient.wire.content"));
   private Log log;

   private Wire(Log log) {
      this.log = log;
   }

   private void wire(String header, InputStream instream) throws IOException {
      StringBuffer buffer = new StringBuffer();

      while(true) {
         int ch;
         while((ch = instream.read()) != -1) {
            if (ch == 13) {
               buffer.append("[\\r]");
            } else if (ch == 10) {
               buffer.append("[\\n]\"");
               buffer.insert(0, "\"");
               buffer.insert(0, header);
               this.log.debug(buffer.toString());
               buffer.setLength(0);
            } else if (ch >= 32 && ch <= 127) {
               buffer.append((char)ch);
            } else {
               buffer.append("[0x");
               buffer.append(Integer.toHexString(ch));
               buffer.append("]");
            }
         }

         if (buffer.length() > 0) {
            buffer.append("\"");
            buffer.insert(0, "\"");
            buffer.insert(0, header);
            this.log.debug(buffer.toString());
         }

         return;
      }
   }

   public boolean enabled() {
      return this.log.isDebugEnabled();
   }

   public void output(InputStream outstream) throws IOException {
      if (outstream == null) {
         throw new IllegalArgumentException("Output may not be null");
      } else {
         this.wire(">> ", outstream);
      }
   }

   public void input(InputStream instream) throws IOException {
      if (instream == null) {
         throw new IllegalArgumentException("Input may not be null");
      } else {
         this.wire("<< ", instream);
      }
   }

   public void output(byte[] b, int off, int len) throws IOException {
      if (b == null) {
         throw new IllegalArgumentException("Output may not be null");
      } else {
         this.wire(">> ", new ByteArrayInputStream(b, off, len));
      }
   }

   public void input(byte[] b, int off, int len) throws IOException {
      if (b == null) {
         throw new IllegalArgumentException("Input may not be null");
      } else {
         this.wire("<< ", new ByteArrayInputStream(b, off, len));
      }
   }

   public void output(byte[] b) throws IOException {
      if (b == null) {
         throw new IllegalArgumentException("Output may not be null");
      } else {
         this.wire(">> ", new ByteArrayInputStream(b));
      }
   }

   public void input(byte[] b) throws IOException {
      if (b == null) {
         throw new IllegalArgumentException("Input may not be null");
      } else {
         this.wire("<< ", new ByteArrayInputStream(b));
      }
   }

   public void output(int b) throws IOException {
      this.output(new byte[]{(byte)b});
   }

   public void input(int b) throws IOException {
      this.input(new byte[]{(byte)b});
   }

   public void output(String s) throws IOException {
      if (s == null) {
         throw new IllegalArgumentException("Output may not be null");
      } else {
         this.output(s.getBytes());
      }
   }

   public void input(String s) throws IOException {
      if (s == null) {
         throw new IllegalArgumentException("Input may not be null");
      } else {
         this.input(s.getBytes());
      }
   }
}
