package soot.jimple.toolkits.ide;

import heros.InterproceduralCFG;
import heros.template.DefaultIFDSTabulationProblem;
import soot.SootMethod;
import soot.Unit;

public abstract class DefaultJimpleIFDSTabulationProblem<D, I extends InterproceduralCFG<Unit, SootMethod>> extends DefaultIFDSTabulationProblem<Unit, D, SootMethod, I> {
   public DefaultJimpleIFDSTabulationProblem(I icfg) {
      super(icfg);
   }
}
