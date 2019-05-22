package corg.vfix.sa.vfg.build.reach;

import corg.vfix.sa.analysis.ModRefAnalyzor;
import corg.vfix.sa.cg.CG;
import corg.vfix.sa.cg.CGNode;
import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import java.util.Iterator;
import soot.SootMethod;
import soot.Value;

public class InterReach {
   public static boolean immediateReach(VFGNode node1, VFGNode node2, Value value, CG callGraph, ArrayList<VFGNode> defNodes) {
      ModRefAnalyzor.modAnalysis(callGraph, getMtdsFromNodes(defNodes));
      ModRefAnalyzor.refAnalysis(callGraph, node2.getMethod());
      CGNode mainNode = callGraph.getMainNode();
      return isReach(mainNode);
   }

   public static boolean immediateReach(VFGNode node1, VFGNode node2, Value value, CG callGraph) {
      ModRefAnalyzor.modAnalysis(callGraph, node1.getMethod());
      ModRefAnalyzor.refAnalysis(callGraph, node2.getMethod());
      CGNode mainNode = callGraph.getMainNode();
      return isReach(mainNode);
   }

   private static boolean isReach(CGNode node) {
      return node.hasMod() && node.hasRef();
   }

   private static ArrayList<SootMethod> getMtdsFromNodes(ArrayList<VFGNode> nodes) {
      ArrayList<SootMethod> mtds = new ArrayList();
      Iterator var3 = nodes.iterator();

      while(var3.hasNext()) {
         VFGNode node = (VFGNode)var3.next();
         SootMethod mtd = node.getMethod();
         if (!mtds.contains(mtd)) {
            mtds.add(mtd);
         }
      }

      return mtds;
   }
}
