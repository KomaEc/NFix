package soot.jimple.toolkits.thread;

import soot.SootMethod;
import soot.Value;

public interface IThreadLocalObjectsAnalysis {
   boolean isObjectThreadLocal(Value var1, SootMethod var2);
}
