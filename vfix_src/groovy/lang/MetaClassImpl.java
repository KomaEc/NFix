package groovy.lang;

import groovyjarjarasm.asm.ClassVisitor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.BytecodeHelper;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedConstructor;
import org.codehaus.groovy.reflection.CachedField;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.reflection.GeneratedMetaMethod;
import org.codehaus.groovy.reflection.ParameterTypes;
import org.codehaus.groovy.reflection.ReflectionCache;
import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.CurriedClosure;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.MethodClosure;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.ConstructorSite;
import org.codehaus.groovy.runtime.callsite.MetaClassConstructorSite;
import org.codehaus.groovy.runtime.callsite.PogoMetaClassSite;
import org.codehaus.groovy.runtime.callsite.PogoMetaMethodSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaClassSite;
import org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;
import org.codehaus.groovy.runtime.callsite.StaticMetaClassSite;
import org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite;
import org.codehaus.groovy.runtime.metaclass.ClosureMetaMethod;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.codehaus.groovy.runtime.metaclass.MetaMethodIndex;
import org.codehaus.groovy.runtime.metaclass.MethodSelectionException;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExecutionFailed;
import org.codehaus.groovy.runtime.metaclass.MissingPropertyExceptionNoStack;
import org.codehaus.groovy.runtime.metaclass.MixinInstanceMetaMethod;
import org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod;
import org.codehaus.groovy.runtime.metaclass.NewMetaMethod;
import org.codehaus.groovy.runtime.metaclass.NewStaticMetaMethod;
import org.codehaus.groovy.runtime.metaclass.TransformMetaMethod;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.NumberMathModificationInfo;
import org.codehaus.groovy.runtime.wrappers.Wrapper;
import org.codehaus.groovy.util.ComplexKeyHashMap;
import org.codehaus.groovy.util.FastArray;
import org.codehaus.groovy.util.SingleKeyHashMap;

public class MetaClassImpl implements MetaClass, MutableMetaClass {
   private static final String CLOSURE_CALL_METHOD = "call";
   private static final String CLOSURE_DO_CALL_METHOD = "doCall";
   private static final String CLOSURE_CURRY_METHOD = "curry";
   protected static final String STATIC_METHOD_MISSING = "$static_methodMissing";
   protected static final String STATIC_PROPERTY_MISSING = "$static_propertyMissing";
   protected static final String METHOD_MISSING = "methodMissing";
   protected static final String PROPERTY_MISSING = "propertyMissing";
   private static final String GET_PROPERTY_METHOD = "getProperty";
   private static final String SET_PROPERTY_METHOD = "setProperty";
   protected static final String INVOKE_METHOD_METHOD = "invokeMethod";
   private static final Class[] METHOD_MISSING_ARGS = new Class[]{String.class, Object.class};
   private static final Class[] GETTER_MISSING_ARGS = new Class[]{String.class};
   private static final Class[] SETTER_MISSING_ARGS;
   protected final Class theClass;
   protected final CachedClass theCachedClass;
   private static final MetaMethod[] EMPTY;
   protected MetaMethod getPropertyMethod;
   protected MetaMethod invokeMethodMethod;
   protected MetaMethod setPropertyMethod;
   protected MetaClassRegistry registry;
   protected final boolean isGroovyObject;
   protected final boolean isMap;
   private ClassNode classNode;
   private final MetaClassImpl.Index classPropertyIndex;
   private MetaClassImpl.Index classPropertyIndexForSuper;
   private final SingleKeyHashMap staticPropertyIndex;
   private final Map<String, MetaMethod> listeners;
   private FastArray constructors;
   private final List<MetaMethod> allMethods;
   private boolean initialized;
   private final MetaProperty arrayLengthProperty;
   private static final MetaMethod AMBIGUOUS_LISTENER_METHOD;
   private static final Object[] EMPTY_ARGUMENTS;
   private final Set<MetaMethod> newGroovyMethodsSet;
   private MetaMethod genericGetMethod;
   private MetaMethod genericSetMethod;
   private MetaMethod propertyMissingGet;
   private MetaMethod propertyMissingSet;
   private MetaMethod methodMissing;
   private MetaMethodIndex.Header mainClassMethodHeader;
   protected final MetaMethodIndex metaMethodIndex;
   private final MetaMethod[] myNewMetaMethods;
   private final MetaMethod[] additionalMetaMethods;
   private static final HashMap<String, String> propNames;
   private static final SingleKeyHashMap.Copier NAME_INDEX_COPIER;
   private static final SingleKeyHashMap.Copier METHOD_INDEX_COPIER;

   public final CachedClass getTheCachedClass() {
      return this.theCachedClass;
   }

   public MetaClassImpl(Class theClass, MetaMethod[] add) {
      this.classPropertyIndex = new MetaClassImpl.MethodIndex();
      this.classPropertyIndexForSuper = new MetaClassImpl.MethodIndex();
      this.staticPropertyIndex = new SingleKeyHashMap();
      this.listeners = new HashMap();
      this.allMethods = new ArrayList();
      this.arrayLengthProperty = new MetaArrayLengthProperty();
      this.newGroovyMethodsSet = new HashSet();
      this.theClass = theClass;
      this.theCachedClass = ReflectionCache.getCachedClass(theClass);
      this.isGroovyObject = GroovyObject.class.isAssignableFrom(theClass);
      this.isMap = Map.class.isAssignableFrom(theClass);
      this.registry = GroovySystem.getMetaClassRegistry();
      this.metaMethodIndex = new MetaMethodIndex(this.theCachedClass);
      MetaMethod[] metaMethods = this.theCachedClass.getNewMetaMethods();
      if (add != null && add.length != 0) {
         ArrayList<MetaMethod> arr = new ArrayList();
         arr.addAll(Arrays.asList(metaMethods));
         arr.addAll(Arrays.asList(add));
         this.myNewMetaMethods = (MetaMethod[])arr.toArray(new MetaMethod[arr.size()]);
         this.additionalMetaMethods = metaMethods;
      } else {
         this.myNewMetaMethods = metaMethods;
         this.additionalMetaMethods = EMPTY;
      }

   }

   public MetaClassImpl(Class theClass) {
      this((Class)theClass, (MetaMethod[])null);
   }

   public MetaClassImpl(MetaClassRegistry registry, Class theClass, MetaMethod[] add) {
      this(theClass, add);
      this.registry = registry;
      this.constructors = new FastArray(this.theCachedClass.getConstructors());
   }

   public MetaClassImpl(MetaClassRegistry registry, Class theClass) {
      this(registry, theClass, (MetaMethod[])null);
   }

   public List respondsTo(Object obj, String name, Object[] argTypes) {
      Class[] classes = MetaClassHelper.castArgumentsToClassArray(argTypes);
      MetaMethod m = this.getMetaMethod(name, classes);
      List<MetaMethod> methods = new ArrayList();
      if (m != null) {
         methods.add(m);
      }

      return methods;
   }

   public List respondsTo(Object obj, String name) {
      Object o = this.getMethods(this.getTheClass(), name, false);
      return o instanceof FastArray ? ((FastArray)o).toList() : Collections.singletonList(o);
   }

   public MetaProperty hasProperty(Object obj, String name) {
      return this.getMetaProperty(name);
   }

   public MetaProperty getMetaProperty(String name) {
      SingleKeyHashMap propertyMap = this.classPropertyIndex.getNotNull(this.theCachedClass);
      if (propertyMap.containsKey(name)) {
         return (MetaProperty)propertyMap.get(name);
      } else if (this.staticPropertyIndex.containsKey(name)) {
         return (MetaProperty)this.staticPropertyIndex.get(name);
      } else {
         propertyMap = this.classPropertyIndexForSuper.getNotNull(this.theCachedClass);
         if (propertyMap.containsKey(name)) {
            return (MetaProperty)propertyMap.get(name);
         } else {
            for(CachedClass superClass = this.theCachedClass; superClass != null && superClass != ReflectionCache.OBJECT_CLASS; superClass = superClass.getCachedSuperClass()) {
               MetaBeanProperty property = this.findPropertyInClassHierarchy(name, superClass);
               if (property != null) {
                  this.onSuperPropertyFoundInHierarchy(property);
                  return property;
               }
            }

            return null;
         }
      }
   }

   public MetaMethod getStaticMetaMethod(String name, Object[] argTypes) {
      Class[] classes = MetaClassHelper.castArgumentsToClassArray(argTypes);
      return this.pickStaticMethod(name, classes);
   }

   public MetaMethod getMetaMethod(String name, Object[] argTypes) {
      Class[] classes = MetaClassHelper.castArgumentsToClassArray(argTypes);
      return this.pickMethod(name, classes);
   }

   public Class getTheClass() {
      return this.theClass;
   }

   public boolean isGroovyObject() {
      return this.isGroovyObject;
   }

   private void fillMethodIndex() {
      this.mainClassMethodHeader = this.metaMethodIndex.getHeader(this.theClass);
      LinkedList superClasses = this.getSuperClasses();
      CachedClass firstGroovySuper = this.calcFirstGroovySuperClass(superClasses);
      Set<CachedClass> interfaces = this.theCachedClass.getInterfaces();
      this.addInterfaceMethods(interfaces);
      this.populateMethods(superClasses, firstGroovySuper);
      this.inheritInterfaceNewMetaMethods(interfaces);
      if (this.isGroovyObject) {
         this.metaMethodIndex.copyMethodsToSuper();
         this.connectMultimethods(superClasses, firstGroovySuper);
         this.removeMultimethodsOverloadedWithPrivateMethods();
         this.replaceWithMOPCalls(this.theCachedClass.mopMethods);
      }

   }

