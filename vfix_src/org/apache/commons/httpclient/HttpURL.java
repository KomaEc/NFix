package org.apache.commons.httpclient;

import org.apache.commons.httpclient.util.URIUtil;

public class HttpURL extends URI {
   public static final char[] DEFAULT_SCHEME = new char[]{'h', 't', 't', 'p'};
   /** @deprecated */
   public static final char[] _default_scheme;
   public static final int DEFAULT_PORT = 80;
   /** @deprecated */
   public static final int _default_port = 80;
   static final long serialVersionUID = -7158031098595039459L;

   protected HttpURL() {
   }

   public HttpURL(char[] escaped, String charset) throws URIException, NullPointerException {
      super.protocolCharset = charset;
      this.parseUriReference(new String(escaped), true);
      this.checkValid();
   }

   public HttpURL(char[] escaped) throws URIException, NullPointerException {
      this.parseUriReference(new String(escaped), true);
      this.checkValid();
   }

   public HttpURL(String original, String charset) throws URIException {
      super.protocolCharset = charset;
      this.parseUriReference(original, false);
      this.checkValid();
   }

   public HttpURL(String original) throws URIException {
      this.parseUriReference(original, false);
      this.checkValid();
   }

   public HttpURL(String host, int port, String path) throws URIException {
      this((String)null, (String)null, host, port, path, (String)null, (String)null);
   }

   public HttpURL(String host, int port, String path, String query) throws URIException {
      this((String)null, (String)null, host, port, path, query, (String)null);
   }

   public HttpURL(String user, String password, String host) throws URIException {
      this(user, password, host, -1, (String)null, (String)null, (String)null);
   }

   public HttpURL(String user, String password, String host, int port) throws URIException {
      this(user, password, host, port, (String)null, (String)null, (String)null);
   }

   public HttpURL(String user, String password, String host, int port, String path) throws URIException {
      this(user, password, host, port, path, (String)null, (String)null);
   }

   public HttpURL(String user, String password, String host, int port, String path, String query) throws URIException {
      this(user, password, host, port, path, query, (String)null);
   }

   public HttpURL(String host, String path, String query, String fragment) throws URIException {
      this((String)null, (String)null, host, -1, path, query, fragment);
   }

   public HttpURL(String userinfo, String host, String path, String query, String fragment) throws URIException {
      this(userinfo, host, -1, path, query, fragment);
   }

   public HttpURL(String userinfo, String host, int port, String path) throws URIException {
      this(userinfo, host, port, path, (String)null, (String)null);
   }

   public HttpURL(String userinfo, String host, int port, String path, String query) throws URIException {
      this(userinfo, host, port, path, query, (String)null);
   }

