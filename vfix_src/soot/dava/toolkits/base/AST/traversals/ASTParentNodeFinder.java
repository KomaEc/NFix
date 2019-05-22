package soot.dava.toolkits.base.AST.traversals;

import java.util.HashMap;
import java.util.Stack;
import soot.Unit;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTStatementSequenceNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.javaRep.DVariableDeclarationStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;

public class ASTParentNodeFinder extends DepthFirstAdapter {
   HashMap<Unit, ASTNode> parentOf = new HashMap();
   Stack<ASTNode> parentStack = new Stack();

   public ASTParentNodeFinder() {
   }

   public ASTParentNodeFinder(boolean verbose) {
      super(verbose);
   }

   public void inASTMethodNode(ASTMethodNode node) {
      this.parentOf.put(node, (Object)null);
      this.parentStack.push(node);
   }

   public void outASTMethodNode(ASTMethodNode node) {
      this.parentStack.pop();
   }

   public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      this.parentStack.pop();
   }

   public void inASTLabeledBlockNode(ASTLabeledBlockNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTLabeledBlockNode(ASTLabeledBlockNode node) {
      this.parentStack.pop();
   }

   public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      this.parentStack.pop();
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTSwitchNode(ASTSwitchNode node) {
      this.parentStack.pop();
   }

   public void inASTIfNode(ASTIfNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTIfNode(ASTIfNode node) {
      this.parentStack.pop();
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTIfElseNode(ASTIfElseNode node) {
      this.parentStack.pop();
   }

   public void inASTWhileNode(ASTWhileNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTWhileNode(ASTWhileNode node) {
      this.parentStack.pop();
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTForLoopNode(ASTForLoopNode node) {
      this.parentStack.pop();
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTDoWhileNode(ASTDoWhileNode node) {
      this.parentStack.pop();
   }

   public void inASTTryNode(ASTTryNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTTryNode(ASTTryNode node) {
      this.parentStack.pop();
   }

   public void inASTStatementSequenceNode(ASTStatementSequenceNode node) {
      this.parentOf.put(node, this.parentStack.peek());
      this.parentStack.push(node);
   }

   public void outASTStatementSequenceNode(ASTStatementSequenceNode node) {
      this.parentStack.pop();
   }

   public void inDefinitionStmt(DefinitionStmt s) {
      this.parentOf.put(s, this.parentStack.peek());
   }

   public void inReturnStmt(ReturnStmt s) {
      this.parentOf.put(s, this.parentStack.peek());
   }

   public void inInvokeStmt(InvokeStmt s) {
      this.parentOf.put(s, this.parentStack.peek());
   }

   public void inThrowStmt(ThrowStmt s) {
      this.parentOf.put(s, this.parentStack.peek());
   }

   public void inDVariableDeclarationStmt(DVariableDeclarationStmt s) {
      this.parentOf.put(s, this.parentStack.peek());
   }

   public void inStmt(Stmt s) {
      this.parentOf.put(s, this.parentStack.peek());
   }

   public Object getParentOf(Object statementOrNode) {
      return this.parentOf.get(statementOrNode);
   }
}
