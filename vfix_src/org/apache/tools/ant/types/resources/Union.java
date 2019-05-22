package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

public class Union extends BaseResourceCollectionContainer {
   public static Union getInstance(ResourceCollection rc) {
      return rc instanceof Union ? (Union)rc : new Union(rc);
   }

   public Union() {
   }

   public Union(ResourceCollection rc) {
      this.add(rc);
   }

   public String[] list() {
      if (this.isReference()) {
         return ((Union)this.getCheckedRef()).list();
      } else {
         Collection result = this.getCollection(true);
         return (String[])((String[])result.toArray(new String[result.size()]));
      }
   }

   public Resource[] listResources() {
      if (this.isReference()) {
         return ((Union)this.getCheckedRef()).listResources();
      } else {
         Collection result = this.getCollection();
         return (Resource[])((Resource[])result.toArray(new Resource[result.size()]));
      }
   }

   protected Collection getCollection() {
      return this.getCollection(false);
   }

   protected Collection getCollection(boolean asString) {
      List rc = this.getResourceCollections();
      if (rc.isEmpty()) {
         return Collections.EMPTY_LIST;
      } else {
         ArrayList union = new ArrayList(rc.size() * 2);
         Iterator rcIter = rc.iterator();

         while(rcIter.hasNext()) {
            Iterator r = nextRC(rcIter).iterator();

            while(r.hasNext()) {
               Object o = r.next();
               if (asString) {
                  o = o.toString();
               }

               if (!union.contains(o)) {
                  union.add(o);
               }
            }
         }

         return union;
      }
   }

   private static ResourceCollection nextRC(Iterator i) {
      return (ResourceCollection)i.next();
   }
}
