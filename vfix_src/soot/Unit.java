package soot;

import java.io.Serializable;
import java.util.List;
import soot.tagkit.Host;
import soot.util.Switchable;

public interface Unit extends Switchable, Host, Serializable, Context {
   List<ValueBox> getUseBoxes();

   List<ValueBox> getDefBoxes();

   List<UnitBox> getUnitBoxes();

   List<UnitBox> getBoxesPointingToThis();

   void addBoxPointingToThis(UnitBox var1);

   void removeBoxPointingToThis(UnitBox var1);

   void clearUnitBoxes();

   List<ValueBox> getUseAndDefBoxes();

   Object clone();

   boolean fallsThrough();

   boolean branches();

   void toString(UnitPrinter var1);

   void redirectJumpsToThisTo(Unit var1);
}
