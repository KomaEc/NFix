package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;

public class First extends BaseResourceCollectionWrapper {
   private static final String BAD_COUNT = "count of first resources should be set to an int >= 0";
   private int count = 1;

   public synchronized void setCount(int i) {
      this.count = i;
   }

   public synchronized int getCount() {
      return this.count;
   }

   protected Collection getCollection() {
      int ct = this.getCount();
      if (ct < 0) {
         throw new BuildException("count of first resources should be set to an int >= 0");
      } else {
         Iterator iter = this.getResourceCollection().iterator();
         ArrayList al = new ArrayList(ct);

         for(int i = 0; i < ct && iter.hasNext(); ++i) {
            al.add(iter.next());
         }

         return al;
      }
   }
}
