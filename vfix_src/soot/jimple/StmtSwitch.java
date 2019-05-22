package soot.jimple;

import soot.util.Switch;

public interface StmtSwitch extends Switch {
   void caseBreakpointStmt(BreakpointStmt var1);

   void caseInvokeStmt(InvokeStmt var1);

   void caseAssignStmt(AssignStmt var1);

   void caseIdentityStmt(IdentityStmt var1);

   void caseEnterMonitorStmt(EnterMonitorStmt var1);

   void caseExitMonitorStmt(ExitMonitorStmt var1);

   void caseGotoStmt(GotoStmt var1);

   void caseIfStmt(IfStmt var1);

   void caseLookupSwitchStmt(LookupSwitchStmt var1);

   void caseNopStmt(NopStmt var1);

   void caseRetStmt(RetStmt var1);

   void caseReturnStmt(ReturnStmt var1);

   void caseReturnVoidStmt(ReturnVoidStmt var1);

   void caseTableSwitchStmt(TableSwitchStmt var1);

   void caseThrowStmt(ThrowStmt var1);

   void defaultCase(Object var1);
}
