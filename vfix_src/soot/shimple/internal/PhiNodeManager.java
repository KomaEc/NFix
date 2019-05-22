package soot.shimple.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import soot.IdentityUnit;
import soot.Local;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleFactory;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DominanceFrontier;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.scalar.GuaranteedDefs;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class PhiNodeManager {
   protected ShimpleBody body;
   protected ShimpleFactory sf;
   protected DominatorTree<Block> dt;
   protected DominanceFrontier<Block> df;
   protected BlockGraph cfg;
   protected GuaranteedDefs gd;
   protected MultiMap<Local, Block> varToBlocks;
   protected Map<Unit, Block> unitToBlock;

   public PhiNodeManager(ShimpleBody body, ShimpleFactory sf) {
      this.body = body;
      this.sf = sf;
   }

   public void update() {
      this.gd = new GuaranteedDefs(this.sf.getUnitGraph());
      this.cfg = this.sf.getBlockGraph();
      this.dt = this.sf.getDominatorTree();
      this.df = this.sf.getDominanceFrontier();
   }

   public boolean insertTrivialPhiNodes() {
      this.update();
      boolean change = false;
      this.varToBlocks = new HashMultiMap();
      Map<Local, List<Block>> localsToDefPoints = new HashMap();
      Iterator var3 = this.cfg.iterator();

      while(var3.hasNext()) {
         Block block = (Block)var3.next();
         Iterator var5 = block.iterator();

         while(var5.hasNext()) {
            Unit unit = (Unit)var5.next();
            List<ValueBox> defBoxes = unit.getDefBoxes();
            Iterator var8 = defBoxes.iterator();

            while(var8.hasNext()) {
               ValueBox vb = (ValueBox)var8.next();
               Value def = vb.getValue();
               if (def instanceof Local) {
                  Local local = (Local)def;
                  List<Block> def_points = null;
                  if (localsToDefPoints.containsKey(local)) {
                     def_points = (List)localsToDefPoints.get(local);
                  } else {
                     def_points = new ArrayList();
                     localsToDefPoints.put(local, def_points);
                  }

                  ((List)def_points).add(block);
               }
            }

            if (Shimple.isPhiNode(unit)) {
               this.varToBlocks.put(Shimple.getLhsLocal(unit), block);
            }
         }
      }

      int[] workFlags = new int[this.cfg.size()];
      int iterCount = 0;
      Stack<Block> workList = new Stack();
      Map<Integer, Integer> has_already = new HashMap();
      Iterator blocksIt = this.cfg.iterator();

      while(blocksIt.hasNext()) {
         Block block = (Block)blocksIt.next();
         has_already.put(block.getIndexInMethod(), 0);
      }

      blocksIt = localsToDefPoints.keySet().iterator();

      while(true) {
         Local local;
         List def_points;
         do {
            if (!blocksIt.hasNext()) {
               return change;
            }

            local = (Local)blocksIt.next();
            ++iterCount;
            def_points = (List)localsToDefPoints.get(local);
         } while(def_points.size() == 1);

         Iterator var24 = def_points.iterator();

         while(var24.hasNext()) {
            Block block = (Block)var24.next();
            workFlags[block.getIndexInMethod()] = iterCount;
            workList.push(block);
         }

         while(!workList.empty()) {
            Block block = (Block)workList.pop();
            DominatorNode<Block> node = this.dt.getDode(block);
            Iterator frontierNodes = this.df.getDominanceFrontierOf(node).iterator();

            while(frontierNodes.hasNext()) {
               Block frontierBlock = (Block)((DominatorNode)frontierNodes.next()).getGode();
               int fBIndex = frontierBlock.getIndexInMethod();
               Iterator<Unit> unitsIt = frontierBlock.iterator();
               if (unitsIt.hasNext() && (Integer)has_already.get(frontierBlock.getIndexInMethod()) < iterCount) {
                  has_already.put(frontierBlock.getIndexInMethod(), iterCount);
                  this.prependTrivialPhiNode(local, frontierBlock);
                  change = true;
                  if (workFlags[fBIndex] < iterCount) {
                     workFlags[fBIndex] = iterCount;
                     workList.push(frontierBlock);
                  }
               }
            }
         }
      }
   }

   public void prependTrivialPhiNode(Local local, Block frontierBlock) {
      List<Block> preds = frontierBlock.getPreds();
      PhiExpr pe = Shimple.v().newPhiExpr(local, preds);
      pe.setBlockId(frontierBlock.getIndexInMethod());
      Unit trivialPhi = Jimple.v().newAssignStmt(local, pe);
      Unit blockHead = frontierBlock.getHead();
      if (blockHead instanceof IdentityUnit) {
         frontierBlock.insertAfter(trivialPhi, frontierBlock.getHead());
      } else {
         frontierBlock.insertBefore(trivialPhi, frontierBlock.getHead());
      }

      this.varToBlocks.put(local, frontierBlock);
   }

   public void trimExceptionalPhiNodes() {
      Set<Unit> handlerUnits = new HashSet();
      Iterator trapsIt = this.body.getTraps().iterator();

      while(trapsIt.hasNext()) {
         Trap trap = (Trap)trapsIt.next();
         handlerUnits.add(trap.getHandlerUnit());
      }

      Iterator var8 = this.cfg.iterator();

      while(true) {
         Block block;
         do {
            if (!var8.hasNext()) {
               return;
            }

            block = (Block)var8.next();
         } while(!handlerUnits.contains(block.getHead()));

         Iterator var5 = block.iterator();

         while(var5.hasNext()) {
            Unit unit = (Unit)var5.next();
            PhiExpr phi = Shimple.getPhiExpr(unit);
            if (phi != null) {
               this.trimPhiNode(phi);
            }
         }
      }
   }

   public void trimPhiNode(PhiExpr phiExpr) {
      MultiMap<Value, ValueUnitPair> valueToPairs = new HashMultiMap();
      Iterator valuesIt = phiExpr.getArgs().iterator();

      while(valuesIt.hasNext()) {
         ValueUnitPair argPair = (ValueUnitPair)valuesIt.next();
         Value value = argPair.getValue();
         valueToPairs.put(value, argPair);
      }

      valuesIt = valueToPairs.keySet().iterator();

      while(valuesIt.hasNext()) {
         Value value = (Value)valuesIt.next();
         Set<ValueUnitPair> pairsSet = valueToPairs.get(value);
         List<ValueUnitPair> champs = new LinkedList(pairsSet);
         List<ValueUnitPair> challengers = new LinkedList(pairsSet);
         ValueUnitPair champ = (ValueUnitPair)champs.remove(0);
         Unit champU = champ.getUnit();
         boolean retry = true;

         while(retry) {
            retry = false;
            Iterator iterator = challengers.iterator();

            while(iterator.hasNext()) {
               ValueUnitPair challenger = (ValueUnitPair)iterator.next();
               if (!challenger.equals(champ)) {
                  Unit challengerU = challenger.getUnit();
                  if (this.dominates(champU, challengerU)) {
                     phiExpr.removeArg(challenger);
                     iterator.remove();
                  } else if (this.dominates(challengerU, champU)) {
                     phiExpr.removeArg(champ);
                     champ = challenger;
                     champU = challenger.getUnit();
                  } else {
                     retry = true;
                  }
               }
            }

            if (retry) {
               if (champs.isEmpty()) {
                  break;
               }

               champ = (ValueUnitPair)champs.remove(0);
               champU = champ.getUnit();
            }
         }
      }

   }

   public boolean dominates(Unit champ, Unit challenger) {
      if (champ != null && challenger != null) {
         if (champ.equals(challenger)) {
            return true;
         } else {
            if (this.unitToBlock == null) {
               this.unitToBlock = this.getUnitToBlockMap(this.cfg);
            }

            Block champBlock = (Block)this.unitToBlock.get(champ);
            Block challengerBlock = (Block)this.unitToBlock.get(challenger);
            if (champBlock.equals(challengerBlock)) {
               Iterator unitsIt = champBlock.iterator();

               Unit unit;
               do {
                  if (!unitsIt.hasNext()) {
                     throw new RuntimeException("Assertion failed.");
                  }

                  unit = (Unit)unitsIt.next();
                  if (unit.equals(champ)) {
                     return true;
                  }
               } while(!unit.equals(challenger));

               return false;
            } else {
               DominatorNode<Block> champNode = this.dt.getDode(champBlock);
               DominatorNode<Block> challengerNode = this.dt.getDode(challengerBlock);
               return this.dt.isDominatorOf(champNode, challengerNode);
            }
         }
      } else {
         throw new RuntimeException("Assertion failed.");
      }
   }

   public boolean doEliminatePhiNodes() {
      boolean addedNewLocals = false;
      List<Unit> phiNodes = new ArrayList();
      List<AssignStmt> equivStmts = new ArrayList();
      List<ValueUnitPair> predBoxes = new ArrayList();
      Chain<Unit> units = this.body.getUnits();
      Iterator phiNodesIt = units.iterator();

      while(true) {
         Unit removeMe;
         PhiExpr phi;
         do {
            if (!phiNodesIt.hasNext()) {
               if (equivStmts.size() != predBoxes.size()) {
                  throw new RuntimeException("Assertion failed.");
               }

               for(int i = 0; i < equivStmts.size(); ++i) {
                  AssignStmt stmt = (AssignStmt)equivStmts.get(i);
                  Unit pred = ((ValueUnitPair)predBoxes.get(i)).getUnit();
                  if (pred == null) {
                     throw new RuntimeException("Assertion failed.");
                  }

                  if (pred.branches()) {
                     boolean needPriming = false;
                     Local lhsLocal = (Local)stmt.getLeftOp();
                     Local savedLocal = Jimple.v().newLocal(lhsLocal.getName() + "_", lhsLocal.getType());
                     Iterator useBoxesIt = pred.getUseBoxes().iterator();

                     while(useBoxesIt.hasNext()) {
                        ValueBox useBox = (ValueBox)useBoxesIt.next();
                        if (lhsLocal.equals(useBox.getValue())) {
                           needPriming = true;
                           addedNewLocals = true;
                           useBox.setValue(savedLocal);
                        }
                     }

                     if (needPriming) {
                        this.body.getLocals().add(savedLocal);
                        AssignStmt copyStmt = Jimple.v().newAssignStmt(savedLocal, lhsLocal);
                        units.insertBefore((Object)copyStmt, pred);
                     }

                     units.insertBefore((Object)stmt, pred);
                  } else {
                     units.insertAfter((Object)stmt, pred);
                  }
               }

               phiNodesIt = phiNodes.iterator();

               while(phiNodesIt.hasNext()) {
                  removeMe = (Unit)phiNodesIt.next();
                  units.remove(removeMe);
                  removeMe.clearUnitBoxes();
               }

               return addedNewLocals;
            }

            removeMe = (Unit)phiNodesIt.next();
            phi = Shimple.getPhiExpr(removeMe);
         } while(phi == null);

         Local lhsLocal = Shimple.getLhsLocal(removeMe);

         for(int i = 0; i < phi.getArgCount(); ++i) {
            Value phiValue = phi.getValue(i);
            AssignStmt convertedPhi = Jimple.v().newAssignStmt(lhsLocal, phiValue);
            equivStmts.add(convertedPhi);
            predBoxes.add(phi.getArgBox(i));
         }

         phiNodes.add(removeMe);
      }
   }

   public Map<Unit, Block> getUnitToBlockMap(BlockGraph blocks) {
      Map<Unit, Block> unitToBlock = new HashMap();
      Iterator blocksIt = blocks.iterator();

      while(blocksIt.hasNext()) {
         Block block = (Block)blocksIt.next();
         Iterator unitsIt = block.iterator();

         while(unitsIt.hasNext()) {
            Unit unit = (Unit)unitsIt.next();
            unitToBlock.put(unit, block);
         }
      }

      return unitToBlock;
   }
}
