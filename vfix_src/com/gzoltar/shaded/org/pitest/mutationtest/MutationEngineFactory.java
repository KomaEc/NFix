package com.gzoltar.shaded.org.pitest.mutationtest;

import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.plugin.ClientClasspathPlugin;
import java.util.Collection;

public interface MutationEngineFactory extends ClientClasspathPlugin {
   MutationEngine createEngine(boolean var1, Predicate<String> var2, Collection<String> var3, Collection<String> var4, boolean var5);

   String name();
}
