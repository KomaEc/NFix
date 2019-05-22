package org.apache.commons.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.list.FixedSizeList;
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.list.PredicatedList;
import org.apache.commons.collections.list.SynchronizedList;
import org.apache.commons.collections.list.TransformedList;
import org.apache.commons.collections.list.TypedList;
import org.apache.commons.collections.list.UnmodifiableList;

public class ListUtils {
   public static final List EMPTY_LIST;

   public static List intersection(List list1, List list2) {
      ArrayList result = new ArrayList();
      Iterator iterator = list2.iterator();

      while(iterator.hasNext()) {
         Object o = iterator.next();
         if (list1.contains(o)) {
            result.add(o);
         }
      }

      return result;
   }

   public static List subtract(List list1, List list2) {
      ArrayList result = new ArrayList(list1);
      Iterator iterator = list2.iterator();

      while(iterator.hasNext()) {
         result.remove(iterator.next());
      }

      return result;
   }

   public static List sum(List list1, List list2) {
      return subtract(union(list1, list2), intersection(list1, list2));
   }

   public static List union(List list1, List list2) {
      ArrayList result = new ArrayList(list1);
      result.addAll(list2);
      return result;
   }

   public static boolean isEqualList(Collection list1, Collection list2) {
      if (list1 == list2) {
         return true;
      } else if (list1 != null && list2 != null && list1.size() == list2.size()) {
         Iterator it1 = list1.iterator();
         Iterator it2 = list2.iterator();
         Object obj1 = null;
         Object obj2 = null;

         while(it1.hasNext() && it2.hasNext()) {
            obj1 = it1.next();
            obj2 = it2.next();
            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
               return false;
            }
         }

         return !it1.hasNext() && !it2.hasNext();
      } else {
         return false;
      }
   }

   public static int hashCodeForList(Collection list) {
      if (list == null) {
         return 0;
      } else {
         int hashCode = 1;
         Iterator it = list.iterator();

         for(Object obj = null; it.hasNext(); hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode())) {
            obj = it.next();
         }

         return hashCode;
      }
   }

   public static List retainAll(Collection collection, Collection retain) {
      List list = new ArrayList(Math.min(collection.size(), retain.size()));
      Iterator iter = collection.iterator();

      while(iter.hasNext()) {
         Object obj = iter.next();
         if (retain.contains(obj)) {
            list.add(obj);
         }
      }

      return list;
   }

   public static List removeAll(Collection collection, Collection remove) {
      List list = new ArrayList();
      Iterator iter = collection.iterator();

      while(iter.hasNext()) {
         Object obj = iter.next();
         if (!remove.contains(obj)) {
            list.add(obj);
         }
      }

      return list;
   }

   public static List synchronizedList(List list) {
      return SynchronizedList.decorate(list);
   }

   public static List unmodifiableList(List list) {
      return UnmodifiableList.decorate(list);
   }

   public static List predicatedList(List list, Predicate predicate) {
      return PredicatedList.decorate(list, predicate);
   }

   public static List typedList(List list, Class type) {
      return TypedList.decorate(list, type);
   }

   public static List transformedList(List list, Transformer transformer) {
      return TransformedList.decorate(list, transformer);
   }

   public static List lazyList(List list, Factory factory) {
      return LazyList.decorate(list, factory);
   }

   public static List fixedSizeList(List list) {
      return FixedSizeList.decorate(list);
   }

   static {
      EMPTY_LIST = Collections.EMPTY_LIST;
   }
}
