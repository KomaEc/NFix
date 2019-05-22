package org.apache.commons.httpclient.methods.multipart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Part {
   private static final Log LOG;
   protected static final String BOUNDARY = "----------------314159265358979323846";
   protected static final byte[] BOUNDARY_BYTES;
   protected static final String CRLF = "\r\n";
   protected static final byte[] CRLF_BYTES;
   protected static final String QUOTE = "\"";
   protected static final byte[] QUOTE_BYTES;
   protected static final String EXTRA = "--";
   protected static final byte[] EXTRA_BYTES;
   protected static final String CONTENT_DISPOSITION = "Content-Disposition: form-data; name=";
   protected static final byte[] CONTENT_DISPOSITION_BYTES;
   protected static final String CONTENT_TYPE = "Content-Type: ";
   protected static final byte[] CONTENT_TYPE_BYTES;
   protected static final String CHARSET = "; charset=";
   protected static final byte[] CHARSET_BYTES;
   protected static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: ";
   protected static final byte[] CONTENT_TRANSFER_ENCODING_BYTES;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$multipart$Part;

   public static String getBoundary() {
      return "----------------314159265358979323846";
   }

   public abstract String getName();

   public abstract String getContentType();

   public abstract String getCharSet();

   public abstract String getTransferEncoding();

   protected void sendStart(OutputStream out) throws IOException {
      LOG.trace("enter sendStart(OutputStream out)");
      out.write(EXTRA_BYTES);
      out.write(BOUNDARY_BYTES);
      out.write(CRLF_BYTES);
   }

   protected void sendDispositionHeader(OutputStream out) throws IOException {
      LOG.trace("enter sendDispositionHeader(OutputStream out)");
      out.write(CONTENT_DISPOSITION_BYTES);
      out.write(QUOTE_BYTES);
      out.write(HttpConstants.getAsciiBytes(this.getName()));
      out.write(QUOTE_BYTES);
   }

   protected void sendContentTypeHeader(OutputStream out) throws IOException {
      LOG.trace("enter sendContentTypeHeader(OutputStream out)");
      String contentType = this.getContentType();
      if (contentType != null) {
         out.write(CRLF_BYTES);
         out.write(CONTENT_TYPE_BYTES);
         out.write(HttpConstants.getAsciiBytes(contentType));
         String charSet = this.getCharSet();
         if (charSet != null) {
            out.write(CHARSET_BYTES);
            out.write(HttpConstants.getAsciiBytes(charSet));
         }
      }

   }

   protected void sendTransferEncodingHeader(OutputStream out) throws IOException {
      LOG.trace("enter sendTransferEncodingHeader(OutputStream out)");
      String transferEncoding = this.getTransferEncoding();
      if (transferEncoding != null) {
         out.write(CRLF_BYTES);
         out.write(CONTENT_TRANSFER_ENCODING_BYTES);
         out.write(HttpConstants.getAsciiBytes(transferEncoding));
      }

   }

   protected void sendEndOfHeader(OutputStream out) throws IOException {
      LOG.trace("enter sendEndOfHeader(OutputStream out)");
      out.write(CRLF_BYTES);
      out.write(CRLF_BYTES);
   }

   protected abstract void sendData(OutputStream var1) throws IOException;

   protected abstract long lengthOfData() throws IOException;

   protected void sendEnd(OutputStream out) throws IOException {
      LOG.trace("enter sendEnd(OutputStream out)");
      out.write(CRLF_BYTES);
   }

   public void send(OutputStream out) throws IOException {
      LOG.trace("enter send(OutputStream out)");
      this.sendStart(out);
      this.sendDispositionHeader(out);
      this.sendContentTypeHeader(out);
      this.sendTransferEncodingHeader(out);
      this.sendEndOfHeader(out);
      this.sendData(out);
      this.sendEnd(out);
   }

   public long length() throws IOException {
      LOG.trace("enter length()");
      ByteArrayOutputStream overhead = new ByteArrayOutputStream();
      this.sendStart(overhead);
      this.sendDispositionHeader(overhead);
      this.sendContentTypeHeader(overhead);
      this.sendTransferEncodingHeader(overhead);
      this.sendEndOfHeader(overhead);
      this.sendEnd(overhead);
      return (long)overhead.size() + this.lengthOfData();
   }

   public String toString() {
      return this.getName();
   }

   public static void sendParts(OutputStream out, Part[] parts) throws IOException {
      LOG.trace("enter sendParts(OutputStream out, Parts[])");
      if (parts == null) {
         throw new IllegalArgumentException("Parts may not be null");
      } else {
         for(int i = 0; i < parts.length; ++i) {
            parts[i].send(out);
         }

         out.write(EXTRA_BYTES);
         out.write(BOUNDARY_BYTES);
         out.write(EXTRA_BYTES);
         out.write(CRLF_BYTES);
      }
   }

   public static long getLengthOfParts(Part[] parts) throws IOException {
      LOG.trace("getLengthOfParts(Parts[])");
      if (parts == null) {
         throw new IllegalArgumentException("Parts may not be null");
      } else {
         long total = 0L;

         for(int i = 0; i < parts.length; ++i) {
            total += parts[i].length();
         }

         total += (long)EXTRA_BYTES.length;
         total += (long)BOUNDARY_BYTES.length;
         total += (long)EXTRA_BYTES.length;
         total += (long)CRLF_BYTES.length;
         return total;
      }
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$multipart$Part == null ? (class$org$apache$commons$httpclient$methods$multipart$Part = class$("org.apache.commons.httpclient.methods.multipart.Part")) : class$org$apache$commons$httpclient$methods$multipart$Part);
      BOUNDARY_BYTES = HttpConstants.getAsciiBytes("----------------314159265358979323846");
      CRLF_BYTES = HttpConstants.getAsciiBytes("\r\n");
      QUOTE_BYTES = HttpConstants.getAsciiBytes("\"");
      EXTRA_BYTES = HttpConstants.getAsciiBytes("--");
      CONTENT_DISPOSITION_BYTES = HttpConstants.getAsciiBytes("Content-Disposition: form-data; name=");
      CONTENT_TYPE_BYTES = HttpConstants.getAsciiBytes("Content-Type: ");
      CHARSET_BYTES = HttpConstants.getAsciiBytes("; charset=");
      CONTENT_TRANSFER_ENCODING_BYTES = HttpConstants.getAsciiBytes("Content-Transfer-Encoding: ");
   }
}
