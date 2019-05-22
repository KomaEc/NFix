package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.mutationtest.config.PluginServices;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.mutationtest.tooling.AnalysisResult;
import com.gzoltar.shaded.org.pitest.mutationtest.tooling.CombinedStatistics;
import com.gzoltar.shaded.org.pitest.mutationtest.tooling.EntryPoint;
import java.io.File;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;

public class RunPitStrategy implements GoalStrategy {
   public CombinedStatistics execute(File baseDir, ReportOptions data, PluginServices plugins, Map<String, String> environmentVariables) throws MojoExecutionException {
      EntryPoint e = new EntryPoint();
      AnalysisResult result = e.execute(baseDir, data, plugins, environmentVariables);
      if (result.getError().hasSome()) {
         throw new MojoExecutionException("fail", (Exception)result.getError().value());
      } else {
         return (CombinedStatistics)result.getStatistics().value();
      }
   }
}
