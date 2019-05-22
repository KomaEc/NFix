package soot.jimple.spark.pag;

import soot.RefType;
import soot.SootMethod;

public class StringConstantNode extends AllocNode {
   public String toString() {
      return "StringConstantNode " + this.getNumber() + " " + this.newExpr;
   }

   public String getString() {
      return (String)this.newExpr;
   }

   StringConstantNode(PAG pag, String sc) {
      super(pag, sc, RefType.v("java.lang.String"), (SootMethod)null);
   }
}
