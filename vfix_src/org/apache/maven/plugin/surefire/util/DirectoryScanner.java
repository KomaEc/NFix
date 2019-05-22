package org.apache.maven.plugin.surefire.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.surefire.util.DefaultScanResult;

public class DirectoryScanner {
   private final File basedir;
   private final List<String> includes;
   private final List<String> excludes;
   private final List<String> specificTests;

   public DirectoryScanner(File basedir, List<String> includes, List<String> excludes, List<String> specificTests) {
      this.basedir = basedir;
      this.includes = includes;
      this.excludes = excludes;
      this.specificTests = specificTests;
   }

   public DefaultScanResult scan() {
      String[] specific = this.specificTests == null ? new String[0] : ScannerUtil.processIncludesExcludes(this.specificTests);
      SpecificFileFilter specificTestFilter = new SpecificFileFilter(specific);
      List<String> result = new ArrayList();
      if (this.basedir.exists()) {
         org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.DirectoryScanner scanner = new org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.DirectoryScanner();
         scanner.setBasedir(this.basedir);
         if (this.includes != null) {
            scanner.setIncludes(ScannerUtil.processIncludesExcludes(this.includes));
         }

         if (this.excludes != null) {
            scanner.setExcludes(ScannerUtil.processIncludesExcludes(this.excludes));
         }

         scanner.scan();
         String[] arr$ = scanner.getIncludedFiles();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String test = arr$[i$];
            if (specificTestFilter.accept(ScannerUtil.convertSlashToSystemFileSeparator(ScannerUtil.stripBaseDir(this.basedir.getAbsolutePath(), test)))) {
               result.add(ScannerUtil.convertToJavaClassName(test));
            }
         }
      }

      return new DefaultScanResult(result);
   }
}
