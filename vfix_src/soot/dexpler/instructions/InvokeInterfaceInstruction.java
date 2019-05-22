package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;

public class InvokeInterfaceInstruction extends MethodInvocationInstruction {
   public InvokeInterfaceInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      this.jimplifyInterface(body);
   }
}
