package corg.vfix.pg.java.init;

import com.github.javaparser.ast.stmt.BlockStmt;
import corg.vfix.pg.java.JavaNode;
import corg.vfix.pg.java.JavaOperationLib;
import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.vfg.VFGNode;
import java.io.FileNotFoundException;
import soot.ArrayType;
import soot.RefType;
import soot.Type;

public class JavaInitTwo {
   public static void patch(JavaNode node) throws FileNotFoundException {
      BlockStmt body = JavaOperationLib.getBlockStmtByStmt(node, node.getStatement());
      Type type = node.getVFGNode().getEOne().getType();
      if (type instanceof ArrayType) {
         JavaOperationLib.replaceNullWithNew(body, node.getStatement(), node.getVFGNode().getEOne().getType(), "");
      } else if (type instanceof RefType) {
         JavaOperationLib.replaceNullWithNew(body, node.getStatement(), node.getVFGNode().getEOne().getType(), node.getNewClsName());
      }

      String filename = JavaOperationLib.name2Path(node.getVFGNode().getClsName());
      OperationLib.outputJavaFile(node.getCompilationUnit(), filename);
   }

   public static void patch(VFGNode vfgnode) throws Exception {
      patch(new JavaNode(vfgnode));
   }
}
