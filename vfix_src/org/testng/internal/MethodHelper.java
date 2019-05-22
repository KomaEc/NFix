package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.testng.ITestNGMethod;
import org.testng.TestNGException;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.ITestOrConfiguration;
import org.testng.collections.Lists;
import org.testng.collections.Sets;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.collections.Pair;

public class MethodHelper {
   private static final Map<ITestNGMethod[], Graph<ITestNGMethod>> GRAPH_CACHE = new ConcurrentHashMap();
   private static final Map<Method, String> CANONICAL_NAME_CACHE = new ConcurrentHashMap();
   private static final Map<Pair<String, String>, Boolean> MATCH_CACHE = new ConcurrentHashMap();

   public static ITestNGMethod[] collectAndOrderMethods(List<ITestNGMethod> methods, boolean forTests, RunInfo runInfo, IAnnotationFinder finder, boolean unique, List<ITestNGMethod> outExcludedMethods) {
      List<ITestNGMethod> includedMethods = Lists.newArrayList();
      MethodGroupsHelper.collectMethodsByGroup((ITestNGMethod[])methods.toArray(new ITestNGMethod[methods.size()]), forTests, includedMethods, outExcludedMethods, runInfo, finder, unique);
      return (ITestNGMethod[])sortMethods(forTests, includedMethods, finder).toArray(new ITestNGMethod[0]);
   }

   protected static ITestNGMethod[] findDependedUponMethods(ITestNGMethod m, ITestNGMethod[] methods) {
      String canonicalMethodName = calculateMethodCanonicalName(m);
      List<ITestNGMethod> vResult = Lists.newArrayList();
      String regexp = null;
      String[] arr$ = m.getMethodsDependedUpon();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String fullyQualifiedRegexp = arr$[i$];
         boolean foundAtLeastAMethod = false;
         if (null != fullyQualifiedRegexp) {
            regexp = fullyQualifiedRegexp.replace("$", "\\$");
            boolean usePackage = regexp.indexOf(46) != -1;
            Pattern pattern = Pattern.compile(regexp);
            ITestNGMethod[] arr$ = methods;
            int len$ = methods.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               ITestNGMethod method = arr$[i$];
               Method thisMethod = method.getMethod();
               String thisMethodName = thisMethod.getName();
               String methodName = usePackage ? calculateMethodCanonicalName(thisMethod) : thisMethodName;
               Pair<String, String> cacheKey = Pair.create(regexp, methodName);
               Boolean match = (Boolean)MATCH_CACHE.get(cacheKey);
               if (match == null) {
                  match = pattern.matcher(methodName).matches();
                  MATCH_CACHE.put(cacheKey, match);
               }

               if (match) {
                  vResult.add(method);
                  foundAtLeastAMethod = true;
               }
            }
         }

