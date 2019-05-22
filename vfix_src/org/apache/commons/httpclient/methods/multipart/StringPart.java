package org.apache.commons.httpclient.methods.multipart;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringPart extends PartBase {
   private static final Log LOG;
   public static final String DEFAULT_CONTENT_TYPE = "text/plain";
   public static final String DEFAULT_CHARSET = "US-ASCII";
   public static final String DEFAULT_TRANSFER_ENCODING = "8bit";
   private byte[] content;
   private String value;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$multipart$StringPart;

   public StringPart(String name, String value, String charset) {
      super(name, "text/plain", charset == null ? "US-ASCII" : charset, "8bit");
      if (value == null) {
         throw new IllegalArgumentException("Value may not be null");
      } else if (value.indexOf(0) != -1) {
         throw new IllegalArgumentException("NULs may not be present in string parts");
      } else {
         this.value = value;
      }
   }

   public StringPart(String name, String value) {
      this(name, value, (String)null);
   }

   private byte[] getContent() {
      if (this.content == null) {
         this.content = HttpConstants.getContentBytes(this.value, this.getCharSet());
      }

      return this.content;
   }

   protected void sendData(OutputStream out) throws IOException {
      LOG.trace("enter sendData(OutputStream)");
      out.write(this.getContent());
   }

   protected long lengthOfData() throws IOException {
      LOG.trace("enter lengthOfData()");
      return (long)this.getContent().length;
   }

   public void setCharSet(String charSet) {
      super.setCharSet(charSet);
      this.content = null;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$multipart$StringPart == null ? (class$org$apache$commons$httpclient$methods$multipart$StringPart = class$("org.apache.commons.httpclient.methods.multipart.StringPart")) : class$org$apache$commons$httpclient$methods$multipart$StringPart);
   }
}
