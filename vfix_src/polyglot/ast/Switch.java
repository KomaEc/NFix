package polyglot.ast;

import java.util.List;

public interface Switch extends CompoundStmt {
   Expr expr();

   Switch expr(Expr var1);

   List elements();

   Switch elements(List var1);
}
