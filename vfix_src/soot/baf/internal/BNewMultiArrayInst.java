package soot.baf.internal;

import soot.ArrayType;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.NewMultiArrayInst;
import soot.util.Switch;

public class BNewMultiArrayInst extends AbstractInst implements NewMultiArrayInst {
   int dimensionCount;
   ArrayType baseType;

   public BNewMultiArrayInst(ArrayType opType, int dimensionCount) {
      this.dimensionCount = dimensionCount;
      this.baseType = opType;
   }

   public int getInCount() {
      return this.dimensionCount;
   }

   public int getOutCount() {
      return 1;
   }

   public int getInMachineCount() {
      return this.dimensionCount;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public Object clone() {
      return new BNewMultiArrayInst(this.getBaseType(), this.getDimensionCount());
   }

   public final String getName() {
      return "newmultiarray";
   }

   final String getParameters() {
      return " " + this.dimensionCount;
   }

   protected void getParameters(UnitPrinter up) {
      up.literal(" ");
      up.literal((new Integer(this.dimensionCount)).toString());
   }

   public ArrayType getBaseType() {
      return this.baseType;
   }

   public void setBaseType(ArrayType type) {
      this.baseType = type;
   }

   public int getDimensionCount() {
      return this.dimensionCount;
   }

   public void setDimensionCount(int x) {
      x = this.dimensionCount;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseNewMultiArrayInst(this);
   }

   public boolean containsNewExpr() {
      return true;
   }
}
