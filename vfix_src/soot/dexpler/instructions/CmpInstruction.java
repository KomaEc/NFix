package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ThreeRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import soot.DoubleType;
import soot.FloatType;
import soot.Local;
import soot.LongType;
import soot.Type;
import soot.Value;
import soot.dexpler.DexBody;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.Expr;
import soot.jimple.Jimple;

public class CmpInstruction extends TaggedInstruction {
   public CmpInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction23x)) {
         throw new IllegalArgumentException("Expected Instruction23x but got: " + this.instruction.getClass());
      } else {
         Instruction23x cmpInstr = (Instruction23x)this.instruction;
         int dest = cmpInstr.getRegisterA();
         Local first = body.getRegisterLocal(cmpInstr.getRegisterB());
         Local second = body.getRegisterLocal(cmpInstr.getRegisterC());
         Opcode opcode = this.instruction.getOpcode();
         Expr cmpExpr = null;
         Type type = null;
         FloatType type;
         DoubleType type;
         switch(opcode) {
         case CMPL_DOUBLE:
            this.setTag(new DoubleOpTag());
            type = DoubleType.v();
            cmpExpr = Jimple.v().newCmplExpr(first, second);
            break;
         case CMPL_FLOAT:
            this.setTag(new FloatOpTag());
            type = FloatType.v();
            cmpExpr = Jimple.v().newCmplExpr(first, second);
            break;
         case CMPG_DOUBLE:
            this.setTag(new DoubleOpTag());
            type = DoubleType.v();
            cmpExpr = Jimple.v().newCmpgExpr(first, second);
            break;
         case CMPG_FLOAT:
            this.setTag(new FloatOpTag());
            type = FloatType.v();
            cmpExpr = Jimple.v().newCmpgExpr(first, second);
            break;
         case CMP_LONG:
            this.setTag(new LongOpTag());
            type = LongType.v();
            cmpExpr = Jimple.v().newCmpExpr(first, second);
            break;
         default:
            throw new RuntimeException("no opcode for CMP: " + opcode);
         }

         AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), (Value)cmpExpr);
         assign.addTag(this.getTag());
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   boolean overridesRegister(int register) {
      ThreeRegisterInstruction i = (ThreeRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }
}
