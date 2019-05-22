package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;

public class ListenerArguments {
   private final ResultOutputStrategy outputStrategy;
   private final CoverageDatabase coverage;
   private final long startTime;
   private final SourceLocator locator;
   private final MutationEngine engine;

   public ListenerArguments(ResultOutputStrategy outputStrategy, CoverageDatabase coverage, SourceLocator locator, MutationEngine engine, long startTime) {
      this.outputStrategy = outputStrategy;
      this.coverage = coverage;
      this.locator = locator;
      this.startTime = startTime;
      this.engine = engine;
   }

   public ResultOutputStrategy getOutputStrategy() {
      return this.outputStrategy;
   }

   public CoverageDatabase getCoverage() {
      return this.coverage;
   }

   public long getStartTime() {
      return this.startTime;
   }

   public SourceLocator getLocator() {
      return this.locator;
   }

   public MutationEngine getEngine() {
      return this.engine;
   }
}
