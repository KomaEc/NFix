package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.config;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationEngineFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.GregorMutationEngine;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.InlinedCodeFilter;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.InlinedFinallyBlockDetector;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.NoInlinedCodeDetection;
import java.util.Collection;

public final class GregorEngineFactory implements MutationEngineFactory {
   public MutationEngine createEngine(boolean mutateStaticInitializers, Predicate<String> excludedMethods, Collection<String> loggingClasses, Collection<String> mutators, boolean detectInlinedCode) {
      return this.createEngineWithMutators(mutateStaticInitializers, excludedMethods, loggingClasses, createMutatorListFromArrayOrUseDefaults(mutators), detectInlinedCode);
   }

   public MutationEngine createEngineWithMutators(boolean mutateStaticInitializers, Predicate<String> excludedMethods, Collection<String> loggingClasses, Collection<? extends MethodMutatorFactory> mutators, boolean detectInlinedCode) {
      Predicate<MethodInfo> filter = pickFilter(mutateStaticInitializers, Prelude.not(stringToMethodInfoPredicate(excludedMethods)));
      DefaultMutationEngineConfiguration config = new DefaultMutationEngineConfiguration(filter, loggingClasses, mutators, inlinedCodeDetector(detectInlinedCode));
      return new GregorMutationEngine(config);
   }

   private static InlinedCodeFilter inlinedCodeDetector(boolean detectInlinedCode) {
      return (InlinedCodeFilter)(detectInlinedCode ? new InlinedFinallyBlockDetector() : new NoInlinedCodeDetection());
   }

   private static Collection<? extends MethodMutatorFactory> createMutatorListFromArrayOrUseDefaults(Collection<String> mutators) {
      return mutators != null && !mutators.isEmpty() ? Mutator.fromStrings(mutators) : Mutator.defaults();
   }

   private static Predicate<MethodInfo> pickFilter(boolean mutateStaticInitializers, Predicate<MethodInfo> excludedMethods) {
      return (Predicate)(!mutateStaticInitializers ? Prelude.and(excludedMethods, notStaticInitializer()) : excludedMethods);
   }

   private static F<MethodInfo, Boolean> stringToMethodInfoPredicate(final Predicate<String> excludedMethods) {
      return new Predicate<MethodInfo>() {
         public Boolean apply(MethodInfo a) {
            return (Boolean)excludedMethods.apply(a.getName());
         }
      };
   }

   private static Predicate<MethodInfo> notStaticInitializer() {
      return new Predicate<MethodInfo>() {
         public Boolean apply(MethodInfo a) {
            return !a.isStaticInitializer();
         }
      };
   }

   public String name() {
      return "gregor";
   }

   public String description() {
      return "Default mutation engine";
   }
}
