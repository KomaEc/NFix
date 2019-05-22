package soot.dava.toolkits.base.AST.analysis;

import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTAndCondition;
import soot.dava.internal.AST.ASTBinaryCondition;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTOrCondition;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnaryCondition;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
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
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;

public class AnalysisAdapter implements Analysis {
   public void defaultCase(Object o) {
   }

   public void caseASTMethodNode(ASTMethodNode node) {
      this.defaultCase(node);
   }

   public void caseASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      this.defaultCase(node);
   }

   public void caseASTLabeledBlockNode(ASTLabeledBlockNode node) {
      this.defaultCase(node);
   }

   public void caseASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      this.defaultCase(node);
   }

   public void caseASTSwitchNode(ASTSwitchNode node) {
      this.defaultCase(node);
   }

   public void caseASTIfNode(ASTIfNode node) {
      this.defaultCase(node);
   }

   public void caseASTIfElseNode(ASTIfElseNode node) {
      this.defaultCase(node);
   }

   public void caseASTWhileNode(ASTWhileNode node) {
      this.defaultCase(node);
   }

   public void caseASTForLoopNode(ASTForLoopNode node) {
      this.defaultCase(node);
   }

   public void caseASTDoWhileNode(ASTDoWhileNode node) {
      this.defaultCase(node);
   }

   public void caseASTTryNode(ASTTryNode node) {
      this.defaultCase(node);
   }

   public void caseASTStatementSequenceNode(ASTStatementSequenceNode node) {
      this.defaultCase(node);
   }

   public void caseASTUnaryCondition(ASTUnaryCondition uc) {
      this.defaultCase(uc);
   }

   public void caseASTBinaryCondition(ASTBinaryCondition bc) {
      this.defaultCase(bc);
   }

   public void caseASTAndCondition(ASTAndCondition ac) {
      this.defaultCase(ac);
   }

   public void caseASTOrCondition(ASTOrCondition oc) {
      this.defaultCase(oc);
   }

   public void caseType(Type t) {
      this.defaultCase(t);
   }

   public void caseDefinitionStmt(DefinitionStmt s) {
      this.defaultCase(s);
   }

   public void caseReturnStmt(ReturnStmt s) {
      this.defaultCase(s);
   }

   public void caseInvokeStmt(InvokeStmt s) {
      this.defaultCase(s);
   }

   public void caseThrowStmt(ThrowStmt s) {
      this.defaultCase(s);
   }

   public void caseDVariableDeclarationStmt(DVariableDeclarationStmt s) {
      this.defaultCase(s);
   }

   public void caseStmt(Stmt s) {
      this.defaultCase(s);
   }

   public void caseValue(Value v) {
      this.defaultCase(v);
   }

   public void caseExpr(Expr e) {
      this.defaultCase(e);
   }

   public void caseRef(Ref r) {
      this.defaultCase(r);
   }

   public void caseBinopExpr(BinopExpr be) {
      this.defaultCase(be);
   }

   public void caseUnopExpr(UnopExpr ue) {
      this.defaultCase(ue);
   }

   public void caseNewArrayExpr(NewArrayExpr nae) {
      this.defaultCase(nae);
   }

   public void caseNewMultiArrayExpr(NewMultiArrayExpr nmae) {
      this.defaultCase(nmae);
   }

   public void caseInstanceOfExpr(InstanceOfExpr ioe) {
      this.defaultCase(ioe);
   }

   public void caseInvokeExpr(InvokeExpr ie) {
      this.defaultCase(ie);
   }

   public void caseInstanceInvokeExpr(InstanceInvokeExpr iie) {
      this.defaultCase(iie);
   }

   public void caseCastExpr(CastExpr ce) {
      this.defaultCase(ce);
   }

   public void caseArrayRef(ArrayRef ar) {
      this.defaultCase(ar);
   }

   public void caseInstanceFieldRef(InstanceFieldRef ifr) {
      this.defaultCase(ifr);
   }

   public void caseStaticFieldRef(StaticFieldRef sfr) {
      this.defaultCase(sfr);
   }
}
