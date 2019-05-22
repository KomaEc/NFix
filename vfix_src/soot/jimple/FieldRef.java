package soot.jimple;

import soot.SootField;
import soot.SootFieldRef;

public interface FieldRef extends ConcreteRef {
   SootFieldRef getFieldRef();

   void setFieldRef(SootFieldRef var1);

   SootField getField();
}
