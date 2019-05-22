package org.codehaus.groovy.reflection;

import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.classgen.BytecodeHelper;
import org.codehaus.groovy.runtime.callsite.CallSiteClassLoader;
import org.codehaus.groovy.util.FastArray;
import org.codehaus.groovy.util.LazyReference;
import org.codehaus.groovy.util.ReferenceBundle;

public class CachedClass {
   private final Class cachedClass;
   public ClassInfo classInfo;
   private static ReferenceBundle softBundle = ReferenceBundle.getSoftBundle();
   private final LazyReference<CachedField[]> fields;
   private LazyReference<CachedConstructor[]> constructors;
   private LazyReference<CachedMethod[]> methods;
   private LazyReference<CachedClass> cachedSuperClass;
   private final LazyReference<CallSiteClassLoader> callSiteClassLoader;
   private final LazyReference<LinkedList<ClassInfo>> hierarchy;
   static final MetaMethod[] EMPTY = new MetaMethod[0];
   int hashCode;
   public CachedMethod[] mopMethods;
   public static final CachedClass[] EMPTY_ARRAY = new CachedClass[0];
   private final LazyReference<Set<CachedClass>> declaredInterfaces;
   private final LazyReference<Set<CachedClass>> interfaces;
   public final boolean isArray;
   public final boolean isPrimitive;
   public final int modifiers;
   int distance;
   public final boolean isInterface;
   public final boolean isNumber;

   public CachedClass(Class klazz, ClassInfo classInfo) {
      this.fields = new LazyReference<CachedField[]>(softBundle) {
         public CachedField[] initValue() {
            Field[] declaredFields = (Field[])((Field[])AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  Field[] df = CachedClass.this.getTheClass().getDeclaredFields();

                  try {
                     AccessibleObject.setAccessible(df, true);
                  } catch (SecurityException var3) {
                  }

                  return df;
               }
            }));
            CachedField[] fields = new CachedField[declaredFields.length];

            for(int i = 0; i != fields.length; ++i) {
               fields[i] = new CachedField(declaredFields[i]);
            }

