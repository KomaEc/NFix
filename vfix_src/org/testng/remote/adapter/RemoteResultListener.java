package org.testng.remote.adapter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.SuiteRunner;
import org.testng.reporters.TestHTMLReporter;

public class RemoteResultListener {
   private final SuiteRunner m_runner;

   public RemoteResultListener(SuiteRunner runner) {
      this.m_runner = runner;
   }

   public void onResult(ISuite remoteSuiteRunner) {
      this.m_runner.setHost(remoteSuiteRunner.getHost());
      Map<String, ISuiteResult> tmpResults = remoteSuiteRunner.getResults();
      Map<String, ISuiteResult> suiteResults = this.m_runner.getResults();
      Iterator i$ = tmpResults.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ISuiteResult> entry = (Entry)i$.next();
         ISuiteResult suiteResult = (ISuiteResult)entry.getValue();
         suiteResults.put(entry.getKey(), suiteResult);
         ITestContext tc = suiteResult.getTestContext();
         TestHTMLReporter.generateLog(tc, remoteSuiteRunner.getHost(), this.m_runner.getOutputDirectory(), tc.getFailedConfigurations().getAllResults(), tc.getSkippedConfigurations().getAllResults(), tc.getPassedTests().getAllResults(), tc.getFailedTests().getAllResults(), tc.getSkippedTests().getAllResults(), tc.getFailedButWithinSuccessPercentageTests().getAllResults());
      }

   }
}
