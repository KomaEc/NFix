package soot;

import soot.jimple.AnyNewExpr;

public interface EscapeAnalysis {
   boolean mayEscapeMethod(AnyNewExpr var1);

   boolean mayEscapeMethod(Context var1, AnyNewExpr var2);

   boolean mayEscapeThread(AnyNewExpr var1);

   boolean mayEscapeThread(Context var1, AnyNewExpr var2);
}
