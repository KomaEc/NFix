package org.apache.maven.scm.provider.svn;

import org.codehaus.plexus.util.StringUtils;

public final class SvnCommandUtils {
   private SvnCommandUtils() {
   }

   public static String fixUrl(String url, String username) {
      if (!StringUtils.isEmpty(username) && url.startsWith("svn+ssh://")) {
         int idx = url.indexOf(64);
         int cutIdx = idx < 0 ? "svn+ssh://".length() : idx + 1;
         url = "svn+ssh://" + username + "@" + url.substring(cutIdx);
      } else if (url.startsWith("file://")) {
         url = url.replace('\\', '/');
      }

      return url;
   }
}
