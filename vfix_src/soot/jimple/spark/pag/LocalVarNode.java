package soot.jimple.spark.pag;

import java.util.HashMap;
import java.util.Map;
import soot.SootMethod;
import soot.Type;

public class LocalVarNode extends VarNode {
   protected Map<Object, ContextVarNode> cvns;
   protected SootMethod method;

   public ContextVarNode context(Object context) {
      return this.cvns == null ? null : (ContextVarNode)this.cvns.get(context);
   }

   public SootMethod getMethod() {
      return this.method;
   }

   public String toString() {
      return "LocalVarNode " + this.getNumber() + " " + this.variable + " " + this.method;
   }

   LocalVarNode(PAG pag, Object variable, Type t, SootMethod m) {
      super(pag, variable, t);
      this.method = m;
   }

   void addContext(ContextVarNode cvn, Object context) {
      if (this.cvns == null) {
         this.cvns = new HashMap();
      }

      this.cvns.put(context, cvn);
   }
}
