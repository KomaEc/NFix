package com.gzoltar.shaded.org.jacoco.core.analysis;

import com.gzoltar.shaded.org.jacoco.core.internal.analysis.CounterImpl;
import java.util.Collection;
import java.util.Iterator;

public class CoverageNodeImpl implements ICoverageNode {
   private final ICoverageNode.ElementType elementType;
   private final String name;
   protected CounterImpl branchCounter;
   protected CounterImpl instructionCounter;
   protected CounterImpl lineCounter;
   protected CounterImpl complexityCounter;
   protected CounterImpl methodCounter;
   protected CounterImpl classCounter;

   public CoverageNodeImpl(ICoverageNode.ElementType elementType, String name) {
      this.elementType = elementType;
      this.name = name;
      this.branchCounter = CounterImpl.COUNTER_0_0;
      this.instructionCounter = CounterImpl.COUNTER_0_0;
      this.complexityCounter = CounterImpl.COUNTER_0_0;
      this.methodCounter = CounterImpl.COUNTER_0_0;
      this.classCounter = CounterImpl.COUNTER_0_0;
      this.lineCounter = CounterImpl.COUNTER_0_0;
   }

   public void increment(ICoverageNode child) {
      this.instructionCounter = this.instructionCounter.increment(child.getInstructionCounter());
      this.branchCounter = this.branchCounter.increment(child.getBranchCounter());
      this.lineCounter = this.lineCounter.increment(child.getLineCounter());
      this.complexityCounter = this.complexityCounter.increment(child.getComplexityCounter());
      this.methodCounter = this.methodCounter.increment(child.getMethodCounter());
      this.classCounter = this.classCounter.increment(child.getClassCounter());
   }

   public void increment(Collection<? extends ICoverageNode> children) {
      Iterator i$ = children.iterator();

      while(i$.hasNext()) {
         ICoverageNode child = (ICoverageNode)i$.next();
         this.increment(child);
      }

   }

   public ICoverageNode.ElementType getElementType() {
      return this.elementType;
   }

   public String getName() {
      return this.name;
   }

   public ICounter getInstructionCounter() {
      return this.instructionCounter;
   }

   public ICounter getBranchCounter() {
      return this.branchCounter;
   }

   public ICounter getLineCounter() {
      return this.lineCounter;
   }

   public ICounter getComplexityCounter() {
      return this.complexityCounter;
   }

   public ICounter getMethodCounter() {
      return this.methodCounter;
   }

   public ICounter getClassCounter() {
      return this.classCounter;
   }

   public ICounter getCounter(ICoverageNode.CounterEntity entity) {
      switch(entity) {
      case INSTRUCTION:
         return this.getInstructionCounter();
      case BRANCH:
         return this.getBranchCounter();
      case LINE:
         return this.getLineCounter();
      case COMPLEXITY:
         return this.getComplexityCounter();
      case METHOD:
         return this.getMethodCounter();
      case CLASS:
         return this.getClassCounter();
      default:
         throw new AssertionError(entity);
      }
   }

   public ICoverageNode getPlainCopy() {
      CoverageNodeImpl copy = new CoverageNodeImpl(this.elementType, this.name);
      copy.instructionCounter = CounterImpl.getInstance(this.instructionCounter);
      copy.branchCounter = CounterImpl.getInstance(this.branchCounter);
      copy.lineCounter = CounterImpl.getInstance(this.lineCounter);
      copy.complexityCounter = CounterImpl.getInstance(this.complexityCounter);
      copy.methodCounter = CounterImpl.getInstance(this.methodCounter);
      copy.classCounter = CounterImpl.getInstance(this.classCounter);
      return copy;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.name).append(" [").append(this.elementType).append("]");
      return sb.toString();
   }
}
