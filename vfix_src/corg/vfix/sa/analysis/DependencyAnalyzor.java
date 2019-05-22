package corg.vfix.sa.analysis;

import corg.vfix.sa.ddg.DDG;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.SimpleDominatorsFinder;
import soot.util.Chain;

public class DependencyAnalyzor {
   public static ArrayList<Value> getDefs(Unit unit) {
      ArrayList<Value> defs = new ArrayList();
      Iterator var3 = unit.getDefBoxes().iterator();

      while(true) {
         Value v;
         do {
            if (!var3.hasNext()) {
               return defs;
            }

            ValueBox valuebox = (ValueBox)var3.next();
            v = valuebox.getValue();
         } while(!(v instanceof Local) && !(v instanceof InstanceFieldRef));

         if (!defs.contains(v)) {
            defs.add(v);
         }
      }
   }

   public static ArrayList<Value> getUses(Unit unit) {
      ArrayList<Value> uses = new ArrayList();
      Iterator var3 = unit.getUseBoxes().iterator();

      while(true) {
         Value v;
         do {
            if (!var3.hasNext()) {
               return uses;
            }

            ValueBox valuebox = (ValueBox)var3.next();
            v = valuebox.getValue();
         } while(!(v instanceof Local) && !(v instanceof InstanceFieldRef));

         if (!uses.contains(v)) {
            uses.add(v);
         }
      }
   }

   public static List<Unit> collectInvalidStmts(Body body, Unit skippedStmt) {
      DDG ddg = new DDG(body);
      List<Unit> invalids = new ArrayList();
      List<Unit> frontier = new ArrayList();
      List<Unit> nextFrontier = new ArrayList();
      frontier.add(skippedStmt);

      label57:
      while(!frontier.isEmpty()) {
         Iterator var7 = frontier.iterator();

         label54:
         while(true) {
            Unit unit;
            do {
               if (!var7.hasNext()) {
                  frontier = new ArrayList(nextFrontier);
                  nextFrontier.clear();
                  continue label57;
               }

               unit = (Unit)var7.next();
            } while(invalids.contains(unit));

            invalids.add(unit);
            List<Unit> allUses = ddg.getUseOf(unit);
            Iterator var10 = allUses.iterator();

            while(true) {
               while(true) {
                  Unit use;
                  do {
                     if (!var10.hasNext()) {
                        continue label54;
                     }

                     use = (Unit)var10.next();
                  } while(invalids.contains(use));

                  if (use instanceof IfStmt) {
                     List<Unit> ifBlock = (List)findIfBlock(body, use);
                     Iterator var13 = ifBlock.iterator();

                     while(var13.hasNext()) {
                        Unit u = (Unit)var13.next();
                        if (!nextFrontier.contains(u)) {
                           nextFrontier.add(u);
                        }
                     }
                  } else if (!nextFrontier.contains(use)) {
                     nextFrontier.add(use);
                  }
               }
            }
         }
      }

      return invalids;
   }

   public static Unit findTargetStmt(Body body, List<Unit> stmts) {
      JimpleBody jBody = (JimpleBody)body;
      Chain<Unit> units = body.getUnits();
      Unit unit = jBody.getFirstNonIdentityStmt();

      Object lastStmt;
      for(lastStmt = null; unit != null; unit = (Unit)units.getSuccOf(unit)) {
         if (stmts.contains(unit)) {
            lastStmt = unit;
         }
      }

      return (Unit)units.getSuccOf(lastStmt);
   }

   public static Stmt findTargetStmt(Body body, List<Unit> invalid, List<Stmt> loopStmts) {
      JimpleBody jBody = (JimpleBody)body;
      Chain<Unit> units = body.getUnits();
      Unit unit = jBody.getFirstNonIdentityStmt();

      Object lastStmt;
      for(lastStmt = null; unit != null; unit = (Unit)units.getSuccOf(unit)) {
         if (invalid.contains(unit) && loopStmts.contains(unit)) {
            lastStmt = unit;
         }
      }

      return (Stmt)units.getSuccOf(lastStmt);
   }

   private static Collection<Unit> findIfBlock(Body body, Unit unit) {
      ExceptionalUnitGraph cfg = new ExceptionalUnitGraph(body);
      SimpleDominatorsFinder simpleDominatorsFinder = new SimpleDominatorsFinder(cfg);
      DominatorTree<Unit> dominatorTree = new DominatorTree(simpleDominatorsFinder);
      List<Unit> ifBlock = new ArrayList();
      ifBlock.add(unit);
      PatchingChain<Unit> allUnits = body.getUnits();
      DominatorNode<Unit> domDominatorTree = dominatorTree.getDode(unit);
      DominatorNode<Unit> domPostDominatorTree = dominatorTree.getDode(unit);
      MHGPostDominatorsFinder postDominatoersFinder = new MHGPostDominatorsFinder(cfg);
      DominatorTree<Unit> postDominatorTree = new DominatorTree(postDominatoersFinder);
      Iterator var12 = allUnits.iterator();

      while(var12.hasNext()) {
         Unit u = (Unit)var12.next();
         DominatorNode<Unit> nodeDominatorTree = dominatorTree.getDode(u);
         DominatorNode<Unit> nodePostDominatorTree = postDominatorTree.getDode(u);
         if (dominatorTree.isDominatorOf(domDominatorTree, nodeDominatorTree) && !postDominatorTree.isDominatorOf(nodePostDominatorTree, domPostDominatorTree) && !ifBlock.contains(u)) {
            ifBlock.add(u);
         }
      }

      return ifBlock;
   }
}
