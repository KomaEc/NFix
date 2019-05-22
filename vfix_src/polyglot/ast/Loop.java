package polyglot.ast;

public interface Loop extends CompoundStmt {
   Expr cond();

   boolean condIsConstant();

   boolean condIsConstantTrue();

   Stmt body();

   Term continueTarget();
}
