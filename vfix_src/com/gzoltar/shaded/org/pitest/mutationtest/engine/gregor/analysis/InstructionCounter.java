package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.analysis;

public interface InstructionCounter {
   void increment();

   int currentInstructionCount();
}
