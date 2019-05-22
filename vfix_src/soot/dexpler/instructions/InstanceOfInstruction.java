package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceOfExpr;
import soot.jimple.Jimple;

public class InstanceOfInstruction extends DexlibAbstractInstruction {
   public InstanceOfInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   public void jimplify(DexBody body) {
      Instruction22c i = (Instruction22c)this.instruction;
      int dest = i.getRegisterA();
      int source = i.getRegisterB();
      Type t = DexType.toSoot((TypeReference)((TypeReference)i.getReference()));
      InstanceOfExpr e = Jimple.v().newInstanceOfExpr(body.getRegisterLocal(source), t);
      AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), e);
      this.setUnit(assign);
      this.addTags(assign);
      body.add(assign);
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
