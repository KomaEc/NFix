package polyglot.ast;

public interface Synchronized extends CompoundStmt {
   Expr expr();

   Synchronized expr(Expr var1);

   Block body();

   Synchronized body(Block var1);
}
