package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction12x;
import soot.Local;
import soot.Value;
import soot.dexpler.DexBody;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.IntOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;

public class UnopInstruction extends TaggedInstruction {
   public UnopInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction12x)) {
         throw new IllegalArgumentException("Expected Instruction12x but got: " + this.instruction.getClass());
      } else {
         Instruction12x cmpInstr = (Instruction12x)this.instruction;
         int dest = cmpInstr.getRegisterA();
         Local source = body.getRegisterLocal(cmpInstr.getRegisterB());
         Value expr = this.getExpression(source);
         AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), expr);
         assign.addTag(this.getTag());
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   private Value getExpression(Local source) {
      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case NEG_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newNegExpr(source);
      case NEG_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newNegExpr(source);
      case NEG_FLOAT:
         this.setTag(new FloatOpTag());
         return Jimple.v().newNegExpr(source);
      case NEG_DOUBLE:
         this.setTag(new DoubleOpTag());
         return Jimple.v().newNegExpr(source);
      case NOT_LONG:
         this.setTag(new LongOpTag());
         return this.getNotLongExpr(source);
      case NOT_INT:
         this.setTag(new IntOpTag());
         return this.getNotIntExpr(source);
      default:
         throw new RuntimeException("Invalid Opcode: " + opcode);
      }
   }

   private Value getNotIntExpr(Local source) {
      return Jimple.v().newXorExpr(source, IntConstant.v(-1));
   }

   private Value getNotLongExpr(Local source) {
      return Jimple.v().newXorExpr(source, LongConstant.v(-1L));
   }

   boolean overridesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }

   boolean isUsedAsFloatingPoint(DexBody body, int register) {
      int source = ((TwoRegisterInstruction)this.instruction).getRegisterB();
      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case NEG_FLOAT:
      case NEG_DOUBLE:
         return source == register;
      default:
         return false;
      }
   }
}
