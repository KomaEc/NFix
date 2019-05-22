package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;

public class Intersect extends BaseResourceCollectionContainer {
   protected Collection getCollection() {
      List rcs = this.getResourceCollections();
      int size = rcs.size();
      if (size < 2) {
         throw new BuildException("The intersection of " + size + " resource collection" + (size == 1 ? "" : "s") + " is undefined.");
      } else {
         ArrayList al = new ArrayList();
         Iterator rc = rcs.iterator();
         al.addAll(this.collect(rc.next()));

         while(rc.hasNext()) {
            al.retainAll(this.collect(rc.next()));
         }

         return al;
      }
   }

   private ArrayList collect(Object o) {
      ArrayList result = new ArrayList();
      Iterator i = ((ResourceCollection)o).iterator();

      while(i.hasNext()) {
         result.add(i.next());
      }

      return result;
   }
}
