package org.testng.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.testng.IClass;
import org.testng.IRetryAnalyzer;
import org.testng.ITestClass;
import org.testng.ITestNGMethod;
import org.testng.annotations.ITestOrConfiguration;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Sets;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.internal.thread.IAtomicInteger;
import org.testng.internal.thread.ThreadUtil;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlTest;

public abstract class BaseTestMethod implements ITestNGMethod {
   private static final long serialVersionUID = -2666032602580652173L;
   private static final Pattern SPACE_SEPARATOR_PATTERN = Pattern.compile(" +");
   protected ITestClass m_testClass;
   protected final transient Class<?> m_methodClass;
   protected final transient ConstructorOrMethod m_method;
   private final transient String m_signature;
   protected String m_id;
   protected long m_date;
   protected final transient IAnnotationFinder m_annotationFinder;
   protected String[] m_groups;
   protected String[] m_groupsDependedUpon;
   protected String[] m_methodsDependedUpon;
   protected String[] m_beforeGroups;
   protected String[] m_afterGroups;
   private boolean m_isAlwaysRun;
   private boolean m_enabled;
   private final String m_methodName;
   private String m_missingGroup;
   private String m_description;
   protected IAtomicInteger m_currentInvocationCount;
   private int m_parameterInvocationCount;
   private IRetryAnalyzer m_retryAnalyzer;
   private boolean m_skipFailedInvocations;
   private long m_invocationTimeOut;
   private List<Integer> m_invocationNumbers;
   private final List<Integer> m_failedInvocationNumbers;
   private long m_timeOut;
   private boolean m_ignoreMissingDependencies;
   private int m_priority;
   private XmlTest m_xmlTest;
   private Object m_instance;
   public static final Comparator<?> DATE_COMPARATOR = new Comparator<Object>() {
      public int compare(Object o1, Object o2) {
         try {
            ITestNGMethod m1 = (ITestNGMethod)o1;
            ITestNGMethod m2 = (ITestNGMethod)o2;
            return (int)(m1.getDate() - m2.getDate());
         } catch (Exception var5) {
            return 0;
         }
      }
   };

   public BaseTestMethod(String methodName, Method method, IAnnotationFinder annotationFinder, Object instance) {
      this(methodName, new ConstructorOrMethod(method), annotationFinder, instance);
   }

   public BaseTestMethod(String methodName, ConstructorOrMethod com, IAnnotationFinder annotationFinder, Object instance) {
      this.m_id = "";
      this.m_date = -1L;
      this.m_groups = new String[0];
      this.m_groupsDependedUpon = new String[0];
      this.m_methodsDependedUpon = new String[0];
      this.m_beforeGroups = new String[0];
      this.m_afterGroups = new String[0];
      this.m_description = null;
      this.m_currentInvocationCount = ThreadUtil.createAtomicInteger(0);
      this.m_parameterInvocationCount = 1;
      this.m_retryAnalyzer = null;
      this.m_skipFailedInvocations = true;
      this.m_invocationTimeOut = 0L;
      this.m_invocationNumbers = Lists.newArrayList();
      this.m_failedInvocationNumbers = Collections.synchronizedList(Lists.newArrayList());
      this.m_timeOut = 0L;
      this.m_methodClass = com.getDeclaringClass();
      this.m_method = com;
      this.m_methodName = methodName;
      this.m_annotationFinder = annotationFinder;
      this.m_instance = instance;
      this.m_signature = this.computeSignature();
   }

   public boolean isAlwaysRun() {
      return this.m_isAlwaysRun;
   }

   protected void setAlwaysRun(boolean alwaysRun) {
      this.m_isAlwaysRun = alwaysRun;
   }

   public Class<?> getRealClass() {
      return this.m_methodClass;
   }

   public ITestClass getTestClass() {
      return this.m_testClass;
   }

   public void setTestClass(ITestClass tc) {
      assert null != tc;

      assert tc.getRealClass().equals(this.m_method.getDeclaringClass()) || this.m_method.getDeclaringClass().isAssignableFrom(tc.getRealClass()) : "\nMISMATCH : " + tc.getRealClass() + " " + this.m_method.getDeclaringClass();

      this.m_testClass = tc;
   }

