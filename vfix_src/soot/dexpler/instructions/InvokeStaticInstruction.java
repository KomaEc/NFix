package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;

public class InvokeStaticInstruction extends MethodInvocationInstruction {
   public InvokeStaticInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      this.jimplifyStatic(body);
   }

   boolean isUsedAsFloatingPoint(DexBody body, int register) {
      return this.isUsedAsFloatingPoint(body, register, true);
   }
}
