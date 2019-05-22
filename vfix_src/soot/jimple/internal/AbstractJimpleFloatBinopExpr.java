package soot.jimple.internal;

import java.util.List;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;

public abstract class AbstractJimpleFloatBinopExpr extends AbstractFloatBinopExpr implements ConvertToBaf {
   AbstractJimpleFloatBinopExpr(Value op1, Value op2) {
      this(Jimple.v().newArgBox(op1), Jimple.v().newArgBox(op2));
   }

   protected AbstractJimpleFloatBinopExpr(ValueBox op1Box, ValueBox op2Box) {
      this.op1Box = op1Box;
      this.op2Box = op2Box;
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)this.getOp1()).convertToBaf(context, out);
      ((ConvertToBaf)this.getOp2()).convertToBaf(context, out);
      Unit u = (Unit)this.makeBafInst(this.getOp1().getType());
      out.add(u);
      u.addAllTagsOf(context.getCurrentUnit());
   }

   abstract Object makeBafInst(Type var1);
}
