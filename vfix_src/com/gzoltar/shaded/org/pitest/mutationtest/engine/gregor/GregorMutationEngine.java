package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutater;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.InlinedCodeFilter;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class GregorMutationEngine implements MutationEngine {
   private final Set<MethodMutatorFactory> mutationOperators = new LinkedHashSet();
   private final Set<String> loggingClasses = new LinkedHashSet();
   private final Predicate<MethodInfo> methodFilter;
   private final InlinedCodeFilter inlinedCodeDetector;

   public GregorMutationEngine(MutationEngineConfiguration config) {
      this.methodFilter = config.methodFilter();
      this.mutationOperators.addAll(config.mutators());
      this.loggingClasses.addAll(config.doNotMutateCallsTo());
      this.inlinedCodeDetector = config.inlinedCodeDetector();
   }

   public Mutater createMutator(ClassByteArraySource byteSource) {
      return new GregorMutater(byteSource, this.methodFilter, this.mutationOperators, this.loggingClasses, this.inlinedCodeDetector);
   }

   public String toString() {
      return "GregorMutationEngine [filter=" + this.methodFilter + ", mutationOperators=" + this.mutationOperators + "]";
   }

   public Collection<String> getMutatorNames() {
      return FCollection.map(this.mutationOperators, toName());
   }

   private static F<MethodMutatorFactory, String> toName() {
      return new F<MethodMutatorFactory, String>() {
         public String apply(MethodMutatorFactory a) {
            return a.getName();
         }
      };
   }
}
