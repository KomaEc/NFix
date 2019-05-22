package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;
import org.apache.tools.ant.types.Resource;

public class And extends ResourceSelectorContainer implements ResourceSelector {
   public And() {
   }

   public And(ResourceSelector[] r) {
      super(r);
   }

   public boolean isSelected(Resource r) {
      Iterator i = this.getSelectors();

      do {
         if (!i.hasNext()) {
            return true;
         }
      } while(((ResourceSelector)i.next()).isSelected(r));

      return false;
   }
}
