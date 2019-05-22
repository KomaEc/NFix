package com.gzoltar.shaded.org.pitest.mutationtest.config;

import java.io.File;

public class UndatedReportDirCreationStrategy implements ReportDirCreationStrategy {
   public File createReportDir(String base) {
      File reportDir = new File(base);
      reportDir.mkdirs();
      return reportDir;
   }
}
