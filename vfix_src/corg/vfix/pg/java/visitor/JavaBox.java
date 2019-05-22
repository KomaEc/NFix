package corg.vfix.pg.java.visitor;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

public class JavaBox {
   public Statement stmt = null;
   public String var = null;
   public String expr = null;
   public int line = -2;
   public Statement outputStmt = null;
   public MethodCallExpr methodCallExpr = null;
   public NameExpr tmp = null;
   public int begin = 100000;
   public int end = -2;
   public BlockStmt blockStmt = null;
}
