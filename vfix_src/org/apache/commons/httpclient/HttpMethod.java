package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;

public interface HttpMethod {
   String getName();

   HostConfiguration getHostConfiguration();

   void setPath(String var1);

   String getPath();

   URI getURI() throws URIException;

   void setStrictMode(boolean var1);

   boolean isStrictMode();

   void setRequestHeader(String var1, String var2);

   void setRequestHeader(Header var1);

   void addRequestHeader(String var1, String var2);

   void addRequestHeader(Header var1);

   Header getRequestHeader(String var1);

   void removeRequestHeader(String var1);

   boolean getFollowRedirects();

   void setFollowRedirects(boolean var1);

   void setQueryString(String var1);

   void setQueryString(NameValuePair[] var1);

   String getQueryString();

   Header[] getRequestHeaders();

   boolean validate();

   int getStatusCode();

   String getStatusText();

   Header[] getResponseHeaders();

   Header getResponseHeader(String var1);

   Header[] getResponseFooters();

   Header getResponseFooter(String var1);

   byte[] getResponseBody();

   String getResponseBodyAsString();

   InputStream getResponseBodyAsStream() throws IOException;

   boolean hasBeenUsed();

   int execute(HttpState var1, HttpConnection var2) throws HttpException, IOException;

   /** @deprecated */
   void recycle();

   void releaseConnection();

   void addResponseFooter(Header var1);

   StatusLine getStatusLine();

   boolean getDoAuthentication();

   void setDoAuthentication(boolean var1);
}
