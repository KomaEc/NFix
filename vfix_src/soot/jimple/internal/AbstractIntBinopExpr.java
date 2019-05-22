package soot.jimple.internal;

import soot.IntType;
import soot.Type;

public abstract class AbstractIntBinopExpr extends AbstractBinopExpr {
   public Type getType() {
      return IntType.v();
   }
}
