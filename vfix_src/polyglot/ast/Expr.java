package polyglot.ast;

import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.visit.PrettyPrinter;

public interface Expr extends Receiver, Term {
   Expr type(Type var1);

   Precedence precedence();

   boolean isConstant();

   Object constantValue();

   void printSubExpr(Expr var1, boolean var2, CodeWriter var3, PrettyPrinter var4);

   void printSubExpr(Expr var1, CodeWriter var2, PrettyPrinter var3);
}
