package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.RefType;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.GrimpValueSwitch;
import soot.grimp.NewInvokeExpr;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.util.Switch;

public class GNewInvokeExpr extends AbstractInvokeExpr implements NewInvokeExpr, Precedence {
   RefType type;

   public GNewInvokeExpr(RefType type, SootMethodRef methodRef, List args) {
      super(methodRef, new ExprBox[args.size()]);
      if (methodRef != null && methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.methodRef = methodRef;
         this.type = type;

         for(int i = 0; i < args.size(); ++i) {
            this.argBoxes[i] = Grimp.v().newExprBox((Value)args.get(i));
         }

      }
   }

   public RefType getBaseType() {
      return this.type;
   }

   public void setBaseType(RefType type) {
      this.type = type;
   }

   public Type getType() {
      return this.type;
   }

   public int getPrecedence() {
      return 850;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("new " + this.type.toString() + "(");
      if (this.argBoxes != null) {
         for(int i = 0; i < this.argBoxes.length; ++i) {
            if (i != 0) {
               buffer.append(", ");
            }

            buffer.append(this.argBoxes[i].getValue().toString());
         }
      }

      buffer.append(")");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("new");
      up.literal(" ");
      up.type(this.type);
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
   }

   public void apply(Switch sw) {
      ((GrimpValueSwitch)sw).caseNewInvokeExpr(this);
   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      return new GNewInvokeExpr(this.getBaseType(), this.methodRef, clonedArgs);
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof GNewInvokeExpr)) {
         return false;
      } else {
         GNewInvokeExpr ie = (GNewInvokeExpr)o;
         if (this.getMethod().equals(ie.getMethod()) && (this.argBoxes == null ? 0 : this.argBoxes.length) == (ie.argBoxes == null ? 0 : ie.argBoxes.length)) {
            if (this.argBoxes != null) {
               ValueBox[] var3 = this.argBoxes;
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  ValueBox element = var3[var5];
                  if (!element.getValue().equivTo(element.getValue())) {
                     return false;
                  }
               }
            }

            return this.type.equals(ie.type);
         } else {
            return false;
         }
      }
   }

   public int equivHashCode() {
      return this.getMethod().equivHashCode();
   }
}
