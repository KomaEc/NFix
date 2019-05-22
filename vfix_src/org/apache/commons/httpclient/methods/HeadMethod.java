package org.apache.commons.httpclient.methods;

import java.io.IOException;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HeadMethod extends HttpMethodBase {
   private static final Log LOG;
   private int bodyCheckTimeout = -1;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$HeadMethod;

   public HeadMethod() {
      this.setFollowRedirects(true);
   }

   public HeadMethod(String uri) {
      super(uri);
      this.setFollowRedirects(true);
   }

   public String getName() {
      return "HEAD";
   }

   /** @deprecated */
   public void recycle() {
      super.recycle();
      this.setFollowRedirects(true);
   }

   protected void readResponseBody(HttpState state, HttpConnection conn) throws IOException {
      LOG.trace("enter HeadMethod.readResponseBody(HttpState, HttpConnection)");
      if (this.bodyCheckTimeout < 0) {
         this.responseBodyConsumed();
      } else {
         if (LOG.isDebugEnabled()) {
            LOG.debug("Check for non-compliant response body. Timeout in " + this.bodyCheckTimeout + " ms");
         }

         boolean responseAvailable = false;

         try {
            responseAvailable = conn.isResponseAvailable(this.bodyCheckTimeout);
         } catch (IOException var5) {
            LOG.debug("An IOException occurred while testing if a response was available, we will assume one is not.", var5);
            responseAvailable = false;
         }

         if (responseAvailable) {
            if (this.isStrictMode()) {
               throw new HttpException("Body content may not be sent in response to HTTP HEAD request");
            }

            LOG.warn("Body content returned in response to HTTP HEAD");
            super.readResponseBody(state, conn);
         }
      }

   }

   public int getBodyCheckTimeout() {
      return this.bodyCheckTimeout;
   }

   public void setBodyCheckTimeout(int timeout) {
      this.bodyCheckTimeout = timeout;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$HeadMethod == null ? (class$org$apache$commons$httpclient$methods$HeadMethod = class$("org.apache.commons.httpclient.methods.HeadMethod")) : class$org$apache$commons$httpclient$methods$HeadMethod);
   }
}
