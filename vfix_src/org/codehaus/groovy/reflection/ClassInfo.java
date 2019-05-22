package org.codehaus.groovy.reflection;

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.ExpandoMetaClassCreationHandle;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaMethod;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.codehaus.groovy.reflection.stdclasses.ArrayCachedClass;
import org.codehaus.groovy.reflection.stdclasses.BigDecimalCachedClass;
import org.codehaus.groovy.reflection.stdclasses.BigIntegerCachedClass;
import org.codehaus.groovy.reflection.stdclasses.BooleanCachedClass;
import org.codehaus.groovy.reflection.stdclasses.ByteCachedClass;
import org.codehaus.groovy.reflection.stdclasses.CachedClosureClass;
import org.codehaus.groovy.reflection.stdclasses.CharacterCachedClass;
import org.codehaus.groovy.reflection.stdclasses.DoubleCachedClass;
import org.codehaus.groovy.reflection.stdclasses.FloatCachedClass;
import org.codehaus.groovy.reflection.stdclasses.IntegerCachedClass;
import org.codehaus.groovy.reflection.stdclasses.LongCachedClass;
import org.codehaus.groovy.reflection.stdclasses.NumberCachedClass;
import org.codehaus.groovy.reflection.stdclasses.ObjectCachedClass;
import org.codehaus.groovy.reflection.stdclasses.ShortCachedClass;
import org.codehaus.groovy.reflection.stdclasses.StringCachedClass;
import org.codehaus.groovy.util.LazyReference;
import org.codehaus.groovy.util.LockableObject;
import org.codehaus.groovy.util.ManagedConcurrentMap;
import org.codehaus.groovy.util.ManagedReference;
import org.codehaus.groovy.util.ReferenceBundle;

public class ClassInfo extends ManagedConcurrentMap.Entry<Class, ClassInfo> {
   private static final Set<ClassInfo> modifiedExpandos = new HashSet();
   private final ClassInfo.LazyCachedClassRef cachedClassRef;
   private final ClassInfo.LazyClassLoaderRef artifactClassLoader;
   private final LockableObject lock = new LockableObject();
   public final int hash;
   private volatile int version;
   private MetaClass strongMetaClass;
   private ManagedReference<MetaClass> weakMetaClass;
   MetaMethod[] dgmMetaMethods;
   MetaMethod[] newMetaMethods;
   private ManagedConcurrentMap perInstanceMetaClassMap;
   private static ReferenceBundle softBundle = ReferenceBundle.getSoftBundle();
   private static ReferenceBundle weakBundle = ReferenceBundle.getWeakBundle();
   private static final ClassInfo.ClassInfoSet globalClassSet;
   private static final WeakReference<ClassInfo.ThreadLocalMapHandler> localMapRef;

   ClassInfo(ManagedConcurrentMap.Segment segment, Class klazz, int hash) {
      super(softBundle, segment, klazz, hash);
      this.dgmMetaMethods = CachedClass.EMPTY;
      this.newMetaMethods = CachedClass.EMPTY;
      this.hash = hash;
      this.cachedClassRef = new ClassInfo.LazyCachedClassRef(softBundle, this);
      this.artifactClassLoader = new ClassInfo.LazyClassLoaderRef(softBundle, this);
   }

   public int getVersion() {
      return this.version;
   }

   public void incVersion() {
      ++this.version;
   }

   public ExpandoMetaClass getModifiedExpando() {
      return this.strongMetaClass == null ? null : (this.strongMetaClass instanceof ExpandoMetaClass ? (ExpandoMetaClass)this.strongMetaClass : null);
   }

   public static void clearModifiedExpandos() {
      Iterator it = modifiedExpandos.iterator();

      while(it.hasNext()) {
         ClassInfo info = (ClassInfo)it.next();
         it.remove();
         info.setStrongMetaClass((MetaClass)null);
      }

   }

   public CachedClass getCachedClass() {
      return (CachedClass)this.cachedClassRef.get();
   }

   public ClassLoaderForClassArtifacts getArtifactClassLoader() {
      return (ClassLoaderForClassArtifacts)this.artifactClassLoader.get();
   }

