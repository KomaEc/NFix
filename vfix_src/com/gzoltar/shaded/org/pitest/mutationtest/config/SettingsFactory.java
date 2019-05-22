package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.classpath.ClassPathByteArraySource;
import com.gzoltar.shaded.org.pitest.coverage.CoverageExporter;
import com.gzoltar.shaded.org.pitest.coverage.execute.CoverageOptions;
import com.gzoltar.shaded.org.pitest.coverage.export.DefaultCoverageExporter;
import com.gzoltar.shaded.org.pitest.coverage.export.NullCoverageExporter;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationEngineFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.build.DefaultMutationGrouperFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.build.DefaultTestPrioritiserFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.build.MutationGrouperFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.build.TestPrioritiserFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.filter.CompoundFilterFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.filter.MutationFilterFactory;
import com.gzoltar.shaded.org.pitest.process.DefaultJavaExecutableLocator;
import com.gzoltar.shaded.org.pitest.process.JavaExecutableLocator;
import com.gzoltar.shaded.org.pitest.process.KnownLocationJavaExecutableLocator;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestPluginFactory;
import com.gzoltar.shaded.org.pitest.util.PitError;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.StringUtil;
import java.util.Collection;
import java.util.Iterator;

public class SettingsFactory {
   private final ReportOptions options;
   private final PluginServices plugins;

   public SettingsFactory(ReportOptions options, PluginServices plugins) {
      this.options = options;
      this.plugins = plugins;
   }

   public ResultOutputStrategy getOutputStrategy() {
      return this.options.getReportDirectoryStrategy();
   }

   public CoverageExporter createCoverageExporter() {
      return (CoverageExporter)(this.options.shouldExportLineCoverage() ? new DefaultCoverageExporter(this.getOutputStrategy()) : new NullCoverageExporter());
   }

   public MutationEngineFactory createEngine() {
      Iterator i$ = this.plugins.findMutationEngines().iterator();

      MutationEngineFactory each;
      do {
         if (!i$.hasNext()) {
            throw new PitError("Could not load requested engine " + this.options.getMutationEngine());
         }

         each = (MutationEngineFactory)i$.next();
      } while(!each.name().equals(this.options.getMutationEngine()));

      return each;
   }

   public MutationResultListenerFactory createListener() {
      return new CompoundListenerFactory(this.findListeners());
   }

   public JavaExecutableLocator getJavaExecutable() {
      return (JavaExecutableLocator)(this.options.getJavaExecutable() != null ? new KnownLocationJavaExecutableLocator(this.options.getJavaExecutable()) : new DefaultJavaExecutableLocator());
   }

   public MutationGrouperFactory getMutationGrouper() {
      Collection<? extends MutationGrouperFactory> groupers = this.plugins.findGroupers();
      return (MutationGrouperFactory)firstOrDefault(groupers, new DefaultMutationGrouperFactory());
   }

   private Iterable<MutationResultListenerFactory> findListeners() {
      Iterable<? extends MutationResultListenerFactory> listeners = this.plugins.findListeners();
      Collection<MutationResultListenerFactory> matches = FCollection.filter(listeners, nameMatches(this.options.getOutputFormats()));
      if (matches.size() < this.options.getOutputFormats().size()) {
         throw new PitError("Unknown listener requested in " + StringUtil.join(this.options.getOutputFormats(), ","));
      } else {
         return matches;
      }
   }

   private static F<MutationResultListenerFactory, Boolean> nameMatches(final Iterable<String> outputFormats) {
      return new F<MutationResultListenerFactory, Boolean>() {
         public Boolean apply(MutationResultListenerFactory a) {
            return FCollection.contains(outputFormats, SettingsFactory.equalsIgnoreCase(a.name()));
         }
      };
   }

   private static Predicate<String> equalsIgnoreCase(final String other) {
      return new Predicate<String>() {
         public Boolean apply(String a) {
            return a.equalsIgnoreCase(other);
         }
      };
   }

   public MutationFilterFactory createMutationFilter() {
      Collection<? extends MutationFilterFactory> filters = this.plugins.findFilters();
      return new CompoundFilterFactory(filters);
   }

   public TestPrioritiserFactory getTestPrioritiser() {
      Collection<? extends TestPrioritiserFactory> testPickers = this.plugins.findTestPrioritisers();
      return (TestPrioritiserFactory)firstOrDefault(testPickers, new DefaultTestPrioritiserFactory());
   }

   public Configuration getTestFrameworkPlugin() {
      Collection<? extends TestPluginFactory> testPlugins = this.plugins.findTestFrameworkPlugins();
      return ((TestPluginFactory)firstOrDefault(testPlugins, new LegacyTestFrameworkPlugin())).createTestFrameworkConfiguration(this.options.getGroupConfig(), new ClassPathByteArraySource(this.options.getClassPath()));
   }

   public CoverageOptions createCoverageOptions() {
      return new CoverageOptions(Prelude.and(this.options.getTargetClassesFilter(), Prelude.not(commonClasses())), this.getTestFrameworkPlugin(), this.options.isVerbose(), this.options.getDependencyAnalysisMaxDistance());
   }

   private static F<String, Boolean> commonClasses() {
      return new F<String, Boolean>() {
         public Boolean apply(String name) {
            return name.startsWith("java") || name.startsWith("sun/") || name.startsWith("org/junit") || name.startsWith("junit") || name.startsWith("com/gzoltar/shaded/org/pitest/coverage") || name.startsWith("com/gzoltar/shaded/org/pitest/reloc") || name.startsWith("com/gzoltar/shaded/org/pitest/boot");
         }
      };
   }

   private static <T> T firstOrDefault(Collection<? extends T> found, T defaultInstance) {
      if (found.isEmpty()) {
         return defaultInstance;
      } else if (found.size() > 1) {
         throw new PitError("Multiple implementations of plugin detected on classpath");
      } else {
         return found.iterator().next();
      }
   }
}
