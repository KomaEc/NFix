package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.testng.IMethodSelector;
import org.testng.IMethodSelectorContext;
import org.testng.ITestNGMethod;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;

public class XmlMethodSelector implements IMethodSelector {
   private static final long serialVersionUID = -9030548178025605629L;
   private Map<String, String> m_includedGroups = Maps.newHashMap();
   private Map<String, String> m_excludedGroups = Maps.newHashMap();
   private List<XmlClass> m_classes = null;
   private String m_expression = null;
   private ListMultiMap<String, XmlInclude> m_includedMethods = Maps.newListMultiMap();
   private IBsh m_bsh = Dynamic.hasBsh() ? new Bsh() : new BshMock();
   private Map<String, String> m_logged = Maps.newHashMap();
   private boolean m_isInitialized = false;
   private List<ITestNGMethod> m_testMethods = null;

   public boolean includeMethod(IMethodSelectorContext context, ITestNGMethod tm, boolean isTestMethod) {
      if (!this.m_isInitialized) {
         this.m_isInitialized = true;
         this.init(context);
      }

      boolean result = false;
      if (null != this.m_expression) {
         return this.m_bsh.includeMethodFromExpression(this.m_expression, tm);
      } else {
         result = this.includeMethodFromIncludeExclude(tm, isTestMethod);
         return result;
      }
   }

   private boolean includeMethodFromIncludeExclude(ITestNGMethod tm, boolean isTestMethod) {
      boolean result = false;
      Method m = tm.getMethod();
      String[] groups = tm.getGroups();
      Map<String, String> includedGroups = this.m_includedGroups;
      Map<String, String> excludedGroups = this.m_excludedGroups;
      List<XmlInclude> includeList = (List)this.m_includedMethods.get(MethodHelper.calculateMethodCanonicalName(tm));
      if (includedGroups.size() == 0 && excludedGroups.size() == 0 && !this.hasIncludedMethods() && !this.hasExcludedMethods()) {
         result = true;
      } else if (includedGroups.size() == 0 && excludedGroups.size() == 0 && !isTestMethod) {
         result = true;
      } else if (includeList != null) {
         result = true;
      } else {
         boolean isIncludedInGroups = isIncluded(groups, this.m_includedGroups.values());
         boolean isExcludedInGroups = isExcluded(groups, this.m_excludedGroups.values());
         if (isIncludedInGroups && !isExcludedInGroups) {
            result = true;
         } else if (isExcludedInGroups) {
            result = false;
         }

         if (isTestMethod) {
            Method method = tm.getMethod();
            Class methodClass = method.getDeclaringClass();
            String fullMethodName = methodClass.getName() + "." + method.getName();
            String[] fullyQualifiedMethodName = new String[]{fullMethodName};
            Iterator i$ = this.m_classes.iterator();

            label60:
            while(true) {
               boolean isIncludedInMethods;
               boolean isExcludedInMethods;
               do {
                  XmlClass xmlClass;
                  Class cls;
                  do {
                     if (!i$.hasNext()) {
                        break label60;
                     }

                     xmlClass = (XmlClass)i$.next();
                     cls = xmlClass.getSupportClass();
                  } while(!this.assignable(methodClass, cls));

                  List<String> includedMethods = this.createQualifiedMethodNames(xmlClass, this.toStringList(xmlClass.getIncludedMethods()));
                  isIncludedInMethods = isIncluded(fullyQualifiedMethodName, includedMethods);
                  List<String> excludedMethods = this.createQualifiedMethodNames(xmlClass, xmlClass.getExcludedMethods());
                  isExcludedInMethods = isExcluded(fullyQualifiedMethodName, excludedMethods);
               } while(!result);

               result = isIncludedInMethods && !isExcludedInMethods;
            }
         }
      }

      Package pkg = m.getDeclaringClass().getPackage();
      String methodName = pkg != null ? pkg.getName() + "." + m.getName() : m.getName();
      this.logInclusion(result ? "Including" : "Excluding", "method", methodName + "()");
      return result;
   }

   private boolean assignable(Class sourceClass, Class targetClass) {
      return sourceClass.isAssignableFrom(targetClass) || targetClass.isAssignableFrom(sourceClass);
   }

   private void logInclusion(String including, String type, String name) {
      if (!this.m_logged.containsKey(name)) {
         log(4, including + " " + type + " " + name);
         this.m_logged.put(name, name);
      }

   }

   private boolean hasIncludedMethods() {
      Iterator i$ = this.m_classes.iterator();

      XmlClass xmlClass;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         xmlClass = (XmlClass)i$.next();
      } while(xmlClass.getIncludedMethods().size() <= 0);

