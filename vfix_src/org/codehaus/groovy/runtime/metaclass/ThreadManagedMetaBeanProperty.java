package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.Closure;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.util.ManagedConcurrentMap;
import org.codehaus.groovy.util.ReferenceBundle;

public class ThreadManagedMetaBeanProperty extends MetaBeanProperty {
   private static final ConcurrentHashMap<String, ManagedConcurrentMap> PROPNAME_TO_MAP = new ConcurrentHashMap();
   private final ManagedConcurrentMap instance2Prop;
   private Class declaringClass;
   private ThreadManagedMetaBeanProperty.ThreadBoundGetter getter;
   private ThreadManagedMetaBeanProperty.ThreadBoundSetter setter;
   private Object initialValue;
   private Closure initialValueCreator;
   private static final ReferenceBundle SOFT_BUNDLE = ReferenceBundle.getSoftBundle();

   public synchronized Object getInitialValue() {
      return this.getInitialValue((Object)null);
   }

   public synchronized Object getInitialValue(Object object) {
      return this.initialValueCreator != null ? this.initialValueCreator.call(object) : this.initialValue;
   }

   public void setInitialValueCreator(Closure callable) {
      this.initialValueCreator = callable;
   }

   public ThreadManagedMetaBeanProperty(Class declaringClass, String name, Class type, Object iv) {
      super(name, type, (MetaMethod)null, (MetaMethod)null);
      this.type = type;
      this.declaringClass = declaringClass;
      this.getter = new ThreadManagedMetaBeanProperty.ThreadBoundGetter(name);
      this.setter = new ThreadManagedMetaBeanProperty.ThreadBoundSetter(name);
      this.initialValue = iv;
      this.instance2Prop = getInstance2PropName(name);
   }

   public ThreadManagedMetaBeanProperty(Class declaringClass, String name, Class type, Closure initialValueCreator) {
      super(name, type, (MetaMethod)null, (MetaMethod)null);
      this.type = type;
      this.declaringClass = declaringClass;
      this.getter = new ThreadManagedMetaBeanProperty.ThreadBoundGetter(name);
      this.setter = new ThreadManagedMetaBeanProperty.ThreadBoundSetter(name);
      this.initialValueCreator = initialValueCreator;
      this.instance2Prop = getInstance2PropName(name);
   }

   private static ManagedConcurrentMap getInstance2PropName(String name) {
      ManagedConcurrentMap res = (ManagedConcurrentMap)PROPNAME_TO_MAP.get(name);
      if (res == null) {
         res = new ManagedConcurrentMap(SOFT_BUNDLE);
         ManagedConcurrentMap ores = (ManagedConcurrentMap)PROPNAME_TO_MAP.putIfAbsent(name, res);
         if (ores != null) {
            return ores;
         }
      }

      return res;
   }

   public MetaMethod getGetter() {
      return this.getter;
   }

   public MetaMethod getSetter() {
      return this.setter;
   }

   private class ThreadBoundSetter extends MetaMethod {
      private final String name;

      public ThreadBoundSetter(String name) {
         this.setParametersTypes(new CachedClass[]{ReflectionCache.getCachedClass(ThreadManagedMetaBeanProperty.this.type)});
         this.name = MetaProperty.getSetterName(name);
      }

      public int getModifiers() {
         return 1;
      }

      public String getName() {
         return this.name;
      }

      public Class getReturnType() {
         return ThreadManagedMetaBeanProperty.this.type;
      }

      public CachedClass getDeclaringClass() {
         return ReflectionCache.getCachedClass(ThreadManagedMetaBeanProperty.this.declaringClass);
      }

      public Object invoke(Object object, Object[] arguments) {
         ThreadManagedMetaBeanProperty.this.instance2Prop.put(object, arguments[0]);
         return null;
      }
   }

   class ThreadBoundGetter extends MetaMethod {
      private final String name;

      public ThreadBoundGetter(String name) {
         this.setParametersTypes(new CachedClass[0]);
         this.name = MetaProperty.getGetterName(name, ThreadManagedMetaBeanProperty.this.type);
      }

      public int getModifiers() {
         return 1;
      }

      public String getName() {
         return this.name;
      }

      public Class getReturnType() {
         return ThreadManagedMetaBeanProperty.this.type;
      }

      public CachedClass getDeclaringClass() {
         return ReflectionCache.getCachedClass(ThreadManagedMetaBeanProperty.this.declaringClass);
      }

      public Object invoke(Object object, Object[] arguments) {
         return ThreadManagedMetaBeanProperty.this.instance2Prop.getOrPut(object, ThreadManagedMetaBeanProperty.this.getInitialValue()).getValue();
      }
   }
}
