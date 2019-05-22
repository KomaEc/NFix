package org.codehaus.groovy.runtime.metaclass;

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaBeanProperty;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;
import groovy.lang.ProxyMetaClass;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedField;
import org.codehaus.groovy.reflection.CachedMethod;
import org.codehaus.groovy.reflection.ParameterTypes;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.PogoMetaClassSite;
import org.codehaus.groovy.runtime.wrappers.Wrapper;
import org.codehaus.groovy.util.FastArray;

public final class ClosureMetaClass extends MetaClassImpl {
   private boolean initialized;
   private final FastArray closureMethods = new FastArray(3);
   private Map attributes = new HashMap();
   private ClosureMetaClass.MethodChooser chooser;
   private volatile boolean attributeInitDone = false;
   private static final MetaClassImpl CLOSURE_METACLASS = new MetaClassImpl(Closure.class);
   private static MetaClassImpl classMetaClass;
   private static final Object[] EMPTY_ARGUMENTS = new Object[0];
   private static final String CLOSURE_CALL_METHOD = "call";
   private static final String CLOSURE_DO_CALL_METHOD = "doCall";
   private static final String CLOSURE_CURRY_METHOD = "curry";

   private static synchronized MetaClass getStaticMetaClass() {
      if (classMetaClass == null) {
         classMetaClass = new MetaClassImpl(Class.class);
         classMetaClass.initialize();
      }

      return classMetaClass;
   }

   public ClosureMetaClass(MetaClassRegistry registry, Class theClass) {
      super(registry, theClass);
   }

   public MetaProperty getMetaProperty(String name) {
      return CLOSURE_METACLASS.getMetaProperty(name);
   }

   private void unwrap(Object[] arguments) {
      for(int i = 0; i != arguments.length; ++i) {
         if (arguments[i] instanceof Wrapper) {
            arguments[i] = ((Wrapper)arguments[i]).unwrap();
         }
      }

   }

   private MetaMethod pickClosureMethod(Class[] argClasses) {
      Object answer = this.chooser.chooseMethod(argClasses, false);
      return (MetaMethod)answer;
   }

   private MetaMethod getDelegateMethod(Closure closure, Object delegate, String methodName, Class[] argClasses) {
      if (delegate != closure && delegate != null) {
         MetaClass delegateMetaClass;
         if (delegate instanceof Class) {
            delegateMetaClass = this.registry.getMetaClass((Class)delegate);
            return delegateMetaClass.getStaticMetaMethod(methodName, argClasses);
         } else {
            delegateMetaClass = this.lookupObjectMetaClass(delegate);
            MetaMethod method = delegateMetaClass.pickMethod(methodName, argClasses);
            if (method != null) {
               return method;
            } else {
               if (delegateMetaClass instanceof ExpandoMetaClass) {
                  method = ((ExpandoMetaClass)delegateMetaClass).findMixinMethod(methodName, argClasses);
                  if (method != null) {
                     this.onMixinMethodFound(method);
                     return method;
                  }
               }

               if (delegateMetaClass instanceof MetaClassImpl) {
                  method = MetaClassImpl.findMethodInClassHierarchy(this.getTheClass(), methodName, argClasses, this);
                  if (method != null) {
                     this.onSuperMethodFoundInHierarchy(method);
                     return method;
                  }
               }

               return method;
            }
         }
      } else {
         return null;
      }
   }

