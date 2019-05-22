package org.jf.dexlib2.dexbacked.instruction;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.util.ExceptionWithContext;

public class DexBackedArrayPayload extends DexBackedInstruction implements ArrayPayload {
   public static final Opcode OPCODE;
   public final int elementWidth;
   public final int elementCount;
   private static final int ELEMENT_WIDTH_OFFSET = 2;
   private static final int ELEMENT_COUNT_OFFSET = 4;
   private static final int ELEMENTS_OFFSET = 8;

   public DexBackedArrayPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
      super(dexFile, OPCODE, instructionStart);
      this.elementWidth = dexFile.readUshort(instructionStart + 2);
      this.elementCount = dexFile.readSmallUint(instructionStart + 4);
      if ((long)this.elementWidth * (long)this.elementCount > 2147483647L) {
         throw new ExceptionWithContext("Invalid array-payload instruction: element width*count overflows", new Object[0]);
      }
   }

   public int getElementWidth() {
      return this.elementWidth;
   }

   @Nonnull
   public List<Number> getArrayElements() {
      final int elementsStart = this.instructionStart + 8;

      abstract class ReturnedList extends FixedSizeList<Number> {
         public int size() {
            return DexBackedArrayPayload.this.elementCount;
         }
      }

      switch(this.elementWidth) {
      case 1:
         return new ReturnedList() {
            @Nonnull
            public Number readItem(int index) {
               return DexBackedArrayPayload.this.dexFile.readByte(elementsStart + index);
            }
         };
      case 2:
         return new ReturnedList() {
            @Nonnull
            public Number readItem(int index) {
               return DexBackedArrayPayload.this.dexFile.readShort(elementsStart + index * 2);
            }
         };
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         throw new ExceptionWithContext("Invalid element width: %d", new Object[]{this.elementWidth});
      case 4:
         return new ReturnedList() {
            @Nonnull
            public Number readItem(int index) {
               return DexBackedArrayPayload.this.dexFile.readInt(elementsStart + index * 4);
            }
         };
      case 8:
         return new ReturnedList() {
            @Nonnull
            public Number readItem(int index) {
               return DexBackedArrayPayload.this.dexFile.readLong(elementsStart + index * 8);
            }
         };
      }
   }

   public int getCodeUnits() {
      return 4 + (this.elementWidth * this.elementCount + 1) / 2;
   }

   static {
      OPCODE = Opcode.ARRAY_PAYLOAD;
   }
}
