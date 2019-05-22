package soot.jimple.toolkits.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.PhaseOptions;
import soot.Trap;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.util.Chain;

public class LoopConditionUnroller extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LoopConditionUnroller.class);
   private Set<Block> visitingSuccs;
   private Set<Block> visitedBlocks;
   private int maxSize;
   private Body body;
   private Map<Unit, List<Trap>> unitsToTraps;

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "]     Unrolling Loop Conditions...");
      }

      this.visitingSuccs = new HashSet();
      this.visitedBlocks = new HashSet();
      this.body = body;
      this.maxSize = PhaseOptions.getInt(options, "maxSize");
      BlockGraph bg = new BriefBlockGraph(body);
      Iterator var5 = bg.getHeads().iterator();

      while(var5.hasNext()) {
         Block b = (Block)var5.next();
         this.unrollConditions(b);
      }

      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "]     Unrolling Loop Conditions done.");
      }

   }

   private Unit insertGotoAfter(Unit node, Unit target) {
      Unit newGoto = Jimple.v().newGotoStmt(target);
      this.body.getUnits().insertAfter((Unit)newGoto, (Unit)node);
      return newGoto;
   }

   private Unit insertCloneAfter(Chain<Unit> unitChain, Unit node, Unit toClone) {
      Unit clone = (Unit)toClone.clone();
      this.body.getUnits().insertAfter(clone, node);
      return clone;
   }

   private int getSize(Block block) {
      int size = 0;
      Chain<Unit> unitChain = this.body.getUnits();

      for(Unit unit = block.getHead(); unit != block.getTail(); unit = (Unit)unitChain.getSuccOf(unit)) {
         ++size;
      }

      ++size;
      return size;
   }

   private Map<Unit, List<Trap>> getTraps() {
      if (this.unitsToTraps != null) {
         return this.unitsToTraps;
      } else {
         this.unitsToTraps = new HashMap();
         Iterator var1 = this.body.getTraps().iterator();

         while(var1.hasNext()) {
            Trap trap = (Trap)var1.next();
            Unit beginUnit = trap.getBeginUnit();
            List<Trap> unitTraps = (List)this.unitsToTraps.get(beginUnit);
            if (unitTraps == null) {
               unitTraps = new ArrayList();
               this.unitsToTraps.put(beginUnit, unitTraps);
            }

            ((List)unitTraps).add(trap);
            Unit endUnit = trap.getEndUnit();
            if (endUnit != beginUnit) {
               unitTraps = (List)this.unitsToTraps.get(endUnit);
               if (unitTraps == null) {
                  unitTraps = new ArrayList();
                  this.unitsToTraps.put(endUnit, unitTraps);
               }

               ((List)unitTraps).add(trap);
            }
         }

         return this.unitsToTraps;
      }
   }

   private Unit copyBlock(Block block) {
      Map<Unit, List<Trap>> traps = this.getTraps();
      Set<Trap> openedTraps = new HashSet();
      Map<Trap, Trap> copiedTraps = new HashMap();
      Chain<Unit> unitChain = this.body.getUnits();
      Unit tail = block.getTail();
      Unit immediateSucc = (Unit)unitChain.getSuccOf(tail);
      Unit newGoto = this.insertGotoAfter(tail, immediateSucc);
      Unit last = newGoto;
      boolean first = true;
      Unit copiedHead = null;

      for(Unit currentUnit = block.getHead(); currentUnit != newGoto; currentUnit = (Unit)unitChain.getSuccOf(currentUnit)) {
         last = this.insertCloneAfter(unitChain, last, currentUnit);
         if (first) {
            first = false;
            copiedHead = last;
         }

         List<Trap> currentTraps = (List)traps.get(currentUnit);
         if (currentTraps != null) {
            Iterator var14 = currentTraps.iterator();

            while(var14.hasNext()) {
               Trap trap = (Trap)var14.next();
               Trap copiedTrap;
               if (trap.getBeginUnit() == currentUnit) {
                  copiedTrap = (Trap)trap.clone();
                  copiedTrap.setBeginUnit(last);
                  copiedTraps.put(trap, copiedTrap);
                  openedTraps.add(copiedTrap);
                  this.body.getTraps().insertAfter((Object)copiedTrap, trap);
               }

               if (trap.getEndUnit() == currentUnit) {
                  copiedTrap = (Trap)copiedTraps.get(trap);
                  if (copiedTrap == null) {
                     copiedTrap = (Trap)trap.clone();
                     copiedTrap.setBeginUnit(copiedHead);
                     this.body.getTraps().insertAfter((Object)copiedTrap, trap);
                  } else {
                     openedTraps.remove(copiedTrap);
                  }

                  copiedTrap.setEndUnit(last);
               }
            }
         }
      }

      Iterator openedIterator = openedTraps.iterator();

      while(openedIterator.hasNext()) {
         ((Trap)openedIterator.next()).setEndUnit(last);
      }

      return copiedHead;
   }

   private void unrollConditions(Block block) {
      if (!this.visitedBlocks.contains(block)) {
         this.visitedBlocks.add(block);
         this.visitingSuccs.add(block);
         Iterator var2 = block.getSuccs().iterator();

         while(var2.hasNext()) {
            Block succ = (Block)var2.next();
            if (this.visitedBlocks.contains(succ)) {
               if (succ != block && this.visitingSuccs.contains(succ) && succ.getPreds().size() >= 2 && succ.getSuccs().size() == 2 && this.getSize(succ) <= this.maxSize) {
                  Unit copiedHead = this.copyBlock(succ);
                  Unit loopTail = block.getTail();
                  if (loopTail instanceof GotoStmt) {
                     ((GotoStmt)loopTail).setTarget(copiedHead);
                  } else if (loopTail instanceof IfStmt) {
                     if (((IfStmt)loopTail).getTarget() == succ.getHead()) {
                        ((IfStmt)loopTail).setTarget(copiedHead);
                     } else {
                        this.insertGotoAfter(loopTail, copiedHead);
                     }
                  } else {
                     this.insertGotoAfter(loopTail, copiedHead);
                  }
               }
            } else {
               this.unrollConditions(succ);
            }
         }

         this.visitingSuccs.remove(block);
      }
   }
}