   public HttpURL(String userinfo, String host, int port, String path, String query, String fragment) throws URIException {
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
            if (port != -1 || port != 80) {
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

   public HttpURL(String user, String password, String host, int port, String path, String query, String fragment) throws URIException {
      this(toUserinfo(user, password), host, port, path, query, fragment);
   }

   protected static String toUserinfo(String user, String password) throws URIException {
      if (user == null) {
         return null;
      } else {
         StringBuffer usrinfo = new StringBuffer(20);
         usrinfo.append(URIUtil.encode(user, URI.allowed_within_userinfo));
         if (password == null) {
            return usrinfo.toString();
         } else {
            usrinfo.append(':');
            usrinfo.append(URIUtil.encode(password, URI.allowed_within_userinfo));
            return usrinfo.toString();
         }
      }
   }

   public HttpURL(HttpURL base, String relative) throws URIException {
      this(base, new HttpURL(relative));
   }

   public HttpURL(HttpURL base, HttpURL relative) throws URIException {
      super((URI)base, (URI)relative);
      this.checkValid();
   }

   public char[] getRawScheme() {
      return super._scheme == null ? null : DEFAULT_SCHEME;
   }

   public String getScheme() {
      return super._scheme == null ? null : new String(DEFAULT_SCHEME);
   }

   public int getPort() {
      return super._port == -1 ? 80 : super._port;
   }

   public void setRawUserinfo(char[] escapedUser, char[] escapedPassword) throws URIException {
      if (escapedUser != null && escapedUser.length != 0) {
         if (this.validate(escapedUser, URI.within_userinfo) && (escapedPassword == null || this.validate(escapedPassword, URI.within_userinfo))) {
            String username = new String(escapedUser);
            String password = escapedPassword == null ? null : new String(escapedPassword);
            String userinfo = username + (password == null ? "" : ":" + password);
            String hostname = new String(this.getRawHost());
            String hostport = super._port == -1 ? hostname : hostname + ":" + super._port;
            String authority = userinfo + "@" + hostport;
            super._userinfo = userinfo.toCharArray();
            super._authority = authority.toCharArray();
            this.setURI();
         } else {
            throw new URIException(3, "escaped userinfo not valid");
         }
      } else {
         throw new URIException(1, "user required");
      }
   }

   public void setEscapedUserinfo(String escapedUser, String escapedPassword) throws URIException, NullPointerException {
      this.setRawUserinfo(escapedUser.toCharArray(), escapedPassword == null ? null : escapedPassword.toCharArray());
   }

   public void setUserinfo(String user, String password) throws URIException, NullPointerException {
      String charset = this.getProtocolCharset();
      this.setRawUserinfo(URI.encode(user, URI.within_userinfo, charset), password == null ? null : URI.encode(password, URI.within_userinfo, charset));
   }

   public void setRawUser(char[] escapedUser) throws URIException {
      if (escapedUser != null && escapedUser.length != 0) {
         if (!this.validate(escapedUser, URI.within_userinfo)) {
            throw new URIException(3, "escaped user not valid");
         } else {
            String username = new String(escapedUser);
            String password = new String(this.getRawPassword());
            String userinfo = username + (password == null ? "" : ":" + password);
            String hostname = new String(this.getRawHost());
            String hostport = super._port == -1 ? hostname : hostname + ":" + super._port;
            String authority = userinfo + "@" + hostport;
            super._userinfo = userinfo.toCharArray();
            super._authority = authority.toCharArray();
            this.setURI();
         }
      } else {
         throw new URIException(1, "user required");
      }
   }

   public void setEscapedUser(String escapedUser) throws URIException, NullPointerException {
      this.setRawUser(escapedUser.toCharArray());
   }

   public void setUser(String user) throws URIException, NullPointerException {
      this.setRawUser(URI.encode(user, URI.allowed_within_userinfo, this.getProtocolCharset()));
   }

   public char[] getRawUser() {
      if (super._userinfo != null && super._userinfo.length != 0) {
         int to = this.indexFirstOf(super._userinfo, ':');
         if (to == -1) {
            return super._userinfo;
         } else {
            char[] result = new char[to];
            System.arraycopy(super._userinfo, 0, result, 0, to);
            return result;
         }
      } else {
         return null;
      }
   }

   public String getEscapedUser() {
      char[] user = this.getRawUser();
      return user == null ? null : new String(user);
   }

   public String getUser() throws URIException {
      char[] user = this.getRawUser();
      return user == null ? null : URI.decode(user, this.getProtocolCharset());
   }

   public void setRawPassword(char[] escapedPassword) throws URIException {
      if (escapedPassword != null && !this.validate(escapedPassword, URI.within_userinfo)) {
         throw new URIException(3, "escaped password not valid");
      } else if (this.getRawUser() != null && this.getRawUser().length != 0) {
         String username = new String(this.getRawUser());
         String password = new String(escapedPassword);
         String userinfo = username + (password == null ? "" : ":" + password);
         String hostname = new String(this.getRawHost());
         String hostport = super._port == -1 ? hostname : hostname + ":" + super._port;
         String authority = userinfo + "@" + hostport;
         super._userinfo = userinfo.toCharArray();
         super._authority = authority.toCharArray();
         this.setURI();
      } else {
         throw new URIException(1, "username required");
      }
   }

   public void setEscapedPassword(String escapedPassword) throws URIException {
      this.setRawPassword(escapedPassword == null ? null : escapedPassword.toCharArray());
   }

   public void setPassword(String password) throws URIException {
      this.setRawPassword(password == null ? null : URI.encode(password, URI.allowed_within_userinfo, this.getProtocolCharset()));
   }

   public char[] getRawPassword() {
      int from = this.indexFirstOf(super._userinfo, ':');
      if (from == -1) {
         return null;
      } else {
         int len = super._userinfo.length - from - 1;
         char[] result = new char[len];
         System.arraycopy(super._userinfo, from + 1, result, 0, len);
         return result;
      }
   }

   public String getEscapedPassword() {
      char[] password = this.getRawPassword();
      return password == null ? null : new String(password);
   }

   public String getPassword() throws URIException {
      char[] password = this.getRawPassword();
      return password == null ? null : URI.decode(password, this.getProtocolCharset());
   }

   public char[] getRawCurrentHierPath() throws URIException {
      return super._path != null && super._path.length != 0 ? super.getRawCurrentHierPath(super._path) : URI.rootPath;
   }

   public char[] getRawAboveHierPath() throws URIException {
      char[] path = this.getRawCurrentHierPath();
      return path != null && path.length != 0 ? this.getRawCurrentHierPath(path) : URI.rootPath;
   }

   public char[] getRawPath() {
      char[] path = super.getRawPath();
      return path != null && path.length != 0 ? path : URI.rootPath;
   }

   public void setQuery(String queryName, String queryValue) throws URIException, NullPointerException {
      StringBuffer buff = new StringBuffer();
      String charset = this.getProtocolCharset();
      buff.append(URI.encode(queryName, URI.allowed_within_query, charset));
      buff.append('=');
      buff.append(URI.encode(queryValue, URI.allowed_within_query, charset));
      super._query = buff.toString().toCharArray();
      this.setURI();
   }

   public void setQuery(String[] queryName, String[] queryValue) throws URIException, NullPointerException {
      int length = queryName.length;
      if (length != queryValue.length) {
         throw new URIException("wrong array size of query");
      } else {
         StringBuffer buff = new StringBuffer();
         String charset = this.getProtocolCharset();

         for(int i = 0; i < length; ++i) {
            buff.append(URI.encode(queryName[i], URI.allowed_within_query, charset));
            buff.append('=');
            buff.append(URI.encode(queryValue[i], URI.allowed_within_query, charset));
            if (i + 1 < length) {
               buff.append('&');
            }
         }

         super._query = buff.toString().toCharArray();
         this.setURI();
      }
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
