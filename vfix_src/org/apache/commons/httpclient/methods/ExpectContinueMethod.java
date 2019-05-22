package org.apache.commons.httpclient.methods;

import java.io.IOException;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ExpectContinueMethod extends GetMethod {
   private boolean useExpectHeader = false;
   private static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$ExpectContinueMethod;

   public ExpectContinueMethod() {
   }

   public ExpectContinueMethod(String uri) {
      super(uri);
   }

   /** @deprecated */
   public ExpectContinueMethod(String uri, String tempDir) {
      super(uri, tempDir);
   }

   /** @deprecated */
   public ExpectContinueMethod(String uri, String tempDir, String tempFile) {
      super(uri, tempDir, tempFile);
   }

   public boolean getUseExpectHeader() {
      return this.useExpectHeader;
   }

   public void setUseExpectHeader(boolean value) {
      this.useExpectHeader = value;
   }

   protected abstract boolean hasRequestContent();

   protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      LOG.trace("enter ExpectContinueMethod.addRequestHeaders(HttpState, HttpConnection)");
      super.addRequestHeaders(state, conn);
      boolean headerPresent = this.getRequestHeader("Expect") != null;
      if (this.getUseExpectHeader() && this.isHttp11() && this.hasRequestContent()) {
         if (!headerPresent) {
            this.setRequestHeader("Expect", "100-continue");
         }
      } else if (headerPresent) {
         this.removeRequestHeader("Expect");
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$ExpectContinueMethod == null ? (class$org$apache$commons$httpclient$methods$ExpectContinueMethod = class$("org.apache.commons.httpclient.methods.ExpectContinueMethod")) : class$org$apache$commons$httpclient$methods$ExpectContinueMethod);
   }
}
