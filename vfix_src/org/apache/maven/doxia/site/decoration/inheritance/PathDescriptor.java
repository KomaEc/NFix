package org.apache.maven.doxia.site.decoration.inheritance;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.codehaus.plexus.util.StringUtils;

public class PathDescriptor {
   private final URL baseUrl;
   private final URL pathUrl;
   private final String relativePath;

   public PathDescriptor(String path) throws MalformedURLException {
      this((URL)null, path);
   }

   public PathDescriptor(String base, String path) throws MalformedURLException {
      this(buildBaseUrl(base), path);
   }

   public PathDescriptor(URL baseUrl, String path) throws MalformedURLException {
      this.baseUrl = baseUrl;
      URL pathURL = null;
      String relPath = null;

      try {
         pathURL = new URL(path);
      } catch (MalformedURLException var8) {
         try {
            pathURL = buildUrl(baseUrl, path);
         } catch (MalformedURLException var7) {
            if (path != null && path.startsWith("/")) {
               relPath = path.substring(1);
            } else {
               relPath = path;
            }
         }
      }

      this.pathUrl = pathURL;
      this.relativePath = relPath;
   }

   private static URL buildBaseUrl(String base) throws MalformedURLException {
      if (base == null) {
         return null;
      } else {
         try {
            return new URL(base);
         } catch (MalformedURLException var2) {
            return (new File(base)).toURI().toURL();
         }
      }
   }

   private static URL buildUrl(URL baseUrl, String path) throws MalformedURLException {
      if (baseUrl == null) {
         throw new MalformedURLException("Base is null!");
      } else if (path == null) {
         return baseUrl;
      } else if (baseUrl.getProtocol().equals("file")) {
         return (new File(baseUrl.getFile(), path)).toURI().toURL();
      } else {
         return path.startsWith("/") && baseUrl.getPath().endsWith("/") ? new URL(baseUrl, path.substring(1)) : new URL(baseUrl, path);
      }
   }

   public boolean isFile() {
      return this.isRelative() || this.pathUrl.getProtocol().equals("file");
   }

   public boolean isRelative() {
      return this.pathUrl == null;
   }

   public URL getBaseUrl() {
      return this.baseUrl;
   }

   public URL getPathUrl() {
      return this.pathUrl;
   }

   public String getPath() {
      if (this.getPathUrl() != null) {
         return this.isFile() ? StringUtils.stripEnd(this.getPathUrl().getPath(), "/") : this.getPathUrl().getPath();
      } else {
         return this.relativePath;
      }
   }

   public String getLocation() {
      if (this.isFile()) {
         return this.getPathUrl() != null ? StringUtils.stripEnd(this.getPathUrl().getFile(), "/") : this.relativePath;
      } else {
         return this.getPathUrl().toExternalForm();
      }
   }

   public String toString() {
      StringBuffer res = new StringBuffer(StringUtils.isNotEmpty(this.relativePath) ? this.relativePath : String.valueOf(this.pathUrl));
      res.append(" (Base: ").append(this.baseUrl).append(") Location: ").append(this.getLocation());
      return res.toString();
   }
}
