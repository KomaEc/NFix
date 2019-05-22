package soot.tagkit;

import java.util.LinkedList;
import soot.Unit;

public abstract class FirstTagAggregator extends TagAggregator {
   public abstract boolean wantTag(Tag var1);

   public abstract String aggregatedName();

   public void considerTag(Tag t, Unit u, LinkedList<Tag> tags, LinkedList<Unit> units) {
      if (units.size() <= 0 || units.getLast() != u) {
         units.add(u);
         tags.add(t);
      }
   }
}
