package soot.baf;

import soot.SootField;
import soot.SootFieldRef;

public interface FieldArgInst extends Inst {
   SootFieldRef getFieldRef();

   SootField getField();
}
