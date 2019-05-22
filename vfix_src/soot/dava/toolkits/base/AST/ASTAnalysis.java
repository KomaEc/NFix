package soot.dava.toolkits.base.AST;

import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;

public abstract class ASTAnalysis {
   public static final int ANALYSE_AST = 0;
   public static final int ANALYSE_STMTS = 1;
   public static final int ANALYSE_VALUES = 2;

   public abstract int getAnalysisDepth();

   public void analyseASTNode(ASTNode n) {
   }

   public void analyseDefinitionStmt(DefinitionStmt s) {
   }

   public void analyseReturnStmt(ReturnStmt s) {
   }

   public void analyseInvokeStmt(InvokeStmt s) {
   }

   public void analyseThrowStmt(ThrowStmt s) {
   }

   public void analyseStmt(Stmt s) {
   }

   public void analyseBinopExpr(BinopExpr v) {
   }

   public void analyseUnopExpr(UnopExpr v) {
   }

   public void analyseNewArrayExpr(NewArrayExpr v) {
   }

   public void analyseNewMultiArrayExpr(NewMultiArrayExpr v) {
   }

   public void analyseInstanceOfExpr(InstanceOfExpr v) {
   }

   public void analyseInstanceInvokeExpr(InstanceInvokeExpr v) {
   }

   public void analyseInvokeExpr(InvokeExpr v) {
   }

   public void analyseExpr(Expr v) {
   }

   public void analyseArrayRef(ArrayRef v) {
   }

   public void analyseInstanceFieldRef(InstanceFieldRef v) {
   }

   public void analyseRef(Ref v) {
   }

   public void analyseValue(Value v) {
   }
}
