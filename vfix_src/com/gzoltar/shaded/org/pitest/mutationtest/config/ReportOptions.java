package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classpath.ClassFilter;
import com.gzoltar.shaded.org.pitest.classpath.ClassPath;
import com.gzoltar.shaded.org.pitest.classpath.ClassPathRoot;
import com.gzoltar.shaded.org.pitest.classpath.PathFilter;
import com.gzoltar.shaded.org.pitest.classpath.ProjectClassPaths;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.FileWriterFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.NullWriterFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.incremental.WriterFactory;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.testapi.execute.Pitest;
import com.gzoltar.shaded.org.pitest.util.Glob;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

public class ReportOptions {
   public static final Collection<String> LOGGING_CLASSES = Arrays.asList("java.util.logging", "org.apache.log4j", "org.slf4j", "org.apache.commons.logging");
   private Collection<Predicate<String>> targetClasses;
   private Collection<Predicate<String>> excludedMethods = Collections.emptyList();
   private Collection<Predicate<String>> excludedClasses = Collections.emptyList();
   private Collection<String> codePaths;
   private String reportDir;
   private File historyInputLocation;
   private File historyOutputLocation;
   private Collection<File> sourceDirs;
   private Collection<String> classPathElements;
   private Collection<String> mutators;
   private int dependencyAnalysisMaxDistance;
   private boolean mutateStaticInitializers = false;
   private final List<String> jvmArgs = new ArrayList();
   private int numberOfThreads = 0;
   private float timeoutFactor = 1.25F;
   private long timeoutConstant = 4000L;
   private Collection<Predicate<String>> targetTests;
   private Collection<String> loggingClasses = new ArrayList();
   private int maxMutationsPerClass;
   private boolean verbose = false;
   private boolean failWhenNoMutations = false;
   private final Collection<String> outputs = new LinkedHashSet();
   private TestGroupConfig groupConfig;
   private int mutationUnitSize;
   private boolean shouldCreateTimestampedReports = true;
   private boolean detectInlinedCode = false;
   private boolean exportLineCoverage = false;
   private int mutationThreshold;
   private int coverageThreshold;
   private String mutationEngine = "gregor";
   private String javaExecutable;
   private boolean includeLaunchClasspath = true;
   private Properties properties;

   public boolean isVerbose() {
      return this.verbose;
   }

   public String getReportDir() {
      return this.reportDir;
   }

   public void setReportDir(String reportDir) {
      this.reportDir = reportDir;
   }

   public Collection<File> getSourceDirs() {
      return this.sourceDirs;
   }

   public Collection<String> getClassPathElements() {
      return this.classPathElements;
   }

   public void setClassPathElements(Collection<String> classPathElements) {
      this.classPathElements = classPathElements;
   }

   public void setSourceDirs(Collection<File> sourceDirs) {
      this.sourceDirs = sourceDirs;
   }

   public Collection<String> getMutators() {
      return this.mutators;
   }

   public void setMutators(Collection<String> mutators) {
      this.mutators = mutators;
   }

   public int getDependencyAnalysisMaxDistance() {
      return this.dependencyAnalysisMaxDistance;
   }

   public void setDependencyAnalysisMaxDistance(int dependencyAnalysisMaxDistance) {
      this.dependencyAnalysisMaxDistance = dependencyAnalysisMaxDistance;
   }

   public List<String> getJvmArgs() {
      return this.jvmArgs;
   }

   public void addChildJVMArgs(List<String> args) {
      this.jvmArgs.addAll(args);
   }

   public ClassPath getClassPath() {
      return this.classPathElements != null ? this.createClassPathFromElements() : new ClassPath();
   }

   private ClassPath createClassPathFromElements() {
      return new ClassPath(FCollection.map(this.classPathElements, stringToFile()));
   }

   private static F<String, File> stringToFile() {
      return new F<String, File>() {
         public File apply(String a) {
            return new File(a);
         }
      };
   }

   public Collection<Predicate<String>> getTargetClasses() {
      return this.targetClasses;
   }

   public Predicate<String> getTargetClassesFilter() {
      Predicate<String> filter = Prelude.and(Prelude.or((Iterable)this.targetClasses), Prelude.not(isBlackListed(this.excludedClasses)));
      this.checkNotTryingToMutateSelf(filter);
      return filter;
   }

   private void checkNotTryingToMutateSelf(Predicate<String> filter) {
      if ((Boolean)filter.apply(Pitest.class.getName())) {
         throw new PitHelpError(Help.BAD_FILTER, new Object[0]);
      }
   }

   public void setTargetClasses(Collection<Predicate<String>> targetClasses) {
      this.targetClasses = targetClasses;
   }

