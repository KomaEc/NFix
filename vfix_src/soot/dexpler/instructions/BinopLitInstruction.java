package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22b;
import org.jf.dexlib2.iface.instruction.formats.Instruction22s;
import soot.Local;
import soot.Value;
import soot.dexpler.DexBody;
import soot.dexpler.tags.IntOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

public class BinopLitInstruction extends TaggedInstruction {
   public BinopLitInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction22s) && !(this.instruction instanceof Instruction22b)) {
         throw new IllegalArgumentException("Expected Instruction22s or Instruction22b but got: " + this.instruction.getClass());
      } else {
         NarrowLiteralInstruction binOpLitInstr = (NarrowLiteralInstruction)this.instruction;
         int dest = ((TwoRegisterInstruction)this.instruction).getRegisterA();
         int source = ((TwoRegisterInstruction)this.instruction).getRegisterB();
         Local source1 = body.getRegisterLocal(source);
         IntConstant constant = IntConstant.v(binOpLitInstr.getNarrowLiteral());
         Value expr = this.getExpression(source1, constant);
         AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), expr);
         assign.addTag(this.getTag());
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   private Value getExpression(Local source1, Value source2) {
      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case ADD_INT_LIT16:
         this.setTag(new IntOpTag());
      case ADD_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newAddExpr(source1, source2);
      case RSUB_INT:
         this.setTag(new IntOpTag());
      case RSUB_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newSubExpr(source2, source1);
      case MUL_INT_LIT16:
         this.setTag(new IntOpTag());
      case MUL_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newMulExpr(source1, source2);
      case DIV_INT_LIT16:
         this.setTag(new IntOpTag());
      case DIV_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newDivExpr(source1, source2);
      case REM_INT_LIT16:
         this.setTag(new IntOpTag());
      case REM_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newRemExpr(source1, source2);
      case AND_INT_LIT8:
         this.setTag(new IntOpTag());
      case AND_INT_LIT16:
         this.setTag(new IntOpTag());
         return Jimple.v().newAndExpr(source1, source2);
      case OR_INT_LIT16:
         this.setTag(new IntOpTag());
      case OR_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newOrExpr(source1, source2);
      case XOR_INT_LIT16:
         this.setTag(new IntOpTag());
      case XOR_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newXorExpr(source1, source2);
      case SHL_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newShlExpr(source1, source2);
      case SHR_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newShrExpr(source1, source2);
      case USHR_INT_LIT8:
         this.setTag(new IntOpTag());
         return Jimple.v().newUshrExpr(source1, source2);
      default:
         throw new RuntimeException("Invalid Opcode: " + opcode);
      }
   }

   boolean overridesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
