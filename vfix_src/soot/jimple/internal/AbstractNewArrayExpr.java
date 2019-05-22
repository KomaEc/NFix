package soot.jimple.internal;

import java.util.ArrayList;
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
import soot.jimple.NewArrayExpr;
import soot.util.Switch;

public abstract class AbstractNewArrayExpr implements NewArrayExpr, ConvertToBaf {
   Type baseType;
   final ValueBox sizeBox;

   protected AbstractNewArrayExpr(Type type, ValueBox sizeBox) {
      this.baseType = type;
      this.sizeBox = sizeBox;
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractNewArrayExpr)) {
         return false;
      } else {
         AbstractNewArrayExpr ae = (AbstractNewArrayExpr)o;
         return this.sizeBox.getValue().equivTo(ae.sizeBox.getValue()) && this.baseType.equals(ae.baseType);
      }
   }

   public int equivHashCode() {
      return this.sizeBox.getValue().equivHashCode() * 101 + this.baseType.hashCode() * 17;
   }

   public abstract Object clone();

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("newarray (" + this.getBaseTypeString() + ")");
      buffer.append("[" + this.sizeBox.getValue().toString() + "]");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("newarray");
      up.literal(" ");
      up.literal("(");
      up.type(this.baseType);
      up.literal(")");
      up.literal("[");
      this.sizeBox.toString(up);
      up.literal("]");
   }

   private String getBaseTypeString() {
      return this.baseType.toString();
   }

   public Type getBaseType() {
      return this.baseType;
   }

   public void setBaseType(Type type) {
      this.baseType = type;
   }

   public ValueBox getSizeBox() {
      return this.sizeBox;
   }

   public Value getSize() {
      return this.sizeBox.getValue();
   }

   public void setSize(Value size) {
      this.sizeBox.setValue(size);
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> useBoxes = new ArrayList();
      useBoxes.addAll(this.sizeBox.getValue().getUseBoxes());
      useBoxes.add(this.sizeBox);
      return useBoxes;
   }

   public Type getType() {
      return this.baseType instanceof ArrayType ? ArrayType.v(((ArrayType)this.baseType).baseType, ((ArrayType)this.baseType).numDimensions + 1) : ArrayType.v(this.baseType, 1);
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseNewArrayExpr(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)((ConvertToBaf)this.getSize())).convertToBaf(context, out);
      Unit u = Baf.v().newNewArrayInst(this.getBaseType());
      out.add(u);
      u.addAllTagsOf(context.getCurrentUnit());
   }
}
