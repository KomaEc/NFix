package org.testng.reporters.jq;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.testng.ITestResult;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Maps;

public class ResultsByClass {
   public static Comparator<ITestResult> METHOD_NAME_COMPARATOR = new Comparator<ITestResult>() {
      public int compare(ITestResult arg0, ITestResult arg1) {
         return arg0.getMethod().getMethodName().compareTo(arg1.getMethod().getMethodName());
      }
   };
   private ListMultiMap<Class<?>, ITestResult> m_results = Maps.newListMultiMap();

   public void addResult(Class<?> c, ITestResult tr) {
      this.m_results.put(c, tr);
   }

   public List<ITestResult> getResults(Class<?> c) {
      List<ITestResult> result = (List)this.m_results.get(c);
      Collections.sort(result, METHOD_NAME_COMPARATOR);
      return result;
   }

   public List<Class<?>> getClasses() {
      return this.m_results.getKeys();
   }
}
