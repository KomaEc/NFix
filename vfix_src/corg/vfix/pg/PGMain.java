package corg.vfix.pg;

import corg.vfix.Main;
import corg.vfix.pg.java.init.JavaInitOne;
import corg.vfix.pg.java.init.JavaInitThree;
import corg.vfix.pg.java.init.JavaInitTwo;
import corg.vfix.pg.java.skip.JavaSkipOne;
import corg.vfix.pg.java.skip.JavaSkipThree;
import corg.vfix.pg.java.skip.JavaSkipTwo;
import corg.vfix.pg.jimple.OperationRules;
import corg.vfix.pg.jimple.OperationType;
import corg.vfix.pg.jimple.element.ExtractRules;
import corg.vfix.pg.jimple.init.RuleInitOne;
import corg.vfix.pg.jimple.init.RuleInitThree;
import corg.vfix.pg.jimple.init.RuleInitTwo;
import corg.vfix.pg.jimple.skip.RuleSkipOne;
import corg.vfix.pg.jimple.skip.RuleSkipThree;
import corg.vfix.pg.jimple.skip.RuleSkipTwo;
import corg.vfix.sa.SAMain;
import corg.vfix.sa.vfg.VFG;
import corg.vfix.sa.vfg.VFGEdge;
import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import java.util.Iterator;

public class PGMain {
   public static ArrayList<VFGNode> visited;

   public static void main(ArrayList<VFGNode> nodes) throws Exception {
      if (nodes != null && nodes.size() > 0) {
         visited = new ArrayList();
         Iterator var2 = nodes.iterator();

         while(var2.hasNext()) {
            VFGNode node = (VFGNode)var2.next();
            patch(node);
         }

      }
   }

   private static void patch(VFGNode node) throws Exception {
      if (!visited.contains(node)) {
         visited.add(node);
         if (!isSystemMtd(node)) {
            if (node.isExecuted()) {
               if (ExtractRules.main(node)) {
                  OperationRules.filter(node);
                  System.out.println("Node: " + node);
                  System.out.println("applicable operations:");
                  if (node.getOperations().isEmpty()) {
                     System.out.println("NONE");
                     if (node.getNodeType() == 0) {
                        ArrayList<VFGNode> bnodes = getNodesBehindSrc(node, SAMain.vfg);
                        if (bnodes == null) {
                           return;
                        }

                        System.out.println("@find " + bnodes.size() + " nodes behind SRC");
                        Iterator var3 = bnodes.iterator();

                        while(var3.hasNext()) {
                           VFGNode bnode = (VFGNode)var3.next();
                           patch(bnode);
                        }
                     }

                  } else {
                     Iterator var2 = node.getOperations().iterator();

                     while(var2.hasNext()) {
                        int type = (Integer)var2.next();
                        System.out.println(OperationType.typeToString(type));
                     }

                     System.out.println("patch generating...:");
                     generate(node.getOperations(), node);
                  }
               }
            }
         }
      }
   }

   private static ArrayList<VFGNode> getNodesBehindSrc(VFGNode node, VFG vfg) {
      ArrayList<VFGEdge> edges = vfg.getEdgesOutOf(node);
      ArrayList<VFGNode> nodes = new ArrayList();
      if (edges == null) {
         return nodes;
      } else {
         Iterator var5 = edges.iterator();

         while(var5.hasNext()) {
            VFGEdge edge = (VFGEdge)var5.next();
            VFGNode tgt = edge.getTo();
            if (!nodes.contains(tgt)) {
               nodes.add(tgt);
            }
         }

         return nodes;
      }
   }

   private static boolean isSystemMtd(VFGNode node) {
      String pkg = node.getMethod().getDeclaringClass().getPackageName();
      return "system".equals(pkg);
   }

   public static void generate(ArrayList<Integer> ops, VFGNode node) throws Exception {
      ArrayList<String> operations = new ArrayList();
      Iterator var4 = ops.iterator();

      while(var4.hasNext()) {
         int i = (Integer)var4.next();
         switch(i) {
         case 11:
            RuleInitOne.patch(node);
            JavaInitOne.patch(node);
            operations.add("INIT-1");
            break;
         case 12:
            RuleInitTwo.patch(node);
            JavaInitTwo.patch(node);
            operations.add("INIT-2");
            break;
         case 13:
            RuleInitThree.patch(node);
            JavaInitThree.patch(node);
            operations.add("INIT-3");
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         default:
            break;
         case 21:
            RuleSkipOne.patch(node);
            JavaSkipOne.patch(node);
            operations.add("SKIP-1");
            break;
         case 22:
            RuleSkipTwo.patch(node);
            JavaSkipTwo.patch(node);
            operations.add("SKIP-2");
            break;
         case 23:
            RuleSkipThree.patch(node);
            JavaSkipThree.patch(node);
            operations.add("SKIP-3");
         }
      }

      Main.bug.addNodePatch(operations);
   }
}
