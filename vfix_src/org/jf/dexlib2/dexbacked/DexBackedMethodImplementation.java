package org.jf.dexlib2.dexbacked;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction;
import org.jf.dexlib2.dexbacked.util.DebugInfo;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.dexbacked.util.VariableSizeListIterator;
import org.jf.dexlib2.dexbacked.util.VariableSizeLookaheadIterator;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.util.AlignmentUtils;
import org.jf.util.ExceptionWithContext;

public class DexBackedMethodImplementation implements MethodImplementation {
   @Nonnull
   public final DexBackedDexFile dexFile;
   @Nonnull
   public final DexBackedMethod method;
   private final int codeOffset;

   public DexBackedMethodImplementation(@Nonnull DexBackedDexFile dexFile, @Nonnull DexBackedMethod method, int codeOffset) {
      this.dexFile = dexFile;
      this.method = method;
      this.codeOffset = codeOffset;
   }

   public int getRegisterCount() {
      return this.dexFile.readUshort(this.codeOffset);
   }

   @Nonnull
   public Iterable<? extends Instruction> getInstructions() {
      int instructionsSize = this.dexFile.readSmallUint(this.codeOffset + 12);
      final int instructionsStartOffset = this.codeOffset + 16;
      final int endOffset = instructionsStartOffset + instructionsSize * 2;
      return new Iterable<Instruction>() {
         public Iterator<Instruction> iterator() {
            return new VariableSizeLookaheadIterator<Instruction>(DexBackedMethodImplementation.this.dexFile, instructionsStartOffset) {
               protected Instruction readNextItem(@Nonnull DexReader reader) {
                  if (reader.getOffset() >= endOffset) {
                     return (Instruction)this.endOfData();
                  } else {
                     Instruction instruction = DexBackedInstruction.readFrom(reader);
                     int offset = reader.getOffset();
                     if (offset <= endOffset && offset >= 0) {
                        return instruction;
                     } else {
                        throw new ExceptionWithContext("The last instruction in method %s is truncated", new Object[]{DexBackedMethodImplementation.this.method});
                     }
                  }
               }
            };
         }
      };
   }

   @Nonnull
   public List<? extends DexBackedTryBlock> getTryBlocks() {
      final int triesSize = this.dexFile.readUshort(this.codeOffset + 6);
      if (triesSize > 0) {
         int instructionsSize = this.dexFile.readSmallUint(this.codeOffset + 12);
         final int triesStartOffset = AlignmentUtils.alignOffset(this.codeOffset + 16 + instructionsSize * 2, 4);
         final int handlersStartOffset = triesStartOffset + triesSize * 8;
         return new FixedSizeList<DexBackedTryBlock>() {
            @Nonnull
            public DexBackedTryBlock readItem(int index) {
               return new DexBackedTryBlock(DexBackedMethodImplementation.this.dexFile, triesStartOffset + index * 8, handlersStartOffset);
            }

            public int size() {
               return triesSize;
            }
         };
      } else {
         return ImmutableList.of();
      }
   }

   @Nonnull
   private DebugInfo getDebugInfo() {
      int debugOffset = this.dexFile.readInt(this.codeOffset + 8);
      if (debugOffset != -1 && debugOffset != 0) {
         if (debugOffset < 0) {
            System.err.println(String.format("%s: Invalid debug offset", this.method));
            return DebugInfo.newOrEmpty(this.dexFile, 0, this);
         } else if (debugOffset >= this.dexFile.buf.length) {
            System.err.println(String.format("%s: Invalid debug offset", this.method));
            return DebugInfo.newOrEmpty(this.dexFile, 0, this);
         } else {
            return DebugInfo.newOrEmpty(this.dexFile, debugOffset, this);
         }
      } else {
         return DebugInfo.newOrEmpty(this.dexFile, 0, this);
      }
   }

   @Nonnull
   public Iterable<? extends DebugItem> getDebugItems() {
      return this.getDebugInfo();
   }

   @Nonnull
   public Iterator<String> getParameterNames(@Nullable DexReader dexReader) {
      return this.getDebugInfo().getParameterNames(dexReader);
   }

   public int getSize() {
      int debugSize = this.getDebugInfo().getSize();
      int lastOffset = this.codeOffset + 16;
      lastOffset += this.dexFile.readSmallUint(this.codeOffset + 12) * 2;

      Iterator tryHandlerIter;
      for(Iterator var3 = this.getTryBlocks().iterator(); var3.hasNext(); lastOffset = ((VariableSizeListIterator)tryHandlerIter).getReaderOffset()) {
         DexBackedTryBlock tryBlock = (DexBackedTryBlock)var3.next();
         tryHandlerIter = tryBlock.getExceptionHandlers().iterator();

         while(tryHandlerIter.hasNext()) {
            tryHandlerIter.next();
         }
      }

      return debugSize + (lastOffset - this.codeOffset);
   }
}
