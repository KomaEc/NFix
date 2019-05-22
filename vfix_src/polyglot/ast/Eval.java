package polyglot.ast;

public interface Eval extends ForInit, ForUpdate {
   Expr expr();

   Eval expr(Expr var1);
}
