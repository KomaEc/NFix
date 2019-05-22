package soot.jimple.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.util.Chain;

public class CriticalEdgeRemover extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(CriticalEdgeRemover.class);

   public CriticalEdgeRemover(Singletons.Global g) {
   }

   public static CriticalEdgeRemover v() {
      return G.v().soot_jimple_toolkits_graph_CriticalEdgeRemover();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Removing Critical Edges...");
      }

      this.removeCriticalEdges(b);
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Removing Critical Edges done.");
      }

   }

   private static Unit insertGotoAfter(Chain<Unit> unitChain, Unit node, Unit target) {
      Unit newGoto = Jimple.v().newGotoStmt(target);
      unitChain.insertAfter((Object)newGoto, node);
      return newGoto;
   }

   private static Unit insertGotoBefore(Chain<Unit> unitChain, Unit node, Unit target) {
      Unit newGoto = Jimple.v().newGotoStmt(target);
      unitChain.insertBefore((Object)newGoto, node);
      newGoto.redirectJumpsToThisTo(node);
      return newGoto;
   }

   private static void redirectBranch(Unit node, Unit oldTarget, Unit newTarget) {
      Iterator var3 = node.getUnitBoxes().iterator();

      while(var3.hasNext()) {
         UnitBox targetBox = (UnitBox)var3.next();
         Unit target = targetBox.getUnit();
         if (target == oldTarget) {
            targetBox.setUnit(newTarget);
         }
      }

   }

   private void removeCriticalEdges(Body b) {
      Chain<Unit> unitChain = b.getUnits();
      int size = unitChain.size();
      Map<Unit, List<Unit>> predecessors = new HashMap(2 * size + 1, 0.7F);
      Iterator unitIt = unitChain.snapshotIterator();

      Unit currentUnit;
      while(unitIt.hasNext()) {
         currentUnit = (Unit)unitIt.next();
         Iterator succsIt = currentUnit.getUnitBoxes().iterator();

         while(succsIt.hasNext()) {
            Unit target = ((UnitBox)succsIt.next()).getUnit();
            List<Unit> predList = (List)predecessors.get(target);
            if (predList == null) {
               List<Unit> predList = new ArrayList();
               predList.add(currentUnit);
               predecessors.put(target, predList);
            } else {
               predList.add(currentUnit);
            }
         }
      }

      unitIt = unitChain.snapshotIterator();
      currentUnit = null;

      while(true) {
         Unit directPredecessor;
         List predList;
         int nbPreds;
         do {
            if (!unitIt.hasNext()) {
               return;
            }

            directPredecessor = currentUnit;
            currentUnit = (Unit)unitIt.next();
            predList = (List)predecessors.get(currentUnit);
            nbPreds = predList == null ? 0 : predList.size();
            if (directPredecessor != null && directPredecessor.fallsThrough()) {
               ++nbPreds;
            }
         } while(nbPreds < 2);

         if (directPredecessor != null && directPredecessor.fallsThrough()) {
            directPredecessor = insertGotoAfter(unitChain, directPredecessor, currentUnit);
         }

         Iterator predIt = predList.iterator();

         while(predIt.hasNext()) {
            Unit predecessor = (Unit)predIt.next();
            int nbSuccs = predecessor.getUnitBoxes().size();
            nbSuccs += predecessor.fallsThrough() ? 1 : 0;
            if (nbSuccs >= 2) {
               if (directPredecessor == null) {
                  directPredecessor = insertGotoBefore(unitChain, currentUnit, currentUnit);
               } else {
                  directPredecessor = insertGotoAfter(unitChain, directPredecessor, currentUnit);
               }

               redirectBranch(predecessor, currentUnit, directPredecessor);
            }
         }
      }
   }
}
