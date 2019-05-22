package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import soot.Local;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;

public class SparseSwitchInstruction extends SwitchInstruction {
   public SparseSwitchInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   protected Stmt switchStatement(DexBody body, Instruction targetData, Local key) {
      SparseSwitchPayload i = (SparseSwitchPayload)targetData;
      List<? extends SwitchElement> seList = i.getSwitchElements();
      int defaultTargetAddress = this.codeAddress + this.instruction.getCodeUnits();
      Unit defaultTarget = body.instructionAtAddress(defaultTargetAddress).getUnit();
      List<IntConstant> lookupValues = new ArrayList();
      List<Unit> targets = new ArrayList();
      Iterator var10 = seList.iterator();

      while(var10.hasNext()) {
         SwitchElement se = (SwitchElement)var10.next();
         lookupValues.add(IntConstant.v(se.getKey()));
         int offset = se.getOffset();
         targets.add(body.instructionAtAddress(this.codeAddress + offset).getUnit());
      }

      LookupSwitchStmt switchStmt = Jimple.v().newLookupSwitchStmt(key, lookupValues, targets, (Unit)defaultTarget);
      this.setUnit(switchStmt);
      this.addTags(switchStmt);
      return switchStmt;
   }

   public void computeDataOffsets(DexBody body) {
   }
}
