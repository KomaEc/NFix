package soot.dava;

public class DavaFlowAnalysisException extends DecompilationException {
   public DavaFlowAnalysisException() {
   }

   public DavaFlowAnalysisException(String message) {
      System.out.println("There was an Error During the Structural Flow Analysis in Dava");
      System.out.println(message);
      this.report();
   }
}
