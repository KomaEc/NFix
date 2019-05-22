package org.apache.maven.plugin.surefire.booterclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

public class ProviderDetector {
   @Nonnull
   public static Set<String> getServiceNames(Class<?> clazz, ClassLoader classLoader) throws IOException {
      String resourceName = "META-INF/services/" + clazz.getName();
      if (classLoader == null) {
         return Collections.emptySet();
      } else {
         Enumeration<URL> urlEnumeration = classLoader.getResources(resourceName);
         return getNames(urlEnumeration);
      }
   }

   @Nonnull
   private static Set<String> getNames(Enumeration<URL> urlEnumeration) throws IOException {
      HashSet names = new HashSet();

      label137:
      while(urlEnumeration.hasMoreElements()) {
         URL url = (URL)urlEnumeration.nextElement();
         BufferedReader reader = getReader(url);

         try {
            while(true) {
               String line;
               int n;
               do {
                  if ((line = reader.readLine()) == null) {
                     continue label137;
                  }

                  int ci = line.indexOf(35);
                  if (ci >= 0) {
                     line = line.substring(0, ci);
                  }

                  line = line.trim();
                  n = line.length();
               } while(n == 0);

               if (line.indexOf(32) >= 0 || line.indexOf(9) >= 0) {
                  break;
               }

               char cp = line.charAt(0);
               if (!Character.isJavaIdentifierStart(cp)) {
                  break;
               }

               for(int i = 1; i < n; ++i) {
                  cp = line.charAt(i);
                  if (!Character.isJavaIdentifierPart(cp) && cp != '.') {
                     continue label137;
                  }
               }

               if (!names.contains(line)) {
                  names.add(line);
               }
            }
         } finally {
            reader.close();
         }
      }

      return names;
   }

   @Nonnull
   private static BufferedReader getReader(@Nonnull URL url) throws IOException {
      InputStream inputStream = url.openStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      return new BufferedReader(inputStreamReader);
   }
}
