package org.jf.dexlib2.builder.debug;

import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.LineNumber;

public class BuilderLineNumber extends BuilderDebugItem implements LineNumber {
   private final int lineNumber;

   public BuilderLineNumber(int lineNumber) {
      this.lineNumber = lineNumber;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public int getDebugItemType() {
      return 10;
   }
}
