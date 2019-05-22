package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.Constant;
import soot.jimple.Jimple;

public class ConstClassInstruction extends DexlibAbstractInstruction {
   public ConstClassInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      if (!(this.instruction instanceof Instruction21c)) {
         throw new IllegalArgumentException("Expected Instruction21c but got: " + this.instruction.getClass());
      } else {
         ReferenceInstruction constClass = (ReferenceInstruction)this.instruction;
         TypeReference tidi = (TypeReference)((TypeReference)constClass.getReference());
         Constant cst = ClassConstant.v(tidi.getType());
         int dest = ((OneRegisterInstruction)this.instruction).getRegisterA();
         AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), cst);
         this.setUnit(assign);
         this.addTags(assign);
         body.add(assign);
      }
   }

   boolean overridesRegister(int register) {
      OneRegisterInstruction i = (OneRegisterInstruction)this.instruction;
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
