package soot.jimple.internal;

import soot.SootFieldRef;
import soot.Value;
import soot.jimple.Jimple;

public class JInstanceFieldRef extends AbstractInstanceFieldRef {
   public JInstanceFieldRef(Value base, SootFieldRef fieldRef) {
      super(Jimple.v().newLocalBox(base), fieldRef);
   }

   public Object clone() {
      return new JInstanceFieldRef(Jimple.cloneIfNecessary(this.getBase()), this.fieldRef);
   }
}
