package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.StmtAddressType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.IdentityInst;
import soot.baf.JSRInst;
import soot.baf.NopInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.jimpleTransformations.FieldRenamer;
import soot.jbco.util.Rand;
import soot.jimple.IntConstant;
import soot.jimple.NullConstant;
import soot.toolkits.graph.BriefUnitGraph;

public class TryCatchCombiner extends BodyTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(TryCatchCombiner.class);
   int totalcount = 0;
   int changedcount = 0;
   public static String[] dependancies = new String[]{"bb.jbco_j2bl", "bb.jbco_ctbcb", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_ctbcb";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Total try blocks found: " + this.totalcount);
      out.println("Combined TryCatches: " + this.changedcount);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         int trapCount = 0;
         PatchingChain<Unit> units = b.getUnits();
         ArrayList<Unit> headList = new ArrayList();
         ArrayList<Trap> trapList = new ArrayList();
         Iterator traps = b.getTraps().iterator();

         while(traps.hasNext()) {
            Trap t = (Trap)traps.next();
            ++this.totalcount;
            if (this.isRewritable(t)) {
               headList.add(t.getBeginUnit());
               trapList.add(t);
               ++trapCount;
            }
         }

         if (trapCount != 0) {
            NopInst stackHeightsBefore;
            for(int i = 0; i < headList.size(); ++i) {
               for(int j = 0; j < headList.size(); ++j) {
                  if (i != j && headList.get(i) == headList.get(j)) {
                     Trap t = (Trap)trapList.get(i);
                     stackHeightsBefore = Baf.v().newNopInst();
                     units.insertBeforeNoRedirect(stackHeightsBefore, (Unit)headList.get(i));
                     headList.set(i, stackHeightsBefore);
                     t.setBeginUnit(stackHeightsBefore);
                  }
               }
            }

            Unit first = null;

            Unit unit;
            for(Iterator uit = units.iterator(); uit.hasNext(); first = unit) {
               unit = (Unit)uit.next();
               if (!(unit instanceof IdentityInst)) {
                  break;
               }
            }

            Object first;
            if (first == null) {
               first = Baf.v().newNopInst();
               units.insertBefore((Unit)first, (Unit)units.getFirst());
            } else {
               first = units.getSuccOf(first);
            }

            Collection<Local> locs = b.getLocals();
            stackHeightsBefore = null;
            Map<Local, Local> bafToJLocals = (Map)Main.methods2Baf2JLocals.get(b.getMethod());
            int varCount = trapCount + 1;
            traps = b.getTraps().snapshotIterator();

            while(true) {
               Trap t;
               Unit begUnit;
               boolean badType;
               Stack s;
               Map stackHeightsBefore;
               do {
                  do {
                     do {
                        if (!traps.hasNext()) {
                           return;
                        }

                        t = (Trap)traps.next();
                        begUnit = t.getBeginUnit();
                     } while(!this.isRewritable(t));
                  } while(Rand.getInt(10) > weight);

                  stackHeightsBefore = StackTypeHeightCalculator.calculateStackHeights(b, bafToJLocals);
                  badType = false;
                  s = (Stack)((Stack)stackHeightsBefore.get(begUnit)).clone();
                  if (s.size() > 0) {
                     for(int i = 0; i < s.size(); ++i) {
                        if (s.pop() instanceof StmtAddressType) {
                           badType = true;
                           break;
                        }
                     }
                  }
               } while(badType);

               Local controlLocal = Baf.v().newLocal("controlLocal_tccomb" + trapCount, IntType.v());
               locs.add(controlLocal);
               Unit pushZero = Baf.v().newPushInst(IntConstant.v(0));
               Unit storZero = Baf.v().newStoreInst(IntType.v(), controlLocal);
               units.insertBeforeNoRedirect((Unit)pushZero.clone(), (Unit)first);
               units.insertBeforeNoRedirect((Unit)storZero.clone(), (Unit)first);
               BriefUnitGraph graph = new BriefUnitGraph(b);
               List<Unit> l = graph.getPredsOf(begUnit);
               units.add((Unit)pushZero);
               units.add((Unit)storZero);
               Stack<Local> varsToLoad = new Stack();
               s = (Stack)stackHeightsBefore.get(begUnit);
               int i;
               Local varLocal;
               if (s.size() > 0) {
                  for(i = 0; i < s.size(); ++i) {
                     Type type = (Type)s.pop();
                     varLocal = Baf.v().newLocal("varLocal_tccomb" + varCount++, type);
                     locs.add(varLocal);
                     varsToLoad.push(varLocal);
                     units.add((Unit)Baf.v().newStoreInst(type, varLocal));
                     units.insertBeforeNoRedirect(FixUndefinedLocals.getPushInitializer(varLocal, type), (Unit)first);
                     units.insertBeforeNoRedirect(Baf.v().newStoreInst(type, varLocal), (Unit)first);
                  }
               }

               units.add((Unit)Baf.v().newPushInst(NullConstant.v()));
               units.add((Unit)Baf.v().newGotoInst(begUnit));

               for(i = 0; i < l.size(); ++i) {
                  Unit pred = (Unit)l.get(i);
                  if (this.isIf(pred)) {
                     TargetArgInst ifPred = (TargetArgInst)pred;
                     if (ifPred.getTarget() == begUnit) {
                        ifPred.setTarget(pushZero);
                     }

                     Unit succ = units.getSuccOf((Unit)ifPred);
                     if (succ == begUnit) {
                        units.insertAfter((Unit)Baf.v().newGotoInst(pushZero), (Unit)ifPred);
                     }
                  } else if (pred instanceof GotoInst && ((GotoInst)pred).getTarget() == begUnit) {
                     ((GotoInst)pred).setTarget(pushZero);
                  } else {
                     units.insertAfter((Unit)Baf.v().newGotoInst(pushZero), (Unit)pred);
                  }
               }

               Unit handlerUnit = t.getHandlerUnit();
               Unit newBeginUnit = Baf.v().newLoadInst(IntType.v(), controlLocal);
               units.insertBefore((Unit)newBeginUnit, (Unit)begUnit);
               units.insertBefore((Unit)Baf.v().newIfNeInst(handlerUnit), (Unit)begUnit);
               units.insertBefore((Unit)Baf.v().newPopInst(RefType.v()), (Unit)begUnit);

               while(varsToLoad.size() > 0) {
                  varLocal = (Local)varsToLoad.pop();
                  units.insertBefore((Unit)Baf.v().newLoadInst(varLocal.getType(), varLocal), (Unit)begUnit);
               }

               try {
                  SootField[] f = FieldRenamer.getRandomOpaques();
                  if (f[0] != null && f[1] != null) {
                     this.loadBooleanValue(units, f[0], begUnit);
                     this.loadBooleanValue(units, f[1], begUnit);
                     units.insertBeforeNoRedirect(Baf.v().newIfCmpEqInst(BooleanType.v(), begUnit), begUnit);
                  }
               } catch (NullPointerException var30) {
                  logger.debug((String)var30.getMessage(), (Throwable)var30);
               }

               if (Rand.getInt() % 2 == 0) {
                  units.insertBeforeNoRedirect(Baf.v().newPushInst(IntConstant.v(Rand.getInt(3) + 1)), begUnit);
                  units.insertBeforeNoRedirect(Baf.v().newStoreInst(IntType.v(), controlLocal), begUnit);
               } else {
                  units.insertBeforeNoRedirect(Baf.v().newIncInst(controlLocal, IntConstant.v(Rand.getInt(3) + 1)), begUnit);
               }

               --trapCount;
               t.setBeginUnit(newBeginUnit);
               t.setHandlerUnit(newBeginUnit);
               ++this.changedcount;
               if (debug) {
                  StackTypeHeightCalculator.printStack(units, StackTypeHeightCalculator.calculateStackHeights(b), false);
               }
            }
         }
      }
   }

   private void loadBooleanValue(PatchingChain<Unit> units, SootField f, Unit insert) {
      units.insertBefore((Unit)Baf.v().newStaticGetInst(f.makeRef()), (Unit)insert);
      if (f.getType() instanceof RefType) {
         SootMethod boolInit = ((RefType)f.getType()).getSootClass().getMethod("boolean booleanValue()");
         units.insertBefore((Unit)Baf.v().newVirtualInvokeInst(boolInit.makeRef()), (Unit)insert);
      }

   }

   private boolean isIf(Unit u) {
      return u instanceof TargetArgInst && !(u instanceof GotoInst) && !(u instanceof JSRInst);
   }

   private boolean isRewritable(Trap t) {
      if (t.getBeginUnit() == t.getHandlerUnit()) {
         return false;
      } else {
         SootClass exc = t.getException();
         if (exc.getName().equals("java.lang.Throwable")) {
            return false;
         } else {
            do {
               if (exc.getName().equals("java.lang.RuntimeException")) {
                  return false;
               }
            } while(exc.hasSuperclass() && (exc = exc.getSuperclass()) != null);

            return true;
         }
      }
   }
}
