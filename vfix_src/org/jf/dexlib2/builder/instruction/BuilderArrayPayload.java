package org.jf.dexlib2.builder.instruction;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.iface.instruction.formats.ArrayPayload;

public class BuilderArrayPayload extends BuilderInstruction implements ArrayPayload {
   public static final Opcode OPCODE;
   protected final int elementWidth;
   @Nonnull
   protected final List<Number> arrayElements;

   public BuilderArrayPayload(int elementWidth, @Nullable List<Number> arrayElements) {
      super(OPCODE);
      this.elementWidth = elementWidth;
      this.arrayElements = (List)(arrayElements == null ? ImmutableList.of() : arrayElements);
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
