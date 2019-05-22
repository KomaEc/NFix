package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;

public class SputInstruction extends FieldInstruction {
   public SputInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      int source = ((OneRegisterInstruction)this.instruction).getRegisterA();
      FieldReference f = (FieldReference)((ReferenceInstruction)this.instruction).getReference();
      StaticFieldRef instanceField = Jimple.v().newStaticFieldRef(this.getStaticSootFieldRef(f));
      Local sourceValue = body.getRegisterLocal(source);
      AssignStmt assign = this.getAssignStmt(body, sourceValue, instanceField);
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
   }

   protected Type getTargetType(DexBody body) {
      FieldReference f = (FieldReference)((ReferenceInstruction)this.instruction).getReference();
      return DexType.toSoot(f.getType());
   }
}
