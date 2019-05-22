package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.PopInst;
import soot.util.Switch;

public class BPopInst extends AbstractInst implements PopInst {
   protected Type mType;

   public BPopInst(Type aType) {
      this.mType = aType;
   }

   public int getWordCount() {
      return this.getInMachineCount();
   }

   public void setWordCount(int count) {
      throw new RuntimeException("not implemented");
   }

   public Object clone() {
      return new BPopInst(this.mType);
   }

   public final String getName() {
      return "pop";
   }

   final String getParameters() {
      return "";
   }

   public int getInCount() {
      return 1;
   }

   public int getOutMachineCount() {
      return 0;
   }

   public int getOutCount() {
      return 0;
   }

   public int getInMachineCount() {
      return AbstractJasminClass.sizeOfType(this.mType);
   }

   public void apply(Switch sw) {
      ((InstSwitch)sw).casePopInst(this);
   }

   public Type getType() {
      return this.mType;
   }
}
