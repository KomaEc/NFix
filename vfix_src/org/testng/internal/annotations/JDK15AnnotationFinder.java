package org.testng.internal.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.IAnnotationTransformer2;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Configuration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Factory;
import org.testng.annotations.IAnnotation;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IExpectedExceptionsAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.IObjectFactoryAnnotation;
import org.testng.annotations.IParametersAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Listeners;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.TestInstance;
import org.testng.internal.collections.Pair;

public class JDK15AnnotationFinder implements IAnnotationFinder {
   private JDK15TagFactory m_tagFactory = new JDK15TagFactory();
   private Map<Class<? extends IAnnotation>, Class<? extends Annotation>> m_annotationMap = new ConcurrentHashMap();
   private Map<Pair<Annotation, ?>, IAnnotation> m_annotations = new ConcurrentHashMap();
   private org.testng.IAnnotationTransformer m_transformer = null;

   public JDK15AnnotationFinder(org.testng.IAnnotationTransformer transformer) {
      this.m_transformer = transformer;
      this.m_annotationMap.put(IConfigurationAnnotation.class, Configuration.class);
      this.m_annotationMap.put(IDataProviderAnnotation.class, DataProvider.class);
      this.m_annotationMap.put(IExpectedExceptionsAnnotation.class, ExpectedExceptions.class);
      this.m_annotationMap.put(IFactoryAnnotation.class, Factory.class);
      this.m_annotationMap.put(IObjectFactoryAnnotation.class, ObjectFactory.class);
      this.m_annotationMap.put(IParametersAnnotation.class, Parameters.class);
      this.m_annotationMap.put(ITestAnnotation.class, Test.class);
      this.m_annotationMap.put(IBeforeSuite.class, BeforeSuite.class);
      this.m_annotationMap.put(IAfterSuite.class, AfterSuite.class);
      this.m_annotationMap.put(IBeforeTest.class, BeforeTest.class);
      this.m_annotationMap.put(IAfterTest.class, AfterTest.class);
      this.m_annotationMap.put(IBeforeClass.class, BeforeClass.class);
      this.m_annotationMap.put(IAfterClass.class, AfterClass.class);
      this.m_annotationMap.put(IBeforeGroups.class, BeforeGroups.class);
      this.m_annotationMap.put(IAfterGroups.class, AfterGroups.class);
      this.m_annotationMap.put(IBeforeMethod.class, BeforeMethod.class);
      this.m_annotationMap.put(IAfterMethod.class, AfterMethod.class);
      this.m_annotationMap.put(IListeners.class, Listeners.class);
   }

   private <A extends Annotation> A findAnnotationInSuperClasses(Class<?> cls, Class<A> a) {
      if (a.equals(Listeners.class)) {
         return cls.getAnnotation(a);
      } else {
         while(cls != null) {
            A result = cls.getAnnotation(a);
            if (result != null) {
               return result;
            }

            cls = cls.getSuperclass();
         }

         return null;
      }
   }

   public <A extends IAnnotation> A findAnnotation(Method m, Class<A> annotationClass) {
      Class<? extends Annotation> a = (Class)this.m_annotationMap.get(annotationClass);
      if (a == null) {
         throw new IllegalArgumentException("Java @Annotation class for '" + annotationClass + "' not found.");
      } else {
         Annotation annotation = m.getAnnotation(a);
         return this.findAnnotation(m.getDeclaringClass(), annotation, annotationClass, (Class)null, (Constructor)null, m, new Pair(annotation, m));
      }
   }

   public <A extends IAnnotation> A findAnnotation(ITestNGMethod tm, Class<A> annotationClass) {
      Class<? extends Annotation> a = (Class)this.m_annotationMap.get(annotationClass);
      if (a == null) {
         throw new IllegalArgumentException("Java @Annotation class for '" + annotationClass + "' not found.");
      } else {
         Method m = tm.getMethod();
         Class testClass;
         if (tm.getInstance() == null) {
            testClass = m.getDeclaringClass();
         } else {
            testClass = tm.getInstance().getClass();
         }

         Annotation annotation = m.getAnnotation(a);
         if (annotation == null) {
            annotation = testClass.getAnnotation(a);
         }

         return this.findAnnotation(testClass, annotation, annotationClass, (Class)null, (Constructor)null, m, new Pair(annotation, m));
      }
   }

