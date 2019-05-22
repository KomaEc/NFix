package soot.baf.internal;

import java.util.Iterator;
import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.DupInst;
import soot.util.Switch;

public abstract class BDupInst extends AbstractInst implements DupInst {
   public int getInCount() {
      return this.getUnderTypes().size() + this.getOpTypes().size();
   }

   public int getInMachineCount() {
      int count = 0;

      for(Iterator underTypesIt = this.getUnderTypes().iterator(); underTypesIt.hasNext(); count += AbstractJasminClass.sizeOfType((Type)underTypesIt.next())) {
      }

      for(Iterator opTypesIt = this.getOpTypes().iterator(); opTypesIt.hasNext(); count += AbstractJasminClass.sizeOfType((Type)opTypesIt.next())) {
      }

      return count;
   }

   public int getOutCount() {
      return this.getUnderTypes().size() + 2 * this.getOpTypes().size();
   }

   public int getOutMachineCount() {
      int count = 0;

      for(Iterator underTypesIt = this.getUnderTypes().iterator(); underTypesIt.hasNext(); count += AbstractJasminClass.sizeOfType((Type)underTypesIt.next())) {
      }

      for(Iterator opTypesIt = this.getOpTypes().iterator(); opTypesIt.hasNext(); count += 2 * AbstractJasminClass.sizeOfType((Type)opTypesIt.next())) {
      }

      return count;
   }

   public void apply(Switch sw) {
      throw new RuntimeException();
   }
}
