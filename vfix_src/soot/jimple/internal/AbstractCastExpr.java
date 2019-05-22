package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.grimp.PrecedenceTest;
import soot.jimple.CastExpr;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.util.Switch;

public abstract class AbstractCastExpr implements CastExpr, ConvertToBaf {
   final ValueBox opBox;
   Type type;

   AbstractCastExpr(Value op, Type type) {
      this(Jimple.v().newImmediateBox(op), type);
   }

   public abstract Object clone();

   protected AbstractCastExpr(ValueBox opBox, Type type) {
      this.opBox = opBox;
      this.type = type;
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractCastExpr)) {
         return false;
      } else {
         AbstractCastExpr ace = (AbstractCastExpr)o;
         return this.opBox.getValue().equivTo(ace.opBox.getValue()) && this.type.equals(ace.type);
      }
   }

   public int equivHashCode() {
      return this.opBox.getValue().equivHashCode() * 101 + this.type.hashCode() + 17;
   }

   public String toString() {
      return "(" + this.type.toString() + ") " + this.opBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("(");
      up.type(this.type);
      up.literal(") ");
      if (PrecedenceTest.needsBrackets(this.opBox, this)) {
         up.literal("(");
      }

      this.opBox.toString(up);
      if (PrecedenceTest.needsBrackets(this.opBox, this)) {
         up.literal(")");
      }

   }

   public Value getOp() {
      return this.opBox.getValue();
   }

   public void setOp(Value op) {
      this.opBox.setValue(op);
   }

   public ValueBox getOpBox() {
      return this.opBox;
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      list.addAll(this.opBox.getValue().getUseBoxes());
      list.add(this.opBox);
      return list;
   }

   public Type getCastType() {
      return this.type;
   }

   public void setCastType(Type castType) {
      this.type = castType;
   }

   public Type getType() {
      return this.type;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseCastExpr(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Type toType = this.getCastType();
      Type fromType = this.getOp().getType();
      ((ConvertToBaf)this.getOp()).convertToBaf(context, out);
      Object u;
      if (!(toType instanceof ArrayType) && !(toType instanceof RefType)) {
         if (!fromType.equals(toType)) {
            u = Baf.v().newPrimitiveCastInst(fromType, toType);
         } else {
            u = Baf.v().newNopInst();
         }
      } else {
         u = Baf.v().newInstanceCastInst(toType);
      }

      out.add(u);
      ((Unit)u).addAllTagsOf(context.getCurrentUnit());
   }
}
