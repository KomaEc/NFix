package org.jf.dexlib2.dexbacked.instruction;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.util.FixedSizeList;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;

public class DexBackedPackedSwitchPayload extends DexBackedInstruction implements PackedSwitchPayload {
   public final int elementCount;
   private static final int ELEMENT_COUNT_OFFSET = 2;
   private static final int FIRST_KEY_OFFSET = 4;
   private static final int TARGETS_OFFSET = 8;

   public DexBackedPackedSwitchPayload(@Nonnull DexBackedDexFile dexFile, int instructionStart) {
      super(dexFile, Opcode.PACKED_SWITCH_PAYLOAD, instructionStart);
      this.elementCount = dexFile.readUshort(instructionStart + 2);
   }

   @Nonnull
   public List<? extends SwitchElement> getSwitchElements() {
      final int firstKey = this.dexFile.readInt(this.instructionStart + 4);
      return new FixedSizeList<SwitchElement>() {
         @Nonnull
         public SwitchElement readItem(final int index) {
            return new SwitchElement() {
               public int getKey() {
                  return firstKey + index;
               }

               public int getOffset() {
                  return DexBackedPackedSwitchPayload.this.dexFile.readInt(DexBackedPackedSwitchPayload.this.instructionStart + 8 + index * 4);
               }
            };
         }

         public int size() {
            return DexBackedPackedSwitchPayload.this.elementCount;
         }
      };
   }

   public int getCodeUnits() {
      return 4 + this.elementCount * 2;
   }
}
