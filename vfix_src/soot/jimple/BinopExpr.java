package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface BinopExpr extends Expr {
   Value getOp1();

   Value getOp2();

   ValueBox getOp1Box();

   ValueBox getOp2Box();

   void setOp1(Value var1);

   void setOp2(Value var1);

   String getSymbol();

   String toString();
}
