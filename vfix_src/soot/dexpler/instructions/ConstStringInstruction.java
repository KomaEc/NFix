package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.reference.StringReference;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;

public class ConstStringInstruction extends DexlibAbstractInstruction {
   public ConstStringInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      int dest = ((OneRegisterInstruction)this.instruction).getRegisterA();
      String s;
      if (this.instruction instanceof Instruction21c) {
         Instruction21c i = (Instruction21c)this.instruction;
         s = ((StringReference)((StringReference)i.getReference())).getString();
      } else {
         if (!(this.instruction instanceof Instruction31c)) {
            throw new IllegalArgumentException("Expected Instruction21c or Instruction31c but got neither.");
         }

         Instruction31c i = (Instruction31c)this.instruction;
         s = ((StringReference)((StringReference)i.getReference())).getString();
      }

      StringConstant sc = StringConstant.v(s);
      AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), sc);
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
   }

   boolean overridesRegister(int register) {
      OneRegisterInstruction i = (OneRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
