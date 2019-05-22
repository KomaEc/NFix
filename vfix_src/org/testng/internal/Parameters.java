package org.testng.internal;

import com.google.inject.Injector;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.IClass;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestNGException;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IParameterizable;
import org.testng.annotations.IParametersAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.annotations.IDataProvidable;
import org.testng.util.Strings;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class Parameters {
   public static final String NULL_VALUE = "null";

   public static Object[] createInstantiationParameters(Constructor ctor, String methodAnnotation, IAnnotationFinder finder, String[] parameterNames, Map<String, String> params, XmlSuite xmlSuite) {
      return createParameters(ctor.toString(), ctor.getParameterTypes(), finder.findOptionalValues(ctor), methodAnnotation, finder, parameterNames, new Parameters.MethodParameters(params, Collections.emptyMap()), xmlSuite);
   }

   public static Object[] createConfigurationParameters(Method m, Map<String, String> params, Object[] parameterValues, @Nullable ITestNGMethod currentTestMethod, IAnnotationFinder finder, XmlSuite xmlSuite, ITestContext ctx, ITestResult testResult) {
      Method currentTestMeth = currentTestMethod != null ? currentTestMethod.getMethod() : null;
      Map<String, String> methodParams = currentTestMethod != null ? currentTestMethod.findMethodParameters(ctx.getCurrentXmlTest()) : Collections.emptyMap();
      return createParameters(m, new Parameters.MethodParameters(params, methodParams, parameterValues, currentTestMeth, ctx, testResult), finder, xmlSuite, IConfigurationAnnotation.class, "@Configuration");
   }

   public static Object getInjectedParameter(Class<?> c, Method method, ITestContext context, ITestResult testResult) {
      Object result = null;
      if (Method.class.equals(c)) {
         result = method;
      } else if (ITestContext.class.equals(c)) {
         result = context;
      } else if (XmlTest.class.equals(c)) {
         result = context.getCurrentXmlTest();
      } else if (ITestResult.class.equals(c)) {
         result = testResult;
      }

      return result;
   }

   private static Object[] createParameters(String methodName, Class[] parameterTypes, String[] optionalValues, String methodAnnotation, IAnnotationFinder finder, String[] parameterNames, Parameters.MethodParameters params, XmlSuite xmlSuite) {
      Object[] result = new Object[0];
      if (parameterTypes.length > 0) {
         List<Object> vResult = Lists.newArrayList();
         checkParameterTypes(methodName, parameterTypes, methodAnnotation, parameterNames);
         int i = 0;

         for(int j = 0; i < parameterTypes.length; ++i) {
            Object inject = getInjectedParameter(parameterTypes[i], params.currentTestMethod, params.context, params.testResult);
            if (inject != null) {
               vResult.add(inject);
            } else if (j < parameterNames.length) {
               String p = parameterNames[j];
               String value = (String)params.xmlParameters.get(p);
               if (null == value) {
                  value = System.getProperty(p);
               }

               if (null == value) {
                  if (optionalValues != null) {
                     value = optionalValues[i];
                  }

                  if (null == value) {
                     throw new TestNGException("Parameter '" + p + "' is required by " + methodAnnotation + " on method " + methodName + " but has not been marked @Optional or defined\n" + (xmlSuite.getFileName() != null ? "in " + xmlSuite.getFileName() : ""));
                  }
               }

               vResult.add(convertType(parameterTypes[i], value, p));
               ++j;
            }
         }

         result = vResult.toArray(new Object[vResult.size()]);
      }

      return result;
   }

   private static void checkParameterTypes(String methodName, Class[] parameterTypes, String methodAnnotation, String[] parameterNames) {
      int totalLength = parameterTypes.length;
      Set<Class> injectedTypes = new HashSet<Class>() {
         private static final long serialVersionUID = -5324894581793435812L;

         {
            this.add(ITestContext.class);
            this.add(ITestResult.class);
            this.add(XmlTest.class);
            this.add(Method.class);
            this.add(Object[].class);
         }
      };
      Class[] arr$ = parameterTypes;
      int len$ = parameterTypes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class parameterType = arr$[i$];
         if (injectedTypes.contains(parameterType)) {
            --totalLength;
         }
      }

      if (parameterNames.length != totalLength) {
         throw new TestNGException("Method " + methodName + " requires " + parameterTypes.length + " parameters but " + parameterNames.length + " were supplied in the " + methodAnnotation + " annotation.");
      }
   }

   public static Object convertType(Class type, String value, String paramName) {
      Object result = null;
      if ("null".equals(value.toLowerCase())) {
         if (type.isPrimitive()) {
            Utils.log("Parameters", 2, "Attempt to pass null value to primitive type parameter '" + paramName + "'");
         }

         return null;
      } else {
         if (type == String.class) {
            result = value;
         } else if (type != Integer.TYPE && type != Integer.class) {
            if (type != Boolean.TYPE && type != Boolean.class) {
               if (type != Byte.TYPE && type != Byte.class) {
                  if (type != Character.TYPE && type != Character.class) {
                     if (type != Double.TYPE && type != Double.class) {
                        if (type != Float.TYPE && type != Float.class) {
                           if (type != Long.TYPE && type != Long.class) {
                              if (type != Short.TYPE && type != Short.class) {
                                 if (type.isEnum()) {
                                    result = Enum.valueOf(type, value);
                                 } else {
                                    assert false : "Unsupported type parameter : " + type;
                                 }
                              } else {
                                 result = Short.parseShort(value);
                              }
                           } else {
                              result = Long.parseLong(value);
                           }
                        } else {
                           result = Float.parseFloat(value);
                        }
                     } else {
                        result = Double.parseDouble(value);
                     }
                  } else {
                     result = value.charAt(0);
                  }
               } else {
                  result = Byte.parseByte(value);
               }
            } else {
               result = Boolean.valueOf(value);
            }
         } else {
            result = Integer.parseInt(value);
         }

         return result;
      }
   }

   private static DataProviderHolder findDataProvider(Object instance, ITestClass clazz, ConstructorOrMethod m, IAnnotationFinder finder, ITestContext context) {
      DataProviderHolder result = null;
      IDataProvidable dp = findDataProviderInfo(clazz, m, finder);
      if (dp != null) {
         String dataProviderName = dp.getDataProvider();
         Class dataProviderClass = dp.getDataProviderClass();
         if (!Utils.isStringEmpty(dataProviderName)) {
            result = findDataProvider(instance, clazz, finder, dataProviderName, dataProviderClass, context);
            if (null == result) {
               throw new TestNGException("Method " + m + " requires a @DataProvider named : " + dataProviderName + (dataProviderClass != null ? " in class " + dataProviderClass.getName() : ""));
            }
         }
      }

      return result;
   }

   private static IDataProvidable findDataProviderInfo(ITestClass clazz, ConstructorOrMethod m, IAnnotationFinder finder) {
      Object result;
      if (m.getMethod() != null) {
         result = AnnotationHelper.findTest(finder, m.getMethod());
         if (result == null) {
            result = AnnotationHelper.findFactory(finder, m.getMethod());
         }

         if (result == null) {
            result = AnnotationHelper.findTest(finder, clazz.getRealClass());
         }
      } else {
         result = AnnotationHelper.findFactory(finder, m.getConstructor());
      }

      return (IDataProvidable)result;
   }

   private static DataProviderHolder findDataProvider(Object instance, ITestClass clazz, IAnnotationFinder finder, String name, Class dataProviderClass, ITestContext context) {
      DataProviderHolder result = null;
      Class cls = clazz.getRealClass();
      boolean shouldBeStatic = false;
      if (dataProviderClass != null) {
         cls = dataProviderClass;
         shouldBeStatic = true;
      }

      Iterator i$ = ClassHelper.getAvailableMethods(cls).iterator();

      while(i$.hasNext()) {
         Method m = (Method)i$.next();
         IDataProviderAnnotation dp = (IDataProviderAnnotation)finder.findAnnotation(m, IDataProviderAnnotation.class);
         if (null != dp && name.equals(getDataProviderName(dp, m))) {
            if (shouldBeStatic && (m.getModifiers() & 8) == 0) {
               Injector injector = context.getInjector((IClass)clazz);
               if (injector != null) {
                  instance = injector.getInstance(dataProviderClass);
               }
            }

            if (result != null) {
               throw new TestNGException("Found two providers called '" + name + "' on " + cls);
            }

            result = new DataProviderHolder(dp, m, instance);
         }
      }

      return result;
   }

   private static String getDataProviderName(IDataProviderAnnotation dp, Method m) {
      return Strings.isNullOrEmpty(dp.getName()) ? m.getName() : dp.getName();
   }

   private static Object[] createParameters(Method m, Parameters.MethodParameters params, IAnnotationFinder finder, XmlSuite xmlSuite, Class annotationClass, String atName) {
      List<Object> result = Lists.newArrayList();
      IParametersAnnotation annotation = (IParametersAnnotation)finder.findAnnotation(m, IParametersAnnotation.class);
      Class<?>[] types = m.getParameterTypes();
      Object[] extraParameters;
      if (null != annotation) {
         String[] parameterNames = annotation.getValue();
         extraParameters = createParameters(m.getName(), types, finder.findOptionalValues(m), atName, finder, parameterNames, params, xmlSuite);
      } else {
         IParameterizable a = (IParameterizable)finder.findAnnotation(m, annotationClass);
         if (null != a && a.getParameters().length > 0) {
            String[] parameterNames = a.getParameters();
            extraParameters = createParameters(m.getName(), types, finder.findOptionalValues(m), atName, finder, parameterNames, params, xmlSuite);
         } else {
            extraParameters = createParameters(m.getName(), types, finder.findOptionalValues(m), atName, finder, new String[0], params, xmlSuite);
         }
      }

      Collections.addAll(result, extraParameters);

      for(int i = 0; i < types.length; ++i) {
         if (Object[].class.equals(types[i])) {
            result.add(i, params.parameterValues);
         }
      }

      return result.toArray(new Object[result.size()]);
   }

   public static ParameterHolder handleParameters(ITestNGMethod testMethod, Map<String, String> allParameterNames, Object instance, Parameters.MethodParameters methodParams, XmlSuite xmlSuite, IAnnotationFinder annotationFinder, Object fedInstance) {
      DataProviderHolder dataProviderHolder = findDataProvider(instance, testMethod.getTestClass(), testMethod.getConstructorOrMethod(), annotationFinder, methodParams.context);
      Iterator parameters;
      ParameterHolder result;
      if (null != dataProviderHolder) {
         int parameterCount = testMethod.getConstructorOrMethod().getParameterTypes().length;

         for(int i = 0; i < parameterCount; ++i) {
            String n = "param" + i;
            allParameterNames.put(n, n);
         }

         parameters = MethodInvocationHelper.invokeDataProvider(dataProviderHolder.instance, dataProviderHolder.method, testMethod, methodParams.context, fedInstance, annotationFinder);
         Iterator<Object[]> filteredParameters = filterParameters(parameters, testMethod.getInvocationNumbers());
         result = new ParameterHolder(filteredParameters, ParameterHolder.ParameterOrigin.ORIGIN_DATA_PROVIDER, dataProviderHolder);
      } else {
         allParameterNames.putAll(methodParams.xmlParameters);
         Object[][] allParameterValuesArray = new Object[][]{createParameters(testMethod.getMethod(), methodParams, annotationFinder, xmlSuite, ITestAnnotation.class, "@Test")};
         testMethod.setParameterInvocationCount(allParameterValuesArray.length);
         parameters = MethodHelper.createArrayIterator(allParameterValuesArray);
         result = new ParameterHolder(parameters, ParameterHolder.ParameterOrigin.ORIGIN_XML, (DataProviderHolder)null);
      }

      return result;
   }

   private static Iterator<Object[]> filterParameters(Iterator<Object[]> parameters, List<Integer> list) {
      if (list.isEmpty()) {
         return parameters;
      } else {
         List<Object[]> result = Lists.newArrayList();

         for(int i = 0; parameters.hasNext(); ++i) {
            Object[] next = (Object[])parameters.next();
            if (list.contains(i)) {
               result.add(next);
            }
         }

         return new ArrayIterator((Object[][])result.toArray(new Object[list.size()][]));
      }
   }

   private static void ppp(String s) {
      System.out.println("[Parameters] " + s);
   }

   public static class MethodParameters {
      private final Map<String, String> xmlParameters;
      private final Method currentTestMethod;
      private final ITestContext context;
      private Object[] parameterValues;
      public ITestResult testResult;

      public MethodParameters(Map<String, String> params, Map<String, String> methodParams) {
         this(params, methodParams, (Object[])null, (Method)null, (ITestContext)null, (ITestResult)null);
      }

      public MethodParameters(Map<String, String> params, Map<String, String> methodParams, Method m) {
         this(params, methodParams, (Object[])null, m, (ITestContext)null, (ITestResult)null);
      }

      public MethodParameters(Map<String, String> params, Map<String, String> methodParams, Object[] pv, Method m, ITestContext ctx, ITestResult tr) {
         Map<String, String> allParams = Maps.newHashMap();
         allParams.putAll(params);
         allParams.putAll(methodParams);
         this.xmlParameters = allParams;
         this.currentTestMethod = m;
         this.context = ctx;
         this.parameterValues = pv;
         this.testResult = tr;
      }
   }
}
