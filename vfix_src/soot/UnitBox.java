package soot;

import java.io.Serializable;

public interface UnitBox extends Serializable {
   void setUnit(Unit var1);

   Unit getUnit();

   boolean canContainUnit(Unit var1);

   boolean isBranchTarget();

   void toString(UnitPrinter var1);
}
