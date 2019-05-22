package soot.jbco.jimpleTransformations;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.util.Chain;

public class GotoInstrumenter extends BodyTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(GotoInstrumenter.class);
   public static final String name = "jtp.jbco_gia";
   public static final String[] dependencies = new String[]{"jtp.jbco_gia"};
   private int trapsAdded = 0;
   private int gotosInstrumented = 0;
   private static final UnitBox[] EMPTY_UNIT_BOX_ARRAY = new UnitBox[0];
   private static final int MAX_TRIES_TO_GET_REORDER_COUNT = 10;

   public String getName() {
      return "jtp.jbco_gia";
   }

   public String[] getDependencies() {
      return (String[])Arrays.copyOf(dependencies, dependencies.length);
   }

   public void outputSummary() {
      logger.info((String)"Instrumented {} GOTOs, added {} traps.", (Object)this.gotosInstrumented, (Object)this.trapsAdded);
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      if (!"<init>".equals(body.getMethod().getName()) && !"<clinit>".equals(body.getMethod().getName())) {
         if (Main.getWeight(phaseName, body.getMethod().getSignature()) != 0) {
            PatchingChain<Unit> units = body.getUnits();
            int precedingFirstNotIdentityIndex = 0;
            Unit precedingFirstNotIdentity = null;

            for(Iterator var7 = units.iterator(); var7.hasNext(); ++precedingFirstNotIdentityIndex) {
               Unit unit = (Unit)var7.next();
               if (!(unit instanceof IdentityStmt)) {
                  break;
               }

               precedingFirstNotIdentity = unit;
            }

            int unitsLeft = units.size() - precedingFirstNotIdentityIndex;
            if (unitsLeft < 8) {
               if (this.isVerbose()) {
                  logger.info((String)"Skipping {} method GOTO instrumentation as it is too small.", (Object)body.getMethod().getSignature());
               }

            } else {
               int tries = 0;
               int unitsQuantityToReorder = 0;

               Unit first;
               while(tries < 10) {
                  unitsQuantityToReorder = Rand.getInt(unitsLeft - 2) + 1;
                  ++tries;
                  first = (Unit)Iterables.get(units, precedingFirstNotIdentityIndex + unitsQuantityToReorder);
                  if (!isExceptionCaught(first, units, body.getTraps())) {
                     break;
                  }
               }

               if (tries < 10) {
                  if (this.isVerbose()) {
                     logger.info((String)"Adding GOTOs to \"{}\".", (Object)body.getMethod().getName());
                  }

                  first = precedingFirstNotIdentity == null ? units.getFirst() : precedingFirstNotIdentity;
                  Unit firstReorderingUnit = units.getSuccOf(first);
                  Unit reorderingUnit = firstReorderingUnit;

                  Unit nextReorderingUnit;
                  for(int reorder = 0; reorder < unitsQuantityToReorder; ++reorder) {
                     UnitBox[] pointingToReorderingUnit = (UnitBox[])reorderingUnit.getBoxesPointingToThis().toArray(EMPTY_UNIT_BOX_ARRAY);
                     UnitBox[] var15 = pointingToReorderingUnit;
                     int var16 = pointingToReorderingUnit.length;

                     int var17;
                     for(var17 = 0; var17 < var16; ++var17) {
                        UnitBox element = var15[var17];
                        reorderingUnit.removeBoxPointingToThis(element);
                     }

                     nextReorderingUnit = units.getSuccOf(reorderingUnit);
                     units.remove(reorderingUnit);
                     units.add(reorderingUnit);
                     UnitBox[] var27 = pointingToReorderingUnit;
                     var17 = pointingToReorderingUnit.length;

                     for(int var30 = 0; var30 < var17; ++var30) {
                        UnitBox element = var27[var30];
                        reorderingUnit.addBoxPointingToThis(element);
                     }

                     reorderingUnit = nextReorderingUnit;
                  }

                  Unit firstReorderingNotGotoStmt = first instanceof GotoStmt ? ((GotoStmt)first).getTargetBox().getUnit() : firstReorderingUnit;
                  GotoStmt gotoFirstReorderingNotGotoStmt = Jimple.v().newGotoStmt(firstReorderingNotGotoStmt);
                  units.insertBeforeNoRedirect(gotoFirstReorderingNotGotoStmt, reorderingUnit);
                  if (units.getLast().fallsThrough()) {
                     Stmt gotoStmt = reorderingUnit instanceof GotoStmt ? Jimple.v().newGotoStmt(((GotoStmt)reorderingUnit).getTargetBox().getUnit()) : Jimple.v().newGotoStmt(reorderingUnit);
                     units.add((Unit)gotoStmt);
                  }

                  ++this.gotosInstrumented;
                  nextReorderingUnit = units.getSuccOf(firstReorderingNotGotoStmt);
                  if (nextReorderingUnit == null || nextReorderingUnit.equals(units.getLast()) && nextReorderingUnit instanceof IdentityStmt) {
                     if (firstReorderingNotGotoStmt instanceof IdentityStmt) {
                        if (this.isVerbose()) {
                           logger.info((String)"Skipping adding try-catch block at \"{}\".", (Object)body.getMethod().getSignature());
                        }

                        return;
                     }

                     nextReorderingUnit = firstReorderingNotGotoStmt;
                  }

                  RefType throwable = Scene.v().getRefType("java.lang.Throwable");
                  Local caughtExceptionLocal = Jimple.v().newLocal("jbco_gi_caughtExceptionLocal", throwable);
                  body.getLocals().add(caughtExceptionLocal);
                  Unit caughtExceptionHandler = Jimple.v().newIdentityStmt(caughtExceptionLocal, Jimple.v().newCaughtExceptionRef());
                  units.add((Unit)caughtExceptionHandler);
                  units.add((Unit)Jimple.v().newThrowStmt(caughtExceptionLocal));
                  Iterator<Unit> reorderedUnitsIterator = units.iterator(nextReorderingUnit, units.getPredOf((Unit)caughtExceptionHandler));

                  Unit trapEndUnit;
                  for(trapEndUnit = (Unit)reorderedUnitsIterator.next(); trapEndUnit instanceof IdentityStmt && reorderedUnitsIterator.hasNext(); trapEndUnit = (Unit)reorderedUnitsIterator.next()) {
                  }

                  trapEndUnit = units.getSuccOf(trapEndUnit);
                  body.getTraps().add(Jimple.v().newTrap(throwable.getSootClass(), (Unit)units.getPredOf(firstReorderingNotGotoStmt), (Unit)trapEndUnit, (Unit)caughtExceptionHandler));
                  ++this.trapsAdded;
               }
            }
         }
      } else {
         if (this.isVerbose()) {
            logger.info((String)"Skipping {} method GOTO instrumentation as it is constructor/initializer.", (Object)body.getMethod().getSignature());
         }

      }
   }

   private static boolean isExceptionCaught(Unit unit, Chain<Unit> units, Chain<Trap> traps) {
      Iterator var3 = traps.iterator();

      Iterator unitsInTryIterator;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         Trap trap = (Trap)var3.next();
         Unit end = trap.getEndUnit();
         if (end.equals(unit)) {
            return true;
         }

         unitsInTryIterator = units.iterator(trap.getBeginUnit(), units.getPredOf(end));
      } while(!Iterators.contains(unitsInTryIterator, unit));

      return true;
   }
}
