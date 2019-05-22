package org.codehaus.groovy.runtime;

import groovy.lang.Closure;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod;

public class GroovyCategorySupport {
   private static AtomicInteger categoriesInUse = new AtomicInteger();
   private static final GroovyCategorySupport.MyThreadLocal THREAD_INFO = new GroovyCategorySupport.MyThreadLocal();

   public static AtomicInteger getCategoryNameUsage(String name) {
      return THREAD_INFO.getUsage(name);
   }

   public static Object use(Class categoryClass, Closure closure) {
      return THREAD_INFO.getInfo().use(categoryClass, closure);
   }

   public static Object use(List<Class> categoryClasses, Closure closure) {
      return THREAD_INFO.getInfo().use(categoryClasses, closure);
   }

   public static boolean hasCategoryInCurrentThread() {
      if (categoriesInUse.get() == 0) {
         return false;
      } else {
         GroovyCategorySupport.ThreadCategoryInfo infoNullable = THREAD_INFO.getInfoNullable();
         return infoNullable != null && infoNullable.level != 0;
      }
   }

   public static boolean hasCategoryInAnyThread() {
      return categoriesInUse.get() != 0;
   }

   public static GroovyCategorySupport.CategoryMethodList getCategoryMethods(String name) {
      GroovyCategorySupport.ThreadCategoryInfo categoryInfo = THREAD_INFO.getInfoNullable();
      return categoryInfo == null ? null : categoryInfo.getCategoryMethods(name);
   }

   public static String getPropertyCategoryGetterName(String propertyName) {
      GroovyCategorySupport.ThreadCategoryInfo categoryInfo = THREAD_INFO.getInfoNullable();
      return categoryInfo == null ? null : categoryInfo.getPropertyCategoryGetterName(propertyName);
   }

   public static String getPropertyCategorySetterName(String propertyName) {
      GroovyCategorySupport.ThreadCategoryInfo categoryInfo = THREAD_INFO.getInfoNullable();
      return categoryInfo == null ? null : categoryInfo.getPropertyCategorySetterName(propertyName);
   }

   private static class MyThreadLocal extends ThreadLocal<SoftReference> {
      ConcurrentHashMap<String, AtomicInteger> usage;

      private MyThreadLocal() {
         this.usage = new ConcurrentHashMap();
      }

      public GroovyCategorySupport.ThreadCategoryInfo getInfo() {
         SoftReference reference = (SoftReference)this.get();
         GroovyCategorySupport.ThreadCategoryInfo tcinfo;
         if (reference != null) {
            tcinfo = (GroovyCategorySupport.ThreadCategoryInfo)reference.get();
            if (tcinfo == null) {
               tcinfo = new GroovyCategorySupport.ThreadCategoryInfo();
               this.set((Object)(new SoftReference(tcinfo)));
            }
         } else {
            tcinfo = new GroovyCategorySupport.ThreadCategoryInfo();
            this.set((Object)(new SoftReference(tcinfo)));
         }

         return tcinfo;
      }

      public GroovyCategorySupport.ThreadCategoryInfo getInfoNullable() {
         SoftReference reference = (SoftReference)this.get();
         return reference == null ? null : (GroovyCategorySupport.ThreadCategoryInfo)reference.get();
      }

      public AtomicInteger getUsage(String name) {
         AtomicInteger u = (AtomicInteger)this.usage.get(name);
         if (u != null) {
            return u;
         } else {
            AtomicInteger ai = new AtomicInteger();
            AtomicInteger prev = (AtomicInteger)this.usage.putIfAbsent(name, ai);
            return prev == null ? ai : prev;
         }
      }

      // $FF: synthetic method
      MyThreadLocal(Object x0) {
         this();
      }
   }

   private static class CategoryMethod extends NewInstanceMetaMethod implements Comparable {
      private final Class metaClass;

      public CategoryMethod(CachedMethod metaMethod, Class metaClass) {
         super(metaMethod);
         this.metaClass = metaClass;
      }

      public boolean isCacheable() {
         return false;
      }

      public int compareTo(Object o) {
         GroovyCategorySupport.CategoryMethod thatMethod = (GroovyCategorySupport.CategoryMethod)o;
         Class thisClass = this.metaClass;
         Class thatClass = thatMethod.metaClass;
         if (thisClass == thatClass) {
            return 0;
         } else if (this.isChildOfParent(thisClass, thatClass)) {
            return -1;
         } else {
            return this.isChildOfParent(thatClass, thisClass) ? 1 : 0;
         }
      }

      private boolean isChildOfParent(Class candidateChild, Class candidateParent) {
         Class loop = candidateChild;

         while(loop != null && loop != Object.class) {
            loop = loop.getSuperclass();
            if (loop == candidateParent) {
               return true;
            }
         }

         return false;
      }
   }

   public static class ThreadCategoryInfo extends HashMap<String, GroovyCategorySupport.CategoryMethodList> {
      int level;
      private Map<String, String> propertyGetterMap;
      private Map<String, String> propertySetterMap;

      private void newScope() {
         GroovyCategorySupport.categoriesInUse.incrementAndGet();
         ++this.level;
      }

