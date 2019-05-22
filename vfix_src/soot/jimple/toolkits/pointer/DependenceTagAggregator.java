package soot.jimple.toolkits.pointer;

import soot.G;
import soot.Singletons;
import soot.tagkit.ImportantTagAggregator;
import soot.tagkit.Tag;

public class DependenceTagAggregator extends ImportantTagAggregator {
   public DependenceTagAggregator(Singletons.Global g) {
   }

   public static DependenceTagAggregator v() {
      return G.v().soot_jimple_toolkits_pointer_DependenceTagAggregator();
   }

   public boolean wantTag(Tag t) {
      return t instanceof DependenceTag;
   }

   public String aggregatedName() {
      return "SideEffectAttribute";
   }
}
