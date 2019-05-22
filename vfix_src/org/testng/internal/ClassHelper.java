package org.testng.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.testng.IClass;
import org.testng.IMethodSelector;
import org.testng.IObjectFactory;
import org.testng.IObjectFactory2;
import org.testng.ITestObjectFactory;
import org.testng.TestNGException;
import org.testng.TestRunner;
import org.testng.annotations.IAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IParametersAnnotation;
import org.testng.collections.Sets;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.junit.IJUnitTestRunner;
import org.testng.xml.XmlTest;

public final class ClassHelper {
   private static final String JUNIT_TESTRUNNER = "org.testng.junit.JUnitTestRunner";
   private static final String JUNIT_4_TESTRUNNER = "org.testng.junit.JUnit4TestRunner";
   private static final List<ClassLoader> m_classLoaders = new Vector();
   private static int m_lastGoodRootIndex = -1;

   public static void addClassLoader(ClassLoader loader) {
      m_classLoaders.add(loader);
   }

   private ClassHelper() {
   }

   public static <T> T newInstance(Class<T> clazz) {
      try {
         T instance = clazz.newInstance();
         return instance;
      } catch (IllegalAccessException var2) {
         throw new TestNGException("Class " + clazz.getName() + " does not have a no-args constructor", var2);
      } catch (InstantiationException var3) {
         throw new TestNGException("Cannot instantiate class " + clazz.getName(), var3);
      } catch (ExceptionInInitializerError var4) {
         throw new TestNGException("An exception occurred in static initialization of class " + clazz.getName(), var4);
      } catch (SecurityException var5) {
         throw new TestNGException(var5);
      }
   }

   public static <T> T newInstance(Constructor<T> constructor, Object... parameters) {
      try {
         return constructor.newInstance(parameters);
      } catch (IllegalAccessException | InvocationTargetException | InstantiationException var3) {
         throw new TestNGException("Cannot instantiate class " + constructor.getDeclaringClass().getName(), var3);
      }
   }

   public static Class<?> forName(String className) {
      Vector<ClassLoader> allClassLoaders = new Vector();
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      if (contextClassLoader != null) {
         allClassLoaders.add(contextClassLoader);
      }

      if (m_classLoaders != null) {
         allClassLoaders.addAll(m_classLoaders);
      }

      Iterator i$ = allClassLoaders.iterator();

      while(true) {
         ClassLoader classLoader;
         do {
            if (!i$.hasNext()) {
               try {
                  return Class.forName(className);
               } catch (ClassNotFoundException var6) {
                  logClassNotFoundError(className, var6);
                  return null;
               }
            }

            classLoader = (ClassLoader)i$.next();
         } while(null == classLoader);

         try {
            return classLoader.loadClass(className);
         } catch (ClassNotFoundException var7) {
            if (null == m_classLoaders || m_classLoaders.size() == 0) {
               logClassNotFoundError(className, var7);
            }
         }
      }
   }

   private static void logClassNotFoundError(String className, Exception ex) {
      Utils.log("ClassHelper", 2, "Could not instantiate " + className + " : Class doesn't exist (" + ex.getMessage() + ")");
   }

   public static ConstructorOrMethod findDeclaredFactoryMethod(Class<?> cls, IAnnotationFinder finder) {
      ConstructorOrMethod result = null;
      Method[] arr$ = cls.getMethods();
      int len$ = arr$.length;

      int i$;
      for(i$ = 0; i$ < len$; ++i$) {
         Method method = arr$[i$];
         IFactoryAnnotation f = (IFactoryAnnotation)finder.findAnnotation(method, IFactoryAnnotation.class);
         if (null != f) {
            result = new ConstructorOrMethod(method);
            result.setEnabled(f.getEnabled());
            break;
         }
      }

      if (result == null) {
         Constructor[] arr$ = cls.getDeclaredConstructors();
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            Constructor constructor = arr$[i$];
            IAnnotation f = finder.findAnnotation(constructor, IFactoryAnnotation.class);
            if (f != null) {
               result = new ConstructorOrMethod(constructor);
            }
         }
      }

