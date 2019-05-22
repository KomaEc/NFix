package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.FieldGetInst;
import soot.grimp.PrecedenceTest;
import soot.jimple.ConvertToBaf;
import soot.jimple.InstanceFieldRef;
import soot.jimple.JimpleToBafContext;
import soot.jimple.RefSwitch;
import soot.util.Switch;

public abstract class AbstractInstanceFieldRef implements InstanceFieldRef, ConvertToBaf {
   protected SootFieldRef fieldRef;
   final ValueBox baseBox;

   protected AbstractInstanceFieldRef(ValueBox baseBox, SootFieldRef fieldRef) {
      if (fieldRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.baseBox = baseBox;
         this.fieldRef = fieldRef;
      }
   }

   public abstract Object clone();

   public String toString() {
      return this.baseBox.getValue().toString() + "." + this.fieldRef.getSignature();
   }

   public void toString(UnitPrinter up) {
      if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
         up.literal("(");
      }

      this.baseBox.toString(up);
      if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
         up.literal(")");
      }

      up.literal(".");
      up.fieldRef(this.fieldRef);
   }

   public Value getBase() {
      return this.baseBox.getValue();
   }

   public ValueBox getBaseBox() {
      return this.baseBox;
   }

   public void setBase(Value base) {
      this.baseBox.setValue(base);
   }

   public SootFieldRef getFieldRef() {
      return this.fieldRef;
   }

   public void setFieldRef(SootFieldRef fieldRef) {
      this.fieldRef = fieldRef;
   }

   public SootField getField() {
      return this.fieldRef.resolve();
   }

   public final List<ValueBox> getUseBoxes() {
      List<ValueBox> useBoxes = new ArrayList();
      useBoxes.addAll(this.baseBox.getValue().getUseBoxes());
      useBoxes.add(this.baseBox);
      return useBoxes;
   }

   public Type getType() {
      return this.fieldRef.type();
   }

   public void apply(Switch sw) {
      ((RefSwitch)sw).caseInstanceFieldRef(this);
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractInstanceFieldRef)) {
         return false;
      } else {
         AbstractInstanceFieldRef fr = (AbstractInstanceFieldRef)o;
         return fr.getField().equals(this.getField()) && fr.baseBox.getValue().equivTo(this.baseBox.getValue());
      }
   }

   public int equivHashCode() {
      return this.getField().equivHashCode() * 101 + this.baseBox.getValue().equivHashCode() + 17;
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)this.getBase()).convertToBaf(context, out);
      FieldGetInst u;
      out.add(u = Baf.v().newFieldGetInst(this.fieldRef));
      u.addAllTagsOf(context.getCurrentUnit());
   }
}
