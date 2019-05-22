package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;

public interface LocalDecl extends ForInit, VarDecl {
   LocalDecl flags(Flags var1);

   LocalDecl type(TypeNode var1);

   LocalDecl name(String var1);

   Expr init();

   LocalDecl init(Expr var1);

   LocalDecl localInstance(LocalInstance var1);
}