   public void setTargetTests(Collection<Predicate<String>> targetTestsPredicates) {
      this.targetTests = targetTestsPredicates;
   }

   public boolean isMutateStaticInitializers() {
      return this.mutateStaticInitializers;
   }

   public void setMutateStaticInitializers(boolean mutateStaticInitializers) {
      this.mutateStaticInitializers = mutateStaticInitializers;
   }

   public int getNumberOfThreads() {
      return this.numberOfThreads;
   }

   public void setNumberOfThreads(int numberOfThreads) {
      this.numberOfThreads = numberOfThreads;
   }

   public float getTimeoutFactor() {
      return this.timeoutFactor;
   }

   public long getTimeoutConstant() {
      return this.timeoutConstant;
   }

   public void setTimeoutConstant(long timeoutConstant) {
      this.timeoutConstant = timeoutConstant;
   }

   public void setTimeoutFactor(float timeoutFactor) {
      this.timeoutFactor = timeoutFactor;
   }

   public Collection<Predicate<String>> getTargetTests() {
      return this.targetTests;
   }

   public Predicate<String> getTargetTestsFilter() {
      return (Predicate)(this.targetTests != null && !this.targetTests.isEmpty() ? Prelude.and(Prelude.or((Iterable)this.targetTests), Prelude.not(isBlackListed(this.excludedClasses))) : this.getTargetClassesFilter());
   }

   private static Predicate<String> isBlackListed(final Collection<Predicate<String>> excludedClasses) {
      return new Predicate<String>() {
         public Boolean apply(String a) {
            return Prelude.or((Iterable)excludedClasses).apply(a);
         }
      };
   }

   public Collection<String> getLoggingClasses() {
      return this.loggingClasses.isEmpty() ? LOGGING_CLASSES : this.loggingClasses;
   }

   public void setLoggingClasses(Collection<String> loggingClasses) {
      this.loggingClasses = loggingClasses;
   }

   public Collection<Predicate<String>> getExcludedMethods() {
      return this.excludedMethods;
   }

   public void setExcludedMethods(Collection<Predicate<String>> excludedMethods) {
      this.excludedMethods = excludedMethods;
   }

   public int getMaxMutationsPerClass() {
      return this.maxMutationsPerClass;
   }

