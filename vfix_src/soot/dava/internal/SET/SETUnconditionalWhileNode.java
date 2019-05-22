package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTUnconditionalLoopNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;

public class SETUnconditionalWhileNode extends SETCycleNode {
   public SETUnconditionalWhileNode(IterableSet body) {
      super((AugmentedStmt)body.getFirst(), body);
      this.add_SubBody(body);
   }

   public IterableSet get_NaturalExits() {
      return new IterableSet();
   }

   public ASTNode emit_AST() {
      return new ASTUnconditionalLoopNode(this.get_Label(), this.emit_ASTBody((IterableSet)this.body2childChain.get(this.subBodies.get(0))));
   }

   public AugmentedStmt get_EntryStmt() {
      return this.get_CharacterizingStmt();
   }
}
