package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.tags.DoubleOpTag;
import soot.dexpler.tags.FloatOpTag;
import soot.dexpler.tags.IntOpTag;
import soot.dexpler.tags.LongOpTag;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Jimple;

public class CastInstruction extends TaggedInstruction {
   public CastInstruction(Instruction instruction, int codeAddress) {
      super(instruction, codeAddress);
   }

   public void jimplify(DexBody body) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      int source = i.getRegisterB();
      Type targetType = this.getTargetType();
      CastExpr cast = Jimple.v().newCastExpr(body.getRegisterLocal(source), targetType);
      AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), cast);
      assign.addTag(this.getTag());
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
   }

   private Type getTargetType() {
      Opcode opcode = this.instruction.getOpcode();
      switch(opcode) {
      case INT_TO_BYTE:
         this.setTag(new IntOpTag());
         return ByteType.v();
      case INT_TO_CHAR:
         this.setTag(new IntOpTag());
         return CharType.v();
      case INT_TO_SHORT:
         this.setTag(new IntOpTag());
         return ShortType.v();
      case LONG_TO_INT:
         this.setTag(new LongOpTag());
         return IntType.v();
      case DOUBLE_TO_INT:
         this.setTag(new DoubleOpTag());
         return IntType.v();
      case FLOAT_TO_INT:
         this.setTag(new FloatOpTag());
         return IntType.v();
      case INT_TO_LONG:
         this.setTag(new IntOpTag());
         return LongType.v();
      case DOUBLE_TO_LONG:
         this.setTag(new DoubleOpTag());
         return LongType.v();
      case FLOAT_TO_LONG:
         this.setTag(new FloatOpTag());
         return LongType.v();
      case LONG_TO_FLOAT:
         this.setTag(new LongOpTag());
         return FloatType.v();
      case DOUBLE_TO_FLOAT:
         this.setTag(new DoubleOpTag());
         return FloatType.v();
      case INT_TO_FLOAT:
         this.setTag(new IntOpTag());
         return FloatType.v();
      case INT_TO_DOUBLE:
         this.setTag(new IntOpTag());
         return DoubleType.v();
      case FLOAT_TO_DOUBLE:
         this.setTag(new FloatOpTag());
         return DoubleType.v();
      case LONG_TO_DOUBLE:
         this.setTag(new LongOpTag());
         return DoubleType.v();
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
