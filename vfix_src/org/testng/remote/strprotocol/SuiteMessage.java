package org.testng.remote.strprotocol;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class SuiteMessage implements IStringMessage {
   protected final String m_suiteName;
   protected final int m_testMethodCount;
   protected final boolean m_startSuite;
   private List<String> m_excludedMethods = Lists.newArrayList();
   private Map<String, String> m_descriptions;

   public SuiteMessage(String suiteName, boolean startSuiteRun, int methodCount) {
      this.m_suiteName = suiteName;
      this.m_startSuite = startSuiteRun;
      this.m_testMethodCount = methodCount;
   }

   public SuiteMessage(ISuite suite, boolean startSuiteRun) {
      this.m_suiteName = suite.getName();
      this.m_testMethodCount = suite.getInvokedMethods().size();
      this.m_startSuite = startSuiteRun;
      Collection<ITestNGMethod> excludedMethods = suite.getExcludedMethods();
      if (excludedMethods != null && excludedMethods.size() > 0) {
         this.m_excludedMethods = Lists.newArrayList();
         this.m_descriptions = Maps.newHashMap();
         Iterator i$ = excludedMethods.iterator();

         while(i$.hasNext()) {
            ITestNGMethod m = (ITestNGMethod)i$.next();
            String methodName = m.getTestClass().getName() + "." + m.getMethodName();
            this.m_excludedMethods.add(methodName);
            if (m.getDescription() != null) {
               this.m_descriptions.put(methodName, m.getDescription());
            }
         }
      }

   }

   public void setExcludedMethods(List<String> methods) {
      this.m_excludedMethods = Lists.newArrayList();
      this.m_excludedMethods.addAll(methods);
   }

   public List<String> getExcludedMethods() {
      return this.m_excludedMethods;
   }

   public String getDescriptionForMethod(String methodName) {
      return (String)this.m_descriptions.get(methodName);
   }

   public boolean isMessageOnStart() {
      return this.m_startSuite;
   }

   public String getSuiteName() {
      return this.m_suiteName;
   }

   public int getTestMethodCount() {
      return this.m_testMethodCount;
   }

   public String getMessageAsString() {
      StringBuffer buf = new StringBuffer();
      buf.append(this.m_startSuite ? 11 : 12).append('\u0001').append(this.m_suiteName).append('\u0001').append(this.m_testMethodCount);
      if (this.m_excludedMethods != null && this.m_excludedMethods.size() > 0) {
         buf.append('\u0001');
         buf.append(this.m_excludedMethods.size());
         Iterator i$ = this.m_excludedMethods.iterator();

         while(i$.hasNext()) {
            String method = (String)i$.next();
            buf.append('\u0001');
            buf.append(method);
         }
      }

      return buf.toString();
   }

   public String toString() {
      return "[SuiteMessage suite:" + this.m_suiteName + (this.m_startSuite ? " starting" : " ending") + " methodCount:" + this.m_testMethodCount + "]";
   }
}
