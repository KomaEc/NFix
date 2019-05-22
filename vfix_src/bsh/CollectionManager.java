package bsh;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CollectionManager {
   private static CollectionManager manager;

   public static synchronized CollectionManager getCollectionManager() {
      if (manager == null && Capabilities.classExists("java.util.Collection")) {
         try {
            Class var0 = Class.forName("bsh.collection.CollectionManagerImpl");
            manager = (CollectionManager)var0.newInstance();
         } catch (Exception var2) {
            Interpreter.debug("unable to load CollectionManagerImpl: " + var2);
         }
      }

      if (manager == null) {
         manager = new CollectionManager();
      }

      return manager;
   }

   public boolean isBshIterable(Object var1) {
      try {
         this.getBshIterator(var1);
         return true;
      } catch (IllegalArgumentException var3) {
         return false;
      }
   }

   public BshIterator getBshIterator(Object var1) throws IllegalArgumentException {
      return new CollectionManager.BasicBshIterator(var1);
   }

   public boolean isMap(Object var1) {
      return var1 instanceof Hashtable;
   }

   public Object getFromMap(Object var1, Object var2) {
      return ((Hashtable)var1).get(var2);
   }

   public Object putInMap(Object var1, Object var2, Object var3) {
      return ((Hashtable)var1).put(var2, var3);
   }

   public static class BasicBshIterator implements BshIterator {
      Enumeration enumeration;

      public BasicBshIterator(Object var1) {
         this.enumeration = this.createEnumeration(var1);
      }

      protected Enumeration createEnumeration(final Object var1) {
         if (var1 == null) {
            throw new NullPointerException("Object arguments passed to the BasicBshIterator constructor cannot be null.");
         } else if (var1 instanceof Enumeration) {
            return (Enumeration)var1;
         } else if (var1 instanceof Vector) {
            return ((Vector)var1).elements();
         } else if (var1.getClass().isArray()) {
            return new Enumeration() {
               int index = 0;
               int length = Array.getLength(var1);

               public Object nextElement() {
                  return Array.get(var1, this.index++);
               }

               public boolean hasMoreElements() {
                  return this.index < this.length;
               }
            };
         } else if (var1 instanceof String) {
            return this.createEnumeration(((String)var1).toCharArray());
         } else if (var1 instanceof StringBuffer) {
            return this.createEnumeration(var1.toString().toCharArray());
         } else {
            throw new IllegalArgumentException("Cannot enumerate object of type " + var1.getClass());
         }
      }

      public Object next() {
         return this.enumeration.nextElement();
      }

      public boolean hasNext() {
         return this.enumeration.hasMoreElements();
      }
   }
}
