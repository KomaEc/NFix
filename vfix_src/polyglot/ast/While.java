package polyglot.ast;

public interface While extends Loop {
   While cond(Expr var1);

   While body(Stmt var1);
}
