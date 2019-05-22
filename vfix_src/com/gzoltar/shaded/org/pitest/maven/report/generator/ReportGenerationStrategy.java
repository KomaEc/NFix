package com.gzoltar.shaded.org.pitest.maven.report.generator;

public interface ReportGenerationStrategy {
   ReportGenerationResultEnum generate(ReportGenerationContext var1);

   String getGeneratorName();

   String getGeneratorDataFormat();
}
