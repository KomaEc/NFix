package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22t;
import soot.Local;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.BinopExpr;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;

public class IfTestInstruction extends ConditionalJumpInstruction {
   public IfTestInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   protected IfStmt ifStatement(DexBody body) {
      Instruction22t i = (Instruction22t)this.instruction;
      Local one = body.getRegisterLocal(i.getRegisterA());
      Local other = body.getRegisterLocal(i.getRegisterB());
      BinopExpr condition = this.getComparisonExpr(one, other);
      IfStmt jif = Jimple.v().newIfStmt(condition, (Unit)this.targetInstruction.getUnit());
      this.addTags(jif);
      return jif;
   }
}
