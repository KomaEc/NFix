package groovy.lang;

import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.MixinInMetaClass;
import org.codehaus.groovy.runtime.DefaultCachedMethodKey;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.MethodKey;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.ConstructorMetaMethodSite;
import org.codehaus.groovy.runtime.callsite.PogoMetaClassSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaClassSite;
import org.codehaus.groovy.runtime.callsite.StaticMetaClassSite;
import org.codehaus.groovy.runtime.metaclass.ClosureMetaMethod;
import org.codehaus.groovy.runtime.metaclass.ClosureStaticMetaMethod;
import org.codehaus.groovy.runtime.metaclass.MetaMethodIndex;
import org.codehaus.groovy.runtime.metaclass.MixedInMetaClass;
import org.codehaus.groovy.runtime.metaclass.MixinInstanceMetaMethod;
import org.codehaus.groovy.runtime.metaclass.OwnedMetaClass;
import org.codehaus.groovy.runtime.metaclass.ThreadManagedMetaBeanProperty;
import org.codehaus.groovy.util.FastArray;

public class ExpandoMetaClass extends MetaClassImpl implements GroovyObject {
   private static final String META_CLASS = "metaClass";
   private static final String CLASS = "class";
   private static final String META_METHODS = "metaMethods";
   private static final String METHODS = "methods";
   private static final String PROPERTIES = "properties";
   public static final String STATIC_QUALIFIER = "static";
   public static final String CONSTRUCTOR = "constructor";
   private static final String CLASS_PROPERTY = "class";
   private static final String META_CLASS_PROPERTY = "metaClass";
   private static final String GROOVY_CONSTRUCTOR = "<init>";
   private MetaClass myMetaClass;
   private boolean allowChangesAfterInit;
   private boolean initialized;
   private boolean initCalled;
   private boolean modified;
   public boolean inRegistry;
   private final Set<MetaMethod> inheritedMetaMethods;
   private final Map<String, MetaProperty> beanPropertyCache;
   private final Map<String, MetaProperty> staticBeanPropertyCache;
   private final Map<MethodKey, MetaMethod> expandoMethods;
   private final ConcurrentHashMap expandoSubclassMethods;
   private final Map<String, MetaProperty> expandoProperties;
   private ClosureStaticMetaMethod invokeStaticMethodMethod;
   private final Set<MixinInMetaClass> mixinClasses;

   public Collection getExpandoSubclassMethods() {
      return this.expandoSubclassMethods.values();
   }

   public ExpandoMetaClass(Class theClass) {
      super(GroovySystem.getMetaClassRegistry(), theClass);
      this.inheritedMetaMethods = new HashSet();
      this.beanPropertyCache = new ConcurrentHashMap();
      this.staticBeanPropertyCache = new ConcurrentHashMap();
      this.expandoMethods = new ConcurrentHashMap();
      this.expandoSubclassMethods = new ConcurrentHashMap();
      this.expandoProperties = new ConcurrentHashMap();
      this.mixinClasses = new LinkedHashSet();
      this.myMetaClass = InvokerHelper.getMetaClass(this.getClass());
   }

   public ExpandoMetaClass(Class theClass, MetaMethod[] add) {
      super(GroovySystem.getMetaClassRegistry(), theClass, add);
      this.inheritedMetaMethods = new HashSet();
      this.beanPropertyCache = new ConcurrentHashMap();
      this.staticBeanPropertyCache = new ConcurrentHashMap();
      this.expandoMethods = new ConcurrentHashMap();
      this.expandoSubclassMethods = new ConcurrentHashMap();
      this.expandoProperties = new ConcurrentHashMap();
      this.mixinClasses = new LinkedHashSet();
      this.myMetaClass = InvokerHelper.getMetaClass(this.getClass());
   }

   public ExpandoMetaClass(Class theClass, boolean register) {
      this(theClass);
      this.inRegistry = register;
   }

   public ExpandoMetaClass(Class theClass, boolean register, MetaMethod[] add) {
      this(theClass, add);
      this.inRegistry = register;
   }

   public ExpandoMetaClass(Class theClass, boolean register, boolean allowChangesAfterInit) {
      this(theClass);
      this.inRegistry = register;
      this.allowChangesAfterInit = allowChangesAfterInit;
   }

   public MetaMethod findMixinMethod(String methodName, Class[] arguments) {
      Iterator i$ = this.mixinClasses.iterator();

      MixinInMetaClass mixin;
      MetaMethod metaMethod;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         mixin = (MixinInMetaClass)i$.next();
         CachedClass mixinClass = mixin.getMixinClass();
         MetaClass metaClass = mixinClass.classInfo.getMetaClassForClass();
         if (metaClass == null) {
            metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(mixinClass.getTheClass());
         }

         metaMethod = metaClass.pickMethod(methodName, arguments);
         if (metaMethod == null && metaClass instanceof MetaClassImpl) {
            MetaClassImpl mc = (MetaClassImpl)metaClass;

            for(CachedClass cl = mc.getTheCachedClass().getCachedSuperClass(); cl != null; cl = cl.getCachedSuperClass()) {
               metaMethod = mc.getMethodWithoutCaching(cl.getTheClass(), methodName, arguments, false);
               if (metaMethod != null) {
                  break;
               }
            }
         }
      } while(metaMethod == null);

