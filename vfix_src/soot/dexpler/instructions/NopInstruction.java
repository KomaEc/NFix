package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;

public class NopInstruction extends DexlibAbstractInstruction {
   public NopInstruction(Instruction instruction, int codeAddress) {
      super(instruction, codeAddress);
   }

   public void jimplify(DexBody body) {
      NopStmt nop = Jimple.v().newNopStmt();
      this.setUnit(nop);
      this.addTags(nop);
      body.add(nop);
   }
}
