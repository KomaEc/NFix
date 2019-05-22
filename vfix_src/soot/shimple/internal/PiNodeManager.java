package soot.shimple.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import soot.Local;
import soot.PatchingChain;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.shimple.PiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleFactory;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.ReversibleGraph;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class PiNodeManager {
   protected ShimpleBody body;
   protected ShimpleFactory sf;
   protected DominatorTree<Block> dt;
   protected DominanceFrontier<Block> df;
   protected ReversibleGraph<Block> cfg;
   protected boolean trimmed;
   protected MultiMap<Local, Block> varToBlocks;

   public PiNodeManager(ShimpleBody body, boolean trimmed, ShimpleFactory sf) {
      this.body = body;
      this.trimmed = trimmed;
      this.sf = sf;
   }

   public void update() {
      this.cfg = this.sf.getReverseBlockGraph();
      this.dt = this.sf.getReverseDominatorTree();
      this.df = this.sf.getReverseDominanceFrontier();
   }

   public boolean insertTrivialPiNodes() {
      this.update();
      boolean change = false;
      MultiMap<Local, Block> localsToUsePoints = new SHashMultiMap();
      this.varToBlocks = new HashMultiMap();
      Iterator var3 = this.cfg.iterator();

      while(var3.hasNext()) {
         Block block = (Block)var3.next();
         Iterator var5 = block.iterator();

         while(var5.hasNext()) {
            Unit unit = (Unit)var5.next();
            List<ValueBox> useBoxes = unit.getUseBoxes();
            Iterator useBoxesIt = useBoxes.iterator();

            while(useBoxesIt.hasNext()) {
               Value use = ((ValueBox)useBoxesIt.next()).getValue();
               if (use instanceof Local) {
                  localsToUsePoints.put((Local)use, block);
               }
            }

            if (Shimple.isPiNode(unit)) {
               this.varToBlocks.put(Shimple.getLhsLocal(unit), block);
            }
         }
      }

      int[] workFlags = new int[this.cfg.size()];
      int[] hasAlreadyFlags = new int[this.cfg.size()];
      int iterCount = 0;
      Stack<Block> workList = new Stack();
      Iterator var19 = localsToUsePoints.keySet().iterator();

      while(var19.hasNext()) {
         Local local = (Local)var19.next();
         ++iterCount;
         Iterator var21 = localsToUsePoints.get(local).iterator();

         while(var21.hasNext()) {
            Block block = (Block)var21.next();
            workFlags[block.getIndexInMethod()] = iterCount;
            workList.push(block);
         }

         while(!workList.empty()) {
            Block block = (Block)workList.pop();
            DominatorNode<Block> node = this.dt.getDode(block);
            Iterator var11 = this.df.getDominanceFrontierOf(node).iterator();

            while(var11.hasNext()) {
               DominatorNode<Block> frontierNode = (DominatorNode)var11.next();
               Block frontierBlock = (Block)frontierNode.getGode();
               int fBIndex = frontierBlock.getIndexInMethod();
               if (hasAlreadyFlags[fBIndex] < iterCount) {
                  this.insertPiNodes(local, frontierBlock);
                  change = true;
                  hasAlreadyFlags[fBIndex] = iterCount;
                  if (workFlags[fBIndex] < iterCount) {
                     workFlags[fBIndex] = iterCount;
                     workList.push(frontierBlock);
                  }
               }
            }
         }
      }

      if (change) {
         this.sf.clearCache();
      }

      return change;
   }

   public void insertPiNodes(Local local, Block frontierBlock) {
      if (!this.varToBlocks.get(local).contains(frontierBlock.getSuccs().get(0))) {
         Unit u = frontierBlock.getTail();
         if (this.trimmed) {
            Iterator var4 = u.getUseBoxes().iterator();

            ValueBox vb;
            do {
               if (!var4.hasNext()) {
                  return;
               }

               vb = (ValueBox)var4.next();
            } while(!vb.getValue().equals(local));
         }

         if (u instanceof IfStmt) {
            this.piHandleIfStmt(local, (IfStmt)u);
         } else {
            if (!(u instanceof LookupSwitchStmt) && !(u instanceof TableSwitchStmt)) {
               throw new RuntimeException("Assertion failed: Unhandled stmt: " + u);
            }

            this.piHandleSwitchStmt(local, u);
         }

      }
   }

   public void piHandleIfStmt(Local local, IfStmt u) {
      Unit target = u.getTarget();
      PiExpr pit = Shimple.v().newPiExpr(local, u, Boolean.TRUE);
      PiExpr pif = Shimple.v().newPiExpr(local, u, Boolean.FALSE);
      Unit addt = Jimple.v().newAssignStmt(local, pit);
      Unit addf = Jimple.v().newAssignStmt(local, pif);
      PatchingChain<Unit> units = this.body.getUnits();
      units.insertAfter((Unit)addf, (Unit)u);
      Unit predOfTarget = null;

      try {
         predOfTarget = units.getPredOf((Unit)target);
      } catch (NoSuchElementException var11) {
         predOfTarget = null;
      }

      if (predOfTarget != null && predOfTarget.fallsThrough()) {
         GotoStmt gotoStmt = Jimple.v().newGotoStmt((Unit)target);
         units.insertAfter((Unit)gotoStmt, (Unit)predOfTarget);
      }

      units.getNonPatchingChain().insertBefore((Object)addt, target);
      u.setTarget(addt);
   }

   public void piHandleSwitchStmt(Local local, Unit u) {
      List<UnitBox> targetBoxes = new ArrayList();
      List<Object> targetKeys = new ArrayList();
      int low;
      if (u instanceof LookupSwitchStmt) {
         LookupSwitchStmt lss = (LookupSwitchStmt)u;
         targetBoxes.add(lss.getDefaultTargetBox());
         targetKeys.add("default");

         for(low = 0; low < lss.getTargetCount(); ++low) {
            targetBoxes.add(lss.getTargetBox(low));
         }

         targetKeys.addAll(lss.getLookupValues());
      } else {
         if (!(u instanceof TableSwitchStmt)) {
            throw new RuntimeException("Assertion failed.");
         }

         TableSwitchStmt tss = (TableSwitchStmt)u;
         low = tss.getLowIndex();
         int hi = tss.getHighIndex();
         targetBoxes.add(tss.getDefaultTargetBox());
         targetKeys.add("default");

         int i;
         for(i = 0; i <= hi - low; ++i) {
            targetBoxes.add(tss.getTargetBox(i));
         }

         for(i = low; i <= hi; ++i) {
            targetKeys.add(new Integer(i));
         }
      }

      for(int count = 0; count < targetBoxes.size(); ++count) {
         UnitBox targetBox = (UnitBox)targetBoxes.get(count);
         Unit target = targetBox.getUnit();
         Object targetKey = targetKeys.get(count);
         PiExpr pi1 = Shimple.v().newPiExpr(local, u, targetKey);
         Unit add1 = Jimple.v().newAssignStmt(local, pi1);
         PatchingChain<Unit> units = this.body.getUnits();
         Unit predOfTarget = null;

         try {
            predOfTarget = units.getPredOf(target);
         } catch (NoSuchElementException var14) {
            predOfTarget = null;
         }

         if (predOfTarget != null && predOfTarget.fallsThrough()) {
            GotoStmt gotoStmt = Jimple.v().newGotoStmt(target);
            units.insertAfter((Unit)gotoStmt, (Unit)predOfTarget);
         }

         units.getNonPatchingChain().insertBefore((Object)add1, target);
         targetBox.setUnit(add1);
      }

   }

   public void eliminatePiNodes(boolean smart) {
      if (smart) {
         Map<Local, Value> newToOld = new HashMap();
         List<ValueBox> boxes = new ArrayList();
         Iterator boxesIt = this.body.getUnits().iterator();

         while(boxesIt.hasNext()) {
            Unit u = (Unit)boxesIt.next();
            PiExpr pe = Shimple.getPiExpr(u);
            if (pe != null) {
               newToOld.put(Shimple.getLhsLocal(u), pe.getValue());
               boxesIt.remove();
            } else {
               boxes.addAll(u.getUseBoxes());
            }
         }

         boxesIt = boxes.iterator();

         while(boxesIt.hasNext()) {
            ValueBox box = (ValueBox)boxesIt.next();
            Value value = box.getValue();
            Value old = (Value)newToOld.get(value);
            if (old != null) {
               box.setValue(old);
            }
         }

         DeadAssignmentEliminator.v().transform(this.body);
         CopyPropagator.v().transform(this.body);
         DeadAssignmentEliminator.v().transform(this.body);
      } else {
         Iterator var8 = this.body.getUnits().iterator();

         while(var8.hasNext()) {
            Unit u = (Unit)var8.next();
            PiExpr pe = Shimple.getPiExpr(u);
            if (pe != null) {
               ((AssignStmt)u).setRightOp(pe.getValue());
            }
         }
      }

   }

   public static List<ValueBox> getUseBoxesFromBlock(Block block) {
      Iterator<Unit> unitsIt = block.iterator();
      ArrayList useBoxesList = new ArrayList();

      while(unitsIt.hasNext()) {
         useBoxesList.addAll(((Unit)unitsIt.next()).getUseBoxes());
      }

      return useBoxesList;
   }
}
