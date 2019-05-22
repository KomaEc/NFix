package org.jf.dexlib2.builder.debug;

import org.jf.dexlib2.builder.BuilderDebugItem;
import org.jf.dexlib2.iface.debug.EpilogueBegin;

public class BuilderEpilogueBegin extends BuilderDebugItem implements EpilogueBegin {
   public int getDebugItemType() {
      return 8;
   }
}
