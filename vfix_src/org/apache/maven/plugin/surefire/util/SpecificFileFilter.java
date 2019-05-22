package org.apache.maven.plugin.surefire.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.SelectorUtils;

public class SpecificFileFilter {
   private Set<String> names;

   public SpecificFileFilter(@Nullable String[] classNames) {
      if (classNames != null && classNames.length > 0) {
         this.names = new HashSet();
         String[] arr$ = classNames;
         int len$ = classNames.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String name = arr$[i$];
            this.names.add(ScannerUtil.convertSlashToSystemFileSeparator(name));
         }
      }

   }

   public boolean accept(@Nullable String resourceName) {
      if (this.names != null && !this.names.isEmpty()) {
         Iterator i$ = this.names.iterator();

         String pattern;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            pattern = (String)i$.next();
         } while(!SelectorUtils.matchPath(pattern, resourceName, true));

         return true;
      } else {
         return true;
      }
   }
}
