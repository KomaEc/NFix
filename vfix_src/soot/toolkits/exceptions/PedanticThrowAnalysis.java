package soot.toolkits.exceptions;

import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.baf.ThrowInst;
import soot.jimple.ThrowStmt;

public class PedanticThrowAnalysis extends AbstractThrowAnalysis {
   public PedanticThrowAnalysis(Singletons.Global g) {
   }

   public static PedanticThrowAnalysis v() {
      return G.v().soot_toolkits_exceptions_PedanticThrowAnalysis();
   }

   public ThrowableSet mightThrow(Unit u) {
      return ThrowableSet.Manager.v().ALL_THROWABLES;
   }

   public ThrowableSet mightThrowImplicitly(ThrowInst t) {
      return ThrowableSet.Manager.v().ALL_THROWABLES;
   }

   public ThrowableSet mightThrowImplicitly(ThrowStmt t) {
      return ThrowableSet.Manager.v().ALL_THROWABLES;
   }
}
