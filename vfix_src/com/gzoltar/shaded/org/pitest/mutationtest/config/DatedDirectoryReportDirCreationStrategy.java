package com.gzoltar.shaded.org.pitest.mutationtest.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatedDirectoryReportDirCreationStrategy implements ReportDirCreationStrategy {
   public File createReportDir(String base) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
      String timeString = sdf.format(new Date());
      File reportDir = new File(this.addPathSeparatorIfMissing(base) + timeString);
      reportDir.mkdirs();
      return reportDir;
   }

   private String addPathSeparatorIfMissing(String s) {
      return !s.endsWith(File.separator) ? s + File.separator : s;
   }
}
