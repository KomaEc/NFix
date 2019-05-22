package org.jf.dexlib2.immutable.instruction;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;
import org.jf.util.ImmutableUtils;

public class ImmutablePackedSwitchPayload extends ImmutableInstruction implements PackedSwitchPayload {
   public static final Opcode OPCODE;
   @Nonnull
   protected final ImmutableList<? extends ImmutableSwitchElement> switchElements;

   public ImmutablePackedSwitchPayload(@Nullable List<? extends SwitchElement> switchElements) {
      super(OPCODE);
      this.switchElements = ImmutableSwitchElement.immutableListOf(switchElements);
   }

   public ImmutablePackedSwitchPayload(@Nullable ImmutableList<? extends ImmutableSwitchElement> switchElements) {
      super(OPCODE);
      this.switchElements = ImmutableUtils.nullToEmptyList(switchElements);
   }

   @Nonnull
   public static ImmutablePackedSwitchPayload of(PackedSwitchPayload instruction) {
      return instruction instanceof ImmutablePackedSwitchPayload ? (ImmutablePackedSwitchPayload)instruction : new ImmutablePackedSwitchPayload(instruction.getSwitchElements());
   }

   @Nonnull
   public List<? extends SwitchElement> getSwitchElements() {
      return this.switchElements;
   }

   public int getCodeUnits() {
      return 4 + this.switchElements.size() * 2;
   }

   public Format getFormat() {
      return OPCODE.format;
   }

   static {
      OPCODE = Opcode.PACKED_SWITCH_PAYLOAD;
   }
}
