package polyglot.ast;

public interface Labeled extends CompoundStmt {
   String label();

   Labeled label(String var1);

   Stmt statement();

   Labeled statement(Stmt var1);
}
