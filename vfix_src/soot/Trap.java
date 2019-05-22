package soot;

import java.util.List;

public interface Trap extends UnitBoxOwner {
   Unit getBeginUnit();

   Unit getEndUnit();

   Unit getHandlerUnit();

   UnitBox getBeginUnitBox();

   UnitBox getEndUnitBox();

   UnitBox getHandlerUnitBox();

   List<UnitBox> getUnitBoxes();

   SootClass getException();

   void setBeginUnit(Unit var1);

   void setEndUnit(Unit var1);

   void setHandlerUnit(Unit var1);

   void setException(SootClass var1);

   Object clone();
}
