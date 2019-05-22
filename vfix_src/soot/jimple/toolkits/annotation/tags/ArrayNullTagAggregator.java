package soot.jimple.toolkits.annotation.tags;

import java.util.LinkedList;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.baf.Inst;
import soot.tagkit.Tag;
import soot.tagkit.TagAggregator;

public class ArrayNullTagAggregator extends TagAggregator {
   public ArrayNullTagAggregator(Singletons.Global g) {
   }

   public static ArrayNullTagAggregator v() {
      return G.v().soot_jimple_toolkits_annotation_tags_ArrayNullTagAggregator();
   }

   public boolean wantTag(Tag t) {
      return t instanceof OneByteCodeTag;
   }

   public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
      Inst i = (Inst)u;
      if (i.containsInvokeExpr() || i.containsFieldRef() || i.containsArrayRef()) {
         OneByteCodeTag obct = (OneByteCodeTag)t;
         if (units.size() == 0 || units.getLast() != u) {
            units.add(u);
            tags.add(new ArrayNullCheckTag());
         }

         ArrayNullCheckTag anct = (ArrayNullCheckTag)tags.getLast();
         anct.accumulate(obct.getValue()[0]);
      }
   }

   public String aggregatedName() {
      return "ArrayNullCheckAttribute";
   }
}
