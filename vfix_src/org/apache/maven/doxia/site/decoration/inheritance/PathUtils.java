package org.apache.maven.doxia.site.decoration.inheritance;

import java.net.MalformedURLException;
import java.net.URL;
import org.codehaus.plexus.util.PathTool;

public abstract class PathUtils {
   private PathUtils() {
   }

   public static final PathDescriptor convertPath(PathDescriptor oldPath, PathDescriptor newPath) throws MalformedURLException {
      String relative = getRelativePath(oldPath, newPath);
      return relative == null ? oldPath : new PathDescriptor(relative);
   }

   public static final String getRelativePath(PathDescriptor oldPathDescriptor, PathDescriptor newPathDescriptor) {
      if (oldPathDescriptor.isFile() && !newPathDescriptor.isFile()) {
         return oldPathDescriptor.isRelative() ? oldPathDescriptor.getPath() : null;
      } else if (!oldPathDescriptor.isFile()) {
         URL oldUrl = oldPathDescriptor.getPathUrl();
         URL newUrl = newPathDescriptor.getPathUrl();
         if (oldUrl != null && newUrl != null) {
            if (newUrl.getProtocol().equalsIgnoreCase(oldUrl.getProtocol()) && newUrl.getHost().equalsIgnoreCase(oldUrl.getHost()) && newUrl.getPort() == oldUrl.getPort()) {
               String oldPath = oldPathDescriptor.getPath();
               String newPath = newPathDescriptor.getPath();
               return PathTool.getRelativeWebPath(newPath, oldPath);
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         String oldPath = oldPathDescriptor.getPath();
         String newPath = newPathDescriptor.getPath();
         return oldPath != null && newPath != null ? PathTool.getRelativeFilePath(oldPath, newPath) : null;
      }
   }
}
