package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup1_x2Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BDup1_x2Inst extends BDupInst implements Dup1_x2Inst {
   private final Type mOpType;
   private final Type mUnder1Type;
   private final Type mUnder2Type;

   public BDup1_x2Inst(Type aOpType, Type aUnder1Type, Type aUnder2Type) {
      this.mOpType = Baf.getDescriptorTypeOf(aOpType);
      this.mUnder1Type = Baf.getDescriptorTypeOf(aUnder1Type);
      this.mUnder2Type = Baf.getDescriptorTypeOf(aUnder2Type);
   }

   public Type getOp1Type() {
      return this.mOpType;
   }

   public Type getUnder1Type() {
      return this.mUnder1Type;
   }

   public Type getUnder2Type() {
      return this.mUnder2Type;
   }

   public List<Type> getOpTypes() {
      List<Type> res = new ArrayList();
      res.add(this.mOpType);
      return res;
   }

   public List<Type> getUnderTypes() {
      List<Type> res = new ArrayList();
      res.add(this.mUnder1Type);
      res.add(this.mUnder2Type);
      return res;
   }

   public final String getName() {
      return "dup1_x2";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseDup1_x2Inst(this);
   }

   public String toString() {
      return "dup1_x2." + Baf.bafDescriptorOf(this.mOpType) + "_" + Baf.bafDescriptorOf(this.mUnder1Type) + "." + Baf.bafDescriptorOf(this.mUnder2Type);
   }
}
