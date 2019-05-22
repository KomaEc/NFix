package soot.dava.internal.javaRep;

import soot.ArrayType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractNewArrayExpr;

public class DNewArrayExpr extends AbstractNewArrayExpr implements Precedence {
   public DNewArrayExpr(Type type, Value size) {
      super(type, Grimp.v().newExprBox(size));
   }

   public int getPrecedence() {
      return 850;
   }

   public Object clone() {
      return new DNewArrayExpr(this.getBaseType(), Grimp.cloneIfNecessary(this.getSize()));
   }

   public void toString(UnitPrinter up) {
      up.literal("new");
      up.literal(" ");
      Type type = this.getBaseType();
      if (type instanceof ArrayType) {
         ArrayType arrayType = (ArrayType)type;
         up.type(arrayType.baseType);
         up.literal("[");
         this.getSizeBox().toString(up);
         up.literal("]");

         for(int i = 0; i < arrayType.numDimensions; ++i) {
            up.literal("[]");
         }
      } else {
         up.type(this.getBaseType());
         up.literal("[");
         this.getSizeBox().toString(up);
         up.literal("]");
      }

   }

   public String toString() {
      return "new " + this.getBaseType() + "[" + this.getSize() + "]";
   }
}
