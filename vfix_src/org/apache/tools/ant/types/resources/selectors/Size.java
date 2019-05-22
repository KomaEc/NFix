package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.Resource;

public class Size implements ResourceSelector {
   private long size = -1L;
   private Comparison when;

   public Size() {
      this.when = Comparison.EQUAL;
   }

   public void setSize(long l) {
      this.size = l;
   }

   public long getSize() {
      return this.size;
   }

   public void setWhen(Comparison c) {
      this.when = c;
   }

   public Comparison getWhen() {
      return this.when;
   }

   public boolean isSelected(Resource r) {
      long diff = r.getSize() - this.size;
      return this.when.evaluate(diff == 0L ? 0 : (int)(diff / Math.abs(diff)));
   }
}