   public static ClassInfo getClassInfo(Class cls) {
      ClassInfo.ThreadLocalMapHandler handler = (ClassInfo.ThreadLocalMapHandler)localMapRef.get();
      SoftReference<ClassInfo.LocalMap> ref = null;
      if (handler != null) {
         ref = handler.get();
      }

      ClassInfo.LocalMap map = null;
      if (ref != null) {
         map = (ClassInfo.LocalMap)ref.get();
      }

      return map != null ? map.get(cls) : (ClassInfo)globalClassSet.getOrPut(cls, (Object)null);
   }

   public MetaClass getStrongMetaClass() {
      return this.strongMetaClass;
   }

   public void setStrongMetaClass(MetaClass answer) {
      ++this.version;
      if (this.strongMetaClass instanceof ExpandoMetaClass) {
         ((ExpandoMetaClass)this.strongMetaClass).inRegistry = false;
         modifiedExpandos.remove(this);
      }

      this.strongMetaClass = answer;
      if (this.strongMetaClass instanceof ExpandoMetaClass) {
         ((ExpandoMetaClass)this.strongMetaClass).inRegistry = true;
         modifiedExpandos.add(this);
      }

      this.weakMetaClass = null;
   }

   public MetaClass getWeakMetaClass() {
      return this.weakMetaClass == null ? null : (MetaClass)this.weakMetaClass.get();
   }

   public void setWeakMetaClass(MetaClass answer) {
      ++this.version;
      this.strongMetaClass = null;
      if (answer == null) {
         this.weakMetaClass = null;
      } else {
         this.weakMetaClass = new ManagedReference(softBundle, answer);
      }

   }

   public MetaClass getMetaClassForClass() {
      return this.strongMetaClass != null ? this.strongMetaClass : (this.weakMetaClass == null ? null : (MetaClass)this.weakMetaClass.get());
   }

   private MetaClass getMetaClassUnderLock() {
      MetaClass answer = this.getStrongMetaClass();
      if (answer != null) {
         return answer;
      } else {
         answer = this.getWeakMetaClass();
         MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
         MetaClassRegistry.MetaClassCreationHandle mccHandle = metaClassRegistry.getMetaClassCreationHandler();
         if (answer != null) {
            boolean enableGloballyOn = mccHandle instanceof ExpandoMetaClassCreationHandle;
            boolean cachedAnswerIsEMC = answer instanceof ExpandoMetaClass;
            if (!enableGloballyOn || cachedAnswerIsEMC) {
               return answer;
            }
         }

         answer = mccHandle.create((Class)this.get(), metaClassRegistry);
         answer.initialize();
         if (GroovySystem.isKeepJavaMetaClasses()) {
            this.setStrongMetaClass(answer);
         } else {
            this.setWeakMetaClass(answer);
         }

         return answer;
      }
   }

   public final MetaClass getMetaClass() {
      MetaClass answer = this.getMetaClassForClass();
      if (answer != null) {
         return answer;
      } else {
         this.lock();

         MetaClass var2;
         try {
            var2 = this.getMetaClassUnderLock();
         } finally {
            this.unlock();
         }

         return var2;
      }
   }

   public MetaClass getMetaClass(Object obj) {
      MetaClass instanceMetaClass = this.getPerInstanceMetaClass(obj);
      if (instanceMetaClass != null) {
         return instanceMetaClass;
      } else {
         this.lock();

         MetaClass var3;
         try {
            var3 = this.getMetaClassUnderLock();
         } finally {
            this.unlock();
         }

         return var3;
      }
   }

   public static int size() {
      return globalClassSet.size();
   }

   public static int fullSize() {
      return globalClassSet.fullSize();
   }

   public void finalizeRef() {
      this.setStrongMetaClass((MetaClass)null);
      this.cachedClassRef.clear();
      this.artifactClassLoader.clear();
      super.finalizeRef();
   }

