package soot.tagkit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.baf.BafBody;

public abstract class TagAggregator extends BodyTransformer {
   public abstract boolean wantTag(Tag var1);

   public abstract void considerTag(Tag var1, Unit var2, LinkedList<Tag> var3, LinkedList<Unit> var4);

   public abstract String aggregatedName();

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      BafBody body = (BafBody)b;
      LinkedList<Tag> tags = new LinkedList();
      LinkedList<Unit> units = new LinkedList();
      Iterator unitIt = body.getUnits().iterator();

      while(unitIt.hasNext()) {
         Unit unit = (Unit)unitIt.next();
         Iterator tagIt = unit.getTags().iterator();

         while(tagIt.hasNext()) {
            Tag tag = (Tag)tagIt.next();
            if (this.wantTag(tag)) {
               this.considerTag(tag, unit, tags, units);
            }
         }
      }

      if (units.size() > 0) {
         b.addTag(new CodeAttribute(this.aggregatedName(), new LinkedList(units), new LinkedList(tags)));
      }

      this.fini();
   }

   public void fini() {
   }
}
