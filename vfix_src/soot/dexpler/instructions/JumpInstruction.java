package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import soot.Unit;
import soot.dexpler.DexBody;

public abstract class JumpInstruction extends DexlibAbstractInstruction {
   protected DexlibAbstractInstruction targetInstruction;
   protected Unit markerUnit;

   public JumpInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   protected DexlibAbstractInstruction getTargetInstruction(DexBody body) {
      int offset = ((OffsetInstruction)this.instruction).getCodeOffset();
      int targetAddress = this.codeAddress + offset;
      this.targetInstruction = body.instructionAtAddress(targetAddress);
      return this.targetInstruction;
   }
}
