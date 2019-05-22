package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import soot.Local;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;

public class ReturnInstruction extends DexlibAbstractInstruction {
   public ReturnInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      Instruction11x returnInstruction = (Instruction11x)this.instruction;
      Local l = body.getRegisterLocal(returnInstruction.getRegisterA());
      ReturnStmt returnStmt = Jimple.v().newReturnStmt(l);
      this.setUnit(returnStmt);
      this.addTags(returnStmt);
      body.add(returnStmt);
   }
}
