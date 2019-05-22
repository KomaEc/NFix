package org.jf.dexlib2.immutable.instruction;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import org.jf.util.ImmutableUtils;

public class ImmutableSparseSwitchPayload extends ImmutableInstruction implements SparseSwitchPayload {
   public static final Opcode OPCODE;
   @Nonnull
   protected final ImmutableList<? extends ImmutableSwitchElement> switchElements;

   public ImmutableSparseSwitchPayload(@Nullable List<? extends SwitchElement> switchElements) {
      super(OPCODE);
      this.switchElements = ImmutableSwitchElement.immutableListOf(switchElements);
   }

   public ImmutableSparseSwitchPayload(@Nullable ImmutableList<? extends ImmutableSwitchElement> switchElements) {
      super(OPCODE);
      this.switchElements = ImmutableUtils.nullToEmptyList(switchElements);
   }

   @Nonnull
   public static ImmutableSparseSwitchPayload of(SparseSwitchPayload instruction) {
      return instruction instanceof ImmutableSparseSwitchPayload ? (ImmutableSparseSwitchPayload)instruction : new ImmutableSparseSwitchPayload(instruction.getSwitchElements());
   }

   @Nonnull
   public List<? extends SwitchElement> getSwitchElements() {
      return this.switchElements;
   }

   public int getCodeUnits() {
      return 2 + this.switchElements.size() * 4;
   }

   public Format getFormat() {
      return OPCODE.format;
   }

   static {
      OPCODE = Opcode.SPARSE_SWITCH_PAYLOAD;
   }
}
