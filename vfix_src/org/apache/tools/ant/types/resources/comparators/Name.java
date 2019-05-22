package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.types.Resource;

public class Name extends ResourceComparator {
   protected int resourceCompare(Resource foo, Resource bar) {
      return foo.getName().compareTo(bar.getName());
   }
}
