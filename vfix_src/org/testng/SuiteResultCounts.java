package org.testng;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.testng.internal.SuiteRunnerMap;
import org.testng.xml.XmlSuite;

class SuiteResultCounts {
   int m_total = 0;
   int m_skipped = 0;
   int m_failed = 0;
   int m_confFailures = 0;
   int m_confSkips = 0;

   public void calculateResultCounts(XmlSuite xmlSuite, SuiteRunnerMap suiteRunnerMap) {
      ISuite iSuite = suiteRunnerMap.get(xmlSuite);
      if (iSuite != null) {
         Map<String, ISuiteResult> results = iSuite.getResults();
         if (results != null) {
            Collection<ISuiteResult> tempSuiteResult = results.values();

            Iterator i$;
            ITestContext ctx;
            int skipped;
            int failed;
            for(i$ = tempSuiteResult.iterator(); i$.hasNext(); this.m_total += ctx.getPassedTests().size() + failed + skipped) {
               ISuiteResult isr = (ISuiteResult)i$.next();
               ctx = isr.getTestContext();
               skipped = ctx.getSkippedTests().size();
               failed = ctx.getFailedTests().size() + ctx.getFailedButWithinSuccessPercentageTests().size();
               this.m_skipped += skipped;
               this.m_failed += failed;
               this.m_confFailures += ctx.getFailedConfigurations().size();
               this.m_confSkips += ctx.getSkippedConfigurations().size();
            }

            i$ = xmlSuite.getChildSuites().iterator();

            while(i$.hasNext()) {
               XmlSuite childSuite = (XmlSuite)i$.next();
               this.calculateResultCounts(childSuite, suiteRunnerMap);
            }
         }
      }

   }
}
