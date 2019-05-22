package org.apache.commons.httpclient.util;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

public class URIUtil {
   protected static final BitSet empty = new BitSet(1);

   public static String getName(String uri) {
      if (uri != null && uri.length() != 0) {
         String path = getPath(uri);
         int at = path.lastIndexOf("/");
         int to = path.length();
         return at >= 0 ? path.substring(at + 1, to) : path;
      } else {
         return uri;
      }
   }

   public static String getQuery(String uri) {
      if (uri != null && uri.length() != 0) {
         int at = uri.indexOf("//");
         int from = uri.indexOf("/", at >= 0 ? (uri.lastIndexOf("/", at - 1) >= 0 ? 0 : at + 2) : 0);
         int to = uri.length();
         at = uri.indexOf("?", from);
         if (at >= 0) {
            from = at + 1;
            if (uri.lastIndexOf("#") > from) {
               to = uri.lastIndexOf("#");
            }

            return from >= 0 && from != to ? uri.substring(from, to) : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static String getPath(String uri) {
      if (uri == null) {
         return null;
      } else {
         int at = uri.indexOf("//");
         int from = uri.indexOf("/", at >= 0 ? (uri.lastIndexOf("/", at - 1) >= 0 ? 0 : at + 2) : 0);
         int to = uri.length();
         if (uri.indexOf(63, from) != -1) {
            to = uri.indexOf(63, from);
         }

         if (uri.lastIndexOf("#") > from && uri.lastIndexOf("#") < to) {
            to = uri.lastIndexOf("#");
         }

         return from < 0 ? (at >= 0 ? "/" : uri) : uri.substring(from, to);
      }
   }

   public static String getPathQuery(String uri) {
      if (uri == null) {
         return null;
      } else {
         int at = uri.indexOf("//");
         int from = uri.indexOf("/", at >= 0 ? (uri.lastIndexOf("/", at - 1) >= 0 ? 0 : at + 2) : 0);
         int to = uri.length();
         if (uri.lastIndexOf("#") > from) {
            to = uri.lastIndexOf("#");
         }

         return from < 0 ? (at >= 0 ? "/" : uri) : uri.substring(from, to);
      }
   }

   public static String getFromPath(String uri) {
      if (uri == null) {
         return null;
      } else {
         int at = uri.indexOf("//");
         int from = uri.indexOf("/", at >= 0 ? (uri.lastIndexOf("/", at - 1) >= 0 ? 0 : at + 2) : 0);
         return from < 0 ? (at >= 0 ? "/" : uri) : uri.substring(from);
      }
   }

   public static String encodeAll(String unescaped) throws URIException {
      return encodeAll(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodeAll(String unescaped, String charset) throws URIException {
      return encode(unescaped, empty, charset);
   }

   public static String encodeWithinAuthority(String unescaped) throws URIException {
      return encodeWithinAuthority(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodeWithinAuthority(String unescaped, String charset) throws URIException {
      return encode(unescaped, URI.allowed_within_authority, charset);
   }

   public static String encodePathQuery(String unescaped) throws URIException {
      return encodePathQuery(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodePathQuery(String unescaped, String charset) throws URIException {
      int at = unescaped.indexOf(63);
      return at < 0 ? encode(unescaped, URI.allowed_abs_path, charset) : encode(unescaped.substring(0, at), URI.allowed_abs_path, charset) + '?' + encode(unescaped.substring(at + 1), URI.allowed_query, charset);
   }

   public static String encodeWithinPath(String unescaped) throws URIException {
      return encodeWithinPath(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodeWithinPath(String unescaped, String charset) throws URIException {
      return encode(unescaped, URI.allowed_within_path, charset);
   }

   public static String encodePath(String unescaped) throws URIException {
      return encodePath(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodePath(String unescaped, String charset) throws URIException {
      return encode(unescaped, URI.allowed_abs_path, charset);
   }

   public static String encodeWithinQuery(String unescaped) throws URIException {
      return encodeWithinQuery(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodeWithinQuery(String unescaped, String charset) throws URIException {
      return encode(unescaped, URI.allowed_within_query, charset);
   }

   public static String encodeQuery(String unescaped) throws URIException {
      return encodeQuery(unescaped, URI.getDefaultProtocolCharset());
   }

   public static String encodeQuery(String unescaped, String charset) throws URIException {
      return encode(unescaped, URI.allowed_query, charset);
   }

   public static String encode(String unescaped, BitSet allowed) throws URIException {
      return encode(unescaped, allowed, URI.getDefaultProtocolCharset());
   }

   public static String encode(String unescaped, BitSet allowed, String charset) throws URIException {
      return new String(URIUtil.Coder.encode(unescaped, allowed, charset));
   }

   public static String decode(String escaped) throws URIException {
      return URIUtil.Coder.decode(escaped.toCharArray(), URI.getDefaultProtocolCharset());
   }

   public static String decode(String escaped, String charset) throws URIException {
      return URIUtil.Coder.decode(escaped.toCharArray(), charset);
   }

   /** @deprecated */
   public static String toProtocolCharset(String target) throws URIException {
      return toUsingCharset(target, URI.getDefaultDocumentCharset(), URI.getDefaultProtocolCharset());
   }

   /** @deprecated */
   public static String toProtocolCharset(String target, String charset) throws URIException {
      return toUsingCharset(target, URI.getDefaultDocumentCharset(), charset);
   }

   /** @deprecated */
   public static String toDocumentCharset(String target) throws URIException {
      return toUsingCharset(target, URI.getDefaultProtocolCharset(), URI.getDefaultDocumentCharset());
   }

   /** @deprecated */
   public static String toDocumentCharset(String target, String charset) throws URIException {
      return toUsingCharset(target, URI.getDefaultProtocolCharset(), charset);
   }

   /** @deprecated */
   public static String toUsingCharset(String target, String fromCharset, String toCharset) throws URIException {
      try {
         return new String(target.getBytes(fromCharset), toCharset);
      } catch (UnsupportedEncodingException var4) {
         throw new URIException(2, var4.getMessage());
      }
   }

   protected static class Coder extends URI {
      public static char[] encode(String unescapedComponent, BitSet allowed, String charset) throws URIException {
         return URI.encode(unescapedComponent, allowed, charset);
      }

      public static String decode(char[] escapedComponent, String charset) throws URIException {
         return URI.decode(escapedComponent, charset);
      }

      public static boolean verifyEscaped(char[] original) {
         for(int i = 0; i < original.length; ++i) {
            int c = original[i];
            if (c > 128) {
               return false;
            }

            if (c == '%') {
               ++i;
               if (Character.digit(original[i], 16) == -1) {
                  return false;
               }

               ++i;
               if (Character.digit(original[i], 16) == -1) {
                  return false;
               }
            }
         }

         return true;
      }

      public static String replace(String original, char[] from, char[] to) {
         for(int i = from.length; i > 0; --i) {
            original = replace(original, from[i], to[i]);
         }

         return original.toString();
      }

      public static String replace(String original, char from, char to) {
         StringBuffer result = new StringBuffer(original.length());
         int saved = 0;

         int at;
         do {
            at = original.indexOf(from);
            if (at >= 0) {
               result.append(original.substring(0, at));
               result.append(to);
            } else {
               result.append(original.substring(saved));
            }

            saved = at;
         } while(at >= 0);

         return result.toString();
      }
   }
}
