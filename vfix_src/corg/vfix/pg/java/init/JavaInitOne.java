package corg.vfix.pg.java.init;

import com.github.javaparser.ast.stmt.BlockStmt;
import corg.vfix.pg.java.JavaNode;
import corg.vfix.pg.java.JavaOperationLib;
import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.vfg.VFGNode;
import java.io.FileNotFoundException;

public class JavaInitOne {
   public static void patch(JavaNode node) throws FileNotFoundException {
      BlockStmt body = JavaOperationLib.getBlockStmtByStmt(node, node.getStatement());
      JavaOperationLib.insertNullCheckInit(body, node.getStatement(), node.getETwo(), node.getNewClsName(), node.getVFGNode().getETwo().getType());
      String filename = JavaOperationLib.name2Path(node.getVFGNode().getClsName());
      OperationLib.outputJavaFile(node.getCompilationUnit(), filename);
   }

   public static void patch(VFGNode vfgnode) throws Exception {
      patch(new JavaNode(vfgnode));
   }
}
