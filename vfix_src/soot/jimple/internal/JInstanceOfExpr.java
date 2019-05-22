package soot.jimple.internal;

import java.util.List;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;

public class JInstanceOfExpr extends AbstractInstanceOfExpr implements ConvertToBaf {
   public JInstanceOfExpr(Value op, Type checkType) {
      super(Jimple.v().newImmediateBox(op), checkType);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)((ConvertToBaf)this.getOp())).convertToBaf(context, out);
      Unit u = Baf.v().newInstanceOfInst(this.getCheckType());
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }

   public Object clone() {
      return new JInstanceOfExpr(Jimple.cloneIfNecessary(this.getOp()), this.checkType);
   }
}
