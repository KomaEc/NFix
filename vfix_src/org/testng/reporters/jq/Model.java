package org.testng.reporters.jq;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.SetMultiMap;
import org.testng.internal.Utils;

public class Model {
   private ListMultiMap<ISuite, ITestResult> m_model = Maps.newListMultiMap();
   private List<ISuite> m_suites = null;
   private Map<String, String> m_testTags = Maps.newHashMap();
   private Map<ITestResult, String> m_testResultMap = Maps.newHashMap();
   private Map<ISuite, ResultsByClass> m_failedResultsByClass = Maps.newHashMap();
   private Map<ISuite, ResultsByClass> m_skippedResultsByClass = Maps.newHashMap();
   private Map<ISuite, ResultsByClass> m_passedResultsByClass = Maps.newHashMap();
   private List<ITestResult> m_allFailedResults = Lists.newArrayList();
   private Map<String, String> m_statusBySuiteName = Maps.newHashMap();
   private SetMultiMap<String, String> m_groupsBySuiteName = Maps.newSetMultiMap();
   private SetMultiMap<String, String> m_methodsByGroup = Maps.newSetMultiMap();

   public Model(List<ISuite> suites) {
      this.m_suites = suites;
      this.init();
   }

   public List<ISuite> getSuites() {
      return this.m_suites;
   }

   private void init() {
      int testCounter = 0;
      Iterator i$ = this.m_suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         List<ITestResult> passed = Lists.newArrayList();
         List<ITestResult> failed = Lists.newArrayList();
         List<ITestResult> skipped = Lists.newArrayList();
         Iterator i$ = suite.getResults().values().iterator();

         while(i$.hasNext()) {
            ISuiteResult sr = (ISuiteResult)i$.next();
            ITestContext context = sr.getTestContext();
            this.m_testTags.put(context.getName(), "test-" + testCounter++);
            failed.addAll(context.getFailedTests().getAllResults());
            skipped.addAll(context.getSkippedTests().getAllResults());
            passed.addAll(context.getPassedTests().getAllResults());
            IResultMap[] map = new IResultMap[]{context.getFailedTests(), context.getSkippedTests(), context.getPassedTests()};
            IResultMap[] arr$ = map;
            int len$ = map.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               IResultMap m = arr$[i$];
               Iterator i$ = m.getAllResults().iterator();

               while(i$.hasNext()) {
                  ITestResult tr = (ITestResult)i$.next();
                  this.m_testResultMap.put(tr, getTestResultName(tr));
               }
            }
         }

         ResultsByClass rbc = new ResultsByClass();
         Iterator i$ = passed.iterator();

         ITestResult tr;
         while(i$.hasNext()) {
            tr = (ITestResult)i$.next();
            rbc.addResult(tr.getTestClass().getRealClass(), tr);
            this.updateGroups(suite, tr);
         }

         this.m_passedResultsByClass.put(suite, rbc);
         rbc = new ResultsByClass();
         i$ = skipped.iterator();

         while(i$.hasNext()) {
            tr = (ITestResult)i$.next();
            this.m_statusBySuiteName.put(suite.getName(), "skipped");
            rbc.addResult(tr.getTestClass().getRealClass(), tr);
            this.updateGroups(suite, tr);
         }

         this.m_skippedResultsByClass.put(suite, rbc);
         rbc = new ResultsByClass();
         i$ = failed.iterator();

         while(i$.hasNext()) {
            tr = (ITestResult)i$.next();
            this.m_statusBySuiteName.put(suite.getName(), "failed");
            rbc.addResult(tr.getTestClass().getRealClass(), tr);
            this.m_allFailedResults.add(tr);
            this.updateGroups(suite, tr);
         }

         this.m_failedResultsByClass.put(suite, rbc);
         this.m_model.putAll(suite, failed);
         this.m_model.putAll(suite, skipped);
         this.m_model.putAll(suite, passed);
      }

   }

   private void updateGroups(ISuite suite, ITestResult tr) {
      String[] groups = tr.getMethod().getGroups();
      this.m_groupsBySuiteName.putAll(suite.getName(), Arrays.asList(groups));
      String[] arr$ = groups;
      int len$ = groups.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String group = arr$[i$];
         this.m_methodsByGroup.put(group, tr.getMethod().getMethodName());
      }

   }

   public ResultsByClass getFailedResultsByClass(ISuite suite) {
      return (ResultsByClass)this.m_failedResultsByClass.get(suite);
   }

   public ResultsByClass getSkippedResultsByClass(ISuite suite) {
      return (ResultsByClass)this.m_skippedResultsByClass.get(suite);
   }

   public ResultsByClass getPassedResultsByClass(ISuite suite) {
      return (ResultsByClass)this.m_passedResultsByClass.get(suite);
   }

   public String getTag(ITestResult tr) {
      return (String)this.m_testResultMap.get(tr);
   }

   public List<ITestResult> getTestResults(ISuite suite) {
      return this.nonnullList((List)this.m_model.get(suite));
   }

   public static String getTestResultName(ITestResult tr) {
      StringBuilder result = new StringBuilder(tr.getMethod().getMethodName());
      Object[] parameters = tr.getParameters();
      if (parameters.length > 0) {
         result.append("(");
         StringBuilder p = new StringBuilder();

         for(int i = 0; i < parameters.length; ++i) {
            if (i > 0) {
               p.append(", ");
            }

            p.append(Utils.toString(parameters[i]));
         }

         if (p.length() > 100) {
            String s = p.toString().substring(0, 100);
            s = s + "...";
            result.append(s);
         } else {
            result.append(p.toString());
         }

         result.append(")");
      }

      return result.toString();
   }

   public List<ITestResult> getAllFailedResults() {
      return this.m_allFailedResults;
   }

   public static String getImage(String tagClass) {
      return tagClass + ".png";
   }

   public String getStatusForSuite(String suiteName) {
      String result = (String)this.m_statusBySuiteName.get(suiteName);
      return result != null ? result : "passed";
   }

   public <T> Set<T> nonnullSet(Set<T> l) {
      return l != null ? l : Collections.emptySet();
   }

   public <T> List<T> nonnullList(List<T> l) {
      return l != null ? l : Collections.emptyList();
   }

   public List<String> getGroups(String name) {
      List<String> result = Lists.newArrayList((Collection)this.nonnullSet((Set)this.m_groupsBySuiteName.get(name)));
      Collections.sort(result);
      return result;
   }

   public List<String> getMethodsInGroup(String groupName) {
      List<String> result = Lists.newArrayList((Collection)this.nonnullSet((Set)this.m_methodsByGroup.get(groupName)));
      Collections.sort(result);
      return result;
   }

   public List<ITestResult> getAllTestResults(ISuite suite) {
      return this.getAllTestResults(suite, true);
   }

   public List<ITestResult> getAllTestResults(ISuite suite, boolean testsOnly) {
      List<ITestResult> result = Lists.newArrayList();
      Iterator i$ = suite.getResults().values().iterator();

      while(i$.hasNext()) {
         ISuiteResult sr = (ISuiteResult)i$.next();
         result.addAll(sr.getTestContext().getPassedTests().getAllResults());
         result.addAll(sr.getTestContext().getFailedTests().getAllResults());
         result.addAll(sr.getTestContext().getSkippedTests().getAllResults());
         if (!testsOnly) {
            result.addAll(sr.getTestContext().getPassedConfigurations().getAllResults());
            result.addAll(sr.getTestContext().getFailedConfigurations().getAllResults());
            result.addAll(sr.getTestContext().getSkippedConfigurations().getAllResults());
         }
      }

      return result;
   }
}
