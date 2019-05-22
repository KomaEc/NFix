package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.Body;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;

public class MoveExceptionInstruction extends DexlibAbstractInstruction implements RetypeableInstruction {
   private Type realType;
   private IdentityStmt stmtToRetype;

   public MoveExceptionInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      int dest = ((OneRegisterInstruction)this.instruction).getRegisterA();
      Local l = body.getRegisterLocal(dest);
      this.stmtToRetype = Jimple.v().newIdentityStmt(l, Jimple.v().newCaughtExceptionRef());
      this.setUnit(this.stmtToRetype);
      this.addTags(this.stmtToRetype);
      body.add(this.stmtToRetype);
   }

   public void setRealType(DexBody body, Type t) {
      this.realType = t;
      body.addRetype(this);
   }

   public void retype(Body body) {
      if (this.realType == null) {
         throw new RuntimeException("Real type of this instruction has not been set or was already retyped: " + this);
      } else {
         if (body.getUnits().contains(this.stmtToRetype)) {
            Local l = (Local)((Local)this.stmtToRetype.getLeftOp());
            l.setType(this.realType);
            this.realType = null;
         }

      }
   }

   boolean overridesRegister(int register) {
      OneRegisterInstruction i = (OneRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
