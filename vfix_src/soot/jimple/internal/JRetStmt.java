package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
import soot.jimple.RetStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JRetStmt extends AbstractStmt implements RetStmt {
   final ValueBox stmtAddressBox;

   public JRetStmt(Value stmtAddress) {
      this(Jimple.v().newLocalBox(stmtAddress));
   }

   protected JRetStmt(ValueBox stmtAddressBox) {
      this.stmtAddressBox = stmtAddressBox;
   }

   public Object clone() {
      return new JRetStmt(Jimple.cloneIfNecessary(this.getStmtAddress()));
   }

   public String toString() {
      return "ret " + this.stmtAddressBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("ret");
      up.literal(" ");
      this.stmtAddressBox.toString(up);
   }

   public Value getStmtAddress() {
      return this.stmtAddressBox.getValue();
   }

   public ValueBox getStmtAddressBox() {
      return this.stmtAddressBox;
   }

   public void setStmtAddress(Value stmtAddress) {
      this.stmtAddressBox.setValue(stmtAddress);
   }

   public List<ValueBox> getUseBoxes() {
      List<ValueBox> useBoxes = new ArrayList();
      useBoxes.addAll(this.stmtAddressBox.getValue().getUseBoxes());
      useBoxes.add(this.stmtAddressBox);
      return useBoxes;
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseRetStmt(this);
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }
}
