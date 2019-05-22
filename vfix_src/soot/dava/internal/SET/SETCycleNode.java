package soot.dava.internal.SET;

import soot.dava.internal.asg.AugmentedStmt;
import soot.util.IterableSet;

public abstract class SETCycleNode extends SETControlFlowNode {
   public SETCycleNode(AugmentedStmt characterizingStmt, IterableSet body) {
      super(characterizingStmt, body);
   }
}
