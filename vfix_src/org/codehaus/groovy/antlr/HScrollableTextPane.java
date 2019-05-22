package org.codehaus.groovy.antlr;

import java.awt.Dimension;
import javax.swing.JTextPane;

class HScrollableTextPane extends JTextPane {
   public boolean getScrollableTracksViewportWidth() {
      return this.getSize().width < this.getParent().getSize().width;
   }

   public void setSize(Dimension d) {
      if (d.width < this.getParent().getSize().width) {
         d.width = this.getParent().getSize().width;
      }

      super.setSize(d);
   }
}
