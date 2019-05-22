package polyglot.ast;

import java.util.List;
import polyglot.types.SemanticException;
import polyglot.types.Type;

public interface ArrayInit extends Expr {
   List elements();

   ArrayInit elements(List var1);

   void typeCheckElements(Type var1) throws SemanticException;
}
