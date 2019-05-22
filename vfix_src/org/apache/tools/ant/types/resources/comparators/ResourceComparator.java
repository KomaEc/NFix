package org.apache.tools.ant.types.resources.comparators;

import java.util.Comparator;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Resource;

public abstract class ResourceComparator extends DataType implements Comparator {
   public final int compare(Object foo, Object bar) {
      this.dieOnCircularReference();
      ResourceComparator c = this.isReference() ? (ResourceComparator)this.getCheckedRef() : this;
      return c.resourceCompare((Resource)foo, (Resource)bar);
   }

   public boolean equals(Object o) {
      if (this.isReference()) {
         return this.getCheckedRef().equals(o);
      } else if (o == null) {
         return false;
      } else {
         return o == this || o.getClass().equals(this.getClass());
      }
   }

   public synchronized int hashCode() {
      return this.isReference() ? this.getCheckedRef().hashCode() : this.getClass().hashCode();
   }

   protected abstract int resourceCompare(Resource var1, Resource var2);
}
