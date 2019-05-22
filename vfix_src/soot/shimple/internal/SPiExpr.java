package soot.shimple.internal;

import java.util.Collections;
import java.util.List;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.shimple.PiExpr;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Switch;

public class SPiExpr implements PiExpr {
   protected ValueUnitPair argBox;
   protected Object targetKey;

   public SPiExpr(Value v, Unit u, Object o) {
      this.argBox = new SValueUnitPair(v, u);
      this.targetKey = o;
   }

   public ValueUnitPair getArgBox() {
      return this.argBox;
   }

   public Value getValue() {
      return this.argBox.getValue();
   }

   public Unit getCondStmt() {
      return this.argBox.getUnit();
   }

   public Object getTargetKey() {
      return this.targetKey;
   }

   public void setValue(Value value) {
      this.argBox.setValue(value);
   }

   public void setCondStmt(Unit pred) {
      this.argBox.setUnit(pred);
   }

   public void setTargetKey(Object targetKey) {
      this.targetKey = targetKey;
   }

   public List<UnitBox> getUnitBoxes() {
      return Collections.singletonList(this.argBox);
   }

   public void clearUnitBoxes() {
      System.out.println("clear unit boxes");
      this.argBox.setUnit((Unit)null);
   }

   public boolean equivTo(Object o) {
      return !(o instanceof SPiExpr) ? false : this.getArgBox().equivTo(((SPiExpr)o).getArgBox());
   }

   public int equivHashCode() {
      return this.getArgBox().equivHashCode() * 17;
   }

   public void apply(Switch sw) {
      throw new RuntimeException("Not Yet Implemented.");
   }

   public Object clone() {
      return new SPiExpr(this.getValue(), this.getCondStmt(), this.getTargetKey());
   }

   public String toString() {
      String s = "Pi(" + this.getValue() + ")";
      return s;
   }

   public void toString(UnitPrinter up) {
      up.literal("Pi");
      up.literal("(");
      this.argBox.toString(up);
      up.literal(" [");
      up.literal(this.targetKey.toString());
      up.literal("])");
   }

   public Type getType() {
      return this.getValue().getType();
   }

   public List getUseBoxes() {
      return Collections.singletonList(this.argBox);
   }
}
