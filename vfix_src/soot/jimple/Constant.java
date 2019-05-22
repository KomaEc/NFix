package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.Immediate;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;

public abstract class Constant implements Value, ConvertToBaf, Immediate {
   public final List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newPushInst(this);
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }

   public Object clone() {
      throw new RuntimeException();
   }

   public boolean equivTo(Object c) {
      return this.equals(c);
   }

   public int equivHashCode() {
      return this.hashCode();
   }

   public void toString(UnitPrinter up) {
      up.constant(this);
   }
}
