package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.baf.Dup1Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BDup1Inst extends BDupInst implements Dup1Inst {
   private final Type mOpType;

   public BDup1Inst(Type aOpType) {
      this.mOpType = Baf.getDescriptorTypeOf(aOpType);
   }

   public Type getOp1Type() {
      return this.mOpType;
   }

   public List<Type> getOpTypes() {
      List<Type> res = new ArrayList();
      res.add(this.mOpType);
      return res;
   }

   public List<Type> getUnderTypes() {
      return new ArrayList();
   }

   public final String getName() {
      return "dup1";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseDup1Inst(this);
   }

   public String toString() {
      return "dup1." + Baf.bafDescriptorOf(this.mOpType);
   }

   public void toString(UnitPrinter up) {
      up.literal("dup1");
      up.literal(".");
      up.literal(Baf.bafDescriptorOf(this.mOpType));
   }
}
