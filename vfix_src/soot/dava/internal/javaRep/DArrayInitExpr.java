package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public class DArrayInitExpr implements Value {
   ValueBox[] elements;
   Type type;

   public DArrayInitExpr(ValueBox[] elements, Type type) {
      this.elements = elements;
      this.type = type;
   }

   public List<ValueBox> getUseBoxes() {
      List<ValueBox> list = new ArrayList();
      ValueBox[] var2 = this.elements;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         list.addAll(element.getValue().getUseBoxes());
         list.add(element);
      }

      return list;
   }

   public Object clone() {
      return this;
   }

   public Type getType() {
      return this.type;
   }

   public void toString(UnitPrinter up) {
      up.literal("{");

      for(int i = 0; i < this.elements.length; ++i) {
         this.elements[i].toString(up);
         if (i + 1 < this.elements.length) {
            up.literal(" , ");
         }
      }

      up.literal("}");
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append("{");

      for(int i = 0; i < this.elements.length; ++i) {
         b.append(this.elements[i].toString());
         if (i + 1 < this.elements.length) {
            b.append(" , ");
         }
      }

      b.append("}");
      return b.toString();
   }

   public void apply(Switch sw) {
   }

   public boolean equivTo(Object o) {
      return false;
   }

   public int equivHashCode() {
      int toReturn = 0;
      ValueBox[] var2 = this.elements;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         toReturn += element.getValue().equivHashCode();
      }

      return toReturn;
   }

   public ValueBox[] getElements() {
      return this.elements;
   }
}
