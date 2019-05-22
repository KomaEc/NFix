package com.mks.connect;

import com.mks.api.IntegrationPointFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

class HttpBlimpInputStream extends BlimpInputStream {
   private HttpCmdRunnerImpl cmdRunner;
   private static final String MKS_APP_URI_PARAMETER = "&mksApp=";
   private HttpClient httpClient;
   private URI url;
   private Map connections = new HashMap(1);

   protected HttpBlimpInputStream(HttpCmdRunnerImpl cmdRunner, String[] cmd, HttpClient httpClient) {
      super(cmdRunner, cmd);
      this.cmdRunner = cmdRunner;
      this.httpClient = httpClient;
   }

   protected void blimpInterrupt(String appName) throws IOException {
      HttpClient hc = ((UserApplicationSessionImpl)this.cmdRunner.getSession()).createHttpClient();

      try {
         URI uri = this.getSessionURI(appName);
         GetMethod method = new GetMethod(uri.getPath());

         try {
            this.setupRequest(method);
            method.setRequestHeader(UserApplicationSessionImpl.OUT_OF_BAND_MESSAGE);
            method.setQueryString(uri.getQuery());
            UserApplicationSessionImpl.handleHTTPResponse(hc, method);
         } finally {
            method.releaseConnection();
         }
      } catch (ConnectException var9) {
         this.cleanup();
      }

   }

   public boolean isFinished() {
      return super.isFinished() || this.url == null;
   }

   private URI getSessionURI(String appName) throws IOException {
      if (this.url == null) {
         try {
            URI uri = this.cmdRunner.getSessionURI();
            this.url = new URI(uri.getURI() + "&mksApp=" + appName);
         } catch (URIException var3) {
            throw new BlimpException(var3);
         }
      }

      return this.url;
   }

   private void invalidateURI() {
      this.cmdRunner.invalidateURI();
      this.url = null;
   }

   protected InputStream blimpInitiate(String appName) throws IOException {
      InputStream input = null;
      GetMethod method = null;

      try {
         URI uri = this.getSessionURI(appName);
         method = new GetMethod(uri.getPath());

         try {
            this.setupRequest(method);
            method.setQueryString(uri.getQuery());
            UserApplicationSessionImpl.handleHTTPResponse(this.httpClient, method);
            this.checkStatusCode(method);
            input = method.getResponseBodyAsStream();
            this.connections.put(input, method);
            return input;
         } catch (InvalidSessionException var7) {
            throw var7;
         } catch (IOException var8) {
            method.releaseConnection();
            throw var8;
         }
      } catch (InvalidSessionException var9) {
         if (input != null) {
            try {
               input.close();
            } catch (IOException var6) {
               IntegrationPointFactory.getLogger().exception(var6);
            }
         }

         input = null;
         if (method != null) {
            method.releaseConnection();
         }

         this.invalidateURI();
         if (!this.cmdRunner.getAutoReconnect()) {
            throw var9;
         } else {
            return this.blimpInitiate(appName);
         }
      }
   }

   protected void blimpTerminate(InputStream stream) throws IOException {
      while(true) {
         try {
            try {
               if (stream.read() != -1) {
                  continue;
               }
            } finally {
               stream.close();
            }
         } finally {
            HttpMethodBase method = (HttpMethodBase)this.connections.get(stream);
            if (method != null) {
               this.connections.remove(stream);
               method.releaseConnection();
            }

         }

         return;
      }
   }

   protected void setupRequest(HttpMethodBase method) {
      ((UserApplicationSessionImpl)this.cmdRunner.getSession()).setupRequest(method);
      method.setFollowRedirects(false);
      method.setRequestHeader("enableAdvancedFeatures", String.valueOf(this.generateSubRtns));
   }

   protected InputStream blimpResponse(InputStream response) throws IOException {
      InputStream input = null;

      try {
         URI uri = this.cmdRunner.getSessionURI();
         PostMethod method = new PostMethod(uri.getPath());

         try {
            this.setupRequest(method);
            method.setQueryString(uri.getQuery());
            method.setRequestBody(response);
            if (((UserApplicationSessionImpl)this.cmdRunner.getSession()).supportsChunking()) {
               method.setRequestContentLength(-1);
            }

            UserApplicationSessionImpl.handleHTTPResponse(this.httpClient, method);
            input = method.getResponseBodyAsStream();
            this.checkStatusCode(method);
            this.connections.put(input, method);
            return input;
         } catch (IOException var6) {
            method.releaseConnection();
            throw var6;
         }
      } catch (ConnectException var7) {
         this.cleanup();
         throw var7;
      }
   }

   private void checkStatusCode(HttpMethod method) throws IOException {
      if (method.getStatusCode() == 503) {
         this.cleanup();
         throw new InvalidAppException(MessageFormat.format("Invalid App Name: {0}", method.getStatusText()));
      } else if (method.getStatusCode() == 410) {
         this.cleanup();
         throw new InvalidSessionException();
      } else if (method.getStatusCode() == 412) {
         this.cleanup();
         String msg = method.getResponseBodyAsString();
         throw new VersionMismatchException(msg);
      } else if (method.getStatusCode() != 200) {
         this.cleanup();
         throw new BlimpException(MessageFormat.format("Unexpected HTTP status: {0}", method.getStatusText()));
      }
   }
}
