package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup2_x1Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BDup2_x1Inst extends BDupInst implements Dup2_x1Inst {
   private final Type mOp1Type;
   private final Type mOp2Type;
   private final Type mUnderType;

   public BDup2_x1Inst(Type aOp1Type, Type aOp2Type, Type aUnderType) {
      this.mOp1Type = Baf.getDescriptorTypeOf(aOp1Type);
      this.mOp2Type = Baf.getDescriptorTypeOf(aOp2Type);
      this.mUnderType = Baf.getDescriptorTypeOf(aUnderType);
   }

   public Type getOp1Type() {
      return this.mOp1Type;
   }

   public Type getOp2Type() {
      return this.mOp2Type;
   }

   public Type getUnder1Type() {
      return this.mUnderType;
   }

   public List<Type> getOpTypes() {
      List<Type> res = new ArrayList();
      res.add(this.mOp1Type);
      res.add(this.mOp2Type);
      return res;
   }

   public List<Type> getUnderTypes() {
      List<Type> res = new ArrayList();
      res.add(this.mUnderType);
      return res;
   }

   public final String getName() {
      return "dup2_x1";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseDup2_x1Inst(this);
   }

   public String toString() {
      return "dup2_x1." + Baf.bafDescriptorOf(this.mOp1Type) + "." + Baf.bafDescriptorOf(this.mOp2Type) + "_" + Baf.bafDescriptorOf(this.mUnderType);
   }
}
