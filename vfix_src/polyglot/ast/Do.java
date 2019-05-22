package polyglot.ast;

public interface Do extends Loop {
   Do body(Stmt var1);

   Do cond(Expr var1);
}
