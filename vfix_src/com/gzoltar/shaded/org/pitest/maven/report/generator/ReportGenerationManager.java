package com.gzoltar.shaded.org.pitest.maven.report.generator;

import com.gzoltar.shaded.org.pitest.maven.report.ReportSourceLocator;
import com.gzoltar.shaded.org.pitest.util.PitError;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ReportGenerationManager {
   private final ReportSourceLocator reportLocator;
   private final List<ReportGenerationStrategy> reportGenerationStrategyList;

   public ReportGenerationManager() {
      this(new ReportSourceLocator(), Arrays.asList(new XMLReportGenerator(), new HTMLReportGenerator()));
   }

   public ReportGenerationManager(ReportSourceLocator reportLocator, List<ReportGenerationStrategy> reportGenerationStrategyList) {
      this.reportLocator = reportLocator;
      this.reportGenerationStrategyList = reportGenerationStrategyList;
   }

   public void generateSiteReport(ReportGenerationContext context) {
      boolean successfulExecution = false;
      context.setReportsDataDirectory(this.reportLocator.locate(context.getReportsDataDirectory(), context.getLogger()));
      context.getLogger().debug((CharSequence)"starting execution of report generators");
      context.getLogger().debug((CharSequence)("using report generation context: " + context));
      Iterator i$ = context.getSourceDataFormats().iterator();

      while(i$.hasNext()) {
         String dataFormat = (String)i$.next();
         context.getLogger().debug((CharSequence)("starting report generator for source data format [" + dataFormat + "]"));
         ReportGenerationResultEnum result = this.locateReportGenerationStrategy(dataFormat).generate(context);
         context.getLogger().debug((CharSequence)("result of report generator for source data format [" + dataFormat + "] was [" + result.toString() + "]"));
         if (result == ReportGenerationResultEnum.SUCCESS) {
            successfulExecution = true;
            break;
         }
      }

      if (!successfulExecution) {
         throw new PitError("no report generators executed successfully");
      } else {
         context.getLogger().debug((CharSequence)"finished execution of report generators");
      }
   }

   private ReportGenerationStrategy locateReportGenerationStrategy(String sourceDataFormat) {
      Iterator i$ = this.reportGenerationStrategyList.iterator();

      ReportGenerationStrategy strategy;
      do {
         if (!i$.hasNext()) {
            throw new PitError("Could not locate report generator for data source [" + sourceDataFormat + "]");
         }

         strategy = (ReportGenerationStrategy)i$.next();
      } while(!sourceDataFormat.equalsIgnoreCase(strategy.getGeneratorDataFormat()));

      return strategy;
   }
}
