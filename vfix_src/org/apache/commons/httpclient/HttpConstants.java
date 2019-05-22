package org.apache.commons.httpclient;

import java.io.UnsupportedEncodingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpConstants {
   public static final String HTTP_ELEMENT_CHARSET = "US-ASCII";
   public static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";
   private static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HttpConstants;

   public static byte[] getBytes(String data) {
      if (data == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         try {
            return data.getBytes("US-ASCII");
         } catch (UnsupportedEncodingException var2) {
            if (LOG.isWarnEnabled()) {
               LOG.warn("Unsupported encoding: US-ASCII. System default encoding used");
            }

            return data.getBytes();
         }
      }
   }

   public static String getString(byte[] data, int offset, int length) {
      if (data == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         try {
            return new String(data, offset, length, "US-ASCII");
         } catch (UnsupportedEncodingException var4) {
            if (LOG.isWarnEnabled()) {
               LOG.warn("Unsupported encoding: US-ASCII. System default encoding used");
            }

            return new String(data, offset, length);
         }
      }
   }

   public static String getString(byte[] data) {
      return getString(data, 0, data.length);
   }

   public static byte[] getContentBytes(String data, String charset) {
      if (data == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         if (charset == null || charset.equals("")) {
            charset = "ISO-8859-1";
         }

         try {
            return data.getBytes(charset);
         } catch (UnsupportedEncodingException var5) {
            if (LOG.isWarnEnabled()) {
               LOG.warn("Unsupported encoding: " + charset + ". HTTP default encoding used");
            }

            try {
               return data.getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException var4) {
               if (LOG.isWarnEnabled()) {
                  LOG.warn("Unsupported encoding: ISO-8859-1. System encoding used");
               }

               return data.getBytes();
            }
         }
      }
   }

   public static String getContentString(byte[] data, int offset, int length, String charset) {
      if (data == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         if (charset == null || charset.equals("")) {
            charset = "ISO-8859-1";
         }

         try {
            return new String(data, offset, length, charset);
         } catch (UnsupportedEncodingException var7) {
            if (LOG.isWarnEnabled()) {
               LOG.warn("Unsupported encoding: " + charset + ". Default HTTP encoding used");
            }

            try {
               return new String(data, offset, length, "ISO-8859-1");
            } catch (UnsupportedEncodingException var6) {
               if (LOG.isWarnEnabled()) {
                  LOG.warn("Unsupported encoding: ISO-8859-1. System encoding used");
               }

               return new String(data, offset, length);
            }
         }
      }
   }

   public static String getContentString(byte[] data, String charset) {
      return getContentString(data, 0, data.length, charset);
   }

   public static byte[] getContentBytes(String data) {
      return getContentBytes(data, (String)null);
   }

   public static String getContentString(byte[] data, int offset, int length) {
      return getContentString(data, offset, length, (String)null);
   }

   public static String getContentString(byte[] data) {
      return getContentString(data, (String)null);
   }

   public static byte[] getAsciiBytes(String data) {
      if (data == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         try {
            return data.getBytes("US-ASCII");
         } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException("HttpClient requires ASCII support");
         }
      }
   }

   public static String getAsciiString(byte[] data, int offset, int length) {
      if (data == null) {
         throw new IllegalArgumentException("Parameter may not be null");
      } else {
         try {
            return new String(data, offset, length, "US-ASCII");
         } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("HttpClient requires ASCII support");
         }
      }
   }

   public static String getAsciiString(byte[] data) {
      return getAsciiString(data, 0, data.length);
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
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HttpConstants == null ? (class$org$apache$commons$httpclient$HttpConstants = class$("org.apache.commons.httpclient.HttpConstants")) : class$org$apache$commons$httpclient$HttpConstants);
   }
}
