package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.Local;
import soot.dexpler.DexBody;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.Jimple;

public class MonitorEnterInstruction extends DexlibAbstractInstruction {
   public MonitorEnterInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      int reg = ((OneRegisterInstruction)this.instruction).getRegisterA();
      Local object = body.getRegisterLocal(reg);
      EnterMonitorStmt enterMonitorStmt = Jimple.v().newEnterMonitorStmt(object);
      this.setUnit(enterMonitorStmt);
      this.addTags(enterMonitorStmt);
      body.add(enterMonitorStmt);
   }
}
