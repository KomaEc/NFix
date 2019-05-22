package org.apache.commons.httpclient.methods;

import java.net.MalformedURLException;
import org.apache.commons.httpclient.HttpUrlMethod;
import org.apache.commons.httpclient.util.URIUtil;

/** @deprecated */
public class UrlPutMethod extends PutMethod implements HttpUrlMethod {
   private String url;

   public UrlPutMethod() {
   }

   public UrlPutMethod(String url) throws MalformedURLException {
      super(URIUtil.getPath(url));
      this.setUrl(url);
   }

   public void setUrl(String url) throws MalformedURLException {
      this.setPath(URIUtil.getPath(url));
      this.url = url;
      String query = URIUtil.getQuery(url);
      if (query != null && query.length() > 0) {
         super.setQueryString(query);
      }

   }

   public String getUrl() {
      return this.url;
   }
}
