package polyglot.ast;

public interface Cast extends Expr {
   TypeNode castType();

   Cast castType(TypeNode var1);

   Expr expr();

   Cast expr(Expr var1);
}
