package soot.baf.internal;

import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.PushInst;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.LongConstant;
import soot.util.Switch;

public class BPushInst extends AbstractInst implements PushInst {
   private Constant constant;

   public BPushInst(Constant c) {
      this.constant = c;
   }

   public Object clone() {
      return new BPushInst(this.getConstant());
   }

   public final String getName() {
      return "push";
   }

   final String getParameters() {
      return " " + this.constant.toString();
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      up.constant(this.constant);
   }

   public int getInCount() {
      return 0;
   }

   public int getInMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return !(this.constant instanceof LongConstant) && !(this.constant instanceof DoubleConstant) ? 1 : 2;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).casePushInst(this);
   }

   public Constant getConstant() {
      return this.constant;
   }

   public void setConstant(Constant c) {
      this.constant = c;
   }
}
