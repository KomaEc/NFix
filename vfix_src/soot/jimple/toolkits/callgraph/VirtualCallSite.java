package soot.jimple.toolkits.callgraph;

import soot.Kind;
import soot.SootMethod;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;
import soot.util.NumberedString;

public class VirtualCallSite {
   private InstanceInvokeExpr iie;
   private Stmt stmt;
   private SootMethod container;
   private NumberedString subSig;
   Kind kind;

   public VirtualCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, NumberedString subSig, Kind kind) {
      this.stmt = stmt;
      this.container = container;
      this.iie = iie;
      this.subSig = subSig;
      this.kind = kind;
   }

   public Stmt stmt() {
      return this.stmt;
   }

   public SootMethod container() {
      return this.container;
   }

   public InstanceInvokeExpr iie() {
      return this.iie;
   }

   public NumberedString subSig() {
      return this.subSig;
   }

   public Kind kind() {
      return this.kind;
   }
}
