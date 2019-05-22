package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;

public class Not implements ResourceSelector {
   private ResourceSelector sel;

   public Not() {
   }

   public Not(ResourceSelector s) {
      this.add(s);
   }

   public void add(ResourceSelector s) {
      if (this.sel != null) {
         throw new IllegalStateException("The Not ResourceSelector accepts a single nested ResourceSelector");
      } else {
         this.sel = s;
      }
   }

   public boolean isSelected(Resource r) {
      return !this.sel.isSelected(r);
   }
}
