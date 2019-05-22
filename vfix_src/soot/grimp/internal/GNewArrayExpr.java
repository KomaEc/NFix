package soot.grimp.internal;

import soot.Type;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractNewArrayExpr;

public class GNewArrayExpr extends AbstractNewArrayExpr implements Precedence {
   public GNewArrayExpr(Type type, Value size) {
      super(type, Grimp.v().newExprBox(size));
   }

   public int getPrecedence() {
      return 850;
   }

   public Object clone() {
      return new GNewArrayExpr(this.getBaseType(), Grimp.cloneIfNecessary(this.getSize()));
   }
}
