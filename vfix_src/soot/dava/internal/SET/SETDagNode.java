package soot.dava.internal.SET;

import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;

public abstract class SETDagNode extends SETControlFlowNode {
   public SETDagNode(AugmentedStmt characterizingStmt, IterableSet body) {
      super(characterizingStmt, body);
   }

   public AugmentedStmt get_EntryStmt() {
      return this.get_CharacterizingStmt();
   }
}
