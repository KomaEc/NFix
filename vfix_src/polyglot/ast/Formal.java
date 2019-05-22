package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;

public interface Formal extends VarDecl {
   Formal flags(Flags var1);

   Formal type(TypeNode var1);

   Formal name(String var1);

   Formal localInstance(LocalInstance var1);
}
