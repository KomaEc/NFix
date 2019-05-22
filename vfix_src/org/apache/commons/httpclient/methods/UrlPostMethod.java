package org.apache.commons.httpclient.methods;

import java.net.MalformedURLException;
import org.apache.commons.httpclient.HttpUrlMethod;
import org.apache.commons.httpclient.util.URIUtil;

/** @deprecated */
public class UrlPostMethod extends PostMethod implements HttpUrlMethod {
   private String url;

   public UrlPostMethod() {
   }

   public UrlPostMethod(String url) throws MalformedURLException {
      super(URIUtil.getPath(url));
      this.setUrl(url);
   }

   public UrlPostMethod(String url, String tempDir) throws MalformedURLException {
      super(URIUtil.getPath(url), tempDir);
      this.setUrl(url);
   }

   public UrlPostMethod(String url, String tempDir, String tempFile) throws MalformedURLException {
      super(URIUtil.getPath(url), tempDir, tempFile);
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