   public Object invokeMethod(Class sender, Object object, String methodName, Object[] originalArguments, boolean isCallToSuper, boolean fromInsideClass) {
      this.checkInitalised();
      if (object == null) {
         throw new NullPointerException("Cannot invoke method: " + methodName + " on null object");
      } else {
         Object[] arguments = this.makeArguments(originalArguments, methodName);
         Class[] argClasses = MetaClassHelper.convertToTypeArray(arguments);
         this.unwrap(arguments);
         Closure closure = (Closure)object;
         Object method;
         if (!"doCall".equals(methodName) && !"call".equals(methodName)) {
            if ("curry".equals(methodName)) {
               return closure.curry(arguments);
            }

            method = CLOSURE_METACLASS.pickMethod(methodName, argClasses);
         } else {
            method = this.pickClosureMethod(argClasses);
            if (method == null && arguments.length == 1 && arguments[0] instanceof List) {
               Object[] newArguments = ((List)arguments[0]).toArray();
               Class[] newArgClasses = MetaClassHelper.convertToTypeArray(newArguments);
               method = this.pickClosureMethod(newArgClasses);
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

            if (method == null) {
               throw new MissingMethodException(methodName, this.theClass, arguments, false);
            }
         }

         if (method != null) {
            return ((MetaMethod)method).doMethodInvoke(object, arguments);
         } else {
            MissingMethodException last = null;
            Object callObject = object;
            Object owner;
            if (method == null) {
               owner = closure.getOwner();
               Object delegate = closure.getDelegate();
               Object thisObject = closure.getThisObject();
               int resolveStrategy = closure.getResolveStrategy();
               boolean invokeOnDelegate = false;
               boolean invokeOnOwner = false;
               boolean ownerFirst = true;
               switch(resolveStrategy) {
               case 1:
                  method = this.getDelegateMethod(closure, delegate, methodName, argClasses);
                  callObject = delegate;
                  if (method == null) {
                     method = this.getDelegateMethod(closure, owner, methodName, argClasses);
                     callObject = owner;
                  }

                  if (method == null) {
                     invokeOnDelegate = delegate != closure && delegate instanceof GroovyObject;
                     invokeOnOwner = owner != closure && owner instanceof GroovyObject;
                     ownerFirst = false;
                  }
                  break;
               case 2:
                  method = this.getDelegateMethod(closure, owner, methodName, argClasses);
                  callObject = owner;
                  if (method == null) {
                     invokeOnOwner = owner != closure && owner instanceof GroovyObject;
                  }
                  break;
               case 3:
                  method = this.getDelegateMethod(closure, delegate, methodName, argClasses);
                  callObject = delegate;
                  if (method == null) {
                     invokeOnDelegate = delegate != closure && delegate instanceof GroovyObject;
                  }
               case 4:
                  break;
               default:
                  method = this.getDelegateMethod(closure, thisObject, methodName, argClasses);
                  callObject = thisObject;
                  if (method == null) {
                     LinkedList list = new LinkedList();

                     Closure currentClosure;
                     for(Object current = closure; current != thisObject; current = currentClosure.getOwner()) {
                        currentClosure = (Closure)current;
                        if (currentClosure.getDelegate() != null) {
                           list.add(current);
                        }
                     }

                     while(!list.isEmpty() && method == null) {
                        Closure closureWithDelegate = (Closure)list.removeLast();
                        Object currentDelegate = closureWithDelegate.getDelegate();
                        method = this.getDelegateMethod(closureWithDelegate, currentDelegate, methodName, argClasses);
                        callObject = currentDelegate;
                     }
                  }

                  if (method == null) {
                     invokeOnDelegate = delegate != closure && delegate instanceof GroovyObject;
                     invokeOnOwner = owner != closure && owner instanceof GroovyObject;
                  }
               }

               if (method == null && (invokeOnOwner || invokeOnDelegate)) {
                  try {
                     if (ownerFirst) {
                        return this.invokeOnDelegationObjects(invokeOnOwner, owner, invokeOnDelegate, delegate, methodName, arguments);
                     }

                     return this.invokeOnDelegationObjects(invokeOnDelegate, delegate, invokeOnOwner, owner, methodName, arguments);
                  } catch (MissingMethodException var24) {
                     last = var24;
                  }
               }
            }

            if (method != null) {
               MetaClass metaClass = this.registry.getMetaClass(callObject.getClass());
               return metaClass instanceof ProxyMetaClass ? metaClass.invokeMethod(callObject, methodName, arguments) : ((MetaMethod)method).doMethodInvoke(callObject, arguments);
            } else {
               owner = null;

               try {
                  owner = this.getProperty(object, methodName);
               } catch (MissingPropertyException var23) {
               }

               if (owner instanceof Closure) {
                  Closure cl = (Closure)owner;
                  MetaClass delegateMetaClass = cl.getMetaClass();
                  return delegateMetaClass.invokeMethod(cl.getClass(), closure, "doCall", originalArguments, false, fromInsideClass);
               } else if (last != null) {
                  throw last;
               } else {
                  throw new MissingMethodException(methodName, this.theClass, arguments, false);
               }
            }
         }
      }
   }

   private Object[] makeArguments(Object[] arguments, String methodName) {
      return arguments == null ? EMPTY_ARGUMENTS : arguments;
   }

   private static Throwable unwrap(GroovyRuntimeException gre) {
      Throwable th = gre;
      if (gre.getCause() != null && gre.getCause() != gre) {
         th = gre.getCause();
      }

      return (Throwable)(th != gre && th instanceof GroovyRuntimeException ? unwrap((GroovyRuntimeException)th) : th);
   }

   private Object invokeOnDelegationObjects(boolean invoke1, Object o1, boolean invoke2, Object o2, String methodName, Object[] args) {
      MissingMethodException first = null;
      GroovyObject go;
      Throwable th;
      if (invoke1) {
         go = (GroovyObject)o1;

         try {
            return go.invokeMethod(methodName, args);
         } catch (MissingMethodException var13) {
            first = var13;
         } catch (GroovyRuntimeException var14) {
            th = unwrap(var14);
            if (!(th instanceof MissingMethodException) || !methodName.equals(((MissingMethodException)th).getMethod())) {
               throw var14;
            }

            first = (MissingMethodException)th;
         }
      }

      if (invoke2 && (!invoke1 || o1 != o2)) {
         go = (GroovyObject)o2;

         try {
            return go.invokeMethod(methodName, args);
         } catch (MissingMethodException var11) {
            if (first == null) {
               first = var11;
            }
         } catch (GroovyRuntimeException var12) {
            th = unwrap(var12);
            if (!(th instanceof MissingMethodException)) {
               throw var12;
            }

            first = (MissingMethodException)th;
         }
      }

      throw first;
   }

   private synchronized void initAttributes() {
      if (this.attributes.isEmpty()) {
         this.attributes.put("!", (Object)null);
         CachedField[] fieldArray = this.theCachedClass.getFields();

         for(int i = 0; i < fieldArray.length; ++i) {
            this.attributes.put(fieldArray[i].getName(), fieldArray[i]);
         }

         this.attributeInitDone = !this.attributes.isEmpty();
      }
   }

   public synchronized void initialize() {
      if (!this.isInitialized()) {
         CachedMethod[] methodArray = this.theCachedClass.getMethods();
         synchronized(this.theCachedClass) {
            int i = 0;

            while(true) {
               if (i >= methodArray.length) {
                  break;
               }

               CachedMethod cachedMethod = methodArray[i];
               if (cachedMethod.getName().equals("doCall")) {
                  this.closureMethods.add(cachedMethod);
               }

               ++i;
            }
         }

         this.assignMethodChooser();
         this.initialized = true;
      }

   }

   private void assignMethodChooser() {
      final MetaMethod doCall;
      int i;
      if (this.closureMethods.size() == 1) {
         doCall = (MetaMethod)this.closureMethods.get(0);
         final CachedClass[] c = doCall.getParameterTypes();
         i = c.length;
         if (i == 0) {
            this.chooser = new ClosureMetaClass.MethodChooser() {
               public Object chooseMethod(Class[] arguments, boolean coerce) {
                  return arguments.length == 0 ? doCall : null;
               }
            };
         } else if (i == 1 && c[0].getTheClass() == Object.class) {
            this.chooser = new ClosureMetaClass.MethodChooser() {
               public Object chooseMethod(Class[] arguments, boolean coerce) {
                  return arguments.length < 2 ? doCall : null;
               }
            };
         } else {
            boolean allObject = true;

            final int minimumLength;
            for(minimumLength = 0; minimumLength < c.length - 1; ++minimumLength) {
               if (c[minimumLength].getTheClass() != Object.class) {
                  allObject = false;
                  break;
               }
            }

            if (allObject && c[c.length - 1].getTheClass() == Object.class) {
               this.chooser = new ClosureMetaClass.MethodChooser() {
                  public Object chooseMethod(Class[] arguments, boolean coerce) {
                     return arguments.length == c.length ? doCall : null;
                  }
               };
            } else if (allObject && c[c.length - 1].getTheClass() == Object[].class) {
               minimumLength = c.length - 2;
               this.chooser = new ClosureMetaClass.MethodChooser() {
                  public Object chooseMethod(Class[] arguments, boolean coerce) {
                     return arguments.length > minimumLength ? doCall : null;
                  }
               };
            } else {
               this.chooser = new ClosureMetaClass.MethodChooser() {
                  public Object chooseMethod(Class[] arguments, boolean coerce) {
                     return doCall.isValidMethod(arguments) ? doCall : null;
                  }
               };
            }
         }
      } else if (this.closureMethods.size() == 2) {
         doCall = null;
         MetaMethod m1 = null;

         for(i = 0; i != this.closureMethods.size(); ++i) {
            MetaMethod m = (MetaMethod)this.closureMethods.get(i);
            CachedClass[] c = m.getParameterTypes();
            if (c.length == 0) {
               doCall = m;
            } else if (c.length == 1 && c[0].getTheClass() == Object.class) {
               m1 = m;
            }
         }

         if (doCall != null && m1 != null) {
            this.chooser = new ClosureMetaClass.StandardClosureChooser(doCall, m1);
         }
      }

      if (this.chooser == null) {
         this.chooser = new ClosureMetaClass.NormalMethodChooser(this.theClass, this.closureMethods);
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
            return this.registry.getMetaClass(ownerClass);
         } else {
            MetaClass metaClass = InvokerHelper.getMetaClass(object);
            return metaClass;
         }
      }
   }

