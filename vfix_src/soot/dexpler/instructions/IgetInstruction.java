package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;

public class IgetInstruction extends FieldInstruction {
   public IgetInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      int object = i.getRegisterB();
      FieldReference f = (FieldReference)((ReferenceInstruction)this.instruction).getReference();
      Jimple jimple = Jimple.v();
      InstanceFieldRef r = jimple.newInstanceFieldRef(body.getRegisterLocal(object), this.getSootFieldRef(f));
      AssignStmt assign = jimple.newAssignStmt(body.getRegisterLocal(dest), r);
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
   }

   boolean overridesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
