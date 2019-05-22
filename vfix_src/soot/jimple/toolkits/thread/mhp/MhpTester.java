package soot.jimple.toolkits.thread.mhp;

import java.util.List;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.thread.AbstractRuntimeThread;

public interface MhpTester {
   boolean mayHappenInParallel(SootMethod var1, SootMethod var2);

   boolean mayHappenInParallel(SootMethod var1, Unit var2, SootMethod var3, Unit var4);

   void printMhpSummary();

   List<AbstractRuntimeThread> getThreads();
}
