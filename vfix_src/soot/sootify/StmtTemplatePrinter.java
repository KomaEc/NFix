package soot.sootify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import soot.PatchingChain;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;

class StmtTemplatePrinter implements StmtSwitch {
   private final TemplatePrinter p;
   private final ValueTemplatePrinter vtp;
   private List<Unit> jumpTargets = new ArrayList();

   public StmtTemplatePrinter(TemplatePrinter templatePrinter, PatchingChain<Unit> units) {
      this.p = templatePrinter;
      this.vtp = new ValueTemplatePrinter(this.p);
      Iterator var3 = units.iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         Iterator var5 = u.getUnitBoxes().iterator();

         while(var5.hasNext()) {
            UnitBox ub = (UnitBox)var5.next();
            this.jumpTargets.add(ub.getUnit());
         }
      }

      final List<Unit> unitsList = new ArrayList(units);
      Collections.sort(this.jumpTargets, new Comparator<Unit>() {
         public int compare(Unit o1, Unit o2) {
            return unitsList.indexOf(o1) - unitsList.indexOf(o2);
         }
      });

      for(int i = 0; i < this.jumpTargets.size(); ++i) {
         this.p.println("NopStmt jumpTarget" + i + "= Jimple.v().newNopStmt();");
      }

   }

   private String nameOfJumpTarget(Unit u) {
      if (!this.isJumpTarget(u)) {
         throw new InternalError("not a jumpt target! " + u);
      } else {
         return "jumpTarget" + this.jumpTargets.indexOf(u);
      }
   }

   private boolean isJumpTarget(Unit u) {
      return this.jumpTargets.contains(u);
   }

   private String printValueAssignment(Value value, String varName) {
      return this.vtp.printValueAssignment(value, varName);
   }

   private void printStmt(Unit u, String... ops) {
      String stmtClassName = u.getClass().getSimpleName();
      if (stmtClassName.charAt(0) == 'J') {
         stmtClassName = stmtClassName.substring(1);
      }

      if (this.isJumpTarget(u)) {
         String nameOfJumpTarget = this.nameOfJumpTarget(u);
         this.p.println("units.add(" + nameOfJumpTarget + ");");
      }

      this.p.print("units.add(");
      this.printFactoryMethodCall(stmtClassName, ops);
      this.p.printlnNoIndent(");");
   }

   private void printFactoryMethodCall(String stmtClassName, String... ops) {
      this.p.printNoIndent("Jimple.v().new");
      this.p.printNoIndent(stmtClassName);
      this.p.printNoIndent("(");
      int i = 1;
      String[] var4 = ops;
      int var5 = ops.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String op = var4[var6];
         this.p.printNoIndent(op);
         if (i < ops.length) {
            this.p.printNoIndent(",");
         }

         ++i;
      }

      this.p.printNoIndent(")");
   }

   public void caseThrowStmt(ThrowStmt stmt) {
      String varName = this.printValueAssignment(stmt.getOp(), "op");
      this.printStmt(stmt, varName);
   }

   public void caseTableSwitchStmt(TableSwitchStmt stmt) {
      this.p.openBlock();
      String varName = this.printValueAssignment(stmt.getKey(), "key");
      int lowIndex = stmt.getLowIndex();
      this.p.println("int lowIndex=" + lowIndex + ";");
      int highIndex = stmt.getHighIndex();
      this.p.println("int highIndex=" + highIndex + ";");
      this.p.println("List<Unit> targets = new LinkedList<Unit>();");
      Iterator var5 = stmt.getTargets().iterator();

      while(var5.hasNext()) {
         Unit s = (Unit)var5.next();
         String nameOfJumpTarget = this.nameOfJumpTarget(s);
         this.p.println("targets.add(" + nameOfJumpTarget + ")");
      }

      Unit defaultTarget = stmt.getDefaultTarget();
      this.p.println("Unit defaultTarget = " + this.nameOfJumpTarget(defaultTarget) + ";");
      this.printStmt(stmt, varName, "lowIndex", "highIndex", "targets", "defaultTarget");
      this.p.closeBlock();
   }

   public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
      this.printStmt(stmt);
   }

   public void caseReturnStmt(ReturnStmt stmt) {
      String varName = this.printValueAssignment(stmt.getOp(), "retVal");
      this.printStmt(stmt, varName);
   }

   public void caseRetStmt(RetStmt stmt) {
      String varName = this.printValueAssignment(stmt.getStmtAddress(), "stmtAddress");
      this.printStmt(stmt, varName);
   }

   public void caseNopStmt(NopStmt stmt) {
      this.printStmt(stmt);
   }

   public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
      this.p.openBlock();
      String keyVarName = this.printValueAssignment(stmt.getKey(), "key");
      this.p.println("List<IntConstant> lookupValues = new LinkedList<IntConstant>();");
      int i = 0;
      Iterator var4 = stmt.getLookupValues().iterator();

      while(var4.hasNext()) {
         IntConstant c = (IntConstant)var4.next();
         this.vtp.suggestVariableName("lookupValue" + i);
         c.apply(this.vtp);
         ++i;
         this.p.println("lookupValues.add(lookupValue" + i + ");");
      }

      this.p.println("List<Unit> targets = new LinkedList<Unit>();");
      var4 = stmt.getTargets().iterator();

      while(var4.hasNext()) {
         Unit u = (Unit)var4.next();
         String nameOfJumpTarget = this.nameOfJumpTarget(u);
         this.p.println("targets.add(" + nameOfJumpTarget + ")");
      }

      Unit defaultTarget = stmt.getDefaultTarget();
      this.p.println("Unit defaultTarget=" + defaultTarget.toString() + ";");
      this.printStmt(stmt, keyVarName, "lookupValues", "targets", "defaultTarget");
      this.p.closeBlock();
   }

   public void caseInvokeStmt(InvokeStmt stmt) {
      String varName = this.printValueAssignment(stmt.getInvokeExpr(), "ie");
      this.printStmt(stmt, varName);
   }

   public void caseIfStmt(IfStmt stmt) {
      String varName = this.printValueAssignment(stmt.getCondition(), "condition");
      Unit target = stmt.getTarget();
      this.vtp.suggestVariableName("target");
      String targetName = this.vtp.getLastAssignedVarName();
      this.p.println("Unit " + targetName + "=" + this.nameOfJumpTarget(target) + ";");
      this.printStmt(stmt, varName, targetName);
   }

   public void caseIdentityStmt(IdentityStmt stmt) {
      String varName = this.printValueAssignment(stmt.getLeftOp(), "lhs");
      String varName2 = this.printValueAssignment(stmt.getRightOp(), "idRef");
      this.printStmt(stmt, varName, varName2);
   }

   public void caseGotoStmt(GotoStmt stmt) {
      Unit target = stmt.getTarget();
      this.vtp.suggestVariableName("target");
      String targetName = this.vtp.getLastAssignedVarName();
      this.p.println("Unit " + targetName + "=" + this.nameOfJumpTarget(target) + ";");
      this.printStmt(stmt, targetName);
   }

   public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
      String varName = this.printValueAssignment(stmt.getOp(), "monitor");
      this.printStmt(stmt, varName);
   }

   public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
      String varName = this.printValueAssignment(stmt.getOp(), "monitor");
      this.printStmt(stmt, varName);
   }

   public void caseBreakpointStmt(BreakpointStmt stmt) {
      this.printStmt(stmt);
   }

   public void caseAssignStmt(AssignStmt stmt) {
      String varName = this.printValueAssignment(stmt.getLeftOp(), "lhs");
      String varName2 = this.printValueAssignment(stmt.getRightOp(), "rhs");
      this.printStmt(stmt, varName, varName2);
   }

   public void defaultCase(Object obj) {
      throw new InternalError("should never be called");
   }
}
