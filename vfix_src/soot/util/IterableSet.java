package soot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class IterableSet<T> extends HashChain<T> implements Set<T> {
   public IterableSet(Collection<T> c) {
      this.addAll(c);
   }

   public IterableSet() {
   }

   public boolean add(T o) {
      if (o == null) {
         throw new IllegalArgumentException("Cannot add \"null\" to an IterableSet.");
      } else {
         return this.contains(o) ? false : super.add(o);
      }
   }

   public boolean remove(Object o) {
      return o != null && this.contains(o) ? super.remove(o) : false;
   }

   public boolean equals(Object o) {
      if (o == null) {
         return false;
      } else if (this == o) {
         return true;
      } else if (!(o instanceof IterableSet)) {
         return false;
      } else {
         IterableSet<T> other = (IterableSet)o;
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
      int code = 23 * this.size();

      Object t;
      for(Iterator var2 = this.iterator(); var2.hasNext(); code += t.hashCode()) {
         t = var2.next();
      }

      return code;
   }

   public Object clone() {
      IterableSet<T> s = new IterableSet();
      s.addAll(this);
      return s;
   }

   public boolean isSubsetOf(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
      } else if (this.size() > other.size()) {
         return false;
      } else {
         Iterator var2 = this.iterator();

         Object t;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            t = var2.next();
         } while(other.contains(t));

         return false;
      }
   }

   public boolean isSupersetOf(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
      } else if (this.size() < other.size()) {
         return false;
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

   public boolean isStrictSubsetOf(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
      } else {
         return this.size() >= other.size() ? false : this.isSubsetOf(other);
      }
   }

   public boolean isStrictSupersetOf(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set compare an IterableSet with \"null\".");
      } else {
         return this.size() <= other.size() ? false : this.isSupersetOf(other);
      }
   }

   public boolean intersects(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set intersect an IterableSet with \"null\".");
      } else {
         Iterator var2;
         Object t;
         if (other.size() < this.size()) {
            var2 = other.iterator();

            while(var2.hasNext()) {
               t = var2.next();
               if (this.contains(t)) {
                  return true;
               }
            }
         } else {
            var2 = this.iterator();

            while(var2.hasNext()) {
               t = var2.next();
               if (other.contains(t)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public IterableSet<T> intersection(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set intersect an IterableSet with \"null\".");
      } else {
         IterableSet<T> c = new IterableSet();
         Iterator var3;
         Object t;
         if (other.size() < this.size()) {
            var3 = other.iterator();

            while(var3.hasNext()) {
               t = var3.next();
               if (this.contains(t)) {
                  c.add(t);
               }
            }
         } else {
            var3 = this.iterator();

            while(var3.hasNext()) {
               t = var3.next();
               if (other.contains(t)) {
                  c.add(t);
               }
            }
         }

         return c;
      }
   }

   public IterableSet<T> union(IterableSet<T> other) {
      if (other == null) {
         throw new IllegalArgumentException("Cannot set union an IterableSet with \"null\".");
      } else {
         IterableSet<T> c = new IterableSet();
         c.addAll(this);
         c.addAll(other);
         return c;
      }
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         T t = var2.next();
         b.append(t.toString());
         b.append("\n");
      }

      return b.toString();
   }

   public UnmodifiableIterableSet<T> asUnmodifiable() {
      return new UnmodifiableIterableSet(this);
   }
}
