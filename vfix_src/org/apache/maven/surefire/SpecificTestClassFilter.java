package org.apache.maven.surefire;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.SelectorUtils;
import org.apache.maven.surefire.util.ScannerFilter;

public class SpecificTestClassFilter implements ScannerFilter {
   private static final char FS = System.getProperty("file.separator").charAt(0);
   private static final String JAVA_CLASS_FILE_EXTENSION = ".class";
   private Set<String> names;

   public SpecificTestClassFilter(String[] classNames) {
      if (classNames != null && classNames.length > 0) {
         this.names = new HashSet();
         Collections.addAll(this.names, classNames);
      }

   }

   public boolean accept(Class testClass) {
      boolean result = true;
      if (this.names != null && !this.names.isEmpty()) {
         String className = testClass.getName().replace('.', FS) + ".class";
         boolean found = false;
         Iterator i$ = this.names.iterator();

         while(i$.hasNext()) {
            String pattern = (String)i$.next();
            if ('\\' == FS) {
               pattern = pattern.replace('/', FS);
            }

            if (SelectorUtils.matchPath(pattern, className, true)) {
               found = true;
               break;
            }
         }

         if (!found) {
            result = false;
         }
      }

      return result;
   }
}
