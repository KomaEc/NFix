package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup2Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;

public class BDup2Inst extends BDupInst implements Dup2Inst {
   private final Type mOp1Type;
   private final Type mOp2Type;

   public BDup2Inst(Type aOp1Type, Type aOp2Type) {
      this.mOp1Type = Baf.getDescriptorTypeOf(aOp1Type);
      this.mOp2Type = Baf.getDescriptorTypeOf(aOp2Type);
   }

   public Type getOp1Type() {
      return this.mOp1Type;
   }

   public Type getOp2Type() {
      return this.mOp2Type;
   }

   public List<Type> getOpTypes() {
      List<Type> res = new ArrayList();
      res.add(this.mOp1Type);
      res.add(this.mOp2Type);
      return res;
   }

   public List<Type> getUnderTypes() {
      return new ArrayList();
   }

   public final String getName() {
      return "dup2";
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).caseDup2Inst(this);
   }

   public String toString() {
      return "dup2." + Baf.bafDescriptorOf(this.mOp1Type) + Baf.bafDescriptorOf(this.mOp2Type);
   }
}
