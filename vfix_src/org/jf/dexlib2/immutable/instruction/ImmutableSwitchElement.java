package org.jf.dexlib2.immutable.instruction;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.util.ImmutableConverter;

public class ImmutableSwitchElement implements SwitchElement {
   protected final int key;
   protected final int offset;
   private static final ImmutableConverter<ImmutableSwitchElement, SwitchElement> CONVERTER = new ImmutableConverter<ImmutableSwitchElement, SwitchElement>() {
      protected boolean isImmutable(@Nonnull SwitchElement item) {
         return item instanceof ImmutableSwitchElement;
      }

      @Nonnull
      protected ImmutableSwitchElement makeImmutable(@Nonnull SwitchElement item) {
         return ImmutableSwitchElement.of(item);
      }
   };

   public ImmutableSwitchElement(int key, int offset) {
      this.key = key;
      this.offset = offset;
   }

   @Nonnull
   public static ImmutableSwitchElement of(SwitchElement switchElement) {
      return switchElement instanceof ImmutableSwitchElement ? (ImmutableSwitchElement)switchElement : new ImmutableSwitchElement(switchElement.getKey(), switchElement.getOffset());
   }

   public int getKey() {
      return this.key;
   }

   public int getOffset() {
      return this.offset;
   }

   @Nonnull
   public static ImmutableList<ImmutableSwitchElement> immutableListOf(@Nullable List<? extends SwitchElement> list) {
      return CONVERTER.toList(list);
   }
}