      return result;
   }

   public static Set<Method> getAvailableMethods(Class<?> clazz) {
      Set<Method> methods = Sets.newHashSet();
      methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

      for(Class parent = clazz.getSuperclass(); Object.class != parent; parent = parent.getSuperclass()) {
         methods.addAll(extractMethods(clazz, parent, methods));
      }

      return methods;
   }

   public static IJUnitTestRunner createTestRunner(TestRunner runner) {
      try {
         Class.forName("org.junit.Test");
         IJUnitTestRunner tr = (IJUnitTestRunner)forName("org.testng.junit.JUnit4TestRunner").newInstance();
         tr.setTestResultNotifier(runner);
         return tr;
      } catch (Throwable var4) {
         Utils.log("ClassHelper", 2, "JUnit 4 was not found on the classpath");

         try {
            Class.forName("junit.framework.Test");
            IJUnitTestRunner tr = (IJUnitTestRunner)forName("org.testng.junit.JUnitTestRunner").newInstance();
            tr.setTestResultNotifier(runner);
            return tr;
         } catch (Exception var3) {
            Utils.log("ClassHelper", 2, "JUnit 3 was not found on the classpath");
            throw new TestNGException("Cannot create JUnit runner", var3);
         }
      }
   }

   private static Set<Method> extractMethods(Class<?> childClass, Class<?> clazz, Set<Method> collected) {
      Set<Method> methods = Sets.newHashSet();
      Method[] declaredMethods = clazz.getDeclaredMethods();
      Package childPackage = childClass.getPackage();
      Package classPackage = clazz.getPackage();
      boolean isSamePackage = false;
      if (null == childPackage && null == classPackage) {
         isSamePackage = true;
      }

      if (null != childPackage && null != classPackage) {
         isSamePackage = childPackage.getName().equals(classPackage.getName());
      }

      Method[] arr$ = declaredMethods;
      int len$ = declaredMethods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method method = arr$[i$];
         int methodModifiers = method.getModifiers();
         if ((Modifier.isPublic(methodModifiers) || Modifier.isProtected(methodModifiers) || isSamePackage && !Modifier.isPrivate(methodModifiers)) && !isOverridden(method, collected) && !Modifier.isAbstract(methodModifiers)) {
            methods.add(method);
         }
      }

      return methods;
   }

   private static boolean isOverridden(Method method, Set<Method> collectedMethods) {
      Class<?> methodClass = method.getDeclaringClass();
      Class<?>[] methodParams = method.getParameterTypes();
      Iterator i$ = collectedMethods.iterator();

      boolean sameParameters;
      do {
         Method m;
         Class[] paramTypes;
         do {
            do {
               do {
                  if (!i$.hasNext()) {
                     return false;
                  }

                  m = (Method)i$.next();
                  paramTypes = m.getParameterTypes();
               } while(!method.getName().equals(m.getName()));
            } while(!methodClass.isAssignableFrom(m.getDeclaringClass()));
         } while(methodParams.length != paramTypes.length);

         sameParameters = true;

         for(int i = 0; i < methodParams.length; ++i) {
            if (!methodParams[i].equals(paramTypes[i])) {
               sameParameters = false;
               break;
            }
         }
      } while(!sameParameters);

      return true;
   }

   public static IMethodSelector createSelector(org.testng.xml.XmlMethodSelector selector) {
      try {
         Class<?> cls = Class.forName(selector.getClassName());
         return (IMethodSelector)cls.newInstance();
      } catch (Exception var2) {
         throw new TestNGException("Couldn't find method selector : " + selector.getClassName(), var2);
      }
   }

   public static Object createInstance(Class<?> declaringClass, Map<Class, IClass> classes, XmlTest xmlTest, IAnnotationFinder finder, ITestObjectFactory objectFactory) {
      if (objectFactory instanceof IObjectFactory) {
         return createInstance1(declaringClass, classes, xmlTest, finder, (IObjectFactory)objectFactory);
      } else if (objectFactory instanceof IObjectFactory2) {
         return createInstance2(declaringClass, (IObjectFactory2)objectFactory);
      } else {
         throw new AssertionError("Unknown object factory type:" + objectFactory);
      }
   }

   private static Object createInstance2(Class<?> declaringClass, IObjectFactory2 objectFactory) {
      return objectFactory.newInstance(declaringClass);
   }

   public static Object createInstance1(Class<?> declaringClass, Map<Class, IClass> classes, XmlTest xmlTest, IAnnotationFinder finder, IObjectFactory objectFactory) {
      Object result = null;

      try {
         Constructor<?> constructor = findAnnotatedConstructor(finder, declaringClass);
         if (null != constructor) {
            IParametersAnnotation annotation = (IParametersAnnotation)finder.findAnnotation(constructor, IParametersAnnotation.class);
            String[] parameterNames = annotation.getValue();
            Object[] parameters = Parameters.createInstantiationParameters(constructor, "@Parameters", finder, parameterNames, xmlTest.getAllParameters(), xmlTest.getSuite());
            result = objectFactory.newInstance(constructor, parameters);
         } else {
            Class<?>[] parameterTypes = new Class[0];
            Object[] parameters = new Object[0];
            Class<?> ec = getEnclosingClass(declaringClass);
            boolean isStatic = 0 != (declaringClass.getModifiers() & 8);
            if (null != ec && !isStatic) {
               parameterTypes = new Class[]{ec};
               IClass enclosingIClass = (IClass)classes.get(ec);
               Object[] enclosingInstances;
               Object o;
               if (null != enclosingIClass) {
                  enclosingInstances = enclosingIClass.getInstances(false);
                  if (null == enclosingInstances || enclosingInstances.length == 0) {
                     o = objectFactory.newInstance(ec.getConstructor(parameterTypes));
                     enclosingIClass.addInstance(o);
                     enclosingInstances = new Object[]{o};
                  }
               } else {
                  enclosingInstances = new Object[]{ec.newInstance()};
               }

               o = enclosingInstances[0];
               parameters = new Object[]{o};
            }

            Constructor ct;
            try {
               ct = declaringClass.getDeclaredConstructor(parameterTypes);
            } catch (NoSuchMethodException var14) {
               ct = declaringClass.getDeclaredConstructor(String.class);
               parameters = new Object[]{"Default test name"};
            }

            result = objectFactory.newInstance(ct, parameters);
         }
      } catch (TestNGException var15) {
         throw var15;
      } catch (NoSuchMethodException var16) {
      } catch (Throwable var17) {
         throw new TestNGException("An error occurred while instantiating class " + declaringClass.getName() + ": " + var17.getMessage(), var17);
      }

      if (result == null && !Modifier.isPublic(declaringClass.getModifiers())) {
         throw new TestNGException("An error occurred while instantiating class " + declaringClass.getName() + ". Check to make sure it can be accessed/instantiated.");
      } else {
         return result;
      }
   }

   private static Class<?> getEnclosingClass(Class<?> declaringClass) {
      Class<?> result = null;
      String className = declaringClass.getName();
      int index = className.indexOf("$");
      if (index != -1) {
         String ecn = className.substring(0, index);

         try {
            result = Class.forName(ecn);
         } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
         }
      }

      return result;
   }

   private static Constructor<?> findAnnotatedConstructor(IAnnotationFinder finder, Class<?> declaringClass) {
      Constructor<?>[] constructors = declaringClass.getDeclaredConstructors();
      Constructor[] arr$ = constructors;
      int len$ = constructors.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Constructor<?> result = arr$[i$];
         IParametersAnnotation annotation = (IParametersAnnotation)finder.findAnnotation(result, IParametersAnnotation.class);
         if (null != annotation) {
            String[] parameters = annotation.getValue();
            Class<?>[] parameterTypes = result.getParameterTypes();
            if (parameters.length != parameterTypes.length) {
               throw new TestNGException("Parameter count mismatch:  " + result + "\naccepts " + parameterTypes.length + " parameters but the @Test annotation declares " + parameters.length);
            }

            return result;
         }
      }

      return null;
   }

   public static <T> T tryOtherConstructor(Class<T> declaringClass) {
      try {
         if (declaringClass.getModifiers() == 0) {
            return null;
         } else {
            Constructor<T> ctor = declaringClass.getConstructor(String.class);
            T result = ctor.newInstance("Default test name");
            return result;
         }
      } catch (Exception var5) {
         String message = var5.getMessage();
         if (message == null && var5.getCause() != null) {
            message = var5.getCause().getMessage();
         }

         String error = "Could not create an instance of class " + declaringClass + (message != null ? ": " + message : "") + ".\nPlease make sure it has a constructor that accepts either a String or no parameter.";
         throw new TestNGException(error);
      }
   }

   public static Class<?> fileToClass(String file) {
      Class<?> result = null;
      if (!file.endsWith(".class") && !file.endsWith(".java")) {
         if (file.startsWith("class ")) {
            file = file.substring("class ".length());
         }

         result = forName(file);
         if (null == result) {
            throw new TestNGException("Cannot load class from file: " + file);
         } else {
            return result;
         }
      } else {
         int classIndex = file.lastIndexOf(".class");
         if (-1 == classIndex) {
            classIndex = file.lastIndexOf(".java");
         }

         String shortFileName = file.substring(0, classIndex);
         String[] segments = shortFileName.split("[/\\\\]", -1);
         String className;
         int i;
         if (-1 != m_lastGoodRootIndex) {
            className = segments[m_lastGoodRootIndex];

            for(i = m_lastGoodRootIndex + 1; i < segments.length; ++i) {
               className = className + "." + segments[i];
            }

            result = forName(className);
            if (null != result) {
               return result;
            }
         }

         className = null;

         for(i = segments.length - 1; i >= 0; --i) {
            if (null == className) {
               className = segments[i];
            } else {
               className = segments[i] + "." + className;
            }

            result = forName(className);
            if (null != result) {
               m_lastGoodRootIndex = i;
               break;
            }
         }

         if (null == result) {
            throw new TestNGException("Cannot load class from file: " + file);
         } else {
            return result;
         }
      }
   }
}
