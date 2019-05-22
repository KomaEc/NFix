package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.testapi.TestGroupConfig;
import com.gzoltar.shaded.org.pitest.util.Glob;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class MojoToReportOptionsConverter {
   private final PitMojo mojo;
   private final Predicate<Artifact> dependencyFilter;
   private final Log log;
   private final SurefireConfigConverter surefireConverter;

   public MojoToReportOptionsConverter(PitMojo mojo, SurefireConfigConverter surefireConverter, Predicate<Artifact> dependencyFilter) {
      this.mojo = mojo;
      this.dependencyFilter = dependencyFilter;
      this.log = mojo.getLog();
      this.surefireConverter = surefireConverter;
   }

   public ReportOptions convert() {
      ArrayList classPath = new ArrayList();

      try {
         classPath.addAll(this.mojo.getProject().getTestClasspathElements());
      } catch (DependencyResolutionRequiredException var5) {
         this.log.info((Throwable)var5);
      }

      this.addOwnDependenciesToClassPath(classPath);
      classPath.addAll(this.mojo.getAdditionalClasspathElements());
      Iterator i$ = this.mojo.getProject().getArtifacts().iterator();

      while(i$.hasNext()) {
         Object artifact = i$.next();
         Artifact dependency = (Artifact)artifact;
         if (this.mojo.getClasspathDependencyExcludes().contains(dependency.getGroupId() + ":" + dependency.getArtifactId())) {
            classPath.remove(dependency.getFile().getPath());
         }
      }

      ReportOptions option = this.parseReportOptions(classPath);
      return this.updateFromSurefire(option);
   }

   private ReportOptions parseReportOptions(List<String> classPath) {
      ReportOptions data = new ReportOptions();
      if (this.mojo.getProject().getBuild() != null) {
         this.log.info((CharSequence)("Mutating from " + this.mojo.getProject().getBuild().getOutputDirectory()));
         data.setCodePaths(Collections.singleton(this.mojo.getProject().getBuild().getOutputDirectory()));
      }

      data.setClassPathElements(classPath);
      data.setDependencyAnalysisMaxDistance(this.mojo.getMaxDependencyDistance());
      data.setFailWhenNoMutations(this.shouldFailWhenNoMutations());
      data.setTargetClasses(this.determineTargetClasses());
      data.setTargetTests(this.determineTargetTests());
      data.setMutateStaticInitializers(this.mojo.isMutateStaticInitializers());
      data.setExcludedMethods(this.globStringsToPredicates(this.mojo.getExcludedMethods()));
      data.setExcludedClasses(this.globStringsToPredicates(this.mojo.getExcludedClasses()));
      data.setNumberOfThreads(this.mojo.getThreads());
      data.setMaxMutationsPerClass(this.mojo.getMaxMutationsPerClass());
      data.setReportDir(this.mojo.getReportsDirectory().getAbsolutePath());
      data.setVerbose(this.mojo.isVerbose());
      if (this.mojo.getJvmArgs() != null) {
         data.addChildJVMArgs(this.mojo.getJvmArgs());
      }

      data.setMutators(this.determineMutators());
      data.setTimeoutConstant(this.mojo.getTimeoutConstant());
      data.setTimeoutFactor(this.mojo.getTimeoutFactor());
      if (this.hasValue(this.mojo.getAvoidCallsTo())) {
         data.setLoggingClasses(this.mojo.getAvoidCallsTo());
      }

      List<String> sourceRoots = new ArrayList();
      sourceRoots.addAll(this.mojo.getProject().getCompileSourceRoots());
      sourceRoots.addAll(this.mojo.getProject().getTestCompileSourceRoots());
      data.setSourceDirs(this.stringsTofiles(sourceRoots));
      data.addOutputFormats(this.determineOutputFormats());
      this.setTestGroups(data);
      data.setMutationUnitSize(this.mojo.getMutationUnitSize());
      data.setShouldCreateTimestampedReports(this.mojo.isTimestampedReports());
      data.setDetectInlinedCode(this.mojo.isDetectInlinedCode());
      data.setHistoryInputLocation(this.mojo.getHistoryInputFile());
      data.setHistoryOutputLocation(this.mojo.getHistoryOutputFile());
      data.setExportLineCoverage(this.mojo.isExportLineCoverage());
      data.setMutationEngine(this.mojo.getMutationEngine());
      data.setJavaExecutable(this.mojo.getJavaExecutable());
      data.setFreeFormProperties(this.createPluginProperties());
      return data;
   }

   private ReportOptions updateFromSurefire(ReportOptions option) {
      Collection<Plugin> plugins = this.lookupPlugin("org.apache.maven.plugins:maven-surefire-plugin");
      if (!this.mojo.isParseSurefireConfig()) {
         return option;
      } else if (plugins.isEmpty()) {
         this.log.warn((CharSequence)"Could not find surefire configuration in pom");
         return option;
      } else {
         Plugin surefire = (Plugin)plugins.iterator().next();
         return surefire != null ? this.surefireConverter.update(option, (Xpp3Dom)surefire.getConfiguration()) : option;
      }
   }

   private Collection<Plugin> lookupPlugin(String key) {
      List<Plugin> plugins = this.mojo.getProject().getBuildPlugins();
      return FCollection.filter(plugins, hasKey(key));
   }

   private static F<Plugin, Boolean> hasKey(final String key) {
      return new F<Plugin, Boolean>() {
         public Boolean apply(Plugin a) {
            return a.getKey().equals(key);
         }
      };
   }

   private boolean shouldFailWhenNoMutations() {
      return this.mojo.isFailWhenNoMutations();
   }

   private void setTestGroups(ReportOptions data) {
      TestGroupConfig conf = new TestGroupConfig(this.mojo.getExcludedGroups(), this.mojo.getIncludedGroups());
      data.setGroupConfig(conf);
   }

   private void addOwnDependenciesToClassPath(List<String> classPath) {
      Iterator i$ = this.filteredDependencies().iterator();

      while(i$.hasNext()) {
         Artifact dependency = (Artifact)i$.next();
         this.log.info((CharSequence)("Adding " + dependency.getGroupId() + ":" + dependency.getArtifactId() + " to SUT classpath"));
         classPath.add(dependency.getFile().getAbsolutePath());
      }

   }

   private Collection<Predicate<String>> globStringsToPredicates(List<String> excludedMethods) {
      return FCollection.map(excludedMethods, Glob.toGlobPredicate());
   }

   private Collection<Predicate<String>> determineTargetTests() {
      return FCollection.map(this.mojo.getTargetTests(), Glob.toGlobPredicate());
   }

   private Collection<Artifact> filteredDependencies() {
      return FCollection.filter(this.mojo.getPluginArtifactMap().values(), this.dependencyFilter);
   }

   private Collection<String> determineMutators() {
      return this.mojo.getMutators() != null ? this.mojo.getMutators() : Collections.emptyList();
   }

   private Collection<Predicate<String>> determineTargetClasses() {
      return this.returnOrDefaultToClassesLikeGroupName(this.mojo.getTargetClasses());
   }

   private Collection<Predicate<String>> returnOrDefaultToClassesLikeGroupName(Collection<String> filters) {
      if (!this.hasValue(filters)) {
         String groupId = this.mojo.getProject().getGroupId() + "*";
         this.mojo.getLog().info((CharSequence)("Defaulting to group id (" + groupId + ")"));
         return Collections.singleton(new Glob(groupId));
      } else {
         return FCollection.map(filters, Glob.toGlobPredicate());
      }
   }

   private Collection<File> stringsTofiles(List<String> sourceRoots) {
      return FCollection.map(sourceRoots, this.stringToFile());
   }

   private F<String, File> stringToFile() {
      return new F<String, File>() {
         public File apply(String a) {
            return new File(a);
         }
      };
   }

   private Collection<String> determineOutputFormats() {
      return this.hasValue(this.mojo.getOutputFormats()) ? this.mojo.getOutputFormats() : Arrays.asList("HTML");
   }

   private boolean hasValue(Collection<?> collection) {
      return collection != null && !collection.isEmpty();
   }

   private Properties createPluginProperties() {
      Properties p = new Properties();
      if (this.mojo.getPluginProperties() != null) {
         p.putAll(this.mojo.getPluginProperties());
      }

      return p;
   }
}
