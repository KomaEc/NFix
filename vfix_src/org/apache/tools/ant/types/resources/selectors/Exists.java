package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Resource;

public class Exists implements ResourceSelector {
   public boolean isSelected(Resource r) {
      return r.isExists();
   }
}