         if (!foundAtLeastAMethod && !m.ignoreMissingDependencies() && !m.isAlwaysRun()) {
            Method maybeReferringTo = findMethodByName(m, regexp);
            if (maybeReferringTo != null) {
               throw new TestNGException(canonicalMethodName + "() is depending on method " + maybeReferringTo + ", which is not annotated with @Test or not included.");
            }

            throw new TestNGException(canonicalMethodName + "() depends on nonexistent method " + regexp);
         }
      }

      return (ITestNGMethod[])vResult.toArray(new ITestNGMethod[vResult.size()]);
   }

   private static Method findMethodByName(ITestNGMethod testngMethod, String regExp) {
      if (regExp == null) {
         return null;
      } else {
         int lastDot = regExp.lastIndexOf(46);
         String className;
         String methodName;
         if (lastDot == -1) {
            className = testngMethod.getMethod().getDeclaringClass().getCanonicalName();
            methodName = regExp;
         } else {
            methodName = regExp.substring(lastDot + 1);
            className = regExp.substring(0, lastDot);
         }

         try {
            Class<?> c = Class.forName(className);
            Method[] arr$ = c.getDeclaredMethods();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Method m = arr$[i$];
               if (methodName.equals(m.getName())) {
                  return m;
               }
            }
         } catch (Exception var10) {
            Utils.log("MethodHelper", 3, "Caught exception while searching for methods using regex");
         }

         return null;
      }
   }

   protected static boolean isEnabled(Class<?> objectClass, IAnnotationFinder finder) {
      ITestAnnotation testClassAnnotation = AnnotationHelper.findTest(finder, objectClass);
      return isEnabled(testClassAnnotation);
   }

   protected static boolean isEnabled(Method m, IAnnotationFinder finder) {
      ITestAnnotation annotation = AnnotationHelper.findTest(finder, m);
      if (null == annotation) {
         annotation = AnnotationHelper.findTest(finder, m.getDeclaringClass());
      }

      return isEnabled(annotation);
   }

   protected static boolean isEnabled(ITestOrConfiguration test) {
      return null == test || test.getEnabled();
   }

   public static List<ITestNGMethod> uniqueMethodList(Collection<List<ITestNGMethod>> methods) {
      Set<ITestNGMethod> resultSet = Sets.newHashSet();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         List<ITestNGMethod> l = (List)i$.next();
         resultSet.addAll(l);
      }

      return Lists.newArrayList((Collection)resultSet);
   }

   private static Graph<ITestNGMethod> topologicalSort(ITestNGMethod[] methods, List<ITestNGMethod> sequentialList, List<ITestNGMethod> parallelList) {
      Graph<ITestNGMethod> result = new Graph();
      if (methods.length == 0) {
         return result;
      } else {
         ITestNGMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod m = arr$[i$];
            result.addNode(m);
            List<ITestNGMethod> predecessors = Lists.newArrayList();
            String[] methodsDependedUpon = m.getMethodsDependedUpon();
            String[] groupsDependedUpon = m.getGroupsDependedUpon();
            int i$;
            if (methodsDependedUpon.length > 0) {
               ITestNGMethod[] methodsNamed = findDependedUponMethods(m, methods);
               ITestNGMethod[] arr$ = methodsNamed;
               i$ = methodsNamed.length;

               for(int i$ = 0; i$ < i$; ++i$) {
                  ITestNGMethod pred = arr$[i$];
                  predecessors.add(pred);
               }
            }

            if (groupsDependedUpon.length > 0) {
               String[] arr$ = groupsDependedUpon;
               int len$ = groupsDependedUpon.length;

               for(i$ = 0; i$ < len$; ++i$) {
                  String group = arr$[i$];
                  ITestNGMethod[] methodsThatBelongToGroup = MethodGroupsHelper.findMethodsThatBelongToGroup(m, methods, group);
                  ITestNGMethod[] arr$ = methodsThatBelongToGroup;
                  int len$ = methodsThatBelongToGroup.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     ITestNGMethod pred = arr$[i$];
                     predecessors.add(pred);
                  }
               }
            }

            Iterator i$ = predecessors.iterator();

            while(i$.hasNext()) {
               ITestNGMethod predecessor = (ITestNGMethod)i$.next();
               result.addPredecessor(m, predecessor);
            }
         }

         result.topologicalSort();
         sequentialList.addAll(result.getStrictlySortedNodes());
         parallelList.addAll(result.getIndependentNodes());
         return result;
      }
   }

   protected static String calculateMethodCanonicalName(ITestNGMethod m) {
      return calculateMethodCanonicalName(m.getMethod());
   }

   private static String calculateMethodCanonicalName(Method m) {
      String result = (String)CANONICAL_NAME_CACHE.get(m);
      if (result != null) {
         return result;
      } else {
         String packageName = m.getDeclaringClass().getName() + "." + m.getName();

         for(Class cls = m.getDeclaringClass(); cls != Object.class; cls = cls.getSuperclass()) {
            try {
               if (cls.getDeclaredMethod(m.getName(), m.getParameterTypes()) != null) {
                  packageName = cls.getName();
                  break;
               }
            } catch (Exception var5) {
            }
         }

         result = packageName + "." + m.getName();
         CANONICAL_NAME_CACHE.put(m, result);
         return result;
      }
   }

   private static List<ITestNGMethod> sortMethods(boolean forTests, List<ITestNGMethod> allMethods, IAnnotationFinder finder) {
      List<ITestNGMethod> sl = Lists.newArrayList();
      List<ITestNGMethod> pl = Lists.newArrayList();
      ITestNGMethod[] allMethodsArray = (ITestNGMethod[])allMethods.toArray(new ITestNGMethod[allMethods.size()]);
      if (!forTests && allMethodsArray.length > 0) {
         ITestNGMethod m = allMethodsArray[0];
         boolean before = m.isBeforeClassConfiguration() || m.isBeforeMethodConfiguration() || m.isBeforeSuiteConfiguration() || m.isBeforeTestConfiguration();
         MethodInheritance.fixMethodInheritance(allMethodsArray, before);
      }

      topologicalSort(allMethodsArray, sl, pl);
      List<ITestNGMethod> result = Lists.newArrayList();
      result.addAll(sl);
      result.addAll(pl);
      return result;
   }

   public static List<ITestNGMethod> getMethodsDependedUpon(ITestNGMethod method, ITestNGMethod[] methods) {
      Graph<ITestNGMethod> g = (Graph)GRAPH_CACHE.get(methods);
      List result;
      if (g == null) {
         result = Lists.newArrayList();
         List<ITestNGMethod> sequentialList = Lists.newArrayList();
         g = topologicalSort(methods, sequentialList, result);
         GRAPH_CACHE.put(methods, g);
      }

      result = g.findPredecessors(method);
      return result;
   }

   protected static Iterator<Object[]> createArrayIterator(Object[][] objects) {
      ArrayIterator result = new ArrayIterator(objects);
      return result;
   }

   protected static String calculateMethodCanonicalName(Class<?> methodClass, String methodName) {
      Set<Method> methods = ClassHelper.getAvailableMethods(methodClass);
      Method result = null;
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         Method m = (Method)i$.next();
         if (methodName.equals(m.getName())) {
            result = m;
            break;
         }
      }

      return result != null ? calculateMethodCanonicalName(result) : null;
   }

   protected static long calculateTimeOut(ITestNGMethod tm) {
      long result = tm.getTimeOut() > 0L ? tm.getTimeOut() : tm.getInvocationTimeOut();
      return result;
   }
}
