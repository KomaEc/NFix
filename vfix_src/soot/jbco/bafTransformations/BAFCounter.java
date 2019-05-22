package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.baf.GotoInst;
import soot.baf.JSRInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;

public class BAFCounter extends BodyTransformer implements IJbcoTransform {
   static int count = 0;
   public static String[] dependancies = new String[]{"bb.jbco_counter"};
   public static String name = "bb.jbco_counter";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Count: " + count);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator var4 = b.getUnits().iterator();

      while(var4.hasNext()) {
         Unit u = (Unit)var4.next();
         if (u instanceof TargetArgInst && !(u instanceof GotoInst) && !(u instanceof JSRInst)) {
            ++count;
         }
      }

   }
}
