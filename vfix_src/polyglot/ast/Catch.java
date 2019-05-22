package polyglot.ast;

import polyglot.types.Type;

public interface Catch extends CompoundStmt {
   Type catchType();

   Formal formal();

   Catch formal(Formal var1);

   Block body();

   Catch body(Block var1);
}
