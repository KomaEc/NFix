package org.jf.dexlib2.builder.instruction;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderSwitchPayload;
import org.jf.dexlib2.builder.SwitchLabelElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;

public class BuilderSparseSwitchPayload extends BuilderSwitchPayload implements SparseSwitchPayload {
   public static final Opcode OPCODE;
   @Nonnull
   protected final List<BuilderSwitchElement> switchElements;

   public BuilderSparseSwitchPayload(@Nullable List<? extends SwitchLabelElement> switchElements) {
      super(OPCODE);
      if (switchElements == null) {
         this.switchElements = ImmutableList.of();
      } else {
         this.switchElements = Lists.transform(switchElements, new Function<SwitchLabelElement, BuilderSwitchElement>() {
            @Nullable
            public BuilderSwitchElement apply(@Nullable SwitchLabelElement element) {
               assert element != null;

               return new BuilderSwitchElement(BuilderSparseSwitchPayload.this, element.key, element.target);
            }
         });
      }

   }

   @Nonnull
   public List<BuilderSwitchElement> getSwitchElements() {
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
