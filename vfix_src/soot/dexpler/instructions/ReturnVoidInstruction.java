package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;

public class ReturnVoidInstruction extends DexlibAbstractInstruction {
   public ReturnVoidInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      ReturnVoidStmt returnStmt = Jimple.v().newReturnVoidStmt();
      this.setUnit(returnStmt);
      this.addTags(returnStmt);
      body.add(returnStmt);
   }
}
