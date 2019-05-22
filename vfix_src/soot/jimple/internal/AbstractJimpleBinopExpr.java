package soot.jimple.internal;

import soot.Value;
import soot.jimple.Jimple;

public abstract class AbstractJimpleBinopExpr extends AbstractBinopExpr {
   protected AbstractJimpleBinopExpr(Value op1, Value op2) {
      this.op1Box = Jimple.v().newArgBox(op1);
      this.op2Box = Jimple.v().newArgBox(op2);
   }
}
