package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.Local;
import soot.dexpler.DexBody;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.Jimple;

public class MonitorExitInstruction extends DexlibAbstractInstruction {
   public MonitorExitInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      int reg = ((OneRegisterInstruction)this.instruction).getRegisterA();
      Local object = body.getRegisterLocal(reg);
      ExitMonitorStmt exitMonitorStmt = Jimple.v().newExitMonitorStmt(object);
      this.setUnit(exitMonitorStmt);
      this.addTags(exitMonitorStmt);
      body.add(exitMonitorStmt);
   }
}
