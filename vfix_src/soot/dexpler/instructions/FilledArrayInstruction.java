package soot.dexpler.instructions;

import java.util.HashSet;
import java.util.Set;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;

public abstract class FilledArrayInstruction extends DexlibAbstractInstruction implements DanglingInstruction {
   public FilledArrayInstruction(Instruction instruction, int codeAddress) {
      super(instruction, codeAddress);
   }

   public void finalize(DexBody body, DexlibAbstractInstruction successor) {
   }

   public Set<Type> introducedTypes() {
      ReferenceInstruction i = (ReferenceInstruction)this.instruction;
      Set<Type> types = new HashSet();
      types.add(DexType.toSoot((TypeReference)i.getReference()));
      return types;
   }
}
