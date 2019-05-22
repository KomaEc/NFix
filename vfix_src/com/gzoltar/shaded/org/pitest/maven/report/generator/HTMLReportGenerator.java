package com.gzoltar.shaded.org.pitest.maven.report.generator;

import com.gzoltar.shaded.org.apache.commons.io.FileUtils;
import com.gzoltar.shaded.org.apache.commons.io.filefilter.NotFileFilter;
import com.gzoltar.shaded.org.apache.commons.io.filefilter.RegexFileFilter;
import java.io.FileFilter;
import java.io.IOException;

public class HTMLReportGenerator implements ReportGenerationStrategy {
   protected static final FileFilter EXCLUDE_TIMESTAMPED_REPORTS_DIRECTORIES = new NotFileFilter(new RegexFileFilter("^\\d+$"));

   public ReportGenerationResultEnum generate(ReportGenerationContext context) {
      try {
         context.getLogger().debug((CharSequence)("HTMLReportGenerator using directory [" + context.getReportsDataDirectory() + "] as directory containing the html report"));
         context.getLogger().debug((CharSequence)("HTMLReportGenerator using directory [" + context.getSiteDirectory() + "] as directory that is the destination of the site report"));
         FileUtils.copyDirectory(context.getReportsDataDirectory(), context.getSiteDirectory(), EXCLUDE_TIMESTAMPED_REPORTS_DIRECTORIES);
      } catch (IOException var3) {
         context.getLogger().warn((Throwable)var3);
         return ReportGenerationResultEnum.FAILURE;
      }

      return ReportGenerationResultEnum.SUCCESS;
   }

   public String getGeneratorName() {
      return "HTMLReportGenerator";
   }

   public String getGeneratorDataFormat() {
      return "HTML";
   }
}
