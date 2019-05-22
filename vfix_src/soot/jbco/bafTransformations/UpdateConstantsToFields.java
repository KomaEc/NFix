package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootField;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.PushInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.jimpleTransformations.CollectConstants;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;

public class UpdateConstantsToFields extends BodyTransformer implements IJbcoTransform {
   public static String[] dependancies = new String[]{"wjtp.jbco_cc", "bb.jbco_ecvf", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_ecvf";
   static int updated = 0;

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Updated constant references: " + updated);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getMethod().getName().indexOf("<clinit>") < 0) {
         int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
         if (weight != 0) {
            PatchingChain<Unit> units = b.getUnits();
            Iterator iter = units.snapshotIterator();

            while(iter.hasNext()) {
               Unit u = (Unit)iter.next();
               if (u instanceof PushInst) {
                  SootField f = (SootField)CollectConstants.constantsToFields.get(((PushInst)u).getConstant());
                  if (f != null && Rand.getInt(10) <= weight) {
                     Unit get = Baf.v().newStaticGetInst(f.makeRef());
                     units.insertBefore((Unit)get, (Unit)u);
                     BodyBuilder.updateTraps(get, u, b.getTraps());
                     units.remove(u);
                     ++updated;
                  }
               }
            }

         }
      }
   }
}
