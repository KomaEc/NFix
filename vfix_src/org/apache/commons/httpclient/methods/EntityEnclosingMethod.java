package org.apache.commons.httpclient.methods;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.httpclient.ChunkedOutputStream;
import org.apache.commons.httpclient.ContentLengthInputStream;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class EntityEnclosingMethod extends ExpectContinueMethod {
   public static final int CONTENT_LENGTH_AUTO = -2;
   public static final int CONTENT_LENGTH_CHUNKED = -1;
   private static final Log LOG;
   private byte[] buffer = null;
   private InputStream requestStream = null;
   private String requestString = null;
   private byte[] contentCache = null;
   private int repeatCount = 0;
   private int requestContentLength = -2;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$EntityEnclosingMethod;

   public EntityEnclosingMethod() {
      this.setFollowRedirects(false);
   }

   public EntityEnclosingMethod(String uri) {
      super(uri);
      this.setFollowRedirects(false);
   }

   /** @deprecated */
   public EntityEnclosingMethod(String uri, String tempDir) {
      super(uri, tempDir);
      this.setFollowRedirects(false);
   }

   /** @deprecated */
   public EntityEnclosingMethod(String uri, String tempDir, String tempFile) {
      super(uri, tempDir, tempFile);
      this.setFollowRedirects(false);
   }

   protected boolean hasRequestContent() {
      LOG.trace("enter EntityEnclosingMethod.hasRequestContent()");
      return this.buffer != null || this.requestStream != null || this.requestString != null;
   }

   protected void clearRequestBody() {
      LOG.trace("enter EntityEnclosingMethod.clearRequestBody()");
      this.requestStream = null;
      this.requestString = null;
      this.buffer = null;
      this.contentCache = null;
   }

   protected byte[] generateRequestBody() {
      LOG.trace("enter EntityEnclosingMethod.renerateRequestBody()");
      if (this.requestStream != null) {
         this.bufferContent();
      }

      if (this.buffer != null) {
         return this.buffer;
      } else {
         return this.requestString != null ? HttpConstants.getContentBytes(this.requestString, this.getRequestCharSet()) : null;
      }
   }

   public boolean getFollowRedirects() {
      return false;
   }

   public void setFollowRedirects(boolean followRedirects) {
      if (followRedirects) {
      }

      super.setFollowRedirects(false);
   }

   public void setRequestContentLength(int length) {
      LOG.trace("enter EntityEnclosingMethod.setRequestContentLength(int)");
      this.requestContentLength = length;
   }

   protected int getRequestContentLength() {
      LOG.trace("enter EntityEnclosingMethod.getRequestContentLength()");
      if (!this.hasRequestContent()) {
         return 0;
      } else if (this.requestContentLength != -2) {
         return this.requestContentLength;
      } else {
         if (this.contentCache == null) {
            this.contentCache = this.generateRequestBody();
         }

         return this.contentCache == null ? 0 : this.contentCache.length;
      }
   }

   protected void addContentLengthRequestHeader(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter HttpMethodBase.addContentLengthRequestHeader(HttpState, HttpConnection)");
      if (this.getRequestHeader("content-length") == null && this.getRequestHeader("Transfer-Encoding") == null) {
         int len = this.getRequestContentLength();
         if (len >= 0) {
            this.addRequestHeader("Content-Length", String.valueOf(len));
         } else if (len == -1 && this.isHttp11()) {
            this.addRequestHeader("Transfer-Encoding", "chunked");
         }
      }

   }

   public void setRequestBody(InputStream body) {
      LOG.trace("enter EntityEnclosingMethod.setRequestBody(InputStream)");
      this.clearRequestBody();
      this.requestStream = body;
   }

   public InputStream getRequestBody() {
      LOG.trace("enter EntityEnclosingMethod.getRequestBody()");
      byte[] content = this.generateRequestBody();
      return content != null ? new ByteArrayInputStream(content) : new ByteArrayInputStream(new byte[0]);
   }

   public void setRequestBody(String body) {
      LOG.trace("enter EntityEnclosingMethod.setRequestBody(String)");
      this.clearRequestBody();
      this.requestString = body;
   }

   public String getRequestBodyAsString() throws IOException {
      LOG.trace("enter EntityEnclosingMethod.getRequestBodyAsString()");
      byte[] content = this.generateRequestBody();
      return content != null ? HttpConstants.getContentString(content, this.getRequestCharSet()) : null;
   }

   protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter EntityEnclosingMethod.writeRequestBody(HttpState, HttpConnection)");
      if (!this.hasRequestContent()) {
         LOG.debug("Request body has not been specified");
         return true;
      } else {
         int contentLength = this.getRequestContentLength();
         if (contentLength == -1 && !this.isHttp11()) {
            throw new HttpException("Chunked transfer encoding not allowed for HTTP/1.0");
         } else {
            InputStream instream = null;
            if (this.requestStream != null) {
               LOG.debug("Using unbuffered request body");
               instream = this.requestStream;
            } else {
               if (this.contentCache == null) {
                  this.contentCache = this.generateRequestBody();
               }

               if (this.contentCache != null) {
                  LOG.debug("Using buffered request body");
                  instream = new ByteArrayInputStream(this.contentCache);
               }
            }

            if (instream == null) {
               LOG.debug("Request body is empty");
               return true;
            } else if (this.repeatCount > 0 && this.contentCache == null) {
               throw new HttpException("Unbuffered entity enclosing request can not be repeated.");
            } else {
               ++this.repeatCount;
               OutputStream outstream = conn.getRequestOutputStream();
               if (contentLength == -1) {
                  outstream = new ChunkedOutputStream((OutputStream)outstream);
               }

               if (contentLength >= 0) {
                  instream = new ContentLengthInputStream((InputStream)instream, contentLength);
               }

               byte[] tmp = new byte[4096];
               int total = 0;

               int i;
               for(boolean var8 = false; (i = ((InputStream)instream).read(tmp)) >= 0; total += i) {
                  ((OutputStream)outstream).write(tmp, 0, i);
               }

               if (outstream instanceof ChunkedOutputStream) {
                  ((ChunkedOutputStream)outstream).writeClosingChunk();
               }

               if (contentLength > 0 && total < contentLength) {
                  throw new IOException("Unexpected end of input stream after " + total + " bytes (expected " + contentLength + " bytes)");
               } else {
                  LOG.debug("Request body sent");
                  return true;
               }
            }
         }
      }
   }

   /** @deprecated */
   public void recycle() {
      LOG.trace("enter EntityEnclosingMethod.recycle()");
      this.clearRequestBody();
      this.requestContentLength = -2;
      this.repeatCount = 0;
      super.recycle();
   }

   private void bufferContent() {
      LOG.trace("enter EntityEnclosingMethod.bufferContent()");
      if (this.buffer == null) {
         if (this.requestStream != null) {
            try {
               ByteArrayOutputStream tmp = new ByteArrayOutputStream();
               byte[] data = new byte[4096];
               boolean var3 = false;

               int l;
               while((l = this.requestStream.read(data)) >= 0) {
                  tmp.write(data, 0, l);
               }

               this.buffer = tmp.toByteArray();
               this.requestStream = null;
            } catch (IOException var4) {
               LOG.error(var4.getMessage(), var4);
               this.buffer = null;
               this.requestStream = null;
            }
         }

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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$EntityEnclosingMethod == null ? (class$org$apache$commons$httpclient$methods$EntityEnclosingMethod = class$("org.apache.commons.httpclient.methods.EntityEnclosingMethod")) : class$org$apache$commons$httpclient$methods$EntityEnclosingMethod);
   }
}
