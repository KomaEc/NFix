package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.HierarchicalClassId;
import com.gzoltar.shaded.org.pitest.classpath.ClassPathByteArraySource;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.coverage.CoverageGenerator;
import com.gzoltar.shaded.org.pitest.coverage.TestInfo;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.mutationtest.HistoryStore;
import com.gzoltar.shaded.org.pitest.mutationtest.ListenerArguments;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationAnalyser;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationConfig;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationAnalysisUnit;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationGrouper;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationSource;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationTestBuilder;
import com.gzoltar.shaded.org.pitest.mutationtest.build.PercentAndConstantTimeoutStrategy;
import com.gzoltar.shaded.org.pitest.mutationtest.build.TestPrioritiser;
import com.gzoltar.shaded.org.pitest.mutationtest.build.WorkerFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.mutationtest.config.SettingsFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.mutationtest.execute.MutationAnalysisExecutor;
import com.gzoltar.shaded.org.pitest.mutationtest.filter.MutationFilterFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.DefaultCodeHistory;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.HistoryListener;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.IncrementalAnalyser;
import com.gzoltar.shaded.org.pitest.mutationtest.statistics.MutationStatisticsListener;
import com.gzoltar.shaded.org.pitest.mutationtest.statistics.Score;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.StringUtil;
import com.gzoltar.shaded.org.pitest.util.Timings;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MutationCoverage {
   private static final int MB = 1048576;
   private static final Logger LOG = Log.getLogger();
   private final ReportOptions data;
   private final MutationStrategies strategies;
   private final Timings timings;
   private final CodeSource code;
   private final File baseDir;
   private final SettingsFactory settings;

   public MutationCoverage(MutationStrategies strategies, File baseDir, CodeSource code, ReportOptions data, SettingsFactory settings, Timings timings) {
      this.strategies = strategies;
      this.data = data;
      this.settings = settings;
      this.timings = timings;
      this.code = code;
      this.baseDir = baseDir;
   }

   public CombinedStatistics runReport() throws IOException {
      Log.setVerbose(this.data.isVerbose());
      Runtime runtime = Runtime.getRuntime();
      if (!this.data.isVerbose()) {
         LOG.info("Verbose logging is disabled. If you encounter an problem please enable it before reporting an issue.");
      }

      LOG.fine("Running report with " + this.data);
      LOG.fine("System class path is " + System.getProperty("java.class.path"));
      LOG.fine("Maximum available memory is " + runtime.maxMemory() / 1048576L + " mb");
      long t0 = System.currentTimeMillis();
      this.verifyBuildSuitableForMutationTesting();
      CoverageDatabase coverageData = this.coverage().calculateCoverage();
      LOG.fine("Used memory after coverage calculation " + (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + " mb");
      LOG.fine("Free Memory after coverage calculation " + runtime.freeMemory() / 1048576L + " mb");
      MutationStatisticsListener stats = new MutationStatisticsListener();
      MutationEngine engine = this.strategies.factory().createEngine(this.data.isMutateStaticInitializers(), Prelude.or((Iterable)this.data.getExcludedMethods()), this.data.getLoggingClasses(), this.data.getMutators(), this.data.isDetectInlinedCode());
      List<MutationResultListener> config = this.createConfig(t0, coverageData, stats, engine);
      this.history().initialize();
      this.timings.registerStart(Timings.Stage.BUILD_MUTATION_TESTS);
      List<MutationAnalysisUnit> tus = this.buildMutationTests(coverageData, engine);
      this.timings.registerEnd(Timings.Stage.BUILD_MUTATION_TESTS);
      LOG.info("Created  " + tus.size() + " mutation test units");
      this.checkMutationsFound(tus);
      this.recordClassPath(coverageData);
      LOG.fine("Used memory before analysis start " + (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + " mb");
      LOG.fine("Free Memory before analysis start " + runtime.freeMemory() / 1048576L + " mb");
      MutationAnalysisExecutor mae = new MutationAnalysisExecutor(this.numberOfThreads(), config);
      this.timings.registerStart(Timings.Stage.RUN_MUTATION_TESTS);
      mae.run(tus);
      this.timings.registerEnd(Timings.Stage.RUN_MUTATION_TESTS);
      LOG.info("Completed in " + this.timeSpan(t0));
      this.printStats(stats);
      return new CombinedStatistics(stats.getStatistics(), coverageData.createSummary());
   }

   private int numberOfThreads() {
      return Math.max(1, this.data.getNumberOfThreads());
   }

   private List<MutationResultListener> createConfig(long t0, CoverageDatabase coverageData, MutationStatisticsListener stats, MutationEngine engine) {
      List<MutationResultListener> ls = new ArrayList();
      ls.add(stats);
      ListenerArguments args = new ListenerArguments(this.strategies.output(), coverageData, new SmartSourceLocator(this.data.getSourceDirs()), engine, t0);
      MutationResultListener mutationReportListener = this.strategies.listenerFactory().getListener(this.data.getFreeFormProperties(), args);
      ls.add(mutationReportListener);
      ls.add(new HistoryListener(this.history()));
      if (!this.data.isVerbose()) {
         ls.add(new SpinnerListener(System.out));
      }

      return ls;
   }

   private void recordClassPath(CoverageDatabase coverageData) {
      Set<ClassName> allClassNames = this.getAllClassesAndTests(coverageData);
      Collection<HierarchicalClassId> ids = FCollection.map(this.code.getClassInfo(allClassNames), ClassInfo.toFullClassId());
      this.history().recordClassPath(ids, coverageData);
   }

   private Set<ClassName> getAllClassesAndTests(CoverageDatabase coverageData) {
      Set<ClassName> names = new HashSet();
      Iterator i$ = this.code.getCodeUnderTestNames().iterator();

      while(i$.hasNext()) {
         ClassName each = (ClassName)i$.next();
         names.add(each);
         FCollection.mapTo(coverageData.getTestsForClass(each), TestInfo.toDefiningClassName(), names);
      }

      return names;
   }

   private void verifyBuildSuitableForMutationTesting() {
      this.strategies.buildVerifier().verify(this.code);
   }

   private void printStats(MutationStatisticsListener stats) {
      PrintStream ps = System.out;
      ps.println(StringUtil.separatorLine('='));
      ps.println("- Timings");
      ps.println(StringUtil.separatorLine('='));
      this.timings.report(ps);
      ps.println(StringUtil.separatorLine('='));
      ps.println("- Statistics");
      ps.println(StringUtil.separatorLine('='));
      stats.getStatistics().report(ps);
      ps.println(StringUtil.separatorLine('='));
      ps.println("- Mutators");
      ps.println(StringUtil.separatorLine('='));
      Iterator i$ = stats.getStatistics().getScores().iterator();

      while(i$.hasNext()) {
         Score each = (Score)i$.next();
         each.report(ps);
         ps.println(StringUtil.separatorLine());
      }

   }

   private List<MutationAnalysisUnit> buildMutationTests(CoverageDatabase coverageData, MutationEngine engine) {
      MutationConfig mutationConfig = new MutationConfig(engine, this.coverage().getLaunchOptions());
      ClassByteArraySource bas = new ClassPathByteArraySource(this.data.getClassPath());
      TestPrioritiser testPrioritiser = this.settings.getTestPrioritiser().makeTestPrioritiser(this.data.getFreeFormProperties(), this.code, coverageData);
      MutationSource source = new MutationSource(mutationConfig, this.makeFilter().createFilter(this.data.getFreeFormProperties(), this.code, this.data.getMaxMutationsPerClass()), testPrioritiser, bas);
      MutationAnalyser analyser = new IncrementalAnalyser(new DefaultCodeHistory(this.code, this.history()), coverageData);
      WorkerFactory wf = new WorkerFactory(this.baseDir, this.coverage().getConfiguration(), mutationConfig, new PercentAndConstantTimeoutStrategy(this.data.getTimeoutFactor(), this.data.getTimeoutConstant()), this.data.isVerbose(), this.data.getClassPath().getLocalClassPath());
      MutationGrouper grouper = this.settings.getMutationGrouper().makeFactory(this.data.getFreeFormProperties(), this.code, this.data.getNumberOfThreads(), this.data.getMutationUnitSize());
      MutationTestBuilder builder = new MutationTestBuilder(wf, analyser, source, grouper);
      return builder.createMutationTestUnits(this.code.getCodeUnderTestNames());
   }

   private MutationFilterFactory makeFilter() {
      return this.settings.createMutationFilter();
   }

   private void checkMutationsFound(List<MutationAnalysisUnit> tus) {
      if (tus.isEmpty()) {
         if (this.data.shouldFailWhenNoMutations()) {
            throw new PitHelpError(Help.NO_MUTATIONS_FOUND, new Object[0]);
         }

         LOG.warning(Help.NO_MUTATIONS_FOUND.toString());
      }

   }

   private String timeSpan(long t0) {
      return "" + (System.currentTimeMillis() - t0) / 1000L + " seconds";
   }

   private CoverageGenerator coverage() {
      return this.strategies.coverage();
   }

   private HistoryStore history() {
      return this.strategies.history();
   }
}