   public List<MetaMethod> getMethods() {
      List<MetaMethod> answer = CLOSURE_METACLASS.getMetaMethods();
      answer.addAll(this.closureMethods.toList());
      return answer;
   }

   public List<MetaMethod> getMetaMethods() {
      return CLOSURE_METACLASS.getMetaMethods();
   }

   public List<MetaProperty> getProperties() {
      return CLOSURE_METACLASS.getProperties();
   }

   public MetaMethod pickMethod(String name, Class[] argTypes) {
      if (argTypes == null) {
         argTypes = MetaClassHelper.EMPTY_CLASS_ARRAY;
      }

      return !name.equals("call") && !name.equals("doCall") ? CLOSURE_METACLASS.getMetaMethod(name, argTypes) : this.pickClosureMethod(argTypes);
   }

   public MetaMethod retrieveStaticMethod(String methodName, Class[] arguments) {
      return null;
   }

   protected boolean isInitialized() {
      return this.initialized;
   }

   public MetaMethod getStaticMetaMethod(String name, Object[] args) {
      return CLOSURE_METACLASS.getStaticMetaMethod(name, args);
   }

   public MetaMethod getStaticMetaMethod(String name, Class[] argTypes) {
      return CLOSURE_METACLASS.getStaticMetaMethod(name, argTypes);
   }

