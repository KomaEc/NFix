package org.apache.commons.collections.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.collection.CompositeCollection;

public class CompositeSet extends CompositeCollection implements Set {
   public CompositeSet() {
   }

   public CompositeSet(Set set) {
      super((Collection)set);
   }

   public CompositeSet(Set[] sets) {
      super((Collection[])sets);
   }

   public synchronized void addComposited(Collection c) {
      if (!(c instanceof Set)) {
         throw new IllegalArgumentException("Collections added must implement java.util.Set");
      } else {
         Iterator i = this.getCollections().iterator();

         while(i.hasNext()) {
            Set set = (Set)i.next();
            Collection intersects = CollectionUtils.intersection(set, c);
            if (intersects.size() > 0) {
               if (super.mutator == null) {
                  throw new UnsupportedOperationException("Collision adding composited collection with no SetMutator set");
               }

               if (!(super.mutator instanceof CompositeSet.SetMutator)) {
                  throw new UnsupportedOperationException("Collision adding composited collection to a CompositeSet with a CollectionMutator instead of a SetMutator");
               }

               ((CompositeSet.SetMutator)super.mutator).resolveCollision(this, set, (Set)c, intersects);
               if (CollectionUtils.intersection(set, c).size() > 0) {
                  throw new IllegalArgumentException("Attempt to add illegal entry unresolved by SetMutator.resolveCollision()");
               }
            }
         }

         super.addComposited(new Collection[]{c});
      }
   }

   public synchronized void addComposited(Collection c, Collection d) {
      if (!(c instanceof Set)) {
         throw new IllegalArgumentException("Argument must implement java.util.Set");
      } else if (!(d instanceof Set)) {
         throw new IllegalArgumentException("Argument must implement java.util.Set");
      } else {
         this.addComposited((Collection[])(new Set[]{(Set)c, (Set)d}));
      }
   }

   public synchronized void addComposited(Collection[] comps) {
      for(int i = comps.length - 1; i >= 0; --i) {
         this.addComposited(comps[i]);
      }

   }

   public void setMutator(CompositeCollection.CollectionMutator mutator) {
      super.setMutator(mutator);
   }

   public boolean remove(Object obj) {
      Iterator i = this.getCollections().iterator();

      while(i.hasNext()) {
         Set set = (Set)i.next();
         if (set.contains(obj)) {
            return set.remove(obj);
         }
      }

      return false;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Set) {
         Set set = (Set)obj;
         if (set.containsAll(this) && set.size() == this.size()) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      int code = 0;

      Object next;
      for(Iterator i = this.iterator(); i.hasNext(); code += next != null ? next.hashCode() : 0) {
         next = i.next();
      }

      return code;
   }

   public interface SetMutator extends CompositeCollection.CollectionMutator {
      void resolveCollision(CompositeSet var1, Set var2, Set var3, Collection var4);
   }
}
