package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.coverage.CoverageSummary;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.config.PluginServices;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.mutationtest.statistics.MutationStatistics;
import com.gzoltar.shaded.org.pitest.mutationtest.tooling.CombinedStatistics;
import com.gzoltar.shaded.org.pitest.plugin.ClientClasspathPlugin;
import com.gzoltar.shaded.org.pitest.plugin.ToolClasspathPlugin;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class PitMojo extends AbstractMojo {
   protected final Predicate<Artifact> filter;
   protected final PluginServices plugins;
   protected ArrayList<String> targetClasses;
   protected ArrayList<String> targetTests;
   private ArrayList<String> excludedMethods;
   private ArrayList<String> excludedClasses;
   private ArrayList<String> avoidCallsTo;
   private File reportsDirectory;
   private File historyOutputFile;
   private File historyInputFile;
   private int maxDependencyDistance;
   private int threads;
   private boolean mutateStaticInitializers;
   private boolean detectInlinedCode;
   private ArrayList<String> mutators;
   private float timeoutFactor;
   private long timeoutConstant;
   private int maxMutationsPerClass;
   private ArrayList<String> jvmArgs;
   private ArrayList<String> outputFormats;
   private boolean verbose;
   private boolean failWhenNoMutations;
   private boolean timestampedReports;
   private ArrayList<String> excludedGroups;
   private ArrayList<String> includedGroups;
   private int mutationUnitSize;
   private boolean exportLineCoverage;
   private int mutationThreshold;
   private int coverageThreshold;
   private String jvm;
   private String mutationEngine;
   private ArrayList<String> additionalClasspathElements;
   private ArrayList<String> classpathDependencyExcludes;
   private boolean skip;
   private boolean parseSurefireConfig;
   private boolean skipTests;
   private boolean useSlf4j;
   private Map<String, String> pluginConfiguration;
   private Map<String, String> environmentVariables;
   protected MavenProject project;
   private Map<String, Artifact> pluginArtifactMap;
   protected final GoalStrategy goalStrategy;

   public PitMojo() {
      this(new RunPitStrategy(), new DependencyFilter(new PluginServices(PitMojo.class.getClassLoader())), new PluginServices(PitMojo.class.getClassLoader()));
   }

   public PitMojo(GoalStrategy strategy, Predicate<Artifact> filter, PluginServices plugins) {
      this.environmentVariables = new HashMap();
      this.goalStrategy = strategy;
      this.filter = filter;
      this.plugins = plugins;
   }

   public final void execute() throws MojoExecutionException, MojoFailureException {
      this.switchLogging();
      if (this.shouldRun()) {
         Iterator i$ = this.plugins.findToolClasspathPlugins().iterator();

         while(i$.hasNext()) {
            ToolClasspathPlugin each = (ToolClasspathPlugin)i$.next();
            this.getLog().info((CharSequence)("Found plugin : " + each.description()));
         }

         i$ = this.plugins.findClientClasspathPlugins().iterator();

         while(i$.hasNext()) {
            ClientClasspathPlugin each = (ClientClasspathPlugin)i$.next();
            this.getLog().info((CharSequence)("Found shared classpath plugin : " + each.description()));
         }

         Option<CombinedStatistics> result = this.analyse();
         if (result.hasSome()) {
            this.throwErrorIfScoreBelowThreshold(((CombinedStatistics)result.value()).getMutationStatistics());
            this.throwErrorIfCoverageBelowThreshold(((CombinedStatistics)result.value()).getCoverageSummary());
         }
      } else {
         this.getLog().info((CharSequence)"Skipping project");
      }

   }

   private void switchLogging() {
      if (this.useSlf4j) {
         SLF4JBridgeHandler.removeHandlersForRootLogger();
         SLF4JBridgeHandler.install();
         Logger.getLogger("PIT").addHandler(new SLF4JBridgeHandler());
         SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
      }

   }

   private void throwErrorIfCoverageBelowThreshold(CoverageSummary coverageSummary) throws MojoFailureException {
      if (this.coverageThreshold != 0 && coverageSummary.getCoverage() < this.coverageThreshold) {
         throw new MojoFailureException("Line coverage of " + coverageSummary.getCoverage() + "(" + coverageSummary.getNumberOfCoveredLines() + "/" + coverageSummary.getNumberOfLines() + ") is below threshold of " + this.coverageThreshold);
      }
   }

   private void throwErrorIfScoreBelowThreshold(MutationStatistics result) throws MojoFailureException {
      if (this.mutationThreshold != 0 && result.getPercentageDetected() < (long)this.mutationThreshold) {
         throw new MojoFailureException("Mutation score of " + result.getPercentageDetected() + " is below threshold of " + this.mutationThreshold);
      }
   }

   protected Option<CombinedStatistics> analyse() throws MojoExecutionException {
      ReportOptions data = (new MojoToReportOptionsConverter(this, new SurefireConfigConverter(), this.filter)).convert();
      return Option.some(this.goalStrategy.execute(this.detectBaseDir(), data, this.plugins, this.environmentVariables));
   }

   protected File detectBaseDir() {
      MavenProject executionProject = this.project.getExecutionProject();
      return executionProject == null ? null : executionProject.getBasedir();
   }

   public List<String> getTargetClasses() {
      return this.targetClasses;
   }

   public List<String> getTargetTests() {
      return this.targetTests;
   }

   public List<String> getExcludedMethods() {
      return this.excludedMethods;
   }

   public List<String> getExcludedClasses() {
      return this.excludedClasses;
   }

   public List<String> getAvoidCallsTo() {
      return this.avoidCallsTo;
   }

   public File getReportsDirectory() {
      return this.reportsDirectory;
   }

   public int getMaxDependencyDistance() {
      return this.maxDependencyDistance;
   }

   public int getThreads() {
      return this.threads;
   }

   public boolean isMutateStaticInitializers() {
      return this.mutateStaticInitializers;
   }

   public List<String> getMutators() {
      return this.mutators;
   }

   public float getTimeoutFactor() {
      return this.timeoutFactor;
   }

   public long getTimeoutConstant() {
      return this.timeoutConstant;
   }

   public int getMaxMutationsPerClass() {
      return this.maxMutationsPerClass;
   }

   public List<String> getJvmArgs() {
      return this.jvmArgs;
   }

   public List<String> getOutputFormats() {
      return this.outputFormats;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public MavenProject getProject() {
      return this.project;
   }

   public Map<String, Artifact> getPluginArtifactMap() {
      return this.pluginArtifactMap;
   }

   public boolean isFailWhenNoMutations() {
      return this.failWhenNoMutations;
   }

   public List<String> getExcludedGroups() {
      return this.excludedGroups;
   }

   public List<String> getIncludedGroups() {
      return this.includedGroups;
   }

   public int getMutationUnitSize() {
      return this.mutationUnitSize;
   }

   public boolean isTimestampedReports() {
      return this.timestampedReports;
   }

   public boolean isDetectInlinedCode() {
      return this.detectInlinedCode;
   }

   public void setTimestampedReports(boolean timestampedReports) {
      this.timestampedReports = timestampedReports;
   }

   public File getHistoryOutputFile() {
      return this.historyOutputFile;
   }

   public File getHistoryInputFile() {
      return this.historyInputFile;
   }

   public boolean isExportLineCoverage() {
      return this.exportLineCoverage;
   }

   protected boolean shouldRun() {
      return !this.skip && !this.skipTests && !this.project.getPackaging().equalsIgnoreCase("pom");
   }

   public String getMutationEngine() {
      return this.mutationEngine;
   }

   public String getJavaExecutable() {
      return this.jvm;
   }

   public void setJavaExecutable(String javaExecutable) {
      this.jvm = javaExecutable;
   }

   public List<String> getAdditionalClasspathElements() {
      return this.additionalClasspathElements;
   }

   public List<String> getClasspathDependencyExcludes() {
      return this.classpathDependencyExcludes;
   }

   public boolean isParseSurefireConfig() {
      return this.parseSurefireConfig;
   }

   public Map<String, String> getPluginProperties() {
      return this.pluginConfiguration;
   }

   public Map<String, String> getEnvironmentVariables() {
      return this.environmentVariables;
   }
}
