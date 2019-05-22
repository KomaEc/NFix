package soot.toolkits.scalar;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractFlowSet<T> implements FlowSet<T> {
   public abstract AbstractFlowSet<T> clone();

   public FlowSet<T> emptySet() {
      FlowSet<T> t = this.clone();
      t.clear();
      return t;
   }

   public void copy(FlowSet<T> dest) {
      if (this != dest) {
         dest.clear();
         Iterator var2 = this.iterator();

         while(var2.hasNext()) {
            T t = var2.next();
            dest.add(t);
         }

      }
   }

   public void clear() {
      Iterator var1 = this.iterator();

      while(var1.hasNext()) {
         T t = var1.next();
         this.remove(t);
      }

   }

   public void union(FlowSet<T> other) {
      if (this != other) {
         this.union(other, this);
      }
   }

   public void union(FlowSet<T> other, FlowSet<T> dest) {
      if (dest != this && dest != other) {
         dest.clear();
      }

      Iterator var3;
      Object t;
      if (dest != null && dest != this) {
         var3 = this.iterator();

         while(var3.hasNext()) {
            t = var3.next();
            dest.add(t);
         }
      }

      if (other != null && dest != other) {
         var3 = other.iterator();

         while(var3.hasNext()) {
            t = var3.next();
            dest.add(t);
         }
      }

   }

   public void intersection(FlowSet<T> other) {
      if (this != other) {
         this.intersection(other, this);
      }
   }

   public void intersection(FlowSet<T> other, FlowSet<T> dest) {
      if (dest != this || dest != other) {
         FlowSet<T> elements = null;
         FlowSet<T> flowSet = null;
         if (dest == this) {
            elements = this;
            flowSet = other;
         } else {
            elements = other;
            flowSet = this;
         }

         dest.clear();
         Iterator var5 = ((FlowSet)elements).iterator();

         while(var5.hasNext()) {
            T t = var5.next();
            if (((FlowSet)flowSet).contains(t)) {
               dest.add(t);
            }
         }

      }
   }

   public void difference(FlowSet<T> other) {
      this.difference(other, this);
   }

   public void difference(FlowSet<T> other, FlowSet<T> dest) {
      if (dest == this && dest == other) {
         dest.clear();
      } else {
         FlowSet<T> flowSet = other == dest ? other.clone() : other;
         dest.clear();
         Iterator var4 = this.iterator();

         while(var4.hasNext()) {
            T t = var4.next();
            if (!flowSet.contains(t)) {
               dest.add(t);
            }
         }

      }
   }

   public abstract boolean isEmpty();

   public abstract int size();

   public abstract void add(T var1);

   public void add(T obj, FlowSet<T> dest) {
      if (dest != this) {
         this.copy(dest);
      }

      dest.add(obj);
   }

   public abstract void remove(T var1);

   public void remove(T obj, FlowSet<T> dest) {
      if (dest != this) {
         this.copy(dest);
      }

      dest.remove(obj);
   }

   public boolean isSubSet(FlowSet<T> other) {
      if (other == this) {
         return true;
      } else {
         Iterator var2 = other.iterator();

         Object t;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            t = var2.next();
         } while(this.contains(t));

         return false;
      }
   }

   public abstract boolean contains(T var1);

   public abstract Iterator<T> iterator();

   public abstract List<T> toList();

   public boolean equals(Object o) {
      if (!(o instanceof FlowSet)) {
         return false;
      } else {
         FlowSet<T> other = (FlowSet)o;
         if (this.size() != other.size()) {
            return false;
         } else {
            Iterator var3 = this.iterator();

            Object t;
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               t = var3.next();
            } while(other.contains(t));

            return false;
         }
      }
   }

   public int hashCode() {
      int result = 1;

      Object t;
      for(Iterator var2 = this.iterator(); var2.hasNext(); result += t.hashCode()) {
         t = var2.next();
      }

      return result;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("{");
      boolean isFirst = true;
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         T t = var3.next();
         if (!isFirst) {
            buffer.append(", ");
         }

         isFirst = false;
         buffer.append(t);
      }

      buffer.append("}");
      return buffer.toString();
   }
}
