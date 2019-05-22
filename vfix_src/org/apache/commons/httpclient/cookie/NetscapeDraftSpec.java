package org.apache.commons.httpclient.cookie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;

public class NetscapeDraftSpec extends CookieSpecBase {
   public void parseAttribute(NameValuePair attribute, Cookie cookie) throws MalformedCookieException {
      if (attribute == null) {
         throw new IllegalArgumentException("Attribute may not be null.");
      } else if (cookie == null) {
         throw new IllegalArgumentException("Cookie may not be null.");
      } else {
         String paramName = attribute.getName().toLowerCase();
         String paramValue = attribute.getValue();
         if (paramName.equals("expires")) {
            if (paramValue == null) {
               throw new MalformedCookieException("Missing value for expires attribute");
            }

            try {
               DateFormat expiryFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);
               Date date = expiryFormat.parse(paramValue);
               cookie.setExpiryDate(date);
            } catch (ParseException var7) {
               throw new MalformedCookieException("Invalid expires attribute: " + var7.getMessage());
            }
         } else {
            super.parseAttribute(attribute, cookie);
         }

      }
   }

   public void validate(String host, int port, String path, boolean secure, Cookie cookie) throws MalformedCookieException {
      CookieSpecBase.LOG.trace("enterNetscapeDraftCookieProcessor RCF2109CookieProcessor.validate(Cookie)");
      super.validate(host, port, path, secure, cookie);
      if (host.indexOf(".") >= 0) {
         int domainParts = (new StringTokenizer(cookie.getDomain(), ".")).countTokens();
         if (isSpecialDomain(cookie.getDomain())) {
            if (domainParts < 2) {
               throw new MalformedCookieException("Domain attribute \"" + cookie.getDomain() + "\" violates the Netscape cookie specification for " + "special domains");
            }
         } else if (domainParts < 3) {
            throw new MalformedCookieException("Domain attribute \"" + cookie.getDomain() + "\" violates the Netscape cookie specification");
         }
      }

   }

   private static boolean isSpecialDomain(String domain) {
      String ucDomain = domain.toUpperCase();
      return ucDomain.endsWith(".COM") || ucDomain.endsWith(".EDU") || ucDomain.endsWith(".NET") || ucDomain.endsWith(".GOV") || ucDomain.endsWith(".MIL") || ucDomain.endsWith(".ORG") || ucDomain.endsWith(".INT");
   }
}
