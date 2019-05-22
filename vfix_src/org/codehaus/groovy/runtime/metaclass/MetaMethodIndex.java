package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.MetaMethod;
import java.util.NoSuchElementException;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.util.FastArray;
import org.codehaus.groovy.util.SingleKeyHashMap;

public class MetaMethodIndex {
   public SingleKeyHashMap methodHeaders = new SingleKeyHashMap();
   protected MetaMethodIndex.Entry[] table;
   protected static final int DEFAULT_CAPACITY = 32;
   protected static final int MINIMUM_CAPACITY = 4;
   protected static final int MAXIMUM_CAPACITY = 268435456;
   protected int size;
   protected transient int threshold;

   public MetaMethodIndex(CachedClass theCachedClass) {
      this.init(32);
      CachedClass last = null;
      if (!theCachedClass.isInterface()) {
         for(CachedClass c = theCachedClass; c != null; c = c.getCachedSuperClass()) {
            SingleKeyHashMap.Entry e = this.methodHeaders.getOrPut(c.getTheClass());
            e.value = new MetaMethodIndex.Header(c.getTheClass(), last == null ? null : last.getTheClass());
            last = c;
         }
      } else {
         SingleKeyHashMap.Entry e = this.methodHeaders.getOrPut(Object.class);
         e.value = new MetaMethodIndex.Header(Object.class, theCachedClass.getTheClass());
      }

   }

   public static int hash(int h) {
      h += ~(h << 9);
      h ^= h >>> 14;
      h += h << 4;
      h ^= h >>> 10;
      return h;
   }

