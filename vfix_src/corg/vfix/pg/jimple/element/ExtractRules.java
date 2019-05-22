package corg.vfix.pg.jimple.element;

import corg.vfix.sa.vfg.NodeType;
import corg.vfix.sa.vfg.VFGNode;

public class ExtractRules {
   public static boolean main(VFGNode node) throws Exception {
      StmtType.setStmtType(node);
      if (node.getNodeType() == 0) {
         return ExtractSrcRules.main(node);
      } else if (node.getNodeType() == 1) {
         return ExtractTransRules.main(node);
      } else {
         return node.getNodeType() == 2 ? ExtractSinkRules.main(node) : false;
      }
   }

   public static void printElements(VFGNode node) {
      System.out.println("*********print Elements***********");
      System.out.println(node.getStmt());
      System.out.println("Type: " + NodeType.typeToString(node.getNodeType()));
      System.out.println("StmtType: " + StmtType.typeToString(node.getStmtType()));
      System.out.println("EOne: " + node.getEOne());
      System.out.println("ETwo: " + node.getETwo());
   }
}
