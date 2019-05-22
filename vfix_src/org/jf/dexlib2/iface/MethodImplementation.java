package org.jf.dexlib2.iface;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;

public interface MethodImplementation {
   int getRegisterCount();

   @Nonnull
   Iterable<? extends Instruction> getInstructions();

   @Nonnull
   List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks();

   @Nonnull
   Iterable<? extends DebugItem> getDebugItems();
}
