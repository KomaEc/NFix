package polyglot.ast;

public interface Case extends SwitchElement {
   Expr expr();

   Case expr(Expr var1);

   boolean isDefault();

   long value();

   Case value(long var1);
}
