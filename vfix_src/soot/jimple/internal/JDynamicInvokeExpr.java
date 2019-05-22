package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.ExprSwitch;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.util.Switch;

public class JDynamicInvokeExpr extends AbstractInvokeExpr implements DynamicInvokeExpr, ConvertToBaf {
   protected SootMethodRef bsmRef;
   protected ValueBox[] bsmArgBoxes;
   protected int tag;

   public JDynamicInvokeExpr(SootMethodRef bootstrapMethodRef, List<? extends Value> bootstrapArgs, SootMethodRef methodRef, int tag, List<? extends Value> methodArgs) {
      super(methodRef, new ValueBox[methodArgs.size()]);
      if (!methodRef.getSignature().startsWith("<soot.dummy.InvokeDynamic: ")) {
         throw new IllegalArgumentException("Receiver type of JDynamicInvokeExpr must be soot.dummy.InvokeDynamic!");
      } else {
         this.bsmRef = bootstrapMethodRef;
         this.bsmArgBoxes = new ValueBox[bootstrapArgs.size()];
         this.tag = tag;

         int i;
         for(i = 0; i < bootstrapArgs.size(); ++i) {
            this.bsmArgBoxes[i] = Jimple.v().newImmediateBox((Value)bootstrapArgs.get(i));
         }

         for(i = 0; i < methodArgs.size(); ++i) {
            this.argBoxes[i] = Jimple.v().newImmediateBox((Value)methodArgs.get(i));
         }

      }
   }

   public JDynamicInvokeExpr(SootMethodRef bootstrapMethodRef, List<? extends Value> bootstrapArgs, SootMethodRef methodRef, List<? extends Value> methodArgs) {
      this(bootstrapMethodRef, bootstrapArgs, methodRef, 6, methodArgs);
   }

   public int getBootstrapArgCount() {
      return this.bsmArgBoxes.length;
   }

   public Value getBootstrapArg(int index) {
      return this.bsmArgBoxes[index].getValue();
   }

   public Object clone() {
      List<Value> clonedBsmArgs = new ArrayList(this.getBootstrapArgCount());

      for(int i = 0; i < this.getBootstrapArgCount(); ++i) {
         clonedBsmArgs.add(i, this.getBootstrapArg(i));
      }

      List<Value> clonedArgs = new ArrayList(this.getArgCount());

      for(int i = 0; i < this.getArgCount(); ++i) {
         clonedArgs.add(i, this.getArg(i));
      }

      return new JDynamicInvokeExpr(this.bsmRef, clonedBsmArgs, this.methodRef, this.tag, clonedArgs);
   }

   public boolean equivTo(Object o) {
      if (!(o instanceof JDynamicInvokeExpr)) {
         return false;
      } else {
         JDynamicInvokeExpr ie = (JDynamicInvokeExpr)o;
         if (this.getMethod().equals(ie.getMethod()) && this.bsmArgBoxes.length == ie.bsmArgBoxes.length) {
            int i = 0;
            ValueBox[] var4 = this.bsmArgBoxes;
            int var5 = var4.length;

            int var6;
            ValueBox element;
            for(var6 = 0; var6 < var5; ++var6) {
               element = var4[var6];
               if (!element.getValue().equivTo(ie.getBootstrapArg(i))) {
                  return false;
               }

               ++i;
            }

            if (this.getMethod().equals(ie.getMethod()) && (this.argBoxes == null ? 0 : this.argBoxes.length) == (ie.argBoxes == null ? 0 : ie.argBoxes.length)) {
               if (this.argBoxes != null) {
                  i = 0;
                  var4 = this.argBoxes;
                  var5 = var4.length;

                  for(var6 = 0; var6 < var5; ++var6) {
                     element = var4[var6];
                     if (!element.getValue().equivTo(ie.getArg(i))) {
                        return false;
                     }

                     ++i;
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
         } else {
            return false;
         }
      }
   }

   public SootMethod getBootstrapMethod() {
      return this.bsmRef.resolve();
   }

   public int equivHashCode() {
      return this.getBootstrapMethod().equivHashCode() * this.getMethod().equivHashCode() * 17;
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

   public void apply(Switch sw) {
      ((ExprSwitch)sw).caseDynamicInvokeExpr(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      int var5;
      if (this.argBoxes != null) {
         ValueBox[] var3 = this.argBoxes;
         int var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            ValueBox element = var3[var5];
            ((ConvertToBaf)((ConvertToBaf)element.getValue())).convertToBaf(context, out);
         }
      }

      List<Value> bsmArgs = new ArrayList();
      ValueBox[] var9 = this.bsmArgBoxes;
      var5 = var9.length;

      for(int var11 = 0; var11 < var5; ++var11) {
         ValueBox argBox = var9[var11];
         bsmArgs.add(argBox.getValue());
      }

      Unit u = Baf.v().newDynamicInvokeInst(this.bsmRef, bsmArgs, this.methodRef, this.tag);
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }

   public SootMethodRef getBootstrapMethodRef() {
      return this.bsmRef;
   }

   public List<Value> getBootstrapArgs() {
      List<Value> l = new ArrayList();
      ValueBox[] var2 = this.bsmArgBoxes;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ValueBox element = var2[var4];
         l.add(element.getValue());
      }

      return l;
   }

   public int getHandleTag() {
      return this.tag;
   }
}
