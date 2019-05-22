package org.jf.dexlib2.builder.instruction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.Format;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderSwitchPayload;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.iface.instruction.formats.PackedSwitchPayload;

public class BuilderPackedSwitchPayload extends BuilderSwitchPayload implements PackedSwitchPayload {
   public static final Opcode OPCODE;
   @Nonnull
   protected final List<BuilderSwitchElement> switchElements;

   public BuilderPackedSwitchPayload(int startKey, @Nullable List<? extends Label> switchElements) {
      super(OPCODE);
      if (switchElements == null) {
         this.switchElements = ImmutableList.of();
      } else {
         this.switchElements = Lists.newArrayList();
         int key = startKey;
         Iterator var4 = switchElements.iterator();

         while(var4.hasNext()) {
            Label target = (Label)var4.next();
            this.switchElements.add(new BuilderSwitchElement(this, key++, target));
         }
      }

   }

   @Nonnull
   public List<BuilderSwitchElement> getSwitchElements() {
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
