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

public interface Analysis {
   void caseASTMethodNode(ASTMethodNode var1);

   void caseASTSynchronizedBlockNode(ASTSynchronizedBlockNode var1);

   void caseASTLabeledBlockNode(ASTLabeledBlockNode var1);

   void caseASTUnconditionalLoopNode(ASTUnconditionalLoopNode var1);

   void caseASTSwitchNode(ASTSwitchNode var1);

   void caseASTIfNode(ASTIfNode var1);

   void caseASTIfElseNode(ASTIfElseNode var1);

   void caseASTWhileNode(ASTWhileNode var1);

   void caseASTForLoopNode(ASTForLoopNode var1);

   void caseASTDoWhileNode(ASTDoWhileNode var1);

   void caseASTTryNode(ASTTryNode var1);

   void caseASTStatementSequenceNode(ASTStatementSequenceNode var1);

   void caseASTUnaryCondition(ASTUnaryCondition var1);

   void caseASTBinaryCondition(ASTBinaryCondition var1);

   void caseASTAndCondition(ASTAndCondition var1);

   void caseASTOrCondition(ASTOrCondition var1);

   void caseType(Type var1);

   void caseDefinitionStmt(DefinitionStmt var1);

   void caseReturnStmt(ReturnStmt var1);

   void caseInvokeStmt(InvokeStmt var1);

   void caseThrowStmt(ThrowStmt var1);

   void caseDVariableDeclarationStmt(DVariableDeclarationStmt var1);

   void caseStmt(Stmt var1);

   void caseValue(Value var1);

   void caseExpr(Expr var1);

   void caseRef(Ref var1);

   void caseBinopExpr(BinopExpr var1);

   void caseUnopExpr(UnopExpr var1);

   void caseNewArrayExpr(NewArrayExpr var1);

   void caseNewMultiArrayExpr(NewMultiArrayExpr var1);

   void caseInstanceOfExpr(InstanceOfExpr var1);

   void caseInvokeExpr(InvokeExpr var1);

   void caseInstanceInvokeExpr(InstanceInvokeExpr var1);

   void caseCastExpr(CastExpr var1);

   void caseArrayRef(ArrayRef var1);

   void caseInstanceFieldRef(InstanceFieldRef var1);

   void caseStaticFieldRef(StaticFieldRef var1);
}
