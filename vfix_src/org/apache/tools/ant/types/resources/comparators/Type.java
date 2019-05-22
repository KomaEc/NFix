package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.types.Resource;

public class Type extends ResourceComparator {
   protected int resourceCompare(Resource foo, Resource bar) {
      boolean f = foo.isDirectory();
      if (f == bar.isDirectory()) {
         return 0;
      } else {
         return f ? 1 : -1;
      }
   }
}
