package soot.jimple.internal;

import java.util.List;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;

public abstract class AbstractJimpleIntLongBinopExpr extends AbstractIntLongBinopExpr implements ConvertToBaf {
   protected AbstractJimpleIntLongBinopExpr(Value op1, Value op2) {
      this.op1Box = Jimple.v().newArgBox(op1);
      this.op2Box = Jimple.v().newArgBox(op2);
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
