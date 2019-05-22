package soot.jimple.internal;

import java.util.List;
import soot.RefType;
import soot.Unit;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.JimpleToBafContext;

public class JNewExpr extends AbstractNewExpr implements ConvertToBaf {
   public JNewExpr(RefType type) {
      this.type = type;
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newNewInst(this.getBaseType());
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }

   public Object clone() {
      return new JNewExpr(this.type);
   }
}
