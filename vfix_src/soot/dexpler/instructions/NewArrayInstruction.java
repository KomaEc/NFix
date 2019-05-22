package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.ArrayType;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;

public class NewArrayInstruction extends DexlibAbstractInstruction {
   public NewArrayInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction22c)) {
         throw new IllegalArgumentException("Expected Instruction22c but got: " + this.instruction.getClass());
      } else {
         Instruction22c newArray = (Instruction22c)this.instruction;
         int dest = newArray.getRegisterA();
         Value size = body.getRegisterLocal(newArray.getRegisterB());
         Type t = DexType.toSoot((TypeReference)newArray.getReference());
         Type arrayType = ((ArrayType)t).getElementType();
         NewArrayExpr newArrayExpr = Jimple.v().newNewArrayExpr(arrayType, size);
         Local l = body.getRegisterLocal(dest);
         AssignStmt assign = Jimple.v().newAssignStmt(l, newArrayExpr);
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   boolean overridesRegister(int register) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int dest = i.getRegisterA();
      return register == dest;
   }

   public Set<Type> introducedTypes() {
      ReferenceInstruction i = (ReferenceInstruction)this.instruction;
      Set<Type> types = new HashSet();
      types.add(DexType.toSoot((TypeReference)i.getReference()));
      return types;
   }
}