            return fields;
         }
      };
      this.constructors = new LazyReference<CachedConstructor[]>(softBundle) {
         public CachedConstructor[] initValue() {
            Constructor[] declaredConstructors = (Constructor[])((Constructor[])AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  return CachedClass.this.getTheClass().getDeclaredConstructors();
               }
            }));
            CachedConstructor[] constructors = new CachedConstructor[declaredConstructors.length];

            for(int i = 0; i != constructors.length; ++i) {
               constructors[i] = new CachedConstructor(CachedClass.this, declaredConstructors[i]);
            }

            return constructors;
         }
      };
      this.methods = new LazyReference<CachedMethod[]>(softBundle) {
         public CachedMethod[] initValue() {
            Method[] declaredMethods = (Method[])((Method[])AccessController.doPrivileged(new PrivilegedAction() {
               public Object run() {
                  Method[] dm = CachedClass.this.getTheClass().getDeclaredMethods();

                  try {
                     AccessibleObject.setAccessible(dm, true);
                  } catch (SecurityException var3) {
                  }

                  return dm;
               }
            }));
            List<CachedMethod> methods = new ArrayList(declaredMethods.length);
            List<CachedMethod> mopMethods = new ArrayList(declaredMethods.length);

            for(int ix = 0; ix != declaredMethods.length; ++ix) {
               CachedMethod cachedMethod = new CachedMethod(CachedClass.this, declaredMethods[ix]);
               String name = cachedMethod.getName();
               if (name.indexOf(43) < 0) {
                  if (!name.startsWith("this$") && !name.startsWith("super$")) {
                     methods.add(cachedMethod);
                  } else {
                     mopMethods.add(cachedMethod);
                  }
               }
            }

            CachedMethod[] resMethods = (CachedMethod[])methods.toArray(new CachedMethod[methods.size()]);
            Arrays.sort(resMethods);
            CachedClass superClass = CachedClass.this.getCachedSuperClass();
            if (superClass != null) {
               superClass.getMethods();
               CachedMethod[] superMopMethods = superClass.mopMethods;

               for(int i = 0; i != superMopMethods.length; ++i) {
                  mopMethods.add(superMopMethods[i]);
               }
            }

            CachedClass.this.mopMethods = (CachedMethod[])mopMethods.toArray(new CachedMethod[mopMethods.size()]);
            Arrays.sort(CachedClass.this.mopMethods, CachedClass.CachedMethodComparatorByName.INSTANCE);
            return resMethods;
         }
      };
      this.cachedSuperClass = new LazyReference<CachedClass>(softBundle) {
         public CachedClass initValue() {
            if (!CachedClass.this.isArray) {
               return ReflectionCache.getCachedClass(CachedClass.this.getTheClass().getSuperclass());
            } else {
               return !CachedClass.this.cachedClass.getComponentType().isPrimitive() && CachedClass.this.cachedClass.getComponentType() != Object.class ? ReflectionCache.OBJECT_ARRAY_CLASS : ReflectionCache.OBJECT_CLASS;
            }
         }
      };
      this.callSiteClassLoader = new LazyReference<CallSiteClassLoader>(softBundle) {
         public CallSiteClassLoader initValue() {
            return (CallSiteClassLoader)AccessController.doPrivileged(new PrivilegedAction<CallSiteClassLoader>() {
               public CallSiteClassLoader run() {
                  return new CallSiteClassLoader(CachedClass.this.cachedClass);
               }
            });
         }
      };
      this.hierarchy = new LazyReference<LinkedList<ClassInfo>>(softBundle) {
         public LinkedList<ClassInfo> initValue() {
            Set<ClassInfo> res = new LinkedHashSet();
            res.add(CachedClass.this.classInfo);
            Iterator i$ = CachedClass.this.getDeclaredInterfaces().iterator();

            while(i$.hasNext()) {
               CachedClass iface = (CachedClass)i$.next();
               res.addAll(iface.getHierarchy());
            }

            CachedClass superClass = CachedClass.this.getCachedSuperClass();
            if (superClass != null) {
               res.addAll(superClass.getHierarchy());
            }

            if (CachedClass.this.isInterface) {
               res.add(ReflectionCache.OBJECT_CLASS.classInfo);
            }

            return new LinkedList(res);
         }
      };
      this.declaredInterfaces = new LazyReference<Set<CachedClass>>(softBundle) {
         public Set<CachedClass> initValue() {
            Set<CachedClass> res = new HashSet(0);
            Class[] classes = CachedClass.this.getTheClass().getInterfaces();
            Class[] arr$ = classes;
            int len$ = classes.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Class cls = arr$[i$];
               res.add(ReflectionCache.getCachedClass(cls));
            }

            return res;
         }
      };
      this.interfaces = new LazyReference<Set<CachedClass>>(softBundle) {
         public Set<CachedClass> initValue() {
            Set<CachedClass> res = new HashSet(0);
            if (CachedClass.this.getTheClass().isInterface()) {
               res.add(CachedClass.this);
            }

            Class[] classes = CachedClass.this.getTheClass().getInterfaces();
            Class[] arr$ = classes;
            int len$ = classes.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Class cls = arr$[i$];
               CachedClass aClass = ReflectionCache.getCachedClass(cls);
               if (!res.contains(aClass)) {
                  res.addAll(aClass.getInterfaces());
               }
            }

            CachedClass superClass = CachedClass.this.getCachedSuperClass();
            if (superClass != null) {
               res.addAll(superClass.getInterfaces());
            }

            return res;
         }
      };
      this.distance = -1;
      this.cachedClass = klazz;
      this.classInfo = classInfo;
      this.isArray = klazz.isArray();
      this.isPrimitive = klazz.isPrimitive();
      this.modifiers = klazz.getModifiers();
      this.isInterface = klazz.isInterface();
      this.isNumber = Number.class.isAssignableFrom(klazz);
      Iterator i$ = this.getInterfaces().iterator();

      while(i$.hasNext()) {
         CachedClass inf = (CachedClass)i$.next();
         ReflectionCache.isAssignableFrom(klazz, inf.cachedClass);
      }

      for(CachedClass cur = this; cur != null; cur = cur.getCachedSuperClass()) {
         ReflectionCache.setAssignableFrom(cur.cachedClass, klazz);
      }

   }

   public CachedClass getCachedSuperClass() {
      return (CachedClass)this.cachedSuperClass.get();
   }

   public Set<CachedClass> getInterfaces() {
      return (Set)this.interfaces.get();
   }

   public Set<CachedClass> getDeclaredInterfaces() {
      return (Set)this.declaredInterfaces.get();
   }

   public CachedMethod[] getMethods() {
      return (CachedMethod[])this.methods.get();
   }

   public CachedField[] getFields() {
      return (CachedField[])this.fields.get();
   }

   public CachedConstructor[] getConstructors() {
      return (CachedConstructor[])this.constructors.get();
   }

   public CachedMethod searchMethods(String name, CachedClass[] parameterTypes) {
      CachedMethod[] methods = this.getMethods();
      CachedMethod res = null;
      CachedMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         CachedMethod m = arr$[i$];
         if (m.getName().equals(name) && ReflectionCache.arrayContentsEq(parameterTypes, m.getParameterTypes()) && (res == null || res.getReturnType().isAssignableFrom(m.getReturnType()))) {
            res = m;
         }
      }

      return res;
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public Object coerceArgument(Object argument) {
      return argument;
   }

   public int getSuperClassDistance() {
      synchronized(this.getTheClass()) {
         if (this.distance == -1) {
            int distance = 0;

            for(Class klazz = this.getTheClass(); klazz != null; klazz = klazz.getSuperclass()) {
               ++distance;
            }

            this.distance = distance;
         }

         return this.distance;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = super.hashCode();
         if (this.hashCode == 0) {
            this.hashCode = -889274690;
         }
      }

      return this.hashCode;
   }

   public boolean isPrimitive() {
      return this.isPrimitive;
   }

   public boolean isVoid() {
      return this.getTheClass() == Void.TYPE;
   }

   public boolean isInterface() {
      return this.isInterface;
   }

   public void doCast(BytecodeHelper helper) {
      helper.doCast(this.getTheClass());
   }

   public String getName() {
      return this.getTheClass().getName();
   }

   public String getTypeDescription() {
      return BytecodeHelper.getTypeDescription(this.getTheClass());
   }

   public final Class getTheClass() {
      return this.cachedClass;
   }

   public MetaMethod[] getNewMetaMethods() {
      List<MetaMethod> arr = new ArrayList();
      arr.addAll(Arrays.asList(this.classInfo.newMetaMethods));
      MetaClass metaClass = this.classInfo.getStrongMetaClass();
      if (metaClass != null && metaClass instanceof ExpandoMetaClass) {
         arr.addAll(((ExpandoMetaClass)metaClass).getExpandoMethods());
      }

      if (this.isInterface) {
         MetaClass mc = ReflectionCache.OBJECT_CLASS.classInfo.getStrongMetaClass();
         this.addSubclassExpandos(arr, mc);
      } else {
         for(CachedClass cls = this; cls != null; cls = cls.getCachedSuperClass()) {
            MetaClass mc = cls.classInfo.getStrongMetaClass();
            this.addSubclassExpandos(arr, mc);
         }
      }

      Iterator i$ = this.getInterfaces().iterator();

      while(i$.hasNext()) {
         CachedClass inf = (CachedClass)i$.next();
         MetaClass mc = inf.classInfo.getStrongMetaClass();
         this.addSubclassExpandos(arr, mc);
      }

      return (MetaMethod[])arr.toArray(new MetaMethod[arr.size()]);
   }

   private void addSubclassExpandos(List<MetaMethod> arr, MetaClass mc) {
      if (mc != null && mc instanceof ExpandoMetaClass) {
         ExpandoMetaClass emc = (ExpandoMetaClass)mc;
         Iterator i$ = emc.getExpandoSubclassMethods().iterator();

         while(true) {
            while(i$.hasNext()) {
               Object mm = i$.next();
               if (mm instanceof MetaMethod) {
                  MetaMethod method = (MetaMethod)mm;
                  if (method.getDeclaringClass() == this) {
                     arr.add(method);
                  }
               } else {
                  FastArray farr = (FastArray)mm;

                  for(int i = 0; i != farr.size; ++i) {
                     MetaMethod method = (MetaMethod)farr.get(i);
                     if (method.getDeclaringClass() == this) {
                        arr.add(method);
                     }
                  }
               }
            }

            return;
         }
      }
   }

   public void setNewMopMethods(List<MetaMethod> arr) {
      MetaClass metaClass = this.classInfo.getStrongMetaClass();
      if (metaClass != null) {
         if (metaClass.getClass() == MetaClassImpl.class) {
            this.classInfo.setStrongMetaClass((MetaClass)null);
            this.updateSetNewMopMethods(arr);
            this.classInfo.setStrongMetaClass(new MetaClassImpl(metaClass.getTheClass()));
         } else if (metaClass.getClass() == ExpandoMetaClass.class) {
            this.classInfo.setStrongMetaClass((MetaClass)null);
            this.updateSetNewMopMethods(arr);
            ExpandoMetaClass newEmc = new ExpandoMetaClass(metaClass.getTheClass());
            newEmc.initialize();
            this.classInfo.setStrongMetaClass(newEmc);
         } else {
            throw new GroovyRuntimeException("Can't add methods to class " + this.getTheClass().getName() + ". Strong custom meta class already set.");
         }
      } else {
         this.classInfo.setWeakMetaClass((MetaClass)null);
         this.updateSetNewMopMethods(arr);
      }
   }

   private void updateSetNewMopMethods(List<MetaMethod> arr) {
      if (arr != null) {
         MetaMethod[] metaMethods = (MetaMethod[])arr.toArray(new MetaMethod[arr.size()]);
         this.classInfo.dgmMetaMethods = metaMethods;
         this.classInfo.newMetaMethods = metaMethods;
      } else {
         this.classInfo.newMetaMethods = this.classInfo.dgmMetaMethods;
      }

   }

   public void addNewMopMethods(List<MetaMethod> arr) {
      MetaClass metaClass = this.classInfo.getStrongMetaClass();
      if (metaClass == null) {
         this.classInfo.setWeakMetaClass((MetaClass)null);
         this.updateAddNewMopMethods(arr);
      } else if (metaClass.getClass() == MetaClassImpl.class) {
         this.classInfo.setStrongMetaClass((MetaClass)null);
         this.updateAddNewMopMethods(arr);
         this.classInfo.setStrongMetaClass(new MetaClassImpl(metaClass.getTheClass()));
      } else if (metaClass.getClass() != ExpandoMetaClass.class) {
         throw new GroovyRuntimeException("Can't add methods to class " + this.getTheClass().getName() + ". Strong custom meta class already set.");
      } else {
         ExpandoMetaClass emc = (ExpandoMetaClass)metaClass;
         this.classInfo.setStrongMetaClass((MetaClass)null);
         this.updateAddNewMopMethods(arr);
         ExpandoMetaClass newEmc = new ExpandoMetaClass(metaClass.getTheClass());
         Iterator i$ = emc.getExpandoMethods().iterator();

         while(i$.hasNext()) {
            MetaMethod mm = (MetaMethod)i$.next();
            newEmc.registerInstanceMethod(mm);
         }

         newEmc.initialize();
         this.classInfo.setStrongMetaClass(newEmc);
      }
   }

   private void updateAddNewMopMethods(List<MetaMethod> arr) {
      List<MetaMethod> res = new ArrayList();
      res.addAll(Arrays.asList(this.classInfo.newMetaMethods));
      res.addAll(arr);
      this.classInfo.newMetaMethods = (MetaMethod[])res.toArray(new MetaMethod[res.size()]);
   }

   public boolean isAssignableFrom(Class argument) {
      return argument == null || ReflectionCache.isAssignableFrom(this.getTheClass(), argument);
   }

   public boolean isDirectlyAssignable(Object argument) {
      return ReflectionCache.isAssignableFrom(this.getTheClass(), argument.getClass());
   }

   public CallSiteClassLoader getCallSiteLoader() {
      return (CallSiteClassLoader)this.callSiteClassLoader.get();
   }

   public Collection<ClassInfo> getHierarchy() {
      return (Collection)this.hierarchy.get();
   }

   public String toString() {
      return this.cachedClass.toString();
   }

   public CachedClass getCachedClass() {
      return this;
   }

   public static class CachedMethodComparatorWithString implements Comparator {
      public static final Comparator INSTANCE = new CachedClass.CachedMethodComparatorWithString();

      public int compare(Object o1, Object o2) {
         return o1 instanceof CachedMethod ? ((CachedMethod)o1).getName().compareTo((String)o2) : ((String)o1).compareTo(((CachedMethod)o2).getName());
      }
   }

   public static class CachedMethodComparatorByName implements Comparator {
      public static final Comparator INSTANCE = new CachedClass.CachedMethodComparatorByName();

      public int compare(Object o1, Object o2) {
         return ((CachedMethod)o1).getName().compareTo(((CachedMethod)o2).getName());
      }
   }
}
