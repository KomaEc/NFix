package polyglot.ast;

public interface If extends CompoundStmt {
   Expr cond();

   If cond(Expr var1);

   Stmt consequent();

   If consequent(Stmt var1);

   Stmt alternative();

   If alternative(Stmt var1);
}
