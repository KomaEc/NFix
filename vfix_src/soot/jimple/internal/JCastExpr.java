package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.Jimple;

public class JCastExpr extends AbstractCastExpr {
   public JCastExpr(Value op, Type type) {
      super(Jimple.v().newImmediateBox(op), type);
   }

   public Object clone() {
      return new JCastExpr(Jimple.cloneIfNecessary(this.getOp()), this.type);
   }
}
