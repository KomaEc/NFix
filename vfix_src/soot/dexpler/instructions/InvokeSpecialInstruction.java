package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;

public class InvokeSpecialInstruction extends MethodInvocationInstruction {
   public InvokeSpecialInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      this.jimplifySpecial(body);
   }
}
