package org.jf.dexlib2.builder.debug;

import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.PrologueEnd;

public class BuilderPrologueEnd extends BuilderDebugItem implements PrologueEnd {
   public int getDebugItemType() {
      return 7;
   }
}
