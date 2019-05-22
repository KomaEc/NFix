package org.jf.dexlib2.immutable.debug;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.debug.LineNumber;

public class ImmutableLineNumber extends ImmutableDebugItem implements LineNumber {
   protected final int lineNumber;

   public ImmutableLineNumber(int codeAddress, int lineNumber) {
      super(codeAddress);
      this.lineNumber = lineNumber;
   }

   @Nonnull
   public static ImmutableLineNumber of(@Nonnull LineNumber lineNumber) {
      return lineNumber instanceof ImmutableLineNumber ? (ImmutableLineNumber)lineNumber : new ImmutableLineNumber(lineNumber.getCodeAddress(), lineNumber.getLineNumber());
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getDebugItemType() {
      return 10;
   }
}
