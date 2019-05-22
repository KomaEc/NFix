package soot.jimple.spark.sets;

import soot.jimple.spark.pag.Node;

public abstract class P2SetVisitor {
   protected boolean returnValue = false;

   public abstract void visit(Node var1);

   public boolean getReturnValue() {
      return this.returnValue;
   }
}
