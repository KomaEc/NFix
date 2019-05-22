package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;

public class IputInstruction extends FieldInstruction {
   public IputInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      TwoRegisterInstruction i = (TwoRegisterInstruction)this.instruction;
      int source = i.getRegisterA();
      int object = i.getRegisterB();
      FieldReference f = (FieldReference)((ReferenceInstruction)this.instruction).getReference();
      InstanceFieldRef instanceField = Jimple.v().newInstanceFieldRef(body.getRegisterLocal(object), this.getSootFieldRef(f));
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
