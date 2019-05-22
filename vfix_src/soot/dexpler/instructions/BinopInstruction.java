package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ThreeRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import soot.Local;
import soot.Value;
import soot.dexpler.DexBody;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.IntOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;

public class BinopInstruction extends TaggedInstruction {
   public BinopInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction23x)) {
         throw new IllegalArgumentException("Expected Instruction23x but got: " + this.instruction.getClass());
      } else {
         Instruction23x binOpInstr = (Instruction23x)this.instruction;
         int dest = binOpInstr.getRegisterA();
         Local source1 = body.getRegisterLocal(binOpInstr.getRegisterB());
         Local source2 = body.getRegisterLocal(binOpInstr.getRegisterC());
         Value expr = this.getExpression(source1, source2);
         AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), expr);
         assign.addTag(this.getTag());
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   private Value getExpression(Local source1, Local source2) {
      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case ADD_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newAddExpr(source1, source2);
      case ADD_FLOAT:
         this.setTag(new FloatOpTag());
         return Jimple.v().newAddExpr(source1, source2);
      case ADD_DOUBLE:
         this.setTag(new DoubleOpTag());
         return Jimple.v().newAddExpr(source1, source2);
      case ADD_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newAddExpr(source1, source2);
      case SUB_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newSubExpr(source1, source2);
      case SUB_FLOAT:
         this.setTag(new FloatOpTag());
         return Jimple.v().newSubExpr(source1, source2);
      case SUB_DOUBLE:
         this.setTag(new DoubleOpTag());
         return Jimple.v().newSubExpr(source1, source2);
      case SUB_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newSubExpr(source1, source2);
      case MUL_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newMulExpr(source1, source2);
      case MUL_FLOAT:
         this.setTag(new FloatOpTag());
         return Jimple.v().newMulExpr(source1, source2);
      case MUL_DOUBLE:
         this.setTag(new DoubleOpTag());
         return Jimple.v().newMulExpr(source1, source2);
      case MUL_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newMulExpr(source1, source2);
      case DIV_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newDivExpr(source1, source2);
      case DIV_FLOAT:
         this.setTag(new FloatOpTag());
         return Jimple.v().newDivExpr(source1, source2);
      case DIV_DOUBLE:
         this.setTag(new DoubleOpTag());
         return Jimple.v().newDivExpr(source1, source2);
      case DIV_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newDivExpr(source1, source2);
      case REM_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newRemExpr(source1, source2);
      case REM_FLOAT:
         this.setTag(new FloatOpTag());
         return Jimple.v().newRemExpr(source1, source2);
      case REM_DOUBLE:
         this.setTag(new DoubleOpTag());
         return Jimple.v().newRemExpr(source1, source2);
      case REM_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newRemExpr(source1, source2);
      case AND_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newAndExpr(source1, source2);
      case AND_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newAndExpr(source1, source2);
      case OR_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newOrExpr(source1, source2);
      case OR_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newOrExpr(source1, source2);
      case XOR_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newXorExpr(source1, source2);
      case XOR_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newXorExpr(source1, source2);
      case SHL_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newShlExpr(source1, source2);
      case SHL_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newShlExpr(source1, source2);
      case SHR_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newShrExpr(source1, source2);
      case SHR_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newShrExpr(source1, source2);
      case USHR_LONG:
         this.setTag(new LongOpTag());
         return Jimple.v().newUshrExpr(source1, source2);
      case USHR_INT:
         this.setTag(new IntOpTag());
         return Jimple.v().newUshrExpr(source1, source2);
      default:
         throw new RuntimeException("Invalid Opcode: " + opcode);
      }
   }

   boolean overridesRegister(int register) {
      ThreeRegisterInstruction i = (ThreeRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
