package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Jimple;

public class CheckCastInstruction extends DexlibAbstractInstruction {
   public CheckCastInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction21c)) {
         throw new IllegalArgumentException("Expected Instruction21c but got: " + this.instruction.getClass());
      } else {
         Instruction21c checkCastInstr = (Instruction21c)this.instruction;
         Local castValue = body.getRegisterLocal(checkCastInstr.getRegisterA());
         Type checkCastType = DexType.toSoot((TypeReference)checkCastInstr.getReference());
         CastExpr castExpr = Jimple.v().newCastExpr(castValue, checkCastType);
         AssignStmt assign = Jimple.v().newAssignStmt(castValue, castExpr);
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   public Set<Type> introducedTypes() {
      ReferenceInstruction i = (ReferenceInstruction)this.instruction;
      Set<Type> types = new HashSet();
      types.add(DexType.toSoot((TypeReference)i.getReference()));
      return types;
   }
}
