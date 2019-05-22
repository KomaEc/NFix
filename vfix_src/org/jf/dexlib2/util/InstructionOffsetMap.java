package org.jf.dexlib2.util;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.util.ExceptionWithContext;

public class InstructionOffsetMap {
   @Nonnull
   private final int[] instructionCodeOffsets;

   public InstructionOffsetMap(@Nonnull List<? extends Instruction> instructions) {
      this.instructionCodeOffsets = new int[instructions.size()];
      int codeOffset = 0;

      for(int i = 0; i < instructions.size(); ++i) {
         this.instructionCodeOffsets[i] = codeOffset;
         codeOffset += ((Instruction)instructions.get(i)).getCodeUnits();
      }

   }

   public int getInstructionIndexAtCodeOffset(int codeOffset) {
      return this.getInstructionIndexAtCodeOffset(codeOffset, true);
   }

   public int getInstructionIndexAtCodeOffset(int codeOffset, boolean exact) {
      int index = Arrays.binarySearch(this.instructionCodeOffsets, codeOffset);
      if (index < 0) {
         if (exact) {
            throw new InstructionOffsetMap.InvalidInstructionOffset(codeOffset);
         } else {
            return ~index - 1;
         }
      } else {
         return index;
      }
   }

   public int getInstructionCodeOffset(int index) {
      if (index >= 0 && index < this.instructionCodeOffsets.length) {
         return this.instructionCodeOffsets[index];
      } else {
         throw new InstructionOffsetMap.InvalidInstructionIndex(index);
      }
   }

   public static class InvalidInstructionIndex extends ExceptionWithContext {
      private final int instructionIndex;

      public InvalidInstructionIndex(int instructionIndex) {
         super("Instruction index out of bounds: %d", instructionIndex);
         this.instructionIndex = instructionIndex;
      }

      public int getInstructionIndex() {
         return this.instructionIndex;
      }
   }

   public static class InvalidInstructionOffset extends ExceptionWithContext {
      private final int instructionOffset;

      public InvalidInstructionOffset(int instructionOffset) {
         super("No instruction at offset %d", instructionOffset);
         this.instructionOffset = instructionOffset;
      }

      public int getInstructionOffset() {
         return this.instructionOffset;
      }
   }
}