   public int compareTo(Object o) {
      int result = -2;
      Class<?> thisClass = this.getRealClass();
      Class<?> otherClass = ((ITestNGMethod)o).getRealClass();
      if (this == o) {
         result = 0;
      } else if (thisClass.isAssignableFrom(otherClass)) {
         result = -1;
      } else if (otherClass.isAssignableFrom(thisClass)) {
         result = 1;
      } else if (this.equals(o)) {
         result = 0;
      }

      return result;
   }

   public Method getMethod() {
      return this.m_method.getMethod();
   }

   public String getMethodName() {
      return this.m_methodName;
   }

   public Object[] getInstances() {
      return new Object[]{this.getInstance()};
   }

   public Object getInstance() {
      return this.m_instance;
   }

   public long[] getInstanceHashCodes() {
      return this.m_testClass.getInstanceHashCodes();
   }

   public String[] getGroups() {
      return this.m_groups;
   }

   public String[] getGroupsDependedUpon() {
      return this.m_groupsDependedUpon;
   }

   public String[] getMethodsDependedUpon() {
      return this.m_methodsDependedUpon;
   }

   public boolean isTest() {
      return false;
   }

   public boolean isBeforeSuiteConfiguration() {
      return false;
   }

   public boolean isAfterSuiteConfiguration() {
      return false;
   }

   public boolean isBeforeTestConfiguration() {
      return false;
   }

   public boolean isAfterTestConfiguration() {
      return false;
   }

   public boolean isBeforeGroupsConfiguration() {
      return false;
   }

   public boolean isAfterGroupsConfiguration() {
      return false;
   }

   public boolean isBeforeClassConfiguration() {
      return false;
   }

   public boolean isAfterClassConfiguration() {
      return false;
   }

   public boolean isBeforeMethodConfiguration() {
      return false;
   }

   public boolean isAfterMethodConfiguration() {
      return false;
   }

   public long getTimeOut() {
      long result = this.m_timeOut != 0L ? this.m_timeOut : (this.m_xmlTest != null ? this.m_xmlTest.getTimeOut(0L) : 0L);
      return result;
   }

   public void setTimeOut(long timeOut) {
      this.m_timeOut = timeOut;
   }

   public int getInvocationCount() {
      return 1;
   }

   public void setInvocationCount(int counter) {
   }

   public int getTotalInvocationCount() {
      return 1;
   }

   public int getSuccessPercentage() {
      return 100;
   }

   public String getId() {
      return this.m_id;
   }

   public void setId(String id) {
      this.m_id = id;
   }

   public long getDate() {
      return this.m_date;
   }

   public void setDate(long date) {
      this.m_date = date;
   }

