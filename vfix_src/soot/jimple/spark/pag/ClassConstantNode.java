package soot.jimple.spark.pag;

import soot.RefType;
import soot.SootMethod;
import soot.jimple.ClassConstant;

public class ClassConstantNode extends AllocNode {
   public String toString() {
      return "ClassConstantNode " + this.getNumber() + " " + this.newExpr;
   }

   public ClassConstant getClassConstant() {
      return (ClassConstant)this.newExpr;
   }

   ClassConstantNode(PAG pag, ClassConstant cc) {
      super(pag, cc, RefType.v("java.lang.Class"), (SootMethod)null);
   }
}
