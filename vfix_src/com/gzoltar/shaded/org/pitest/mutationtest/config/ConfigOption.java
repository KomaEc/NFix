package com.gzoltar.shaded.org.pitest.mutationtest.config;

import java.io.Serializable;

public enum ConfigOption {
   REPORT_DIR("reportDir"),
   TARGET_CLASSES("targetClasses"),
   SOURCE_DIR("sourceDirs"),
   MUTATIONS("mutators"),
   DEPENDENCY_DISTANCE("dependencyDistance", -1),
   CHILD_JVM("jvmArgs"),
   MUTATE_STATIC_INITIALIZERS("mutateStaticInits", false),
   TIME_STAMPED_REPORTS("timestampedReports", true),
   THREADS("threads", 1),
   TIMEOUT_FACTOR("timeoutFactor", 1.25F),
   TIMEOUT_CONST("timeoutConst", 4000L),
   TEST_FILTER("targetTests"),
   AVOID_CALLS("avoidCallsTo"),
   EXCLUDED_METHOD("excludedMethods"),
   MAX_MUTATIONS_PER_CLASS("maxMutationsPerClass", 0),
   VERBOSE("verbose", false),
   EXCLUDED_CLASSES("excludedClasses"),
   OUTPUT_FORMATS("outputFormats"),
   CLASSPATH("classPath"),
   FAIL_WHEN_NOT_MUTATIONS("failWhenNoMutations", true),
   CODE_PATHS("mutableCodePaths"),
   INCLUDED_GROUPS("includedGroups"),
   EXCLUDED_GROUPS("excludedGroups"),
   MUTATION_UNIT_SIZE("mutationUnitSize", 0),
   USE_INLINED_CODE_DETECTION("detectInlinedCode", true),
   HISTORY_INPUT_LOCATION("historyInputLocation"),
   HISTORY_OUTPUT_LOCATION("historyOutputLocation"),
   MUTATION_THRESHOLD("mutationThreshold", 0),
   COVERAGE_THRESHOLD("coverageThreshold", 0),
   MUTATION_ENGINE("mutationEngine", "gregor"),
   EXPORT_LINE_COVERAGE("exportLineCoverage", false),
   INCLUDE_LAUNCH_CLASSPATH("includeLaunchClasspath", true),
   JVM_PATH("jvmPath"),
   PLUGIN_CONFIGURATION("pluginConfiguration");

   private final String text;
   private final Serializable defaultValue;

   private ConfigOption(String text) {
      this(text, (Serializable)null);
   }

   private ConfigOption(String text, Serializable defaultValue) {
      this.text = text;
      this.defaultValue = defaultValue;
   }

   public String getParamName() {
      return this.text;
   }

   public <T> T getDefault(Class<T> type) {
      return this.defaultValue;
   }

   public String toString() {
      return this.text;
   }
}
