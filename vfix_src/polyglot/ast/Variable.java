package polyglot.ast;

import polyglot.types.Flags;

public interface Variable extends Expr {
   Flags flags();
}
