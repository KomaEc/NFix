package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import soot.Local;
import soot.dexpler.DexBody;
import soot.dexpler.InvalidDalvikBytecodeException;
import soot.dexpler.tags.ObjectOpTag;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;

public class AgetInstruction extends DexlibAbstractInstruction {
   public AgetInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) throws InvalidDalvikBytecodeException {
      if (!(this.instruction instanceof Instruction23x)) {
         throw new IllegalArgumentException("Expected Instruction23x but got: " + this.instruction.getClass());
      } else {
         Instruction23x aGetInstr = (Instruction23x)this.instruction;
         int dest = aGetInstr.getRegisterA();
         Local arrayBase = body.getRegisterLocal(aGetInstr.getRegisterB());
         Local index = body.getRegisterLocal(aGetInstr.getRegisterC());
         ArrayRef arrayRef = Jimple.v().newArrayRef(arrayBase, index);
         Local l = body.getRegisterLocal(dest);
         AssignStmt assign = Jimple.v().newAssignStmt(l, arrayRef);
         if (aGetInstr.getOpcode() == Opcode.AGET_OBJECT) {
            assign.addTag(new ObjectOpTag());
         }

         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   boolean overridesRegister(int register) {
      OneRegisterInstruction i = (OneRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
