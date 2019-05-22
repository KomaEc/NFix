package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaClassRegistryChangeEvent;
import groovy.lang.MetaClassRegistryChangeEventListener;
import groovy.lang.MetaMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.DefaultGroovyStaticMethods;
import org.codehaus.groovy.util.FastArray;
import org.codehaus.groovy.util.ManagedLinkedList;
import org.codehaus.groovy.util.ReferenceBundle;
import org.codehaus.groovy.vmplugin.VMPluginFactory;

public class MetaClassRegistryImpl implements MetaClassRegistry {
   private boolean useAccessible;
   private FastArray instanceMethods;
   private FastArray staticMethods;
   private LinkedList changeListenerList;
   private ManagedLinkedList metaClassInfo;
   public static final int LOAD_DEFAULT = 0;
   public static final int DONT_LOAD_DEFAULT = 1;
   private static MetaClassRegistry instanceInclude;
   private static MetaClassRegistry instanceExclude;
   private volatile MetaClassRegistry.MetaClassCreationHandle metaClassCreationHandle;

   public MetaClassRegistryImpl() {
      this(0, true);
   }

   public MetaClassRegistryImpl(int loadDefault) {
      this(loadDefault, true);
   }

   public MetaClassRegistryImpl(boolean useAccessible) {
      this(0, useAccessible);
   }

