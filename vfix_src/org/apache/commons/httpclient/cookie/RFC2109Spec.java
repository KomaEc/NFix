package org.apache.commons.httpclient.cookie;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;

public class RFC2109Spec extends CookieSpecBase {
   public void parseAttribute(NameValuePair attribute, Cookie cookie) throws MalformedCookieException {
      if (attribute == null) {
         throw new IllegalArgumentException("Attribute may not be null.");
      } else if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null.");
      } else {
         String paramName = attribute.getName().toLowerCase();
         String paramValue = attribute.getValue();
         if (paramName.equals("path")) {
            if (paramValue == null) {
               throw new MalformedCookieException("Missing value for path attribute");
            }

            if (paramValue.trim().equals("")) {
               throw new MalformedCookieException("Blank value for path attribute");
            }

            cookie.setPath(paramValue);
            cookie.setPathAttributeSpecified(true);
         } else if (paramName.equals("version")) {
            if (paramValue == null) {
               throw new MalformedCookieException("Missing value for version attribute");
            }

            try {
               cookie.setVersion(Integer.parseInt(paramValue));
            } catch (NumberFormatException var6) {
               throw new MalformedCookieException("Invalid version: " + var6.getMessage());
            }
         } else {
            super.parseAttribute(attribute, cookie);
         }

      }
   }

   public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException {
      CookieSpecBase.LOG.trace("enter RFC2109Spec.validate(String, int, String, boolean, Cookie)");
      super.validate(host, port, path, secure, cookie);
      if (cookie.getName().indexOf(32) != -1) {
         throw new MalformedCookieException("Cookie name may not contain blanks");
      } else if (cookie.getName().startsWith("$")) {
         throw new MalformedCookieException("Cookie name may not start with $");
      } else {
         if (cookie.isDomainAttributeSpecified() && !cookie.getDomain().equals(host)) {
            if (!cookie.getDomain().startsWith(".")) {
               throw new MalformedCookieException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2109: domain must start with a dot");
            }

            int dotIndex = cookie.getDomain().indexOf(46, 1);
            if (dotIndex < 0 || dotIndex == cookie.getDomain().length() - 1) {
               throw new MalformedCookieException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2109: domain must contain an embedded dot");
            }

            host = host.toLowerCase();
            if (host.indexOf(46) >= 0) {
               if (!host.endsWith(cookie.getDomain())) {
                  throw new MalformedCookieException("Illegal domain attribute \"" + cookie.getDomain() + "\". Domain of origin: \"" + host + "\"");
               }

               String hostWithoutDomain = host.substring(0, host.length() - cookie.getDomain().length());
               if (hostWithoutDomain.indexOf(46) != -1) {
                  throw new MalformedCookieException("Domain attribute \"" + cookie.getDomain() + "\" violates RFC 2109: host minus domain may not contain any dots");
               }
            }
         }

      }
   }

   private String formatNameValuePair(String name, String value, int version) {
      StringBuffer buffer = new StringBuffer();
      if (version < 1) {
         buffer.append(name);
         buffer.append("=");
         if (value != null) {
            buffer.append(value);
         }
      } else {
         buffer.append(name);
         buffer.append("=\"");
         if (value != null) {
            buffer.append(value);
         }

         buffer.append("\"");
      }

      return buffer.toString();
   }

   private String formatCookieAsVer(Cookie cookie, int version) {
      CookieSpecBase.LOG.trace("enter RFC2109Spec.formatCookieAsVer(Cookie)");
      if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null");
      } else {
         StringBuffer buf = new StringBuffer();
         buf.append(this.formatNameValuePair(cookie.getName(), cookie.getValue(), version));
         if (cookie.getDomain() != null && cookie.isDomainAttributeSpecified()) {
            buf.append("; ");
            buf.append(this.formatNameValuePair("$Domain", cookie.getDomain(), version));
         }

         if (cookie.getPath() != null && cookie.isPathAttributeSpecified()) {
            buf.append("; ");
            buf.append(this.formatNameValuePair("$Path", cookie.getPath(), version));
         }

         return buf.toString();
      }
   }

   public String formatCookie(Cookie cookie) {
      CookieSpecBase.LOG.trace("enter RFC2109Spec.formatCookie(Cookie)");
      if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null");
      } else {
         int ver = cookie.getVersion();
         StringBuffer buffer = new StringBuffer();
         buffer.append(this.formatNameValuePair("$Version", Integer.toString(ver), ver));
         buffer.append("; ");
         buffer.append(this.formatCookieAsVer(cookie, ver));
         return buffer.toString();
      }
   }

   public String formatCookies(Cookie[] cookies) {
      CookieSpecBase.LOG.trace("enter RFC2109Spec.formatCookieHeader(Cookie[])");
      int version = Integer.MAX_VALUE;

      for(int i = 0; i < cookies.length; ++i) {
         Cookie cookie = cookies[i];
         if (cookie.getVersion() < version) {
            version = cookie.getVersion();
         }
      }

      StringBuffer buffer = new StringBuffer();
      buffer.append(this.formatNameValuePair("$Version", Integer.toString(version), version));

      for(int i = 0; i < cookies.length; ++i) {
         buffer.append("; ");
         buffer.append(this.formatCookieAsVer(cookies[i], version));
      }

      return buffer.toString();
   }
}
