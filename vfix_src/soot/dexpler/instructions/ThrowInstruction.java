package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.ThrowStmt;

public class ThrowInstruction extends DexlibAbstractInstruction {
   public ThrowInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      Instruction11x throwInstruction = (Instruction11x)this.instruction;
      ThrowStmt throwStmt = Jimple.v().newThrowStmt(body.getRegisterLocal(throwInstruction.getRegisterA()));
      this.setUnit(throwStmt);
      this.addTags(throwStmt);
      body.add(throwStmt);
   }
}
