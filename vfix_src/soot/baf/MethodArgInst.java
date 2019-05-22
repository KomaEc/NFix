package soot.baf;

import soot.SootMethod;
import soot.SootMethodRef;

public interface MethodArgInst extends Inst {
   SootMethodRef getMethodRef();

   SootMethod getMethod();
}
