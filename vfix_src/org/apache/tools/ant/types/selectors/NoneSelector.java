package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

public class NoneSelector extends BaseSelectorContainer {
   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (this.hasSelectors()) {
         buf.append("{noneselect: ");
         buf.append(super.toString());
         buf.append("}");
      }

      return buf.toString();
   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      Enumeration e = this.selectorElements();

      boolean result;
      do {
         if (!e.hasMoreElements()) {
            return true;
         }

         result = ((FileSelector)e.nextElement()).isSelected(basedir, filename, file);
      } while(!result);

      return false;
   }
}
