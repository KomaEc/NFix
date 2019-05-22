package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.BinopExpr;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;

public class IfTestzInstruction extends ConditionalJumpInstruction {
   public IfTestzInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   protected IfStmt ifStatement(DexBody body) {
      Instruction21t i = (Instruction21t)this.instruction;
      BinopExpr condition = this.getComparisonExpr(body, i.getRegisterA());
      IfStmt jif = Jimple.v().newIfStmt(condition, (Unit)this.targetInstruction.getUnit());
      this.addTags(jif);
      return jif;
   }
}
