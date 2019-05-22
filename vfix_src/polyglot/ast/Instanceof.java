package polyglot.ast;

public interface Instanceof extends Expr {
   Expr expr();

   Instanceof expr(Expr var1);

   TypeNode compareType();

   Instanceof compareType(TypeNode var1);
}
