package soot.dava;

public class CorruptASTException extends DecompilationException {
   public CorruptASTException(String message) {
      System.out.println("The Abstract Syntax Tree is corrupt");
      System.out.println(message);
      this.report();
   }
}
