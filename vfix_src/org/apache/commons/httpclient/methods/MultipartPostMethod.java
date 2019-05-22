package org.apache.commons.httpclient.methods;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MultipartPostMethod extends ExpectContinueMethod {
   public static final String MULTIPART_FORM_CONTENT_TYPE = "multipart/form-data";
   private static final Log LOG;
   private final List parameters = new ArrayList();
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$MultipartPostMethod;

   public MultipartPostMethod() {
   }

   public MultipartPostMethod(String uri) {
      super(uri);
   }

   public MultipartPostMethod(String uri, String tempDir) {
      super(uri, tempDir);
   }

   public MultipartPostMethod(String uri, String tempDir, String tempFile) {
      super(uri, tempDir, tempFile);
   }

   protected boolean hasRequestContent() {
      return true;
   }

   public String getName() {
      return "POST";
   }

   public void addParameter(String parameterName, String parameterValue) {
      LOG.trace("enter addParameter(String parameterName, String parameterValue)");
      Part param = new StringPart(parameterName, parameterValue);
      this.parameters.add(param);
   }

   public void addParameter(String parameterName, File parameterFile) throws FileNotFoundException {
      LOG.trace("enter MultipartPostMethod.addParameter(String parameterName, File parameterFile)");
      Part param = new FilePart(parameterName, parameterFile);
      this.parameters.add(param);
   }

   public void addParameter(String parameterName, String fileName, File parameterFile) throws FileNotFoundException {
      LOG.trace("enter MultipartPostMethod.addParameter(String parameterName, String fileName, File parameterFile)");
      Part param = new FilePart(parameterName, fileName, parameterFile);
      this.parameters.add(param);
   }

   public void addPart(Part part) {
      LOG.trace("enter addPart(Part part)");
      this.parameters.add(part);
   }

   public Part[] getParts() {
      return (Part[])this.parameters.toArray(new Part[this.parameters.size()]);
   }

   protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter MultipartPostMethod.addRequestHeaders(HttpState state, HttpConnection conn)");
      super.addRequestHeaders(state, conn);
      if (!this.parameters.isEmpty()) {
         StringBuffer buffer = new StringBuffer("multipart/form-data");
         if (Part.getBoundary() != null) {
            buffer.append("; boundary=");
            buffer.append(Part.getBoundary());
         }

         this.setRequestHeader("Content-Type", buffer.toString());
      }

   }

   protected boolean writeRequestBody(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter MultipartPostMethod.writeRequestBody(HttpState state, HttpConnection conn)");
      OutputStream out = conn.getRequestOutputStream();
      Part.sendParts(out, this.getParts());
      return true;
   }

   protected int getRequestContentLength() {
      LOG.trace("enter MultipartPostMethod.getRequestContentLength()");

      try {
         long len = Part.getLengthOfParts(this.getParts());
         return len <= 2147483647L ? (int)len : Integer.MAX_VALUE;
      } catch (IOException var4) {
         throw new RuntimeException(var4.toString());
      }
   }

   /** @deprecated */
   public void recycle() {
      LOG.trace("enter MultipartPostMethod.recycle()");
      super.recycle();
      this.parameters.clear();
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$MultipartPostMethod == null ? (class$org$apache$commons$httpclient$methods$MultipartPostMethod = class$("org.apache.commons.httpclient.methods.MultipartPostMethod")) : class$org$apache$commons$httpclient$methods$MultipartPostMethod);
   }
}
