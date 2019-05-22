package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface DefinitionStmt extends Stmt {
   Value getLeftOp();

   Value getRightOp();

   ValueBox getLeftOpBox();

   ValueBox getRightOpBox();
}
