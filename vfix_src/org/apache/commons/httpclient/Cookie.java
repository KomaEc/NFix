package org.apache.commons.httpclient;

import java.io.Serializable;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Cookie extends NameValuePair implements Serializable, Comparator {
   private String cookieComment;
   private String cookieDomain;
   private Date cookieExpiryDate;
   private String cookiePath;
   private boolean isSecure;
   private boolean hasPathAttribute;
   private boolean hasDomainAttribute;
   private int cookieVersion;
   private static final RuleBasedCollator STRING_COLLATOR = (RuleBasedCollator)Collator.getInstance(new Locale("en", "US", ""));
   private static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$Cookie;

   public Cookie() {
      this((String)null, "noname", (String)null, (String)null, (Date)null, false);
   }

   public Cookie(String domain, String name, String value) {
      this(domain, name, value, (String)null, (Date)null, false);
   }

   public Cookie(String domain, String name, String value, String path, Date expires, boolean secure) {
      super(name, value);
      this.hasPathAttribute = false;
      this.hasDomainAttribute = false;
      this.cookieVersion = 0;
      LOG.trace("enter Cookie(String, String, String, String, Date, boolean)");
      if (name == null) {
         throw new IllegalArgumentException("Cookie name may not be null");
      } else if (name.trim().equals("")) {
         throw new IllegalArgumentException("Cookie name may not be blank");
      } else {
         this.setPath(path);
         this.setDomain(domain);
         this.setExpiryDate(expires);
         this.setSecure(secure);
      }
   }

   public Cookie(String domain, String name, String value, String path, int maxAge, boolean secure) {
      this(domain, name, value, path, (Date)null, secure);
      if (maxAge < -1) {
         throw new IllegalArgumentException("Invalid max age:  " + Integer.toString(maxAge));
      } else {
         if (maxAge >= 0) {
            this.setExpiryDate(new Date(System.currentTimeMillis() + (long)maxAge * 1000L));
         }

      }
   }

   public String getComment() {
      return this.cookieComment;
   }

   public void setComment(String comment) {
      this.cookieComment = comment;
   }

   public Date getExpiryDate() {
      return this.cookieExpiryDate;
   }

   public void setExpiryDate(Date expiryDate) {
      this.cookieExpiryDate = expiryDate;
   }

   public boolean isPersistent() {
      return null != this.cookieExpiryDate;
   }

   public String getDomain() {
      return this.cookieDomain;
   }

   public void setDomain(String domain) {
      if (domain != null) {
         int ndx = domain.indexOf(":");
         if (ndx != -1) {
            domain = domain.substring(0, ndx);
         }

         this.cookieDomain = domain.toLowerCase();
      }

   }

   public String getPath() {
      return this.cookiePath;
   }

   public void setPath(String path) {
      this.cookiePath = path;
   }

   public boolean getSecure() {
      return this.isSecure;
   }

   public void setSecure(boolean secure) {
      this.isSecure = secure;
   }

   public int getVersion() {
      return this.cookieVersion;
   }

   public void setVersion(int version) {
      this.cookieVersion = version;
   }

   public boolean isExpired() {
      return this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= System.currentTimeMillis();
   }

   public boolean isExpired(Date now) {
      return this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= now.getTime();
   }

   public void setPathAttributeSpecified(boolean value) {
      this.hasPathAttribute = value;
   }

   public boolean isPathAttributeSpecified() {
      return this.hasPathAttribute;
   }

   public void setDomainAttributeSpecified(boolean value) {
      this.hasDomainAttribute = value;
   }

   public boolean isDomainAttributeSpecified() {
      return this.hasDomainAttribute;
   }

   public int hashCode() {
      return super.hashCode() ^ (null == this.cookiePath ? 0 : this.cookiePath.hashCode()) ^ (null == this.cookieDomain ? 0 : this.cookieDomain.hashCode());
   }

   public boolean equals(Object obj) {
      LOG.trace("enter Cookie.equals(Object)");
      if (obj != null && obj instanceof Cookie) {
         Cookie that = (Cookie)obj;
         return (null == this.getName() ? null == that.getName() : this.getName().equals(that.getName())) && (null == this.getPath() ? null == that.getPath() : this.getPath().equals(that.getPath())) && (null == this.getDomain() ? null == that.getDomain() : this.getDomain().equals(that.getDomain()));
      } else {
         return false;
      }
   }

   public String toExternalForm() {
      return CookiePolicy.getSpecByVersion(this.getVersion()).formatCookie(this);
   }

   /** @deprecated */
   public boolean matches(String domain, int port, String path, boolean secure, Date date) {
      LOG.trace("enter Cookie.matches(Strinng, int, String, boolean, Date");
      CookieSpec matcher = CookiePolicy.getDefaultSpec();
      return matcher.match(domain, port, path, secure, this);
   }

   /** @deprecated */
   public boolean matches(String domain, int port, String path, boolean secure) {
      LOG.trace("enter Cookie.matches(String, int, String, boolean");
      return this.matches(domain, port, path, secure, new Date());
   }

   /** @deprecated */
   public static Header createCookieHeader(String domain, String path, Cookie[] cookies) {
      LOG.trace("enter Cookie.createCookieHeader(String,String,Cookie[])");
      return createCookieHeader(domain, path, false, cookies);
   }

   /** @deprecated */
   public static Header createCookieHeader(String domain, String path, boolean secure, Cookie[] cookies) throws IllegalArgumentException {
      LOG.trace("enter Cookie.createCookieHeader(String, String, boolean, Cookie[])");
      if (domain == null) {
         throw new IllegalArgumentException("null domain in createCookieHeader.");
      } else {
         int port = secure ? 443 : 80;
         int ndx = domain.indexOf(":");
         if (ndx != -1) {
            try {
               port = Integer.parseInt(domain.substring(ndx + 1, domain.length()));
            } catch (NumberFormatException var7) {
               LOG.warn("Cookie.createCookieHeader():  Invalid port number in domain " + domain);
            }
         }

         return createCookieHeader(domain, port, path, secure, cookies);
      }
   }

   /** @deprecated */
   public static Header createCookieHeader(String domain, int port, String path, boolean secure, Cookie[] cookies) throws IllegalArgumentException {
      LOG.trace("enter Cookie.createCookieHeader(String, int, String, boolean, Cookie[])");
      return createCookieHeader(domain, port, path, secure, new Date(), cookies);
   }

   /** @deprecated */
   public static Header createCookieHeader(String domain, int port, String path, boolean secure, Date now, Cookie[] cookies) throws IllegalArgumentException {
      LOG.trace("enter Cookie.createCookieHeader(String, int, String, boolean, Date, Cookie[])");
      CookieSpec matcher = CookiePolicy.getDefaultSpec();
      cookies = matcher.match(domain, port, path, secure, cookies);
      return cookies != null && cookies.length > 0 ? matcher.formatCookieHeader(cookies) : null;
   }

   public int compare(Object o1, Object o2) {
      LOG.trace("enter Cookie.compare(Object, Object)");
      if (!(o1 instanceof Cookie)) {
         throw new ClassCastException(o1.getClass().getName());
      } else if (!(o2 instanceof Cookie)) {
         throw new ClassCastException(o2.getClass().getName());
      } else {
         Cookie c1 = (Cookie)o1;
         Cookie c2 = (Cookie)o2;
         if (c1.getPath() == null && c2.getPath() == null) {
            return 0;
         } else if (c1.getPath() == null) {
            return c2.getPath().equals("/") ? 0 : -1;
         } else if (c2.getPath() == null) {
            return c1.getPath().equals("/") ? 0 : 1;
         } else {
            return STRING_COLLATOR.compare(c1.getPath(), c2.getPath());
         }
      }
   }

   public String toString() {
      return this.toExternalForm();
   }

   /** @deprecated */
   public static Cookie[] parse(String domain, int port, String path, Header setCookie) throws HttpException, IllegalArgumentException {
      LOG.trace("enter Cookie.parse(String, int, String, Header)");
      return parse(domain, port, path, false, setCookie);
   }

   /** @deprecated */
   public static Cookie[] parse(String domain, String path, Header setCookie) throws HttpException, IllegalArgumentException {
      LOG.trace("enter Cookie.parse(String, String, Header)");
      return parse(domain, 80, path, false, setCookie);
   }

   /** @deprecated */
   public static Cookie[] parse(String domain, String path, boolean secure, Header setCookie) throws HttpException, IllegalArgumentException {
      LOG.trace("enter Cookie.parse(String, String, boolean, Header)");
      return parse(domain, secure ? 443 : 80, path, secure, setCookie);
   }

   /** @deprecated */
   public static Cookie[] parse(String domain, int port, String path, boolean secure, Header setCookie) throws HttpException {
      LOG.trace("enter Cookie.parse(String, int, String, boolean, Header)");
      CookieSpec parser = CookiePolicy.getDefaultSpec();
      Cookie[] cookies = parser.parse(domain, port, path, secure, setCookie);

      for(int i = 0; i < cookies.length; ++i) {
         Cookie cookie = cookies[i];
         CookieSpec validator = CookiePolicy.getSpecByVersion(cookie.getVersion());
         validator.validate(domain, port, path, secure, cookie);
      }

      return cookies;
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$Cookie == null ? (class$org$apache$commons$httpclient$Cookie = class$("org.apache.commons.httpclient.Cookie")) : class$org$apache$commons$httpclient$Cookie);
   }
}