   private void populateMethods(LinkedList superClasses, CachedClass firstGroovySuper) {
      Iterator iter = superClasses.iterator();
      MetaMethodIndex.Header header = this.metaMethodIndex.getHeader(firstGroovySuper.getTheClass());

      CachedClass c;
      CachedMethod[] arr$;
      int len$;
      int i$;
      CachedMethod metaMethod;
      MetaMethod[] arr$;
      while(iter.hasNext()) {
         c = (CachedClass)iter.next();
         CachedMethod[] cachedMethods = c.getMethods();
         arr$ = cachedMethods;
         len$ = cachedMethods.length;

         for(i$ = 0; i$ < len$; ++i$) {
            metaMethod = arr$[i$];
            this.addToAllMethodsIfPublic(metaMethod);
            if (!metaMethod.isPrivate() || c == firstGroovySuper) {
               this.addMetaMethodToIndex(metaMethod, header);
            }
         }

         arr$ = this.getNewMetaMethods(c);
         MetaMethod[] arr$ = arr$;
         i$ = arr$.length;

         for(int i$ = 0; i$ < i$; ++i$) {
            MetaMethod method = arr$[i$];
            if (!this.newGroovyMethodsSet.contains(method)) {
               this.newGroovyMethodsSet.add(method);
               this.addMetaMethodToIndex(method, header);
            }
         }

         if (c == firstGroovySuper) {
            break;
         }
      }

      MetaMethodIndex.Header last = header;

      while(iter.hasNext()) {
         c = (CachedClass)iter.next();
         header = this.metaMethodIndex.getHeader(c.getTheClass());
         if (last != null) {
            this.metaMethodIndex.copyNonPrivateMethods(last, header);
         }

         last = header;
         arr$ = c.getMethods();
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            metaMethod = arr$[i$];
            this.addToAllMethodsIfPublic(metaMethod);
            this.addMetaMethodToIndex(metaMethod, header);
         }

         arr$ = this.getNewMetaMethods(c);
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            MetaMethod method = arr$[i$];
            if (!this.newGroovyMethodsSet.contains(method)) {
               this.newGroovyMethodsSet.add(method);
               this.addMetaMethodToIndex(method, header);
            }
         }
      }

   }

   private MetaMethod[] getNewMetaMethods(CachedClass c) {
      return this.theCachedClass != c ? c.getNewMetaMethods() : this.myNewMetaMethods;
   }

   private void addInterfaceMethods(Set<CachedClass> interfaces) {
      MetaMethodIndex.Header header = this.metaMethodIndex.getHeader(this.theClass);
      Iterator i$ = interfaces.iterator();

      while(i$.hasNext()) {
         CachedClass c = (CachedClass)i$.next();
         CachedMethod[] m = c.getMethods();

         for(int i = 0; i != m.length; ++i) {
            MetaMethod method = m[i];
            this.addMetaMethodToIndex(method, header);
         }
      }

   }

   protected LinkedList<CachedClass> getSuperClasses() {
      LinkedList<CachedClass> superClasses = new LinkedList();
      if (this.theClass.isInterface()) {
         superClasses.addFirst(ReflectionCache.OBJECT_CLASS);
      } else {
         for(CachedClass c = this.theCachedClass; c != null; c = c.getCachedSuperClass()) {
            superClasses.addFirst(c);
         }

         if (this.theCachedClass.isArray && this.theClass != Object[].class && !this.theClass.getComponentType().isPrimitive()) {
            superClasses.addFirst(ReflectionCache.OBJECT_ARRAY_CLASS);
         }
      }

      return superClasses;
   }

   private void removeMultimethodsOverloadedWithPrivateMethods() {
      MetaClassImpl.MethodIndexAction mia = new MetaClassImpl.MethodIndexAction() {
         public boolean skipClass(Class clazz) {
            return clazz == MetaClassImpl.this.theClass;
         }

         public void methodNameAction(Class clazz, MetaMethodIndex.Entry e) {
            if (e.methods != null) {
               boolean hasPrivate = false;
               if (e.methods instanceof FastArray) {
                  FastArray methods = (FastArray)e.methods;
                  int len = methods.size();
                  Object[] data = methods.getArray();

                  for(int i = 0; i != len; ++i) {
                     MetaMethod method = (MetaMethod)data[i];
                     if (method.isPrivate() && clazz == method.getDeclaringClass().getTheClass()) {
                        hasPrivate = true;
                        break;
                     }
                  }
               } else {
                  MetaMethod methodx = (MetaMethod)e.methods;
                  if (methodx.isPrivate() && clazz == methodx.getDeclaringClass().getTheClass()) {
                     hasPrivate = true;
                  }
               }

               if (hasPrivate) {
                  Object o = e.methodsForSuper;
                  if (o instanceof FastArray) {
                     e.methods = ((FastArray)o).copy();
                  } else {
                     e.methods = o;
                  }

               }
            }
         }
      };
      mia.iterate();
   }

   private void replaceWithMOPCalls(final CachedMethod[] mopMethods) {
      if (this.isGroovyObject) {
         class MOPIter extends MetaClassImpl.MethodIndexAction {
            boolean useThis;

            MOPIter() {
               super(null);
            }

            public boolean skipClass(CachedClass clazz) {
               return !this.useThis && clazz == MetaClassImpl.this.theCachedClass;
            }

            public void methodNameAction(Class clazz, MetaMethodIndex.Entry e) {
               FastArray methods;
               String mopName;
               int index;
               int from;
               int to;
               int matchingMethod;
               MetaMethod method;
               if (this.useThis) {
                  if (e.methods == null) {
                     return;
                  }

                  if (e.methods instanceof FastArray) {
                     methods = (FastArray)e.methods;
                     this.processFastArray(methods);
                  } else {
                     method = (MetaMethod)e.methods;
                     if (method instanceof NewMetaMethod) {
                        return;
                     }

                     if (this.useThis ^ (method.getModifiers() & 5) == 0) {
                        return;
                     }

                     mopName = method.getMopName();
                     index = Arrays.binarySearch(mopMethods, mopName, CachedClass.CachedMethodComparatorWithString.INSTANCE);
                     if (index >= 0) {
                        for(from = index; from > 0 && mopMethods[from - 1].getName().equals(mopName); --from) {
                        }

                        for(to = index; to < mopMethods.length - 1 && mopMethods[to + 1].getName().equals(mopName); ++to) {
                        }

                        matchingMethod = MetaClassImpl.this.findMatchingMethod(mopMethods, from, to, method);
                        if (matchingMethod != -1) {
                           e.methods = mopMethods[matchingMethod];
                        }
                     }
                  }
               } else {
                  if (e.methodsForSuper == null) {
                     return;
                  }

                  if (e.methodsForSuper instanceof FastArray) {
                     methods = (FastArray)e.methodsForSuper;
                     this.processFastArray(methods);
                  } else {
                     method = (MetaMethod)e.methodsForSuper;
                     if (method instanceof NewMetaMethod) {
                        return;
                     }

                     if (this.useThis ^ (method.getModifiers() & 5) == 0) {
                        return;
                     }

                     mopName = method.getMopName();
                     index = Arrays.binarySearch(mopMethods, mopName, CachedClass.CachedMethodComparatorWithString.INSTANCE);
                     if (index >= 0) {
                        for(from = index; from > 0 && mopMethods[from - 1].getName().equals(mopName); --from) {
                        }

                        for(to = index; to < mopMethods.length - 1 && mopMethods[to + 1].getName().equals(mopName); ++to) {
                        }

                        matchingMethod = MetaClassImpl.this.findMatchingMethod(mopMethods, from, to, method);
                        if (matchingMethod != -1) {
                           e.methodsForSuper = mopMethods[matchingMethod];
                        }
                     }
                  }
               }

            }

            private void processFastArray(FastArray methods) {
               int len = methods.size();
               Object[] data = methods.getArray();

               for(int i = 0; i != len; ++i) {
                  MetaMethod method = (MetaMethod)data[i];
                  if (!(method instanceof NewMetaMethod) && !(this.useThis ^ (method.getModifiers() & 5) == 0)) {
                     String mopName = method.getMopName();
                     int index = Arrays.binarySearch(mopMethods, mopName, CachedClass.CachedMethodComparatorWithString.INSTANCE);
                     if (index >= 0) {
                        int from;
                        for(from = index; from > 0 && mopMethods[from - 1].getName().equals(mopName); --from) {
                        }

                        int to;
                        for(to = index; to < mopMethods.length - 1 && mopMethods[to + 1].getName().equals(mopName); ++to) {
                        }

                        int matchingMethod = MetaClassImpl.this.findMatchingMethod(mopMethods, from, to, method);
                        if (matchingMethod != -1) {
                           methods.set(i, mopMethods[matchingMethod]);
                        }
                     }
                  }
               }

            }
         }

         MOPIter iter = new MOPIter();
         iter.useThis = false;
         iter.iterate();
         iter.useThis = true;
         iter.iterate();
      }
   }

   private void inheritInterfaceNewMetaMethods(Set<CachedClass> interfaces) {
      Iterator i$ = interfaces.iterator();

      while(i$.hasNext()) {
         CachedClass cls = (CachedClass)i$.next();
         MetaMethod[] methods = this.getNewMetaMethods(cls);
         MetaMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            MetaMethod method = arr$[i$];
            if (!this.newGroovyMethodsSet.contains(method)) {
               this.newGroovyMethodsSet.add(method);
            }

            this.addMetaMethodToIndex(method, this.mainClassMethodHeader);
         }
      }

   }

   private void connectMultimethods(List superClasses, CachedClass firstGroovyClass) {
      superClasses = DefaultGroovyMethods.reverse(superClasses);
      MetaMethodIndex.Header last = null;
      Iterator iter = superClasses.iterator();

      while(iter.hasNext()) {
         CachedClass c = (CachedClass)iter.next();
         MetaMethodIndex.Header methodIndex = this.metaMethodIndex.getHeader(c.getTheClass());
         if (last != null) {
            this.metaMethodIndex.copyNonPrivateNonNewMetaMethods(last, methodIndex);
         }

         last = methodIndex;
         if (c == firstGroovyClass) {
            break;
         }
      }

   }

   private CachedClass calcFirstGroovySuperClass(Collection superClasses) {
      if (this.theCachedClass.isInterface) {
         return ReflectionCache.OBJECT_CLASS;
      } else {
         CachedClass firstGroovy = null;
         Iterator iter = superClasses.iterator();

         while(iter.hasNext()) {
            CachedClass c = (CachedClass)iter.next();
            if (GroovyObject.class.isAssignableFrom(c.getTheClass())) {
               firstGroovy = c;
               break;
            }
         }

         if (firstGroovy == null) {
            firstGroovy = this.theCachedClass;
         } else if (firstGroovy.getTheClass() == GroovyObjectSupport.class && iter.hasNext()) {
            firstGroovy = (CachedClass)iter.next();
            if (firstGroovy.getTheClass() == Closure.class && iter.hasNext()) {
               firstGroovy = (CachedClass)iter.next();
            }
         }

         return GroovyObject.class.isAssignableFrom(firstGroovy.getTheClass()) ? firstGroovy.getCachedSuperClass() : firstGroovy;
      }
   }

   private Object getMethods(Class sender, String name, boolean isCallToSuper) {
      MetaMethodIndex.Entry entry = this.metaMethodIndex.getMethods(sender, name);
      Object answer;
      if (entry == null) {
         answer = FastArray.EMPTY_LIST;
      } else if (isCallToSuper) {
         answer = entry.methodsForSuper;
      } else {
         answer = entry.methods;
      }

      if (answer == null) {
         answer = FastArray.EMPTY_LIST;
      }

      if (!isCallToSuper) {
         List used = GroovyCategorySupport.getCategoryMethods(name);
         if (used != null) {
            FastArray arr;
            if (answer instanceof MetaMethod) {
               arr = new FastArray();
               arr.add(answer);
            } else {
               arr = ((FastArray)answer).copy();
            }

            Iterator iter = used.iterator();

            while(iter.hasNext()) {
               MetaMethod element = (MetaMethod)iter.next();
               if (element.getDeclaringClass().getTheClass().isAssignableFrom(sender)) {
                  this.filterMatchingMethodForCategory(arr, element);
               }
            }

            answer = arr;
         }
      }

      return answer;
   }

   private Object getStaticMethods(Class sender, String name) {
      MetaMethodIndex.Entry entry = this.metaMethodIndex.getMethods(sender, name);
      if (entry == null) {
         return FastArray.EMPTY_LIST;
      } else {
         Object answer = entry.staticMethods;
         return answer == null ? FastArray.EMPTY_LIST : answer;
      }
   }

   public boolean isModified() {
      return false;
   }

   public void addNewInstanceMethod(Method method) {
      CachedMethod cachedMethod = CachedMethod.find(method);
      NewInstanceMetaMethod newMethod = new NewInstanceMetaMethod(cachedMethod);
      CachedClass declaringClass = newMethod.getDeclaringClass();
      this.addNewInstanceMethodToIndex(newMethod, this.metaMethodIndex.getHeader(declaringClass.getTheClass()));
   }

   private void addNewInstanceMethodToIndex(MetaMethod newMethod, MetaMethodIndex.Header header) {
      if (!this.newGroovyMethodsSet.contains(newMethod)) {
         this.newGroovyMethodsSet.add(newMethod);
         this.addMetaMethodToIndex(newMethod, header);
      }

   }

   public void addNewStaticMethod(Method method) {
      CachedMethod cachedMethod = CachedMethod.find(method);
      NewStaticMetaMethod newMethod = new NewStaticMetaMethod(cachedMethod);
      CachedClass declaringClass = newMethod.getDeclaringClass();
      this.addNewStaticMethodToIndex(newMethod, this.metaMethodIndex.getHeader(declaringClass.getTheClass()));
   }

   private void addNewStaticMethodToIndex(MetaMethod newMethod, MetaMethodIndex.Header header) {
      if (!this.newGroovyMethodsSet.contains(newMethod)) {
         this.newGroovyMethodsSet.add(newMethod);
         this.addMetaMethodToIndex(newMethod, header);
      }

   }

   public Object invokeMethod(Object object, String methodName, Object arguments) {
      if (arguments == null) {
         return this.invokeMethod(object, methodName, MetaClassHelper.EMPTY_ARRAY);
      } else if (arguments instanceof Tuple) {
         Tuple tuple = (Tuple)arguments;
         return this.invokeMethod(object, methodName, tuple.toArray());
      } else {
         return arguments instanceof Object[] ? this.invokeMethod(object, methodName, (Object[])((Object[])arguments)) : this.invokeMethod(object, methodName, new Object[]{arguments});
      }
   }

   public Object invokeMissingMethod(Object instance, String methodName, Object[] arguments) {
      return this.invokeMissingMethod(instance, methodName, arguments, (RuntimeException)null, false);
   }

   public Object invokeMissingProperty(Object instance, String propertyName, Object optionalValue, boolean isGetter) {
      Class theClass = instance instanceof Class ? (Class)instance : instance.getClass();

      for(CachedClass superClass = this.theCachedClass; superClass != null && superClass != ReflectionCache.OBJECT_CLASS; superClass = superClass.getCachedSuperClass()) {
         MetaBeanProperty property = this.findPropertyInClassHierarchy(propertyName, superClass);
         if (property != null) {
            this.onSuperPropertyFoundInHierarchy(property);
            if (!isGetter) {
               property.setProperty(instance, optionalValue);
               return null;
            }

            return property.getProperty(instance);
         }
      }

      MetaMethod method;
      Class[] getPropertyArgs;
      if (isGetter) {
         getPropertyArgs = new Class[]{String.class};
         method = findMethodInClassHierarchy(instance.getClass(), "getProperty", getPropertyArgs, this);
         if (method != null && method instanceof ClosureMetaMethod) {
            this.onGetPropertyFoundInHierarchy(method);
            return method.invoke(instance, new Object[]{propertyName});
         }
      } else {
         getPropertyArgs = new Class[]{String.class, Object.class};
         method = findMethodInClassHierarchy(instance.getClass(), "setProperty", getPropertyArgs, this);
         if (method != null && method instanceof ClosureMetaMethod) {
            this.onSetPropertyFoundInHierarchy(method);
            return method.invoke(instance, new Object[]{propertyName, optionalValue});
         }
      }

      try {
         if (!(instance instanceof Class)) {
            if (isGetter && this.propertyMissingGet != null) {
               return this.propertyMissingGet.invoke(instance, new Object[]{propertyName});
            }

            if (this.propertyMissingSet != null) {
               return this.propertyMissingSet.invoke(instance, new Object[]{propertyName, optionalValue});
            }
         }
      } catch (InvokerInvocationException var9) {
         boolean shouldHandle = isGetter && this.propertyMissingGet != null;
         if (!shouldHandle) {
            shouldHandle = !isGetter && this.propertyMissingSet != null;
         }

         if (shouldHandle && var9.getCause() instanceof MissingPropertyException) {
            throw (MissingPropertyException)var9.getCause();
         }

         throw var9;
      }

      if (instance instanceof Class && theClass != Class.class) {
         MetaProperty metaProperty = InvokerHelper.getMetaClass(Class.class).hasProperty(instance, propertyName);
         if (metaProperty != null) {
            if (isGetter) {
               return metaProperty.getProperty(instance);
            }

            metaProperty.setProperty(instance, optionalValue);
            return null;
         }
      }

      throw new MissingPropertyExceptionNoStack(propertyName, theClass);
   }

   private Object invokeMissingMethod(Object instance, String methodName, Object[] arguments, RuntimeException original, boolean isCallToSuper) {
      if (!isCallToSuper) {
         Class instanceKlazz = instance.getClass();
         if (this.theClass != instanceKlazz && this.theClass.isAssignableFrom(instanceKlazz)) {
            instanceKlazz = this.theClass;
         }

         Class[] argClasses = MetaClassHelper.castArgumentsToClassArray(arguments);
         MetaMethod method = this.findMixinMethod(methodName, argClasses);
         if (method != null) {
            this.onMixinMethodFound(method);
            return method.invoke(instance, arguments);
         }

         method = findMethodInClassHierarchy(instanceKlazz, methodName, argClasses, this);
         if (method != null) {
            this.onSuperMethodFoundInHierarchy(method);
            return method.invoke(instance, arguments);
         }

         Class[] invokeMethodArgs = new Class[]{String.class, Object[].class};
         method = findMethodInClassHierarchy(instanceKlazz, "invokeMethod", invokeMethodArgs, this);
         if (method != null && method instanceof ClosureMetaMethod) {
            this.onInvokeMethodFoundInHierarchy(method);
            return method.invoke(instance, invokeMethodArgs);
         }
      }

      if (this.methodMissing != null) {
         try {
            return this.methodMissing.invoke(instance, new Object[]{methodName, arguments});
         } catch (InvokerInvocationException var10) {
            if (this.methodMissing instanceof ClosureMetaMethod && var10.getCause() instanceof MissingMethodException) {
               MissingMethodException mme = (MissingMethodException)var10.getCause();
               throw new MissingMethodExecutionFailed(mme.getMethod(), mme.getClass(), mme.getArguments(), mme.isStatic(), mme);
            } else {
               throw var10;
            }
         } catch (MissingMethodException var11) {
            if (this.methodMissing instanceof ClosureMetaMethod) {
               throw new MissingMethodExecutionFailed(var11.getMethod(), var11.getClass(), var11.getArguments(), var11.isStatic(), var11);
            } else {
               throw var11;
            }
         }
      } else if (original != null) {
         throw original;
      } else {
         throw new MissingMethodExceptionNoStack(methodName, this.theClass, arguments, false);
      }
   }

   protected void onSuperPropertyFoundInHierarchy(MetaBeanProperty property) {
   }

   protected void onMixinMethodFound(MetaMethod method) {
   }

   protected void onSuperMethodFoundInHierarchy(MetaMethod method) {
   }

   protected void onInvokeMethodFoundInHierarchy(MetaMethod method) {
   }

   protected void onSetPropertyFoundInHierarchy(MetaMethod method) {
   }

   protected void onGetPropertyFoundInHierarchy(MetaMethod method) {
   }

   protected Object invokeStaticMissingProperty(Object instance, String propertyName, Object optionalValue, boolean isGetter) {
      MetaClass mc = instance instanceof Class ? this.registry.getMetaClass((Class)instance) : this;
      MetaMethod propertyMissing;
      if (isGetter) {
         propertyMissing = ((MetaClass)mc).getMetaMethod("$static_propertyMissing", GETTER_MISSING_ARGS);
         if (propertyMissing != null) {
            return propertyMissing.invoke(instance, new Object[]{propertyName});
         }
      } else {
         propertyMissing = ((MetaClass)mc).getMetaMethod("$static_propertyMissing", SETTER_MISSING_ARGS);
         if (propertyMissing != null) {
            return propertyMissing.invoke(instance, new Object[]{propertyName, optionalValue});
         }
      }

      if (instance instanceof Class) {
         throw new MissingPropertyException(propertyName, (Class)instance);
      } else {
         throw new MissingPropertyException(propertyName, this.theClass);
      }
   }

   public Object invokeMethod(Object object, String methodName, Object[] originalArguments) {
      return this.invokeMethod(this.theClass, object, methodName, originalArguments, false, false);
   }

   public Object invokeMethod(Class sender, Object object, String methodName, Object[] originalArguments, boolean isCallToSuper, boolean fromInsideClass) {
      this.checkInitalised();
      if (object == null) {
         throw new NullPointerException("Cannot invoke method: " + methodName + " on null object");
      } else {
         Object[] arguments = originalArguments == null ? EMPTY_ARGUMENTS : originalArguments;
         MetaMethod method = this.getMethodWithCaching(sender, methodName, arguments, isCallToSuper);
         MetaClassHelper.unwrap(arguments);
         if (method == null) {
            method = this.tryListParamMetaMethod(sender, methodName, isCallToSuper, arguments);
         }

         boolean isClosure = object instanceof Closure;
         if (isClosure) {
            Closure closure = (Closure)object;
            Object owner = closure.getOwner();
            MetaClass ownerMetaClass;
            if (!"call".equals(methodName) && !"doCall".equals(methodName)) {
               if ("curry".equals(methodName)) {
                  return closure.curry(arguments);
               }
            } else {
               Class objectClass = object.getClass();
               if (objectClass == MethodClosure.class) {
                  MethodClosure mc = (MethodClosure)object;
                  methodName = mc.getMethod();
                  Class ownerClass = owner instanceof Class ? (Class)owner : owner.getClass();
                  MetaClass ownerMetaClass = this.registry.getMetaClass(ownerClass);
                  return ownerMetaClass.invokeMethod(ownerClass, owner, methodName, arguments, false, false);
               }

               if (objectClass == CurriedClosure.class) {
                  CurriedClosure cc = (CurriedClosure)object;
                  Object[] curriedArguments = cc.getUncurriedArguments(arguments);
                  Class ownerClass = owner instanceof Class ? (Class)owner : owner.getClass();
                  ownerMetaClass = this.registry.getMetaClass(ownerClass);
                  return ownerMetaClass.invokeMethod(owner, methodName, curriedArguments);
               }

               if (method == null) {
                  this.invokeMissingMethod(object, methodName, arguments);
               }
            }

            Object delegate = closure.getDelegate();
            boolean isClosureNotOwner = owner != closure;
            int resolveStrategy = closure.getResolveStrategy();
            Class[] argClasses = MetaClassHelper.convertToTypeArray(arguments);
            MissingMethodException last;
            switch(resolveStrategy) {
            case 1:
               if (method == null && delegate != closure && delegate != null) {
                  ownerMetaClass = this.lookupObjectMetaClass(delegate);
                  method = ownerMetaClass.pickMethod(methodName, argClasses);
                  if (method != null) {
                     return ownerMetaClass.invokeMethod(delegate, methodName, originalArguments);
                  }
               }

               if (method == null && owner != closure) {
                  ownerMetaClass = this.lookupObjectMetaClass(owner);
                  method = ownerMetaClass.pickMethod(methodName, argClasses);
                  if (method != null) {
                     return ownerMetaClass.invokeMethod(owner, methodName, originalArguments);
                  }
               }

               if (method == null && resolveStrategy != 4) {
                  last = null;
                  if (delegate != closure && delegate instanceof GroovyObject) {
                     try {
                        return this.invokeMethodOnGroovyObject(methodName, originalArguments, delegate);
                     } catch (MissingMethodException var20) {
                        if (last == null) {
                           last = var20;
                        }
                     }
                  }

                  if (isClosureNotOwner && owner instanceof GroovyObject) {
                     try {
                        return this.invokeMethodOnGroovyObject(methodName, originalArguments, owner);
                     } catch (MissingMethodException var19) {
                        last = var19;
                     }
                  }

                  if (last != null) {
                     return this.invokeMissingMethod(object, methodName, originalArguments, last, isCallToSuper);
                  }
               }
               break;
            case 2:
               if (method == null && owner != closure) {
                  ownerMetaClass = this.lookupObjectMetaClass(owner);
                  return ownerMetaClass.invokeMethod(owner, methodName, originalArguments);
               }
               break;
            case 3:
               if (method == null && delegate != closure && delegate != null) {
                  ownerMetaClass = this.lookupObjectMetaClass(delegate);
                  method = ownerMetaClass.pickMethod(methodName, argClasses);
                  if (method != null) {
                     return ownerMetaClass.invokeMethod(delegate, methodName, originalArguments);
                  }

                  if (delegate != closure && delegate instanceof GroovyObject) {
                     return this.invokeMethodOnGroovyObject(methodName, originalArguments, delegate);
                  }
               }
               break;
            case 4:
               method = closure.getMetaClass().pickMethod(methodName, argClasses);
               if (method != null) {
                  return method.invoke(closure, arguments);
               }
               break;
            default:
               if (method == null && owner != closure) {
                  ownerMetaClass = this.lookupObjectMetaClass(owner);
                  method = ownerMetaClass.pickMethod(methodName, argClasses);
                  if (method != null) {
                     return ownerMetaClass.invokeMethod(owner, methodName, originalArguments);
                  }
               }

               if (method == null && delegate != closure && delegate != null) {
                  ownerMetaClass = this.lookupObjectMetaClass(delegate);
                  method = ownerMetaClass.pickMethod(methodName, argClasses);
                  if (method != null) {
                     return ownerMetaClass.invokeMethod(delegate, methodName, originalArguments);
                  }
               }

               if (method == null && resolveStrategy != 4) {
                  last = null;
                  if (isClosureNotOwner && owner instanceof GroovyObject) {
                     try {
                        return this.invokeMethodOnGroovyObject(methodName, originalArguments, owner);
                     } catch (MissingMethodException var23) {
                        if (!methodName.equals(var23.getMethod())) {
                           throw var23;
                        }

                        if (last == null) {
                           last = var23;
                        }
                     } catch (InvokerInvocationException var24) {
                        if (!(var24.getCause() instanceof MissingMethodException)) {
                           throw var24;
                        }

                        MissingMethodException mme = (MissingMethodException)var24.getCause();
                        if (!methodName.equals(mme.getMethod())) {
                           throw var24;
                        }

                        if (last == null) {
                           last = mme;
                        }
                     }
                  }

                  if (delegate != closure && delegate instanceof GroovyObject) {
                     try {
                        return this.invokeMethodOnGroovyObject(methodName, originalArguments, delegate);
                     } catch (MissingMethodException var21) {
                        last = var21;
                     } catch (InvokerInvocationException var22) {
                        if (!(var22.getCause() instanceof MissingMethodException)) {
                           throw var22;
                        }

                        last = (MissingMethodException)var22.getCause();
                     }
                  }

                  if (last != null) {
                     return this.invokeMissingMethod(object, methodName, originalArguments, last, isCallToSuper);
                  }
               }
            }
         }

         return method != null ? method.doMethodInvoke(object, arguments) : this.invokePropertyOrMissing(object, methodName, originalArguments, fromInsideClass, isCallToSuper);
      }
   }

   private MetaMethod tryListParamMetaMethod(Class sender, String methodName, boolean isCallToSuper, Object[] arguments) {
      MetaMethod method = null;
      if (arguments.length == 1 && arguments[0] instanceof List) {
         Object[] newArguments = ((List)arguments[0]).toArray();
         method = this.getMethodWithCaching(sender, methodName, newArguments, isCallToSuper);
         if (method != null) {
            method = new TransformMetaMethod((MetaMethod)method) {
               public Object invoke(Object object, Object[] arguments) {
                  Object firstArgument = arguments[0];
                  List list = (List)firstArgument;
                  arguments = list.toArray();
                  return super.invoke(object, arguments);
               }
            };
         }
      }

      return (MetaMethod)method;
   }

   private Object invokePropertyOrMissing(Object object, String methodName, Object[] originalArguments, boolean fromInsideClass, boolean isCallToSuper) {
      Object value = null;
      MetaProperty metaProperty = this.getMetaProperty(methodName, false);
      if (metaProperty != null) {
         value = metaProperty.getProperty(object);
      } else if (object instanceof Map) {
         value = ((Map)object).get(methodName);
      }

      MetaClass bindingVarMC;
      if (value instanceof Closure) {
         Closure closure = (Closure)value;
         bindingVarMC = closure.getMetaClass();
         return bindingVarMC.invokeMethod(closure.getClass(), closure, "doCall", originalArguments, false, fromInsideClass);
      } else {
         if (object instanceof Script) {
            Object bindingVar = ((Script)object).getBinding().getVariables().get(methodName);
            if (bindingVar != null) {
               bindingVarMC = ((MetaClassRegistryImpl)this.registry).getMetaClass(bindingVar);
               return bindingVarMC.invokeMethod(bindingVar, "call", originalArguments);
            }
         }

         return this.invokeMissingMethod(object, methodName, originalArguments, (RuntimeException)null, isCallToSuper);
      }
   }

   private MetaClass lookupObjectMetaClass(Object object) {
      if (object instanceof GroovyObject) {
         GroovyObject go = (GroovyObject)object;
         return go.getMetaClass();
      } else {
         Class ownerClass = object.getClass();
         if (ownerClass == Class.class) {
            ownerClass = (Class)object;
         }

         MetaClass metaClass = this.registry.getMetaClass(ownerClass);
         return metaClass;
      }
   }

   private Object invokeMethodOnGroovyObject(String methodName, Object[] originalArguments, Object owner) {
      GroovyObject go = (GroovyObject)owner;
      return go.invokeMethod(methodName, originalArguments);
   }

   public MetaMethod getMethodWithCaching(Class sender, String methodName, Object[] arguments, boolean isCallToSuper) {
      if (!isCallToSuper && GroovyCategorySupport.hasCategoryInCurrentThread()) {
         return this.getMethodWithoutCaching(sender, methodName, MetaClassHelper.convertToTypeArray(arguments), isCallToSuper);
      } else {
         MetaMethodIndex.Entry e = this.metaMethodIndex.getMethods(sender, methodName);
         if (e == null) {
            return null;
         } else {
            return isCallToSuper ? this.getSuperMethodWithCaching(arguments, e) : this.getNormalMethodWithCaching(arguments, e);
         }
      }
   }

   private static boolean sameClasses(Class[] params, Class[] arguments, boolean weakNullCheck) {
      if (params == null) {
         return false;
      } else if (params.length != arguments.length) {
         return false;
      } else {
         for(int i = params.length - 1; i >= 0; --i) {
            Object arg = arguments[i];
            if (arg != null) {
               if (params[i] != arguments[i]) {
                  return false;
               }
            } else if (!weakNullCheck) {
               return false;
            }
         }

         return true;
      }
   }

   private MetaMethod getMethodWithCachingInternal(Class sender, CallSite site, Class[] params) {
      if (site.getUsage().get() != 0 && GroovyCategorySupport.hasCategoryInCurrentThread()) {
         return this.getMethodWithoutCaching(sender, site.getName(), params, false);
      } else {
         MetaMethodIndex.Entry e = this.metaMethodIndex.getMethods(sender, site.getName());
         if (e == null) {
            return null;
         } else {
            Object methods = e.methods;
            if (methods == null) {
               return null;
            } else {
               MetaMethodIndex.CacheEntry cacheEntry = e.cachedMethod;
               if (cacheEntry != null && sameClasses(cacheEntry.params, params, methods instanceof MetaMethod)) {
                  return cacheEntry.method;
               } else {
                  cacheEntry = new MetaMethodIndex.CacheEntry();
                  cacheEntry.params = params;
                  cacheEntry.method = (MetaMethod)this.chooseMethod(e.name, methods, params);
                  e.cachedMethod = cacheEntry;
                  return cacheEntry.method;
               }
            }
         }
      }
   }

   private MetaMethod getSuperMethodWithCaching(Object[] arguments, MetaMethodIndex.Entry e) {
      if (e.methodsForSuper == null) {
         return null;
      } else {
         MetaMethodIndex.CacheEntry cacheEntry = e.cachedMethodForSuper;
         if (cacheEntry != null && MetaClassHelper.sameClasses(cacheEntry.params, arguments, e.methodsForSuper instanceof MetaMethod)) {
            MetaMethod method = cacheEntry.method;
            if (method != null) {
               return method;
            }
         }

         cacheEntry = new MetaMethodIndex.CacheEntry();
         Class[] classes = MetaClassHelper.convertToTypeArray(arguments);
         cacheEntry.params = classes;
         cacheEntry.method = (MetaMethod)this.chooseMethod(e.name, e.methodsForSuper, classes);
         if (cacheEntry.method.isAbstract()) {
            cacheEntry.method = null;
         }

         e.cachedMethodForSuper = cacheEntry;
         return cacheEntry.method;
      }
   }

   private MetaMethod getNormalMethodWithCaching(Object[] arguments, MetaMethodIndex.Entry e) {
      Object methods = e.methods;
      if (methods == null) {
         return null;
      } else {
         MetaMethodIndex.CacheEntry cacheEntry = e.cachedMethod;
         if (cacheEntry != null && MetaClassHelper.sameClasses(cacheEntry.params, arguments, methods instanceof MetaMethod)) {
            MetaMethod method = cacheEntry.method;
            if (method != null) {
               return method;
            }
         }

         cacheEntry = new MetaMethodIndex.CacheEntry();
         Class[] classes = MetaClassHelper.convertToTypeArray(arguments);
         cacheEntry.params = classes;
         cacheEntry.method = (MetaMethod)this.chooseMethod(e.name, methods, classes);
         e.cachedMethod = cacheEntry;
         return cacheEntry.method;
      }
   }

   public Constructor retrieveConstructor(Class[] arguments) {
      CachedConstructor constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, arguments);
      if (constructor != null) {
         return constructor.cachedConstructor;
      } else {
         constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, arguments);
         return constructor != null ? constructor.cachedConstructor : null;
      }
   }

   public MetaMethod retrieveStaticMethod(String methodName, Object[] arguments) {
      MetaMethodIndex.Entry e = this.metaMethodIndex.getMethods(this.theClass, methodName);
      if (e != null) {
         MetaMethodIndex.CacheEntry cacheEntry = e.cachedStaticMethod;
         if (cacheEntry != null && MetaClassHelper.sameClasses(cacheEntry.params, arguments, e.staticMethods instanceof MetaMethod)) {
            return cacheEntry.method;
         } else {
            cacheEntry = new MetaMethodIndex.CacheEntry();
            Class[] classes = MetaClassHelper.convertToTypeArray(arguments);
            cacheEntry.params = classes;
            cacheEntry.method = this.pickStaticMethod(methodName, classes);
            e.cachedStaticMethod = cacheEntry;
            return cacheEntry.method;
         }
      } else {
         return this.pickStaticMethod(methodName, MetaClassHelper.convertToTypeArray(arguments));
      }
   }

   public MetaMethod getMethodWithoutCaching(Class sender, String methodName, Class[] arguments, boolean isCallToSuper) {
      MetaMethod method = null;
      Object methods = this.getMethods(sender, methodName, isCallToSuper);
      if (methods != null) {
         method = (MetaMethod)this.chooseMethod(methodName, methods, arguments);
      }

      return method;
   }

   public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
      this.checkInitalised();
      Class sender = object instanceof Class ? (Class)object : object.getClass();
      if (sender != this.theClass) {
         MetaClass mc = this.registry.getMetaClass(sender);
         return mc.invokeStaticMethod(sender, methodName, arguments);
      } else if (sender == Class.class) {
         return this.invokeMethod(object, methodName, arguments);
      } else {
         if (arguments == null) {
            arguments = EMPTY_ARGUMENTS;
         }

         MetaMethod method = this.retrieveStaticMethod(methodName, arguments);
         if (method != null) {
            MetaClassHelper.unwrap(arguments);
            return method.doMethodInvoke(object, arguments);
         } else {
            Object prop = null;

            try {
               prop = this.getProperty(this.theClass, this.theClass, methodName, false, false);
            } catch (MissingPropertyException var13) {
            }

            if (prop instanceof Closure) {
               return this.invokeStaticClosureProperty(arguments, prop);
            } else {
               Object[] originalArguments = (Object[])((Object[])arguments.clone());
               MetaClassHelper.unwrap(arguments);
               Class superClass = sender.getSuperclass();

               MetaClass mc;
               for(Class[] argClasses = MetaClassHelper.convertToTypeArray(arguments); superClass != Object.class && superClass != null; superClass = superClass.getSuperclass()) {
                  mc = this.registry.getMetaClass(superClass);
                  method = mc.getStaticMetaMethod(methodName, argClasses);
                  if (method != null) {
                     return method.doMethodInvoke(object, arguments);
                  }

                  try {
                     prop = mc.getProperty(superClass, superClass, methodName, false, false);
                  } catch (MissingPropertyException var12) {
                  }

                  if (prop instanceof Closure) {
                     return this.invokeStaticClosureProperty(originalArguments, prop);
                  }
               }

               if (prop != null) {
                  mc = this.registry.getMetaClass(prop.getClass());
                  return mc.invokeMethod(prop, "call", arguments);
               } else {
                  return this.invokeStaticMissingMethod(sender, methodName, arguments);
               }
            }
         }
      }
   }

   private Object invokeStaticClosureProperty(Object[] originalArguments, Object prop) {
      Closure closure = (Closure)prop;
      MetaClass delegateMetaClass = closure.getMetaClass();
      return delegateMetaClass.invokeMethod(closure.getClass(), closure, "doCall", originalArguments, false, false);
   }

   private Object invokeStaticMissingMethod(Class sender, String methodName, Object[] arguments) {
      MetaMethod metaMethod = this.getStaticMetaMethod("$static_methodMissing", METHOD_MISSING_ARGS);
      if (metaMethod != null) {
         return metaMethod.invoke(sender, new Object[]{methodName, arguments});
      } else {
         throw new MissingMethodException(methodName, sender, arguments, true);
      }
   }

   private MetaMethod pickStaticMethod(String methodName, Class[] arguments) {
      MetaMethod method = null;
      MethodSelectionException mse = null;
      Object methods = this.getStaticMethods(this.theClass, methodName);
      if (!(methods instanceof FastArray) || !((FastArray)methods).isEmpty()) {
         try {
            method = (MetaMethod)this.chooseMethod(methodName, methods, arguments);
         } catch (MethodSelectionException var7) {
            mse = var7;
         }
      }

      if (method == null && this.theClass != Class.class) {
         MetaClass classMetaClass = this.registry.getMetaClass(Class.class);
         method = classMetaClass.pickMethod(methodName, arguments);
      }

      if (method == null) {
         method = (MetaMethod)this.chooseMethod(methodName, methods, MetaClassHelper.convertToTypeArray(arguments));
      }

      if (method == null && mse != null) {
         throw mse;
      } else {
         return method;
      }
   }

   /** @deprecated */
   public Object invokeConstructorAt(Class at, Object[] arguments) {
      return this.invokeConstructor(arguments);
   }

   public Object invokeConstructor(Object[] arguments) {
      return this.invokeConstructor(this.theClass, arguments);
   }

   public int selectConstructorAndTransformArguments(int numberOfConstructors, Object[] arguments) {
      if (numberOfConstructors != this.constructors.size()) {
         throw new IncompatibleClassChangeError("the number of constructors during runtime and compile time for " + this.theClass.getName() + " do not match. Expected " + numberOfConstructors + " but got " + this.constructors.size());
      } else {
         if (arguments == null) {
            arguments = EMPTY_ARGUMENTS;
         }

         Class[] argClasses = MetaClassHelper.convertToTypeArray(arguments);
         MetaClassHelper.unwrap(arguments);
         CachedConstructor constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, argClasses);
         if (constructor == null) {
            constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, argClasses);
         }

         if (constructor == null) {
            throw new GroovyRuntimeException("Could not find matching constructor for: " + this.theClass.getName() + "(" + InvokerHelper.toTypeString(arguments) + ")");
         } else {
            List l = new ArrayList(this.constructors.toList());
            Comparator comp = new Comparator() {
               public int compare(Object arg0, Object arg1) {
                  CachedConstructor c0 = (CachedConstructor)arg0;
                  CachedConstructor c1 = (CachedConstructor)arg1;
                  String descriptor0 = BytecodeHelper.getMethodDescriptor(Void.TYPE, c0.getNativeParameterTypes());
                  String descriptor1 = BytecodeHelper.getMethodDescriptor(Void.TYPE, c1.getNativeParameterTypes());
                  return descriptor0.compareTo(descriptor1);
               }
            };
            Collections.sort(l, comp);
            int found = -1;

            for(int i = 0; i < l.size(); ++i) {
               if (l.get(i) == constructor) {
                  found = i;
                  break;
               }
            }

            return 0 | found << 8;
         }
      }
   }

   protected void checkInitalised() {
      if (!this.isInitialized()) {
         throw new IllegalStateException("initialize must be called for meta class of " + this.theClass + "(" + this.getClass() + ") " + "to complete initialisation process " + "before any invocation or field/property " + "access can be done");
      }
   }

   private Object invokeConstructor(Class at, Object[] arguments) {
      this.checkInitalised();
      if (arguments == null) {
         arguments = EMPTY_ARGUMENTS;
      }

      Class[] argClasses = MetaClassHelper.convertToTypeArray(arguments);
      MetaClassHelper.unwrap(arguments);
      CachedConstructor constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, argClasses);
      if (constructor != null) {
         return constructor.doConstructorInvoke(arguments);
      } else {
         if (arguments.length == 1) {
            Object firstArgument = arguments[0];
            if (firstArgument instanceof Map) {
               constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, MetaClassHelper.EMPTY_TYPE_ARRAY);
               if (constructor != null) {
                  Object bean = constructor.doConstructorInvoke(MetaClassHelper.EMPTY_ARRAY);
                  this.setProperties(bean, (Map)firstArgument);
                  return bean;
               }
            }
         }

         throw new GroovyRuntimeException("Could not find matching constructor for: " + this.theClass.getName() + "(" + InvokerHelper.toTypeString(arguments) + ")");
      }
   }

   public void setProperties(Object bean, Map map) {
      this.checkInitalised();
      Iterator iter = map.entrySet().iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         String key = entry.getKey().toString();
         Object value = entry.getValue();
         this.setProperty(bean, key, value);
      }

   }

   public Object getProperty(Class sender, Object object, String name, boolean useSuper, boolean fromInsideClass) {
      boolean isStatic = this.theClass != Class.class && object instanceof Class;
      if (isStatic && object != this.theClass) {
         MetaClass mc = this.registry.getMetaClass((Class)object);
         return mc.getProperty(sender, object, name, useSuper, false);
      } else {
         this.checkInitalised();
         if (!isStatic && this.isMap) {
            return ((Map)object).get(name);
         } else {
            MetaMethod method = null;
            Object[] arguments = EMPTY_ARGUMENTS;
            MetaProperty mp = this.getMetaProperty(sender, name, useSuper, isStatic);
            if (mp != null && mp instanceof MetaBeanProperty) {
               MetaBeanProperty mbp = (MetaBeanProperty)mp;
               method = mbp.getGetter();
               mp = mbp.getField();
            }

            if (!useSuper && !isStatic && GroovyCategorySupport.hasCategoryInCurrentThread()) {
               String getterName = GroovyCategorySupport.getPropertyCategoryGetterName(name);
               if (getterName != null) {
                  MetaMethod categoryMethod = this.getCategoryMethodGetter(sender, getterName, false);
                  if (categoryMethod != null) {
                     method = categoryMethod;
                  }
               }
            }

            if (method == null && mp != null) {
               try {
                  return ((MetaProperty)mp).getProperty(object);
               } catch (IllegalArgumentException var12) {
                  mp = null;
               }
            }

            if (method == null && !useSuper && !isStatic && GroovyCategorySupport.hasCategoryInCurrentThread()) {
               method = this.getCategoryMethodGetter(sender, "get", true);
               if (method != null) {
                  arguments = new Object[]{name};
               }
            }

            if (method == null && this.genericGetMethod != null && (this.genericGetMethod.isStatic() || !isStatic)) {
               arguments = new Object[]{name};
               method = this.genericGetMethod;
            }

            if (method == null) {
               if (this.theClass != Class.class && object instanceof Class) {
                  MetaClass mc = this.registry.getMetaClass(Class.class);
                  return mc.getProperty(Class.class, object, name, useSuper, false);
               } else if (object instanceof Collection) {
                  return DefaultGroovyMethods.getAt((Collection)object, name);
               } else if (object instanceof Object[]) {
                  return DefaultGroovyMethods.getAt((Collection)Arrays.asList((Object[])((Object[])object)), (String)name);
               } else {
                  MetaMethod addListenerMethod = (MetaMethod)this.listeners.get(name);
                  if (addListenerMethod != null) {
                     return null;
                  } else {
                     return !isStatic && !(object instanceof Class) ? this.invokeMissingProperty(object, name, (Object)null, true) : this.invokeStaticMissingProperty(object, name, (Object)null, true);
                  }
               }
            } else {
               return method.doMethodInvoke(object, arguments);
            }
         }
      }
   }

   public MetaProperty getEffectiveGetMetaProperty(final Class sender, final Object object, String name, final boolean useSuper) {
      boolean isStatic = this.theClass != Class.class && object instanceof Class;
      if (isStatic && object != this.theClass) {
         return new MetaProperty(name, Object.class) {
            final MetaClass mc;

            {
               this.mc = MetaClassImpl.this.registry.getMetaClass((Class)object);
            }

            public Object getProperty(Object objectx) {
               return this.mc.getProperty(sender, objectx, this.name, useSuper, false);
            }

            public void setProperty(Object objectx, Object newValue) {
               throw new UnsupportedOperationException();
            }
         };
      } else {
         this.checkInitalised();
         if (!isStatic && this.isMap) {
            return new MetaProperty(name, Object.class) {
               public Object getProperty(Object object) {
                  return ((Map)object).get(this.name);
               }

               public void setProperty(Object object, Object newValue) {
                  throw new UnsupportedOperationException();
               }
            };
         } else {
            MetaMethod method = null;
            MetaProperty mp = this.getMetaProperty(sender, name, useSuper, isStatic);
            if (mp != null && mp instanceof MetaBeanProperty) {
               MetaBeanProperty mbp = (MetaBeanProperty)mp;
               method = mbp.getGetter();
               mp = mbp.getField();
            }

            if (!useSuper && !isStatic && GroovyCategorySupport.hasCategoryInCurrentThread()) {
               String getterName = GroovyCategorySupport.getPropertyCategoryGetterName(name);
               if (getterName != null) {
                  MetaMethod categoryMethod = this.getCategoryMethodGetter(sender, getterName, false);
                  if (categoryMethod != null) {
                     method = categoryMethod;
                  }
               }
            }

            if (method != null) {
               return new MetaClassImpl.GetBeanMethodMetaProperty(name, method);
            } else if (mp != null) {
               return (MetaProperty)mp;
            } else {
               if (!useSuper && !isStatic && GroovyCategorySupport.hasCategoryInCurrentThread()) {
                  method = this.getCategoryMethodGetter(sender, "get", true);
                  if (method != null) {
                     return new MetaClassImpl.GetMethodMetaProperty(name, method);
                  }
               }

               if (this.genericGetMethod != null && (this.genericGetMethod.isStatic() || !isStatic)) {
                  method = this.genericGetMethod;
                  if (method != null) {
                     return new MetaClassImpl.GetMethodMetaProperty(name, method);
                  }
               }

               if (this.theClass != Class.class && object instanceof Class) {
                  return new MetaProperty(name, Object.class) {
                     public Object getProperty(Object object) {
                        MetaClass mc = MetaClassImpl.this.registry.getMetaClass(Class.class);
                        return mc.getProperty(Class.class, object, this.name, useSuper, false);
                     }

                     public void setProperty(Object object, Object newValue) {
                        throw new UnsupportedOperationException();
                     }
                  };
               } else if (object instanceof Collection) {
                  return new MetaProperty(name, Object.class) {
                     public Object getProperty(Object object) {
                        return DefaultGroovyMethods.getAt((Collection)object, this.name);
                     }

                     public void setProperty(Object object, Object newValue) {
                        throw new UnsupportedOperationException();
                     }
                  };
               } else if (object instanceof Object[]) {
                  return new MetaProperty(name, Object.class) {
                     public Object getProperty(Object object) {
                        return DefaultGroovyMethods.getAt((Collection)Arrays.asList((Object[])((Object[])object)), (String)this.name);
                     }

                     public void setProperty(Object object, Object newValue) {
                        throw new UnsupportedOperationException();
                     }
                  };
               } else {
                  MetaMethod addListenerMethod = (MetaMethod)this.listeners.get(name);
                  if (addListenerMethod != null) {
                     return new MetaProperty(name, Object.class) {
                        public Object getProperty(Object object) {
                           return null;
                        }

                        public void setProperty(Object object, Object newValue) {
                           throw new UnsupportedOperationException();
                        }
                     };
                  } else {
                     return !isStatic && !(object instanceof Class) ? new MetaProperty(name, Object.class) {
                        public Object getProperty(Object object) {
                           return MetaClassImpl.this.invokeMissingProperty(object, this.name, (Object)null, true);
                        }

                        public void setProperty(Object object, Object newValue) {
                           throw new UnsupportedOperationException();
                        }
                     } : new MetaProperty(name, Object.class) {
                        public Object getProperty(Object object) {
                           return MetaClassImpl.this.invokeStaticMissingProperty(object, this.name, (Object)null, true);
                        }

                        public void setProperty(Object object, Object newValue) {
                           throw new UnsupportedOperationException();
                        }
                     };
                  }
               }
            }
         }
      }
   }

   private MetaMethod getCategoryMethodGetter(Class sender, String name, boolean useLongVersion) {
      List possibleGenericMethods = GroovyCategorySupport.getCategoryMethods(name);
      if (possibleGenericMethods != null) {
         Iterator iter = possibleGenericMethods.iterator();

         while(iter.hasNext()) {
            MetaMethod mmethod = (MetaMethod)iter.next();
            if (mmethod.getDeclaringClass().getTheClass().isAssignableFrom(sender)) {
               CachedClass[] paramTypes = mmethod.getParameterTypes();
               if (useLongVersion) {
                  if (paramTypes.length == 1 && paramTypes[0].getTheClass() == String.class) {
                     return mmethod;
                  }
               } else if (paramTypes.length == 0) {
                  return mmethod;
               }
            }
         }
      }

      return null;
   }

   private MetaMethod getCategoryMethodSetter(Class sender, String name, boolean useLongVersion) {
      List possibleGenericMethods = GroovyCategorySupport.getCategoryMethods(name);
      if (possibleGenericMethods != null) {
         Iterator iter = possibleGenericMethods.iterator();

         while(iter.hasNext()) {
            MetaMethod mmethod = (MetaMethod)iter.next();
            if (mmethod.getDeclaringClass().getTheClass().isAssignableFrom(sender)) {
               CachedClass[] paramTypes = mmethod.getParameterTypes();
               if (useLongVersion) {
                  if (paramTypes.length == 2 && paramTypes[0].getTheClass() == String.class) {
                     return mmethod;
                  }
               } else if (paramTypes.length == 1) {
                  return mmethod;
               }
            }
         }
      }

      return null;
   }

   public List<MetaProperty> getProperties() {
      this.checkInitalised();
      SingleKeyHashMap propertyMap = this.classPropertyIndex.getNullable(this.theCachedClass);
      List ret = new ArrayList(propertyMap.size());
      ComplexKeyHashMap.EntryIterator iter = propertyMap.getEntrySetIterator();

      while(true) {
         MetaProperty element;
         boolean setter;
         boolean getter;
         do {
            do {
               if (!iter.hasNext()) {
                  return ret;
               }

               element = (MetaProperty)((SingleKeyHashMap.Entry)iter.next()).value;
            } while(element instanceof CachedField);

            if (!(element instanceof MetaBeanProperty)) {
               break;
            }

            MetaBeanProperty mp = (MetaBeanProperty)element;
            setter = true;
            getter = true;
            if (mp.getGetter() == null || mp.getGetter() instanceof GeneratedMetaMethod || mp.getGetter() instanceof NewInstanceMetaMethod) {
               getter = false;
            }

            if (mp.getSetter() == null || mp.getSetter() instanceof GeneratedMetaMethod || mp.getSetter() instanceof NewInstanceMetaMethod) {
               setter = false;
            }
         } while(!setter && !getter);

         ret.add(element);
      }
   }

   private MetaMethod findPropertyMethod(Object methodOrList, boolean isGetter, boolean booleanGetter) {
      if (methodOrList == null) {
         return null;
      } else {
         Object ret = null;
         MetaMethod method;
         int distance;
         if (methodOrList instanceof MetaMethod) {
            method = (MetaMethod)methodOrList;
            if (!isGetter && method.getParameterTypes().length == 1) {
               ret = this.addElementToList(ret, method);
            }

            if (isGetter && method.getReturnType() != Void.class && method.getReturnType() != Void.TYPE && (!booleanGetter || method.getReturnType() == Boolean.class || method.getReturnType() == Boolean.TYPE) && method.getParameterTypes().length == 0) {
               ret = this.addElementToList(ret, method);
            }
         } else {
            FastArray methods = (FastArray)methodOrList;
            distance = methods.size();
            Object[] data = methods.getArray();

            for(int i = 0; i != distance; ++i) {
               MetaMethod element = (MetaMethod)data[i];
               if (!isGetter && element.getParameterTypes().length == 1) {
                  ret = this.addElementToList(ret, element);
               }

               if (isGetter && element.getReturnType() != Void.class && element.getReturnType() != Void.TYPE && element.getParameterTypes().length == 0) {
                  ret = this.addElementToList(ret, element);
               }
            }
         }

         if (ret == null) {
            return null;
         } else if (ret instanceof MetaMethod) {
            return (MetaMethod)ret;
         } else {
            method = null;
            distance = -1;
            Iterator iter = ((List)ret).iterator();

            while(true) {
               int localDistance;
               MetaMethod element;
               do {
                  if (!iter.hasNext()) {
                     return method;
                  }

                  element = (MetaMethod)iter.next();
                  Class c;
                  if (isGetter) {
                     c = element.getReturnType();
                  } else {
                     c = element.getParameterTypes()[0].getTheClass();
                  }

                  localDistance = distanceToObject(c);
               } while(distance != -1 && distance <= localDistance);

               distance = localDistance;
               method = element;
            }
         }
      }
   }

   private Object addElementToList(Object ret, MetaMethod element) {
      if (ret == null) {
         ret = element;
      } else if (ret instanceof List) {
         ((List)ret).add(element);
      } else {
         List list = new LinkedList();
         list.add(ret);
         list.add(element);
         ret = list;
      }

      return ret;
   }

   private static int distanceToObject(Class c) {
      int count;
      for(count = 0; c != null; ++count) {
         c = c.getSuperclass();
      }

      return count;
   }

   private void setupProperties(PropertyDescriptor[] propertyDescriptors) {
      LinkedList superClasses;
      Set interfaces;
      SingleKeyHashMap cPI;
      if (this.theCachedClass.isInterface) {
         superClasses = new LinkedList();
         superClasses.add(ReflectionCache.OBJECT_CLASS);
         interfaces = this.theCachedClass.getInterfaces();
         this.classPropertyIndexForSuper = this.classPropertyIndex;
         cPI = this.classPropertyIndex.getNotNull(this.theCachedClass);
         Iterator interfaceIter = interfaces.iterator();

         while(interfaceIter.hasNext()) {
            CachedClass iclass = (CachedClass)interfaceIter.next();
            this.addFields(iclass, cPI);
            this.classPropertyIndex.put(iclass, cPI);
         }

         this.classPropertyIndex.put(ReflectionCache.OBJECT_CLASS, cPI);
         this.applyPropertyDescriptors(propertyDescriptors);
         this.applyStrayPropertyMethods(superClasses, this.classPropertyIndex, true);
         this.makeStaticPropertyIndex();
      } else {
         superClasses = this.getSuperClasses();
         interfaces = this.theCachedClass.getInterfaces();
         if (this.theCachedClass.isArray) {
            cPI = new SingleKeyHashMap();
            cPI.put("length", this.arrayLengthProperty);
            this.classPropertyIndex.put(this.theCachedClass, cPI);
         }

         this.inheritStaticInterfaceFields(superClasses, interfaces);
         this.inheritFields(superClasses);
         this.applyPropertyDescriptors(propertyDescriptors);
         this.applyStrayPropertyMethods(superClasses, this.classPropertyIndex, true);
         this.applyStrayPropertyMethods(superClasses, this.classPropertyIndexForSuper, false);
         this.copyClassPropertyIndexForSuper(this.classPropertyIndexForSuper);
         this.makeStaticPropertyIndex();
      }

   }

   private void makeStaticPropertyIndex() {
      SingleKeyHashMap propertyMap = this.classPropertyIndex.getNotNull(this.theCachedClass);
      ComplexKeyHashMap.EntryIterator iter = propertyMap.getEntrySetIterator();

      while(true) {
         SingleKeyHashMap.Entry entry;
         MetaProperty mp;
         while(true) {
            if (!iter.hasNext()) {
               return;
            }

            entry = (SingleKeyHashMap.Entry)iter.next();
            mp = (MetaProperty)entry.getValue();
            if (mp instanceof CachedField) {
               CachedField mfp = (CachedField)mp;
               if (!mfp.isStatic()) {
                  continue;
               }
               break;
            } else if (mp instanceof MetaBeanProperty) {
               MetaProperty result = this.establishStaticMetaProperty(mp);
               if (result != null) {
                  mp = result;
                  break;
               }
            }
         }

         this.staticPropertyIndex.put(entry.getKey(), mp);
      }
   }

   private MetaProperty establishStaticMetaProperty(MetaProperty mp) {
      MetaBeanProperty mbp = (MetaBeanProperty)mp;
      MetaProperty result = null;
      MetaMethod getterMethod = mbp.getGetter();
      MetaMethod setterMethod = mbp.getSetter();
      CachedField metaField = mbp.getField();
      boolean getter = getterMethod == null || getterMethod.isStatic();
      boolean setter = setterMethod == null || setterMethod.isStatic();
      boolean field = metaField == null || metaField.isStatic();
      if (!getter && !setter && !field) {
         return (MetaProperty)result;
      } else {
         String propertyName = mbp.getName();
         Class propertyType = mbp.getType();
         if (setter && getter) {
            if (field) {
               result = mbp;
            } else {
               result = new MetaBeanProperty(propertyName, propertyType, getterMethod, setterMethod);
            }
         } else {
            MetaBeanProperty newmp;
            if (getter && !setter) {
               if (getterMethod == null) {
                  result = metaField;
               } else {
                  newmp = new MetaBeanProperty(propertyName, propertyType, getterMethod, (MetaMethod)null);
                  if (field) {
                     newmp.setField(metaField);
                  }

                  result = newmp;
               }
            } else if (setter && !getter) {
               if (setterMethod == null) {
                  result = metaField;
               } else {
                  newmp = new MetaBeanProperty(propertyName, propertyType, (MetaMethod)null, setterMethod);
                  if (field) {
                     newmp.setField(metaField);
                  }

                  result = newmp;
               }
            } else {
               result = metaField;
            }
         }

         return (MetaProperty)result;
      }
   }

   private void copyClassPropertyIndexForSuper(MetaClassImpl.Index dest) {
      ComplexKeyHashMap.EntryIterator iter = this.classPropertyIndex.getEntrySetIterator();

      while(iter.hasNext()) {
         SingleKeyHashMap.Entry entry = (SingleKeyHashMap.Entry)iter.next();
         SingleKeyHashMap newVal = new SingleKeyHashMap();
         dest.put((CachedClass)entry.getKey(), newVal);
      }

   }

   private void inheritStaticInterfaceFields(LinkedList superClasses, Set interfaces) {
      Iterator interfaceIter = interfaces.iterator();

      while(interfaceIter.hasNext()) {
         CachedClass iclass = (CachedClass)interfaceIter.next();
         SingleKeyHashMap iPropertyIndex = this.classPropertyIndex.getNotNull(iclass);
         this.addFields(iclass, iPropertyIndex);
         Iterator classIter = superClasses.iterator();

         while(classIter.hasNext()) {
            CachedClass sclass = (CachedClass)classIter.next();
            if (iclass.getTheClass().isAssignableFrom(sclass.getTheClass())) {
               SingleKeyHashMap sPropertyIndex = this.classPropertyIndex.getNotNull(sclass);
               this.copyNonPrivateFields(iPropertyIndex, sPropertyIndex);
            }
         }
      }

   }

   private void inheritFields(LinkedList<CachedClass> superClasses) {
      SingleKeyHashMap last = null;
      Iterator i$ = superClasses.iterator();

      while(i$.hasNext()) {
         CachedClass klass = (CachedClass)i$.next();
         SingleKeyHashMap propertyIndex = this.classPropertyIndex.getNotNull(klass);
         if (last != null) {
            this.copyNonPrivateFields(last, propertyIndex);
         }

         last = propertyIndex;
         this.addFields(klass, propertyIndex);
      }

   }

   private void addFields(CachedClass klass, SingleKeyHashMap propertyIndex) {
      CachedField[] fields = klass.getFields();
      CachedField[] arr$ = fields;
      int len$ = fields.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         CachedField field = arr$[i$];
         propertyIndex.put(field.getName(), field);
      }

   }

   private void copyNonPrivateFields(SingleKeyHashMap from, SingleKeyHashMap to) {
      ComplexKeyHashMap.EntryIterator iter = from.getEntrySetIterator();

      while(true) {
         SingleKeyHashMap.Entry entry;
         CachedField mfp;
         do {
            if (!iter.hasNext()) {
               return;
            }

            entry = (SingleKeyHashMap.Entry)iter.next();
            mfp = (CachedField)entry.getValue();
         } while(!Modifier.isPublic(mfp.getModifiers()) && !Modifier.isProtected(mfp.getModifiers()));

         to.put(entry.getKey(), mfp);
      }
   }

   private void applyStrayPropertyMethods(LinkedList<CachedClass> superClasses, MetaClassImpl.Index classPropertyIndex, boolean isThis) {
      Iterator i$ = superClasses.iterator();

      while(i$.hasNext()) {
         CachedClass klass = (CachedClass)i$.next();
         MetaMethodIndex.Header header = this.metaMethodIndex.getHeader(klass.getTheClass());
         SingleKeyHashMap propertyIndex = classPropertyIndex.getNotNull(klass);

         for(MetaMethodIndex.Entry e = header.head; e != null; e = e.nextClassEntry) {
            String methodName = e.name;
            if (methodName.length() >= 3 && (methodName.startsWith("is") || methodName.length() >= 4)) {
               boolean isGetter = methodName.startsWith("get") || methodName.startsWith("is");
               boolean isBooleanGetter = methodName.startsWith("is");
               boolean isSetter = methodName.startsWith("set");
               if (isGetter || isSetter) {
                  MetaMethod propertyMethod = this.findPropertyMethod(isThis ? e.methods : e.methodsForSuper, isGetter, isBooleanGetter);
                  if (propertyMethod != null) {
                     String propName = this.getPropName(methodName);
                     this.createMetaBeanProperty(propertyIndex, propName, isGetter, propertyMethod);
                  }
               }
            }
         }
      }

   }

   private String getPropName(String methodName) {
      String name = (String)propNames.get(methodName);
      if (name != null) {
         return name;
      } else {
         synchronized(propNames) {
            String stripped = methodName.startsWith("is") ? methodName.substring(2) : methodName.substring(3);
            String propName = Introspector.decapitalize(stripped);
            propNames.put(methodName, propName);
            return propName;
         }
      }
   }

   private void createMetaBeanProperty(SingleKeyHashMap propertyIndex, String propName, boolean isGetter, MetaMethod propertyMethod) {
      MetaProperty mp = (MetaProperty)propertyIndex.get(propName);
      MetaBeanProperty mp;
      if (mp == null) {
         if (isGetter) {
            mp = new MetaBeanProperty(propName, propertyMethod.getReturnType(), propertyMethod, (MetaMethod)null);
         } else {
            mp = new MetaBeanProperty(propName, propertyMethod.getParameterTypes()[0].getTheClass(), (MetaMethod)null, propertyMethod);
         }
      } else {
         MetaBeanProperty mbp;
         CachedField mfp;
         if (mp instanceof MetaBeanProperty) {
            mbp = (MetaBeanProperty)mp;
            mfp = mbp.getField();
         } else {
            if (!(mp instanceof CachedField)) {
               throw new GroovyBugError("unknown MetaProperty class used. Class is " + mp.getClass());
            }

            mfp = (CachedField)mp;
            mbp = new MetaBeanProperty(propName, mfp.getType(), (MetaMethod)null, (MetaMethod)null);
         }

         if (isGetter && mbp.getGetter() == null) {
            mbp.setGetter(propertyMethod);
         } else if (!isGetter && mbp.getSetter() == null) {
            mbp.setSetter(propertyMethod);
         }

         mbp.setField(mfp);
         mp = mbp;
      }

      propertyIndex.put(propName, mp);
   }

   protected void applyPropertyDescriptors(PropertyDescriptor[] propertyDescriptors) {
      PropertyDescriptor[] arr$ = propertyDescriptors;
      int len$ = propertyDescriptors.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         PropertyDescriptor pd = arr$[i$];
         if (pd.getPropertyType() != null) {
            Method method = pd.getReadMethod();
            MetaMethod getter;
            if (method != null) {
               CachedMethod cachedGetter = CachedMethod.find(method);
               getter = cachedGetter == null ? null : this.findMethod(cachedGetter);
            } else {
               getter = null;
            }

            method = pd.getWriteMethod();
            MetaMethod setter;
            if (method != null) {
               CachedMethod cachedSetter = CachedMethod.find(method);
               setter = cachedSetter == null ? null : this.findMethod(cachedSetter);
            } else {
               setter = null;
            }

            MetaBeanProperty mp = new MetaBeanProperty(pd.getName(), pd.getPropertyType(), getter, setter);
            this.addMetaBeanProperty(mp);
         }
      }

   }

   public void addMetaBeanProperty(MetaBeanProperty mp) {
      MetaProperty staticProperty = this.establishStaticMetaProperty(mp);
      if (staticProperty != null) {
         this.staticPropertyIndex.put(mp.getName(), mp);
      } else {
         SingleKeyHashMap propertyMap = this.classPropertyIndex.getNotNull(this.theCachedClass);
         MetaProperty old = (MetaProperty)propertyMap.get(mp.getName());
         if (old != null) {
            CachedField field;
            if (old instanceof MetaBeanProperty) {
               field = ((MetaBeanProperty)old).getField();
            } else {
               field = (CachedField)old;
            }

            mp.setField(field);
         }

         propertyMap.put(mp.getName(), mp);
      }

   }

   public void setProperty(Class sender, Object object, String name, Object newValue, boolean useSuper, boolean fromInsideClass) {
      this.checkInitalised();
      boolean isStatic = this.theClass != Class.class && object instanceof Class;
      if (isStatic && object != this.theClass) {
         MetaClass mc = this.registry.getMetaClass((Class)object);
         mc.getProperty(sender, object, name, useSuper, fromInsideClass);
      } else {
         if (newValue instanceof Wrapper) {
            newValue = ((Wrapper)newValue).unwrap();
         }

         MetaMethod method = null;
         Object[] arguments = null;
         MetaProperty mp = this.getMetaProperty(sender, name, useSuper, isStatic);
         MetaProperty field = null;
         if (mp != null) {
            if (mp instanceof MetaBeanProperty) {
               MetaBeanProperty mbp = (MetaBeanProperty)mp;
               method = mbp.getSetter();
               MetaProperty f = mbp.getField();
               if (method != null || f != null && !Modifier.isFinal(f.getModifiers())) {
                  arguments = new Object[]{newValue};
                  field = f;
               }
            } else {
               field = mp;
            }
         }

         if (!useSuper && !isStatic && GroovyCategorySupport.hasCategoryInCurrentThread() && name.length() > 0) {
            String getterName = GroovyCategorySupport.getPropertyCategorySetterName(name);
            if (getterName != null) {
               MetaMethod categoryMethod = this.getCategoryMethodSetter(sender, getterName, false);
               if (categoryMethod != null) {
                  method = categoryMethod;
                  arguments = new Object[]{newValue};
               }
            }
         }

         boolean ambiguousListener = false;
         if (method == null) {
            method = (MetaMethod)this.listeners.get(name);
            ambiguousListener = method == AMBIGUOUS_LISTENER_METHOD;
            if (method != null && !ambiguousListener && newValue instanceof Closure) {
               Object proxy = Proxy.newProxyInstance(this.theClass.getClassLoader(), new Class[]{method.getParameterTypes()[0].getTheClass()}, new ConvertedClosure((Closure)newValue, name));
               arguments = new Object[]{proxy};
               newValue = proxy;
            } else {
               method = null;
            }
         }

         if (method == null && field != null) {
            if (Modifier.isFinal(((MetaProperty)field).getModifiers())) {
               throw new ReadOnlyPropertyException(name, this.theClass);
            }

            if (!this.isMap || !this.isPrivateOrPkgPrivate(((MetaProperty)field).getModifiers())) {
               ((MetaProperty)field).setProperty(object, newValue);
               return;
            }
         }

         if (method == null && !useSuper && !isStatic && GroovyCategorySupport.hasCategoryInCurrentThread()) {
            method = this.getCategoryMethodSetter(sender, "set", true);
            if (method != null) {
               arguments = new Object[]{name, newValue};
            }
         }

         if (method == null && this.genericSetMethod != null && (this.genericSetMethod.isStatic() || !isStatic)) {
            arguments = new Object[]{name, newValue};
            method = this.genericSetMethod;
         }

         if (method != null) {
            if (arguments.length == 1) {
               newValue = DefaultTypeTransformation.castToType(newValue, method.getParameterTypes()[0].getTheClass());
               arguments[0] = newValue;
            } else {
               newValue = DefaultTypeTransformation.castToType(newValue, method.getParameterTypes()[1].getTheClass());
               arguments[1] = newValue;
            }

            method.doMethodInvoke(object, arguments);
         } else if (!isStatic && this.isMap) {
            ((Map)object).put(name, newValue);
         } else if (ambiguousListener) {
            throw new GroovyRuntimeException("There are multiple listeners for the property " + name + ". Please do not use the bean short form to access this listener.");
         } else if (mp != null) {
            throw new ReadOnlyPropertyException(name, this.theClass);
         } else {
            this.invokeMissingProperty(object, name, newValue, false);
         }
      }
   }

   private boolean isPrivateOrPkgPrivate(int mod) {
      return !Modifier.isProtected(mod) && !Modifier.isPublic(mod);
   }

   private MetaProperty getMetaProperty(Class _clazz, String name, boolean useSuper, boolean useStatic) {
      if (_clazz == this.theClass) {
         return this.getMetaProperty(name, useStatic);
      } else {
         CachedClass clazz = ReflectionCache.getCachedClass(_clazz);

         while(true) {
            SingleKeyHashMap propertyMap;
            if (useStatic) {
               propertyMap = this.staticPropertyIndex;
            } else if (useSuper) {
               propertyMap = this.classPropertyIndexForSuper.getNullable(clazz);
            } else {
               propertyMap = this.classPropertyIndex.getNullable(clazz);
            }

            if (propertyMap != null) {
               return (MetaProperty)propertyMap.get(name);
            }

            if (clazz == this.theCachedClass) {
               return null;
            }

            clazz = this.theCachedClass;
         }
      }
   }

   private MetaProperty getMetaProperty(String name, boolean useStatic) {
      CachedClass clazz = this.theCachedClass;
      SingleKeyHashMap propertyMap;
      if (useStatic) {
         propertyMap = this.staticPropertyIndex;
      } else {
         propertyMap = this.classPropertyIndex.getNullable(clazz);
      }

      return propertyMap == null ? null : (MetaProperty)propertyMap.get(name);
   }

   public Object getAttribute(Class sender, Object receiver, String messageName, boolean useSuper) {
      return this.getAttribute(receiver, messageName);
   }

   public Object getAttribute(Class sender, Object object, String attribute, boolean useSuper, boolean fromInsideClass) {
      this.checkInitalised();
      boolean isStatic = this.theClass != Class.class && object instanceof Class;
      if (isStatic && object != this.theClass) {
         MetaClass mc = this.registry.getMetaClass((Class)object);
         return mc.getAttribute(sender, object, attribute, useSuper);
      } else {
         MetaProperty mp = this.getMetaProperty(sender, attribute, useSuper, isStatic);
         if (mp != null) {
            if (mp instanceof MetaBeanProperty) {
               MetaBeanProperty mbp = (MetaBeanProperty)mp;
               mp = mbp.getField();
            }

            try {
               if (mp != null) {
                  return ((MetaProperty)mp).getProperty(object);
               }
            } catch (Exception var9) {
               throw new GroovyRuntimeException("Cannot read field: " + attribute, var9);
            }
         }

         throw new MissingFieldException(attribute, this.theClass);
      }
   }

   public void setAttribute(Class sender, Object object, String attribute, Object newValue, boolean useSuper, boolean fromInsideClass) {
      this.checkInitalised();
      boolean isStatic = this.theClass != Class.class && object instanceof Class;
      if (isStatic && object != this.theClass) {
         MetaClass mc = this.registry.getMetaClass((Class)object);
         mc.setAttribute(sender, object, attribute, newValue, useSuper, fromInsideClass);
      } else {
         MetaProperty mp = this.getMetaProperty(sender, attribute, useSuper, isStatic);
         if (mp != null) {
            if (mp instanceof MetaBeanProperty) {
               MetaBeanProperty mbp = (MetaBeanProperty)mp;
               mp = mbp.getField();
            }

            if (mp != null) {
               ((MetaProperty)mp).setProperty(object, newValue);
               return;
            }
         }

         throw new MissingFieldException(attribute, this.theClass);
      }
   }

   public ClassNode getClassNode() {
      if (this.classNode == null && GroovyObject.class.isAssignableFrom(this.theClass)) {
         String groovyFile = this.theClass.getName();
         int idx = groovyFile.indexOf(36);
         if (idx > 0) {
            groovyFile = groovyFile.substring(0, idx);
         }

         groovyFile = groovyFile.replace('.', '/') + ".groovy";
         URL url = this.theClass.getClassLoader().getResource(groovyFile);
         if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(groovyFile);
         }

         if (url != null) {
            try {
               CompilationUnit.ClassgenCallback search = new CompilationUnit.ClassgenCallback() {
                  public void call(ClassVisitor writer, ClassNode node) {
                     if (node.getName().equals(MetaClassImpl.this.theClass.getName())) {
                        MetaClassImpl.this.classNode = node;
                     }

                  }
               };
               final ClassLoader parent = this.theClass.getClassLoader();
               GroovyClassLoader gcl = (GroovyClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
                  public Object run() {
                     return new GroovyClassLoader(parent);
                  }
               });
               CompilationUnit unit = new CompilationUnit();
               unit.setClassgenCallback(search);
               unit.addSource(url);
               unit.compile(7);
            } catch (Exception var8) {
               throw new GroovyRuntimeException("Exception thrown parsing: " + groovyFile + ". Reason: " + var8, var8);
            }
         }
      }

      return this.classNode;
   }

   public String toString() {
      return super.toString() + "[" + this.theClass + "]";
   }

   public void addMetaMethod(MetaMethod method) {
      if (this.isInitialized()) {
         throw new RuntimeException("Already initialized, cannot add new method: " + method);
      } else {
         CachedClass declaringClass = method.getDeclaringClass();
         this.addMetaMethodToIndex(method, this.metaMethodIndex.getHeader(declaringClass.getTheClass()));
      }
   }

   protected void addMetaMethodToIndex(MetaMethod method, MetaMethodIndex.Header header) {
      this.checkIfStdMethod(method);
      String name = method.getName();
      MetaMethodIndex.Entry e = this.metaMethodIndex.getOrPutMethods(name, header);
      if (method.isStatic()) {
         e.staticMethods = this.metaMethodIndex.addMethodToList(e.staticMethods, method);
      }

      e.methods = this.metaMethodIndex.addMethodToList(e.methods, method);
   }

   protected final void checkIfGroovyObjectMethod(MetaMethod metaMethod) {
      if (metaMethod instanceof ClosureMetaMethod || metaMethod instanceof MixinInstanceMetaMethod) {
         if (this.isGetPropertyMethod(metaMethod)) {
            this.getPropertyMethod = metaMethod;
         } else if (this.isInvokeMethod(metaMethod)) {
            this.invokeMethodMethod = metaMethod;
         } else if (this.isSetPropertyMethod(metaMethod)) {
            this.setPropertyMethod = metaMethod;
         }
      }

   }

   private boolean isSetPropertyMethod(MetaMethod metaMethod) {
      return "setProperty".equals(metaMethod.getName()) && metaMethod.getParameterTypes().length == 2;
   }

   private boolean isGetPropertyMethod(MetaMethod metaMethod) {
      return "getProperty".equals(metaMethod.getName());
   }

   private boolean isInvokeMethod(MetaMethod metaMethod) {
      return "invokeMethod".equals(metaMethod.getName()) && metaMethod.getParameterTypes().length == 2;
   }

   private void checkIfStdMethod(MetaMethod method) {
      this.checkIfGroovyObjectMethod(method);
      if (this.isGenericGetMethod(method) && this.genericGetMethod == null) {
         this.genericGetMethod = method;
      } else if (MetaClassHelper.isGenericSetMethod(method) && this.genericSetMethod == null) {
         this.genericSetMethod = method;
      }

      CachedClass[] parameterTypes;
      if (method.getName().equals("propertyMissing")) {
         parameterTypes = method.getParameterTypes();
         if (parameterTypes.length == 1) {
            this.propertyMissingGet = method;
         }
      }

      if (this.propertyMissingSet == null && method.getName().equals("propertyMissing")) {
         parameterTypes = method.getParameterTypes();
         if (parameterTypes.length == 2) {
            this.propertyMissingSet = method;
         }
      }

      if (method.getName().equals("methodMissing")) {
         parameterTypes = method.getParameterTypes();
         if (parameterTypes.length == 2 && parameterTypes[0].getTheClass() == String.class && parameterTypes[1].getTheClass() == Object.class) {
            this.methodMissing = method;
         }
      }

      if (this.theCachedClass.isNumber) {
         NumberMathModificationInfo.instance.checkIfStdMethod(method);
      }

   }

   protected boolean isInitialized() {
      return this.initialized;
   }

   private Boolean getMatchKindForCategory(MetaMethod aMethod, MetaMethod categoryMethod) {
      CachedClass[] params1 = aMethod.getParameterTypes();
      CachedClass[] params2 = categoryMethod.getParameterTypes();
      if (params1.length != params2.length) {
         return Boolean.FALSE;
      } else {
         for(int i = 0; i < params1.length; ++i) {
            if (params1[i] != params2[i]) {
               return Boolean.FALSE;
            }
         }

         Class aMethodClass = aMethod.getDeclaringClass().getTheClass();
         Class categoryMethodClass = categoryMethod.getDeclaringClass().getTheClass();
         if (aMethodClass == categoryMethodClass) {
            return Boolean.TRUE;
         } else {
            boolean match = aMethodClass.isAssignableFrom(categoryMethodClass);
            if (match) {
               return Boolean.TRUE;
            } else {
               return null;
            }
         }
      }
   }

   private void filterMatchingMethodForCategory(FastArray list, MetaMethod method) {
      int len = list.size();
      if (len == 0) {
         list.add(method);
      } else {
         Object[] data = list.getArray();

         for(int j = 0; j != len; ++j) {
            MetaMethod aMethod = (MetaMethod)data[j];
            Boolean match = this.getMatchKindForCategory(aMethod, method);
            if (match == Boolean.TRUE) {
               list.set(j, method);
               return;
            }

            if (match == null) {
               return;
            }
         }

         list.add(method);
      }
   }

   private int findMatchingMethod(CachedMethod[] data, int from, int to, MetaMethod method) {
      for(int j = from; j <= to; ++j) {
         CachedMethod aMethod = data[j];
         CachedClass[] params1 = aMethod.getParameterTypes();
         CachedClass[] params2 = method.getParameterTypes();
         if (params1.length == params2.length) {
            boolean matches = true;

            for(int i = 0; i < params1.length; ++i) {
               if (params1[i] != params2[i]) {
                  matches = false;
                  break;
               }
            }

            if (matches) {
               return j;
            }
         }
      }

      return -1;
   }

   private MetaMethod findMethod(CachedMethod aMethod) {
      Object methods = this.getMethods(this.theClass, aMethod.getName(), false);
      if (methods instanceof FastArray) {
         FastArray m = (FastArray)methods;
         int len = m.size;
         Object[] data = m.getArray();

         for(int i = 0; i != len; ++i) {
            MetaMethod method = (MetaMethod)data[i];
            if (method.isMethod(aMethod)) {
               return method;
            }
         }
      } else {
         MetaMethod method = (MetaMethod)methods;
         if (method.getName().equals(aMethod.getName()) && method.getReturnType().equals(aMethod.getReturnType()) && MetaMethod.equal(method.getParameterTypes(), aMethod.getParameterTypes())) {
            return method;
         }
      }

      synchronized(aMethod.cachedClass) {
         return aMethod;
      }
   }

   protected Object chooseMethod(String methodName, Object methodOrList, Class[] arguments) {
      Object method = this.chooseMethodInternal(methodName, methodOrList, arguments);
      return method instanceof GeneratedMetaMethod.Proxy ? ((GeneratedMetaMethod.Proxy)method).proxy() : method;
   }

   private Object chooseMethodInternal(String methodName, Object methodOrList, Class[] arguments) {
      if (methodOrList instanceof MetaMethod) {
         return ((ParameterTypes)methodOrList).isValidMethod(arguments) ? methodOrList : null;
      } else {
         FastArray methods = (FastArray)methodOrList;
         if (methods == null) {
            return null;
         } else {
            int methodCount = methods.size();
            if (methodCount <= 0) {
               return null;
            } else {
               Object answer;
               if (methodCount == 1) {
                  answer = methods.get(0);
                  return ((ParameterTypes)answer).isValidMethod(arguments) ? answer : null;
               } else {
                  if (arguments != null && arguments.length != 0) {
                     if (arguments.length != 1 || arguments[0] != null) {
                        Object matchingMethods = null;
                        int len = methods.size;
                        Object[] data = methods.getArray();

                        for(int i = 0; i != len; ++i) {
                           Object method = data[i];
                           if (((ParameterTypes)method).isValidMethod(arguments)) {
                              if (matchingMethods == null) {
                                 matchingMethods = method;
                              } else if (matchingMethods instanceof ArrayList) {
                                 ((ArrayList)matchingMethods).add(method);
                              } else {
                                 ArrayList arr = new ArrayList(4);
                                 arr.add(matchingMethods);
                                 arr.add(method);
                                 matchingMethods = arr;
                              }
                           }
                        }

                        if (matchingMethods == null) {
                           return null;
                        } else if (!(matchingMethods instanceof ArrayList)) {
                           return matchingMethods;
                        } else {
                           return this.chooseMostSpecificParams(methodName, (List)matchingMethods, arguments);
                        }
                     }

                     answer = MetaClassHelper.chooseMostGeneralMethodWith1NullParam(methods);
                  } else {
                     answer = MetaClassHelper.chooseEmptyMethodParams(methods);
                  }

                  if (answer != null) {
                     return answer;
                  } else {
                     throw new MethodSelectionException(methodName, methods, arguments);
                  }
               }
            }
         }
      }
   }

   private Object chooseMostSpecificParams(String name, List matchingMethods, Class[] arguments) {
      long matchesDistance = -1L;
      LinkedList matches = new LinkedList();
      Iterator iter = matchingMethods.iterator();

      while(iter.hasNext()) {
         Object method = iter.next();
         ParameterTypes paramTypes = (ParameterTypes)method;
         long dist = MetaClassHelper.calculateParameterDistance(arguments, paramTypes);
         if (dist == 0L) {
            return method;
         }

         if (matches.size() == 0) {
            matches.add(method);
            matchesDistance = dist;
         } else if (dist < matchesDistance) {
            matchesDistance = dist;
            matches.clear();
            matches.add(method);
         } else if (dist == matchesDistance) {
            matches.add(method);
         }
      }

      if (matches.size() == 1) {
         return matches.getFirst();
      } else if (matches.size() == 0) {
         return null;
      } else {
         String msg = "Ambiguous method overloading for method ";
         msg = msg + this.theClass.getName() + "#" + name;
         msg = msg + ".\nCannot resolve which method to invoke for ";
         msg = msg + InvokerHelper.toString(arguments);
         msg = msg + " due to overlapping prototypes between:";

         Class[] types;
         for(Iterator iter = matches.iterator(); iter.hasNext(); msg = msg + "\n\t" + InvokerHelper.toString(types)) {
            types = ((ParameterTypes)iter.next()).getNativeParameterTypes();
         }

         throw new GroovyRuntimeException(msg);
      }
   }

   private boolean isGenericGetMethod(MetaMethod method) {
      if (!method.getName().equals("get")) {
         return false;
      } else {
         CachedClass[] parameterTypes = method.getParameterTypes();
         return parameterTypes.length == 1 && parameterTypes[0].getTheClass() == String.class;
      }
   }

   public synchronized void initialize() {
      if (!this.isInitialized()) {
         this.fillMethodIndex();
         this.addProperties();
         this.initialized = true;
      }

   }

   private void addProperties() {
      BeanInfo info;
      try {
         if (this.isBeanDerivative(this.theClass)) {
            info = (BeanInfo)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws IntrospectionException {
                  return Introspector.getBeanInfo(MetaClassImpl.this.theClass, 3);
               }
            });
         } else {
            info = (BeanInfo)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws IntrospectionException {
                  return Introspector.getBeanInfo(MetaClassImpl.this.theClass);
               }
            });
         }
      } catch (PrivilegedActionException var15) {
         throw new GroovyRuntimeException("exception during bean introspection", var15.getException());
      }

      PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
      this.setupProperties(descriptors);
      EventSetDescriptor[] eventDescriptors = info.getEventSetDescriptors();
      EventSetDescriptor[] arr$ = eventDescriptors;
      int len$ = eventDescriptors.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         EventSetDescriptor descriptor = arr$[i$];
         Method[] listenerMethods = descriptor.getListenerMethods();
         Method[] arr$ = listenerMethods;
         int len$ = listenerMethods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method listenerMethod = arr$[i$];
            MetaMethod metaMethod = CachedMethod.find(descriptor.getAddListenerMethod());
            this.addToAllMethodsIfPublic(metaMethod);
            String name = listenerMethod.getName();
            if (this.listeners.containsKey(name)) {
               this.listeners.put(name, AMBIGUOUS_LISTENER_METHOD);
            } else {
               this.listeners.put(name, metaMethod);
            }
         }
      }

   }

   private boolean isBeanDerivative(Class theClass) {
      for(Class next = theClass; next != null; next = next.getSuperclass()) {
         if (Arrays.asList(next.getInterfaces()).contains(BeanInfo.class)) {
            return true;
         }
      }

      return false;
   }

   private void addToAllMethodsIfPublic(MetaMethod metaMethod) {
      if (Modifier.isPublic(metaMethod.getModifiers())) {
         this.allMethods.add(metaMethod);
      }

   }

   public List<MetaMethod> getMethods() {
      return this.allMethods;
   }

   public List<MetaMethod> getMetaMethods() {
      return new ArrayList(this.newGroovyMethodsSet);
   }

   protected void dropStaticMethodCache(String name) {
      this.metaMethodIndex.clearCaches(name);
   }

   protected void dropMethodCache(String name) {
      this.metaMethodIndex.clearCaches(name);
   }

   public CallSite createPojoCallSite(CallSite site, Object receiver, Object[] args) {
      if (!(this instanceof AdaptingMetaClass)) {
         Class[] params = MetaClassHelper.convertToTypeArray(args);
         MetaMethod metaMethod = this.getMethodWithCachingInternal(this.getTheClass(), site, params);
         if (metaMethod != null) {
            return PojoMetaMethodSite.createPojoMetaMethodSite(site, this, metaMethod, params, receiver, args);
         }
      }

      return new PojoMetaClassSite(site, this);
   }

   public CallSite createStaticSite(CallSite site, Object[] args) {
      if (!(this instanceof AdaptingMetaClass)) {
         Class[] params = MetaClassHelper.convertToTypeArray(args);
         MetaMethod metaMethod = this.retrieveStaticMethod(site.getName(), args);
         if (metaMethod != null) {
            return StaticMetaMethodSite.createStaticMetaMethodSite(site, this, metaMethod, params, args);
         }
      }

      return new StaticMetaClassSite(site, this);
   }

   public CallSite createPogoCallSite(CallSite site, Object[] args) {
      if (site.getUsage().get() == 0 && !(this instanceof AdaptingMetaClass)) {
         Class[] params = MetaClassHelper.convertToTypeArray(args);
         MetaMethod metaMethod = this.getMethodWithCachingInternal(this.theClass, site, params);
         if (metaMethod != null) {
            return PogoMetaMethodSite.createPogoMetaMethodSite(site, this, metaMethod, params, args);
         }
      }

      return new PogoMetaClassSite(site, this);
   }

   public CallSite createPogoCallCurrentSite(CallSite site, Class sender, Object[] args) {
      if (site.getUsage().get() == 0 && !(this instanceof AdaptingMetaClass)) {
         Class[] params = MetaClassHelper.convertToTypeArray(args);
         MetaMethod metaMethod = this.getMethodWithCachingInternal(sender, site, params);
         if (metaMethod != null) {
            return PogoMetaMethodSite.createPogoMetaMethodSite(site, this, metaMethod, params, args);
         }
      }

      return new PogoMetaClassSite(site, this);
   }

   public CallSite createConstructorSite(CallSite site, Object[] args) {
      if (!(this instanceof AdaptingMetaClass)) {
         Class[] params = MetaClassHelper.convertToTypeArray(args);
         CachedConstructor constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, params);
         if (constructor != null) {
            return ConstructorSite.createConstructorSite(site, this, constructor, params, args);
         }

         if (args.length == 1 && args[0] instanceof Map) {
            constructor = (CachedConstructor)this.chooseMethod("<init>", this.constructors, MetaClassHelper.EMPTY_TYPE_ARRAY);
            if (constructor != null) {
               return new ConstructorSite.NoParamSite(site, this, constructor, params);
            }
         }
      }

      return new MetaClassConstructorSite(site, this);
   }

   public ClassInfo getClassInfo() {
      return this.theCachedClass.classInfo;
   }

   public int getVersion() {
      return this.theCachedClass.classInfo.getVersion();
   }

   public void incVersion() {
      this.theCachedClass.classInfo.incVersion();
   }

   public MetaMethod[] getAdditionalMetaMethods() {
      return this.additionalMetaMethods;
   }

   protected MetaBeanProperty findPropertyInClassHierarchy(String propertyName, CachedClass theClass) {
      MetaBeanProperty property = null;
      if (theClass == null) {
         return null;
      } else {
         CachedClass superClass = theClass.getCachedSuperClass();
         if (superClass == null) {
            return null;
         } else {
            MetaClass metaClass = this.registry.getMetaClass(superClass.getTheClass());
            if (metaClass instanceof MutableMetaClass) {
               property = this.getMetaPropertyFromMutableMetaClass(propertyName, metaClass);
               if (property == null) {
                  if (superClass != ReflectionCache.OBJECT_CLASS) {
                     property = this.findPropertyInClassHierarchy(propertyName, superClass);
                  }

                  if (property == null) {
                     Class[] interfaces = theClass.getTheClass().getInterfaces();
                     property = this.searchInterfacesForMetaProperty(propertyName, interfaces);
                  }
               }
            }

            return property;
         }
      }
   }

   private MetaBeanProperty searchInterfacesForMetaProperty(String propertyName, Class[] interfaces) {
      MetaBeanProperty property = null;
      Class[] arr$ = interfaces;
      int len$ = interfaces.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class anInterface = arr$[i$];
         MetaClass metaClass = this.registry.getMetaClass(anInterface);
         if (metaClass instanceof MutableMetaClass) {
            property = this.getMetaPropertyFromMutableMetaClass(propertyName, metaClass);
            if (property != null) {
               break;
            }
         }

         Class[] superInterfaces = anInterface.getInterfaces();
         if (superInterfaces.length > 0) {
            property = this.searchInterfacesForMetaProperty(propertyName, superInterfaces);
            if (property != null) {
               break;
            }
         }
      }

      return property;
   }

   private MetaBeanProperty getMetaPropertyFromMutableMetaClass(String propertyName, MetaClass metaClass) {
      boolean isModified = ((MutableMetaClass)metaClass).isModified();
      if (isModified) {
         MetaProperty metaProperty = metaClass.getMetaProperty(propertyName);
         if (metaProperty instanceof MetaBeanProperty) {
            return (MetaBeanProperty)metaProperty;
         }
      }

      return null;
   }

   protected MetaMethod findMixinMethod(String methodName, Class[] arguments) {
      return null;
   }

   protected static MetaMethod findMethodInClassHierarchy(Class instanceKlazz, String methodName, Class[] arguments, MetaClass metaClass) {
      if (metaClass instanceof MetaClassImpl) {
         boolean check = false;
         Iterator i$ = ((MetaClassImpl)metaClass).theCachedClass.getHierarchy().iterator();

         while(i$.hasNext()) {
            ClassInfo ci = (ClassInfo)i$.next();
            MetaClass aClass = ci.getStrongMetaClass();
            if (aClass instanceof MutableMetaClass && ((MutableMetaClass)aClass).isModified()) {
               check = true;
               break;
            }
         }

         if (!check) {
            return null;
         }
      }

      MetaMethod method = null;
      Class superClass;
      if (metaClass.getTheClass().isArray() && !metaClass.getTheClass().getComponentType().isPrimitive() && metaClass.getTheClass().getComponentType() != Object.class) {
         superClass = Object[].class;
      } else {
         superClass = metaClass.getTheClass().getSuperclass();
      }

      MetaClass superMetaClass;
      if (superClass != null) {
         superMetaClass = GroovySystem.getMetaClassRegistry().getMetaClass(superClass);
         method = findMethodInClassHierarchy(instanceKlazz, methodName, arguments, superMetaClass);
      } else if (metaClass.getTheClass().isInterface()) {
         superMetaClass = GroovySystem.getMetaClassRegistry().getMetaClass(Object.class);
         method = findMethodInClassHierarchy(instanceKlazz, methodName, arguments, superMetaClass);
      }

      method = findSubClassMethod(instanceKlazz, methodName, arguments, metaClass, method);
      MetaMethod infMethod = searchInterfacesForMetaMethod(instanceKlazz, methodName, arguments, metaClass);
      if (infMethod != null) {
         if (method == null) {
            method = infMethod;
         } else {
            method = mostSpecific(method, infMethod, instanceKlazz);
         }
      }

      method = findOwnMethod(instanceKlazz, methodName, arguments, metaClass, method);
      return method;
   }

   private static MetaMethod findSubClassMethod(Class instanceKlazz, String methodName, Class[] arguments, MetaClass metaClass, MetaMethod method) {
      if (metaClass instanceof MetaClassImpl) {
         Object list = ((MetaClassImpl)metaClass).getSubclassMetaMethods(methodName);
         if (list != null) {
            if (list instanceof MetaMethod) {
               MetaMethod m = (MetaMethod)list;
               if (m.getDeclaringClass().getTheClass().isAssignableFrom(instanceKlazz) && m.isValidExactMethod(arguments)) {
                  if (method == null) {
                     method = m;
                  } else {
                     method = mostSpecific(method, m, instanceKlazz);
                  }
               }
            } else {
               FastArray arr = (FastArray)list;

               for(int i = 0; i != arr.size(); ++i) {
                  MetaMethod m = (MetaMethod)arr.get(i);
                  if (m.getDeclaringClass().getTheClass().isAssignableFrom(instanceKlazz) && m.isValidExactMethod(arguments)) {
                     if (method == null) {
                        method = m;
                     } else {
                        method = mostSpecific(method, m, instanceKlazz);
                     }
                  }
               }
            }
         }
      }

      return method;
   }

   private static MetaMethod mostSpecific(MetaMethod method, MetaMethod newMethod, Class instanceKlazz) {
      Class newMethodC = newMethod.getDeclaringClass().getTheClass();
      Class methodC = method.getDeclaringClass().getTheClass();
      if (!newMethodC.isAssignableFrom(instanceKlazz)) {
         return method;
      } else if (newMethodC == methodC) {
         return newMethod;
      } else if (newMethodC.isAssignableFrom(methodC)) {
         return method;
      } else {
         return methodC.isAssignableFrom(newMethodC) ? newMethod : newMethod;
      }
   }

   private static MetaMethod searchInterfacesForMetaMethod(Class instanceKlazz, String methodName, Class[] arguments, MetaClass metaClass) {
      Class[] interfaces = metaClass.getTheClass().getInterfaces();
      MetaMethod method = null;
      Class[] arr$ = interfaces;
      int len$ = interfaces.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class anInterface = arr$[i$];
         MetaClass infMetaClass = GroovySystem.getMetaClassRegistry().getMetaClass(anInterface);
         MetaMethod infMethod = searchInterfacesForMetaMethod(instanceKlazz, methodName, arguments, infMetaClass);
         if (infMethod != null) {
            if (method == null) {
               method = infMethod;
            } else {
               method = mostSpecific(method, infMethod, instanceKlazz);
            }
         }
      }

      method = findSubClassMethod(instanceKlazz, methodName, arguments, metaClass, method);
      method = findOwnMethod(instanceKlazz, methodName, arguments, metaClass, method);
      return method;
   }

   protected static MetaMethod findOwnMethod(Class instanceKlazz, String methodName, Class[] arguments, MetaClass metaClass, MetaMethod method) {
      if (instanceKlazz == metaClass.getTheClass()) {
         return method;
      } else {
         MetaMethod ownMethod = metaClass.pickMethod(methodName, arguments);
         if (ownMethod != null) {
            if (method == null) {
               method = ownMethod;
            } else {
               method = mostSpecific(method, ownMethod, instanceKlazz);
            }
         }

         return method;
      }
   }

   protected Object getSubclassMetaMethods(String methodName) {
      return null;
   }

   public Object getProperty(Object object, String property) {
      return this.getProperty(this.theClass, object, property, false, false);
   }

   public void setProperty(Object object, String property, Object newValue) {
      this.setProperty(this.theClass, object, property, newValue, false, false);
   }

   public Object getAttribute(Object object, String attribute) {
      return this.getAttribute(this.theClass, object, attribute, false, false);
   }

   public void setAttribute(Object object, String attribute, Object newValue) {
      this.setAttribute(this.theClass, object, attribute, newValue, false, false);
   }

   public MetaMethod pickMethod(String methodName, Class[] arguments) {
      return this.getMethodWithoutCaching(this.theClass, methodName, arguments, false);
   }

   /** @deprecated */
   protected MetaMethod retrieveMethod(String methodName, Class[] arguments) {
      return this.pickMethod(methodName, arguments);
   }

   protected void clearInvocationCaches() {
      this.metaMethodIndex.clearCaches();
   }

   static {
      SETTER_MISSING_ARGS = METHOD_MISSING_ARGS;
      EMPTY = new MetaMethod[0];
      AMBIGUOUS_LISTENER_METHOD = new MetaClassImpl.DummyMetaMethod();
      EMPTY_ARGUMENTS = new Object[0];
      propNames = new HashMap(1024);
      NAME_INDEX_COPIER = new SingleKeyHashMap.Copier() {
         public Object copy(Object value) {
            return value instanceof FastArray ? ((FastArray)value).copy() : value;
         }
      };
      METHOD_INDEX_COPIER = new SingleKeyHashMap.Copier() {
         public Object copy(Object value) {
            return SingleKeyHashMap.copy(new SingleKeyHashMap(false), (SingleKeyHashMap)value, MetaClassImpl.NAME_INDEX_COPIER);
         }
      };
   }

   private static class GetBeanMethodMetaProperty extends MetaProperty {
      private final MetaMethod theMethod;

      public GetBeanMethodMetaProperty(String name, MetaMethod theMethod) {
         super(name, Object.class);
         this.theMethod = theMethod;
      }

      public Object getProperty(Object object) {
         return this.theMethod.doMethodInvoke(object, MetaClassImpl.EMPTY_ARGUMENTS);
      }

      public void setProperty(Object object, Object newValue) {
         throw new UnsupportedOperationException();
      }
   }

   private static class GetMethodMetaProperty extends MetaProperty {
      private final MetaMethod theMethod;

      public GetMethodMetaProperty(String name, MetaMethod theMethod) {
         super(name, Object.class);
         this.theMethod = theMethod;
      }

      public Object getProperty(Object object) {
         return this.theMethod.doMethodInvoke(object, new Object[]{this.name});
      }

      public void setProperty(Object object, Object newValue) {
         throw new UnsupportedOperationException();
      }
   }

   private static class DummyMetaMethod extends MetaMethod {
      private DummyMetaMethod() {
      }

      public int getModifiers() {
         return 0;
      }

      public String getName() {
         return null;
      }

      public Class getReturnType() {
         return null;
      }

      public CachedClass getDeclaringClass() {
         return null;
      }

      public ParameterTypes getParamTypes() {
         return null;
      }

      public Object invoke(Object object, Object[] arguments) {
         return null;
      }

      // $FF: synthetic method
      DummyMetaMethod(Object x0) {
         this();
      }
   }

   public static class Index extends SingleKeyHashMap {
      public Index(int size) {
      }

      public Index() {
      }

      public Index(boolean size) {
         super(false);
      }

      public SingleKeyHashMap getNotNull(CachedClass key) {
         SingleKeyHashMap.Entry res = this.getOrPut(key);
         if (res.value == null) {
            res.value = new SingleKeyHashMap();
         }

         return (SingleKeyHashMap)res.value;
      }

      public void put(CachedClass key, SingleKeyHashMap value) {
         this.getOrPut(key).value = value;
      }

      public SingleKeyHashMap getNullable(CachedClass clazz) {
         return (SingleKeyHashMap)this.get(clazz);
      }

      public boolean checkEquals(ComplexKeyHashMap.Entry e, Object key) {
         return ((SingleKeyHashMap.Entry)e).key.equals(key);
      }
   }

   class MethodIndex extends MetaClassImpl.Index {
      public MethodIndex(boolean b) {
         super(false);
      }

      public MethodIndex(int size) {
         super(size);
      }

      public MethodIndex() {
      }

      MetaClassImpl.MethodIndex copy() {
         return (MetaClassImpl.MethodIndex)SingleKeyHashMap.copy(MetaClassImpl.this.new MethodIndex(false), this, MetaClassImpl.METHOD_INDEX_COPIER);
      }

      protected Object clone() throws CloneNotSupportedException {
         return super.clone();
      }
   }

   private abstract class MethodIndexAction {
      private MethodIndexAction() {
      }

      public void iterate() {
         ComplexKeyHashMap.Entry[] table = MetaClassImpl.this.metaMethodIndex.methodHeaders.getTable();
         int len = table.length;

         for(int i = 0; i != len; ++i) {
            for(SingleKeyHashMap.Entry classEntry = (SingleKeyHashMap.Entry)table[i]; classEntry != null; classEntry = (SingleKeyHashMap.Entry)classEntry.next) {
               Class clazz = (Class)classEntry.getKey();
               if (!this.skipClass(clazz)) {
                  MetaMethodIndex.Header header = (MetaMethodIndex.Header)classEntry.getValue();

                  for(MetaMethodIndex.Entry nameEntry = header.head; nameEntry != null; nameEntry = nameEntry.nextClassEntry) {
                     this.methodNameAction(clazz, nameEntry);
                  }
               }
            }
         }

      }

      public abstract void methodNameAction(Class var1, MetaMethodIndex.Entry var2);

      public boolean skipClass(Class clazz) {
         return false;
      }

      // $FF: synthetic method
      MethodIndexAction(Object x1) {
         this();
      }
   }
}
