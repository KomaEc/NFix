package soot;

public interface SideEffectTester {
   boolean unitCanReadFrom(Unit var1, Value var2);

   boolean unitCanWriteTo(Unit var1, Value var2);

   void newMethod(SootMethod var1);
}
