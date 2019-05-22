package soot.baf;

import soot.Unit;

public interface Inst extends Unit {
   int getInCount();

   int getOutCount();

   int getNetCount();

   int getInMachineCount();

   int getOutMachineCount();

   int getNetMachineCount();

   boolean containsInvokeExpr();

   boolean containsFieldRef();

   boolean containsArrayRef();

   boolean containsNewExpr();
}
