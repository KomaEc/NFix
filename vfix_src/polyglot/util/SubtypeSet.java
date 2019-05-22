package polyglot.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import polyglot.types.Type;
import polyglot.types.TypeSystem;

public class SubtypeSet implements Set {
   Vector v;
   TypeSystem ts;
   Type topType;

   public SubtypeSet(TypeSystem ts) {
      this((Type)ts.Object());
   }

   public SubtypeSet(Type top) {
      this.v = new Vector();
      this.ts = top.typeSystem();
      this.topType = top;
   }

   public SubtypeSet(SubtypeSet s) {
      this.v = new Vector(s.v);
      this.ts = s.ts;
      this.topType = s.topType;
   }

   public SubtypeSet(TypeSystem ts, Collection c) {
      this(ts);
      this.addAll(c);
   }

   public SubtypeSet(Type top, Collection c) {
      this(top);
      this.addAll(c);
   }

   public boolean add(Object o) {
      if (o == null) {
         return false;
      } else {
         if (o instanceof Type) {
            Type type = (Type)o;
            if (this.ts.isSubtype(type, this.topType)) {
               boolean haveToAdd = true;
               Iterator i = this.v.iterator();

               while(i.hasNext()) {
                  Type t = (Type)i.next();
                  if (this.ts.descendsFrom(t, type)) {
                     i.remove();
                  }

                  if (this.ts.isSubtype(type, t)) {
                     haveToAdd = false;
                     break;
                  }
               }

               if (haveToAdd) {
                  this.v.add(type);
               }

               return haveToAdd;
            }
         }

         throw new InternalCompilerError("Can only add " + this.topType + "s to the set. Got a " + o);
      }
   }

   public boolean addAll(Collection c) {
      if (c == null) {
         return false;
      } else {
         boolean changed = false;

         for(Iterator i = c.iterator(); i.hasNext(); changed |= this.add(i.next())) {
         }

         return changed;
      }
   }

   public void clear() {
      this.v.clear();
   }

   public boolean contains(Object o) {
      if (o instanceof Type) {
         Type type = (Type)o;
         Iterator i = this.v.iterator();

         while(i.hasNext()) {
            Type t = (Type)i.next();
            if (this.ts.isSubtype(type, t)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean containsSubtype(Type type) {
      Iterator i = this.v.iterator();

      Type t;
      do {
         if (!i.hasNext()) {
            return false;
         }

         t = (Type)i.next();
      } while(!this.ts.isSubtype(type, t) && !this.ts.isSubtype(t, type));

      return true;
   }

   public boolean containsAll(Collection c) {
      Iterator i = c.iterator();

      do {
         if (!i.hasNext()) {
            return true;
         }
      } while(this.contains(i.next()));

      return false;
   }

   public boolean isEmpty() {
      return this.v.isEmpty();
   }

   public Iterator iterator() {
      return this.v.iterator();
   }

   public boolean remove(Object o) {
      Type type = (Type)o;
      boolean removed = false;
      Iterator i = this.v.iterator();

      while(i.hasNext()) {
         Type t = (Type)i.next();
         if (this.ts.isSubtype(t, type)) {
            removed = true;
            i.remove();
         }
      }

      return removed;
   }

   public boolean removeAll(Collection c) {
      boolean changed = false;

      Object o;
      for(Iterator i = c.iterator(); i.hasNext(); changed |= this.remove(o)) {
         o = i.next();
      }

      return changed;
   }

   public boolean retainAll(Collection c) {
      throw new UnsupportedOperationException("Not supported");
   }

   public int size() {
      return this.v.size();
   }

   public Object[] toArray() {
      return this.v.toArray();
   }

   public Object[] toArray(Object[] a) {
      return this.v.toArray(a);
   }

   public String toString() {
      return this.v.toString();
   }
}
