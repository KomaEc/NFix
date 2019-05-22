package soot.jimple;

public abstract class AbstractStmtSwitch implements StmtSwitch {
   Object result;

   public void caseBreakpointStmt(BreakpointStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseAssignStmt(AssignStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseGotoStmt(GotoStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseIfStmt(IfStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseNopStmt(NopStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseRetStmt(RetStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      this.defaultCase(stmt);
   }

   public void caseThrowStmt(ThrowStmt stmt) {
      this.defaultCase(stmt);
   }

   public void defaultCase(Object obj) {
   }

   public void setResult(Object result) {
      this.result = result;
   }

   public Object getResult() {
      return this.result;
   }
}
