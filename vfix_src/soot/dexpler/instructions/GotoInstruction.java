package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;

public class GotoInstruction extends JumpInstruction implements DeferableInstruction {
   public GotoInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (this.getTargetInstruction(body).getUnit() != null) {
         body.add(this.gotoStatement());
      } else {
         body.addDeferredJimplification(this);
         this.markerUnit = Jimple.v().newNopStmt();
         this.addTags(this.markerUnit);
         this.unit = this.markerUnit;
         body.add(this.markerUnit);
      }
   }

   public void deferredJimplify(DexBody body) {
      body.getBody().getUnits().insertAfter((Unit)this.gotoStatement(), (Unit)this.markerUnit);
   }

   private GotoStmt gotoStatement() {
      GotoStmt go = Jimple.v().newGotoStmt(this.targetInstruction.getUnit());
      this.setUnit(go);
      this.addTags(go);
      return go;
   }
}
