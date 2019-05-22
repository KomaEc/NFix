package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.InlinedCodeFilter;
import java.util.Collection;

public interface MutationEngineConfiguration {
   Collection<? extends MethodMutatorFactory> mutators();

   Predicate<MethodInfo> methodFilter();

   Collection<String> doNotMutateCallsTo();

   InlinedCodeFilter inlinedCodeDetector();
}