      return true;
   }

   private boolean hasExcludedMethods() {
      Iterator i$ = this.m_classes.iterator();

      XmlClass xmlClass;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         xmlClass = (XmlClass)i$.next();
      } while(xmlClass.getExcludedMethods().size() <= 0);

      return true;
   }

   private List<String> toStringList(List<XmlInclude> methods) {
      List<String> result = Lists.newArrayList();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         XmlInclude m = (XmlInclude)i$.next();
         result.add(m.getName());
      }

      return result;
   }

   private List<String> createQualifiedMethodNames(XmlClass xmlClass, List<String> methods) {
      List<String> vResult = Lists.newArrayList();

      for(Class cls = xmlClass.getSupportClass(); null != cls; cls = cls.getSuperclass()) {
         Iterator i$ = methods.iterator();

         while(i$.hasNext()) {
            String im = (String)i$.next();
            Method[] allMethods = cls.getDeclaredMethods();
            Pattern pattern = Pattern.compile(im);
            Method[] arr$ = allMethods;
            int len$ = allMethods.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Method m = arr$[i$];
               if (pattern.matcher(m.getName()).matches()) {
                  vResult.add(this.makeMethodName(cls.getName(), m.getName()));
               }
            }
         }
      }

      return vResult;
   }

   private String makeMethodName(String className, String methodName) {
      return className + "." + methodName;
   }

   private void checkMethod(Class<?> c, String methodName) {
      Pattern p = Pattern.compile(methodName);
      Method[] arr$ = c.getMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         if (p.matcher(m.getName()).matches()) {
            return;
         }
      }

      Utils.log("Warning", 2, "The regular expression \"" + methodName + "\" didn't match any" + " method in class " + c.getName());
   }

   public void setXmlClasses(List<XmlClass> classes) {
      this.m_classes = classes;
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         XmlClass c = (XmlClass)i$.next();
         Iterator i$ = c.getIncludedMethods().iterator();

         while(i$.hasNext()) {
            XmlInclude m = (XmlInclude)i$.next();
            this.checkMethod(c.getSupportClass(), m.getName());
            String methodName = this.makeMethodName(c.getName(), m.getName());
            this.m_includedMethods.put(methodName, m);
         }
      }

   }

   public Map<String, String> getExcludedGroups() {
      return this.m_excludedGroups;
   }

   public Map<String, String> getIncludedGroups() {
      return this.m_includedGroups;
   }

   public void setExcludedGroups(Map<String, String> excludedGroups) {
      this.m_excludedGroups = excludedGroups;
   }

   public void setIncludedGroups(Map<String, String> includedGroups) {
      this.m_includedGroups = includedGroups;
   }

   private static boolean isIncluded(String[] groups, Collection<String> includedGroups) {
      return includedGroups.size() == 0 ? true : isMemberOf(groups, includedGroups);
   }

   private static boolean isExcluded(String[] groups, Collection<String> excludedGroups) {
      return isMemberOf(groups, excludedGroups);
   }

   private static boolean isMemberOf(String[] groups, Collection<String> list) {
      String[] arr$ = groups;
      int len$ = groups.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String group = arr$[i$];
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            String regexpStr = o.toString();
            boolean match = Pattern.matches(regexpStr, group);
            if (match) {
               return true;
            }
         }
      }

      return false;
   }

   private static void log(int level, String s) {
      Utils.log("XmlMethodSelector", level, s);
   }

   private static void ppp(String s) {
      System.out.println("[XmlMethodSelector] " + s);
   }

   public void setExpression(String expression) {
      this.m_expression = expression;
   }

   public void setTestMethods(List<ITestNGMethod> testMethods) {
      this.m_testMethods = testMethods;
   }

   private void init(IMethodSelectorContext context) {
      String[] groups = (String[])this.m_includedGroups.keySet().toArray(new String[this.m_includedGroups.size()]);
      Set<String> groupClosure = new HashSet();
      Set<ITestNGMethod> methodClosure = new HashSet();
      List<ITestNGMethod> includedMethods = Lists.newArrayList();
      Iterator i$ = this.m_testMethods.iterator();

      ITestNGMethod m;
      while(i$.hasNext()) {
         m = (ITestNGMethod)i$.next();
         if (this.includeMethod(context, m, true)) {
            includedMethods.add(m);
         }
      }

      MethodGroupsHelper.findGroupTransitiveClosure(this, includedMethods, this.m_testMethods, groups, groupClosure, methodClosure);
      if (this.m_includedGroups.size() > 0) {
         i$ = groupClosure.iterator();

         while(i$.hasNext()) {
            String g = (String)i$.next();
            log(4, "Including group " + (this.m_includedGroups.containsKey(g) ? ": " : "(implicitly): ") + g);
            this.m_includedGroups.put(g, g);
         }

         i$ = methodClosure.iterator();

         while(i$.hasNext()) {
            m = (ITestNGMethod)i$.next();
            String methodName = m.getMethod().getDeclaringClass().getName() + "." + m.getMethodName();
            List<XmlInclude> includeList = (List)this.m_includedMethods.get(methodName);
            XmlInclude xi = new XmlInclude(methodName);
            this.m_includedMethods.put(methodName, xi);
            this.logInclusion("Including", "method ", methodName);
         }
      }

   }
}
