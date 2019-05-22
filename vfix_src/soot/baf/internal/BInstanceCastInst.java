package soot.baf.internal;

import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.InstanceCastInst;
import soot.util.Switch;

public class BInstanceCastInst extends AbstractInst implements InstanceCastInst {
   protected Type castType;

   public BInstanceCastInst(Type opType) {
      if (!(opType instanceof RefType) && !(opType instanceof ArrayType)) {
         throw new RuntimeException("invalid InstanceCastInst: " + opType);
      } else {
         this.castType = opType;
      }
   }

   public int getInCount() {
      return 1;
   }

   public Object clone() {
      return new BInstanceCastInst(this.castType);
   }

   public int getInMachineCount() {
      return 1;
   }

   public int getOutCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 1;
   }

   public final String getName() {
      return "checkcast";
   }

   public Type getCastType() {
      return this.castType;
   }

   public void setCastType(Type t) {
      this.castType = t;
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseInstanceCastInst(this);
   }
}
