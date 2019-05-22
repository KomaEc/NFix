package org.apache.tools.ant.types.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.resources.selectors.ResourceSelectorContainer;

public class Restrict extends ResourceSelectorContainer implements ResourceCollection {
   private BaseResourceCollectionWrapper w = new BaseResourceCollectionWrapper() {
      protected Collection getCollection() {
         ArrayList result = new ArrayList();
         Iterator ri = Restrict.this.w.getResourceCollection().iterator();

         while(true) {
            label20:
            while(ri.hasNext()) {
               Resource r = (Resource)ri.next();
               Iterator i = Restrict.this.getSelectors();

               while(i.hasNext()) {
                  if (!((ResourceSelector)((ResourceSelector)i.next())).isSelected(r)) {
                     continue label20;
                  }
               }

               result.add(r);
            }

            return result;
         }
      }
   };

   public synchronized void add(ResourceCollection c) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (c != null) {
         this.w.add(c);
      }
   }

   public synchronized void setCache(boolean b) {
      this.w.setCache(b);
   }

   public synchronized boolean isCache() {
      return this.w.isCache();
   }

   public synchronized void add(ResourceSelector s) {
      if (s != null) {
         super.add(s);
         FailFast.invalidate(this);
      }
   }

   public final synchronized Iterator iterator() {
      if (this.isReference()) {
         return ((Restrict)this.getCheckedRef()).iterator();
      } else {
         this.dieOnCircularReference();
         return this.w.iterator();
      }
   }

   public synchronized int size() {
      if (this.isReference()) {
         return ((Restrict)this.getCheckedRef()).size();
      } else {
         this.dieOnCircularReference();
         return this.w.size();
      }
   }

   public synchronized boolean isFilesystemOnly() {
      if (this.isReference()) {
         return ((Restrict)this.getCheckedRef()).isFilesystemOnly();
      } else {
         this.dieOnCircularReference();
         return this.w.isFilesystemOnly();
      }
   }

   public synchronized String toString() {
      if (this.isReference()) {
         return this.getCheckedRef().toString();
      } else {
         this.dieOnCircularReference();
         return this.w.toString();
      }
   }
}
