package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.ITestOrConfiguration;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.collections.Pair;

public class MethodGroupsHelper {
   private static final Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap();
   private static final Map<Pair<String, String>, Boolean> MATCH_CACHE = new ConcurrentHashMap();

   static void collectMethodsByGroup(ITestNGMethod[] methods, boolean forTests, List<ITestNGMethod> outIncludedMethods, List<ITestNGMethod> outExcludedMethods, RunInfo runInfo, IAnnotationFinder finder, boolean unique) {
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod tm = arr$[i$];
         boolean in = false;
         Method m = tm.getMethod();
         if (forTests) {
            in = includeMethod(AnnotationHelper.findTest(finder, m), runInfo, tm, forTests, unique, outIncludedMethods);
         } else {
            IConfigurationAnnotation annotation = AnnotationHelper.findConfiguration(finder, m);
            if (annotation.getAlwaysRun()) {
               if (!unique || !isMethodAlreadyPresent(outIncludedMethods, tm)) {
                  in = true;
               }
            } else {
               in = includeMethod(AnnotationHelper.findTest(finder, tm), runInfo, tm, forTests, unique, outIncludedMethods);
            }
         }

         if (in) {
            outIncludedMethods.add(tm);
         } else {
            outExcludedMethods.add(tm);
         }
      }

   }

   private static boolean includeMethod(ITestOrConfiguration annotation, RunInfo runInfo, ITestNGMethod tm, boolean forTests, boolean unique, List<ITestNGMethod> outIncludedMethods) {
      boolean result = false;
      if (MethodHelper.isEnabled(annotation) && runInfo.includeMethod(tm, forTests)) {
         if (unique) {
            if (!isMethodAlreadyPresent(outIncludedMethods, tm)) {
               result = true;
            }
         } else {
            result = true;
         }
      }

      return result;
   }

   private static boolean isMethodAlreadyPresent(List<ITestNGMethod> result, ITestNGMethod tm) {
      Iterator i$ = result.iterator();

      Class c1;
      Class c2;
      do {
         Method jm1;
         Method jm2;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            ITestNGMethod m = (ITestNGMethod)i$.next();
            jm1 = m.getMethod();
            jm2 = tm.getMethod();
         } while(!jm1.getName().equals(jm2.getName()));

         c1 = jm1.getDeclaringClass();
         c2 = jm2.getDeclaringClass();
      } while(!c1.isAssignableFrom(c2) && !c2.isAssignableFrom(c1));

      return true;
   }

   public static Map<String, List<ITestNGMethod>> findGroupsMethods(Collection<ITestClass> classes, boolean before) {
      Map<String, List<ITestNGMethod>> result = Maps.newHashMap();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         ITestClass cls = (ITestClass)i$.next();
         ITestNGMethod[] methods = before ? cls.getBeforeGroupsMethods() : cls.getAfterGroupsMethods();
         ITestNGMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod method = arr$[i$];
            String[] arr$ = before ? method.getBeforeGroups() : method.getAfterGroups();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String group = arr$[i$];
               List<ITestNGMethod> methodList = (List)result.get(group);
               if (methodList == null) {
                  methodList = Lists.newArrayList();
                  result.put(group, methodList);
               }

               if (!methodList.contains(method)) {
                  methodList.add(method);
               }
            }
         }
      }

      return result;
   }

   protected static void findGroupTransitiveClosure(XmlMethodSelector xms, List<ITestNGMethod> includedMethods, List<ITestNGMethod> allMethods, String[] includedGroups, Set<String> outGroups, Set<ITestNGMethod> outMethods) {
      Map<ITestNGMethod, ITestNGMethod> runningMethods = Maps.newHashMap();
      Iterator i$ = includedMethods.iterator();

      while(i$.hasNext()) {
         ITestNGMethod m = (ITestNGMethod)i$.next();
         runningMethods.put(m, m);
      }

      Map<String, String> runningGroups = Maps.newHashMap();
      String[] arr$ = includedGroups;
      int len$ = includedGroups.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String thisGroup = arr$[i$];
         runningGroups.put(thisGroup, thisGroup);
      }

      boolean keepGoing = true;

      for(Map newMethods = Maps.newHashMap(); keepGoing; newMethods = Maps.newHashMap()) {
         Iterator i$ = includedMethods.iterator();

         while(i$.hasNext()) {
            ITestNGMethod m = (ITestNGMethod)i$.next();
            String[] ig = m.getGroupsDependedUpon();
            String[] mdu = ig;
            int len$ = ig.length;

            int len$;
            for(len$ = 0; len$ < len$; ++len$) {
               String g = mdu[len$];
               if (!runningGroups.containsKey(g)) {
                  runningGroups.put(g, g);
                  ITestNGMethod[] im = findMethodsThatBelongToGroup(m, (ITestNGMethod[])allMethods.toArray(new ITestNGMethod[allMethods.size()]), g);
                  ITestNGMethod[] arr$ = im;
                  int len$ = im.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     ITestNGMethod thisMethod = arr$[i$];
                     if (!runningMethods.containsKey(thisMethod)) {
                        runningMethods.put(thisMethod, thisMethod);
                        newMethods.put(thisMethod, thisMethod);
                     }
                  }
               }
            }

            mdu = m.getMethodsDependedUpon();
            String[] arr$ = mdu;
            len$ = mdu.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String tm = arr$[i$];
               ITestNGMethod thisMethod = findMethodNamed(tm, allMethods);
               if (thisMethod != null && !runningMethods.containsKey(thisMethod)) {
                  runningMethods.put(thisMethod, thisMethod);
                  newMethods.put(thisMethod, thisMethod);
               }
            }
         }

         keepGoing = newMethods.size() > 0;
         includedMethods = Lists.newArrayList();
         includedMethods.addAll(newMethods.keySet());
      }

      outMethods.addAll(runningMethods.keySet());
      outGroups.addAll(runningGroups.keySet());
   }

   private static ITestNGMethod findMethodNamed(String tm, List<ITestNGMethod> allMethods) {
      Iterator i$ = allMethods.iterator();

      ITestNGMethod m;
      String methodName;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         m = (ITestNGMethod)i$.next();
         methodName = m.getMethod().getDeclaringClass().getName() + "." + m.getMethodName();
      } while(!methodName.equals(tm));

      return m;
   }

   protected static ITestNGMethod[] findMethodsThatBelongToGroup(ITestNGMethod method, ITestNGMethod[] methods, String groupRegexp) {
      ITestNGMethod[] found = findMethodsThatBelongToGroup(methods, groupRegexp);
      if (found.length == 0) {
         method.setMissingGroup(groupRegexp);
      }

      return found;
   }

   protected static ITestNGMethod[] findMethodsThatBelongToGroup(ITestNGMethod[] methods, String groupRegexp) {
      List<ITestNGMethod> vResult = Lists.newArrayList();
      Pattern pattern = getPattern(groupRegexp);
      ITestNGMethod[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod tm = arr$[i$];
         String[] groups = tm.getGroups();
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            Boolean match = isMatch(pattern, group);
            if (match) {
               vResult.add(tm);
            }
         }
      }

      return (ITestNGMethod[])vResult.toArray(new ITestNGMethod[vResult.size()]);
   }

   private static Boolean isMatch(Pattern pattern, String group) {
      Pair<String, String> cacheKey = Pair.create(pattern.pattern(), group);
      Boolean match = (Boolean)MATCH_CACHE.get(cacheKey);
      if (match == null) {
         match = pattern.matcher(group).matches();
         MATCH_CACHE.put(cacheKey, match);
      }

      return match;
   }

   private static Pattern getPattern(String groupRegexp) {
      Pattern groupPattern = (Pattern)PATTERN_CACHE.get(groupRegexp);
      if (groupPattern == null) {
         groupPattern = Pattern.compile(groupRegexp);
         PATTERN_CACHE.put(groupRegexp, groupPattern);
      }

      return groupPattern;
   }
}
