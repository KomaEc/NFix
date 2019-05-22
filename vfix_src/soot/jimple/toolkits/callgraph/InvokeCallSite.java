package soot.jimple.toolkits.callgraph;

import soot.Local;
import soot.SootMethod;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Stmt;

public class InvokeCallSite {
   public static final int MUST_BE_NULL = 0;
   public static final int MUST_NOT_BE_NULL = 1;
   public static final int MAY_BE_NULL = -1;
   private InstanceInvokeExpr iie;
   private Stmt stmt;
   private SootMethod container;
   private Local argArray;
   private Local base;
   private int nullnessCode;
   private ConstantArrayAnalysis.ArrayTypes reachingTypes;

   public InvokeCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, Local base) {
      this(stmt, container, iie, base, (Local)((Local)null), 0);
   }

   public InvokeCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, Local base, Local argArray, int nullnessCode) {
      this.stmt = stmt;
      this.container = container;
      this.iie = iie;
      this.base = base;
      this.argArray = argArray;
      this.nullnessCode = nullnessCode;
   }

   public InvokeCallSite(Stmt stmt, SootMethod container, InstanceInvokeExpr iie, Local base, ConstantArrayAnalysis.ArrayTypes reachingArgTypes, int nullnessCode) {
      this.stmt = stmt;
      this.container = container;
      this.iie = iie;
      this.base = base;
      this.nullnessCode = nullnessCode;
      this.argArray = null;
      this.reachingTypes = reachingArgTypes;
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

   public Local base() {
      return this.base;
   }

   public Local argArray() {
      return this.argArray;
   }

   public int nullnessCode() {
      return this.nullnessCode;
   }

   public ConstantArrayAnalysis.ArrayTypes reachingTypes() {
      return this.reachingTypes;
   }
}
