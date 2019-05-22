package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.mutationtest.config.PluginServices;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.mutationtest.tooling.CombinedStatistics;
import java.io.File;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;

public interface GoalStrategy {
   CombinedStatistics execute(File var1, ReportOptions var2, PluginServices var3, Map<String, String> var4) throws MojoExecutionException;
}
