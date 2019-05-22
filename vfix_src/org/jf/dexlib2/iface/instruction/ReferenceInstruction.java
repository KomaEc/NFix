package org.jf.dexlib2.iface.instruction;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.Reference;

public interface ReferenceInstruction extends Instruction {
   @Nonnull
   Reference getReference();

   int getReferenceType();
}
