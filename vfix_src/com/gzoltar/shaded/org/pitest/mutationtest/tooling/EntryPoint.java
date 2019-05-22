package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.classpath.ClassPath;
import com.gzoltar.shaded.org.pitest.classpath.ClassPathByteArraySource;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.classpath.ProjectClassPaths;
import com.gzoltar.shaded.org.pitest.coverage.CoverageGenerator;
import com.gzoltar.shaded.org.pitest.coverage.execute.CoverageOptions;
import com.gzoltar.shaded.org.pitest.coverage.execute.DefaultCoverageGenerator;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.HistoryStore;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.config.PluginServices;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.mutationtest.config.SettingsFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.WriterFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.XStreamHistoryStore;
import com.gzoltar.shaded.org.pitest.process.JavaAgent;
import com.gzoltar.shaded.org.pitest.process.LaunchOptions;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.Timings;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class EntryPoint {
   public AnalysisResult execute(File baseDir, ReportOptions data, PluginServices plugins, Map<String, String> environmentVariables) {
      SettingsFactory settings = new SettingsFactory(data, plugins);
      return this.execute(baseDir, data, settings, environmentVariables);
   }

   public AnalysisResult execute(File baseDir, ReportOptions data, SettingsFactory settings, Map<String, String> environmentVariables) {
      ClassPath cp = data.getClassPath();
      Option<Reader> reader = data.createHistoryReader();
      WriterFactory historyWriter = data.createHistoryWriter();
      JavaAgent jac = new JarCreatingJarFinder(new ClassPathByteArraySource(cp));
      KnownLocationJavaAgentFinder ja = new KnownLocationJavaAgentFinder((String)jac.getJarLocation().value());
      ResultOutputStrategy reportOutput = settings.getOutputStrategy();
      MutationResultListenerFactory reportFactory = settings.createListener();
      CoverageOptions coverageOptions = settings.createCoverageOptions();
      LaunchOptions launchOptions = new LaunchOptions(ja, settings.getJavaExecutable(), data.getJvmArgs(), environmentVariables);
      ProjectClassPaths cps = data.getMutationClassPaths();
      CodeSource code = new CodeSource(cps, coverageOptions.getPitConfig().testClassIdentifier());
      Timings timings = new Timings();
      CoverageGenerator coverageDatabase = new DefaultCoverageGenerator(baseDir, coverageOptions, launchOptions, code, settings.createCoverageExporter(), timings, !data.isVerbose());
      HistoryStore history = new XStreamHistoryStore(historyWriter, reader);
      MutationStrategies strategies = new MutationStrategies(settings.createEngine(), history, coverageDatabase, reportFactory, reportOutput);
      MutationCoverage report = new MutationCoverage(strategies, baseDir, code, data, settings, timings);

      AnalysisResult var22;
      try {
         AnalysisResult var21 = AnalysisResult.success(report.runReport());
         return var21;
      } catch (IOException var26) {
         var22 = AnalysisResult.fail(var26);
      } finally {
         jac.close();
         ja.close();
         historyWriter.close();
      }

      return var22;
   }
}
