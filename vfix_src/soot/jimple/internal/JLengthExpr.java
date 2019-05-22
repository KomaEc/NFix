package soot.jimple.internal;

import java.util.List;
import soot.Unit;
import soot.Value;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;

public class JLengthExpr extends AbstractLengthExpr implements ConvertToBaf {
   public JLengthExpr(Value op) {
      super(Jimple.v().newImmediateBox(op));
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)((ConvertToBaf)this.getOp())).convertToBaf(context, out);
      Unit u = Baf.v().newArrayLengthInst();
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }

   public Object clone() {
      return new JLengthExpr(Jimple.cloneIfNecessary(this.getOp()));
   }
}
