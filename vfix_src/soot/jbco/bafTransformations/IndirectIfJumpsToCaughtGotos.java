package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.ByteType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.JSRInst;
import soot.baf.TargetArgInst;
import soot.baf.ThrowInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.jimpleTransformations.FieldRenamer;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jbco.util.ThrowSet;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
import soot.util.Chain;

public class IndirectIfJumpsToCaughtGotos extends BodyTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(IndirectIfJumpsToCaughtGotos.class);
   int count = 0;
   public static String[] dependancies = new String[]{"bb.jbco_iii", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_iii";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Indirected Ifs through Traps: " + this.count);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         PatchingChain<Unit> units = b.getUnits();
         Unit nonTrap = this.findNonTrappedUnit(units, b.getTraps());
         if (nonTrap == null) {
            Unit last = null;
            nonTrap = Baf.v().newNopInst();

            Unit u;
            for(Iterator it = units.iterator(); it.hasNext(); last = u) {
               u = (Unit)it.next();
               if (!(u instanceof IdentityInst) || !(((IdentityInst)u).getLeftOp() instanceof Local)) {
                  if (last != null) {
                     units.insertAfter((Unit)nonTrap, (Unit)last);
                  } else {
                     units.addFirst((Unit)nonTrap);
                  }
                  break;
               }
            }
         }

         Stack<Type> stack = StackTypeHeightCalculator.getAfterStack((Body)b, (Unit)nonTrap);
         ArrayList<Unit> addedUnits = new ArrayList();
         Iterator it = units.snapshotIterator();

         while(it.hasNext()) {
            Unit u = (Unit)it.next();
            if (this.isIf(u) && Rand.getInt(10) <= weight) {
               TargetArgInst ifu = (TargetArgInst)u;
               Unit newTarg = Baf.v().newGotoInst(ifu.getTarget());
               units.add((Unit)newTarg);
               ifu.setTarget(newTarg);
               addedUnits.add(newTarg);
            }
         }

         if (addedUnits.size() > 0) {
            Unit nop = Baf.v().newNopInst();
            units.add((Unit)nop);
            ArrayList<Unit> toinsert = new ArrayList();
            SootField field = null;

            try {
               field = FieldRenamer.getRandomOpaques()[Rand.getInt(2)];
            } catch (NullPointerException var16) {
               logger.debug((String)var16.getMessage(), (Throwable)var16);
            }

            if (field != null && Rand.getInt(3) > 0) {
               toinsert.add(Baf.v().newStaticGetInst(field.makeRef()));
               if (field.getType() instanceof IntegerType) {
                  toinsert.add(Baf.v().newIfGeInst(units.getSuccOf((Unit)nonTrap)));
               } else {
                  SootMethod boolInit = ((RefType)field.getType()).getSootClass().getMethod("boolean booleanValue()");
                  toinsert.add(Baf.v().newVirtualInvokeInst(boolInit.makeRef()));
                  toinsert.add(Baf.v().newIfGeInst(units.getSuccOf((Unit)nonTrap)));
               }
            } else {
               toinsert.add(Baf.v().newPushInst(IntConstant.v(BodyBuilder.getIntegerNine())));
               toinsert.add(Baf.v().newPrimitiveCastInst(IntType.v(), ByteType.v()));
               toinsert.add(Baf.v().newPushInst(IntConstant.v(Rand.getInt() % 2 == 0 ? 9 : 3)));
               toinsert.add(Baf.v().newRemInst(ByteType.v()));
               toinsert.add(Baf.v().newIfEqInst(units.getSuccOf((Unit)nonTrap)));
            }

            ArrayList toinserttry = new ArrayList();

            while(stack.size() > 0) {
               toinserttry.add(Baf.v().newPopInst((Type)stack.pop()));
            }

            toinserttry.add(Baf.v().newPushInst(NullConstant.v()));
            Unit handler = Baf.v().newThrowInst();
            int var15 = Rand.getInt(toinserttry.size());

            while(var15++ < toinserttry.size()) {
               toinsert.add(toinserttry.get(0));
               toinserttry.remove(0);
            }

            if (toinserttry.size() > 0) {
               toinserttry.add(Baf.v().newGotoInst(handler));
               toinsert.add(Baf.v().newGotoInst((Unit)toinserttry.get(0)));
               units.insertBefore((List)toinserttry, (Unit)nop);
            }

            toinsert.add(handler);
            units.insertAfter((List)toinsert, (Unit)nonTrap);
            b.getTraps().add(Baf.v().newTrap(ThrowSet.getRandomThrowable(), (Unit)addedUnits.get(0), nop, handler));
            this.count += addedUnits.size();
            if (addedUnits.size() > 0 && debug) {
               StackTypeHeightCalculator.calculateStackHeights(b);
            }

         }
      }
   }

   private Unit findNonTrappedUnit(PatchingChain<Unit> units, Chain<Trap> traps) {
      int intrap = 0;
      ArrayList<Unit> untrapped = new ArrayList();
      Iterator it = units.snapshotIterator();

      Unit result;
      while(it.hasNext()) {
         result = (Unit)it.next();
         Iterator tit = traps.iterator();

         while(tit.hasNext()) {
            Trap t = (Trap)tit.next();
            if (result == t.getBeginUnit()) {
               ++intrap;
            }

            if (result == t.getEndUnit()) {
               --intrap;
            }
         }

         if (intrap == 0) {
            untrapped.add(result);
         }
      }

      result = null;
      if (untrapped.size() > 0) {
         int count = 0;

         while(true) {
            do {
               if (result != null || count >= 10) {
                  return result;
               }

               ++count;
               result = (Unit)untrapped.get(Rand.getInt(999999) % untrapped.size());
            } while(result.fallsThrough() && units.getSuccOf(result) != null && !(units.getSuccOf(result) instanceof ThrowInst));

            result = null;
         }
      } else {
         return result;
      }
   }

   private boolean isIf(Unit u) {
      return u instanceof TargetArgInst && !(u instanceof GotoInst) && !(u instanceof JSRInst);
   }
}
