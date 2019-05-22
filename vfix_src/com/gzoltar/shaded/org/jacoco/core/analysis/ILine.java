package com.gzoltar.shaded.org.jacoco.core.analysis;

public interface ILine {
   ICounter getInstructionCounter();

   ICounter getBranchCounter();

   int getStatus();
}