   private void transform(IAnnotation a, Class<?> testClass, Constructor<?> testConstructor, Method testMethod) {
      if (a instanceof ITestAnnotation) {
         this.m_transformer.transform((ITestAnnotation)a, testClass, testConstructor, testMethod);
      } else if (this.m_transformer instanceof IAnnotationTransformer2) {
         IAnnotationTransformer2 transformer2 = (IAnnotationTransformer2)this.m_transformer;
         if (a instanceof IConfigurationAnnotation) {
            IConfigurationAnnotation configuration = (IConfigurationAnnotation)a;
            transformer2.transform(configuration, testClass, testConstructor, testMethod);
         } else if (a instanceof IDataProviderAnnotation) {
            transformer2.transform((IDataProviderAnnotation)a, testMethod);
         } else if (a instanceof IFactoryAnnotation) {
            transformer2.transform((IFactoryAnnotation)a, testMethod);
         }
      }

   }

   public <A extends IAnnotation> A findAnnotation(Class<?> cls, Class<A> annotationClass) {
      Class<? extends Annotation> a = (Class)this.m_annotationMap.get(annotationClass);
      if (a == null) {
         throw new IllegalArgumentException("Java @Annotation class for '" + annotationClass + "' not found.");
      } else {
         Annotation annotation = this.findAnnotationInSuperClasses(cls, a);
         return this.findAnnotation(cls, annotation, annotationClass, cls, (Constructor)null, (Method)null, new Pair(annotation, annotationClass));
      }
   }

   public <A extends IAnnotation> A findAnnotation(Constructor<?> cons, Class<A> annotationClass) {
      Class<? extends Annotation> a = (Class)this.m_annotationMap.get(annotationClass);
      if (a == null) {
         throw new IllegalArgumentException("Java @Annotation class for '" + annotationClass + "' not found.");
      } else {
         Annotation annotation = cons.getAnnotation(a);
         return this.findAnnotation(cons.getDeclaringClass(), annotation, annotationClass, (Class)null, cons, (Method)null, new Pair(annotation, cons));
      }
   }

   private <A extends IAnnotation> A findAnnotation(Class cls, Annotation a, Class<A> annotationClass, Class<?> testClass, Constructor<?> testConstructor, Method testMethod, Pair<Annotation, ?> p) {
      if (a == null) {
         return null;
      } else {
         IAnnotation result = (IAnnotation)this.m_annotations.get(p);
         if (result == null) {
            result = this.m_tagFactory.createTag(cls, a, annotationClass, this.m_transformer);
            this.m_annotations.put(p, result);
            this.transform(result, testClass, testConstructor, testMethod);
         }

         return result;
      }
   }

   public boolean hasTestInstance(Method method, int i) {
      Annotation[][] annotations = method.getParameterAnnotations();
      if (annotations.length > 0 && annotations[i].length > 0) {
         Annotation[] pa = annotations[i];
         Annotation[] arr$ = pa;
         int len$ = pa.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Annotation a = arr$[i$];
            if (a instanceof TestInstance) {
               return true;
            }
         }
      }

      return false;
   }

   public String[] findOptionalValues(Method method) {
      return this.optionalValues(method.getParameterAnnotations());
   }

   public String[] findOptionalValues(Constructor method) {
      return this.optionalValues(method.getParameterAnnotations());
   }

   private String[] optionalValues(Annotation[][] annotations) {
      String[] result = new String[annotations.length];

      for(int i = 0; i < annotations.length; ++i) {
         Annotation[] arr$ = annotations[i];
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Annotation a = arr$[i$];
            if (a instanceof Optional) {
               result[i] = ((Optional)a).value();
               break;
            }
         }
      }

      return result;
   }
}
