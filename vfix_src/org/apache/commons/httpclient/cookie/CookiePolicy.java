package org.apache.commons.httpclient.cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CookiePolicy {
   private static final String SYSTEM_PROPERTY = "apache.commons.httpclient.cookiespec";
   public static final int COMPATIBILITY = 0;
   public static final int NETSCAPE_DRAFT = 1;
   public static final int RFC2109 = 2;
   private static int defaultPolicy = 2;
   protected static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$cookie$CookiePolicy;

   public static int getDefaultPolicy() {
      return defaultPolicy;
   }

   public static void setDefaultPolicy(int policy) {
      defaultPolicy = policy;
   }

   public static CookieSpec getSpecByPolicy(int policy) {
      switch(policy) {
      case 0:
         return new CookieSpecBase();
      case 1:
         return new NetscapeDraftSpec();
      case 2:
         return new RFC2109Spec();
      default:
         return getSpecByPolicy(defaultPolicy);
      }
   }

   public static CookieSpec getDefaultSpec() {
      return getSpecByPolicy(defaultPolicy);
   }

   public static CookieSpec getSpecByVersion(int ver) {
      switch(ver) {
      case 0:
         return new NetscapeDraftSpec();
      case 1:
         return new RFC2109Spec();
      default:
         return getDefaultSpec();
      }
   }

   public static CookieSpec getCompatibilitySpec() {
      return getSpecByPolicy(0);
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$cookie$CookiePolicy == null ? (class$org$apache$commons$httpclient$cookie$CookiePolicy = class$("org.apache.commons.httpclient.cookie.CookiePolicy")) : class$org$apache$commons$httpclient$cookie$CookiePolicy);
      String s = null;

      try {
         s = System.getProperty("apache.commons.httpclient.cookiespec");
      } catch (SecurityException var2) {
      }

      if ("COMPATIBILITY".equalsIgnoreCase(s)) {
         setDefaultPolicy(0);
      } else if ("NETSCAPE_DRAFT".equalsIgnoreCase(s)) {
         setDefaultPolicy(1);
      } else if ("RFC2109".equalsIgnoreCase(s)) {
         setDefaultPolicy(2);
      } else {
         if (s != null) {
            LOG.warn("Unrecognized cookiespec property '" + s + "' - using default");
         }

         setDefaultPolicy(defaultPolicy);
      }

   }
}
