package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction22c;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootResolver;
import soot.Type;
import soot.UnknownType;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.AssignStmt;
import soot.jimple.ConcreteRef;
import soot.jimple.Jimple;

public abstract class FieldInstruction extends DexlibAbstractInstruction {
   public FieldInstruction(Instruction instruction, int codeAdress) {
      super(instruction, codeAdress);
   }

   protected SootFieldRef getStaticSootFieldRef(FieldReference fref) {
      return this.getSootFieldRef(fref, true);
   }

   protected SootFieldRef getSootFieldRef(FieldReference fref) {
      return this.getSootFieldRef(fref, false);
   }

   private SootFieldRef getSootFieldRef(FieldReference fref, boolean isStatic) {
      String className = Util.dottedClassName(fref.getDefiningClass());
      SootClass sc = SootResolver.v().makeClassRef(className);
      return Scene.v().makeFieldRef(sc, fref.getName(), DexType.toSoot(fref.getType()), isStatic);
   }

   protected AssignStmt getAssignStmt(DexBody body, Local sourceValue, ConcreteRef instanceField) {
      AssignStmt assign = Jimple.v().newAssignStmt(instanceField, sourceValue);
      return assign;
   }

   boolean isUsedAsFloatingPoint(DexBody body, int register) {
      return this.sourceRegister() == register && Util.isFloatLike(this.getTargetType(body));
   }

   private int sourceRegister() {
      if (this.instruction instanceof Instruction23x) {
         return ((Instruction23x)this.instruction).getRegisterA();
      } else if (this.instruction instanceof Instruction22c) {
         return ((Instruction22c)this.instruction).getRegisterA();
      } else if (this.instruction instanceof Instruction21c) {
         return ((Instruction21c)this.instruction).getRegisterA();
      } else {
         throw new RuntimeException("Instruction is not a instance, array or static op");
      }
   }

   protected Type getTargetType(DexBody body) {
      return UnknownType.v();
   }

   public Set<Type> introducedTypes() {
      Set<Type> types = new HashSet();
      if (!(this.instruction instanceof ReferenceInstruction)) {
         return types;
      } else {
         ReferenceInstruction i = (ReferenceInstruction)this.instruction;
         FieldReference field = (FieldReference)i.getReference();
         types.add(DexType.toSoot(field.getType()));
         types.add(DexType.toSoot(field.getDefiningClass()));
         return types;
      }
   }
}
