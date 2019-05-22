package org.jboss.util.propertyeditor;

import org.jboss.util.threadpool.BlockingMode;

public class BlockingModeEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      String text = this.getAsText();
      BlockingMode mode = BlockingMode.toBlockingMode(text);
      return mode;
   }
}
