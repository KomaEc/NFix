package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;

public class Difference extends BaseResourceCollectionContainer {
   protected Collection getCollection() {
      List rc = this.getResourceCollections();
      int size = rc.size();
      if (size < 2) {
         throw new BuildException("The difference of " + size + " resource collection" + (size == 1 ? "" : "s") + " is undefined.");
      } else {
         HashSet hs = new HashSet();
         ArrayList al = new ArrayList();
         Iterator rcIter = rc.iterator();

         while(rcIter.hasNext()) {
            Iterator r = nextRC(rcIter).iterator();

            while(r.hasNext()) {
               Object next = r.next();
               if (hs.add(next)) {
                  al.add(next);
               } else {
                  al.remove(next);
               }
            }
         }

         return al;
      }
   }

   private static ResourceCollection nextRC(Iterator i) {
      return (ResourceCollection)i.next();
   }
}
