package org.testng.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.testng.IResultMap;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Objects;

public class ResultMap implements IResultMap {
   private static final long serialVersionUID = 80134376515999093L;
   private Map<ITestResult, ITestNGMethod> m_map = new ConcurrentHashMap();

   public void addResult(ITestResult result, ITestNGMethod method) {
      this.m_map.put(result, method);
   }

   public Set<ITestResult> getResults(ITestNGMethod method) {
      Set<ITestResult> result = new HashSet();
      Iterator i$ = this.m_map.keySet().iterator();

      while(i$.hasNext()) {
         ITestResult tr = (ITestResult)i$.next();
         if (((ITestNGMethod)this.m_map.get(tr)).equals(method)) {
            result.add(tr);
         }
      }

      return result;
   }

   public void removeResult(ITestNGMethod m) {
      Iterator i$ = this.m_map.entrySet().iterator();

      Entry entry;
      do {
         if (!i$.hasNext()) {
            return;
         }

         entry = (Entry)i$.next();
      } while(!((ITestNGMethod)entry.getValue()).equals(m));

      this.m_map.remove(entry.getKey());
   }

   public void removeResult(ITestResult r) {
      this.m_map.remove(r);
   }

   public Set<ITestResult> getAllResults() {
      return this.m_map.keySet();
   }

   public int size() {
      return this.m_map.size();
   }

   public Collection<ITestNGMethod> getAllMethods() {
      return this.m_map.values();
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("map", (Object)this.m_map).toString();
   }
}
