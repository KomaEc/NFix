package soot.jbco.bafTransformations;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.G;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.baf.PopInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.NullConstant;

public class IfNullToTryCatch extends BodyTransformer implements IJbcoTransform {
   int count = 0;
   int totalifs = 0;
   public static String[] dependancies = new String[]{"bb.jbco_riitcb", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_riitcb";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("If(Non)Nulls changed to traps: " + this.count);
      out.println("Total ifs found: " + this.totalifs);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         SootClass exc = G.v().soot_Scene().getSootClass("java.lang.NullPointerException");
         SootClass obj = G.v().soot_Scene().getSootClass("java.lang.Object");
         SootMethod toStrg = obj.getMethodByName("toString");
         SootMethod eq = obj.getMethodByName("equals");
         boolean change = false;
         PatchingChain<Unit> units = b.getUnits();
         Iterator uit = units.snapshotIterator();

         while(true) {
            while(uit.hasNext()) {
               Unit u = (Unit)uit.next();
               if (BodyBuilder.isBafIf(u)) {
                  ++this.totalifs;
               }

               Unit targ;
               PopInst pop;
               if (u instanceof IfNullInst && Rand.getInt(10) <= weight) {
                  targ = ((IfNullInst)u).getTarget();
                  Unit succ = units.getSuccOf(u);
                  pop = Baf.v().newPopInst(RefType.v());
                  Unit popClone = (Unit)pop.clone();
                  units.insertBefore((Unit)pop, (Unit)targ);
                  Unit gotoTarg = Baf.v().newGotoInst(targ);
                  units.insertBefore((Unit)gotoTarg, (Unit)pop);
                  if (Rand.getInt(2) == 0) {
                     Unit methCall = Baf.v().newVirtualInvokeInst(toStrg.makeRef());
                     units.insertBefore((Unit)methCall, (Unit)u);
                     if (Rand.getInt(2) == 0) {
                        units.remove(u);
                        units.insertAfter((Unit)popClone, (Unit)methCall);
                     }

                     b.getTraps().add(Baf.v().newTrap(exc, methCall, succ, pop));
                  } else {
                     Unit throwu = Baf.v().newThrowInst();
                     units.insertBefore((Unit)throwu, (Unit)u);
                     units.remove(u);
                     units.insertBefore((Unit)Baf.v().newPushInst(NullConstant.v()), (Unit)throwu);
                     Unit ifunit = Baf.v().newIfCmpNeInst(RefType.v(), succ);
                     units.insertBefore((Unit)ifunit, (Unit)throwu);
                     units.insertBefore((Unit)Baf.v().newPushInst(NullConstant.v()), (Unit)throwu);
                     b.getTraps().add(Baf.v().newTrap(exc, throwu, succ, pop));
                  }

                  ++this.count;
                  change = true;
               } else if (u instanceof IfNonNullInst && Rand.getInt(10) <= weight) {
                  targ = ((IfNonNullInst)u).getTarget();
                  Unit methCall = Baf.v().newVirtualInvokeInst(eq.makeRef());
                  units.insertBefore((Unit)methCall, (Unit)u);
                  units.insertBefore((Unit)Baf.v().newPushInst(NullConstant.v()), (Unit)methCall);
                  if (Rand.getInt(2) == 0) {
                     pop = Baf.v().newPopInst(BooleanType.v());
                     units.insertBefore((Unit)pop, (Unit)u);
                     Unit gotoTarg = Baf.v().newGotoInst(targ);
                     units.insertBefore((Unit)gotoTarg, (Unit)u);
                     pop = Baf.v().newPopInst(RefType.v());
                     units.insertAfter((Unit)pop, (Unit)u);
                     units.remove(u);
                     b.getTraps().addFirst(Baf.v().newTrap(exc, methCall, gotoTarg, pop));
                  } else {
                     Unit iffalse = Baf.v().newIfEqInst(targ);
                     units.insertBefore((Unit)iffalse, (Unit)u);
                     units.insertBefore((Unit)Baf.v().newPushInst(NullConstant.v()), (Unit)u);
                     Unit pop = Baf.v().newPopInst(RefType.v());
                     units.insertAfter((Unit)pop, (Unit)u);
                     units.remove(u);
                     b.getTraps().addFirst(Baf.v().newTrap(exc, methCall, iffalse, pop));
                  }

                  ++this.count;
                  change = true;
               }
            }

            if (change && debug) {
               StackTypeHeightCalculator.calculateStackHeights(b);
            }

            return;
         }
      }
   }
}
