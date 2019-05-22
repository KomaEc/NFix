package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.internal.AbstractInvokeExpr;
import soot.util.Switch;

public class GDynamicInvokeExpr extends AbstractInvokeExpr implements DynamicInvokeExpr {
   protected ValueBox[] bsmArgBoxes;
   private SootMethodRef bsmRef;
   protected int tag;

   public GDynamicInvokeExpr(SootMethodRef bootStrapMethodRef, List<Value> bootstrapArgs, SootMethodRef methodRef, int tag, List args) {
      super(methodRef, new ValueBox[args.size()]);
      this.bsmRef = bootStrapMethodRef;
      this.tag = tag;

      int i;
      for(i = 0; i < args.size(); ++i) {
         this.argBoxes[i] = Grimp.v().newExprBox((Value)args.get(i));
      }

      for(i = 0; i < bootstrapArgs.size(); ++i) {
         this.bsmArgBoxes[i] = Grimp.v().newExprBox((Value)bootstrapArgs.get(i));
      }

   }

   public Object clone() {
      ArrayList clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, Grimp.cloneIfNecessary(this.getArg(i)));
      }

      ArrayList clonedBsmArgs = new ArrayList(this.getBootstrapArgCount());

      for(int i = 0; i < this.getBootstrapArgCount(); ++i) {
         clonedBsmArgs.add(i, this.getBootstrapArg(i));
      }

      return new GDynamicInvokeExpr(this.bsmRef, clonedBsmArgs, this.methodRef, this.tag, clonedArgs);
   }

   public Value getBootstrapArg(int i) {
      return this.bsmArgBoxes[i].getValue();
   }

   public int getBootstrapArgCount() {
      return this.bsmArgBoxes.length;
   }

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseDynamicInvokeExpr(this);
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof GDynamicInvokeExpr)) {
         return false;
      } else {
         GDynamicInvokeExpr ie = (GDynamicInvokeExpr)o;
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

            if (!this.methodRef.equals(ie.methodRef)) {
               return false;
            } else {
               return this.bsmRef.equals(ie.bsmRef);
            }
         } else {
            return false;
         }
      }
   }

   public int equivHashCode() {
      return this.getMethod().equivHashCode();
   }

   public SootMethodRef getBootstrapMethodRef() {
      return this.bsmRef;
   }

   public List<Value> getBootstrapArgs() {
      List l = new ArrayList();
      ValueBox[] var2 = this.bsmArgBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         l.add(element.getValue());
      }

      return l;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("dynamicinvoke");
      buffer.append(" \"");
      buffer.append(this.methodRef.name());
      buffer.append("\" <");
      buffer.append(SootMethod.getSubSignature("", this.methodRef.parameterTypes(), this.methodRef.returnType()));
      buffer.append(">(");
      int i;
      if (this.argBoxes != null) {
         for(i = 0; i < this.argBoxes.length; ++i) {
            if (i != 0) {
               buffer.append(", ");
            }

            buffer.append(this.argBoxes[i].getValue().toString());
         }
      }

      buffer.append(") ");
      buffer.append(this.bsmRef.getSignature());
      buffer.append("(");

      for(i = 0; i < this.bsmArgBoxes.length; ++i) {
         if (i != 0) {
            buffer.append(", ");
         }

         buffer.append(this.bsmArgBoxes[i].getValue().toString());
      }

      buffer.append(")");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("dynamicinvoke");
      up.literal(" \"" + this.methodRef.name() + "\" <" + SootMethod.getSubSignature("", this.methodRef.parameterTypes(), this.methodRef.returnType()) + ">(");
      int i;
      if (this.argBoxes != null) {
         for(i = 0; i < this.argBoxes.length; ++i) {
            if (i != 0) {
               up.literal(", ");
            }

            this.argBoxes[i].toString(up);
         }
      }

      up.literal(") ");
      up.methodRef(this.bsmRef);
      up.literal("(");

      for(i = 0; i < this.bsmArgBoxes.length; ++i) {
         if (i != 0) {
            up.literal(", ");
         }

         this.bsmArgBoxes[i].toString(up);
      }

      up.literal(")");
   }

   public int getHandleTag() {
      return this.tag;
   }
}
