package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction12x;
import soot.Local;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;

public class ArrayLengthInstruction extends DexlibAbstractInstruction {
   public ArrayLengthInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction12x)) {
         throw new IllegalArgumentException("Expected Instruction12x but got: " + this.instruction.getClass());
      } else {
         Instruction12x lengthOfArrayInstruction = (Instruction12x)this.instruction;
         int dest = lengthOfArrayInstruction.getRegisterA();
         Local arrayReference = body.getRegisterLocal(lengthOfArrayInstruction.getRegisterB());
         LengthExpr lengthExpr = Jimple.v().newLengthExpr(arrayReference);
         AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), lengthExpr);
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   boolean overridesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
