package org.apache.commons.httpclient.methods;

import java.net.MalformedURLException;
import org.apache.commons.httpclient.HttpUrlMethod;
import org.apache.commons.httpclient.util.URIUtil;

/** @deprecated */
public class UrlHeadMethod extends HeadMethod implements HttpUrlMethod {
   private String url;

   public UrlHeadMethod() {
   }

   public UrlHeadMethod(String url) throws MalformedURLException {
      super(URIUtil.getPath(url));
      this.setUrl(url);
   }

   public void setUrl(String url) throws MalformedURLException {
      super.setPath(URIUtil.getPath(url));
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
