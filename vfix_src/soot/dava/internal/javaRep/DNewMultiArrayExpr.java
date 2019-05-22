package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNewMultiArrayExpr;

public class DNewMultiArrayExpr extends AbstractNewMultiArrayExpr {
   public DNewMultiArrayExpr(ArrayType type, List sizes) {
      super(type, new ValueBox[sizes.size()]);

      for(int i = 0; i < sizes.size(); ++i) {
         this.sizeBoxes[i] = Grimp.v().newExprBox((Value)sizes.get(i));
      }

   }

   public Object clone() {
      List clonedSizes = new ArrayList(this.getSizeCount());

      for(int i = 0; i < this.getSizeCount(); ++i) {
         clonedSizes.add(i, Grimp.cloneIfNecessary(this.getSize(i)));
      }

      return new DNewMultiArrayExpr(this.getBaseType(), clonedSizes);
   }

   public void toString(UnitPrinter up) {
      up.literal("new");
      up.literal(" ");
      up.type(this.getBaseType().baseType);
      ValueBox[] var2 = this.sizeBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         up.literal("[");
         element.toString(up);
         up.literal("]");
      }

      for(int i = this.getSizeCount(); i < this.getBaseType().numDimensions; ++i) {
         up.literal("[]");
      }

   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("new " + this.getBaseType().baseType);
      List sizes = this.getSizes();
      Iterator it = this.getSizes().iterator();

      while(it.hasNext()) {
         buffer.append("[" + it.next().toString() + "]");
      }

      for(int i = this.getSizeCount(); i < this.getBaseType().numDimensions; ++i) {
         buffer.append("[]");
      }

      return buffer.toString();
   }
}
