package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.NullType;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.grimp.PrecedenceTest;
import soot.grimp.internal.GInterfaceInvokeExpr;

public class DInterfaceInvokeExpr extends GInterfaceInvokeExpr {
   public DInterfaceInvokeExpr(Value base, SootMethodRef methodRef, List args) {
      super(base, methodRef, args);
   }

   public void toString(UnitPrinter up) {
      if (this.getBase().getType() instanceof NullType) {
         up.literal("((");
         up.type(this.getMethodRef().declaringClass().getType());
         up.literal(") ");
         if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
            up.literal("(");
         }

         this.baseBox.toString(up);
         if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
            up.literal(")");
         }

         up.literal(")");
         up.literal(".");
         up.methodRef(this.methodRef);
         up.literal("(");
         if (this.argBoxes != null) {
            for(int i = 0; i < this.argBoxes.length; ++i) {
               if (i != 0) {
                  up.literal(", ");
               }

               this.argBoxes[i].toString(up);
            }
         }

         up.literal(")");
      } else {
         super.toString(up);
      }

   }

   public String toString() {
      if (this.getBase().getType() instanceof NullType) {
         StringBuffer b = new StringBuffer();
         b.append("((");
         b.append(this.getMethodRef().declaringClass().getJavaStyleName());
         b.append(") ");
         String baseStr = this.getBase().toString();
         if (this.getBase() instanceof Precedence && ((Precedence)this.getBase()).getPrecedence() < this.getPrecedence()) {
            baseStr = "(" + baseStr + ")";
         }

         b.append(baseStr);
         b.append(").");
         b.append(this.getMethodRef().name());
         b.append("(");
         if (this.argBoxes != null) {
            for(int i = 0; i < this.argBoxes.length; ++i) {
               if (i != 0) {
                  b.append(", ");
               }

               b.append(this.argBoxes[i].getValue().toString());
            }
         }

         b.append(")");
         return b.toString();
      } else {
         return super.toString();
      }
   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      return new DInterfaceInvokeExpr(this.getBase(), this.methodRef, clonedArgs);
   }
}
