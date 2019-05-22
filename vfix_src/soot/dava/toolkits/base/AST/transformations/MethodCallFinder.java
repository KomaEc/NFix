package soot.dava.toolkits.base.AST.transformations;

import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.grimp.internal.GAssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;

class MethodCallFinder extends DepthFirstAdapter {
   GAssignStmt def;
   boolean foundIt = false;
   boolean anyMethodCalls = false;

   public MethodCallFinder(GAssignStmt def) {
      this.def = def;
   }

   public MethodCallFinder(boolean verbose, GAssignStmt def) {
      super(verbose);
      this.def = def;
   }

   public void outDefinitionStmt(DefinitionStmt s) {
      if (s instanceof GAssignStmt && ((GAssignStmt)s).equals(this.def)) {
         this.foundIt = true;
      }

   }

   public void inInvokeExpr(InvokeExpr ie) {
      if (this.foundIt) {
         this.anyMethodCalls = true;
      }

   }

   public boolean anyMethodCalls() {
      return this.anyMethodCalls;
   }
}