      private void endScope() {
         Iterator it = this.entrySet().iterator();

         while(it.hasNext()) {
            Entry<String, GroovyCategorySupport.CategoryMethodList> e = (Entry)it.next();
            GroovyCategorySupport.CategoryMethodList list = (GroovyCategorySupport.CategoryMethodList)e.getValue();
            if (list.level == this.level) {
               GroovyCategorySupport.CategoryMethodList prev = list.previous;
               if (prev == null) {
                  it.remove();
                  list.usage.addAndGet(-list.size());
               } else {
                  e.setValue(prev);
                  list.usage.addAndGet(prev.size() - list.size());
               }
            }
         }

         --this.level;
         GroovyCategorySupport.categoriesInUse.getAndDecrement();
         if (this.level == 0) {
            GroovyCategorySupport.THREAD_INFO.remove();
         }

      }

      private Object use(Class categoryClass, Closure closure) {
         this.newScope();

         Object var3;
         try {
            this.use(categoryClass);
            var3 = closure.call();
         } finally {
            this.endScope();
         }

         return var3;
      }

      public Object use(List<Class> categoryClasses, Closure closure) {
         this.newScope();

         try {
            Iterator i$ = categoryClasses.iterator();

            while(i$.hasNext()) {
               Class categoryClass = (Class)i$.next();
               this.use(categoryClass);
            }

            Object var8 = closure.call();
            return var8;
         } finally {
            this.endScope();
         }
      }

      private void applyUse(CachedClass cachedClass) {
         CachedMethod[] methods = cachedClass.getMethods();
         CachedMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            CachedMethod cachedMethod = arr$[i$];
            if (cachedMethod.isStatic() && cachedMethod.isPublic()) {
               CachedClass[] paramTypes = cachedMethod.getParameterTypes();
               if (paramTypes.length > 0) {
                  CachedClass metaClass = paramTypes[0];
                  GroovyCategorySupport.CategoryMethod mmethod = new GroovyCategorySupport.CategoryMethod(cachedMethod, metaClass.getTheClass());
                  String name = cachedMethod.getName();
                  GroovyCategorySupport.CategoryMethodList list = (GroovyCategorySupport.CategoryMethodList)this.get(name);
                  if (list == null || list.level != this.level) {
                     list = new GroovyCategorySupport.CategoryMethodList(name, this.level, list);
                     this.put((Object)name, (Object)list);
                  }

                  list.add(mmethod);
                  Collections.sort(list);
                  this.cachePropertyAccessor(mmethod);
               }
            }
         }

      }

      private void cachePropertyAccessor(GroovyCategorySupport.CategoryMethod method) {
         String name = method.getName();
         int parameterLength = method.getParameterTypes().length;
         if (name.startsWith("get") && name.length() > 3 && parameterLength == 0) {
            this.propertyGetterMap = this.putPropertyAccessor(3, name, this.propertyGetterMap);
         } else if (name.startsWith("set") && name.length() > 3 && parameterLength == 1) {
            this.propertySetterMap = this.putPropertyAccessor(3, name, this.propertySetterMap);
         }

      }

      private Map<String, String> putPropertyAccessor(int prefixLength, String accessorName, Map<String, String> map) {
         if (map == null) {
            map = new HashMap();
         }

         String property = accessorName.substring(prefixLength, prefixLength + 1).toLowerCase() + accessorName.substring(prefixLength + 1);
         ((Map)map).put(property, accessorName);
         return (Map)map;
      }

      private void use(Class categoryClass) {
         CachedClass cachedClass = ReflectionCache.getCachedClass(categoryClass);
         LinkedList<CachedClass> classStack = new LinkedList();

         CachedClass klazz;
         for(klazz = cachedClass; klazz.getTheClass() != Object.class; klazz = klazz.getCachedSuperClass()) {
            classStack.add(klazz);
         }

         while(!classStack.isEmpty()) {
            klazz = (CachedClass)classStack.removeLast();
            this.applyUse(klazz);
         }

      }

      public GroovyCategorySupport.CategoryMethodList getCategoryMethods(String name) {
         return this.level == 0 ? null : (GroovyCategorySupport.CategoryMethodList)this.get(name);
      }

      String getPropertyCategoryGetterName(String propertyName) {
         return this.propertyGetterMap != null ? (String)this.propertyGetterMap.get(propertyName) : null;
      }

      String getPropertyCategorySetterName(String propertyName) {
         return this.propertySetterMap != null ? (String)this.propertySetterMap.get(propertyName) : null;
      }
   }

   public static class CategoryMethodList extends ArrayList<GroovyCategorySupport.CategoryMethod> {
      public final int level;
      final GroovyCategorySupport.CategoryMethodList previous;
      final AtomicInteger usage;

      public CategoryMethodList(String name, int level, GroovyCategorySupport.CategoryMethodList previous) {
         this.level = level;
         this.previous = previous;
         if (previous != null) {
            this.addAll(previous);
            this.usage = previous.usage;
         } else {
            this.usage = GroovyCategorySupport.getCategoryNameUsage(name);
         }

      }

      public boolean add(GroovyCategorySupport.CategoryMethod o) {
         this.usage.incrementAndGet();
         return super.add(o);
      }
   }
}
