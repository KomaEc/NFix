package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.config;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationEngineConfiguration;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.InlinedCodeFilter;
import java.util.Collection;

public class DefaultMutationEngineConfiguration implements MutationEngineConfiguration {
   private final Predicate<MethodInfo> methodFilter;
   private final Collection<String> doNotMutate;
   private final Collection<? extends MethodMutatorFactory> mutators;
   private final InlinedCodeFilter inlinedCodeDetector;

   public DefaultMutationEngineConfiguration(Predicate<MethodInfo> filter, Collection<String> loggingClasses, Collection<? extends MethodMutatorFactory> mutators, InlinedCodeFilter inlinedCodeDetector) {
      this.methodFilter = filter;
      this.doNotMutate = loggingClasses;
      this.mutators = mutators;
      this.inlinedCodeDetector = inlinedCodeDetector;
   }

   public Collection<? extends MethodMutatorFactory> mutators() {
      return this.mutators;
   }

   public Collection<String> doNotMutateCallsTo() {
      return this.doNotMutate;
   }

   public Predicate<MethodInfo> methodFilter() {
      return this.methodFilter;
   }

   public InlinedCodeFilter inlinedCodeDetector() {
      return this.inlinedCodeDetector;
   }
}
