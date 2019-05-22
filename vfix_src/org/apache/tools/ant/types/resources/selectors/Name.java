package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.selectors.SelectorUtils;

public class Name implements ResourceSelector {
   private String pattern;
   private boolean cs = true;

   public void setName(String n) {
      this.pattern = n;
   }

   public String getName() {
      return this.pattern;
   }

   public void setCaseSensitive(boolean b) {
      this.cs = b;
   }

   public boolean isCaseSensitive() {
      return this.cs;
   }

   public boolean isSelected(Resource r) {
      String n = r.getName();
      if (SelectorUtils.match(this.pattern, n, this.cs)) {
         return true;
      } else {
         String s = r.toString();
         return s.equals(n) ? false : SelectorUtils.match(this.pattern, s, this.cs);
      }
   }
}
