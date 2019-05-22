package org.apache.commons.httpclient.methods;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OptionsMethod extends HttpMethodBase {
   private static final Log LOG;
   private Vector methodsAllowed = new Vector();
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$OptionsMethod;

   public OptionsMethod() {
   }

   public OptionsMethod(String uri) {
      super(uri);
   }

   public String getName() {
      return "OPTIONS";
   }

   public boolean isAllowed(String method) {
      this.checkUsed();
      return this.methodsAllowed.contains(method);
   }

   public Enumeration getAllowedMethods() {
      this.checkUsed();
      return this.methodsAllowed.elements();
   }

   protected void processResponseHeaders(HttpState state, HttpConnection conn) {
      LOG.trace("enter OptionsMethod.processResponseHeaders(HttpState, HttpConnection)");
      Header allowHeader = this.getResponseHeader("allow");
      if (allowHeader != null) {
         String allowHeaderValue = allowHeader.getValue();
         StringTokenizer tokenizer = new StringTokenizer(allowHeaderValue, ",");

         while(tokenizer.hasMoreElements()) {
            String methodAllowed = tokenizer.nextToken().trim().toUpperCase();
            this.methodsAllowed.addElement(methodAllowed);
         }
      }

   }

   public boolean needContentLength() {
      return false;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$OptionsMethod == null ? (class$org$apache$commons$httpclient$methods$OptionsMethod = class$("org.apache.commons.httpclient.methods.OptionsMethod")) : class$org$apache$commons$httpclient$methods$OptionsMethod);
   }
}
