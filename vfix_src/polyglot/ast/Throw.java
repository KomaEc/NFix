package polyglot.ast;

public interface Throw extends Stmt {
   Expr expr();

   Throw expr(Expr var1);
}
