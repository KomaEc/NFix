package soot.dava.toolkits.base.AST.traversals;

import java.util.HashMap;
import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTForLoopNode;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTLabeledNode;
import soot.dava.internal.AST.ASTSwitchNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;

public class LabelToNodeMapper extends DepthFirstAdapter {
   private final HashMap<String, ASTLabeledNode> labelsToNode = new HashMap();

   public LabelToNodeMapper() {
   }

   public LabelToNodeMapper(boolean verbose) {
      super(verbose);
   }

   public Object getTarget(String label) {
      return this.labelsToNode.get(label);
   }

   private void addToMap(ASTLabeledNode node) {
      String str = node.get_Label().toString();
      if (str != null) {
         this.labelsToNode.put(str, node);
      }

   }

   public void inASTLabeledBlockNode(ASTLabeledBlockNode node) {
      this.addToMap(node);
   }

   public void inASTTryNode(ASTTryNode node) {
      this.addToMap(node);
   }

   public void inASTUnconditionalLoopNode(ASTUnconditionalLoopNode node) {
      this.addToMap(node);
   }

   public void inASTDoWhileNode(ASTDoWhileNode node) {
      this.addToMap(node);
   }

   public void inASTForLoopNode(ASTForLoopNode node) {
      this.addToMap(node);
   }

   public void inASTIfElseNode(ASTIfElseNode node) {
      this.addToMap(node);
   }

   public void inASTIfNode(ASTIfNode node) {
      this.addToMap(node);
   }

   public void inASTWhileNode(ASTWhileNode node) {
      this.addToMap(node);
   }

   public void inASTSwitchNode(ASTSwitchNode node) {
      this.addToMap(node);
   }

   public void inASTSynchronizedBlockNode(ASTSynchronizedBlockNode node) {
      this.addToMap(node);
   }
}
