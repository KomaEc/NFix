package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;

public class SETTopNode extends SETNode {
   public SETTopNode(IterableSet body) {
      super(body);
      this.add_SubBody(body);
   }

   public IterableSet get_NaturalExits() {
      return new IterableSet();
   }

   public ASTNode emit_AST() {
      return new ASTMethodNode(this.emit_ASTBody((IterableSet)this.body2childChain.get(this.subBodies.get(0))));
   }

   public AugmentedStmt get_EntryStmt() {
      throw new RuntimeException("Not implemented.");
   }

   protected boolean resolve(SETNode parent) {
      throw new RuntimeException("Attempting auto-nest a SETTopNode.");
   }
}
