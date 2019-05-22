package org.apache.maven.surefire.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.BitSet;

public class UrlUtils {
   private static final BitSet UNRESERVED = new BitSet(256);
   private static final int RADIX = 16;
   private static final int MASK = 15;
   private static final String ENCODING = "UTF-8";

   private UrlUtils() {
   }

   public static URL getURL(File file) throws MalformedURLException {
      URL url = file.toURL();

      try {
         byte[] bytes = url.toString().getBytes("UTF-8");
         StringBuilder buf = new StringBuilder(bytes.length);
         byte[] arr$ = bytes;
         int len$ = bytes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            if (b > 0 && UNRESERVED.get(b)) {
               buf.append((char)b);
            } else {
               buf.append('%');
               buf.append(Character.forDigit(b >>> 4 & 15, 16));
               buf.append(Character.forDigit(b & 15, 16));
            }
         }

         return new URL(buf.toString());
      } catch (UnsupportedEncodingException var8) {
         throw new RuntimeException(var8);
      }
   }

   static {
      try {
         byte[] bytes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'():/".getBytes("UTF-8");
         byte[] arr$ = bytes;
         int len$ = bytes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte aByte = arr$[i$];
            UNRESERVED.set(aByte);
         }
      } catch (UnsupportedEncodingException var5) {
      }

   }
}
