package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;
import org.apache.tools.ant.types.Resource;

public class None extends ResourceSelectorContainer implements ResourceSelector {
   public None() {
   }

   public None(ResourceSelector[] r) {
      super(r);
   }

   public boolean isSelected(Resource r) {
      boolean none = true;

      for(Iterator i = this.getSelectors(); none && i.hasNext(); none = !((ResourceSelector)i.next()).isSelected(r)) {
      }

      return none;
   }
}
