package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.baf.OpTypeArgInst;
import soot.baf.TargetArgInst;
import soot.baf.internal.BLoadInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.toolkits.graph.BriefUnitGraph;

public class MoveLoadsAboveIfs extends BodyTransformer implements IJbcoTransform {
   int movedloads = 0;
   public static String[] dependancies = new String[]{"bb.jbco_rlaii", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_rlaii";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Moved Loads Above Ifs: " + this.movedloads);
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         BriefUnitGraph bug = new BriefUnitGraph(b);
         List<Unit> candidates = new ArrayList();
         List<Unit> visited = new ArrayList();
         List<Unit> worklist = new ArrayList();
         worklist.addAll(bug.getHeads());

         while(true) {
            Unit u;
            do {
               if (worklist.size() <= 0) {
                  int orig = this.movedloads;
                  boolean changed = false;
                  PatchingChain<Unit> units = b.getUnits();

                  for(int i = 0; i < candidates.size(); ++i) {
                     Unit u = (Unit)candidates.get(i);
                     List<Unit> succs = bug.getSuccsOf(u);
                     BLoadInst clone = (BLoadInst)((BLoadInst)succs.get(0)).clone();
                     if (!(u instanceof IfNonNullInst) && !(u instanceof IfNullInst)) {
                        if (u instanceof OpTypeArgInst) {
                           Type t = ((OpTypeArgInst)u).getOpType();
                           if (this.category(t) == 2 || Rand.getInt(10) > weight) {
                              continue;
                           }

                           units.insertBefore((Unit)clone, (Unit)u);
                           Type t2 = clone.getOpType();
                           Object dup;
                           if (this.category(t2) == 2) {
                              dup = Baf.v().newDup2_x2Inst(t2, (Type)null, t, t);
                           } else {
                              dup = Baf.v().newDup1_x2Inst(t2, t, t);
                           }

                           units.insertBefore((Unit)dup, (Unit)u);
                           units.insertBefore((Unit)Baf.v().newPopInst(t2), (Unit)u);
                        } else {
                           if (this.category(clone.getOpType()) == 2 || Rand.getInt(10) > weight) {
                              continue;
                           }

                           units.insertBefore((Unit)clone, (Unit)u);
                           units.insertBefore((Unit)Baf.v().newSwapInst(IntType.v(), clone.getOpType()), (Unit)u);
                        }
                     } else {
                        if (this.category(clone.getOpType()) == 2 || Rand.getInt(10) > weight) {
                           continue;
                        }

                        units.insertBefore((Unit)clone, (Unit)u);
                        units.insertBefore((Unit)Baf.v().newSwapInst(RefType.v(), clone.getOpType()), (Unit)u);
                     }

                     ++this.movedloads;

                     for(int j = 0; j < succs.size(); ++j) {
                        Unit suc = (Unit)succs.get(j);
                        List<Unit> sucPreds = bug.getPredsOf(suc);
                        if (sucPreds.size() > 1) {
                           if (suc == ((TargetArgInst)u).getTarget()) {
                              ((TargetArgInst)u).setTarget((Unit)bug.getSuccsOf(suc).get(0));
                           } else {
                              units.insertAfter((Unit)Baf.v().newGotoInst((Unit)bug.getSuccsOf(suc).get(0)), (Unit)u);
                           }
                        } else {
                           units.remove(suc);
                        }
                     }

                     if (i < candidates.size() - 1) {
                        bug = new BriefUnitGraph(b);
                     }

                     changed = true;
                  }

                  if (changed) {
                     if (output) {
                        out.println(this.movedloads - orig + " loads moved above ifs in " + b.getMethod().getSignature());
                     }

                     if (debug) {
                        StackTypeHeightCalculator.calculateStackHeights(b);
                     }
                  }

                  return;
               }

               u = (Unit)worklist.remove(0);
            } while(visited.contains(u));

            visited.add(u);
            List<Unit> succs = bug.getSuccsOf(u);
            if (u instanceof TargetArgInst && this.checkCandidate(succs, bug)) {
               candidates.add(u);
            }

            for(int i = 0; i < succs.size(); ++i) {
               Unit o = (Unit)succs.get(i);
               if (!visited.contains(o)) {
                  worklist.add(o);
               }
            }
         }
      }
   }

   private boolean checkCandidate(List<Unit> succs, BriefUnitGraph bug) {
      if (succs.size() < 2) {
         return false;
      } else {
         Object o = succs.get(0);

         for(int i = 1; i < succs.size(); ++i) {
            if (((Unit)succs.get(i)).getClass() != o.getClass()) {
               return false;
            }
         }

         if (!(o instanceof BLoadInst)) {
            return false;
         } else {
            BLoadInst bl = (BLoadInst)o;
            Local l = bl.getLocal();

            for(int i = 1; i < succs.size(); ++i) {
               BLoadInst bld = (BLoadInst)succs.get(i);
               if (bld.getLocal() != l || bug.getPredsOf(bld).size() > 1) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private int category(Type t) {
      return !(t instanceof LongType) && !(t instanceof DoubleType) ? 1 : 2;
   }
}
