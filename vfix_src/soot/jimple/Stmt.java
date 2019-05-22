package soot.jimple;

import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;

public interface Stmt extends Unit {
   void toString(UnitPrinter var1);

   boolean containsInvokeExpr();

   InvokeExpr getInvokeExpr();

   ValueBox getInvokeExprBox();

   boolean containsArrayRef();

   ArrayRef getArrayRef();

   ValueBox getArrayRefBox();

   boolean containsFieldRef();

   FieldRef getFieldRef();

   ValueBox getFieldRefBox();
}
