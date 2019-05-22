package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.List;
import soot.toolkits.scalar.AbstractFlowSet;
import soot.toolkits.scalar.BoundedFlowSet;
import soot.toolkits.scalar.FlowSet;

public class ToppedSet<T> extends AbstractFlowSet<T> {
   FlowSet<T> underlyingSet;
   boolean isTop;

   public void setTop(boolean top) {
      this.isTop = top;
   }

   public boolean isTop() {
      return this.isTop;
   }

   public ToppedSet(FlowSet<T> under) {
      this.underlyingSet = under;
   }

   public ToppedSet<T> clone() {
      ToppedSet<T> newSet = new ToppedSet(this.underlyingSet.clone());
      newSet.setTop(this.isTop());
      return newSet;
   }

   public void copy(FlowSet<T> d) {
      if (this != d) {
         ToppedSet<T> dest = (ToppedSet)d;
         dest.isTop = this.isTop;
         if (!this.isTop) {
            this.underlyingSet.copy(dest.underlyingSet);
         }

      }
   }

   public FlowSet<T> emptySet() {
      return new ToppedSet(this.underlyingSet.emptySet());
   }

   public void clear() {
      this.isTop = false;
      this.underlyingSet.clear();
   }

   public void union(FlowSet<T> o, FlowSet<T> d) {
      if (o instanceof ToppedSet && d instanceof ToppedSet) {
         ToppedSet<T> other = (ToppedSet)o;
         ToppedSet<T> dest = (ToppedSet)d;
         if (this.isTop()) {
            this.copy(dest);
            return;
         }

         if (other.isTop()) {
            other.copy(dest);
         } else {
            this.underlyingSet.union(other.underlyingSet, dest.underlyingSet);
            dest.setTop(false);
         }
      } else {
         super.union(o, d);
      }

   }

   public void intersection(FlowSet<T> o, FlowSet<T> d) {
      if (this.isTop()) {
         o.copy(d);
      } else {
         ToppedSet<T> other = (ToppedSet)o;
         ToppedSet<T> dest = (ToppedSet)d;
         if (other.isTop()) {
            this.copy(dest);
         } else {
            this.underlyingSet.intersection(other.underlyingSet, dest.underlyingSet);
            dest.setTop(false);
         }
      }
   }

   public void difference(FlowSet<T> o, FlowSet<T> d) {
      ToppedSet<T> other = (ToppedSet)o;
      ToppedSet<T> dest = (ToppedSet)d;
      if (this.isTop()) {
         if (other.isTop()) {
            dest.clear();
         } else {
            if (!(other.underlyingSet instanceof BoundedFlowSet)) {
               throw new RuntimeException("can't take difference!");
            }

            ((BoundedFlowSet)other.underlyingSet).complement(dest);
         }
      } else if (other.isTop()) {
         dest.clear();
      } else {
         this.underlyingSet.difference(other.underlyingSet, dest.underlyingSet);
      }

   }

   public boolean isEmpty() {
      return this.isTop() ? false : this.underlyingSet.isEmpty();
   }

   public int size() {
      if (this.isTop()) {
         throw new UnsupportedOperationException();
      } else {
         return this.underlyingSet.size();
      }
   }

   public void add(T obj) {
      if (!this.isTop()) {
         this.underlyingSet.add(obj);
      }
   }

   public void remove(T obj) {
      if (!this.isTop()) {
         this.underlyingSet.remove(obj);
      }
   }

   public boolean contains(T obj) {
      return this.isTop() ? true : this.underlyingSet.contains(obj);
   }

   public List<T> toList() {
      if (this.isTop()) {
         throw new UnsupportedOperationException();
      } else {
         return this.underlyingSet.toList();
      }
   }

   public boolean equals(Object o) {
      if (!(o instanceof ToppedSet)) {
         return false;
      } else {
         ToppedSet<T> other = (ToppedSet)o;
         return other.isTop() != this.isTop() ? false : this.underlyingSet.equals(other.underlyingSet);
      }
   }

   public String toString() {
      return this.isTop() ? "{TOP}" : this.underlyingSet.toString();
   }

   public Iterator<T> iterator() {
      if (this.isTop()) {
         throw new UnsupportedOperationException();
      } else {
         return this.underlyingSet.iterator();
      }
   }
}
