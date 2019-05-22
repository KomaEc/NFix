package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNewMultiArrayExpr;

public class GNewMultiArrayExpr extends AbstractNewMultiArrayExpr {
   public GNewMultiArrayExpr(ArrayType type, List sizes) {
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

      return new GNewMultiArrayExpr(this.getBaseType(), clonedSizes);
   }
}
