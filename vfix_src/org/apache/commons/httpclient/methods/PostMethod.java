package org.apache.commons.httpclient.methods;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpConstants;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PostMethod extends EntityEnclosingMethod {
   private static final Log LOG;
   public static final String FORM_URL_ENCODED_CONTENT_TYPE = "application/x-www-form-urlencoded";
   private Vector params = new Vector();
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$methods$PostMethod;

   public PostMethod() {
   }

   public PostMethod(String uri) {
      super(uri);
   }

   /** @deprecated */
   public PostMethod(String uri, String tempDir) {
      super(uri, tempDir);
   }

   /** @deprecated */
   public PostMethod(String uri, String tempDir, String tempFile) {
      super(uri, tempDir, tempFile);
   }

   public String getName() {
      return "POST";
   }

   protected boolean hasRequestContent() {
      LOG.trace("enter PostMethod.hasRequestContent()");
      return !this.params.isEmpty() ? true : super.hasRequestContent();
   }

   protected void clearRequestBody() {
      LOG.trace("enter PostMethod.clearRequestBody()");
      this.params.clear();
      super.clearRequestBody();
   }

   protected byte[] generateRequestBody() {
      LOG.trace("enter PostMethod.renerateRequestBody()");
      if (!this.params.isEmpty()) {
         String content = EncodingUtil.formUrlEncode(this.getParameters(), this.getRequestCharSet());
         return HttpConstants.getContentBytes(content);
      } else {
         return super.generateRequestBody();
      }
   }

   public void setParameter(String parameterName, String parameterValue) {
      LOG.trace("enter PostMethod.setParameter(String, String)");
      this.removeParameter(parameterName);
      this.addParameter(parameterName, parameterValue);
   }

   public NameValuePair getParameter(String paramName) {
      LOG.trace("enter PostMethod.getParameter(String)");
      if (paramName == null) {
         return null;
      } else {
         Iterator iter = this.params.iterator();

         NameValuePair parameter;
         do {
            if (!iter.hasNext()) {
               return null;
            }

            parameter = (NameValuePair)iter.next();
         } while(!paramName.equals(parameter.getName()));

         return parameter;
      }
   }

   public NameValuePair[] getParameters() {
      LOG.trace("enter PostMethod.getParameters()");
      int numPairs = this.params.size();
      Object[] objectArr = this.params.toArray();
      NameValuePair[] nvPairArr = new NameValuePair[numPairs];

      for(int i = 0; i < numPairs; ++i) {
         nvPairArr[i] = (NameValuePair)objectArr[i];
      }

      return nvPairArr;
   }

   public void addParameter(String paramName, String paramValue) throws IllegalArgumentException {
      LOG.trace("enter PostMethod.addParameter(String, String)");
      if (paramName != null && paramValue != null) {
         super.clearRequestBody();
         this.params.add(new NameValuePair(paramName, paramValue));
      } else {
         throw new IllegalArgumentException("Arguments to addParameter(String, String) cannot be null");
      }
   }

   public void addParameter(NameValuePair param) throws IllegalArgumentException {
      LOG.trace("enter PostMethod.addParameter(NameValuePair)");
      if (param == null) {
         throw new IllegalArgumentException("NameValuePair may not be null");
      } else {
         this.addParameter(param.getName(), param.getValue());
      }
   }

   public void addParameters(NameValuePair[] parameters) {
      LOG.trace("enter PostMethod.addParameters(NameValuePair[])");
      if (parameters == null) {
         LOG.warn("Attempt to addParameters(null) ignored");
      } else {
         super.clearRequestBody();

         for(int i = 0; i < parameters.length; ++i) {
            this.params.add(parameters[i]);
         }
      }

   }

   public boolean removeParameter(String paramName) throws IllegalArgumentException {
      LOG.trace("enter PostMethod.removeParameter(String)");
      if (paramName == null) {
         throw new IllegalArgumentException("Argument passed to removeParameter(String) cannot be null");
      } else {
         boolean removed = false;
         Iterator iter = this.params.iterator();

         while(iter.hasNext()) {
            NameValuePair pair = (NameValuePair)iter.next();
            if (paramName.equals(pair.getName())) {
               iter.remove();
               removed = true;
            }
         }

         return removed;
      }
   }

   public boolean removeParameter(String paramName, String paramValue) throws IllegalArgumentException {
      LOG.trace("enter PostMethod.removeParameter(String, String)");
      if (paramName == null) {
         throw new IllegalArgumentException("Parameter name may not be null");
      } else if (paramValue == null) {
         throw new IllegalArgumentException("Parameter value may not be null");
      } else {
         Iterator iter = this.params.iterator();

         NameValuePair pair;
         do {
            if (!iter.hasNext()) {
               return false;
            }

            pair = (NameValuePair)iter.next();
         } while(!paramName.equals(pair.getName()) || !paramValue.equals(pair.getValue()));

         iter.remove();
         return true;
      }
   }

   public void setRequestBody(NameValuePair[] parametersBody) throws IllegalArgumentException {
      LOG.trace("enter PostMethod.setRequestBody(NameValuePair[])");
      if (parametersBody == null) {
         throw new IllegalArgumentException("Array of parameters may not be null");
      } else {
         this.clearRequestBody();
         this.addParameters(parametersBody);
      }
   }

   protected void addRequestHeaders(HttpState state, HttpConnection conn) throws IOException, HttpException {
      super.addRequestHeaders(state, conn);
      if (!this.params.isEmpty() && this.getRequestHeader("Content-Type") == null) {
         this.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$methods$PostMethod == null ? (class$org$apache$commons$httpclient$methods$PostMethod = class$("org.apache.commons.httpclient.methods.PostMethod")) : class$org$apache$commons$httpclient$methods$PostMethod);
   }
}