   public Object getProperty(Class sender, Object object, String name, boolean useSuper, boolean fromInsideClass) {
      return object instanceof Class ? getStaticMetaClass().getProperty(sender, object, name, useSuper, fromInsideClass) : CLOSURE_METACLASS.getProperty(sender, object, name, useSuper, fromInsideClass);
   }

   public Object getAttribute(Class sender, Object object, String attribute, boolean useSuper, boolean fromInsideClass) {
      if (object instanceof Class) {
         return getStaticMetaClass().getAttribute(sender, object, attribute, useSuper);
      } else {
         if (!this.attributeInitDone) {
            this.initAttributes();
         }

         CachedField mfp = (CachedField)this.attributes.get(attribute);
         return mfp == null ? CLOSURE_METACLASS.getAttribute(sender, object, attribute, useSuper) : mfp.getProperty(object);
      }
   }

   public void setAttribute(Class sender, Object object, String attribute, Object newValue, boolean useSuper, boolean fromInsideClass) {
      if (object instanceof Class) {
         getStaticMetaClass().setAttribute(sender, object, attribute, newValue, useSuper, fromInsideClass);
      } else {
         if (!this.attributeInitDone) {
            this.initAttributes();
         }

         CachedField mfp = (CachedField)this.attributes.get(attribute);
         if (mfp == null) {
            CLOSURE_METACLASS.setAttribute(sender, object, attribute, newValue, useSuper, fromInsideClass);
         } else {
            mfp.setProperty(object, newValue);
         }
      }

   }

