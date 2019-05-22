package org.apache.tools.ant.types.resources.comparators;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;

public class Reverse extends ResourceComparator {
   private static final String ONE_NESTED = "You must not nest more than one ResourceComparator for reversal.";
   private ResourceComparator nested;

   public Reverse() {
   }

   public Reverse(ResourceComparator c) {
      this.add(c);
   }

   public void add(ResourceComparator c) {
      if (this.nested != null) {
         throw new BuildException("You must not nest more than one ResourceComparator for reversal.");
      } else {
         this.nested = c;
      }
   }

   protected int resourceCompare(Resource foo, Resource bar) {
      return -1 * (this.nested == null ? foo.compareTo(bar) : this.nested.compare(foo, bar));
   }
}
