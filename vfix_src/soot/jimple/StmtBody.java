package soot.jimple;

import soot.Body;
import soot.SootMethod;

public abstract class StmtBody extends Body {
   protected StmtBody(SootMethod m) {
      super(m);
   }

   protected StmtBody() {
   }
}
