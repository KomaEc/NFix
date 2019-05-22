package soot.grimp.internal;

import soot.Local;
import soot.Value;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.ConcreteRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;

public class ObjExprBox extends ExprBox {
   public ObjExprBox(Value value) {
      super(value);
   }

   public boolean canContainValue(Value value) {
      return value instanceof ConcreteRef || value instanceof InvokeExpr || value instanceof NewArrayExpr || value instanceof NewMultiArrayExpr || value instanceof Local || value instanceof NullConstant || value instanceof StringConstant || value instanceof ClassConstant || value instanceof CastExpr && this.canContainValue(((CastExpr)value).getOp());
   }
}
