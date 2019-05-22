package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction3rc;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.ArrayType;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;

public class FilledNewArrayRangeInstruction extends FilledArrayInstruction {
   public FilledNewArrayRangeInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction3rc)) {
         throw new IllegalArgumentException("Expected Instruction3rc but got: " + this.instruction.getClass());
      } else {
         Instruction3rc filledNewArrayInstr = (Instruction3rc)this.instruction;
         int usedRegister = filledNewArrayInstr.getRegisterCount();
         Type t = DexType.toSoot((TypeReference)filledNewArrayInstr.getReference());
         Type arrayType = ((ArrayType)t).getElementType();
         NewArrayExpr arrayExpr = Jimple.v().newNewArrayExpr(arrayType, IntConstant.v(usedRegister));
         Local arrayLocal = body.getStoreResultLocal();
         AssignStmt assignStmt = Jimple.v().newAssignStmt(arrayLocal, arrayExpr);
         body.add(assignStmt);

         for(int i = 0; i < usedRegister; ++i) {
            ArrayRef arrayRef = Jimple.v().newArrayRef(arrayLocal, IntConstant.v(i));
            AssignStmt assign = Jimple.v().newAssignStmt(arrayRef, body.getRegisterLocal(i + filledNewArrayInstr.getStartRegister()));
            this.addTags(assign);
            body.add(assign);
         }

         this.setUnit(assignStmt);
      }
   }

   boolean isUsedAsFloatingPoint(DexBody body, int register) {
      Instruction3rc i = (Instruction3rc)this.instruction;
      Type arrayType = DexType.toSoot((TypeReference)i.getReference());
      int startRegister = i.getStartRegister();
      int endRegister = startRegister + i.getRegisterCount();
      return register >= startRegister && register <= endRegister && Util.isFloatLike(arrayType);
   }
}
