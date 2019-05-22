package soot.dexpler;

import soot.Body;
import soot.Type;
import soot.ValueBox;

public interface IDalvikTyper {
   boolean ENABLE_DVKTYPER = false;
   boolean DEBUG = false;

   void setType(ValueBox var1, Type var2, boolean var3);

   void addConstraint(ValueBox var1, ValueBox var2);

   void assignType(Body var1);
}
