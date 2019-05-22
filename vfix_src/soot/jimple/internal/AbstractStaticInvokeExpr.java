package soot.jimple.internal;

import java.util.List;
import soot.SootMethodRef;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StaticInvokeExpr;
import soot.util.Switch;

public abstract class AbstractStaticInvokeExpr extends AbstractInvokeExpr implements StaticInvokeExpr, ConvertToBaf {
   AbstractStaticInvokeExpr(SootMethodRef methodRef, List<Value> args) {
      this(methodRef, new ValueBox[args.size()]);

      for(int i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Jimple.v().newImmediateBox((Value)args.get(i));
      }

   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractStaticInvokeExpr)) {
         return false;
      } else {
         AbstractStaticInvokeExpr ie = (AbstractStaticInvokeExpr)o;
         if (this.getMethod().equals(ie.getMethod()) && (this.argBoxes == null ? 0 : this.argBoxes.length) == (ie.argBoxes == null ? 0 : ie.argBoxes.length)) {
            if (this.argBoxes != null) {
               for(int i = 0; i < this.argBoxes.length; ++i) {
                  if (!this.argBoxes[i].getValue().equivTo(ie.argBoxes[i].getValue())) {
                     return false;
                  }
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public int equivHashCode() {
      return this.getMethod().equivHashCode();
   }

   public abstract Object clone();

   protected AbstractStaticInvokeExpr(SootMethodRef methodRef, ValueBox[] argBoxes) {
      super(methodRef, argBoxes);
      if (!methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      } else {
         this.methodRef = methodRef;
      }
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("staticinvoke " + this.methodRef.getSignature() + "(");
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
      up.literal("staticinvoke");
      up.literal(" ");
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
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseStaticInvokeExpr(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      if (this.argBoxes != null) {
         ValueBox[] var3 = this.argBoxes;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ValueBox element = var3[var5];
            ((ConvertToBaf)((ConvertToBaf)element.getValue())).convertToBaf(context, out);
         }
      }

      Unit u = Baf.v().newStaticInvokeInst(this.methodRef);
      out.add(u);
      u.addAllTagsOf(context.getCurrentUnit());
   }
}
