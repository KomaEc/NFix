package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.mutationtest.TimeoutLengthStrategy;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationEngine;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import java.util.Collection;

public class SlaveArguments {
   final Collection<MutationDetails> mutations;
   final Collection<ClassName> testClasses;
   final MutationEngine engine;
   final TimeoutLengthStrategy timeoutStrategy;
   final boolean verbose;
   final Configuration pitConfig;

   public SlaveArguments(Collection<MutationDetails> mutations, Collection<ClassName> tests, MutationEngine engine, TimeoutLengthStrategy timeoutStrategy, boolean verbose, Configuration pitConfig) {
      this.mutations = mutations;
      this.testClasses = tests;
      this.engine = engine;
      this.timeoutStrategy = timeoutStrategy;
      this.verbose = verbose;
      this.pitConfig = pitConfig;
   }

   public boolean isVerbose() {
      return this.verbose;
   }
}
