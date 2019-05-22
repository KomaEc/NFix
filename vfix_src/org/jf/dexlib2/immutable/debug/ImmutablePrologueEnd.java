package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.debug.PrologueEnd;

public class ImmutablePrologueEnd extends ImmutableDebugItem implements PrologueEnd {
   public ImmutablePrologueEnd(int codeAddress) {
      super(codeAddress);
   }

   @Nonnull
   public static ImmutablePrologueEnd of(@Nonnull PrologueEnd prologueEnd) {
      return prologueEnd instanceof ImmutablePrologueEnd ? (ImmutablePrologueEnd)prologueEnd : new ImmutablePrologueEnd(prologueEnd.getCodeAddress());
   }

   public int getDebugItemType() {
      return 7;
   }
}
