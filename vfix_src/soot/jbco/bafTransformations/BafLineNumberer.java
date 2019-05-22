package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.baf.IdentityInst;
import soot.baf.Inst;
import soot.jbco.IJbcoTransform;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;

public class BafLineNumberer extends BodyTransformer implements IJbcoTransform {
   public static String name = "bb.jbco_bln";

   public void outputSummary() {
   }

   public String[] getDependencies() {
      return new String[]{name};
   }

   public String getName() {
      return name;
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int idx = 0;
      PatchingChain<Unit> units = b.getUnits();
      Iterator it = units.iterator();

      while(it.hasNext()) {
         Inst i = (Inst)it.next();
         List<Tag> tags = i.getTags();

         for(int k = 0; k < tags.size(); ++k) {
            Tag t = (Tag)tags.get(k);
            if (t instanceof LineNumberTag) {
               tags.remove(k);
               break;
            }
         }

         if (!(i instanceof IdentityInst)) {
            i.addTag(new LineNumberTag(idx++));
         }
      }

   }
}
