package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Local;
import soot.NullType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.baf.ArrayReadInst;
import soot.baf.Baf;
import soot.jimple.ArrayRef;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.RefSwitch;
import soot.tagkit.Tag;
import soot.util.Switch;

public class JArrayRef implements ArrayRef, ConvertToBaf {
   protected ValueBox baseBox;
   protected ValueBox indexBox;

   public JArrayRef(Value base, Value index) {
      this(Jimple.v().newLocalBox(base), Jimple.v().newImmediateBox(index));
   }

   protected JArrayRef(ValueBox baseBox, ValueBox indexBox) {
      this.baseBox = baseBox;
      this.indexBox = indexBox;
   }

   public Object clone() {
      return new JArrayRef(Jimple.cloneIfNecessary(this.getBase()), Jimple.cloneIfNecessary(this.getIndex()));
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof ArrayRef)) {
         return false;
      } else {
         return this.getBase().equivTo(((ArrayRef)o).getBase()) && this.getIndex().equivTo(((ArrayRef)o).getIndex());
      }
   }

   public int equivHashCode() {
      return this.getBase().equivHashCode() * 101 + this.getIndex().equivHashCode() + 17;
   }

   public String toString() {
      return this.baseBox.getValue().toString() + "[" + this.indexBox.getValue().toString() + "]";
   }

   public void toString(UnitPrinter up) {
      this.baseBox.toString(up);
      up.literal("[");
      this.indexBox.toString(up);
      up.literal("]");
   }

   public Value getBase() {
      return this.baseBox.getValue();
   }

   public void setBase(Local base) {
      this.baseBox.setValue(base);
   }

   public ValueBox getBaseBox() {
      return this.baseBox;
   }

   public Value getIndex() {
      return this.indexBox.getValue();
   }

   public void setIndex(Value index) {
      this.indexBox.setValue(index);
   }

   public ValueBox getIndexBox() {
      return this.indexBox;
   }

   public List getUseBoxes() {
      List useBoxes = new ArrayList();
      useBoxes.addAll(this.baseBox.getValue().getUseBoxes());
      useBoxes.add(this.baseBox);
      useBoxes.addAll(this.indexBox.getValue().getUseBoxes());
      useBoxes.add(this.indexBox);
      return useBoxes;
   }

   public Type getType() {
      Value base = this.baseBox.getValue();
      Type type = base.getType();
      if (type.equals(UnknownType.v())) {
         return UnknownType.v();
      } else if (type.equals(NullType.v())) {
         return NullType.v();
      } else {
         ArrayType arrayType;
         if (type instanceof ArrayType) {
            arrayType = (ArrayType)type;
         } else {
            arrayType = type.makeArrayType();
         }

         return (Type)(arrayType.numDimensions == 1 ? arrayType.baseType : ArrayType.v(arrayType.baseType, arrayType.numDimensions - 1));
      }
   }

   public void apply(Switch sw) {
      ((RefSwitch)sw).caseArrayRef(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)this.getBase()).convertToBaf(context, out);
      ((ConvertToBaf)this.getIndex()).convertToBaf(context, out);
      Unit currentUnit = context.getCurrentUnit();
      ArrayReadInst x;
      out.add(x = Baf.v().newArrayReadInst(this.getType()));
      Iterator it = currentUnit.getTags().iterator();

      while(it.hasNext()) {
         x.addTag((Tag)it.next());
      }

   }
}
