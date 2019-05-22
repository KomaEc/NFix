package soot.baf.internal;

import java.util.List;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.Value;
import soot.VoidType;
import soot.baf.DynamicInvokeInst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BDynamicInvokeInst extends AbstractInvokeInst implements DynamicInvokeInst {
   protected final SootMethodRef bsmRef;
   private final List<Value> bsmArgs;
   protected int tag;

   public BDynamicInvokeInst(SootMethodRef bsmMethodRef, List<Value> bsmArgs, SootMethodRef methodRef, int tag) {
      this.bsmRef = bsmMethodRef;
      this.bsmArgs = bsmArgs;
      this.methodRef = methodRef;
      this.tag = tag;
   }

   public int getInCount() {
      return this.methodRef.parameterTypes().size();
   }

   public Object clone() {
      return new BDynamicInvokeInst(this.bsmRef, this.bsmArgs, this.methodRef, this.tag);
   }

   public int getOutCount() {
      return this.methodRef.returnType() instanceof VoidType ? 0 : 1;
   }

   public SootMethodRef getBootstrapMethodRef() {
      return this.bsmRef;
   }

   public List<Value> getBootstrapArgs() {
      return this.bsmArgs;
   }

   public String getName() {
      return "dynamicinvoke";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseDynamicInvokeInst(this);
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("dynamicinvoke");
      buffer.append(" \"");
      buffer.append(this.methodRef.name());
      buffer.append("\" <");
      buffer.append(SootMethod.getSubSignature("", this.methodRef.parameterTypes(), this.methodRef.returnType()));
      buffer.append(">");
      buffer.append(this.bsmRef.getSignature());
      buffer.append("(");

      for(int i = 0; i < this.bsmArgs.size(); ++i) {
         if (i != 0) {
            buffer.append(", ");
         }

         buffer.append(((Value)this.bsmArgs.get(i)).toString());
      }

      buffer.append(")");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("dynamicinvoke");
      up.literal(" \"" + this.methodRef.name() + "\" <" + SootMethod.getSubSignature("", this.methodRef.parameterTypes(), this.methodRef.returnType()) + "> ");
      up.methodRef(this.bsmRef);
      up.literal("(");

      for(int i = 0; i < this.bsmArgs.size(); ++i) {
         if (i != 0) {
            up.literal(", ");
         }

         ((Value)this.bsmArgs.get(i)).toString(up);
      }

      up.literal(")");
   }

   public int getHandleTag() {
      return this.tag;
   }
}