   private static CachedClass createCachedClass(Class klazz, ClassInfo classInfo) {
      if (klazz == Object.class) {
         return new ObjectCachedClass(classInfo);
      } else if (klazz == String.class) {
         return new StringCachedClass(classInfo);
      } else {
         Object cachedClass;
         if (!Number.class.isAssignableFrom(klazz) && !klazz.isPrimitive()) {
            if (klazz.getName().charAt(0) == '[') {
               cachedClass = new ArrayCachedClass(klazz, classInfo);
            } else if (klazz == Boolean.class) {
               cachedClass = new BooleanCachedClass(klazz, classInfo, true);
            } else if (klazz == Character.class) {
               cachedClass = new CharacterCachedClass(klazz, classInfo, true);
            } else if (Closure.class.isAssignableFrom(klazz)) {
               cachedClass = new CachedClosureClass(klazz, classInfo);
            } else {
               cachedClass = new CachedClass(klazz, classInfo);
            }
         } else if (klazz == Number.class) {
            cachedClass = new NumberCachedClass(klazz, classInfo);
         } else if (klazz != Integer.class && klazz != Integer.TYPE) {
            if (klazz != Double.class && klazz != Double.TYPE) {
               if (klazz == BigDecimal.class) {
                  cachedClass = new BigDecimalCachedClass(klazz, classInfo);
               } else if (klazz != Long.class && klazz != Long.TYPE) {
                  if (klazz != Float.class && klazz != Float.TYPE) {
                     if (klazz != Short.class && klazz != Short.TYPE) {
                        if (klazz == Boolean.TYPE) {
                           cachedClass = new BooleanCachedClass(klazz, classInfo, false);
                        } else if (klazz == Character.TYPE) {
                           cachedClass = new CharacterCachedClass(klazz, classInfo, false);
                        } else if (klazz == BigInteger.class) {
                           cachedClass = new BigIntegerCachedClass(klazz, classInfo);
                        } else if (klazz != Byte.class && klazz != Byte.TYPE) {
                           cachedClass = new CachedClass(klazz, classInfo);
                        } else {
                           cachedClass = new ByteCachedClass(klazz, classInfo, klazz == Byte.class);
                        }
                     } else {
                        cachedClass = new ShortCachedClass(klazz, classInfo, klazz == Short.class);
                     }
                  } else {
                     cachedClass = new FloatCachedClass(klazz, classInfo, klazz == Float.class);
                  }
               } else {
                  cachedClass = new LongCachedClass(klazz, classInfo, klazz == Long.class);
               }
            } else {
               cachedClass = new DoubleCachedClass(klazz, classInfo, klazz == Double.class);
            }
         } else {
            cachedClass = new IntegerCachedClass(klazz, classInfo, klazz == Integer.class);
         }

         return (CachedClass)cachedClass;
      }
   }

   public void lock() {
      this.lock.lock();
   }

   public void unlock() {
      this.lock.unlock();
   }

   public MetaClass getPerInstanceMetaClass(Object obj) {
      return this.perInstanceMetaClassMap == null ? null : (MetaClass)this.perInstanceMetaClassMap.get(obj);
   }

   public void setPerInstanceMetaClass(Object obj, MetaClass metaClass) {
      ++this.version;
      if (metaClass != null) {
         if (this.perInstanceMetaClassMap == null) {
            this.perInstanceMetaClassMap = new ManagedConcurrentMap(ReferenceBundle.getWeakBundle());
         }

         this.perInstanceMetaClassMap.put(obj, metaClass);
      } else if (this.perInstanceMetaClassMap != null) {
         this.perInstanceMetaClassMap.remove(obj);
      }

   }

   public boolean hasPerInstanceMetaClasses() {
      return this.perInstanceMetaClassMap != null;
   }

   static {
      globalClassSet = new ClassInfo.ClassInfoSet(softBundle);
      ClassInfo.ThreadLocalMapHandler localMap = new ClassInfo.ThreadLocalMapHandler();
      localMapRef = new WeakReference(localMap, (ReferenceQueue)null);
   }

   private static class DebugRef extends ManagedReference<Class> {
      public static final boolean debug = false;
      private static final AtomicInteger count = new AtomicInteger();
      final String name;

      public DebugRef(Class klazz) {
         super(ClassInfo.softBundle, klazz);
         this.name = klazz == null ? "<null>" : klazz.getName();
         count.incrementAndGet();
      }

      public void finalizeRef() {
         System.out.println(this.name + " unloaded " + count.decrementAndGet() + " classes kept");
         super.finalizeReference();
      }
   }

   private static class LazyClassLoaderRef extends LazyReference<ClassLoaderForClassArtifacts> {
      private final ClassInfo info;

      LazyClassLoaderRef(ReferenceBundle bundle, ClassInfo info) {
         super(bundle);
         this.info = info;
      }

      public ClassLoaderForClassArtifacts initValue() {
         return new ClassLoaderForClassArtifacts((Class)this.info.get());
      }
   }

