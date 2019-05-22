package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTLabeledBlockNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;

public class SETLabeledBlockNode extends SETNode {
   public SETLabeledBlockNode(IterableSet body) {
      super(body);
      this.add_SubBody(body);
   }

   public IterableSet get_NaturalExits() {
      return ((SETNode)((IterableSet)this.body2childChain.get(this.subBodies.get(0))).getLast()).get_NaturalExits();
   }

   public ASTNode emit_AST() {
      return new ASTLabeledBlockNode(this.get_Label(), this.emit_ASTBody((IterableSet)this.body2childChain.get(this.subBodies.get(0))));
   }

   public AugmentedStmt get_EntryStmt() {
      return ((SETNode)((IterableSet)this.body2childChain.get(this.subBodies.get(0))).getFirst()).get_EntryStmt();
   }

   protected boolean resolve(SETNode parent) {
      throw new RuntimeException("Attempting auto-nest a SETLabeledBlockNode.");
   }
}
