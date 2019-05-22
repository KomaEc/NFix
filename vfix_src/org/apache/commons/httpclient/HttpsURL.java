package org.apache.commons.httpclient;

import org.apache.commons.httpclient.util.URIUtil;

public class HttpsURL extends HttpURL {
   public static final char[] DEFAULT_SCHEME = new char[]{'h', 't', 't', 'p', 's'};
   /** @deprecated */
   public static final char[] _default_scheme;
   public static final int DEFAULT_PORT = 443;
   /** @deprecated */
   public static final int _default_port = 443;
   static final long serialVersionUID = 887844277028676648L;

   protected HttpsURL() {
   }

   public HttpsURL(char[] escaped, String charset) throws URIException, NullPointerException {
      super.protocolCharset = charset;
      this.parseUriReference(new String(escaped), true);
      this.checkValid();
   }

   public HttpsURL(char[] escaped) throws URIException, NullPointerException {
      this.parseUriReference(new String(escaped), true);
      this.checkValid();
   }

   public HttpsURL(String original, String charset) throws URIException {
      super.protocolCharset = charset;
      this.parseUriReference(original, false);
      this.checkValid();
   }

   public HttpsURL(String original) throws URIException {
      this.parseUriReference(original, false);
      this.checkValid();
   }

   public HttpsURL(String host, int port, String path) throws URIException {
      this((String)null, host, port, path, (String)null, (String)null);
   }

   public HttpsURL(String host, int port, String path, String query) throws URIException {
      this((String)null, host, port, path, query, (String)null);
   }

   public HttpsURL(String user, String password, String host) throws URIException {
      this(user, password, host, -1, (String)null, (String)null, (String)null);
   }

   public HttpsURL(String user, String password, String host, int port) throws URIException {
      this(user, password, host, port, (String)null, (String)null, (String)null);
   }

   public HttpsURL(String user, String password, String host, int port, String path) throws URIException {
      this(user, password, host, port, path, (String)null, (String)null);
   }

   public HttpsURL(String user, String password, String host, int port, String path, String query) throws URIException {
      this(user, password, host, port, path, query, (String)null);
   }

   public HttpsURL(String host, String path, String query, String fragment) throws URIException {
      this((String)null, host, -1, path, query, fragment);
   }

   public HttpsURL(String userinfo, String host, String path, String query, String fragment) throws URIException {
      this(userinfo, host, -1, path, query, fragment);
   }

   public HttpsURL(String userinfo, String host, int port, String path) throws URIException {
      this(userinfo, host, port, path, (String)null, (String)null);
   }

   public HttpsURL(String userinfo, String host, int port, String path, String query) throws URIException {
      this(userinfo, host, port, path, query, (String)null);
   }

   public HttpsURL(String userinfo, String host, int port, String path, String query, String fragment) throws URIException {
      StringBuffer buff = new StringBuffer();
      if (userinfo != null || host != null || port != -1) {
         super._scheme = DEFAULT_SCHEME;
         buff.append(_default_scheme);
         buff.append("://");
         if (userinfo != null) {
            buff.append(userinfo);
            buff.append('@');
         }

         if (host != null) {
            buff.append(URIUtil.encode(host, URI.allowed_host));
            if (port != -1 || port != 443) {
               buff.append(':');
               buff.append(port);
            }
         }
      }

      if (path != null) {
         if (URI.scheme != null && !path.startsWith("/")) {
            throw new URIException(1, "abs_path requested");
         }

         buff.append(URIUtil.encode(path, URI.allowed_abs_path));
      }

      if (query != null) {
         buff.append('?');
         buff.append(URIUtil.encode(query, URI.allowed_query));
      }

      if (fragment != null) {
         buff.append('#');
         buff.append(URIUtil.encode(fragment, URI.allowed_fragment));
      }

      this.parseUriReference(buff.toString(), true);
      this.checkValid();
   }

   public HttpsURL(String user, String password, String host, int port, String path, String query, String fragment) throws URIException {
      this(HttpURL.toUserinfo(user, password), host, port, path, query, fragment);
   }

   public HttpsURL(HttpsURL base, String relative) throws URIException {
      this(base, new HttpsURL(relative));
   }

   public HttpsURL(HttpsURL base, HttpsURL relative) throws URIException {
      super((HttpURL)base, (HttpURL)relative);
      this.checkValid();
   }

   public char[] getRawScheme() {
      return super._scheme == null ? null : DEFAULT_SCHEME;
   }

   public String getScheme() {
      return super._scheme == null ? null : new String(DEFAULT_SCHEME);
   }

   public int getPort() {
      return super._port == -1 ? 443 : super._port;
   }

   protected void checkValid() throws URIException {
      if (!this.equals(super._scheme, DEFAULT_SCHEME) && super._scheme != null) {
         throw new URIException(1, "wrong class use");
      }
   }

   static {
      _default_scheme = DEFAULT_SCHEME;
   }
}