   public MetaClassRegistryImpl(int loadDefault, boolean useAccessible) {
      this.instanceMethods = new FastArray();
      this.staticMethods = new FastArray();
      this.changeListenerList = new LinkedList();
      this.metaClassInfo = new ManagedLinkedList(ReferenceBundle.getWeakBundle());
      this.metaClassCreationHandle = new MetaClassRegistry.MetaClassCreationHandle();
      this.useAccessible = useAccessible;
      if (loadDefault == 0) {
         Map<CachedClass, List<MetaMethod>> map = new HashMap();
         this.registerMethods((Class)null, true, true, map);
         Class[] additionals = DefaultGroovyMethods.additionals;

         for(int i = 0; i != additionals.length; ++i) {
            this.createMetaMethodFromClass(map, additionals[i]);
         }

         Class[] pluginDGMs = VMPluginFactory.getPlugin().getPluginDefaultGroovyMethods();
         Class[] arr$ = pluginDGMs;
         int len$ = pluginDGMs.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class plugin = arr$[i$];
            this.registerMethods(plugin, false, true, map);
         }

         this.registerMethods(DefaultGroovyStaticMethods.class, false, false, map);
         Iterator i$ = map.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<CachedClass, List<MetaMethod>> e = (Entry)i$.next();
            CachedClass cls = (CachedClass)e.getKey();
            cls.setNewMopMethods((List)e.getValue());
         }
      }

      this.installMetaClassCreationHandle();
      MetaClass emcMetaClass = this.metaClassCreationHandle.create(ExpandoMetaClass.class, this);
      emcMetaClass.initialize();
      ClassInfo.getClassInfo(ExpandoMetaClass.class).setStrongMetaClass(emcMetaClass);
      this.addMetaClassRegistryChangeEventListener(new MetaClassRegistryChangeEventListener() {
         public void updateConstantMetaClass(MetaClassRegistryChangeEvent cmcu) {
            synchronized(MetaClassRegistryImpl.this.metaClassInfo) {
               MetaClassRegistryImpl.this.metaClassInfo.add(cmcu.getNewMetaClass());
            }
         }
      });
   }

   private void installMetaClassCreationHandle() {
      try {
         Class customMetaClassHandle = Class.forName("groovy.runtime.metaclass.CustomMetaClassCreationHandle");
         Constructor customMetaClassHandleConstructor = customMetaClassHandle.getConstructor();
         this.metaClassCreationHandle = (MetaClassRegistry.MetaClassCreationHandle)customMetaClassHandleConstructor.newInstance();
      } catch (ClassNotFoundException var3) {
         this.metaClassCreationHandle = new MetaClassRegistry.MetaClassCreationHandle();
      } catch (Exception var4) {
         throw new GroovyRuntimeException("Could not instantiate custom Metaclass creation handle: " + var4, var4);
      }

   }

   private void registerMethods(Class theClass, boolean useMethodWrapper, boolean useInstanceMethods, Map<CachedClass, List<MetaMethod>> map) {
      if (useMethodWrapper) {
         try {
            List<GeneratedMetaMethod.DgmMethodRecord> records = GeneratedMetaMethod.DgmMethodRecord.loadDgmInfo();
            Iterator i$ = records.iterator();

            while(i$.hasNext()) {
               GeneratedMetaMethod.DgmMethodRecord record = (GeneratedMetaMethod.DgmMethodRecord)i$.next();
               Class[] newParams = new Class[record.parameters.length - 1];
               System.arraycopy(record.parameters, 1, newParams, 0, record.parameters.length - 1);
               MetaMethod method = new GeneratedMetaMethod.Proxy(record.className, record.methodName, ReflectionCache.getCachedClass(record.parameters[0]), record.returnType, newParams);
               CachedClass declClass = method.getDeclaringClass();
               List<MetaMethod> arr = (List)map.get(declClass);
               if (arr == null) {
                  arr = new ArrayList(4);
                  map.put(declClass, arr);
               }

               ((List)arr).add(method);
               this.instanceMethods.add(method);
            }
         } catch (Throwable var14) {
            var14.printStackTrace();
            throw new GroovyRuntimeException("Failed to register the DGM methods : " + var14, var14);
         }
      } else {
         CachedMethod[] methods = ReflectionCache.getCachedClass(theClass).getMethods();
         CachedMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            CachedMethod method = arr$[i$];
            int mod = method.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isPublic(mod) && method.getCachedMethod().getAnnotation(Deprecated.class) == null) {
               CachedClass[] paramTypes = method.getParameterTypes();
               if (paramTypes.length > 0) {
                  List<MetaMethod> arr = (List)map.get(paramTypes[0]);
                  if (arr == null) {
                     arr = new ArrayList(4);
                     map.put(paramTypes[0], arr);
                  }

                  if (useInstanceMethods) {
                     NewInstanceMetaMethod metaMethod = new NewInstanceMetaMethod(method);
                     ((List)arr).add(metaMethod);
                     this.instanceMethods.add(metaMethod);
                  } else {
                     NewStaticMetaMethod metaMethod = new NewStaticMetaMethod(method);
                     ((List)arr).add(metaMethod);
                     this.staticMethods.add(metaMethod);
                  }
               }
            }
         }
      }

   }

   private void createMetaMethodFromClass(Map<CachedClass, List<MetaMethod>> map, Class aClass) {
      try {
         MetaMethod method = (MetaMethod)aClass.newInstance();
         CachedClass declClass = method.getDeclaringClass();
         List<MetaMethod> arr = (List)map.get(declClass);
         if (arr == null) {
            arr = new ArrayList(4);
            map.put(declClass, arr);
         }

         ((List)arr).add(method);
         this.instanceMethods.add(method);
      } catch (InstantiationException var6) {
      } catch (IllegalAccessException var7) {
      }

   }

   public final MetaClass getMetaClass(Class theClass) {
      return ClassInfo.getClassInfo(theClass).getMetaClass();
   }

   public MetaClass getMetaClass(Object obj) {
      return ClassInfo.getClassInfo(obj.getClass()).getMetaClass(obj);
   }

   private void setMetaClass(Class theClass, MetaClass oldMc, MetaClass newMc) {
      ClassInfo info = ClassInfo.getClassInfo(theClass);
      MetaClass mc = null;
      info.lock();

      try {
         mc = info.getStrongMetaClass();
         info.setStrongMetaClass(newMc);
      } finally {
         info.unlock();
      }

      if (oldMc == null && mc != newMc || oldMc != null && mc != newMc && mc != oldMc) {
         this.fireConstantMetaClassUpdate(theClass, newMc);
      }

   }

   public void removeMetaClass(Class theClass) {
      this.setMetaClass(theClass, (MetaClass)null, (MetaClass)null);
   }

   public void setMetaClass(Class theClass, MetaClass theMetaClass) {
      this.setMetaClass(theClass, (MetaClass)null, theMetaClass);
   }

   public void setMetaClass(Object obj, MetaClass theMetaClass) {
      Class theClass = obj.getClass();
      ClassInfo info = ClassInfo.getClassInfo(theClass);
      info.lock();

      try {
         info.setPerInstanceMetaClass(obj, theMetaClass);
      } finally {
         info.unlock();
      }

      this.fireConstantMetaClassUpdate(theClass, theMetaClass);
   }

   public boolean useAccessible() {
      return this.useAccessible;
   }

   public MetaClassRegistry.MetaClassCreationHandle getMetaClassCreationHandler() {
      return this.metaClassCreationHandle;
   }

   public void setMetaClassCreationHandle(MetaClassRegistry.MetaClassCreationHandle handle) {
      if (handle == null) {
         throw new IllegalArgumentException("Cannot set MetaClassCreationHandle to null value!");
      } else {
         ClassInfo.clearModifiedExpandos();
         handle.setDisableCustomMetaClassLookup(this.metaClassCreationHandle.isDisableCustomMetaClassLookup());
         this.metaClassCreationHandle = handle;
      }
   }

   public void addMetaClassRegistryChangeEventListener(MetaClassRegistryChangeEventListener listener) {
      synchronized(this.changeListenerList) {
         this.changeListenerList.add(listener);
      }
   }

   public void removeMetaClassRegistryChangeEventListener(MetaClassRegistryChangeEventListener listener) {
      synchronized(this.changeListenerList) {
         Object first = this.changeListenerList.getFirst();
         this.changeListenerList.remove(listener);
         if (this.changeListenerList.size() == 0) {
            this.changeListenerList.addFirst(first);
         }

      }
   }

   protected void fireConstantMetaClassUpdate(Class c, MetaClass newMc) {
      MetaClassRegistryChangeEventListener[] listener = this.getMetaClassRegistryChangeEventListeners();
      MetaClassRegistryChangeEvent cmcu = new MetaClassRegistryChangeEvent(this, c, newMc);

      for(int i = 0; i < listener.length; ++i) {
         listener[i].updateConstantMetaClass(cmcu);
      }

   }

   public MetaClassRegistryChangeEventListener[] getMetaClassRegistryChangeEventListeners() {
      synchronized(this.changeListenerList) {
         return (MetaClassRegistryChangeEventListener[])((MetaClassRegistryChangeEventListener[])this.changeListenerList.toArray(new MetaClassRegistryChangeEventListener[this.changeListenerList.size()]));
      }
   }

   public static MetaClassRegistry getInstance(int includeExtension) {
      if (includeExtension != 1) {
         if (instanceInclude == null) {
            instanceInclude = new MetaClassRegistryImpl();
         }

         return instanceInclude;
      } else {
         if (instanceExclude == null) {
            instanceExclude = new MetaClassRegistryImpl(1);
         }

         return instanceExclude;
      }
   }

   public FastArray getInstanceMethods() {
      return this.instanceMethods;
   }

   public FastArray getStaticMethods() {
      return this.staticMethods;
   }

   public Iterator iterator() {
      final MetaClass[] refs;
      synchronized(this.metaClassInfo) {
         refs = (MetaClass[])((MetaClass[])this.metaClassInfo.toArray(new MetaClass[0]));
      }

      return new Iterator() {
         private int index = 0;
         private MetaClass currentMeta;
         private boolean hasNextCalled = false;
         private boolean hasNext = false;

         public boolean hasNext() {
            if (this.hasNextCalled) {
               return this.hasNext;
            } else {
               this.hasNextCalled = true;
               if (this.index < refs.length) {
                  this.hasNext = true;
                  this.currentMeta = refs[this.index];
                  ++this.index;
               } else {
                  this.hasNext = false;
               }

               return this.hasNext;
            }
         }

         private void ensureNext() {
            this.hasNext();
            this.hasNextCalled = false;
         }

         public Object next() {
            this.ensureNext();
            return this.currentMeta;
         }

         public void remove() {
            this.ensureNext();
            MetaClassRegistryImpl.this.setMetaClass(this.currentMeta.getTheClass(), this.currentMeta, (MetaClass)null);
            this.currentMeta = null;
         }
      };
   }
}
