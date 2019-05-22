package org.apache.tools.ant.types.resources.selectors;

import java.util.Iterator;
import org.apache.tools.ant.types.Resource;

public class Or extends ResourceSelectorContainer implements ResourceSelector {
   public Or() {
   }

   public Or(ResourceSelector[] r) {
      super(r);
   }

   public boolean isSelected(Resource r) {
      Iterator i = this.getSelectors();

      do {
         if (!i.hasNext()) {
            return false;
         }
      } while(!((ResourceSelector)i.next()).isSelected(r));

      return true;
   }
}
