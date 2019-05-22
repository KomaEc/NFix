package org.apache.velocity.app.event.implement;

import org.apache.velocity.app.event.IncludeEventHandler;

public class IncludeRelativePath implements IncludeEventHandler {
   public String includeEvent(String includeResourcePath, String currentResourcePath, String directiveName) {
      if (!includeResourcePath.startsWith("/") && !includeResourcePath.startsWith("\\")) {
         int lastslashpos = Math.max(currentResourcePath.lastIndexOf("/"), currentResourcePath.lastIndexOf("\\"));
         return lastslashpos == -1 ? includeResourcePath : currentResourcePath.substring(0, lastslashpos) + "/" + includeResourcePath;
      } else {
         return includeResourcePath;
      }
   }
}
