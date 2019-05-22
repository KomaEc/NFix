package org.codehaus.classworlds.uberjar.protocol.jar;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {
   private static final Handler INSTANCE = new Handler();

   public static Handler getInstance() {
      return INSTANCE;
   }

   public URLConnection openConnection(URL url) throws IOException {
      return new JarUrlConnection(url);
   }

   public void parseURL(URL url, String spec, int start, int limit) {
      String specPath = spec.substring(start, limit);
      String urlPath = null;
      if (specPath.charAt(0) == '/') {
         urlPath = specPath;
      } else {
         String relPath;
         int lastSlashLoc;
         if (specPath.charAt(0) == '!') {
            relPath = url.getFile();
            lastSlashLoc = relPath.lastIndexOf("!");
            if (lastSlashLoc < 0) {
               urlPath = relPath + specPath;
            } else {
               urlPath = relPath.substring(0, lastSlashLoc) + specPath;
            }
         } else {
            relPath = url.getFile();
            if (relPath != null) {
               lastSlashLoc = relPath.lastIndexOf("/");
               if (lastSlashLoc < 0) {
                  urlPath = "/" + specPath;
               } else {
                  urlPath = relPath.substring(0, lastSlashLoc + 1) + specPath;
               }
            } else {
               urlPath = specPath;
            }
         }
      }

      this.setURL(url, "jar", "", 0, (String)null, (String)null, urlPath, (String)null, (String)null);
   }
}
