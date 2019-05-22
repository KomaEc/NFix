package soot.JastAddJ;

public interface FinallyHost {
   boolean isDUafterFinally(Variable var1);

   boolean isDAafterFinally(Variable var1);

   void emitFinallyCode(Body var1);

   soot.jimple.Stmt label_finally_block();
}
