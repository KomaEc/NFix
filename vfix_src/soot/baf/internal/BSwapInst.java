package soot.baf.internal;

import soot.DoubleType;
import soot.LongType;
import soot.Type;
import soot.baf.Baf;
import soot.baf.InstSwitch;
import soot.baf.SwapInst;
import soot.util.Switch;

public class BSwapInst extends AbstractInst implements SwapInst {
   protected Type mFromType;
   protected Type mToType;

   public BSwapInst(Type fromType, Type toType) {
      if (!(fromType instanceof LongType) && !(fromType instanceof DoubleType)) {
         if (!(toType instanceof LongType) && !(toType instanceof DoubleType)) {
            this.mFromType = Baf.getDescriptorTypeOf(fromType);
            this.mToType = Baf.getDescriptorTypeOf(toType);
         } else {
            throw new RuntimeException("toType is LongType or DoubleType !");
         }
      } else {
         throw new RuntimeException("fromType is LongType or DoubleType !");
      }
   }

   public Type getFromType() {
      return this.mFromType;
   }

   public void setFromType(Type fromType) {
      this.mFromType = fromType;
   }

   public Type getToType() {
      return this.mToType;
   }

   public void setToType(Type toType) {
      this.mToType = toType;
   }

   public int getInCount() {
      return 2;
   }

   public int getInMachineCount() {
      return 2;
   }

   public int getOutCount() {
      return 2;
   }

   public int getOutMachineCount() {
      return 2;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseSwapInst(this);
   }

   public String toString() {
      return "swap." + Baf.bafDescriptorOf(this.mFromType) + Baf.bafDescriptorOf(this.mToType);
   }

   public String getName() {
      return "swap";
   }
}
