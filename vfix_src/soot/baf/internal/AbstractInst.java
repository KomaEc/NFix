package soot.baf.internal;

import soot.AbstractUnit;
import soot.UnitPrinter;
import soot.baf.Inst;

public abstract class AbstractInst extends AbstractUnit implements Inst {
   public String toString() {
      return this.getName() + this.getParameters();
   }

   public void toString(UnitPrinter up) {
      up.literal(this.getName());
      this.getParameters(up);
   }

   public int getInCount() {
      throw new RuntimeException("undefined " + this.toString() + "!");
   }

   public int getOutCount() {
      throw new RuntimeException("undefined " + this.toString() + "!");
   }

   public int getNetCount() {
      return this.getOutCount() - this.getInCount();
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return false;
   }

   public int getInMachineCount() {
      throw new RuntimeException("undefined" + this.toString() + "!");
   }

   public int getOutMachineCount() {
      throw new RuntimeException("undefined" + this.toString() + "!");
   }

   public int getNetMachineCount() {
      return this.getOutMachineCount() - this.getInMachineCount();
   }

   public Object clone() {
      throw new RuntimeException("undefined clone for: " + this.toString());
   }

   public abstract String getName();

   String getParameters() {
      return "";
   }

   protected void getParameters(UnitPrinter up) {
   }

   public boolean containsInvokeExpr() {
      return false;
   }

   public boolean containsArrayRef() {
      return false;
   }

   public boolean containsFieldRef() {
      return false;
   }

   public boolean containsNewExpr() {
      return false;
   }
}
