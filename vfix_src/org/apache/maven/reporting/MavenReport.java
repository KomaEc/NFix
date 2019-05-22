package org.apache.maven.reporting;

import java.io.File;
import java.util.Locale;
import org.codehaus.doxia.sink.Sink;

public interface MavenReport {
   String ROLE = MavenReport.class.getName();
   String CATEGORY_PROJECT_INFORMATION = "Project Info";
   String CATEGORY_PROJECT_REPORTS = "Project Reports";

   void generate(Sink var1, Locale var2) throws MavenReportException;

   String getOutputName();

   String getCategoryName();

   String getName(Locale var1);

   String getDescription(Locale var1);

   void setReportOutputDirectory(File var1);

   File getReportOutputDirectory();

   boolean isExternalReport();

   boolean canGenerateReport();
}
