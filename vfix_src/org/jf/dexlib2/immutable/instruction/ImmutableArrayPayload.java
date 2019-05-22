package org.jf.dexlib2.immutable.instruction;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;
import org.jf.util.ImmutableUtils;

public class ImmutableArrayPayload extends ImmutableInstruction implements ArrayPayload {
   public static final Opcode OPCODE;
   protected final int elementWidth;
   @Nonnull
   protected final ImmutableList<Number> arrayElements;

   public ImmutableArrayPayload(int elementWidth, @Nullable List<Number> arrayElements) {
      super(OPCODE);
      this.elementWidth = elementWidth;
      this.arrayElements = arrayElements == null ? ImmutableList.of() : ImmutableList.copyOf((Collection)arrayElements);
   }

   public ImmutableArrayPayload(int elementWidth, @Nullable ImmutableList<Number> arrayElements) {
      super(OPCODE);
      this.elementWidth = elementWidth;
      this.arrayElements = ImmutableUtils.nullToEmptyList(arrayElements);
   }

   @Nonnull
   public static ImmutableArrayPayload of(ArrayPayload instruction) {
      return instruction instanceof ImmutableArrayPayload ? (ImmutableArrayPayload)instruction : new ImmutableArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
   }

   public int getElementWidth() {
      return this.elementWidth;
   }

   @Nonnull
   public List<Number> getArrayElements() {
      return this.arrayElements;
   }

   public int getCodeUnits() {
      return 4 + (this.elementWidth * this.arrayElements.size() + 1) / 2;
   }

   public Format getFormat() {
      return OPCODE.format;
   }

   static {
      OPCODE = Opcode.ARRAY_PAYLOAD;
   }
}
