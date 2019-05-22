package com.gzoltar.shaded.org.jacoco.core.analysis;

public interface ICoverageNode {
   ICoverageNode.ElementType getElementType();

   String getName();

   ICounter getInstructionCounter();

   ICounter getBranchCounter();

   ICounter getLineCounter();

   ICounter getComplexityCounter();

   ICounter getMethodCounter();

   ICounter getClassCounter();

   ICounter getCounter(ICoverageNode.CounterEntity var1);

   ICoverageNode getPlainCopy();

   public static enum CounterEntity {
      INSTRUCTION,
      BRANCH,
      LINE,
      COMPLEXITY,
      METHOD,
      CLASS;
   }

   public static enum ElementType {
      METHOD,
      CLASS,
      SOURCEFILE,
      PACKAGE,
      BUNDLE,
      GROUP;
   }
}
