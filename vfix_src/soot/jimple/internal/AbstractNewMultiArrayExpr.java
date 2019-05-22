package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.ArrayType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.JimpleToBafContext;
import soot.jimple.NewMultiArrayExpr;
import soot.util.Switch;

public abstract class AbstractNewMultiArrayExpr implements NewMultiArrayExpr, ConvertToBaf {
   ArrayType baseType;
   protected final ValueBox[] sizeBoxes;

   public abstract Object clone();

   protected AbstractNewMultiArrayExpr(ArrayType type, ValueBox[] sizeBoxes) {
      this.baseType = type;
      this.sizeBoxes = sizeBoxes;
   }

   public boolean equivTo(Object o) {
      if (o instanceof AbstractNewMultiArrayExpr) {
         AbstractNewMultiArrayExpr ae = (AbstractNewMultiArrayExpr)o;
         return this.baseType.equals(ae.baseType) && this.sizeBoxes.length == ae.sizeBoxes.length;
      } else {
         return false;
      }
   }

   public int equivHashCode() {
      return this.baseType.hashCode();
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      Type t = this.baseType.baseType;
      buffer.append("newmultiarray (" + t.toString() + ")");
      ValueBox[] var3 = this.sizeBoxes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ValueBox element = var3[var5];
         buffer.append("[" + element.getValue().toString() + "]");
      }

      for(int i = 0; i < this.baseType.numDimensions - this.sizeBoxes.length; ++i) {
         buffer.append("[]");
      }

      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      Type t = this.baseType.baseType;
      up.literal("newmultiarray");
      up.literal(" (");
      up.type(t);
      up.literal(")");
      ValueBox[] var3 = this.sizeBoxes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ValueBox element = var3[var5];
         up.literal("[");
         element.toString(up);
         up.literal("]");
      }

      for(int i = 0; i < this.baseType.numDimensions - this.sizeBoxes.length; ++i) {
         up.literal("[]");
      }

   }

   public ArrayType getBaseType() {
      return this.baseType;
   }

   public void setBaseType(ArrayType baseType) {
      this.baseType = baseType;
   }

   public ValueBox getSizeBox(int index) {
      return this.sizeBoxes[index];
   }

   public int getSizeCount() {
      return this.sizeBoxes.length;
   }

   public Value getSize(int index) {
      return this.sizeBoxes[index].getValue();
   }

   public List<Value> getSizes() {
      List<Value> toReturn = new ArrayList();
      ValueBox[] var2 = this.sizeBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         toReturn.add(element.getValue());
      }

      return toReturn;
   }

   public void setSize(int index, Value size) {
      this.sizeBoxes[index].setValue(size);
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      Collections.addAll(list, this.sizeBoxes);
      ValueBox[] var2 = this.sizeBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         list.addAll(element.getValue().getUseBoxes());
      }

      return list;
   }

   public Type getType() {
      return this.baseType;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseNewMultiArrayExpr(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      List<Value> sizes = this.getSizes();

      for(int i = 0; i < sizes.size(); ++i) {
         ((ConvertToBaf)((ConvertToBaf)sizes.get(i))).convertToBaf(context, out);
      }

      Unit u = Baf.v().newNewMultiArrayInst(this.getBaseType(), sizes.size());
      out.add(u);
      u.addAllTagsOf(context.getCurrentUnit());
   }
}
