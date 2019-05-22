package org.apache.commons.httpclient.cookie;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CookieSpecBase implements CookieSpec {
   protected static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$cookie$CookieSpec;

   public Cookie[] parse(String host, int port, String path, boolean secure, String header) throws MalformedCookieException {
      LOG.trace("enter CookieSpecBase.parse(String, port, path, boolean, Header)");
      if (host == null) {
         throw new IllegalArgumentException("Host of origin may not be null");
      } else if (host.trim().equals("")) {
         throw new IllegalArgumentException("Host of origin may not be blank");
      } else if (port < 0) {
         throw new IllegalArgumentException("Invalid port: " + port);
      } else if (path == null) {
         throw new IllegalArgumentException("Path of origin may not be null.");
      } else if (header == null) {
         throw new IllegalArgumentException("Header may not be null.");
      } else {
         if (path.trim().equals("")) {
            path = "/";
         }

         host = host.toLowerCase();
         HeaderElement[] headerElements = null;

         try {
            headerElements = HeaderElement.parse(header);
         } catch (HttpException var16) {
            throw new MalformedCookieException(var16.getMessage());
         }

         String defaultPath = path;
         int lastSlashIndex = path.lastIndexOf("/");
         if (lastSlashIndex >= 0) {
            if (lastSlashIndex == 0) {
               lastSlashIndex = 1;
            }

            defaultPath = path.substring(0, lastSlashIndex);
         }

         Cookie[] cookies = new Cookie[headerElements.length];

         for(int i = 0; i < headerElements.length; ++i) {
            HeaderElement headerelement = headerElements[i];
            Cookie cookie = null;

            try {
               cookie = new Cookie(host, headerelement.getName(), headerelement.getValue(), defaultPath, (Date)null, false);
            } catch (IllegalArgumentException var15) {
               throw new MalformedCookieException(var15.getMessage());
            }

            NameValuePair[] parameters = headerelement.getParameters();
            if (parameters != null) {
               for(int j = 0; j < parameters.length; ++j) {
                  this.parseAttribute(parameters[j], cookie);
               }
            }

            cookies[i] = cookie;
         }

         return cookies;
      }
   }

   public Cookie[] parse(String host, int port, String path, boolean secure, Header header) throws MalformedCookieException {
      LOG.trace("enter CookieSpecBase.parse(String, port, path, boolean, String)");
      if (header == null) {
         throw new IllegalArgumentException("Header may not be null.");
      } else {
         return this.parse(host, port, path, secure, header.getValue());
      }
   }

   public void parseAttribute(NameValuePair attribute, Cookie cookie) throws MalformedCookieException {
      if (attribute == null) {
         throw new IllegalArgumentException("Attribute may not be null.");
      } else if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null.");
      } else {
         String paramName = attribute.getName().toLowerCase();
         String paramValue = attribute.getValue();
         if (paramName.equals("path")) {
            if (paramValue == null || paramValue.trim().equals("")) {
               paramValue = "/";
            }

            cookie.setPath(paramValue);
            cookie.setPathAttributeSpecified(true);
         } else if (paramName.equals("domain")) {
            if (paramValue == null) {
               throw new MalformedCookieException("Missing value for domain attribute");
            }

            if (paramValue.trim().equals("")) {
               throw new MalformedCookieException("Blank value for domain attribute");
            }

            cookie.setDomain(paramValue);
            cookie.setDomainAttributeSpecified(true);
         } else if (paramName.equals("max-age")) {
            if (paramValue == null) {
               throw new MalformedCookieException("Missing value for max-age attribute");
            }

            int age;
            try {
               age = Integer.parseInt(paramValue);
            } catch (NumberFormatException var8) {
               throw new MalformedCookieException("Invalid max-age attribute: " + var8.getMessage());
            }

            cookie.setExpiryDate(new Date(System.currentTimeMillis() + (long)age * 1000L));
         } else if (paramName.equals("secure")) {
            cookie.setSecure(true);
         } else if (paramName.equals("comment")) {
            cookie.setComment(paramValue);
         } else if (paramName.equals("expires")) {
            if (paramValue == null) {
               throw new MalformedCookieException("Missing value for expires attribute");
            }

            if (paramValue.length() > 1 && paramValue.startsWith("'") && paramValue.endsWith("'")) {
               paramValue = paramValue.substring(1, paramValue.length() - 1);
            }

            try {
               cookie.setExpiryDate(DateParser.parseDate(paramValue));
            } catch (DateParseException var7) {
               LOG.debug("Error parsing cookie date", var7);
               throw new MalformedCookieException("Unable to parse expiration date parameter: " + paramValue);
            }
         } else if (LOG.isDebugEnabled()) {
            LOG.debug("Unrecognized cookie attribute: " + attribute.toString());
         }

      }
   }

   public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException {
      LOG.trace("enter CookieSpecBase.validate(String, port, path, boolean, Cookie)");
      if (host == null) {
         throw new IllegalArgumentException("Host of origin may not be null");
      } else if (host.trim().equals("")) {
         throw new IllegalArgumentException("Host of origin may not be blank");
      } else if (port < 0) {
         throw new IllegalArgumentException("Invalid port: " + port);
      } else if (path == null) {
         throw new IllegalArgumentException("Path of origin may not be null.");
      } else {
         if (path.trim().equals("")) {
            path = "/";
         }

         host = host.toLowerCase();
         if (cookie.getVersion() < 0) {
            throw new MalformedCookieException("Illegal version number " + cookie.getValue());
         } else {
            if (host.indexOf(".") >= 0) {
               if (!host.endsWith(cookie.getDomain())) {
                  String s = cookie.getDomain();
                  if (s.startsWith(".")) {
                     s = s.substring(1, s.length());
                  }

                  if (!host.equals(s)) {
                     throw new MalformedCookieException("Illegal domain attribute \"" + cookie.getDomain() + "\". Domain of origin: \"" + host + "\"");
                  }
               }
            } else if (!host.equals(cookie.getDomain())) {
               throw new MalformedCookieException("Illegal domain attribute \"" + cookie.getDomain() + "\". Domain of origin: \"" + host + "\"");
            }

            if (!path.startsWith(cookie.getPath())) {
               throw new MalformedCookieException("Illegal path attribute \"" + cookie.getPath() + "\". Path of origin: \"" + path + "\"");
            }
         }
      }
   }

   public boolean match(String host, int port, String path, boolean secure, Cookie cookie) {
      LOG.trace("enter CookieSpecBase.match(String, int, String, boolean, Cookie");
      if (host == null) {
         throw new IllegalArgumentException("Host of origin may not be null");
      } else if (host.trim().equals("")) {
         throw new IllegalArgumentException("Host of origin may not be blank");
      } else if (port < 0) {
         throw new IllegalArgumentException("Invalid port: " + port);
      } else if (path == null) {
         throw new IllegalArgumentException("Path of origin may not be null.");
      } else if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null");
      } else {
         if (path.trim().equals("")) {
            path = "/";
         }

         host = host.toLowerCase();
         if (cookie.getDomain() == null) {
            LOG.warn("Invalid cookie state: domain not specified");
            return false;
         } else if (cookie.getPath() == null) {
            LOG.warn("Invalid cookie state: path not specified");
            return false;
         } else {
            return (cookie.getExpiryDate() == null || cookie.getExpiryDate().after(new Date())) && domainMatch(host, cookie.getDomain()) && pathMatch(path, cookie.getPath()) && (cookie.getSecure() ? secure : true);
         }
      }
   }

   private static boolean domainMatch(String host, String domain) {
      boolean match = host.equals(domain) || domain.startsWith(".") && host.endsWith(domain);
      return match;
   }

   private static boolean pathMatch(String path, String topmostPath) {
      boolean match = path.startsWith(topmostPath);
      if (match && path.length() != topmostPath.length() && !topmostPath.endsWith("/")) {
         match = path.charAt(topmostPath.length()) == CookieSpec.PATH_DELIM_CHAR;
      }

      return match;
   }

   public Cookie[] match(String host, int port, String path, boolean secure, Cookie[] cookies) {
      LOG.trace("enter CookieSpecBase.match(String, int, String, boolean, Cookie[])");
      if (host == null) {
         throw new IllegalArgumentException("Host of origin may not be null");
      } else if (host.trim().equals("")) {
         throw new IllegalArgumentException("Host of origin may not be blank");
      } else if (port < 0) {
         throw new IllegalArgumentException("Invalid port: " + port);
      } else if (path == null) {
         throw new IllegalArgumentException("Path of origin may not be null.");
      } else if (cookies == null) {
         throw new IllegalArgumentException("Cookie array may not be null");
      } else {
         if (path.trim().equals("")) {
            path = "/";
         }

         host = host.toLowerCase();
         if (cookies.length <= 0) {
            return null;
         } else {
            List matching = new LinkedList();

            for(int i = 0; i < cookies.length; ++i) {
               if (this.match(host, port, path, secure, cookies[i])) {
                  addInPathOrder(matching, cookies[i]);
               }
            }

            return (Cookie[])matching.toArray(new Cookie[matching.size()]);
         }
      }
   }

   private static void addInPathOrder(List list, Cookie addCookie) {
      int i = false;

      int i;
      for(i = 0; i < list.size(); ++i) {
         Cookie c = (Cookie)list.get(i);
         if (addCookie.compare(addCookie, c) > 0) {
            break;
         }
      }

      list.add(i, addCookie);
   }

   public String formatCookie(Cookie cookie) {
      LOG.trace("enter CookieSpecBase.formatCookie(Cookie)");
      if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null");
      } else {
         StringBuffer buf = new StringBuffer();
         buf.append(cookie.getName());
         buf.append("=");
         String s = cookie.getValue();
         if (s != null) {
            buf.append(s);
         }

         return buf.toString();
      }
   }

   public String formatCookies(Cookie[] cookies) throws IllegalArgumentException {
      LOG.trace("enter CookieSpecBase.formatCookies(Cookie[])");
      if (cookies == null) {
         throw new IllegalArgumentException("Cookie array may not be null");
      } else if (cookies.length == 0) {
         throw new IllegalArgumentException("Cookie array may not be empty");
      } else {
         StringBuffer buffer = new StringBuffer();

         for(int i = 0; i < cookies.length; ++i) {
            if (i > 0) {
               buffer.append("; ");
            }

            buffer.append(this.formatCookie(cookies[i]));
         }

         return buffer.toString();
      }
   }

   public Header formatCookieHeader(Cookie[] cookies) {
      LOG.trace("enter CookieSpecBase.formatCookieHeader(Cookie[])");
      return new Header("Cookie", this.formatCookies(cookies));
   }

   public Header formatCookieHeader(Cookie cookie) {
      LOG.trace("enter CookieSpecBase.formatCookieHeader(Cookie)");
      return new Header("Cookie", this.formatCookie(cookie));
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$cookie$CookieSpec == null ? (class$org$apache$commons$httpclient$cookie$CookieSpec = class$("org.apache.commons.httpclient.cookie.CookieSpec")) : class$org$apache$commons$httpclient$cookie$CookieSpec);
   }
}
