package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;

public class MoveInstruction extends DexlibAbstractInstruction {
   public MoveInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      int source = i.getRegisterB();
      AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), body.getRegisterLocal(source));
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
   }

   int movesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      int source = i.getRegisterB();
      return register == source ? dest : -1;
   }

   int movesToRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      int source = i.getRegisterB();
      return register == dest ? source : -1;
   }

   boolean overridesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
