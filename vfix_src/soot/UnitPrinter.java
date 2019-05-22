package soot;

import soot.jimple.Constant;
import soot.jimple.IdentityRef;

public interface UnitPrinter {
   void startUnit(Unit var1);

   void endUnit(Unit var1);

   void startUnitBox(UnitBox var1);

   void endUnitBox(UnitBox var1);

   void startValueBox(ValueBox var1);

   void endValueBox(ValueBox var1);

   void incIndent();

   void decIndent();

   void noIndent();

   void setIndent(String var1);

   String getIndent();

   void literal(String var1);

   void newline();

   void local(Local var1);

   void type(Type var1);

   void methodRef(SootMethodRef var1);

   void constant(Constant var1);

   void fieldRef(SootFieldRef var1);

   void unitRef(Unit var1, boolean var2);

   void identityRef(IdentityRef var1);

   void setPositionTagger(AttributesUnitPrinter var1);

   AttributesUnitPrinter getPositionTagger();

   StringBuffer output();
}
