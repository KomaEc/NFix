package polyglot.ast;

public interface Conditional extends Expr {
   Expr cond();

   Conditional cond(Expr var1);

   Expr consequent();

   Conditional consequent(Expr var1);

   Expr alternative();

   Conditional alternative(Expr var1);
}
