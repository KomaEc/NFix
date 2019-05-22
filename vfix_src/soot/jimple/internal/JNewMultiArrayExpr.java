package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;

public class JNewMultiArrayExpr extends AbstractNewMultiArrayExpr {
   public JNewMultiArrayExpr(ArrayType type, List<? extends Value> sizes) {
      super(type, new ValueBox[sizes.size()]);

      for(int i = 0; i < sizes.size(); ++i) {
         this.sizeBoxes[i] = Jimple.v().newImmediateBox((Value)sizes.get(i));
      }

   }

   public Object clone() {
      List<Value> clonedSizes = new ArrayList(this.getSizeCount());

      for(int i = 0; i < this.getSizeCount(); ++i) {
         clonedSizes.add(i, Jimple.cloneIfNecessary(this.getSize(i)));
      }

      return new JNewMultiArrayExpr(this.baseType, clonedSizes);
   }
}
