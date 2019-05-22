package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import soot.Immediate;
import soot.Local;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

public abstract class ConditionalJumpInstruction extends JumpInstruction implements DeferableInstruction {
   public ConditionalJumpInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   protected abstract IfStmt ifStatement(DexBody var1);

   public void jimplify(DexBody body) {
      if (this.getTargetInstruction(body).getUnit() != null) {
         IfStmt s = this.ifStatement(body);
         body.add(s);
         this.setUnit(s);
      } else {
         body.addDeferredJimplification(this);
         this.markerUnit = Jimple.v().newNopStmt();
         this.unit = this.markerUnit;
         body.add(this.markerUnit);
      }

   }

   public void deferredJimplify(DexBody body) {
      IfStmt s = this.ifStatement(body);
      body.getBody().getUnits().swapWith((Unit)this.markerUnit, (Unit)s);
      this.setUnit(s);
   }

   protected ConditionExpr getComparisonExpr(DexBody body, int reg) {
      Local one = body.getRegisterLocal(reg);
      return this.getComparisonExpr(one, IntConstant.v(0));
   }

   protected ConditionExpr getComparisonExpr(Immediate one, Immediate other) {
      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case IF_EQ:
      case IF_EQZ:
         return Jimple.v().newEqExpr(one, other);
      case IF_NE:
      case IF_NEZ:
         return Jimple.v().newNeExpr(one, other);
      case IF_LT:
      case IF_LTZ:
         return Jimple.v().newLtExpr(one, other);
      case IF_GE:
      case IF_GEZ:
         return Jimple.v().newGeExpr(one, other);
      case IF_GT:
      case IF_GTZ:
         return Jimple.v().newGtExpr(one, other);
      case IF_LE:
      case IF_LEZ:
         return Jimple.v().newLeExpr(one, other);
      default:
         throw new RuntimeException("Instruction is not an IfTest(z) instruction.");
      }
   }
}
