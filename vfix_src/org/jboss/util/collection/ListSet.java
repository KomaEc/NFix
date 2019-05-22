package org.jboss.util.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jboss.util.NullArgumentException;

public class ListSet extends AbstractSet implements Set, Cloneable, Serializable {
   private static final long serialVersionUID = 7333619218072079496L;
   protected final List list;

   public ListSet(List list) {
      if (list == null) {
         throw new NullArgumentException("list");
      } else {
         int size = list.size();

         for(int i = 0; i < size; ++i) {
            Object obj = list.get(i);
            if (list.indexOf(obj) != list.lastIndexOf(obj)) {
               throw new IllegalArgumentException("list contains duplicate entries");
            }
         }

         this.list = list;
      }
   }

   public ListSet() {
      this((List)(new ArrayList()));
   }

   public ListSet(Collection elements) {
      this((List)(new ArrayList(elements)));
   }

   public List getList() {
      return this.list;
   }

   public int size() {
      return this.list.size();
   }

   public Iterator iterator() {
      return this.list.iterator();
   }

   public boolean add(Object obj) {
      boolean added = false;
      if (!this.list.contains(obj)) {
         added = this.list.add(obj);
      }

      return added;
   }

   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   public boolean contains(Object obj) {
      return this.list.contains(obj);
   }

   public boolean remove(Object obj) {
      return this.list.remove(obj);
   }

   public void clear() {
      this.list.clear();
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalError();
      }
   }
}
