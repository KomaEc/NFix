package org.testng;

import java.util.List;
import org.testng.collections.Lists;
import org.testng.collections.Objects;
import org.testng.internal.SuiteRunnerMap;
import org.testng.internal.Utils;
import org.testng.internal.thread.graph.IWorker;
import org.testng.xml.XmlSuite;

public class SuiteRunnerWorker implements IWorker<ISuite> {
   private SuiteRunner m_suiteRunner;
   private Integer m_verbose;
   private String m_defaultSuiteName;
   private SuiteRunnerMap m_suiteRunnerMap;

   public SuiteRunnerWorker(ISuite suiteRunner, SuiteRunnerMap suiteRunnerMap, int verbose, String defaultSuiteName) {
      this.m_suiteRunnerMap = suiteRunnerMap;
      this.m_suiteRunner = (SuiteRunner)suiteRunner;
      this.m_verbose = verbose;
      this.m_defaultSuiteName = defaultSuiteName;
   }

   private void runSuite(SuiteRunnerMap suiteRunnerMap, XmlSuite xmlSuite) {
      if (this.m_verbose > 0) {
         StringBuffer allFiles = new StringBuffer();
         allFiles.append("  ").append(xmlSuite.getFileName() != null ? xmlSuite.getFileName() : this.m_defaultSuiteName).append('\n');
         Utils.log("TestNG", 0, "Running:\n" + allFiles.toString());
      }

      SuiteRunner suiteRunner = (SuiteRunner)suiteRunnerMap.get(xmlSuite);
      suiteRunner.run();
      if (xmlSuite.getVerbose() > 0) {
         SuiteResultCounts counts = new SuiteResultCounts();
         synchronized(suiteRunnerMap) {
            counts.calculateResultCounts(xmlSuite, suiteRunnerMap);
         }

         StringBuffer bufLog = (new StringBuffer("\n===============================================\n")).append(xmlSuite.getName());
         bufLog.append("\nTotal tests run: ").append(counts.m_total).append(", Failures: ").append(counts.m_failed).append(", Skips: ").append(counts.m_skipped);
         if (counts.m_confFailures > 0 || counts.m_confSkips > 0) {
            bufLog.append("\nConfiguration Failures: ").append(counts.m_confFailures).append(", Skips: ").append(counts.m_confSkips);
         }

         bufLog.append("\n===============================================\n");
         System.out.println(bufLog.toString());
      }

   }

   public void run() {
      this.runSuite(this.m_suiteRunnerMap, this.m_suiteRunner.getXmlSuite());
   }

   public int compareTo(IWorker<ISuite> arg0) {
      return 0;
   }

   public List<ISuite> getTasks() {
      List<ISuite> suiteRunnerList = Lists.newArrayList();
      suiteRunnerList.add(this.m_suiteRunner);
      return suiteRunnerList;
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("name", this.m_suiteRunner.getName()).toString();
   }

   public long getTimeOut() {
      return this.m_suiteRunner.getXmlSuite().getTimeOut(Long.MAX_VALUE);
   }

   public int getPriority() {
      return 0;
   }
}
