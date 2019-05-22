package soot;

import java.io.Serializable;
import java.util.List;
import soot.util.Switchable;

public interface Value extends Switchable, EquivTo, Serializable {
   List<ValueBox> getUseBoxes();

   Type getType();

   Object clone();

   void toString(UnitPrinter var1);
}
