package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Enumeration;

public class MajoritySelector extends BaseSelectorContainer {
   private boolean allowtie = true;

   public String toString() {
      StringBuffer buf = new StringBuffer();
      if (this.hasSelectors()) {
         buf.append("{majorityselect: ");
         buf.append(super.toString());
         buf.append("}");
      }

      return buf.toString();
   }

   public void setAllowtie(boolean tiebreaker) {
      this.allowtie = tiebreaker;
   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      int yesvotes = 0;
      int novotes = 0;
      Enumeration e = this.selectorElements();

      while(e.hasMoreElements()) {
         boolean result = ((FileSelector)e.nextElement()).isSelected(basedir, filename, file);
         if (result) {
            ++yesvotes;
         } else {
            ++novotes;
         }
      }

      if (yesvotes > novotes) {
         return true;
      } else if (novotes > yesvotes) {
         return false;
      } else {
         return this.allowtie;
      }
   }
}