   public int size() {
      return this.size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void clear() {
      Object[] tab = this.table;

      for(int i = 0; i < tab.length; ++i) {
         tab[i] = null;
      }

      this.size = 0;
   }

   public void init(int initCapacity) {
      this.threshold = initCapacity * 6 / 8;
      this.table = new MetaMethodIndex.Entry[initCapacity];
   }

   public void resize(int newLength) {
      MetaMethodIndex.Entry[] oldTable = this.table;
      int oldLength = this.table.length;
      MetaMethodIndex.Entry[] newTable = new MetaMethodIndex.Entry[newLength];

      MetaMethodIndex.Entry next;
      for(int j = 0; j < oldLength; ++j) {
         for(MetaMethodIndex.Entry e = oldTable[j]; e != null; e = next) {
            next = e.nextHashEntry;
            int index = e.hash & newLength - 1;
            e.nextHashEntry = newTable[index];
            newTable[index] = e;
         }
      }

      this.table = newTable;
      this.threshold = 6 * newLength / 8;
   }

   public MetaMethodIndex.Entry[] getTable() {
      return this.table;
   }

   public MetaMethodIndex.EntryIterator getEntrySetIterator() {
      return new MetaMethodIndex.EntryIterator() {
         MetaMethodIndex.Entry next;
         int index;
         MetaMethodIndex.Entry current;

         {
            MetaMethodIndex.Entry[] t = MetaMethodIndex.this.table;
            int i = t.length;
            MetaMethodIndex.Entry n = null;
            if (MetaMethodIndex.this.size != 0) {
               while(i > 0) {
                  --i;
                  if ((n = t[i]) != null) {
                     break;
                  }
               }
            }

            this.next = n;
            this.index = i;
         }

         public boolean hasNext() {
            return this.next != null;
         }

         public MetaMethodIndex.Entry next() {
            return this.nextEntry();
         }

         MetaMethodIndex.Entry nextEntry() {
            MetaMethodIndex.Entry e = this.next;
            if (e == null) {
               throw new NoSuchElementException();
            } else {
               MetaMethodIndex.Entry n = e.nextHashEntry;
               MetaMethodIndex.Entry[] t = MetaMethodIndex.this.table;

               int i;
               for(i = this.index; n == null && i > 0; n = t[i]) {
                  --i;
               }

               this.index = i;
               this.next = n;
               return this.current = e;
            }
         }
      };
   }

   public final MetaMethodIndex.Entry getMethods(Class cls, String name) {
      int h = hash(31 * cls.hashCode() + name.hashCode());

      for(MetaMethodIndex.Entry e = this.table[h & this.table.length - 1]; e != null; e = e.nextHashEntry) {
         if (e.hash == h && cls == e.cls && (e.name == name || e.name.equals(name))) {
            return e;
         }
      }

      return null;
   }

   public MetaMethodIndex.Entry getOrPutMethods(String name, MetaMethodIndex.Header header) {
      Class cls = header.cls;
      int h = hash(header.clsHashCode31 + name.hashCode());
      MetaMethodIndex.Entry[] t = this.table;
      int index = h & t.length - 1;

      for(MetaMethodIndex.Entry e = t[index]; e != null; e = e.nextHashEntry) {
         if (e.hash == h && cls == e.cls && (e.name == name || e.name.equals(name))) {
            return e;
         }
      }

      MetaMethodIndex.Entry entry = new MetaMethodIndex.Entry();
      entry.nextHashEntry = t[index];
      entry.hash = h;
      entry.name = name.intern();
      entry.cls = cls;
      t[index] = entry;
      entry.nextClassEntry = header.head;
      header.head = entry;
      if (++this.size == this.threshold) {
         this.resize(2 * t.length);
      }

      return entry;
   }

   public MetaMethodIndex.Header getHeader(Class cls) {
      SingleKeyHashMap.Entry head = this.methodHeaders.getOrPut(cls);
      if (head.value == null) {
         head.value = new MetaMethodIndex.Header(cls);
      }

      MetaMethodIndex.Header header = (MetaMethodIndex.Header)head.value;
      return header;
   }

   public void copyNonPrivateMethods(Class from, Class to) {
      this.copyNonPrivateMethods(this.getHeader(from), this.getHeader(to));
   }

   public void copyNonPrivateMethods(MetaMethodIndex.Header from, MetaMethodIndex.Header to) {
      for(MetaMethodIndex.Entry e = from.head; e != null; e = e.nextClassEntry) {
         this.copyNonPrivateMethods(e, to);
      }

   }

   public void copyAllMethodsToSuper(MetaMethodIndex.Header from, MetaMethodIndex.Header to) {
      for(MetaMethodIndex.Entry e = from.head; e != null; e = e.nextClassEntry) {
         this.copyAllMethodsToSuper(e, to);
      }

   }

   public void copyNonPrivateMethodsFromSuper(MetaMethodIndex.Header from) {
      for(MetaMethodIndex.Entry e = from.head; e != null; e = e.nextClassEntry) {
         this.copyNonPrivateMethodsFromSuper(e);
      }

   }

   private void copyNonPrivateMethods(MetaMethodIndex.Entry from, MetaMethodIndex.Header to) {
      Object oldListOrMethod = from.methods;
      MetaMethodIndex.Entry e;
      if (oldListOrMethod instanceof FastArray) {
         FastArray oldList = (FastArray)oldListOrMethod;
         e = null;
         int len1 = oldList.size();
         Object[] list = oldList.getArray();

         for(int j = 0; j != len1; ++j) {
            MetaMethod method = (MetaMethod)list[j];
            if (!method.isPrivate()) {
               if (e == null) {
                  e = this.getOrPutMethods(from.name, to);
               }

               e.methods = this.addMethodToList(e.methods, method);
            }
         }
      } else {
         MetaMethod method = (MetaMethod)oldListOrMethod;
         if (!method.isPrivate()) {
            e = this.getOrPutMethods(from.name, to);
            e.methods = this.addMethodToList(e.methods, method);
         }
      }

   }

   private void copyAllMethodsToSuper(MetaMethodIndex.Entry from, MetaMethodIndex.Header to) {
      Object oldListOrMethod = from.methods;
      MetaMethodIndex.Entry e;
      if (oldListOrMethod instanceof FastArray) {
         FastArray oldList = (FastArray)oldListOrMethod;
         e = null;
         int len1 = oldList.size();
         Object[] list = oldList.getArray();

         for(int j = 0; j != len1; ++j) {
            MetaMethod method = (MetaMethod)list[j];
            if (e == null) {
               e = this.getOrPutMethods(from.name, to);
            }

            e.methodsForSuper = this.addMethodToList(e.methodsForSuper, method);
         }
      } else {
         MetaMethod method = (MetaMethod)oldListOrMethod;
         e = this.getOrPutMethods(from.name, to);
         e.methodsForSuper = this.addMethodToList(e.methodsForSuper, method);
      }

   }

   private void copyNonPrivateMethodsFromSuper(MetaMethodIndex.Entry e) {
      Object oldListOrMethod = e.methodsForSuper;
      if (oldListOrMethod != null) {
         if (oldListOrMethod instanceof FastArray) {
            FastArray oldList = (FastArray)oldListOrMethod;
            int len1 = oldList.size();
            Object[] list = oldList.getArray();

            for(int j = 0; j != len1; ++j) {
               MetaMethod method = (MetaMethod)list[j];
               if (!method.isPrivate()) {
                  e.methods = this.addMethodToList(e.methods, method);
               }
            }
         } else {
            MetaMethod method = (MetaMethod)oldListOrMethod;
            if (!method.isPrivate()) {
               e.methods = this.addMethodToList(e.methods, method);
            }
         }

      }
   }

   public void copyNonPrivateMethodsDown(Class from, Class to) {
      this.copyNonPrivateNonNewMetaMethods(this.getHeader(from), this.getHeader(to));
   }

   public void copyNonPrivateNonNewMetaMethods(MetaMethodIndex.Header from, MetaMethodIndex.Header to) {
      for(MetaMethodIndex.Entry e = from.head; e != null; e = e.nextClassEntry) {
         this.copyNonPrivateNonNewMetaMethods(e, to);
      }

   }

   private void copyNonPrivateNonNewMetaMethods(MetaMethodIndex.Entry from, MetaMethodIndex.Header to) {
      Object oldListOrMethod = from.methods;
      if (oldListOrMethod != null) {
         MetaMethodIndex.Entry e;
         if (oldListOrMethod instanceof FastArray) {
            FastArray oldList = (FastArray)oldListOrMethod;
            e = null;
            int len1 = oldList.size();
            Object[] list = oldList.getArray();

            for(int j = 0; j != len1; ++j) {
               MetaMethod method = (MetaMethod)list[j];
               if (!(method instanceof NewMetaMethod) && !method.isPrivate()) {
                  if (e == null) {
                     e = this.getOrPutMethods(from.name, to);
                  }

                  e.methods = this.addMethodToList(e.methods, method);
               }
            }
         } else {
            MetaMethod method = (MetaMethod)oldListOrMethod;
            if (method instanceof NewMetaMethod || method.isPrivate()) {
               return;
            }

            e = this.getOrPutMethods(from.name, to);
            e.methods = this.addMethodToList(e.methods, method);
         }

      }
   }

   public Object addMethodToList(Object o, MetaMethod method) {
      if (o == null) {
         return method;
      } else if (o instanceof MetaMethod) {
         MetaMethod match = (MetaMethod)o;
         if (!this.isMatchingMethod(match, method)) {
            FastArray list = new FastArray(2);
            list.add(match);
            list.add(method);
            return list;
         } else {
            if (!match.isPrivate() && (this.isNonRealMethod(match) || !match.getDeclaringClass().isInterface() || method.getDeclaringClass().isInterface())) {
               CachedClass methodC = method.getDeclaringClass();
               CachedClass matchC = match.getDeclaringClass();
               if (methodC == matchC) {
                  if (this.isNonRealMethod(method)) {
                     return method;
                  }
               } else if (!methodC.isAssignableFrom(matchC.getTheClass())) {
                  return method;
               }
            }

            return o;
         }
      } else {
         if (o instanceof FastArray) {
            FastArray list = (FastArray)o;
            int found = this.findMatchingMethod(list, method);
            if (found == -1) {
               list.add(method);
            } else {
               MetaMethod match = (MetaMethod)list.get(found);
               if (match == method) {
                  return o;
               }

               if (!match.isPrivate() && (this.isNonRealMethod(match) || !match.getDeclaringClass().isInterface() || method.getDeclaringClass().isInterface())) {
                  CachedClass methodC = method.getDeclaringClass();
                  CachedClass matchC = match.getDeclaringClass();
                  if (methodC == matchC) {
                     if (this.isNonRealMethod(method)) {
                        list.set(found, method);
                     }
                  } else if (!methodC.isAssignableFrom(matchC.getTheClass())) {
                     list.set(found, method);
                  }
               }
            }
         }

         return o;
      }
   }

   private boolean isNonRealMethod(MetaMethod method) {
      return method instanceof NewInstanceMetaMethod || method instanceof NewStaticMetaMethod || method instanceof ClosureMetaMethod || method instanceof GeneratedMetaMethod || method instanceof ClosureStaticMetaMethod || method instanceof MixinInstanceMetaMethod;
   }

   private boolean isMatchingMethod(MetaMethod aMethod, MetaMethod method) {
      if (aMethod == method) {
         return true;
      } else {
         CachedClass[] params1 = aMethod.getParameterTypes();
         CachedClass[] params2 = method.getParameterTypes();
         if (params1.length != params2.length) {
            return false;
         } else {
            boolean matches = true;

            for(int i = 0; i < params1.length; ++i) {
               if (params1[i] != params2[i]) {
                  matches = false;
                  break;
               }
            }

            return matches;
         }
      }
   }

   private int findMatchingMethod(FastArray list, MetaMethod method) {
      int len = list.size();
      Object[] data = list.getArray();

      for(int j = 0; j != len; ++j) {
         MetaMethod aMethod = (MetaMethod)data[j];
         if (this.isMatchingMethod(aMethod, method)) {
            return j;
         }
      }

      return -1;
   }

   public void copyMethodsToSuper() {
      MetaMethodIndex.Entry[] table = this.table;
      int length = table.length;

      for(int j = 0; j < length; ++j) {
         for(MetaMethodIndex.Entry e = table[j]; e != null; e = e.nextHashEntry) {
            if (e.methods instanceof FastArray) {
               e.methodsForSuper = ((FastArray)e.methods).copy();
            } else {
               e.methodsForSuper = e.methods;
            }
         }
      }

   }

   public void copy(Class c, MetaMethodIndex.Header index) {
      this.copy(this.getHeader(c), index);
   }

   public void copy(MetaMethodIndex.Header from, MetaMethodIndex.Header to) {
      for(MetaMethodIndex.Entry e = from.head; e != null; e = e.nextClassEntry) {
         this.copyAllMethods(e, to);
      }

   }

   private void copyAllMethods(MetaMethodIndex.Entry from, MetaMethodIndex.Header to) {
      Object oldListOrMethod = from.methods;
      MetaMethodIndex.Entry e;
      if (oldListOrMethod instanceof FastArray) {
         FastArray oldList = (FastArray)oldListOrMethod;
         e = null;
         int len1 = oldList.size();
         Object[] list = oldList.getArray();

         for(int j = 0; j != len1; ++j) {
            MetaMethod method = (MetaMethod)list[j];
            if (e == null) {
               e = this.getOrPutMethods(from.name, to);
            }

            e.methods = this.addMethodToList(e.methods, method);
         }
      } else {
         MetaMethod method = (MetaMethod)oldListOrMethod;
         if (!method.isPrivate()) {
            e = this.getOrPutMethods(from.name, to);
            e.methods = this.addMethodToList(e.methods, method);
         }
      }

   }

   public void clearCaches() {
      for(int i = 0; i != this.table.length; ++i) {
         for(MetaMethodIndex.Entry e = this.table[i]; e != null; e = e.nextHashEntry) {
            e.cachedMethod = e.cachedMethodForSuper = e.cachedStaticMethod = null;
         }
      }

   }

   public void clearCaches(String name) {
      for(int i = 0; i != this.table.length; ++i) {
         for(MetaMethodIndex.Entry e = this.table[i]; e != null; e = e.nextHashEntry) {
            if (e.name.equals(name)) {
               e.cachedMethod = e.cachedMethodForSuper = e.cachedStaticMethod = null;
            }
         }
      }

   }

   public interface EntryIterator {
      boolean hasNext();

      MetaMethodIndex.Entry next();
   }

   public static class Entry {
      public int hash;
      public MetaMethodIndex.Entry nextHashEntry;
      public MetaMethodIndex.Entry nextClassEntry;
      public String name;
      public Class cls;
      public Object methods;
      public Object methodsForSuper;
      public Object staticMethods;
      public MetaMethodIndex.CacheEntry cachedMethod;
      public MetaMethodIndex.CacheEntry cachedMethodForSuper;
      public MetaMethodIndex.CacheEntry cachedStaticMethod;

      public String toString() {
         return "[" + this.name + ", " + this.cls.getName() + "]";
      }
   }

   public static class CacheEntry {
      public Class[] params;
      public MetaMethod method;
   }

   public static class Header {
      public MetaMethodIndex.Entry head;
      Class cls;
      public int clsHashCode31;
      public Class subclass;

      public Header(Class cls) {
         this(cls, (Class)null);
      }

      public Header(Class cls, Class subclass) {
         this.cls = cls;
         this.subclass = subclass;
         this.clsHashCode31 = 31 * cls.hashCode();
      }
   }
}
