package org.jf.dexlib2.dexbacked.instruction;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;

public class DexBackedSparseSwitchPayload extends DexBackedInstruction implements SparseSwitchPayload {
   public final int elementCount;
   private static final int ELEMENT_COUNT_OFFSET = 2;
   private static final int KEYS_OFFSET = 4;

   public DexBackedSparseSwitchPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
      super(dexFile, Opcode.SPARSE_SWITCH_PAYLOAD, instructionStart);
      this.elementCount = dexFile.readUshort(instructionStart + 2);
   }

   @Nonnull
   public List<? extends SwitchElement> getSwitchElements() {
      return new FixedSizeList<SwitchElement>() {
         @Nonnull
         public SwitchElement readItem(final int index) {
            return new SwitchElement() {
               public int getKey() {
                  return DexBackedSparseSwitchPayload.this.dexFile.readInt(DexBackedSparseSwitchPayload.this.instructionStart + 4 + index * 4);
               }

               public int getOffset() {
                  return DexBackedSparseSwitchPayload.this.dexFile.readInt(DexBackedSparseSwitchPayload.this.instructionStart + 4 + DexBackedSparseSwitchPayload.this.elementCount * 4 + index * 4);
               }
            };
         }

         public int size() {
            return DexBackedSparseSwitchPayload.this.elementCount;
         }
      };
   }

   public int getCodeUnits() {
      return 2 + this.elementCount * 4;
   }
}
