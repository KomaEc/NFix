package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.debug.EpilogueBegin;

public class ImmutableEpilogueBegin extends ImmutableDebugItem implements EpilogueBegin {
   public ImmutableEpilogueBegin(int codeAddress) {
      super(codeAddress);
   }

   @Nonnull
   public static ImmutableEpilogueBegin of(@Nonnull EpilogueBegin epilogueBegin) {
      return epilogueBegin instanceof ImmutableEpilogueBegin ? (ImmutableEpilogueBegin)epilogueBegin : new ImmutableEpilogueBegin(epilogueBegin.getCodeAddress());
   }

   public int getDebugItemType() {
      return 8;
   }
}
