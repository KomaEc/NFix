package org.codehaus.groovy.control;

public enum CompilePhase {
   INITIALIZATION(1),
   PARSING(2),
   CONVERSION(3),
   SEMANTIC_ANALYSIS(4),
   CANONICALIZATION(5),
   INSTRUCTION_SELECTION(6),
   CLASS_GENERATION(7),
   OUTPUT(8),
   FINALIZATION(9);

   public static CompilePhase[] phases = new CompilePhase[]{null, INITIALIZATION, PARSING, CONVERSION, SEMANTIC_ANALYSIS, CANONICALIZATION, INSTRUCTION_SELECTION, CLASS_GENERATION, OUTPUT, FINALIZATION};
   int phaseNumber;

   private CompilePhase(int phaseNumber) {
      this.phaseNumber = phaseNumber;
   }

   public int getPhaseNumber() {
      return this.phaseNumber;
   }
}
