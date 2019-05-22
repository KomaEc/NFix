package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.Type;

public interface VarDecl extends Term {
   Type declType();

   Flags flags();

   TypeNode type();

   String name();

   LocalInstance localInstance();
}
