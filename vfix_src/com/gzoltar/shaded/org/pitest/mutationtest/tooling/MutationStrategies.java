package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.coverage.CoverageGenerator;
import com.gzoltar.shaded.org.pitest.mutationtest.HistoryStore;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationEngineFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.verify.BuildVerifier;
import com.gzoltar.shaded.org.pitest.mutationtest.verify.DefaultBuildVerifier;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;

public class MutationStrategies {
   private final HistoryStore history;
   private final CoverageGenerator coverage;
   private final MutationResultListenerFactory listenerFactory;
   private final BuildVerifier buildVerifier;
   private final MutationEngineFactory factory;
   private final ResultOutputStrategy output;

   public MutationStrategies(MutationEngineFactory factory, HistoryStore history, CoverageGenerator coverage, MutationResultListenerFactory listenerFactory, ResultOutputStrategy output) {
      this(factory, history, coverage, listenerFactory, output, new DefaultBuildVerifier());
   }

   private MutationStrategies(MutationEngineFactory factory, HistoryStore history, CoverageGenerator coverage, MutationResultListenerFactory listenerFactory, ResultOutputStrategy output, BuildVerifier buildVerifier) {
      this.history = history;
      this.coverage = coverage;
      this.listenerFactory = listenerFactory;
      this.buildVerifier = buildVerifier;
      this.factory = factory;
      this.output = output;
   }

   public HistoryStore history() {
      return this.history;
   }

   public CoverageGenerator coverage() {
      return this.coverage;
   }

   public MutationResultListenerFactory listenerFactory() {
      return this.listenerFactory;
   }

   public BuildVerifier buildVerifier() {
      return this.buildVerifier;
   }

   public MutationEngineFactory factory() {
      return this.factory;
   }

   public ResultOutputStrategy output() {
      return this.output;
   }

   public MutationStrategies with(MutationEngineFactory factory) {
      return new MutationStrategies(factory, this.history, this.coverage, this.listenerFactory, this.output, this.buildVerifier);
   }

   public MutationStrategies with(BuildVerifier verifier) {
      return new MutationStrategies(this.factory, this.history, this.coverage, this.listenerFactory, this.output, verifier);
   }
}
