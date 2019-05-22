package com.gzoltar.shaded.org.pitest.maven.report.generator;

public class XMLReportGenerator implements ReportGenerationStrategy {
   public ReportGenerationResultEnum generate(ReportGenerationContext context) {
      context.getLogger().debug((CharSequence)"XMLReportGenerator not yet implemented");
      return ReportGenerationResultEnum.NOT_EXECUTED;
   }

   public String getGeneratorName() {
      return "XMLReportGenerator";
   }

   public String getGeneratorDataFormat() {
      return "XML";
   }
}
