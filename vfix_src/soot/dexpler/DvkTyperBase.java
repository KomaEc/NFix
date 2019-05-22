package soot.dexpler;

import soot.Type;
import soot.ValueBox;

public abstract class DvkTyperBase {
   public static boolean ENABLE_DVKTYPER = false;

   public abstract void setType(ValueBox var1, Type var2);

   public abstract void setObjectType(ValueBox var1);

   public abstract void setConstraint(ValueBox var1, ValueBox var2);

   abstract void assignType();

   public static DvkTyperBase getDvkTyper() {
      return null;
   }
}
