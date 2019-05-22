package soot.jimple;

import soot.Local;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public interface ArrayRef extends ConcreteRef {
   Value getBase();

   void setBase(Local var1);

   ValueBox getBaseBox();

   Value getIndex();

   void setIndex(Value var1);

   ValueBox getIndexBox();

   Type getType();

   void apply(Switch var1);
}
