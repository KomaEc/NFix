package corg.vfix.pg.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import corg.vfix.pg.java.element.TmpExprMaps;
import corg.vfix.pg.java.visitor.GetStmtByLineVisitor;
import corg.vfix.pg.java.visitor.JavaBox;
import corg.vfix.pg.java.visitor.JavaNodeMtdVisitor;
import corg.vfix.sa.vfg.VFGNode;

public class JavaNode {
   private int lineNumber;
   private VFGNode node;
   private MethodDeclaration methodDeclaration;
   private ConstructorDeclaration constructorDeclaration;
   private Expression eOne;
   private Expression eTwo;
   private Statement stmt;
   private CompilationUnit cu;
   private String newClsName;

   public JavaNode(VFGNode vfgnode) throws Exception {
      this.node = vfgnode;
      this.lineNumber = this.node.getLineNumber();
      this.cu = JavaOperationLib.getCUByVFGNode(this.node);
      this.newClsName = vfgnode.getNewClsName();
      this.setMethodByVisitor();
      this.setStatement();
      this.setElements();
   }

   public CompilationUnit getCompilationUnit() {
      return this.cu;
   }

   public String getNewClsName() {
      return this.newClsName;
   }

   public VFGNode getVFGNode() {
      return this.node;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public Expression getEOne() {
      return this.eOne;
   }

   public Expression getETwo() {
      return this.eTwo;
   }

   public Statement getStatement() {
      return this.stmt;
   }

   public BlockStmt getBody() {
      BlockStmt ob = null;
      if (this.methodDeclaration != null && this.methodDeclaration.getBody().isPresent()) {
         ob = (BlockStmt)this.methodDeclaration.getBody().get();
      } else if (this.constructorDeclaration != null) {
         ob = this.constructorDeclaration.getBody();
      }

      return ob;
   }

   private void setStatement() throws Exception {
      JavaBox javaBox = new JavaBox();
      javaBox.line = this.lineNumber;
      GetStmtByLineVisitor gsvlVisitor = new GetStmtByLineVisitor();
      gsvlVisitor.visit((CompilationUnit)this.cu, (Object)javaBox);
      this.stmt = javaBox.stmt;
      if (this.stmt == null) {
         System.out.println("cannot find statements in line " + this.lineNumber);
         throw new Exception();
      }
   }

   private void setElements() {
      Expression one = JavaOperationLib.value2Expr(this.node.getMethod(), this.node.getEOne(), this.stmt);
      Expression two = JavaOperationLib.value2Expr(this.node.getMethod(), this.node.getETwo(), this.stmt);

      try {
         this.eOne = one == null ? null : JavaParser.parseExpression(cleanExpr(one.toString()));
         this.eTwo = two == null ? null : JavaParser.parseExpression(cleanExpr(two.toString()));
      } catch (Exception var4) {
         System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
         System.out.println("fail to parse expression");
         System.out.println("eOne: " + one);
         System.out.println("eTwo: " + two);
      }

   }

   private static String cleanExpr(String expr) {
      return !TmpExprMaps.hasVarNum(expr) ? expr : TmpExprMaps.removeVarNum(expr);
   }

   private void setMethodByVisitor() {
      if (this.cu != null) {
         JavaNodeMtdVisitor mv = new JavaNodeMtdVisitor();
         mv.visit((CompilationUnit)this.cu, (Object)this);
      }

   }

   public void setMethod(ConstructorDeclaration cd) {
      this.constructorDeclaration = cd;
   }

   public void setMethod(MethodDeclaration md) {
      this.methodDeclaration = md;
   }

   public static void main(String[] args) {
      String expr = "series.value()";
      System.out.println(cleanExpr(expr));
   }
}
