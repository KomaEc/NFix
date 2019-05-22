package soot.jimple;

import soot.Value;
import soot.ValueBox;

public interface InstanceFieldRef extends FieldRef {
   Value getBase();

   ValueBox getBaseBox();

   void setBase(Value var1);
}
