package polyglot.ast;

public interface ArrayAccess extends Variable {
   Expr array();

   ArrayAccess array(Expr var1);

   Expr index();

   ArrayAccess index(Expr var1);
}
