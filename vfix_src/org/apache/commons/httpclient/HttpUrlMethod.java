package org.apache.commons.httpclient;

import java.net.MalformedURLException;

/** @deprecated */
public interface HttpUrlMethod extends HttpMethod {
   void setUrl(String var1) throws MalformedURLException;

   String getUrl();
}
