package soot.tagkit;

import java.util.LinkedList;
import soot.G;
import soot.IdentityUnit;
import soot.Singletons;
import soot.Unit;

public class LineNumberTagAggregator extends FirstTagAggregator {
   public LineNumberTagAggregator(Singletons.Global g) {
   }

   public static LineNumberTagAggregator v() {
      return G.v().soot_tagkit_LineNumberTagAggregator();
   }

   public boolean wantTag(Tag t) {
      return t instanceof LineNumberTag || t instanceof SourceLnPosTag;
   }

   public String aggregatedName() {
      return "LineNumberTable";
   }

   public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
      if (!(u instanceof IdentityUnit)) {
         super.considerTag(t, u, tags, units);
      }

   }
}
