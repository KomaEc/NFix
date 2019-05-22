package polyglot.ast;

public interface Assert extends Stmt {
   Expr cond();

   Assert cond(Expr var1);

   Expr errorMessage();

   Assert errorMessage(Expr var1);
}