   private static class LazyCachedClassRef extends LazyReference<CachedClass> {
      private final ClassInfo info;

      LazyCachedClassRef(ReferenceBundle bundle, ClassInfo info) {
         super(bundle);
         this.info = info;
      }

      public CachedClass initValue() {
         return ClassInfo.createCachedClass((Class)this.info.get(), this.info);
      }
   }

   private static class ThreadLocalMapHandler extends ThreadLocal<SoftReference<ClassInfo.LocalMap>> {
      SoftReference<ClassInfo.LocalMap> recentThreadMapRef;

      private ThreadLocalMapHandler() {
      }

      protected SoftReference<ClassInfo.LocalMap> initialValue() {
         return new SoftReference(new ClassInfo.LocalMap(), (ReferenceQueue)null);
      }

      public SoftReference<ClassInfo.LocalMap> get() {
         SoftReference<ClassInfo.LocalMap> mapRef = this.recentThreadMapRef;
         ClassInfo.LocalMap recent = null;
         if (mapRef != null) {
            recent = (ClassInfo.LocalMap)mapRef.get();
         }

         if (recent != null && recent.myThread.get() == Thread.currentThread()) {
            return mapRef;
         } else {
            SoftReference<ClassInfo.LocalMap> ref = (SoftReference)super.get();
            this.recentThreadMapRef = ref;
            return ref;
         }
      }

      // $FF: synthetic method
      ThreadLocalMapHandler(Object x0) {
         this();
      }
   }

   private static final class LocalMap extends HashMap<Class, ClassInfo> {
      private static final int CACHE_SIZE = 5;
      private final PhantomReference<Thread> myThread;
      private int nextCacheEntry;
      private final ClassInfo[] cache;
      private static final ClassInfo NOINFO = new ClassInfo((ManagedConcurrentMap.Segment)null, (Class)null, 0);

      private LocalMap() {
         this.myThread = new PhantomReference(Thread.currentThread(), (ReferenceQueue)null);
         this.cache = new ClassInfo[5];

         for(int i = 0; i < this.cache.length; ++i) {
            this.cache[i] = NOINFO;
         }

      }

      public ClassInfo get(Class key) {
         ClassInfo info = this.getFromCache(key);
         if (info != null) {
            return info;
         } else {
            info = (ClassInfo)super.get(key);
            return info != null ? this.putToCache(info) : this.putToCache((ClassInfo)ClassInfo.globalClassSet.getOrPut(key, (Object)null));
         }
      }

      private ClassInfo getFromCache(Class klazz) {
         int i = 0;

         for(int k = this.nextCacheEntry - 1; i < this.cache.length; --k) {
            if (k < 0) {
               k += 5;
            }

            ClassInfo info = this.cache[k];
            if (klazz == info.get()) {
               this.nextCacheEntry = k + 1;
               if (this.nextCacheEntry == 5) {
                  this.nextCacheEntry = 0;
               }

               return info;
            }

            ++i;
         }

         return null;
      }

      private ClassInfo putToCache(ClassInfo classInfo) {
         this.cache[this.nextCacheEntry++] = classInfo;
         if (this.nextCacheEntry == 5) {
            this.nextCacheEntry = 0;
         }

         return classInfo;
      }

      // $FF: synthetic method
      LocalMap(Object x0) {
         this();
      }
   }

   public static class ClassInfoSet extends ManagedConcurrentMap<Class, ClassInfo> {
      public ClassInfoSet(ReferenceBundle bundle) {
         super(bundle);
      }

      protected ClassInfo.ClassInfoSet.Segment createSegment(Object segmentInfo, int cap) {
         ReferenceBundle bundle = (ReferenceBundle)segmentInfo;
         if (bundle == null) {
            throw new IllegalArgumentException("bundle must not be null ");
         } else {
            return new ClassInfo.ClassInfoSet.Segment(bundle, cap);
         }
      }

      static final class Segment extends ManagedConcurrentMap.Segment<Class, ClassInfo> {
         Segment(ReferenceBundle bundle, int initialCapacity) {
            super(bundle, initialCapacity);
         }

         protected ClassInfo createEntry(Class key, int hash, ClassInfo unused) {
            return new ClassInfo(this, key, hash);
         }
      }
   }
}
