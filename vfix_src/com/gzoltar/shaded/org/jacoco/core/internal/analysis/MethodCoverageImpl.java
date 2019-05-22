package com.gzoltar.shaded.org.jacoco.core.internal.analysis;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;

public class MethodCoverageImpl extends SourceNodeImpl implements IMethodCoverage {
   private final String desc;
   private final String signature;

   public MethodCoverageImpl(String name, String desc, String signature) {
      super(ICoverageNode.ElementType.METHOD, name);
      this.desc = desc;
      this.signature = signature;
   }

   public void increment(ICounter instructions, ICounter branches, int line) {
      super.increment(instructions, branches, line);
      if (branches.getTotalCount() > 1) {
         int c = Math.max(0, branches.getCoveredCount() - 1);
         int m = Math.max(0, branches.getTotalCount() - c - 1);
         this.complexityCounter = this.complexityCounter.increment(m, c);
      }

   }

   public void incrementMethodCounter() {
      ICounter base = this.instructionCounter.getCoveredCount() == 0 ? CounterImpl.COUNTER_1_0 : CounterImpl.COUNTER_0_1;
      this.methodCounter = this.methodCounter.increment(base);
      this.complexityCounter = this.complexityCounter.increment(base);
   }

   public String getDesc() {
      return this.desc;
   }

   public String getSignature() {
      return this.signature;
   }
}
