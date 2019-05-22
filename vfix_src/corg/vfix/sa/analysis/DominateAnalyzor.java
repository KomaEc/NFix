package corg.vfix.sa.analysis;

import corg.vfix.sa.ddg.DDGNode;
import corg.vfix.sa.vfg.VFGNode;
import java.util.Iterator;
import soot.Unit;
import soot.Value;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.DominatorTree;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.util.Chain;

public class DominateAnalyzor {
   public static boolean isDominate(JimpleBody body, Stmt s1, Stmt s2) {
      ExceptionalUnitGraph cfg = new ExceptionalUnitGraph(body);
      MHGDominatorsFinder dominatoersFinder = new MHGDominatorsFinder(cfg);
      DominatorTree<Unit> dominatorTree = new DominatorTree(dominatoersFinder);
      DominatorNode<Unit> node1 = dominatorTree.getDode(s1);
      DominatorNode<Unit> node2 = dominatorTree.getDode(s2);
      return dominatorTree.isDominatorOf(node1, node2);
   }

   public static boolean isPostDominate(JimpleBody body, Stmt s1, Stmt s2) {
      ExceptionalUnitGraph cfg = new ExceptionalUnitGraph(body);
      MHGPostDominatorsFinder postDominatoersFinder = new MHGPostDominatorsFinder(cfg);
      DominatorTree<Unit> postDominatorTree = new DominatorTree(postDominatoersFinder);
      DominatorNode<Unit> node1 = postDominatorTree.getDode(s1);
      DominatorNode<Unit> node2 = postDominatorTree.getDode(s2);
      return postDominatorTree.isDominatorOf(node1, node2);
   }

   public static boolean hasPostDomUse(VFGNode node) {
      Chain<Unit> units = node.getUnits();
      Value v = node.getETwo();
      Iterator var4 = units.iterator();

      while(true) {
         Stmt stmt;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            Unit unit = (Unit)var4.next();
            stmt = (Stmt)unit;
         } while(stmt.equals(node.getStmt()));

         DDGNode ddgNode = new DDGNode(stmt);
         Iterator var8 = ddgNode.getUses().iterator();

         while(var8.hasNext()) {
            Value value = (Value)var8.next();
            if (value.equals(v) && isPostDominate((JimpleBody)node.getBody(), stmt, node.getStmt())) {
               return true;
            }
         }
      }
   }
}