   public boolean canRunFromClass(IClass testClass) {
      return this.m_methodClass.isAssignableFrom(testClass.getRealClass());
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         BaseTestMethod other = (BaseTestMethod)obj;
         boolean isEqual = this.m_testClass == null ? other.m_testClass == null : other.m_testClass != null && this.m_testClass.getRealClass().equals(other.m_testClass.getRealClass()) && this.m_instance == other.getInstance();
         return isEqual && this.getConstructorOrMethod().equals(other.getConstructorOrMethod());
      }
   }

   public int hashCode() {
      return this.m_method.hashCode();
   }

   protected void initGroups(Class<? extends ITestOrConfiguration> annotationClass) {
      ITestOrConfiguration annotation = (ITestOrConfiguration)this.getAnnotationFinder().findAnnotation(this.getMethod(), annotationClass);
      ITestOrConfiguration classAnnotation = (ITestOrConfiguration)this.getAnnotationFinder().findAnnotation(this.getMethod().getDeclaringClass(), annotationClass);
      this.setGroups(this.getStringArray(null != annotation ? annotation.getGroups() : null, null != classAnnotation ? classAnnotation.getGroups() : null));
      annotation = (ITestOrConfiguration)this.getAnnotationFinder().findAnnotation(this.getMethod(), annotationClass);
      classAnnotation = (ITestOrConfiguration)this.getAnnotationFinder().findAnnotation(this.getMethod().getDeclaringClass(), annotationClass);
      Map<String, Set<String>> xgd = calculateXmlGroupDependencies(this.m_xmlTest);
      List<String> xmlGroupDependencies = Lists.newArrayList();
      String[] methodsDependedUpon = this.getGroups();
      int i = methodsDependedUpon.length;

      for(int i$ = 0; i$ < i; ++i$) {
         String g = methodsDependedUpon[i$];
         Set<String> gdu = (Set)xgd.get(g);
         if (gdu != null) {
            xmlGroupDependencies.addAll(gdu);
         }
      }

      this.setGroupsDependedUpon(this.getStringArray(null != annotation ? annotation.getDependsOnGroups() : null, null != classAnnotation ? classAnnotation.getDependsOnGroups() : null), xmlGroupDependencies);
      methodsDependedUpon = this.getStringArray(null != annotation ? annotation.getDependsOnMethods() : null, null != classAnnotation ? classAnnotation.getDependsOnMethods() : null);

      for(i = 0; i < methodsDependedUpon.length; ++i) {
         String m = methodsDependedUpon[i];
         if (!m.contains(".")) {
            m = MethodHelper.calculateMethodCanonicalName(this.m_methodClass, methodsDependedUpon[i]);
            methodsDependedUpon[i] = m != null ? m : methodsDependedUpon[i];
         }
      }

      this.setMethodsDependedUpon(methodsDependedUpon);
   }

   private static Map<String, Set<String>> calculateXmlGroupDependencies(XmlTest xmlTest) {
      Map<String, Set<String>> result = Maps.newHashMap();
      if (xmlTest == null) {
         return result;
      } else {
         String dependsOn;
         Set set;
         for(Iterator i$ = xmlTest.getXmlDependencyGroups().entrySet().iterator(); i$.hasNext(); set.addAll(Arrays.asList(SPACE_SEPARATOR_PATTERN.split(dependsOn)))) {
            Entry<String, String> e = (Entry)i$.next();
            String name = (String)e.getKey();
            dependsOn = (String)e.getValue();
            set = (Set)result.get(name);
            if (set == null) {
               set = Sets.newHashSet();
               result.put(name, set);
            }
         }

         return result;
      }
   }

   protected IAnnotationFinder getAnnotationFinder() {
      return this.m_annotationFinder;
   }

   protected IClass getIClass() {
      return this.m_testClass;
   }

   private String computeSignature() {
      String classLong = this.m_method.getDeclaringClass().getName();
      String cls = classLong.substring(classLong.lastIndexOf(".") + 1);
      StringBuilder result = (new StringBuilder(cls)).append(".").append(this.m_method.getName()).append("(");
      int i = 0;
      Class[] arr$ = this.m_method.getParameterTypes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> p = arr$[i$];
         if (i++ > 0) {
            result.append(", ");
         }

         result.append(p.getName());
      }

      result.append(")");
      result.append("[pri:").append(this.getPriority()).append(", instance:").append(this.m_instance).append("]");
      return result.toString();
   }

   protected String getSignature() {
      return this.m_signature;
   }

   public String toString() {
      return this.getSignature();
   }

   protected String[] getStringArray(String[] methodArray, String[] classArray) {
      Set<String> vResult = Sets.newHashSet();
      if (null != methodArray) {
         Collections.addAll(vResult, methodArray);
      }

      if (null != classArray) {
         Collections.addAll(vResult, classArray);
      }

      return (String[])vResult.toArray(new String[vResult.size()]);
   }

   protected void setGroups(String[] groups) {
      this.m_groups = groups;
   }

   protected void setGroupsDependedUpon(String[] groups, Collection<String> xmlGroupDependencies) {
      List<String> l = Lists.newArrayList();
      l.addAll(Arrays.asList(groups));
      l.addAll(xmlGroupDependencies);
      this.m_groupsDependedUpon = (String[])l.toArray(new String[l.size()]);
   }

   protected void setMethodsDependedUpon(String[] methods) {
      this.m_methodsDependedUpon = methods;
   }

   public void addMethodDependedUpon(String method) {
      String[] newMethods = new String[this.m_methodsDependedUpon.length + 1];
      newMethods[0] = method;
      System.arraycopy(this.m_methodsDependedUpon, 0, newMethods, 1, this.m_methodsDependedUpon.length);
      this.m_methodsDependedUpon = newMethods;
   }

   private static void ppp(String s) {
      System.out.println("[BaseTestMethod] " + s);
   }

   public String getMissingGroup() {
      return this.m_missingGroup;
   }

   public void setMissingGroup(String group) {
      this.m_missingGroup = group;
   }

   public int getThreadPoolSize() {
      return 0;
   }

   public void setThreadPoolSize(int threadPoolSize) {
   }

   public void setDescription(String description) {
      this.m_description = description;
   }

   public String getDescription() {
      return this.m_description;
   }

   public void setEnabled(boolean enabled) {
      this.m_enabled = enabled;
   }

   public boolean getEnabled() {
      return this.m_enabled;
   }

   public String[] getBeforeGroups() {
      return this.m_beforeGroups;
   }

   public String[] getAfterGroups() {
      return this.m_afterGroups;
   }

   public void incrementCurrentInvocationCount() {
      this.m_currentInvocationCount.incrementAndGet();
   }

   public int getCurrentInvocationCount() {
      return this.m_currentInvocationCount.get();
   }

   public void setParameterInvocationCount(int n) {
      this.m_parameterInvocationCount = n;
   }

   public int getParameterInvocationCount() {
      return this.m_parameterInvocationCount;
   }

   public abstract ITestNGMethod clone();

   public IRetryAnalyzer getRetryAnalyzer() {
      return this.m_retryAnalyzer;
   }

   public void setRetryAnalyzer(IRetryAnalyzer retryAnalyzer) {
      this.m_retryAnalyzer = retryAnalyzer;
   }

   public boolean skipFailedInvocations() {
      return this.m_skipFailedInvocations;
   }

   public void setSkipFailedInvocations(boolean s) {
      this.m_skipFailedInvocations = s;
   }

   public void setInvocationTimeOut(long timeOut) {
      this.m_invocationTimeOut = timeOut;
   }

   public long getInvocationTimeOut() {
      return this.m_invocationTimeOut;
   }

   public boolean ignoreMissingDependencies() {
      return this.m_ignoreMissingDependencies;
   }

   public void setIgnoreMissingDependencies(boolean i) {
      this.m_ignoreMissingDependencies = i;
   }

   public List<Integer> getInvocationNumbers() {
      return this.m_invocationNumbers;
   }

   public void setInvocationNumbers(List<Integer> numbers) {
      this.m_invocationNumbers = numbers;
   }

   public List<Integer> getFailedInvocationNumbers() {
      return this.m_failedInvocationNumbers;
   }

   public void addFailedInvocationNumber(int number) {
      this.m_failedInvocationNumbers.add(number);
   }

   public int getPriority() {
      return this.m_priority;
   }

   public void setPriority(int priority) {
      this.m_priority = priority;
   }

   public XmlTest getXmlTest() {
      return this.m_xmlTest;
   }

   public void setXmlTest(XmlTest xmlTest) {
      this.m_xmlTest = xmlTest;
   }

   public ConstructorOrMethod getConstructorOrMethod() {
      return this.m_method;
   }

   public Map<String, String> findMethodParameters(XmlTest test) {
      Map<String, String> result = test.getAllParameters();
      Iterator i$ = test.getXmlClasses().iterator();

      while(true) {
         while(true) {
            XmlClass xmlClass;
            do {
               if (!i$.hasNext()) {
                  return result;
               }

               xmlClass = (XmlClass)i$.next();
            } while(!xmlClass.getName().equals(this.getTestClass().getName()));

            result.putAll(xmlClass.getLocalParameters());
            Iterator i$ = xmlClass.getIncludedMethods().iterator();

            while(i$.hasNext()) {
               XmlInclude include = (XmlInclude)i$.next();
               if (include.getName().equals(this.getMethodName())) {
                  result.putAll(include.getLocalParameters());
                  break;
               }
            }
         }
      }
   }
}