      MetaMethod method = new MixinInstanceMetaMethod(metaMethod, mixin);
      if (method.getParameterTypes().length == 1 && !method.getParameterTypes()[0].isPrimitive) {
         MetaMethod noParam = this.pickMethod(methodName, new Class[0]);
         if (noParam == null && arguments.length != 0) {
            this.findMixinMethod(methodName, new Class[0]);
         }
      }

      this.registerInstanceMethod(method);
      return method;
   }

   protected void onInvokeMethodFoundInHierarchy(MetaMethod method) {
      this.invokeMethodMethod = method;
   }

   protected void onSuperMethodFoundInHierarchy(MetaMethod method) {
      this.addSuperMethodIfNotOverridden(method);
   }

   protected void onSuperPropertyFoundInHierarchy(MetaBeanProperty property) {
      this.addMetaBeanProperty(property);
   }

   protected void onSetPropertyFoundInHierarchy(MetaMethod method) {
      this.setPropertyMethod = method;
   }

   protected void onGetPropertyFoundInHierarchy(MetaMethod method) {
      this.getPropertyMethod = method;
   }

   public synchronized boolean isModified() {
      return this.modified;
   }

   public void registerSubclassInstanceMethod(String name, Class klazz, Closure closure) {
      List<MetaMethod> list = ClosureMetaMethod.createMethodList(name, klazz, closure);
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         MetaMethod metaMethod = (MetaMethod)i$.next();
         this.registerSubclassInstanceMethod(metaMethod);
      }

   }

   public void registerSubclassInstanceMethod(MetaMethod metaMethod) {
      this.modified = true;
      String name = metaMethod.getName();
      Object methodOrList = this.expandoSubclassMethods.get(name);
      if (methodOrList == null) {
         this.expandoSubclassMethods.put(name, metaMethod);
      } else if (methodOrList instanceof MetaMethod) {
         FastArray arr = new FastArray(2);
         arr.add(methodOrList);
         arr.add(metaMethod);
         this.expandoSubclassMethods.put(name, arr);
      } else {
         ((FastArray)methodOrList).add(metaMethod);
      }

   }

   public void addMixinClass(MixinInMetaClass mixin) {
      this.mixinClasses.add(mixin);
   }

   public Object castToMixedType(Object obj, Class type) {
      Iterator i$ = this.mixinClasses.iterator();

      MixinInMetaClass mixin;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         mixin = (MixinInMetaClass)i$.next();
      } while(!type.isAssignableFrom(mixin.getMixinClass().getTheClass()));

      return mixin.getMixinInstance(obj);
   }

   public static void enableGlobally() {
      ExpandoMetaClassCreationHandle.enable();
   }

   public static void disableGlobally() {
      ExpandoMetaClassCreationHandle.disable();
   }

   public synchronized void initialize() {
      if (!this.isInitialized()) {
         super.initialize();
         this.setInitialized(true);
         this.initCalled = true;
      }

   }

   protected synchronized boolean isInitialized() {
      return this.initialized;
   }

   protected synchronized void setInitialized(boolean b) {
      this.initialized = b;
   }

   private void addSuperMethodIfNotOverridden(final MetaMethod metaMethodFromSuper) {
      this.performOperationOnMetaClass(new ExpandoMetaClass.Callable() {
         public void call() {
            MetaMethodIndex.Header header = ExpandoMetaClass.this.metaMethodIndex.getHeader(ExpandoMetaClass.this.theClass);
            MetaMethodIndex.Entry methods = ExpandoMetaClass.this.metaMethodIndex.getOrPutMethods(metaMethodFromSuper.getName(), header);
            MetaMethod existing = null;

            try {
               existing = ExpandoMetaClass.this.pickMethod(metaMethodFromSuper.getName(), metaMethodFromSuper.getNativeParameterTypes());
            } catch (GroovyRuntimeException var5) {
            }

            if (existing == null) {
               this.addMethodWithKey(metaMethodFromSuper);
            } else {
               boolean isGroovyMethod = ExpandoMetaClass.this.getMetaMethods().contains(existing);
               if (isGroovyMethod) {
                  this.addMethodWithKey(metaMethodFromSuper);
               } else if (ExpandoMetaClass.this.inheritedMetaMethods.contains(existing)) {
                  ExpandoMetaClass.this.inheritedMetaMethods.remove(existing);
                  this.addMethodWithKey(metaMethodFromSuper);
               }
            }

         }

         private void addMethodWithKey(MetaMethod metaMethodFromSuperx) {
            ExpandoMetaClass.this.inheritedMetaMethods.add(metaMethodFromSuperx);
            if (metaMethodFromSuperx instanceof ClosureMetaMethod) {
               ClosureMetaMethod closureMethod = (ClosureMetaMethod)metaMethodFromSuperx;
               Closure cloned = (Closure)closureMethod.getClosure().clone();
               String name = metaMethodFromSuperx.getName();
               Class declaringClass = metaMethodFromSuperx.getDeclaringClass().getTheClass();
               ClosureMetaMethod localMethod = ClosureMetaMethod.copy(closureMethod);
               ExpandoMetaClass.this.addMetaMethod(localMethod);
               MethodKey key = new DefaultCachedMethodKey(declaringClass, name, localMethod.getParameterTypes(), false);
               ExpandoMetaClass.this.checkIfGroovyObjectMethod(localMethod);
               ExpandoMetaClass.this.expandoMethods.put(key, localMethod);
            }

         }
      });
   }

   public Object invokeConstructor(Object[] arguments) {
      Class[] argClasses = MetaClassHelper.convertToTypeArray(arguments);
      MetaMethod method = this.pickMethod("<init>", argClasses);
      return method != null && method.getParameterTypes().length == arguments.length ? method.invoke(this.theClass, arguments) : super.invokeConstructor(arguments);
   }

   public MetaClass getMetaClass() {
      return this.myMetaClass;
   }

   public Object getProperty(String property) {
      if (isValidExpandoProperty(property)) {
         if (property.equals("static")) {
            return new ExpandoMetaClass.ExpandoMetaProperty(property, true);
         } else if (property.equals("constructor")) {
            return new ExpandoMetaClass.ExpandoMetaConstructor();
         } else {
            return this.myMetaClass.hasProperty(this, property) == null ? new ExpandoMetaClass.ExpandoMetaProperty(property) : this.myMetaClass.getProperty(this, property);
         }
      } else {
         return this.myMetaClass.getProperty(this, property);
      }
   }

   public static boolean isValidExpandoProperty(String property) {
      return !property.equals("metaClass") && !property.equals("class") && !property.equals("metaMethods") && !property.equals("methods") && !property.equals("properties");
   }

   public Object invokeMethod(String name, Object args) {
      Object[] argsArr = args instanceof Object[] ? (Object[])((Object[])args) : new Object[]{args};
      MetaMethod metaMethod = this.myMetaClass.getMetaMethod(name, argsArr);
      if (metaMethod != null) {
         return metaMethod.doMethodInvoke(this, argsArr);
      } else if (argsArr.length == 2 && argsArr[0] instanceof Class && argsArr[1] instanceof Closure) {
         if (argsArr[0] == this.theClass) {
            this.registerInstanceMethod(name, (Closure)argsArr[1]);
         } else {
            this.registerSubclassInstanceMethod(name, (Class)argsArr[0], (Closure)argsArr[1]);
         }

         return null;
      } else if (argsArr.length == 1 && argsArr[0] instanceof Closure) {
         this.registerInstanceMethod(name, (Closure)argsArr[0]);
         return null;
      } else {
         throw new MissingMethodException(name, this.getClass(), argsArr);
      }
   }

   public void setMetaClass(MetaClass metaClass) {
      this.myMetaClass = metaClass;
   }

   public void setProperty(String property, Object newValue) {
      if (newValue instanceof Closure) {
         if (property.equals("constructor")) {
            property = "<init>";
         }

         Closure callable = (Closure)newValue;
         List<MetaMethod> list = ClosureMetaMethod.createMethodList(property, this.theClass, callable);
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            MetaMethod method = (MetaMethod)i$.next();
            this.registerInstanceMethod(method);
         }
      } else {
         this.registerBeanProperty(property, newValue);
      }

   }

   public ExpandoMetaClass define(Closure closure) {
      ExpandoMetaClass.DefiningClosure definer = new ExpandoMetaClass.DefiningClosure();
      Object delegate = closure.getDelegate();
      closure.setDelegate(definer);
      closure.setResolveStrategy(1);
      closure.call((Object[])null);
      closure.setDelegate(delegate);
      definer.definition = false;
      return this;
   }

   protected synchronized void performOperationOnMetaClass(ExpandoMetaClass.Callable c) {
      try {
         if (this.allowChangesAfterInit) {
            this.setInitialized(false);
         }

         c.call();
      } finally {
         if (this.initCalled) {
            this.setInitialized(true);
         }

      }

   }

   public void registerBeanProperty(final String property, final Object newValue) {
      this.performOperationOnMetaClass(new ExpandoMetaClass.Callable() {
         public void call() {
            Class type = newValue == null ? Object.class : newValue.getClass();
            MetaBeanProperty mbp = newValue instanceof MetaBeanProperty ? (MetaBeanProperty)newValue : new ThreadManagedMetaBeanProperty(ExpandoMetaClass.this.theClass, property, type, newValue);
            MetaMethod getter = ((MetaBeanProperty)mbp).getGetter();
            MethodKey getterKey = new DefaultCachedMethodKey(ExpandoMetaClass.this.theClass, getter.getName(), CachedClass.EMPTY_ARRAY, false);
            MetaMethod setter = ((MetaBeanProperty)mbp).getSetter();
            MethodKey setterKey = new DefaultCachedMethodKey(ExpandoMetaClass.this.theClass, setter.getName(), setter.getParameterTypes(), false);
            ExpandoMetaClass.this.addMetaMethod(getter);
            ExpandoMetaClass.this.addMetaMethod(setter);
            ExpandoMetaClass.this.expandoMethods.put(setterKey, setter);
            ExpandoMetaClass.this.expandoMethods.put(getterKey, getter);
            ExpandoMetaClass.this.expandoProperties.put(((MetaBeanProperty)mbp).getName(), mbp);
            ExpandoMetaClass.this.addMetaBeanProperty((MetaBeanProperty)mbp);
            ExpandoMetaClass.this.performRegistryCallbacks();
         }
      });
   }

   public void registerInstanceMethod(final MetaMethod metaMethod) {
      final boolean inited = this.initCalled;
      this.performOperationOnMetaClass(new ExpandoMetaClass.Callable() {
         public void call() {
            String methodName = metaMethod.getName();
            ExpandoMetaClass.this.checkIfGroovyObjectMethod(metaMethod);
            MethodKey key = new DefaultCachedMethodKey(ExpandoMetaClass.this.theClass, methodName, metaMethod.getParameterTypes(), false);
            if (ExpandoMetaClass.this.isInitialized()) {
               throw new RuntimeException("Already initialized, cannot add new method: " + metaMethod);
            } else {
               ExpandoMetaClass.this.addMetaMethodToIndex(metaMethod, ExpandoMetaClass.this.metaMethodIndex.getHeader(ExpandoMetaClass.this.theClass));
               ExpandoMetaClass.this.dropMethodCache(methodName);
               ExpandoMetaClass.this.expandoMethods.put(key, metaMethod);
               String propertyName;
               if (inited && ExpandoMetaClass.this.isGetter(methodName, metaMethod.getParameterTypes())) {
                  propertyName = ExpandoMetaClass.this.getPropertyForGetter(methodName);
                  ExpandoMetaClass.this.registerBeanPropertyForMethod(metaMethod, propertyName, true, false);
               } else if (inited && ExpandoMetaClass.this.isSetter(methodName, metaMethod.getParameterTypes())) {
                  propertyName = ExpandoMetaClass.this.getPropertyForSetter(methodName);
                  ExpandoMetaClass.this.registerBeanPropertyForMethod(metaMethod, propertyName, false, false);
               }

               ExpandoMetaClass.this.performRegistryCallbacks();
            }
         }
      });
   }

   public void registerInstanceMethod(String name, Closure closure) {
      List<MetaMethod> list = ClosureMetaMethod.createMethodList(name, this.theClass, closure);
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         MetaMethod method = (MetaMethod)i$.next();
         this.registerInstanceMethod(method);
      }

   }

   public List<MetaMethod> getMethods() {
      List<MetaMethod> methodList = new ArrayList();
      methodList.addAll(this.expandoMethods.values());
      methodList.addAll(super.getMethods());
      return methodList;
   }

   public List<MetaProperty> getProperties() {
      List<MetaProperty> propertyList = new ArrayList();
      propertyList.addAll(super.getProperties());
      return propertyList;
   }

   private void performRegistryCallbacks() {
      MetaClassRegistry registry = GroovySystem.getMetaClassRegistry();
      this.incVersion();
      if (!this.modified) {
         this.modified = true;
         if (this.inRegistry) {
            MetaClass currMetaClass = registry.getMetaClass(this.theClass);
            if (!(currMetaClass instanceof ExpandoMetaClass) && currMetaClass instanceof AdaptingMetaClass) {
               ((AdaptingMetaClass)currMetaClass).setAdaptee(this);
            } else {
               registry.setMetaClass(this.theClass, this);
            }
         }
      }

   }

   private void registerBeanPropertyForMethod(MetaMethod metaMethod, String propertyName, boolean getter, boolean isStatic) {
      Map<String, MetaProperty> propertyCache = isStatic ? this.staticBeanPropertyCache : this.beanPropertyCache;
      MetaBeanProperty beanProperty = (MetaBeanProperty)propertyCache.get(propertyName);
      if (beanProperty == null) {
         if (getter) {
            beanProperty = new MetaBeanProperty(propertyName, Object.class, metaMethod, (MetaMethod)null);
         } else {
            beanProperty = new MetaBeanProperty(propertyName, Object.class, (MetaMethod)null, metaMethod);
         }

         propertyCache.put(propertyName, beanProperty);
      } else {
         MetaMethod setterMethod;
         if (getter) {
            setterMethod = beanProperty.getSetter();
            Class type = setterMethod != null ? setterMethod.getParameterTypes()[0].getTheClass() : Object.class;
            beanProperty = new MetaBeanProperty(propertyName, type, metaMethod, setterMethod);
            propertyCache.put(propertyName, beanProperty);
         } else {
            setterMethod = beanProperty.getGetter();
            beanProperty = new MetaBeanProperty(propertyName, metaMethod.getParameterTypes()[0].getTheClass(), setterMethod, metaMethod);
            propertyCache.put(propertyName, beanProperty);
         }
      }

      this.expandoProperties.put(beanProperty.getName(), beanProperty);
      this.addMetaBeanProperty(beanProperty);
   }

   protected void registerStaticMethod(String name, Closure callable) {
      this.registerStaticMethod(name, callable, (Class[])null);
   }

   protected void registerStaticMethod(final String name, final Closure callable, final Class[] paramTypes) {
      this.performOperationOnMetaClass(new ExpandoMetaClass.Callable() {
         public void call() {
            String methodName;
            if (name.equals("methodMissing")) {
               methodName = "$static_methodMissing";
            } else if (name.equals("propertyMissing")) {
               methodName = "$static_propertyMissing";
            } else {
               methodName = name;
            }

            ClosureStaticMetaMethod metaMethod = null;
            if (paramTypes != null) {
               metaMethod = new ClosureStaticMetaMethod(methodName, ExpandoMetaClass.this.theClass, callable, paramTypes);
            } else {
               metaMethod = new ClosureStaticMetaMethod(methodName, ExpandoMetaClass.this.theClass, callable);
            }

            if (methodName.equals("invokeMethod") && callable.getParameterTypes().length == 2) {
               ExpandoMetaClass.this.invokeStaticMethodMethod = metaMethod;
            } else {
               if (methodName.equals("methodMissing")) {
                  methodName = "$static_methodMissing";
               }

               MethodKey key = new DefaultCachedMethodKey(ExpandoMetaClass.this.theClass, methodName, metaMethod.getParameterTypes(), false);
               ExpandoMetaClass.this.addMetaMethod(metaMethod);
               ExpandoMetaClass.this.dropStaticMethodCache(methodName);
               String propertyName;
               if (ExpandoMetaClass.this.isGetter(methodName, metaMethod.getParameterTypes())) {
                  propertyName = ExpandoMetaClass.this.getPropertyForGetter(methodName);
                  ExpandoMetaClass.this.registerBeanPropertyForMethod(metaMethod, propertyName, true, true);
               } else if (ExpandoMetaClass.this.isSetter(methodName, metaMethod.getParameterTypes())) {
                  propertyName = ExpandoMetaClass.this.getPropertyForSetter(methodName);
                  ExpandoMetaClass.this.registerBeanPropertyForMethod(metaMethod, propertyName, false, true);
               }

               ExpandoMetaClass.this.performRegistryCallbacks();
               ExpandoMetaClass.this.expandoMethods.put(key, metaMethod);
            }

         }
      });
   }

   protected Object getSubclassMetaMethods(String methodName) {
      return !this.isModified() ? null : this.expandoSubclassMethods.get(methodName);
   }

   public Class getJavaClass() {
      return this.theClass;
   }

   public void refreshInheritedMethods(Set modifiedSuperExpandos) {
      Iterator i = modifiedSuperExpandos.iterator();

      while(i.hasNext()) {
         ExpandoMetaClass superExpando = (ExpandoMetaClass)i.next();
         if (superExpando != this) {
            this.refreshInheritedMethods(superExpando);
         }
      }

   }

   private void refreshInheritedMethods(ExpandoMetaClass superExpando) {
      List<MetaMethod> metaMethods = superExpando.getExpandoMethods();
      Iterator i$ = metaMethods.iterator();

      while(i$.hasNext()) {
         MetaMethod metaMethod = (MetaMethod)i$.next();
         if (metaMethod.isStatic()) {
            if (superExpando.getTheClass() == this.getTheClass()) {
               this.registerStaticMethod(metaMethod.getName(), (Closure)((ClosureStaticMetaMethod)metaMethod).getClosure().clone());
            }
         } else {
            this.addSuperMethodIfNotOverridden(metaMethod);
         }
      }

      Collection<MetaProperty> metaProperties = superExpando.getExpandoProperties();
      Iterator i$ = metaProperties.iterator();

      while(i$.hasNext()) {
         Object metaProperty = i$.next();
         MetaBeanProperty property = (MetaBeanProperty)metaProperty;
         this.expandoProperties.put(property.getName(), property);
         this.addMetaBeanProperty(property);
      }

   }

   public List<MetaMethod> getExpandoMethods() {
      return Collections.unmodifiableList(DefaultGroovyMethods.toList(this.expandoMethods.values()));
   }

   public Collection<MetaProperty> getExpandoProperties() {
      return Collections.unmodifiableCollection(this.expandoProperties.values());
   }

   public Object invokeMethod(Class sender, Object object, String methodName, Object[] originalArguments, boolean isCallToSuper, boolean fromInsideClass) {
      return this.invokeMethodMethod != null ? this.invokeMethodMethod.invoke(object, new Object[]{methodName, originalArguments}) : super.invokeMethod(sender, object, methodName, originalArguments, isCallToSuper, fromInsideClass);
   }

   public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
      return this.invokeStaticMethodMethod != null ? this.invokeStaticMethodMethod.invoke(object, new Object[]{methodName, arguments}) : super.invokeStaticMethod(object, methodName, arguments);
   }

   public Object getProperty(Class sender, Object object, String name, boolean useSuper, boolean fromInsideClass) {
      if (this.hasOverrideGetProperty(name) && this.getJavaClass().isInstance(object)) {
         return this.getPropertyMethod.invoke(object, new Object[]{name});
      } else {
         return "mixedIn".equals(name) ? new ExpandoMetaClass.MixedInAccessor(object, this.mixinClasses) : super.getProperty(sender, object, name, useSuper, fromInsideClass);
      }
   }

   public Object getProperty(Object object, String name) {
      return this.hasOverrideGetProperty(name) && this.getJavaClass().isInstance(object) ? this.getPropertyMethod.invoke(object, new Object[]{name}) : super.getProperty(object, name);
   }

   private boolean hasOverrideGetProperty(String name) {
      return this.getPropertyMethod != null && !name.equals("metaClass") && !name.equals("class");
   }

   public void setProperty(Class sender, Object object, String name, Object newValue, boolean useSuper, boolean fromInsideClass) {
      if (this.setPropertyMethod != null && !name.equals("metaClass") && this.getJavaClass().isInstance(object)) {
         this.setPropertyMethod.invoke(object, new Object[]{name, newValue});
      } else {
         super.setProperty(sender, object, name, newValue, useSuper, fromInsideClass);
      }
   }

   public MetaProperty getMetaProperty(String name) {
      MetaProperty mp = (MetaProperty)this.expandoProperties.get(name);
      return mp != null ? mp : super.getMetaProperty(name);
   }

   public boolean hasMetaProperty(String name) {
      return this.getMetaProperty(name) != null;
   }

   public boolean hasMetaMethod(String name, Class[] args) {
      return super.pickMethod(name, args) != null;
   }

   private static boolean isPropertyName(String name) {
      return name.length() > 0 && Character.isUpperCase(name.charAt(0)) || name.length() > 1 && Character.isUpperCase(name.charAt(1));
   }

   private boolean isGetter(String name, CachedClass[] args) {
      if (name != null && name.length() != 0 && args != null) {
         if (args.length != 0) {
            return false;
         } else if (name.startsWith("get")) {
            name = name.substring(3);
            return isPropertyName(name);
         } else if (name.startsWith("is")) {
            name = name.substring(2);
            return isPropertyName(name);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private String getPropertyForGetter(String getterName) {
      if (getterName != null && getterName.length() != 0) {
         String prop;
         if (getterName.startsWith("get")) {
            prop = getterName.substring(3);
            return this.convertPropertyName(prop);
         } else if (getterName.startsWith("is")) {
            prop = getterName.substring(2);
            return this.convertPropertyName(prop);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private String convertPropertyName(String prop) {
      return Character.isDigit(prop.charAt(0)) ? prop : Introspector.decapitalize(prop);
   }

   public String getPropertyForSetter(String setterName) {
      if (setterName != null && setterName.length() != 0) {
         if (setterName.startsWith("set")) {
            String prop = setterName.substring(3);
            return this.convertPropertyName(prop);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public boolean isSetter(String name, CachedClass[] args) {
      if (name != null && name.length() != 0 && args != null) {
         if (name.startsWith("set")) {
            if (args.length != 1) {
               return false;
            } else {
               name = name.substring(3);
               return isPropertyName(name);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public CallSite createPojoCallSite(CallSite site, Object receiver, Object[] args) {
      return (CallSite)(this.invokeMethodMethod != null ? new PojoMetaClassSite(site, this) : super.createPojoCallSite(site, receiver, args));
   }

   public CallSite createStaticSite(CallSite site, Object[] args) {
      return (CallSite)(this.invokeStaticMethodMethod != null ? new StaticMetaClassSite(site, this) : super.createStaticSite(site, args));
   }

   public CallSite createPogoCallSite(CallSite site, Object[] args) {
      return (CallSite)(this.invokeMethodMethod != null ? new PogoMetaClassSite(site, this) : super.createPogoCallSite(site, args));
   }

   public CallSite createPogoCallCurrentSite(CallSite site, Class sender, String name, Object[] args) {
      return (CallSite)(this.invokeMethodMethod != null ? new PogoMetaClassSite(site, this) : super.createPogoCallCurrentSite(site, sender, args));
   }

   public CallSite createConstructorSite(CallSite site, Object[] args) {
      Class[] params = MetaClassHelper.convertToTypeArray(args);
      MetaMethod method = this.pickMethod("<init>", params);
      return (CallSite)(method != null && method.getParameterTypes().length == args.length && method.getDeclaringClass().getTheClass().equals(this.getTheClass()) ? new ConstructorMetaMethodSite(site, this, method, params) : super.createConstructorSite(site, args));
   }

   private static class MixedInAccessor {
      private final Object object;
      private final Set<MixinInMetaClass> mixinClasses;

      public MixedInAccessor(Object object, Set<MixinInMetaClass> mixinClasses) {
         this.object = object;
         this.mixinClasses = mixinClasses;
      }

      public Object getAt(Class key) {
         if (key.isAssignableFrom(this.object.getClass())) {
            return new GroovyObjectSupport() {
               {
                  MetaClass ownMetaClass = InvokerHelper.getMetaClass(MixedInAccessor.this.object.getClass());
                  this.setMetaClass(new OwnedMetaClass(ownMetaClass) {
                     protected Object getOwner() {
                        return MixedInAccessor.this.object;
                     }

                     protected MetaClass getOwnerMetaClass(Object owner) {
                        return this.getAdaptee();
                     }
                  });
               }
            };
         } else {
            Iterator i$ = this.mixinClasses.iterator();

            final MixinInMetaClass mixin;
            do {
               if (!i$.hasNext()) {
                  throw new RuntimeException("Class " + key + " isn't mixed in " + this.object.getClass());
               }

               mixin = (MixinInMetaClass)i$.next();
            } while(!key.isAssignableFrom(mixin.getMixinClass().getTheClass()));

            return new GroovyObjectSupport() {
               {
                  final Object mixedInInstance = mixin.getMixinInstance(MixedInAccessor.this.object);
                  this.setMetaClass(new OwnedMetaClass(InvokerHelper.getMetaClass(mixedInInstance)) {
                     protected Object getOwner() {
                        return mixedInInstance;
                     }

                     protected MetaClass getOwnerMetaClass(Object owner) {
                        return ((MixedInMetaClass)this.getAdaptee()).getAdaptee();
                     }
                  });
               }
            };
         }
      }

      public void putAt(Class key, Object value) {
         Iterator i$ = this.mixinClasses.iterator();

         MixinInMetaClass mixin;
         do {
            if (!i$.hasNext()) {
               throw new RuntimeException("Class " + key + " isn't mixed in " + this.object.getClass());
            }

            mixin = (MixinInMetaClass)i$.next();
         } while(mixin.getMixinClass().getTheClass() != key);

         mixin.setMixinInstance(this.object, value);
      }
   }

   private class StaticDefiningClosure extends ExpandoMetaClass.ExpandoMetaProperty {
      protected StaticDefiningClosure() {
         super("static", true);
      }

      public Object invokeMethod(String name, Object obj) {
         if (obj instanceof Object[]) {
            Object[] args = (Object[])((Object[])obj);
            if (args.length == 1 && args[0] instanceof Closure) {
               ExpandoMetaClass.this.registerStaticMethod(name, (Closure)args[0]);
               return null;
            }
         }

         throw new MissingMethodException(name, this.getClass(), obj instanceof Object[] ? (Object[])((Object[])obj) : new Object[]{obj});
      }
   }

   private class DefiningClosure extends GroovyObjectSupport {
      boolean definition;

      private DefiningClosure() {
         this.definition = true;
      }

      public void mixin(Class category) {
         this.mixin(Collections.singletonList(category));
      }

      public void mixin(List categories) {
         DefaultGroovyMethods.mixin((MetaClass)ExpandoMetaClass.this, (List)categories);
      }

      public void mixin(Class[] categories) {
         DefaultGroovyMethods.mixin((MetaClass)ExpandoMetaClass.this, (Class[])categories);
      }

      public void define(Class subClass, Closure closure) {
         ExpandoMetaClass.SubClassDefiningClosure definer = ExpandoMetaClass.this.new SubClassDefiningClosure(subClass);
         closure.setDelegate(definer);
         closure.setResolveStrategy(1);
         closure.call((Object[])null);
      }

      public Object invokeMethod(String name, Object obj) {
         try {
            return this.getMetaClass().invokeMethod(this, name, obj);
         } catch (MissingMethodException var6) {
            if (obj instanceof Object[]) {
               if ("static".equals(name)) {
                  ExpandoMetaClass.StaticDefiningClosure staticDef = ExpandoMetaClass.this.new StaticDefiningClosure();
                  Closure c = (Closure)((Object[])((Object[])obj))[0];
                  c.setDelegate(staticDef);
                  c.setResolveStrategy(3);
                  c.call((Object[])null);
                  return null;
               } else {
                  Object[] args = (Object[])((Object[])obj);
                  if (args.length == 1 && args[0] instanceof Closure) {
                     ExpandoMetaClass.this.registerInstanceMethod(name, (Closure)args[0]);
                  } else if (args.length == 2 && args[0] instanceof Class && args[1] instanceof Closure) {
                     ExpandoMetaClass.this.registerSubclassInstanceMethod(name, (Class)args[0], (Closure)args[1]);
                  } else {
                     ExpandoMetaClass.this.setProperty(name, ((Object[])((Object[])obj))[0]);
                  }

                  return null;
               }
            } else {
               throw var6;
            }
         }
      }

      public void setProperty(String property, Object newValue) {
         ExpandoMetaClass.this.setProperty(property, newValue);
      }

      public Object getProperty(String property) {
         if ("static".equals(property)) {
            return ExpandoMetaClass.this.new StaticDefiningClosure();
         } else if (this.definition) {
            return ExpandoMetaClass.this.new ExpandoMetaProperty(property);
         } else {
            throw new MissingPropertyException(property, this.getClass());
         }
      }

      // $FF: synthetic method
      DefiningClosure(Object x1) {
         this();
      }
   }

   private class SubClassDefiningClosure extends GroovyObjectSupport {
      private final Class klazz;

      public SubClassDefiningClosure(Class klazz) {
         this.klazz = klazz;
      }

      public Object invokeMethod(String name, Object obj) {
         if (obj instanceof Object[]) {
            Object[] args = (Object[])((Object[])obj);
            if (args.length == 1 && args[0] instanceof Closure) {
               ExpandoMetaClass.this.registerSubclassInstanceMethod(name, this.klazz, (Closure)args[0]);
               return null;
            }
         }

         throw new MissingMethodException(name, this.getClass(), new Object[]{obj});
      }
   }

   protected class ExpandoMetaConstructor extends GroovyObjectSupport {
      public Object leftShift(Closure c) {
         if (c != null) {
            List<MetaMethod> list = ClosureMetaMethod.createMethodList("<init>", ExpandoMetaClass.this.theClass, c);
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               MetaMethod method = (MetaMethod)i$.next();
               Class[] paramTypes = method.getNativeParameterTypes();
               Constructor ctor = ExpandoMetaClass.this.retrieveConstructor(paramTypes);
               if (ctor != null) {
                  throw new GroovyRuntimeException("Cannot add new constructor for arguments [" + DefaultGroovyMethods.inspect(paramTypes) + "]. It already exists!");
               }

               ExpandoMetaClass.this.registerInstanceMethod(method);
            }
         }

         return this;
      }
   }

   protected class ExpandoMetaProperty extends GroovyObjectSupport {
      protected String propertyName;
      protected boolean isStatic;

      protected ExpandoMetaProperty(String name) {
         this(name, false);
      }

      protected ExpandoMetaProperty(String name, boolean isStatic) {
         this.propertyName = name;
         this.isStatic = isStatic;
      }

      public String getPropertyName() {
         return this.propertyName;
      }

      public boolean isStatic() {
         return this.isStatic;
      }

      public Object leftShift(Object arg) {
         this.registerIfClosure(arg, false);
         return this;
      }

      private void registerIfClosure(Object arg, boolean replace) {
         if (arg instanceof Closure) {
            Closure callable = (Closure)arg;
            List<MetaMethod> list = ClosureMetaMethod.createMethodList(this.propertyName, ExpandoMetaClass.this.theClass, callable);
            if (list.isEmpty() && this.isStatic) {
               Class[] paramTypesx = callable.getParameterTypes();
               this.registerStatic(callable, replace, paramTypesx);
               return;
            }

            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               MetaMethod method = (MetaMethod)i$.next();
               Class[] paramTypes = method.getNativeParameterTypes();
               if (this.isStatic) {
                  this.registerStatic(callable, replace, paramTypes);
               } else {
                  this.registerInstance(method, replace, paramTypes);
               }
            }
         }

      }

      private void registerStatic(Closure callable, boolean replace, Class[] paramTypes) {
         Method foundMethod = this.checkIfMethodExists(ExpandoMetaClass.this.theClass, this.propertyName, paramTypes, true);
         if (foundMethod != null && !replace) {
            throw new GroovyRuntimeException("Cannot add new static method [" + this.propertyName + "] for arguments [" + DefaultGroovyMethods.inspect(paramTypes) + "]. It already exists!");
         } else {
            ExpandoMetaClass.this.registerStaticMethod(this.propertyName, callable, paramTypes);
         }
      }

      private void registerInstance(MetaMethod method, boolean replace, Class[] paramTypes) {
         Method foundMethod = this.checkIfMethodExists(ExpandoMetaClass.this.theClass, this.propertyName, paramTypes, false);
         if (foundMethod != null && !replace) {
            throw new GroovyRuntimeException("Cannot add new method [" + this.propertyName + "] for arguments [" + DefaultGroovyMethods.inspect(paramTypes) + "]. It already exists!");
         } else {
            ExpandoMetaClass.this.registerInstanceMethod(method);
         }
      }

      private Method checkIfMethodExists(Class methodClass, String methodName, Class[] paramTypes, boolean staticMethod) {
         Method foundMethod = null;
         Method[] methods = methodClass.getMethods();
         Method[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if (method.getName().equals(methodName) && Modifier.isStatic(method.getModifiers()) == staticMethod && MetaClassHelper.parametersAreCompatible(paramTypes, method.getParameterTypes())) {
               foundMethod = method;
               break;
            }
         }

         return foundMethod;
      }

      public Object getProperty(String property) {
         this.propertyName = property;
         return this;
      }

      public void setProperty(String property, Object newValue) {
         this.propertyName = property;
         this.registerIfClosure(newValue, true);
      }
   }

   private interface Callable {
      void call();
   }
}
