package org.testng.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.testng.IConfigurable;
import org.testng.IConfigureCallBack;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestNGException;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.collections.Pair;
import org.testng.internal.thread.IExecutor;
import org.testng.internal.thread.IFutureResult;
import org.testng.internal.thread.ThreadExecutionException;
import org.testng.internal.thread.ThreadTimeoutException;
import org.testng.internal.thread.ThreadUtil;

public class MethodInvocationHelper {
   protected static Object invokeMethod(Method thisMethod, Object instance, Object[] parameters) throws InvocationTargetException, IllegalAccessException {
      Utils.checkInstanceOrStatic(instance, thisMethod);
      if (instance == null || !thisMethod.getDeclaringClass().isAssignableFrom(instance.getClass())) {
         boolean isStatic = Modifier.isStatic(thisMethod.getModifiers());
         if (!isStatic) {
            Class clazz = instance.getClass();

            try {
               thisMethod = clazz.getMethod(thisMethod.getName(), thisMethod.getParameterTypes());
            } catch (Exception var11) {
               boolean found = false;

               while(clazz != null) {
                  try {
                     thisMethod = clazz.getDeclaredMethod(thisMethod.getName(), thisMethod.getParameterTypes());
                     found = true;
                     break;
                  } catch (Exception var10) {
                     clazz = clazz.getSuperclass();
                  }
               }

               if (!found) {
                  if (thisMethod.getDeclaringClass().getName().equals(instance.getClass().getName())) {
                     throw new RuntimeException("Can't invoke method " + thisMethod + ", probably due to classloader mismatch");
                  }

                  throw new RuntimeException("Can't invoke method " + thisMethod + " on this instance of " + instance.getClass() + " due to class mismatch");
               }
            }
         }
      }

      synchronized(thisMethod) {
         if (!Modifier.isPublic(thisMethod.getModifiers())) {
            thisMethod.setAccessible(true);
         }
      }

      return thisMethod.invoke(instance, parameters);
   }

