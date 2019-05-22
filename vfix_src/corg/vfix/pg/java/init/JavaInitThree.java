package corg.vfix.pg.java.init;

import com.github.javaparser.ast.stmt.BlockStmt;
import corg.vfix.pg.java.JavaNode;
import corg.vfix.pg.java.JavaOperationLib;
import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.vfg.VFGNode;

public class JavaInitThree {
   public static void patch(JavaNode node) throws Exception {
      BlockStmt body = JavaOperationLib.getBlockStmtByStmt(node, node.getStatement());
      JavaOperationLib.insertNullCheckReplace(body, node.getStatement(), node.getETwo(), node.getVFGNode().getEOne().getType(), node.getNewClsName(), node.getVFGNode().getStmt());
      String filename = JavaOperationLib.name2Path(node.getVFGNode().getClsName());
      OperationLib.outputJavaFile(node.getCompilationUnit(), filename);
   }

   public static void patch(VFGNode vfgnode) throws Exception {
      patch(new JavaNode(vfgnode));
   }
}
