package polyglot.ast;

import java.util.List;

public interface NewArray extends Expr {
   TypeNode baseType();

   NewArray baseType(TypeNode var1);

   int numDims();

   List dims();

   NewArray dims(List var1);

   int additionalDims();

   NewArray additionalDims(int var1);

   ArrayInit init();

   NewArray init(ArrayInit var1);
}
