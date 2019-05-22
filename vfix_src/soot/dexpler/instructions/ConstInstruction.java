package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.WideLiteralInstruction;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;

public class ConstInstruction extends DexlibAbstractInstruction {
   public ConstInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      int dest = ((OneRegisterInstruction)this.instruction).getRegisterA();
      Constant cst = this.getConstant(dest, body);
      AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), cst);
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
   }

   private Constant getConstant(int dest, DexBody body) {
      long literal = 0L;
      if (this.instruction instanceof WideLiteralInstruction) {
         literal = ((WideLiteralInstruction)this.instruction).getWideLiteral();
      } else {
         if (!(this.instruction instanceof NarrowLiteralInstruction)) {
            throw new RuntimeException("literal error: expected narrow or wide literal.");
         }

         literal = (long)((NarrowLiteralInstruction)this.instruction).getNarrowLiteral();
      }

      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case CONST:
      case CONST_4:
      case CONST_16:
         return IntConstant.v((int)literal);
      case CONST_HIGH16:
         return IntConstant.v((int)literal);
      case CONST_WIDE_HIGH16:
         return LongConstant.v(literal);
      case CONST_WIDE:
      case CONST_WIDE_16:
      case CONST_WIDE_32:
         return LongConstant.v(literal);
      default:
         throw new IllegalArgumentException("Expected a const or a const-wide instruction, got neither.");
      }
   }

   boolean overridesRegister(int register) {
      OneRegisterInstruction i = (OneRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
