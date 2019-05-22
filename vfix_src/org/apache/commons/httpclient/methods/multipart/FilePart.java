package org.apache.commons.httpclient.methods.multipart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FilePart extends PartBase {
   public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
   public static final String DEFAULT_CHARSET = "ISO-8859-1";
   public static final String DEFAULT_TRANSFER_ENCODING = "binary";
   private static final Log LOG;
   protected static final String FILE_NAME = "; filename=";
   protected static final byte[] FILE_NAME_BYTES;
   private PartSource source;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$multipart$FilePart;

   public FilePart(String name, PartSource partSource, String contentType, String charset) {
      super(name, contentType == null ? "application/octet-stream" : contentType, charset == null ? "ISO-8859-1" : charset, "binary");
      if (partSource == null) {
         throw new IllegalArgumentException("Source may not be null");
      } else if (partSource.getLength() < 0L) {
         throw new IllegalArgumentException("Source length must be >= 0");
      } else {
         this.source = partSource;
      }
   }

   public FilePart(String name, PartSource partSource) {
      this(name, (PartSource)partSource, (String)null, (String)null);
   }

   public FilePart(String name, File file) throws FileNotFoundException {
      this(name, (PartSource)(new FilePartSource(file)), (String)null, (String)null);
   }

   public FilePart(String name, File file, String contentType, String charset) throws FileNotFoundException {
      this(name, (PartSource)(new FilePartSource(file)), contentType, charset);
   }

   public FilePart(String name, String fileName, File file) throws FileNotFoundException {
      this(name, (PartSource)(new FilePartSource(fileName, file)), (String)null, (String)null);
   }

   public FilePart(String name, String fileName, File file, String contentType, String charset) throws FileNotFoundException {
      this(name, (PartSource)(new FilePartSource(fileName, file)), contentType, charset);
   }

   protected void sendDispositionHeader(OutputStream out) throws IOException {
      LOG.trace("enter sendDispositionHeader(OutputStream out)");
      super.sendDispositionHeader(out);
      String filename = this.source.getFileName();
      if (filename != null) {
         out.write(FILE_NAME_BYTES);
         out.write(Part.QUOTE_BYTES);
         out.write(HttpConstants.getAsciiBytes(filename));
         out.write(Part.QUOTE_BYTES);
      }

   }

   protected void sendData(OutputStream out) throws IOException {
      LOG.trace("enter sendData(OutputStream out)");
      if (this.lengthOfData() == 0L) {
         LOG.debug("No data to send.");
      } else {
         byte[] tmp = new byte[4096];
         InputStream instream = this.source.createInputStream();

         int len;
         try {
            while((len = instream.read(tmp)) >= 0) {
               out.write(tmp, 0, len);
            }
         } finally {
            instream.close();
         }

      }
   }

   protected PartSource getSource() {
      LOG.trace("enter getSource()");
      return this.source;
   }

   protected long lengthOfData() throws IOException {
      LOG.trace("enter lengthOfData()");
      return this.source.getLength();
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$multipart$FilePart == null ? (class$org$apache$commons$httpclient$methods$multipart$FilePart = class$("org.apache.commons.httpclient.methods.multipart.FilePart")) : class$org$apache$commons$httpclient$methods$multipart$FilePart);
      FILE_NAME_BYTES = HttpConstants.getAsciiBytes("; filename=");
   }
}