   protected static Iterator<Object[]> invokeDataProvider(Object instance, Method dataProvider, ITestNGMethod method, ITestContext testContext, Object fedInstance, IAnnotationFinder annotationFinder) {
      ConstructorOrMethod com = method.getConstructorOrMethod();

      try {
         List<Object> lParameters = Lists.newArrayList();
         Class<?>[] parameterTypes = dataProvider.getParameterTypes();
         Collection<Pair<Integer, Class<?>>> unresolved = new ArrayList(parameterTypes.length);
         int i = 0;
         Class[] arr$ = parameterTypes;
         int len$ = parameterTypes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> cls = arr$[i$];
            boolean isTestInstance = annotationFinder.hasTestInstance(dataProvider, i++);
            if (cls.equals(Method.class)) {
               lParameters.add(com.getMethod());
            } else if (cls.equals(Constructor.class)) {
               lParameters.add(com.getConstructor());
            } else if (cls.equals(ConstructorOrMethod.class)) {
               lParameters.add(com);
            } else if (cls.equals(ITestNGMethod.class)) {
               lParameters.add(method);
            } else if (cls.equals(ITestContext.class)) {
               lParameters.add(testContext);
            } else if (isTestInstance) {
               lParameters.add(fedInstance);
            } else {
               unresolved.add(new Pair(i, cls));
            }
         }

         if (!unresolved.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Some DataProvider ").append(dataProvider).append(" parameters unresolved: ");
            Iterator i$ = unresolved.iterator();

            while(i$.hasNext()) {
               Pair<Integer, Class<?>> pair = (Pair)i$.next();
               sb.append(" at ").append(pair.first()).append(" type ").append(pair.second()).append("\n");
            }

            throw new TestNGException(sb.toString());
         } else {
            Object[] parameters = lParameters.toArray(new Object[lParameters.size()]);
            Class<?> returnType = dataProvider.getReturnType();
            Iterator result;
            if (Object[][].class.isAssignableFrom(returnType)) {
               Object[][] originalResult = (Object[][])((Object[][])invokeMethod(dataProvider, instance, parameters));
               int[] indices = ((DataProvider)dataProvider.getAnnotation(DataProvider.class)).indices();
               Object[][] oResult;
               if (indices.length > 0) {
                  oResult = new Object[indices.length][];

                  for(int j = 0; j < indices.length; ++j) {
                     oResult[j] = originalResult[indices[j]];
                  }
               } else {
                  oResult = originalResult;
               }

               method.setParameterInvocationCount(oResult.length);
               result = MethodHelper.createArrayIterator(oResult);
            } else {
               if (!Iterator.class.isAssignableFrom(returnType)) {
                  throw new TestNGException("Data Provider " + dataProvider + " must return" + " either Object[][] or Iterator<Object>[], not " + returnType);
               }

               result = (Iterator)invokeMethod(dataProvider, instance, parameters);
            }

            return result;
         }
      } catch (IllegalAccessException | InvocationTargetException var18) {
         throw new RuntimeException(var18.getCause());
      }
   }

   protected static void invokeHookable(final Object testInstance, final Object[] parameters, IHookable hookable, final Method thisMethod, ITestResult testResult) throws Throwable {
      final Throwable[] error = new Throwable[1];
      IHookCallBack callback = new IHookCallBack() {
         public void runTestMethod(ITestResult tr) {
            try {
               MethodInvocationHelper.invokeMethod(thisMethod, testInstance, parameters);
            } catch (Throwable var3) {
               error[0] = var3;
               tr.setThrowable(var3);
            }

         }

         public Object[] getParameters() {
            return parameters;
         }
      };
      hookable.run(callback, testResult);
      if (error[0] != null) {
         throw error[0];
      }
   }

   protected static void invokeWithTimeout(ITestNGMethod tm, Object instance, Object[] parameterValues, ITestResult testResult) throws InterruptedException, ThreadExecutionException {
      invokeWithTimeout(tm, instance, parameterValues, testResult, (IHookable)null);
   }

   protected static void invokeWithTimeout(ITestNGMethod tm, Object instance, Object[] parameterValues, ITestResult testResult, IHookable hookable) throws InterruptedException, ThreadExecutionException {
      if (ThreadUtil.isTestNGThread()) {
         invokeWithTimeoutWithNoExecutor(tm, instance, parameterValues, testResult, hookable);
      } else {
         invokeWithTimeoutWithNewExecutor(tm, instance, parameterValues, testResult, hookable);
      }

   }

   private static void invokeWithTimeoutWithNoExecutor(ITestNGMethod tm, Object instance, Object[] parameterValues, ITestResult testResult, IHookable hookable) {
      InvokeMethodRunnable imr = new InvokeMethodRunnable(tm, instance, parameterValues, hookable, testResult);

      try {
         imr.run();
         testResult.setStatus(1);
      } catch (Exception var7) {
         testResult.setThrowable(var7.getCause());
         testResult.setStatus(2);
      }

   }

   private static void invokeWithTimeoutWithNewExecutor(ITestNGMethod tm, Object instance, Object[] parameterValues, ITestResult testResult, IHookable hookable) throws InterruptedException, ThreadExecutionException {
      IExecutor exec = ThreadUtil.createExecutor(1, tm.getMethodName());
      InvokeMethodRunnable imr = new InvokeMethodRunnable(tm, instance, parameterValues, hookable, testResult);
      IFutureResult future = exec.submitRunnable(imr);
      exec.shutdown();
      long realTimeOut = MethodHelper.calculateTimeOut(tm);
      boolean finished = exec.awaitTermination(realTimeOut);
      if (!finished) {
         exec.stopNow();
         ThreadTimeoutException exception = new ThreadTimeoutException("Method " + tm.getClass().getName() + "." + tm.getMethodName() + "()" + " didn't finish within the time-out " + realTimeOut);
         exception.setStackTrace(exec.getStackTraces()[0]);
         testResult.setThrowable(exception);
         testResult.setStatus(2);
      } else {
         Utils.log("Invoker " + Thread.currentThread().hashCode(), 3, "Method " + tm.getMethodName() + " completed within the time-out " + tm.getTimeOut());
         future.get();
         testResult.setStatus(1);
      }

   }

   protected static void invokeConfigurable(final Object instance, final Object[] parameters, IConfigurable configurableInstance, final Method thisMethod, ITestResult testResult) throws Throwable {
      final Throwable[] error = new Throwable[1];
      IConfigureCallBack callback = new IConfigureCallBack() {
         public void runConfigurationMethod(ITestResult tr) {
            try {
               MethodInvocationHelper.invokeMethod(thisMethod, instance, parameters);
            } catch (Throwable var3) {
               error[0] = var3;
               tr.setThrowable(var3);
            }

         }

         public Object[] getParameters() {
            return parameters;
         }
      };
      configurableInstance.run(callback, testResult);
      if (error[0] != null) {
         throw error[0];
      }
   }
}
