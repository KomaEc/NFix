package soot.jimple.internal;

import java.util.Iterator;
import java.util.List;
import soot.DoubleType;
import soot.LongType;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.VoidType;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.JimpleToBafContext;
import soot.util.Switch;

public abstract class AbstractInterfaceInvokeExpr extends AbstractInstanceInvokeExpr implements InterfaceInvokeExpr, ConvertToBaf {
   protected AbstractInterfaceInvokeExpr(ValueBox baseBox, SootMethodRef methodRef, ValueBox[] argBoxes) {
      super(methodRef, baseBox, argBoxes);
      if (methodRef.isStatic()) {
         throw new RuntimeException("wrong static-ness");
      }
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof AbstractInterfaceInvokeExpr)) {
         return false;
      } else {
         AbstractInterfaceInvokeExpr ie = (AbstractInterfaceInvokeExpr)o;
         if (this.baseBox.getValue().equivTo(ie.baseBox.getValue()) && this.getMethod().equals(ie.getMethod()) && (this.argBoxes == null ? 0 : this.argBoxes.length) == (ie.argBoxes == null ? 0 : ie.argBoxes.length)) {
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
      return this.baseBox.getValue().equivHashCode() * 101 + this.getMethod().equivHashCode() * 17;
   }

   public abstract Object clone();

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("interfaceinvoke " + this.baseBox.getValue().toString() + "." + this.methodRef.getSignature() + "(");
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
      up.literal("interfaceinvoke");
      up.literal(" ");
      this.baseBox.toString(up);
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
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseInterfaceInvokeExpr(this);
   }

   private static int sizeOfType(Type t) {
      if (!(t instanceof DoubleType) && !(t instanceof LongType)) {
         return t instanceof VoidType ? 0 : 1;
      } else {
         return 2;
      }
   }

   private static int argCountOf(SootMethodRef m) {
      int argCount = 0;

      Type t;
      for(Iterator var2 = m.parameterTypes().iterator(); var2.hasNext(); argCount += sizeOfType(t)) {
         t = (Type)var2.next();
      }

      return argCount;
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      ((ConvertToBaf)this.getBase()).convertToBaf(context, out);
      if (this.argBoxes != null) {
         ValueBox[] var3 = this.argBoxes;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ValueBox element = var3[var5];
            ((ConvertToBaf)((ConvertToBaf)element.getValue())).convertToBaf(context, out);
         }
      }

      Unit u = Baf.v().newInterfaceInvokeInst(this.methodRef, argCountOf(this.methodRef));
      out.add(u);
      u.addAllTagsOf(context.getCurrentUnit());
   }
}