   public void setMaxMutationsPerClass(int maxMutationsPerClass) {
      this.maxMutationsPerClass = maxMutationsPerClass;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public void setExcludedClasses(Collection<Predicate<String>> excludedClasses) {
      this.excludedClasses = excludedClasses;
   }

   public void addOutputFormats(Collection<String> formats) {
      this.outputs.addAll(formats);
   }

   public Collection<String> getOutputFormats() {
      return this.outputs;
   }

   public Collection<Predicate<String>> getExcludedClasses() {
      return this.excludedClasses;
   }

   public boolean shouldFailWhenNoMutations() {
      return this.failWhenNoMutations;
   }

   public void setFailWhenNoMutations(boolean failWhenNoMutations) {
      this.failWhenNoMutations = failWhenNoMutations;
   }

   public ProjectClassPaths getMutationClassPaths() {
      return new ProjectClassPaths(this.getClassPath(), this.createClassesFilter(), this.createPathFilter());
   }

   public ClassFilter createClassesFilter() {
      return new ClassFilter(this.getTargetTestsFilter(), this.getTargetClassesFilter());
   }

   private PathFilter createPathFilter() {
      return new PathFilter(this.createCodePathFilter(), Prelude.not(new DefaultDependencyPathPredicate()));
   }

   private Predicate<ClassPathRoot> createCodePathFilter() {
      return (Predicate)(this.codePaths != null && !this.codePaths.isEmpty() ? new PathNamePredicate(Prelude.or((Iterable)Glob.toGlobPredicates(this.codePaths))) : new DefaultCodePathPredicate());
   }

   public Collection<String> getCodePaths() {
      return this.codePaths;
   }

   public void setCodePaths(Collection<String> codePaths) {
      this.codePaths = codePaths;
   }

   public void setGroupConfig(TestGroupConfig groupConfig) {
      this.groupConfig = groupConfig;
   }

   public TestGroupConfig getGroupConfig() {
      return this.groupConfig;
   }

   public int getMutationUnitSize() {
      return this.mutationUnitSize;
   }

   public void setMutationUnitSize(int size) {
      this.mutationUnitSize = size;
   }

   public ResultOutputStrategy getReportDirectoryStrategy() {
      return new DirectoryResultOutputStrategy(this.getReportDir(), this.pickDirectoryStrategy());
   }

   public void setShouldCreateTimestampedReports(boolean shouldCreateTimestampedReports) {
      this.shouldCreateTimestampedReports = shouldCreateTimestampedReports;
   }

   private ReportDirCreationStrategy pickDirectoryStrategy() {
      return (ReportDirCreationStrategy)(this.shouldCreateTimestampedReports ? new DatedDirectoryReportDirCreationStrategy() : new UndatedReportDirCreationStrategy());
   }

   public boolean shouldCreateTimeStampedReports() {
      return this.shouldCreateTimestampedReports;
   }

   public boolean isDetectInlinedCode() {
      return this.detectInlinedCode;
   }

   public void setDetectInlinedCode(boolean b) {
      this.detectInlinedCode = b;
   }

   public WriterFactory createHistoryWriter() {
      return (WriterFactory)(this.historyOutputLocation == null ? new NullWriterFactory() : new FileWriterFactory(this.historyOutputLocation));
   }

   public Option<Reader> createHistoryReader() {
      if (this.historyInputLocation == null) {
         return Option.none();
      } else {
         try {
            return (Option)(this.historyInputLocation.exists() && this.historyInputLocation.length() > 0L ? Option.some(new InputStreamReader(new FileInputStream(this.historyInputLocation), "UTF-8")) : Option.none());
         } catch (IOException var2) {
            throw Unchecked.translateCheckedException(var2);
         }
      }
   }

   public void setHistoryInputLocation(File historyInputLocation) {
      this.historyInputLocation = historyInputLocation;
   }

   public void setHistoryOutputLocation(File historyOutputLocation) {
      this.historyOutputLocation = historyOutputLocation;
   }

   public File getHistoryInputLocation() {
      return this.historyInputLocation;
   }

   public File getHistoryOutputLocation() {
      return this.historyOutputLocation;
   }

   public void setExportLineCoverage(boolean value) {
      this.exportLineCoverage = value;
   }

   public boolean shouldExportLineCoverage() {
      return this.exportLineCoverage;
   }

   public int getMutationThreshold() {
      return this.mutationThreshold;
   }

   public void setMutationThreshold(int value) {
      this.mutationThreshold = value;
   }

   public String getMutationEngine() {
      return this.mutationEngine;
   }

   public void setMutationEngine(String mutationEngine) {
      this.mutationEngine = mutationEngine;
   }

   public int getCoverageThreshold() {
      return this.coverageThreshold;
   }

   public void setCoverageThreshold(int coverageThreshold) {
      this.coverageThreshold = coverageThreshold;
   }

   public String getJavaExecutable() {
      return this.javaExecutable;
   }

   public void setJavaExecutable(String javaExecutable) {
      this.javaExecutable = javaExecutable;
   }

   public void setIncludeLaunchClasspath(boolean b) {
      this.includeLaunchClasspath = b;
   }

   public boolean isIncludeLaunchClasspath() {
      return this.includeLaunchClasspath;
   }

   public Properties getFreeFormProperties() {
      return this.properties;
   }

   public void setFreeFormProperties(Properties props) {
      this.properties = props;
   }

   public String toString() {
      return "ReportOptions [targetClasses=" + this.targetClasses + ", excludedMethods=" + this.excludedMethods + ", excludedClasses=" + this.excludedClasses + ", codePaths=" + this.codePaths + ", reportDir=" + this.reportDir + ", historyInputLocation=" + this.historyInputLocation + ", historyOutputLocation=" + this.historyOutputLocation + ", sourceDirs=" + this.sourceDirs + ", classPathElements=" + this.classPathElements + ", mutators=" + this.mutators + ", dependencyAnalysisMaxDistance=" + this.dependencyAnalysisMaxDistance + ", mutateStaticInitializers=" + this.mutateStaticInitializers + ", jvmArgs=" + this.jvmArgs + ", numberOfThreads=" + this.numberOfThreads + ", timeoutFactor=" + this.timeoutFactor + ", timeoutConstant=" + this.timeoutConstant + ", targetTests=" + this.targetTests + ", loggingClasses=" + this.loggingClasses + ", maxMutationsPerClass=" + this.maxMutationsPerClass + ", verbose=" + this.verbose + ", failWhenNoMutations=" + this.failWhenNoMutations + ", outputs=" + this.outputs + ", groupConfig=" + this.groupConfig + ", mutationUnitSize=" + this.mutationUnitSize + ", shouldCreateTimestampedReports=" + this.shouldCreateTimestampedReports + ", detectInlinedCode=" + this.detectInlinedCode + ", exportLineCoverage=" + this.exportLineCoverage + ", mutationThreshold=" + this.mutationThreshold + ", coverageThreshold=" + this.coverageThreshold + ", mutationEngine=" + this.mutationEngine + ", javaExecutable=" + this.javaExecutable + ", includeLaunchClasspath=" + this.includeLaunchClasspath + "]";
   }
}
