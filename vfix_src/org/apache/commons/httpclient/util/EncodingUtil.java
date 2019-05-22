package org.apache.commons.httpclient.util;

import java.util.BitSet;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncodingUtil {
   private static final Log LOG;
   private static final BitSet WWW_FORM_URL;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$util$EncodingUtil;

   public static String formUrlEncode(NameValuePair[] pairs, String charset) {
      StringBuffer buf = new StringBuffer();

      for(int i = 0; i < pairs.length; ++i) {
         if (pairs[i].getName() != null) {
            if (i > 0) {
               buf.append("&");
            }

            String queryName = pairs[i].getName();

            try {
               queryName = URIUtil.encode(queryName, WWW_FORM_URL, charset).replace(' ', '+');
            } catch (URIException var8) {
               LOG.error("Error encoding pair name: " + queryName, var8);
            }

            buf.append(queryName);
            buf.append("=");
            if (pairs[i].getValue() != null) {
               String queryValue = pairs[i].getValue();

               try {
                  queryValue = URIUtil.encode(queryValue, WWW_FORM_URL, charset).replace(' ', '+');
               } catch (URIException var7) {
                  LOG.error("Error encoding pair value: " + queryValue, var7);
               }

               buf.append(queryValue);
            }
         }
      }

      return buf.toString();
   }

   private EncodingUtil() {
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$util$EncodingUtil == null ? (class$org$apache$commons$httpclient$util$EncodingUtil = class$("org.apache.commons.httpclient.util.EncodingUtil")) : class$org$apache$commons$httpclient$util$EncodingUtil);
      WWW_FORM_URL = new BitSet(256);

      for(int i = 97; i <= 122; ++i) {
         WWW_FORM_URL.set(i);
      }

      for(int i = 65; i <= 90; ++i) {
         WWW_FORM_URL.set(i);
      }

      for(int i = 48; i <= 57; ++i) {
         WWW_FORM_URL.set(i);
      }

      WWW_FORM_URL.set(32);
      WWW_FORM_URL.set(45);
      WWW_FORM_URL.set(95);
      WWW_FORM_URL.set(46);
      WWW_FORM_URL.set(42);
   }
}
