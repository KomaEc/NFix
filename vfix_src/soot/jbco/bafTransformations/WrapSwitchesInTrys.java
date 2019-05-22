package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.TableSwitchInst;
import soot.baf.ThrowInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jbco.util.ThrowSet;
import soot.util.Chain;

public class WrapSwitchesInTrys extends BodyTransformer implements IJbcoTransform {
   int totaltraps = 0;
   public static String[] dependancies = new String[]{"bb.jbco_ptss", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_ptss";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Switches wrapped in Tries: " + this.totaltraps);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         int i = 0;
         Unit handler = null;
         Chain<Trap> traps = b.getTraps();
         PatchingChain<Unit> units = b.getUnits();
         Iterator it = units.snapshotIterator();

         while(true) {
            TableSwitchInst twi;
            do {
               do {
                  Unit u;
                  do {
                     if (!it.hasNext()) {
                        this.totaltraps += i;
                        if (i > 0 && debug) {
                           StackTypeHeightCalculator.calculateStackHeights(b);
                        }

                        return;
                     }

                     u = (Unit)it.next();
                  } while(!(u instanceof TableSwitchInst));

                  twi = (TableSwitchInst)u;
               } while(BodyBuilder.isExceptionCaughtAt(units, twi, traps.iterator()));
            } while(Rand.getInt(10) > weight);

            Unit uthrow;
            if (handler == null) {
               Iterator uit = units.snapshotIterator();

               while(uit.hasNext()) {
                  uthrow = (Unit)uit.next();
                  if (uthrow instanceof ThrowInst && !BodyBuilder.isExceptionCaughtAt(units, uthrow, traps.iterator())) {
                     handler = uthrow;
                     break;
                  }
               }

               if (handler == null) {
                  handler = Baf.v().newThrowInst();
                  units.add((Unit)handler);
               }
            }

            int size = 4;

            Unit o;
            for(uthrow = units.getSuccOf((Unit)twi); !BodyBuilder.isExceptionCaughtAt(units, uthrow, traps.iterator()) && size-- > 0; uthrow = (Unit)o) {
               o = units.getSuccOf(uthrow);
               if (o == null) {
                  break;
               }
            }

            traps.add(Baf.v().newTrap(ThrowSet.getRandomThrowable(), twi, uthrow, (Unit)handler));
            ++i;
         }
      }
   }
}
