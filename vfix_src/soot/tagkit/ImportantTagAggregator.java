package soot.tagkit;

import java.util.LinkedList;
import soot.Unit;
import soot.baf.Inst;

public abstract class ImportantTagAggregator extends TagAggregator {
   public abstract boolean wantTag(Tag var1);

   public abstract String aggregatedName();

   public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
      Inst i = (Inst)u;
      if (i.containsInvokeExpr() || i.containsFieldRef() || i.containsArrayRef() || i.containsNewExpr()) {
         units.add(u);
         tags.add(t);
      }
   }
}
