package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.Expr;
import soot.util.Switch;

public class DShortcutIf implements Expr {
   ValueBox testExprBox;
   ValueBox trueExprBox;
   ValueBox falseExprBox;
   Type exprType;

   public DShortcutIf(ValueBox test, ValueBox left, ValueBox right) {
      this.testExprBox = test;
      this.trueExprBox = left;
      this.falseExprBox = right;
   }

   public Object clone() {
      return this;
   }

   public List getUseBoxes() {
      List toReturn = new ArrayList();
      toReturn.addAll(this.testExprBox.getValue().getUseBoxes());
      toReturn.add(this.testExprBox);
      toReturn.addAll(this.trueExprBox.getValue().getUseBoxes());
      toReturn.add(this.trueExprBox);
      toReturn.addAll(this.falseExprBox.getValue().getUseBoxes());
      toReturn.add(this.falseExprBox);
      return toReturn;
   }

   public Type getType() {
      return this.exprType;
   }

   public String toString() {
      String toReturn = "";
      toReturn = toReturn + this.testExprBox.getValue().toString();
      toReturn = toReturn + " ? ";
      toReturn = toReturn + this.trueExprBox.getValue().toString();
      toReturn = toReturn + " : ";
      toReturn = toReturn + this.falseExprBox.getValue().toString();
      return toReturn;
   }

   public void toString(UnitPrinter up) {
      this.testExprBox.getValue().toString(up);
      up.literal(" ? ");
      this.trueExprBox.getValue().toString(up);
      up.literal(" : ");
      this.falseExprBox.getValue().toString(up);
   }

   public void apply(Switch sw) {
   }

   public boolean equivTo(Object o) {
      return false;
   }

   public int equivHashCode() {
      int toReturn = 0;
      int toReturn = toReturn + this.testExprBox.getValue().equivHashCode();
      toReturn += this.trueExprBox.getValue().equivHashCode();
      toReturn += this.falseExprBox.getValue().equivHashCode();
      return toReturn;
   }
}
