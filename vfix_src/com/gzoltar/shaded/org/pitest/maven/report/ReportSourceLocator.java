package com.gzoltar.shaded.org.pitest.maven.report;

import com.gzoltar.shaded.org.apache.commons.io.comparator.LastModifiedFileComparator;
import com.gzoltar.shaded.org.apache.commons.io.filefilter.AndFileFilter;
import com.gzoltar.shaded.org.apache.commons.io.filefilter.CanWriteFileFilter;
import com.gzoltar.shaded.org.apache.commons.io.filefilter.DirectoryFileFilter;
import com.gzoltar.shaded.org.apache.commons.io.filefilter.RegexFileFilter;
import com.gzoltar.shaded.org.pitest.util.PitError;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import org.apache.maven.plugin.logging.Log;

public class ReportSourceLocator {
   static final FileFilter TIMESTAMPED_REPORTS_FILE_FILTER;

   public File locate(File reportsDirectory, Log log) {
      if (!reportsDirectory.exists()) {
         throw new PitError("could not find reports directory [" + reportsDirectory + "]");
      } else if (!reportsDirectory.canRead()) {
         throw new PitError("reports directory [" + reportsDirectory + "] not readable");
      } else if (!reportsDirectory.isDirectory()) {
         throw new PitError("reports directory [" + reportsDirectory + "] is actually a file, it must be a directory");
      } else {
         return this.executeLocator(reportsDirectory, log);
      }
   }

   private File executeLocator(File reportsDirectory, Log log) {
      File[] subdirectories = reportsDirectory.listFiles(TIMESTAMPED_REPORTS_FILE_FILTER);
      File latest = reportsDirectory;
      log.debug((CharSequence)("ReportSourceLocator starting search in directory [" + reportsDirectory.getAbsolutePath() + "]"));
      if (subdirectories != null) {
         LastModifiedFileComparator c = new LastModifiedFileComparator();
         File[] arr$ = subdirectories;
         int len$ = subdirectories.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File f = arr$[i$];
            log.debug((CharSequence)("comparing directory [" + f.getAbsolutePath() + "] with the current latest directory of [" + latest.getAbsolutePath() + "]"));
            if (c.compare((Object)latest, (Object)f) < 0) {
               latest = f;
               log.debug((CharSequence)("directory [" + f.getAbsolutePath() + "] is now the latest"));
            }
         }

         log.debug((CharSequence)("ReportSourceLocator determined directory [" + latest.getAbsolutePath() + "] is the directory containing the latest pit reports"));
         return latest;
      } else {
         throw new PitError("could not list files in directory [" + reportsDirectory.getAbsolutePath() + "] because of an unknown I/O error");
      }
   }

   static {
      TIMESTAMPED_REPORTS_FILE_FILTER = new AndFileFilter(Arrays.asList(DirectoryFileFilter.DIRECTORY, new RegexFileFilter("^\\d+$"), CanWriteFileFilter.CAN_WRITE));
   }
}
