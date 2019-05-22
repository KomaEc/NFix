package polyglot.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TypedList implements List, Serializable, Cloneable {
   static final long serialVersionUID = -1390984392613203018L;
   private Class allowed_type;
   private boolean immutable;
   private List backing_list;

   public static TypedList copy(List list, Class c, boolean immutable) {
      return new TypedList(new ArrayList(list), c, immutable);
   }

   public static TypedList copyAndCheck(List list, Class c, boolean immutable) {
      if (c != null) {
         check(list, c);
      }

      return copy(list, c, immutable);
   }

   public static void check(List list, Class c) {
      Iterator i = list.iterator();

      Object o;
      do {
         if (!i.hasNext()) {
            return;
         }

         o = i.next();
      } while(o == null || c.isAssignableFrom(o.getClass()));

      throw new UnsupportedOperationException("Tried to add a " + o.getClass().getName() + " to a list of type " + c.getName());
   }

   public TypedList(List list, Class c, boolean immutable) {
      this.immutable = immutable;
      this.allowed_type = c;
      this.backing_list = list;
   }

   public Class getAllowedType() {
      return this.allowed_type;
   }

   public TypedList copy() {
      return (TypedList)this.clone();
   }

   public Object clone() {
      try {
         TypedList l = (TypedList)super.clone();
         l.backing_list = new ArrayList(this.backing_list);
         return l;
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone weirdness.");
      }
   }

   public void add(int idx, Object o) {
      this.tryIns(o);
      this.backing_list.add(idx, o);
   }

   public boolean add(Object o) {
      this.tryIns(o);
      return this.backing_list.add(o);
   }

   public boolean addAll(int idx, Collection coll) {
      this.tryIns(coll);
      return this.backing_list.addAll(idx, coll);
   }

   public boolean addAll(Collection coll) {
      this.tryIns(coll);
      return this.backing_list.addAll(coll);
   }

   public ListIterator listIterator() {
      return new TypedListIterator(this.backing_list.listIterator(), this.allowed_type, this.immutable);
   }

   public ListIterator listIterator(int idx) {
      return new TypedListIterator(this.backing_list.listIterator(idx), this.allowed_type, this.immutable);
   }

   public Object set(int idx, Object o) {
      this.tryIns(o);
      return this.backing_list.set(idx, o);
   }

   public List subList(int from, int to) {
      return new TypedList(this.backing_list.subList(from, to), this.allowed_type, this.immutable);
   }

   public void clear() {
      this.tryMod();
      this.backing_list.clear();
   }

   public boolean contains(Object o) {
      return this.backing_list.contains(o);
   }

   public boolean containsAll(Collection coll) {
      return this.backing_list.containsAll(coll);
   }

   public boolean equals(Object o) {
      return this.backing_list.equals(o);
   }

   public Object get(int idx) {
      return this.backing_list.get(idx);
   }

   public int hashCode() {
      return this.backing_list.hashCode();
   }

   public int indexOf(Object o) {
      return this.backing_list.indexOf(o);
   }

   public boolean isEmpty() {
      return this.backing_list.isEmpty();
   }

   public Iterator iterator() {
      return this.listIterator();
   }

   public int lastIndexOf(Object o) {
      return this.backing_list.lastIndexOf(o);
   }

   public Object remove(int idx) {
      this.tryMod();
      return this.backing_list.remove(idx);
   }

   public boolean remove(Object o) {
      this.tryMod();
      return this.backing_list.remove(o);
   }

   public boolean removeAll(Collection coll) {
      this.tryMod();
      return this.backing_list.removeAll(coll);
   }

   public boolean retainAll(Collection coll) {
      this.tryMod();
      return this.backing_list.retainAll(coll);
   }

   public int size() {
      return this.backing_list.size();
   }

   public Object[] toArray() {
      return this.backing_list.toArray();
   }

   public Object[] toArray(Object[] oa) {
      return this.backing_list.toArray(oa);
   }

   public String toString() {
      return this.backing_list.toString();
   }

   private final void tryIns(Object o) {
      if (this.immutable) {
         throw new UnsupportedOperationException("Add to an immutable TypedListIterator");
      } else if (this.allowed_type != null && !this.allowed_type.isAssignableFrom(o.getClass())) {
         String why = "Tried to add a " + o.getClass().getName() + " to a list of type " + this.allowed_type.getName();
         throw new UnsupportedOperationException(why);
      }
   }

   private final void tryIns(Collection coll) {
      if (this.immutable) {
         throw new UnsupportedOperationException("Add to an immutable TypedListIterator");
      } else {
         Iterator it = coll.iterator();

         Object o;
         do {
            if (!it.hasNext()) {
               return;
            }

            o = it.next();
         } while(this.allowed_type == null || this.allowed_type.isAssignableFrom(o.getClass()));

         String why = "Tried to add a " + o.getClass().getName() + " to a list of type " + this.allowed_type.getName();
         throw new UnsupportedOperationException(why);
      }
   }

   private final void tryMod() {
      if (this.immutable) {
         throw new UnsupportedOperationException("Change to an immutable TypedListIterator");
      }
   }
}
