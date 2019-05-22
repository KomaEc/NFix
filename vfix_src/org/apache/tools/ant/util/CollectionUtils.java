package org.apache.tools.ant.util;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

public class CollectionUtils {
   /** @deprecated */
   public static boolean equals(Vector v1, Vector v2) {
      if (v1 == v2) {
         return true;
      } else {
         return v1 != null && v2 != null ? v1.equals(v2) : false;
      }
   }

   /** @deprecated */
   public static boolean equals(Dictionary d1, Dictionary d2) {
      if (d1 == d2) {
         return true;
      } else if (d1 != null && d2 != null) {
         if (d1.size() != d2.size()) {
            return false;
         } else {
            Enumeration e1 = d1.keys();

            Object value1;
            Object value2;
            do {
               if (!e1.hasMoreElements()) {
                  return true;
               }

               Object key = e1.nextElement();
               value1 = d1.get(key);
               value2 = d2.get(key);
            } while(value2 != null && value1.equals(value2));

            return false;
         }
      } else {
         return false;
      }
   }

   /** @deprecated */
   public static void putAll(Dictionary m1, Dictionary m2) {
      Enumeration it = m2.keys();

      while(it.hasMoreElements()) {
         Object key = it.nextElement();
         m1.put(key, m2.get(key));
      }

   }

   public static Enumeration append(Enumeration e1, Enumeration e2) {
      return new CollectionUtils.CompoundEnumeration(e1, e2);
   }

   public static Enumeration asEnumeration(final Iterator iter) {
      return new Enumeration() {
         public boolean hasMoreElements() {
            return iter.hasNext();
         }

         public Object nextElement() {
            return iter.next();
         }
      };
   }

   public static Iterator asIterator(final Enumeration e) {
      return new Iterator() {
         public boolean hasNext() {
            return e.hasMoreElements();
         }

         public Object next() {
            return e.nextElement();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   private static final class CompoundEnumeration implements Enumeration {
      private final Enumeration e1;
      private final Enumeration e2;

      public CompoundEnumeration(Enumeration e1, Enumeration e2) {
         this.e1 = e1;
         this.e2 = e2;
      }

      public boolean hasMoreElements() {
         return this.e1.hasMoreElements() || this.e2.hasMoreElements();
      }

      public Object nextElement() throws NoSuchElementException {
         return this.e1.hasMoreElements() ? this.e1.nextElement() : this.e2.nextElement();
      }
   }

   public static final class EmptyEnumeration implements Enumeration {
      public boolean hasMoreElements() {
         return false;
      }

      public Object nextElement() throws NoSuchElementException {
         throw new NoSuchElementException();
      }
   }
}
