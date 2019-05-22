package polyglot.ast;

public interface Return extends Stmt {
   Expr expr();

   Return expr(Expr var1);
}