   public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
      return getStaticMetaClass().invokeMethod(Class.class, object, methodName, arguments, false, false);
   }

   public void setProperty(Class sender, Object object, String name, Object newValue, boolean useSuper, boolean fromInsideClass) {
      if (object instanceof Class) {
         getStaticMetaClass().setProperty(sender, object, name, newValue, useSuper, fromInsideClass);
      } else {
         CLOSURE_METACLASS.setProperty(sender, object, name, newValue, useSuper, fromInsideClass);
      }

   }

   public MetaMethod getMethodWithoutCaching(int index, Class sender, String methodName, Class[] arguments, boolean isCallToSuper) {
      throw new UnsupportedOperationException();
   }

   public void setProperties(Object bean, Map map) {
      throw new UnsupportedOperationException();
   }

   public void addMetaBeanProperty(MetaBeanProperty mp) {
      throw new UnsupportedOperationException();
   }

   public void addMetaMethod(MetaMethod method) {
      throw new UnsupportedOperationException();
   }

   public void addNewInstanceMethod(Method method) {
      throw new UnsupportedOperationException();
   }

   public void addNewStaticMethod(Method method) {
      throw new UnsupportedOperationException();
   }

   public Constructor retrieveConstructor(Class[] arguments) {
      throw new UnsupportedOperationException();
   }

   public CallSite createPojoCallSite(CallSite site, Object receiver, Object[] args) {
      throw new UnsupportedOperationException();
   }

   public CallSite createPogoCallSite(CallSite site, Object[] args) {
      return new PogoMetaClassSite(site, this);
   }

   public CallSite createPogoCallCurrentSite(CallSite site, Class sender, Object[] args) {
      return new PogoMetaClassSite(site, this);
   }

   public List respondsTo(Object obj, String name, Object[] argTypes) {
      this.loadMetaInfo();
      return super.respondsTo(obj, name, argTypes);
   }

   public List respondsTo(Object obj, String name) {
      this.loadMetaInfo();
      return super.respondsTo(obj, name);
   }

   private synchronized void loadMetaInfo() {
      if (this.metaMethodIndex.isEmpty()) {
         this.initialized = false;
         super.initialize();
         this.initialized = true;
      }

   }

   protected void applyPropertyDescriptors(PropertyDescriptor[] propertyDescriptors) {
   }

   static {
      CLOSURE_METACLASS.initialize();
   }

   private static class NormalMethodChooser implements ClosureMetaClass.MethodChooser {
      private final FastArray methods;
      final Class theClass;

      NormalMethodChooser(Class theClass, FastArray methods) {
         this.theClass = theClass;
         this.methods = methods;
      }

      public Object chooseMethod(Class[] arguments, boolean coerce) {
         if (arguments.length == 0) {
            return MetaClassHelper.chooseEmptyMethodParams(this.methods);
         } else if (arguments.length == 1 && arguments[0] == null) {
            return MetaClassHelper.chooseMostGeneralMethodWith1NullParam(this.methods);
         } else {
            List matchingMethods = new ArrayList();
            int len = this.methods.size();
            Object[] data = this.methods.getArray();

            for(int i = 0; i != len; ++i) {
               Object method = data[i];
               if (((ParameterTypes)method).isValidMethod(arguments)) {
                  matchingMethods.add(method);
               }
            }

            if (matchingMethods.isEmpty()) {
               return null;
            } else if (matchingMethods.size() == 1) {
               return matchingMethods.get(0);
            } else {
               return this.chooseMostSpecificParams("doCall", matchingMethods, arguments);
            }
         }
      }

      private Object chooseMostSpecificParams(String name, List matchingMethods, Class[] arguments) {
         long matchesDistance = -1L;
         LinkedList matches = new LinkedList();
         Iterator iter = matchingMethods.iterator();

         while(iter.hasNext()) {
            Object method = iter.next();
            ParameterTypes parameterTypes = (ParameterTypes)method;
            Class[] paramTypes = parameterTypes.getNativeParameterTypes();
            if (MetaClassHelper.parametersAreCompatible(arguments, paramTypes)) {
               long dist = MetaClassHelper.calculateParameterDistance(arguments, parameterTypes);
               if (dist == 0L) {
                  return method;
               }

               if (matches.isEmpty()) {
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
         }

         if (matches.size() == 1) {
            return matches.getFirst();
         } else if (matches.isEmpty()) {
            return null;
         } else {
            String msg = "Ambiguous method overloading for method ";
            msg = msg + this.theClass.getName() + "#" + name;
            msg = msg + ".\nCannot resolve which method to invoke for ";
            msg = msg + InvokerHelper.toString(arguments);
            msg = msg + " due to overlapping prototypes between:";

            CachedClass[] types;
            for(Iterator iter = matches.iterator(); iter.hasNext(); msg = msg + "\n\t" + InvokerHelper.toString(types)) {
               types = ((ParameterTypes)iter.next()).getParameterTypes();
            }

            throw new GroovyRuntimeException(msg);
         }
      }
   }

   private static class StandardClosureChooser implements ClosureMetaClass.MethodChooser {
      private final MetaMethod doCall0;
      private final MetaMethod doCall1;

      StandardClosureChooser(MetaMethod m0, MetaMethod m1) {
         this.doCall0 = m0;
         this.doCall1 = m1;
      }

      public Object chooseMethod(Class[] arguments, boolean coerce) {
         if (arguments.length == 0) {
            return this.doCall0;
         } else {
            return arguments.length == 1 ? this.doCall1 : null;
         }
      }
   }

   private interface MethodChooser {
      Object chooseMethod(Class[] var1, boolean var2);
   }
}
