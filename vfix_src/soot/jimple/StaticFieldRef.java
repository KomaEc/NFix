package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.Baf;
import soot.util.Switch;

public class StaticFieldRef implements FieldRef, ConvertToBaf {
   protected SootFieldRef fieldRef;

   protected StaticFieldRef(SootFieldRef fieldRef) {
      if (!fieldRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.fieldRef = fieldRef;
      }
   }

   public Object clone() {
      return new StaticFieldRef(this.fieldRef);
   }

   public String toString() {
      return this.fieldRef.getSignature();
   }

   public void toString(UnitPrinter up) {
      up.fieldRef(this.fieldRef);
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

   public List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Type getType() {
      return this.fieldRef.type();
   }

   public void apply(Switch sw) {
      ((RefSwitch)sw).caseStaticFieldRef(this);
   }

   public boolean equivTo(Object o) {
      return o instanceof StaticFieldRef ? ((StaticFieldRef)o).getField().equals(this.getField()) : false;
   }

   public int equivHashCode() {
      return this.getField().equivHashCode();
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newStaticGetInst(this.fieldRef);
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }
}
