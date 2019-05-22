package soot.jimple.spark.pag;

import soot.Context;

public class ContextVarNode extends LocalVarNode {
   private Context context;

   public Context context() {
      return this.context;
   }

   public String toString() {
      return "ContextVarNode " + this.getNumber() + " " + this.variable + " " + this.method + " " + this.context;
   }

   ContextVarNode(PAG pag, LocalVarNode base, Context context) {
      super(pag, base.getVariable(), base.getType(), base.getMethod());
      this.context = context;
      base.addContext(this, context);
   }
}
