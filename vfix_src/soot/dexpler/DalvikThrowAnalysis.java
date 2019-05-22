package soot.dexpler;

import soot.G;
import soot.Singletons;
import soot.baf.EnterMonitorInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.StringConstant;
import soot.toolkits.exceptions.ThrowableSet;
import soot.toolkits.exceptions.UnitThrowAnalysis;

public class DalvikThrowAnalysis extends UnitThrowAnalysis {
   public static DalvikThrowAnalysis interproceduralAnalysis = null;

   public DalvikThrowAnalysis(Singletons.Global g) {
   }

   public static DalvikThrowAnalysis v() {
      return G.v().soot_dexpler_DalvikThrowAnalysis();
   }

   protected DalvikThrowAnalysis(boolean isInterproc) {
      super(isInterproc);
   }

   public DalvikThrowAnalysis(Singletons.Global g, boolean isInterproc) {
      super(isInterproc);
   }

   public static DalvikThrowAnalysis interproc() {
      return G.v().interproceduralDalvikThrowAnalysis();
   }

   protected ThrowableSet defaultResult() {
      return this.mgr.EMPTY;
   }

   protected UnitThrowAnalysis.UnitSwitch unitSwitch() {
      return new UnitThrowAnalysis.UnitSwitch() {
         public void caseReturnInst(ReturnInst i) {
         }

         public void caseReturnVoidInst(ReturnVoidInst i) {
         }

         public void caseEnterMonitorInst(EnterMonitorInst i) {
            this.result = this.result.add(DalvikThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(DalvikThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
         }

         public void caseEnterMonitorStmt(EnterMonitorStmt s) {
            this.result = this.result.add(DalvikThrowAnalysis.this.mgr.NULL_POINTER_EXCEPTION);
            this.result = this.result.add(DalvikThrowAnalysis.this.mgr.ILLEGAL_MONITOR_STATE_EXCEPTION);
            this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(s.getOp()));
         }

         public void caseAssignStmt(AssignStmt s) {
            this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(s.getLeftOp()));
            this.result = this.result.add(DalvikThrowAnalysis.this.mightThrow(s.getRightOp()));
         }
      };
   }

   protected UnitThrowAnalysis.ValueSwitch valueSwitch() {
      return new UnitThrowAnalysis.ValueSwitch() {
         public void caseStringConstant(StringConstant c) {
         }

         public void caseClassConstant(ClassConstant c) {
         }
      };
   }
}
