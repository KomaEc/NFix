package soot.jimple.internal;

import soot.Type;
import soot.Value;
import soot.jimple.Jimple;

public class JNewArrayExpr extends AbstractNewArrayExpr {
   public JNewArrayExpr(Type type, Value size) {
      super(type, Jimple.v().newImmediateBox(size));
   }

   public Object clone() {
      return new JNewArrayExpr(this.getBaseType(), Jimple.cloneIfNecessary(this.getSize()));
   }
}
