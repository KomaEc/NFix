package polyglot.ast;

import polyglot.types.Flags;
import polyglot.types.Named;

public interface TopLevelDecl extends Node {
   Flags flags();

   String name();

   Named declaration();
}
