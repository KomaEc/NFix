package soot;

import java.io.Serializable;
import soot.tagkit.Host;

public interface ValueBox extends Host, Serializable {
   void setValue(Value var1);

   Value getValue();

   boolean canContainValue(Value var1);

   void toString(UnitPrinter var1);
}
