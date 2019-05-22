package org.apache.maven.wagon.proxy;

import java.util.StringTokenizer;

public final class ProxyUtils {
   private ProxyUtils() {
   }

   public static boolean validateNonProxyHosts(ProxyInfo proxy, String targetHost) {
      if (targetHost == null) {
         targetHost = new String();
      }

      if (proxy == null) {
         return false;
      } else {
         String nonProxyHosts = proxy.getNonProxyHosts();
         if (nonProxyHosts == null) {
            return false;
         } else {
            StringTokenizer tokenizer = new StringTokenizer(nonProxyHosts, "|");

            String pattern;
            do {
               if (!tokenizer.hasMoreTokens()) {
                  return false;
               }

               pattern = tokenizer.nextToken();
               pattern = pattern.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*");
            } while(!targetHost.matches(pattern));

            return true;
         }
      }
   }
}
