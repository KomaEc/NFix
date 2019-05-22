package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.immutable.debug.ImmutableDebugItem;
import org.jf.dexlib2.immutable.instruction.ImmutableInstruction;
import org.jf.util.ImmutableUtils;

public class ImmutableMethodImplementation implements MethodImplementation {
   protected final int registerCount;
   @Nonnull
   protected final ImmutableList<? extends ImmutableInstruction> instructions;
   @Nonnull
   protected final ImmutableList<? extends ImmutableTryBlock> tryBlocks;
   @Nonnull
   protected final ImmutableList<? extends ImmutableDebugItem> debugItems;

   public ImmutableMethodImplementation(int registerCount, @Nullable Iterable<? extends Instruction> instructions, @Nullable List<? extends TryBlock<? extends ExceptionHandler>> tryBlocks, @Nullable Iterable<? extends DebugItem> debugItems) {
      this.registerCount = registerCount;
      this.instructions = ImmutableInstruction.immutableListOf(instructions);
      this.tryBlocks = ImmutableTryBlock.immutableListOf(tryBlocks);
      this.debugItems = ImmutableDebugItem.immutableListOf(debugItems);
   }

   public ImmutableMethodImplementation(int registerCount, @Nullable ImmutableList<? extends ImmutableInstruction> instructions, @Nullable ImmutableList<? extends ImmutableTryBlock> tryBlocks, @Nullable ImmutableList<? extends ImmutableDebugItem> debugItems) {
      this.registerCount = registerCount;
      this.instructions = ImmutableUtils.nullToEmptyList(instructions);
      this.tryBlocks = ImmutableUtils.nullToEmptyList(tryBlocks);
      this.debugItems = ImmutableUtils.nullToEmptyList(debugItems);
   }

   @Nullable
   public static ImmutableMethodImplementation of(@Nullable MethodImplementation methodImplementation) {
      if (methodImplementation == null) {
         return null;
      } else {
         return methodImplementation instanceof ImmutableMethodImplementation ? (ImmutableMethodImplementation)methodImplementation : new ImmutableMethodImplementation(methodImplementation.getRegisterCount(), methodImplementation.getInstructions(), methodImplementation.getTryBlocks(), methodImplementation.getDebugItems());
      }
   }

   public int getRegisterCount() {
      return this.registerCount;
   }

   @Nonnull
   public ImmutableList<? extends ImmutableInstruction> getInstructions() {
      return this.instructions;
   }

   @Nonnull
   public ImmutableList<? extends ImmutableTryBlock> getTryBlocks() {
      return this.tryBlocks;
   }

   @Nonnull
   public ImmutableList<? extends ImmutableDebugItem> getDebugItems() {
      return this.debugItems;
   }
}
