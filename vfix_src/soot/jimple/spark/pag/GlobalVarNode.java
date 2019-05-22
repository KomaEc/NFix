package soot.jimple.spark.pag;

import soot.SootClass;
import soot.SootField;
import soot.Type;

public class GlobalVarNode extends VarNode {
   GlobalVarNode(PAG pag, Object variable, Type t) {
      super(pag, variable, t);
   }

   public String toString() {
      return "GlobalVarNode " + this.getNumber() + " " + this.variable;
   }

   public SootClass getDeclaringClass() {
      return this.variable instanceof SootField ? ((SootField)this.variable).getDeclaringClass() : null;
   }
}
