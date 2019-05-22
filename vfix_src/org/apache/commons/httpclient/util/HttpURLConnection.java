package org.apache.commons.httpclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Permission;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpURLConnection extends java.net.HttpURLConnection {
   private static final Log LOG;
   private HttpMethod method;
   private URL url;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$util$HttpURLConnection;

   public HttpURLConnection(HttpMethod method, URL url) {
      super(url);
      this.method = method;
      this.url = url;
   }

   protected HttpURLConnection(URL url) {
      super(url);
      throw new RuntimeException("An HTTP URL connection can only be constructed from a HttpMethod class");
   }

   public InputStream getInputStream() throws IOException {
      LOG.trace("enter HttpURLConnection.getInputStream()");
      return this.method.getResponseBodyAsStream();
   }

   public InputStream getErrorStream() {
      LOG.trace("enter HttpURLConnection.getErrorStream()");
      throw new RuntimeException("Not implemented yet");
   }

   public void disconnect() {
      LOG.trace("enter HttpURLConnection.disconnect()");
      throw new RuntimeException("Not implemented yet");
   }

   public void connect() throws IOException {
      LOG.trace("enter HttpURLConnection.connect()");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public boolean usingProxy() {
      LOG.trace("enter HttpURLConnection.usingProxy()");
      throw new RuntimeException("Not implemented yet");
   }

   public String getRequestMethod() {
      LOG.trace("enter HttpURLConnection.getRequestMethod()");
      return this.method.getName();
   }

   public int getResponseCode() throws IOException {
      LOG.trace("enter HttpURLConnection.getResponseCode()");
      return this.method.getStatusCode();
   }

   public String getResponseMessage() throws IOException {
      LOG.trace("enter HttpURLConnection.getResponseMessage()");
      return this.method.getStatusText();
   }

   public String getHeaderField(String name) {
      LOG.trace("enter HttpURLConnection.getHeaderField(String)");
      Header[] headers = this.method.getResponseHeaders();

      for(int i = headers.length - 1; i >= 0; --i) {
         if (headers[i].getName().equalsIgnoreCase(name)) {
            return headers[i].getValue();
         }
      }

      return null;
   }

   public String getHeaderFieldKey(int keyPosition) {
      LOG.trace("enter HttpURLConnection.getHeaderFieldKey(int)");
      if (keyPosition == 0) {
         return null;
      } else {
         Header[] headers = this.method.getResponseHeaders();
         return keyPosition >= 0 && keyPosition <= headers.length ? headers[keyPosition - 1].getName() : null;
      }
   }

   public String getHeaderField(int position) {
      LOG.trace("enter HttpURLConnection.getHeaderField(int)");
      if (position == 0) {
         return this.method.getStatusLine().toString();
      } else {
         Header[] headers = this.method.getResponseHeaders();
         return position >= 0 && position <= headers.length ? headers[position - 1].getValue() : null;
      }
   }

   public URL getURL() {
      LOG.trace("enter HttpURLConnection.getURL()");
      return this.url;
   }

   public void setInstanceFollowRedirects(boolean isFollowingRedirects) {
      LOG.trace("enter HttpURLConnection.setInstanceFollowRedirects(boolean)");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public boolean getInstanceFollowRedirects() {
      LOG.trace("enter HttpURLConnection.getInstanceFollowRedirects()");
      throw new RuntimeException("Not implemented yet");
   }

   public void setRequestMethod(String method) throws ProtocolException {
      LOG.trace("enter HttpURLConnection.setRequestMethod(String)");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public Permission getPermission() throws IOException {
      LOG.trace("enter HttpURLConnection.getPermission()");
      throw new RuntimeException("Not implemented yet");
   }

   public Object getContent() throws IOException {
      LOG.trace("enter HttpURLConnection.getContent()");
      throw new RuntimeException("Not implemented yet");
   }

   public Object getContent(Class[] classes) throws IOException {
      LOG.trace("enter HttpURLConnection.getContent(Class[])");
      throw new RuntimeException("Not implemented yet");
   }

   public OutputStream getOutputStream() throws IOException {
      LOG.trace("enter HttpURLConnection.getOutputStream()");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public void setDoInput(boolean isInput) {
      LOG.trace("enter HttpURLConnection.setDoInput()");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public boolean getDoInput() {
      LOG.trace("enter HttpURLConnection.getDoInput()");
      throw new RuntimeException("Not implemented yet");
   }

   public void setDoOutput(boolean isOutput) {
      LOG.trace("enter HttpURLConnection.setDoOutput()");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public boolean getDoOutput() {
      LOG.trace("enter HttpURLConnection.getDoOutput()");
      throw new RuntimeException("Not implemented yet");
   }

   public void setAllowUserInteraction(boolean isAllowInteraction) {
      LOG.trace("enter HttpURLConnection.setAllowUserInteraction(boolean)");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public boolean getAllowUserInteraction() {
      LOG.trace("enter HttpURLConnection.getAllowUserInteraction()");
      throw new RuntimeException("Not implemented yet");
   }

   public void setUseCaches(boolean isUsingCaches) {
      LOG.trace("enter HttpURLConnection.setUseCaches(boolean)");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public boolean getUseCaches() {
      LOG.trace("enter HttpURLConnection.getUseCaches()");
      throw new RuntimeException("Not implemented yet");
   }

   public void setIfModifiedSince(long modificationDate) {
      LOG.trace("enter HttpURLConnection.setIfModifiedSince(long)");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public long getIfModifiedSince() {
      LOG.trace("enter HttpURLConnection.getIfmodifiedSince()");
      throw new RuntimeException("Not implemented yet");
   }

   public boolean getDefaultUseCaches() {
      LOG.trace("enter HttpURLConnection.getDefaultUseCaches()");
      throw new RuntimeException("Not implemented yet");
   }

   public void setDefaultUseCaches(boolean isUsingCaches) {
      LOG.trace("enter HttpURLConnection.setDefaultUseCaches(boolean)");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public void setRequestProperty(String key, String value) {
      LOG.trace("enter HttpURLConnection.setRequestProperty()");
      throw new RuntimeException("This class can only be used with alreadyretrieved data");
   }

   public String getRequestProperty(String key) {
      LOG.trace("enter HttpURLConnection.getRequestProperty()");
      throw new RuntimeException("Not implemented yet");
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$util$HttpURLConnection == null ? (class$org$apache$commons$httpclient$util$HttpURLConnection = class$("org.apache.commons.httpclient.util.HttpURLConnection")) : class$org$apache$commons$httpclient$util$HttpURLConnection);
   }
}
